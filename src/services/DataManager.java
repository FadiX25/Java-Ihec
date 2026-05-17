package services;

import model.Certificate;
import model.Lesson;
import model.SavedCourse;
import model.Student;
import model.User;

import java.util.List;

/**
 * DataManager - Interface defining all database operations.
 *
 * WHY AN INTERFACE?
 * - The UI (Views) depend on this contract, NOT on a specific implementation
 * - We can swap CsvDataManager <-> FirebaseDataManager without touching the UI
 * - This is the Strategy Pattern + Dependency Inversion Principle (SOLID)
 *
 * USAGE:
 *   DataManager db = new FirebaseDataManager();   // or new CsvDataManager()
 *   List<User> users = db.loadUsers();            // same call, different backend
 */
public interface DataManager {

    // ==================== USER OPERATIONS ====================

    /**
     * Load all users (Students and Admins) from the data source.
     */
    List<User> loadUsers();

    /**
     * Authenticate a user by username and password.
     * @return matching User or null if credentials are wrong
     */
    User authenticate(String username, String password);

    /**
     * Check if a username is already taken.
     */
    boolean isUsernameTaken(String username);

    /**
     * Register a new student account.
     * @return the created Student, or null if username is taken
     */
    Student registerUser(String username, String password);

    /**
     * Create a temporary guest user (not persisted).
     */
    Student createGuestUser();

    /**
     * Save updated XP and progress for a student.
     */
    void saveUserProgress(Student student);

    /**
     * Delete a user by their ID.
     */
    void deleteUser(int userId);

    /**
     * Update a student's profile info (name, email, bio, etc.).
     */
    void updateStudentProfile(Student student);

    // ==================== LESSON OPERATIONS ====================

    /**
     * Load all lessons from the data source.
     */
    List<Lesson> loadLessons();

    /**
     * Find a specific lesson by ID.
     * @return Lesson or null if not found
     */
    Lesson getLessonById(int lessonId);

    /**
     * Add a new lesson to the data source.
     */
    void addLesson(Lesson lesson);

    /**
     * Delete a lesson by ID.
     */
    void deleteLesson(int lessonId);

    // ==================== CERTIFICATE OPERATIONS ====================

    /**
     * Load all certificates from the data source.
     */
    List<Certificate> loadCertificates();

    /**
     * Get all certificates belonging to a specific user.
     */
    List<Certificate> getCertificatesForUser(int userId);

    /**
     * Issue a new certificate to a user after completing a category.
     */
    void issueCertificate(int userId, String category, String certificateName, int lessonsCompleted);

    /**
     * Check if a user already has a certificate for a given category.
     */
    boolean hasCertificateForCategory(int userId, String category);

    // ==================== SAVED COURSES OPERATIONS ====================

    /**
     * Load all saved courses from the data source.
     */
    List<SavedCourse> loadSavedCourses();

    /**
     * Get saved courses for a specific user.
     */
    List<SavedCourse> getSavedCoursesForUser(int userId);

    /**
     * Save a course (lesson) for a user.
     */
    void saveCourseForUser(int userId, int lessonId);

    /**
     * Remove a saved course for a user.
     */
    void unsaveCourseForUser(int userId, int lessonId);

    /**
     * Check if a specific course is saved by a user.
     */
    boolean isCourseSaved(int userId, int lessonId);
}
