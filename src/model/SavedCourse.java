package model;

import java.time.LocalDate;

/**
 * SavedCourse - Represents a bookmarked/saved course by a user.
 * 
 * Users can save courses they're interested in to access them quickly later.
 */
public class SavedCourse {

    // ==================== FIELDS ====================
    
    private int userId;
    private int lessonId;
    private LocalDate saveDate;

    // ==================== CONSTRUCTORS ====================
    
    /**
     * Full constructor for SavedCourse.
     */
    public SavedCourse(int userId, int lessonId, LocalDate saveDate) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.saveDate = saveDate;
    }
    
    /**
     * Constructor with date as String.
     */
    public SavedCourse(int userId, int lessonId, String dateString) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.saveDate = LocalDate.parse(dateString);
    }
    
    /**
     * Constructor that uses current date.
     */
    public SavedCourse(int userId, int lessonId) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.saveDate = LocalDate.now();
    }

    // ==================== GETTERS & SETTERS ====================
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public LocalDate getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(LocalDate saveDate) {
        this.saveDate = saveDate;
    }

    // ==================== OVERRIDE toString() ====================
    
    @Override
    public String toString() {
        return "SavedCourse{" +
                "userId=" + userId +
                ", lessonId=" + lessonId +
                ", saveDate=" + saveDate +
                '}';
    }
}
