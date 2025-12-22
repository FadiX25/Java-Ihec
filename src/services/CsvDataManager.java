package services;

import model.Admin;
import model.Certificate;
import model.Lesson;
import model.SavedCourse;
import model.Student;
import model.User;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CsvDataManager - Handles all CSV file operations.
 * 
 * This is the CONTROLLER in our MVC pattern:
 * - The VIEW (UI) never touches files directly
 * - The VIEW asks CsvDataManager for data
 * - CsvDataManager reads/writes to CSV files
 * 
 * FILE STRUCTURE:
 * users.csv         -> id, username, password, role, xp_score, first_name, last_name, email, bio
 * lessons.csv       -> id, category, title, youtube_id, date_created, correct_answer_keyword, theory_text
 * certificates.csv  -> id, user_id, category, certificate_name, issue_date, lessons_completed
 * saved_courses.csv -> user_id, lesson_id, save_date
 */
public class CsvDataManager {

    // ==================== FILE PATHS ====================
    // These are relative to the project root
    
    private static final String USER_FILE = "users.csv";
    private static final String LESSON_FILE = "lessons.csv";
    private static final String CERTIFICATE_FILE = "certificates.csv";
    private static final String SAVED_COURSES_FILE = "saved_courses.csv";

    // ==================== USER OPERATIONS ====================
    
    /**
     * Load all users from the CSV file.
     * 
     * This method demonstrates:
     * - File reading with BufferedReader
     * - Exception handling (try-catch)
     * - Polymorphism: We return List<User> but it contains Student/Admin objects
     * 
     * @return List of all users (Students and Admins)
     */
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        
        // Try-with-resources: automatically closes the file when done
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            
            String line;
            boolean isFirstLine = true;
            
            // Read file line by line
            while ((line = reader.readLine()) != null) {
                // Skip header row if present
                if (isFirstLine) {
                    isFirstLine = false;
                    // Check if this looks like a header (contains "id" or "username")
                    if (line.toLowerCase().contains("id") || 
                        line.toLowerCase().contains("username")) {
                        continue;  // Skip this line
                    }
                }
                
                // Parse the CSV line
                User user = parseUserLine(line);
                if (user != null) {
                    users.add(user);
                }
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("Error: " + USER_FILE + " not found!");
            System.err.println("Creating a sample users.csv file...");
            createSampleUserFile();
        } catch (IOException e) {
            System.err.println("Error reading " + USER_FILE + ": " + e.getMessage());
        }
        
        return users;
    }
    
    /**
     * Parse a single CSV line into a User object.
     * 
     * CSV Format: id, username, password, role, xp_score
     * Example: 1,ahmed,12345,STUDENT,50
     * 
     * @param line The CSV line to parse
     * @return User object (Student or Admin), or null if parsing fails
     */
    private User parseUserLine(String line) {
        try {
            // Split by comma
            String[] parts = line.split(",");
            
            if (parts.length < 4) {
                System.err.println("Invalid user line (not enough columns): " + line);
                return null;
            }
            
            // Extract values
            int id = Integer.parseInt(parts[0].trim());
            String username = parts[1].trim();
            String password = parts[2].trim();
            String role = parts[3].trim().toUpperCase();
            
            // Create appropriate object based on role (POLYMORPHISM!)
            if (role.equals("STUDENT")) {
                int xpScore = 0;
                if (parts.length > 4) {
                    xpScore = Integer.parseInt(parts[4].trim());
                }
                return new Student(id, username, password, xpScore);
            } else if (role.equals("ADMIN")) {
                return new Admin(id, username, password);
            } else {
                System.err.println("Unknown role: " + role);
                return null;
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Error parsing numbers in line: " + line);
            return null;
        }
    }
    
    /**
     * Authenticate a user by username and password.
     * 
     * @param username The entered username
     * @param password The entered password
     * @return The User object if credentials match, null otherwise
     */
    public User authenticate(String username, String password) {
        List<User> users = loadUsers();
        
        for (User user : users) {
            if (user.getUsername().equals(username) && 
                user.getPassword().equals(password)) {
                return user;  // Found matching user!
            }
        }
        
        return null;  // No match found
    }
    
    /**
     * Check if a username is already taken.
     * 
     * @param username The username to check
     * @return true if the username exists, false otherwise
     */
    public boolean isUsernameTaken(String username) {
        List<User> users = loadUsers();
        
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;  // Username already exists!
            }
        }
        
        return false;  // Username is available
    }
    
    /**
     * Register a new student user.
     * 
     * HOW IT WORKS:
     * 1. Find the highest existing ID
     * 2. Create new user with ID + 1
     * 3. Append to the CSV file
     * 
     * @param username The new user's username
     * @param password The new user's password
     * @return The created Student object, or null if registration fails
     */
    public Student registerUser(String username, String password) {
        // First, check if username is taken
        if (isUsernameTaken(username)) {
            return null;
        }
        
        // Find the next available ID
        List<User> users = loadUsers();
        int nextId = 1;
        for (User user : users) {
            if (user.getId() >= nextId) {
                nextId = user.getId() + 1;
            }
        }
        
        // Create the new student (starts with 0 XP)
        Student newStudent = new Student(nextId, username, password, 0);
        
        // Append to CSV file
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE, true))) {
            // 'true' means append mode - adds to end of file
            writer.println(nextId + "," + username + "," + password + ",STUDENT,0");
            System.out.println("Registered new user: " + username + " (ID: " + nextId + ")");
            return newStudent;
            
        } catch (IOException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Create a guest user (not saved to file).
     * Guest users can browse but their progress won't be saved.
     * 
     * @return A temporary Student object for guest access
     */
    public Student createGuestUser() {
        // Guest users have ID = -1 (special marker)
        // This way we know not to save their progress
        return new Student(-1, "Guest", "", 0);
    }
    
    /**
     * Save a student's progress (XP and completed lessons) back to CSV.
     * 
     * @param student The student whose progress to save
     */
    public void saveUserProgress(Student student) {
        // Don't save guest users (they have ID = -1)
        if (student.getId() < 0) {
            System.out.println("Guest user - progress not saved.");
            return;
        }
        
        List<User> users = loadUsers();
        
        // Find and update the student in the list
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == student.getId()) {
                users.set(i, student);
                break;
            }
        }
        
        // Write all users back to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            // Write header
            writer.println("id,username,password,role,xp_score");
            
            // Write each user
            for (User user : users) {
                if (user instanceof Student) {
                    Student s = (Student) user;
                    writer.println(s.getId() + "," + s.getUsername() + "," + 
                                 s.getPassword() + ",STUDENT," + s.getXpScore());
                } else {
                    writer.println(user.getId() + "," + user.getUsername() + "," + 
                                 user.getPassword() + ",ADMIN,0");
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error saving user progress: " + e.getMessage());
        }
    }

    // ==================== LESSON OPERATIONS ====================
    
    /**
     * Load all lessons from the CSV file.
     * 
     * @return List of all lessons
     */
    public List<Lesson> loadLessons() {
        List<Lesson> lessons = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(LESSON_FILE))) {
            
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    if (line.toLowerCase().contains("id") || 
                        line.toLowerCase().contains("title")) {
                        continue;
                    }
                }
                
                Lesson lesson = parseLessonLine(line);
                if (lesson != null) {
                    lessons.add(lesson);
                }
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("Error: " + LESSON_FILE + " not found!");
            System.err.println("Creating a sample lessons.csv file...");
            createSampleLessonFile();
        } catch (IOException e) {
            System.err.println("Error reading " + LESSON_FILE + ": " + e.getMessage());
        }
        
        return lessons;
    }
    
    /**
     * Parse a single CSV line into a Lesson object.
     * 
     * CSV Format: id, category, title, youtube_id, date_created, correct_answer_keyword, theory_text
     * 
     * @param line The CSV line to parse
     * @return Lesson object, or null if parsing fails
     */
    private Lesson parseLessonLine(String line) {
        try {
            // Split by comma (but be careful - theory_text might contain commas!)
            // We'll use a limit of 7 to keep the theory text together
            String[] parts = line.split(",", 7);
            
            if (parts.length < 7) {
                System.err.println("Invalid lesson line (not enough columns): " + line);
                return null;
            }
            
            int id = Integer.parseInt(parts[0].trim());
            String category = parts[1].trim();
            String title = parts[2].trim();
            String youtubeId = parts[3].trim();
            String dateString = parts[4].trim();
            String correctAnswer = parts[5].trim();
            String theoryText = parts[6].trim();
            
            // Remove surrounding quotes from theory text if present
            if (theoryText.startsWith("\"") && theoryText.endsWith("\"")) {
                theoryText = theoryText.substring(1, theoryText.length() - 1);
            }
            
            return new Lesson(id, category, title, youtubeId, dateString, correctAnswer, theoryText);
            
        } catch (Exception e) {
            System.err.println("Error parsing lesson line: " + line);
            System.err.println("Reason: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get a specific lesson by ID.
     * 
     * @param lessonId The ID to search for
     * @return The Lesson, or null if not found
     */
    public Lesson getLessonById(int lessonId) {
        List<Lesson> lessons = loadLessons();
        
        for (Lesson lesson : lessons) {
            if (lesson.getId() == lessonId) {
                return lesson;
            }
        }
        
        return null;
    }
    
    /**
     * Add a new lesson to the CSV file.
     * 
     * @param lesson The lesson to add
     */
    public void addLesson(Lesson lesson) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LESSON_FILE, true))) {
            // Append to file (true = append mode)
            writer.println(lesson.getId() + "," + 
                         lesson.getCategory() + "," +
                         lesson.getTitle() + "," + 
                         lesson.getYoutubeId() + "," + 
                         lesson.getDateCreated() + "," + 
                         lesson.getCorrectAnswer() + "," + 
                         "\"" + lesson.getTheoryText() + "\"");
                         
        } catch (IOException e) {
            System.err.println("Error adding lesson: " + e.getMessage());
        }
    }
    
    /**
     * Delete a user by ID.
     * 
     * @param userId The ID of the user to delete
     */
    public void deleteUser(int userId) {
        List<User> users = loadUsers();
        
        // Remove the user with matching ID
        users.removeIf(user -> user.getId() == userId);
        
        // Rewrite the file without the deleted user
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            // Write header
            writer.println("id,username,password,role,xp_score");
            
            // Write each remaining user
            for (User user : users) {
                if (user instanceof Student) {
                    Student s = (Student) user;
                    writer.println(s.getId() + "," + s.getUsername() + "," + 
                                 s.getPassword() + ",STUDENT," + s.getXpScore());
                } else {
                    writer.println(user.getId() + "," + user.getUsername() + "," + 
                                 user.getPassword() + ",ADMIN,0");
                }
            }
            
            System.out.println("User " + userId + " deleted successfully.");
            
        } catch (IOException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }
    
    /**
     * Delete a lesson by ID.
     * 
     * @param lessonId The ID of the lesson to delete
     */
    public void deleteLesson(int lessonId) {
        List<Lesson> lessons = loadLessons();
        
        // Remove the lesson with matching ID
        lessons.removeIf(lesson -> lesson.getId() == lessonId);
        
        // Rewrite the file without the deleted lesson
        try (PrintWriter writer = new PrintWriter(new FileWriter(LESSON_FILE))) {
            // Write header
            writer.println("id,category,title,youtube_id,date_created,correct_answer_keyword,theory_text");
            
            // Write each remaining lesson
            for (Lesson lesson : lessons) {
                writer.println(lesson.getId() + "," + 
                             lesson.getCategory() + "," +
                             lesson.getTitle() + "," + 
                             lesson.getYoutubeId() + "," + 
                             lesson.getDateCreated() + "," + 
                             lesson.getCorrectAnswer() + "," + 
                             "\"" + lesson.getTheoryText() + "\"");
            }
            
            System.out.println("Lesson " + lessonId + " deleted successfully.");
            
        } catch (IOException e) {
            System.err.println("Error deleting lesson: " + e.getMessage());
        }
    }

    // ==================== HELPER METHODS ====================
    
    /**
     * Create a sample users.csv file with demo data.
     */
    private void createSampleUserFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            writer.println("id,username,password,role,xp_score");
            writer.println("1,ahmed,12345,STUDENT,50");
            writer.println("2,sara,password123,STUDENT,120");
            writer.println("3,admin,admin123,ADMIN,0");
            System.out.println("Created sample " + USER_FILE);
        } catch (IOException e) {
            System.err.println("Could not create sample user file: " + e.getMessage());
        }
    }
    
    /**
     * Create a sample lessons.csv file with demo data.
     */
    private void createSampleLessonFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LESSON_FILE))) {
            writer.println("id,category,title,youtube_id,date_created,correct_answer_keyword,theory_text");
            writer.println("101,Java,Introduction to Java,Hl-zzrqQoSE,2023-10-01,class,\"Java is an Object-Oriented Programming language. In this lesson, you will learn how to create your first class. A class is a blueprint for creating objects.\"");
            writer.println("102,Java,Variables and Data Types,eIrMbAQSU34,2023-10-15,int,\"Variables are containers for storing data values. Java has different types: int for integers, double for decimals, String for text, and boolean for true/false.\"");
            writer.println("103,Java,Object-Oriented Programming,pTB0EiLXUC8,2023-11-01,object,\"OOP is a programming paradigm based on the concept of objects. Objects contain data (fields) and code (methods). The four pillars are: Encapsulation, Inheritance, Polymorphism, and Abstraction.\"");
            System.out.println("Created sample " + LESSON_FILE);
        } catch (IOException e) {
            System.err.println("Could not create sample lesson file: " + e.getMessage());
        }
    }
    
    // ==================== CERTIFICATE OPERATIONS ====================
    
    /**
     * Load all certificates from the CSV file.
     */
    public List<Certificate> loadCertificates() {
        List<Certificate> certificates = new ArrayList<>();
        
        File file = new File(CERTIFICATE_FILE);
        if (!file.exists()) {
            createEmptyCertificateFile();
            return certificates;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(CERTIFICATE_FILE))) {
            String line;
            boolean isHeader = true;
            
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                
                Certificate cert = parseCertificateLine(line);
                if (cert != null) {
                    certificates.add(cert);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading certificates: " + e.getMessage());
        }
        
        return certificates;
    }
    
    /**
     * Get certificates for a specific user.
     */
    public List<Certificate> getCertificatesForUser(int userId) {
        return loadCertificates().stream()
                .filter(c -> c.getUserId() == userId)
                .collect(Collectors.toList());
    }
    
    /**
     * Parse a single certificate line from CSV.
     */
    private Certificate parseCertificateLine(String line) {
        try {
            String[] parts = line.split(",", 6);
            if (parts.length < 6) return null;
            
            int id = Integer.parseInt(parts[0].trim());
            int userId = Integer.parseInt(parts[1].trim());
            String category = parts[2].trim();
            String certificateName = parts[3].trim();
            String issueDate = parts[4].trim();
            int lessonsCompleted = Integer.parseInt(parts[5].trim());
            
            return new Certificate(id, userId, category, certificateName, issueDate, lessonsCompleted);
        } catch (Exception e) {
            System.err.println("Error parsing certificate line: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Issue a new certificate to a user.
     */
    public void issueCertificate(int userId, String category, String certificateName, int lessonsCompleted) {
        List<Certificate> certificates = loadCertificates();
        int newId = certificates.stream().mapToInt(Certificate::getId).max().orElse(0) + 1;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(CERTIFICATE_FILE, true))) {
            writer.println(newId + "," + userId + "," + category + "," + 
                          certificateName + "," + LocalDate.now() + "," + lessonsCompleted);
            System.out.println("Certificate issued: " + certificateName);
        } catch (IOException e) {
            System.err.println("Error issuing certificate: " + e.getMessage());
        }
    }
    
    /**
     * Check if user already has a certificate for a category.
     */
    public boolean hasCertificateForCategory(int userId, String category) {
        return getCertificatesForUser(userId).stream()
                .anyMatch(c -> c.getCategory().equalsIgnoreCase(category));
    }
    
    private void createEmptyCertificateFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CERTIFICATE_FILE))) {
            writer.println("id,user_id,category,certificate_name,issue_date,lessons_completed");
        } catch (IOException e) {
            System.err.println("Could not create certificate file: " + e.getMessage());
        }
    }
    
    // ==================== SAVED COURSES OPERATIONS ====================
    
    /**
     * Load all saved courses from CSV.
     */
    public List<SavedCourse> loadSavedCourses() {
        List<SavedCourse> savedCourses = new ArrayList<>();
        
        File file = new File(SAVED_COURSES_FILE);
        if (!file.exists()) {
            createEmptySavedCoursesFile();
            return savedCourses;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVED_COURSES_FILE))) {
            String line;
            boolean isHeader = true;
            
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                
                SavedCourse sc = parseSavedCourseLine(line);
                if (sc != null) {
                    savedCourses.add(sc);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading saved courses: " + e.getMessage());
        }
        
        return savedCourses;
    }
    
    /**
     * Get saved courses for a specific user.
     */
    public List<SavedCourse> getSavedCoursesForUser(int userId) {
        return loadSavedCourses().stream()
                .filter(sc -> sc.getUserId() == userId)
                .collect(Collectors.toList());
    }
    
    /**
     * Parse a saved course line from CSV.
     */
    private SavedCourse parseSavedCourseLine(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length < 3) return null;
            
            int userId = Integer.parseInt(parts[0].trim());
            int lessonId = Integer.parseInt(parts[1].trim());
            String saveDate = parts[2].trim();
            
            return new SavedCourse(userId, lessonId, saveDate);
        } catch (Exception e) {
            System.err.println("Error parsing saved course line: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Save a course for a user.
     */
    public void saveCourseForUser(int userId, int lessonId) {
        // Check if already saved
        List<SavedCourse> savedCourses = getSavedCoursesForUser(userId);
        boolean alreadySaved = savedCourses.stream()
                .anyMatch(sc -> sc.getLessonId() == lessonId);
        
        if (alreadySaved) {
            System.out.println("Course already saved.");
            return;
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVED_COURSES_FILE, true))) {
            writer.println(userId + "," + lessonId + "," + LocalDate.now());
            System.out.println("Course saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving course: " + e.getMessage());
        }
    }
    
    /**
     * Remove a saved course for a user.
     */
    public void unsaveCourseForUser(int userId, int lessonId) {
        List<SavedCourse> allSaved = loadSavedCourses();
        
        // Filter out the one to remove
        allSaved.removeIf(sc -> sc.getUserId() == userId && sc.getLessonId() == lessonId);
        
        // Rewrite file
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVED_COURSES_FILE))) {
            writer.println("user_id,lesson_id,save_date");
            for (SavedCourse sc : allSaved) {
                writer.println(sc.getUserId() + "," + sc.getLessonId() + "," + sc.getSaveDate());
            }
            System.out.println("Course unsaved successfully.");
        } catch (IOException e) {
            System.err.println("Error unsaving course: " + e.getMessage());
        }
    }
    
    /**
     * Check if a course is saved by a user.
     */
    public boolean isCourseSaved(int userId, int lessonId) {
        return getSavedCoursesForUser(userId).stream()
                .anyMatch(sc -> sc.getLessonId() == lessonId);
    }
    
    private void createEmptySavedCoursesFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVED_COURSES_FILE))) {
            writer.println("user_id,lesson_id,save_date");
        } catch (IOException e) {
            System.err.println("Could not create saved courses file: " + e.getMessage());
        }
    }
    
    // ==================== PROFILE OPERATIONS ====================
    
    /**
     * Update a student's profile information.
     */
    public void updateStudentProfile(Student student) {
        List<User> users = loadUsers();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            writer.println("id,username,password,role,xp_score,first_name,last_name,email,bio");
            
            for (User user : users) {
                if (user.getId() == student.getId()) {
                    // Write updated student
                    writer.println(student.getId() + "," +
                                  student.getUsername() + "," +
                                  student.getPassword() + "," +
                                  student.getRole() + "," +
                                  student.getXpScore() + "," +
                                  student.getFirstName() + "," +
                                  student.getLastName() + "," +
                                  student.getEmail() + "," +
                                  "\"" + student.getBio() + "\"");
                } else if (user instanceof Student) {
                    Student s = (Student) user;
                    writer.println(s.getId() + "," +
                                  s.getUsername() + "," +
                                  s.getPassword() + "," +
                                  s.getRole() + "," +
                                  s.getXpScore() + "," +
                                  s.getFirstName() + "," +
                                  s.getLastName() + "," +
                                  s.getEmail() + "," +
                                  "\"" + s.getBio() + "\"");
                } else {
                    // Admin
                    writer.println(user.getId() + "," +
                                  user.getUsername() + "," +
                                  user.getPassword() + "," +
                                  user.getRole() + ",0,,,,");
                }
            }
            System.out.println("Profile updated successfully.");
        } catch (IOException e) {
            System.err.println("Error updating profile: " + e.getMessage());
        }
    }
}
