package services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * CsvToFirebaseMigration - One-time migration script.
 *
 * RUN THIS ONCE to seed your Firestore database from the existing CSV files.
 *
 * HOW TO RUN:
 *   1. Set env var: export FIREBASE_SERVICE_ACCOUNT_PATH=/path/to/serviceAccountKey.json
 *   2. Make sure your CSV files are in the project root
 *   3. Run main() — it will push all data to Firestore
 *   4. Verify in Firebase Console, then delete this file (or keep for reference)
 *
 * CSV FILES EXPECTED:
 *   users.csv         -> id, username, password, role, xp_score, first_name, last_name, email, bio
 *   lessons.csv       -> id, category, title, youtube_id, date_created, correct_answer_keyword, theory_text
 *   certificates.csv  -> id, user_id, category, certificate_name, issue_date, lessons_completed
 *   saved_courses.csv -> user_id, lesson_id, save_date
 */
public class CsvToFirebaseMigration {

    private static Firestore db;

    private static final String USER_FILE          = "users.csv";
    private static final String LESSON_FILE        = "lessons.csv";
    private static final String CERTIFICATE_FILE   = "certificates.csv";
    private static final String SAVED_COURSES_FILE = "saved_courses.csv";


    public static void main(String[] args) throws Exception {
        System.out.println("=== CSV -> Firestore Migration ===\n");

        initFirebase();

        System.out.println("Migrating users...");
        int users = migrateUsers();
        System.out.println("  -> Migrated " + users + " users.\n");

        System.out.println("Migrating lessons...");
        int lessons = migrateLessons();
        System.out.println("  -> Migrated " + lessons + " lessons.\n");

        System.out.println("Migrating certificates...");
        int certs = migrateCertificates();
        System.out.println("  -> Migrated " + certs + " certificates.\n");

        System.out.println("Migrating saved courses...");
        int saved = migrateSavedCourses();
        System.out.println("  -> Migrated " + saved + " saved courses.\n");

        System.out.println("=== Migration complete! ===");
        System.out.println("Verify data at https://console.firebase.google.com");
    }

    // ==================== FIREBASE INIT ====================

    private static void initFirebase() throws Exception {
        String path = System.getenv("FIREBASE_SERVICE_ACCOUNT_PATH");
        if (path == null || path.isEmpty()) {
            throw new IllegalStateException(
                "Set FIREBASE_SERVICE_ACCOUNT_PATH env variable to your serviceAccountKey.json path."
            );
        }

        InputStream serviceAccount = new FileInputStream(path);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        db = FirestoreClient.getFirestore();
        System.out.println("Firebase connected.\n");
    }

    // ==================== USERS ====================

    private static int migrateUsers() throws Exception {
        int count = 0;
        File file = new File(USER_FILE);
        if (!file.exists()) {
            System.err.println("  WARNING: " + USER_FILE + " not found, skipping.");
            return 0;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; }
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", 9);

                int    id       = Integer.parseInt(parts[0].trim());
                String username = parts[1].trim();
                String password = parts[2].trim();
                String role     = parts[3].trim().toUpperCase();
                int    xpScore  = parts.length > 4 && !parts[4].trim().isEmpty()
                                  ? Integer.parseInt(parts[4].trim()) : 0;
                String firstName = parts.length > 5 ? parts[5].trim() : "";
                String lastName  = parts.length > 6 ? parts[6].trim() : "";
                String email     = parts.length > 7 ? parts[7].trim() : "";
                String bio       = parts.length > 8 ? parts[8].trim().replace("\"", "") : "";

                Map<String, Object> data = new HashMap<>();
                data.put("id",        id);
                data.put("username",  username);
                data.put("password",  password);
                data.put("role",      role);
                data.put("xpScore",   xpScore);
                data.put("firstName", firstName);
                data.put("lastName",  lastName);
                data.put("email",     email);
                data.put("bio",       bio);

                db.collection("users").document(String.valueOf(id)).set(data).get();
                System.out.println("  [USER] " + id + " - " + username + " (" + role + ")");
                count++;
            }
        }
        return count;
    }

    // ==================== LESSONS ====================

    private static int migrateLessons() throws Exception {
        int count = 0;
        File file = new File(LESSON_FILE);
        if (!file.exists()) {
            System.err.println("  WARNING: " + LESSON_FILE + " not found, skipping.");
            return 0;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; }
                if (line.trim().isEmpty()) continue;

                // Limit to 7 parts because theoryText may contain commas
                String[] parts = line.split(",", 7);
                if (parts.length < 7) {
                    System.err.println("  SKIP (bad format): " + line);
                    continue;
                }

                int    id            = Integer.parseInt(parts[0].trim());
                String category      = parts[1].trim();
                String title         = parts[2].trim();
                String youtubeId     = parts[3].trim();
                String dateCreated   = parts[4].trim();
                String correctAnswer = parts[5].trim();
                String theoryText    = parts[6].trim().replace("\"", "");

                Map<String, Object> data = new HashMap<>();
                data.put("id",            id);
                data.put("category",      category);
                data.put("title",         title);
                data.put("youtubeId",     youtubeId);
                data.put("dateCreated",   dateCreated);
                data.put("correctAnswer", correctAnswer);
                data.put("theoryText",    theoryText);

                db.collection("lessons").document(String.valueOf(id)).set(data).get();
                System.out.println("  [LESSON] " + id + " - " + title);
                count++;
            }
        }
        return count;
    }

    // ==================== CERTIFICATES ====================

    private static int migrateCertificates() throws Exception {
        int count = 0;
        File file = new File(CERTIFICATE_FILE);
        if (!file.exists()) {
            System.err.println("  WARNING: " + CERTIFICATE_FILE + " not found, skipping.");
            return 0;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; }
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", 6);
                if (parts.length < 6) continue;

                int    id               = Integer.parseInt(parts[0].trim());
                int    userId           = Integer.parseInt(parts[1].trim());
                String category         = parts[2].trim();
                String certificateName  = parts[3].trim();
                String issueDate        = parts[4].trim();
                int    lessonsCompleted = Integer.parseInt(parts[5].trim());

                Map<String, Object> data = new HashMap<>();
                data.put("id",               id);
                data.put("userId",           userId);
                data.put("category",         category);
                data.put("certificateName",  certificateName);
                data.put("issueDate",        issueDate);
                data.put("lessonsCompleted", lessonsCompleted);

                db.collection("certificates").document(String.valueOf(id)).set(data).get();
                System.out.println("  [CERT] " + id + " - " + certificateName + " (user " + userId + ")");
                count++;
            }
        }
        return count;
    }

    // ==================== SAVED COURSES ====================

    private static int migrateSavedCourses() throws Exception {
        int count = 0;
        File file = new File(SAVED_COURSES_FILE);
        if (!file.exists()) {
            System.err.println("  WARNING: " + SAVED_COURSES_FILE + " not found, skipping.");
            return 0;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; }
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", 3);
                if (parts.length < 3) continue;

                int    userId   = Integer.parseInt(parts[0].trim());
                int    lessonId = Integer.parseInt(parts[1].trim());
                String saveDate = parts[2].trim();

                Map<String, Object> data = new HashMap<>();
                data.put("userId",   userId);
                data.put("lessonId", lessonId);
                data.put("saveDate", saveDate);

                // Use auto-generated doc ID for saved courses (no natural PK)
                db.collection("savedCourses").add(data).get();
                System.out.println("  [SAVED] user=" + userId + " lesson=" + lessonId);
                count++;
            }
        }
        return count;
    }
}
