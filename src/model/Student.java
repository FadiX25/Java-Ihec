package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Student - Represents a student user who takes courses.
 * 
 * INHERITANCE:
 * - 'extends User' means Student IS-A User
 * - Student inherits all fields and methods from User
 * - Student adds its OWN special fields: xpScore, completedLessonIds
 * 
 * POLYMORPHISM:
 * - We OVERRIDE showDashboard() to provide student-specific behavior
 */
public class Student extends User {

    // ==================== STUDENT-SPECIFIC FIELDS ====================
    
    /** XP (Experience Points) earned by completing lessons */
    private int xpScore;
    
    /** 
     * List of lesson IDs the student has completed.
     * Using ArrayList because we'll add to it as they progress.
     */
    private List<Integer> completedLessonIds;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor for Student.
     * 
     * @param id       Unique identifier
     * @param username Student's login name
     * @param password Student's password
     * @param xpScore  Starting XP (loaded from CSV)
     */
    public Student(int id, String username, String password, int xpScore) {
        // 'super()' calls the PARENT (User) constructor
        // This sets id, username, password, and role
        super(id, username, password, "STUDENT");
        
        // Now set Student-specific fields
        this.xpScore = xpScore;
        this.completedLessonIds = new ArrayList<>();
    }

    // ==================== POLYMORPHISM: OVERRIDE showDashboard() ====================
    
    /**
     * Implementation of the abstract method from User.
     * 
     * For now, we just print a message.
     * Later, this will trigger the GUI to show the student dashboard.
     */
    @Override
    public void showDashboard() {
        System.out.println("=== STUDENT DASHBOARD ===");
        System.out.println("Welcome, " + username + "!");
        System.out.println("Your XP: " + xpScore);
        System.out.println("Courses Completed: " + completedLessonIds.size());
    }

    // ==================== STUDENT-SPECIFIC METHODS ====================
    
    /**
     * Add XP points when student completes a lesson.
     * 
     * @param points The amount of XP to add
     */
    public void addXp(int points) {
        if (points > 0) {
            this.xpScore += points;
            System.out.println("+" + points + " XP! Total: " + xpScore);
        }
    }
    
    /**
     * Mark a lesson as completed.
     * 
     * @param lessonId The ID of the completed lesson
     */
    public void completeLesson(int lessonId) {
        // Only add if not already completed (avoid duplicates)
        if (!completedLessonIds.contains(lessonId)) {
            completedLessonIds.add(lessonId);
        }
    }
    
    /**
     * Check if a specific lesson has been completed.
     * 
     * @param lessonId The lesson ID to check
     * @return true if completed, false otherwise
     */
    public boolean hasCompletedLesson(int lessonId) {
        return completedLessonIds.contains(lessonId);
    }

    // ==================== GETTERS & SETTERS ====================
    
    public int getXpScore() {
        return xpScore;
    }

    public void setXpScore(int xpScore) {
        this.xpScore = xpScore;
    }

    public List<Integer> getCompletedLessonIds() {
        return completedLessonIds;
    }

    public void setCompletedLessonIds(List<Integer> completedLessonIds) {
        this.completedLessonIds = completedLessonIds;
    }

    // ==================== OVERRIDE toString() ====================
    
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", xpScore=" + xpScore +
                ", completedLessons=" + completedLessonIds.size() +
                '}';
    }
}
