package services;

import model.Admin;
import model.Lesson;
import model.Student;
import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CsvDataManager - Handles all CSV file operations.
 */
public class CsvDataManager {

    // ==================== FILE PATHS ====================
    // These are relative to the project root

    private static final String USER_FILE = "users.csv";
    private static final String LESSON_FILE = "lessons.csv";

    // ==================== USER OPERATIONS ====================

    /**
     * Load all users from the CSV file.
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
     */
    public Student createGuestUser() {
        // Guest users have ID = -1 (special marker)
        // This way we know not to save their progress
        return new Student(-1, "Guest", "", 0);
    }

    /**
     * Save a student's progress (XP and completed lessons) back to CSV.
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
     * Load all lessons from the CSV file with support for new fields.
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
     */
    private Lesson parseLessonLine(String line) {
        try {
            // Split by comma with limit to handle quoted text
            String[] parts = line.split(",", 10);

            if (parts.length < 6) {
                System.err.println("Invalid lesson line (not enough columns): " + line);
                return null;
            }

            int id = Integer.parseInt(parts[0].trim());
            String title = parts[1].trim();
            String youtubeId = parts[2].trim();
            String dateString = parts[3].trim();
            String correctAnswer = parts[4].trim();
            String theoryText = parts[5].trim();

            // Remove surrounding quotes from theory text if present
            if (theoryText.startsWith("\"") && theoryText.endsWith("\"")) {
                theoryText = theoryText.substring(1, theoryText.length() - 1);
            }

            // Handle optional new fields with default values
            int duration = 25; // Valeur par défaut
            String difficulty = "Intermédiaire";
            String category = "Java";
            String tags = "";

            if (parts.length > 6 && !parts[6].trim().isEmpty()) {
                try {
                    duration = Integer.parseInt(parts[6].trim());
                } catch (NumberFormatException e) {
                    duration = 25;
                }
            }

            if (parts.length > 7 && !parts[7].trim().isEmpty()) {
                difficulty = parts[7].trim();
            }

            if (parts.length > 8 && !parts[8].trim().isEmpty()) {
                category = parts[8].trim();
            }

            if (parts.length > 9 && !parts[9].trim().isEmpty()) {
                tags = parts[9].trim();
            }

            return new Lesson(id, title, youtubeId, dateString, correctAnswer, theoryText,
                    duration, difficulty, category, tags);

        } catch (Exception e) {
            System.err.println("Error parsing lesson line: " + line);
            System.err.println("Reason: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get a specific lesson by ID.
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
     * Add a new lesson to the CSV file with all fields.
     */
    public void addLesson(Lesson lesson) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LESSON_FILE, true))) {
            // Append to file (true = append mode)
            String theoryText = "\"" + lesson.getTheoryText() + "\"";
            writer.println(lesson.getId() + "," +
                    lesson.getTitle() + "," +
                    lesson.getYoutubeId() + "," +
                    lesson.getDateCreated() + "," +
                    lesson.getCorrectAnswer() + "," +
                    theoryText + "," +
                    lesson.getDuration() + "," +
                    lesson.getDifficulty() + "," +
                    lesson.getCategory() + "," +
                    lesson.getTags());

        } catch (IOException e) {
            System.err.println("Error adding lesson: " + e.getMessage());
        }
    }

    /**
     * Delete a user by ID.
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
     */
    public void deleteLesson(int lessonId) {
        List<Lesson> lessons = loadLessons();

        // Remove the lesson with matching ID
        lessons.removeIf(lesson -> lesson.getId() == lessonId);

        // Rewrite the file without the deleted lesson
        try (PrintWriter writer = new PrintWriter(new FileWriter(LESSON_FILE))) {
            // Write header with all fields
            writer.println("id,title,youtube_id,date_created,correct_answer_keyword,theory_text,duration,difficulty,category,tags");

            // Write each remaining lesson
            for (Lesson lesson : lessons) {
                String theoryText = "\"" + lesson.getTheoryText() + "\"";
                writer.println(lesson.getId() + "," +
                        lesson.getTitle() + "," +
                        lesson.getYoutubeId() + "," +
                        lesson.getDateCreated() + "," +
                        lesson.getCorrectAnswer() + "," +
                        theoryText + "," +
                        lesson.getDuration() + "," +
                        lesson.getDifficulty() + "," +
                        lesson.getCategory() + "," +
                        lesson.getTags());
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
     * Create a sample lessons.csv file with demo data including new fields.
     */
    private void createSampleLessonFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LESSON_FILE))) {
            writer.println("id,title,youtube_id,date_created,correct_answer_keyword,theory_text,duration,difficulty,category,tags");
            writer.println("101,Introduction to Java,Hl-zzrqQoSE,2023-10-01,class,\"Java is an Object-Oriented Programming language. In this lesson, you will learn how to create your first class. A class is a blueprint for creating objects.\",25,Débutant,Java,POO,Bases");
            writer.println("102,Variables and Data Types,eIrMbAQSU34,2023-10-15,int,\"Variables are containers for storing data values. Java has different types: int for integers, double for decimals, String for text, and boolean for true/false.\",20,Débutant,Java,Variables,Types");
            writer.println("103,Object-Oriented Programming,pTB0EiLXUC8,2023-11-01,object,\"OOP is a programming paradigm based on the concept of objects. Objects contain data (fields) and code (methods). The four pillars are: Encapsulation, Inheritance, Polymorphism, and Abstraction.\",35,Intermédiaire,Java,POO,Avancé");
            System.out.println("Created sample " + LESSON_FILE);
        } catch (IOException e) {
            System.err.println("Could not create sample lesson file: " + e.getMessage());
        }
    }
}