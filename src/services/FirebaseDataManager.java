package services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import model.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * FirebaseDataManager - Firestore implementation of DataManager.
 *
 * FIRESTORE COLLECTIONS:
 *   users           -> id (string), username, password, role, xpScore,
 *                      firstName, lastName, email, bio
 *   lessons         -> id (string), category, title, youtubeId,
 *                      dateCreated, correctAnswer, theoryText
 *   certificates    -> id (string), userId, category, certificateName,
 *                      issueDate, lessonsCompleted
 *   savedCourses    -> auto-id, userId, lessonId, saveDate
 *
 * DOCUMENT ID STRATEGY:
 *   We store the numeric int id as a field AND use it as the Firestore
 *   document ID (as a String) so lookups stay O(1) with no full-scan.
 *
 * SETUP:
 *   Set environment variable FIREBASE_SERVICE_ACCOUNT_PATH to the path
 *   of your serviceAccountKey.json downloaded from the Firebase console.
 */
public class FirebaseDataManager implements DataManager {

    private final Firestore db;

    // Firestore collection names
    private static final String USERS_COL        = "users";
    private static final String LESSONS_COL      = "lessons";
    private static final String CERTS_COL        = "certificates";
    private static final String SAVED_COL        = "savedCourses";

    // ==================== CONSTRUCTOR / INIT ====================

    public FirebaseDataManager() throws Exception {
        initFirebase();
        this.db = FirestoreClient.getFirestore();
    }

    private void initFirebase() throws Exception {
        if (!FirebaseApp.getApps().isEmpty()) return; // already initialized

        String path = System.getenv("FIREBASE_SERVICE_ACCOUNT_PATH");
        if (path == null || path.isEmpty()) {
            throw new IllegalStateException(
                "Environment variable FIREBASE_SERVICE_ACCOUNT_PATH is not set. " +
                "Point it to your serviceAccountKey.json file."
            );
        }

        InputStream serviceAccount = new FileInputStream(path);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
        System.out.println("Firebase initialized successfully.");
    }

    // ==================== USER OPERATIONS ====================

    @Override
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try {
            QuerySnapshot snapshot = db.collection(USERS_COL).get().get();
            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                User user = documentToUser(doc);
                if (user != null) users.add(user);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    @Override
    public User authenticate(String username, String password) {
        try {
            QuerySnapshot snapshot = db.collection(USERS_COL)
                    .whereEqualTo("username", username)
                    .whereEqualTo("password", password)
                    .limit(1)
                    .get().get();

            if (!snapshot.isEmpty()) {
                return documentToUser(snapshot.getDocuments().get(0));
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error authenticating: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean isUsernameTaken(String username) {
        try {
            QuerySnapshot snapshot = db.collection(USERS_COL)
                    .whereEqualTo("username", username)
                    .limit(1)
                    .get().get();
            return !snapshot.isEmpty();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error checking username: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Student registerUser(String username, String password) {
        if (isUsernameTaken(username)) return null;

        int nextId = getNextId(USERS_COL);
        Student newStudent = new Student(nextId, username, password, 0);

        Map<String, Object> data = new HashMap<>();
        data.put("id",        nextId);
        data.put("username",  username);
        data.put("password",  password);
        data.put("role",      "STUDENT");
        data.put("xpScore",   0);
        data.put("firstName", "");
        data.put("lastName",  "");
        data.put("email",     "");
        data.put("bio",       "");

        try {
            db.collection(USERS_COL).document(String.valueOf(nextId)).set(data).get();
            System.out.println("Registered new user: " + username + " (ID: " + nextId + ")");
            return newStudent;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Student createGuestUser() {
        // Guest users are NOT persisted — ID = -1 is the marker
        return new Student(-1, "Guest", "", 0);
    }

    @Override
    public void saveUserProgress(Student student) {
        if (student.getId() < 0) {
            System.out.println("Guest user - progress not saved.");
            return;
        }
        try {
            db.collection(USERS_COL)
              .document(String.valueOf(student.getId()))
              .update("xpScore", student.getXpScore())
              .get();
            System.out.println("Progress saved for: " + student.getUsername());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error saving progress: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(int userId) {
        try {
            db.collection(USERS_COL).document(String.valueOf(userId)).delete().get();
            System.out.println("User " + userId + " deleted.");
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }

    @Override
    public void updateStudentProfile(Student student) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("username",  student.getUsername());
        updates.put("password",  student.getPassword());
        updates.put("xpScore",   student.getXpScore());
        updates.put("firstName", student.getFirstName());
        updates.put("lastName",  student.getLastName());
        updates.put("email",     student.getEmail());
        updates.put("bio",       student.getBio());

        try {
            db.collection(USERS_COL)
              .document(String.valueOf(student.getId()))
              .update(updates)
              .get();
            System.out.println("Profile updated for: " + student.getUsername());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error updating profile: " + e.getMessage());
        }
    }

    // ==================== LESSON OPERATIONS ====================

    @Override
    public List<Lesson> loadLessons() {
        List<Lesson> lessons = new ArrayList<>();
        try {
            QuerySnapshot snapshot = db.collection(LESSONS_COL).get().get();
            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                Lesson lesson = documentToLesson(doc);
                if (lesson != null) lessons.add(lesson);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error loading lessons: " + e.getMessage());
        }
        return lessons;
    }

    @Override
    public Lesson getLessonById(int lessonId) {
        try {
            DocumentSnapshot doc = db.collection(LESSONS_COL)
                    .document(String.valueOf(lessonId))
                    .get().get();
            if (doc.exists()) return documentToLesson(doc);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error getting lesson: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void addLesson(Lesson lesson) {
        Map<String, Object> data = new HashMap<>();
        data.put("id",            lesson.getId());
        data.put("category",      lesson.getCategory());
        data.put("title",         lesson.getTitle());
        data.put("youtubeId",     lesson.getYoutubeId());
        data.put("dateCreated",   lesson.getDateCreated().toString());
        data.put("correctAnswer", lesson.getCorrectAnswer());
        data.put("theoryText",    lesson.getTheoryText());

        try {
            db.collection(LESSONS_COL)
              .document(String.valueOf(lesson.getId()))
              .set(data).get();
            System.out.println("Lesson added: " + lesson.getTitle());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error adding lesson: " + e.getMessage());
        }
    }

    @Override
    public void deleteLesson(int lessonId) {
        try {
            db.collection(LESSONS_COL).document(String.valueOf(lessonId)).delete().get();
            System.out.println("Lesson " + lessonId + " deleted.");
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error deleting lesson: " + e.getMessage());
        }
    }

    // ==================== CERTIFICATE OPERATIONS ====================

    @Override
    public List<Certificate> loadCertificates() {
        List<Certificate> certs = new ArrayList<>();
        try {
            QuerySnapshot snapshot = db.collection(CERTS_COL).get().get();
            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                Certificate cert = documentToCertificate(doc);
                if (cert != null) certs.add(cert);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error loading certificates: " + e.getMessage());
        }
        return certs;
    }

    @Override
    public List<Certificate> getCertificatesForUser(int userId) {
        try {
            QuerySnapshot snapshot = db.collection(CERTS_COL)
                    .whereEqualTo("userId", userId)
                    .get().get();
            List<Certificate> certs = new ArrayList<>();
            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                Certificate cert = documentToCertificate(doc);
                if (cert != null) certs.add(cert);
            }
            return certs;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error getting certificates for user: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void issueCertificate(int userId, String category, String certificateName, int lessonsCompleted) {
        int newId = getNextId(CERTS_COL);

        Map<String, Object> data = new HashMap<>();
        data.put("id",               newId);
        data.put("userId",           userId);
        data.put("category",         category);
        data.put("certificateName",  certificateName);
        data.put("issueDate",        LocalDate.now().toString());
        data.put("lessonsCompleted", lessonsCompleted);

        try {
            db.collection(CERTS_COL).document(String.valueOf(newId)).set(data).get();
            System.out.println("Certificate issued: " + certificateName);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error issuing certificate: " + e.getMessage());
        }
    }

    @Override
    public boolean hasCertificateForCategory(int userId, String category) {
        return getCertificatesForUser(userId).stream()
                .anyMatch(c -> c.getCategory().equalsIgnoreCase(category));
    }

    // ==================== SAVED COURSES OPERATIONS ====================

    @Override
    public List<SavedCourse> loadSavedCourses() {
        List<SavedCourse> savedCourses = new ArrayList<>();
        try {
            QuerySnapshot snapshot = db.collection(SAVED_COL).get().get();
            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                SavedCourse sc = documentToSavedCourse(doc);
                if (sc != null) savedCourses.add(sc);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error loading saved courses: " + e.getMessage());
        }
        return savedCourses;
    }

    @Override
    public List<SavedCourse> getSavedCoursesForUser(int userId) {
        try {
            QuerySnapshot snapshot = db.collection(SAVED_COL)
                    .whereEqualTo("userId", userId)
                    .get().get();
            List<SavedCourse> result = new ArrayList<>();
            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                SavedCourse sc = documentToSavedCourse(doc);
                if (sc != null) result.add(sc);
            }
            return result;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error getting saved courses: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void saveCourseForUser(int userId, int lessonId) {
        if (isCourseSaved(userId, lessonId)) {
            System.out.println("Course already saved.");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("userId",   userId);
        data.put("lessonId", lessonId);
        data.put("saveDate", LocalDate.now().toString());

        try {
            // Auto-generated document ID for saved courses
            db.collection(SAVED_COL).add(data).get();
            System.out.println("Course saved successfully.");
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error saving course: " + e.getMessage());
        }
    }

    @Override
    public void unsaveCourseForUser(int userId, int lessonId) {
        try {
            QuerySnapshot snapshot = db.collection(SAVED_COL)
                    .whereEqualTo("userId",   userId)
                    .whereEqualTo("lessonId", lessonId)
                    .get().get();

            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                doc.getReference().delete().get();
            }
            System.out.println("Course unsaved successfully.");
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error unsaving course: " + e.getMessage());
        }
    }

    @Override
    public boolean isCourseSaved(int userId, int lessonId) {
        try {
            QuerySnapshot snapshot = db.collection(SAVED_COL)
                    .whereEqualTo("userId",   userId)
                    .whereEqualTo("lessonId", lessonId)
                    .limit(1)
                    .get().get();
            return !snapshot.isEmpty();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error checking saved course: " + e.getMessage());
            return false;
        }
    }

    // ==================== PRIVATE HELPERS ====================

    /**
     * Convert a Firestore document to a User (Student or Admin).
     */
    private User documentToUser(DocumentSnapshot doc) {
        try {
            int    id       = Objects.requireNonNull(doc.getLong("id")).intValue();
            String username = doc.getString("username");
            String password = doc.getString("password");
            String role     = doc.getString("role");

            if ("STUDENT".equalsIgnoreCase(role)) {
                Long xpLong = doc.getLong("xpScore");
                int xpScore = (xpLong != null) ? xpLong.intValue() : 0;

                Student s = new Student(id, username, password, xpScore);
                s.setFirstName(doc.getString("firstName"));
                s.setLastName(doc.getString("lastName"));
                s.setEmail(doc.getString("email"));
                s.setBio(doc.getString("bio"));
                return s;

            } else if ("ADMIN".equalsIgnoreCase(role)) {
                return new Admin(id, username, password);
            }
        } catch (Exception e) {
            System.err.println("Error converting document to User: " + e.getMessage());
        }
        return null;
    }

    /**
     * Convert a Firestore document to a Lesson.
     */
    private Lesson documentToLesson(DocumentSnapshot doc) {
        try {
            int    id            = Objects.requireNonNull(doc.getLong("id")).intValue();
            String category      = doc.getString("category");
            String title         = doc.getString("title");
            String youtubeId     = doc.getString("youtubeId");
            String dateCreated   = doc.getString("dateCreated");
            String correctAnswer = doc.getString("correctAnswer");
            String theoryText    = doc.getString("theoryText");

            return new Lesson(id, category, title, youtubeId, dateCreated, correctAnswer, theoryText);
        } catch (Exception e) {
            System.err.println("Error converting document to Lesson: " + e.getMessage());
            return null;
        }
    }

    /**
     * Convert a Firestore document to a Certificate.
     */
    private Certificate documentToCertificate(DocumentSnapshot doc) {
        try {
            int    id               = Objects.requireNonNull(doc.getLong("id")).intValue();
            int    userId           = Objects.requireNonNull(doc.getLong("userId")).intValue();
            String category         = doc.getString("category");
            String certificateName  = doc.getString("certificateName");
            String issueDate        = doc.getString("issueDate");
            int    lessonsCompleted = Objects.requireNonNull(doc.getLong("lessonsCompleted")).intValue();

            return new Certificate(id, userId, category, certificateName, issueDate, lessonsCompleted);
        } catch (Exception e) {
            System.err.println("Error converting document to Certificate: " + e.getMessage());
            return null;
        }
    }

    /**
     * Convert a Firestore document to a SavedCourse.
     */
    private SavedCourse documentToSavedCourse(DocumentSnapshot doc) {
        try {
            int    userId   = Objects.requireNonNull(doc.getLong("userId")).intValue();
            int    lessonId = Objects.requireNonNull(doc.getLong("lessonId")).intValue();
            String saveDate = doc.getString("saveDate");

            return new SavedCourse(userId, lessonId, saveDate);
        } catch (Exception e) {
            System.err.println("Error converting document to SavedCourse: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get the next available numeric ID for a collection.
     * Finds max existing ID and adds 1.
     */
    private int getNextId(String collection) {
        try {
            QuerySnapshot snapshot = db.collection(collection).get().get();
            return snapshot.getDocuments().stream()
                    .mapToInt(doc -> {
                        Long id = doc.getLong("id");
                        return (id != null) ? id.intValue() : 0;
                    })
                    .max()
                    .orElse(0) + 1;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error getting next ID: " + e.getMessage());
            return (int) (System.currentTimeMillis() % 100000); // fallback
        }
    }
}
