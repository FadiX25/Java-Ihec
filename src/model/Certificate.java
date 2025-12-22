package model;

import java.time.LocalDate;

/**
 * Certificate - Represents an earned certificate for completing a course/skill.
 * 
 * Certificates are awarded when a student completes all lessons in a category
 * or achieves specific milestones in the learning platform.
 */
public class Certificate {

    // ==================== FIELDS ====================
    
    private int id;
    private int userId;
    private String category;           // The skill category (e.g., "Java", "Python")
    private String certificateName;    // Display name of the certificate
    private LocalDate issueDate;       // When the certificate was earned
    private int lessonsCompleted;      // Number of lessons completed to earn this

    // ==================== CONSTRUCTORS ====================
    
    /**
     * Full constructor for Certificate.
     */
    public Certificate(int id, int userId, String category, String certificateName, 
                       LocalDate issueDate, int lessonsCompleted) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.certificateName = certificateName;
        this.issueDate = issueDate;
        this.lessonsCompleted = lessonsCompleted;
    }
    
    /**
     * Constructor with date as String.
     */
    public Certificate(int id, int userId, String category, String certificateName, 
                       String dateString, int lessonsCompleted) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.certificateName = certificateName;
        this.issueDate = LocalDate.parse(dateString);
        this.lessonsCompleted = lessonsCompleted;
    }

    // ==================== GETTERS & SETTERS ====================
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public int getLessonsCompleted() {
        return lessonsCompleted;
    }

    public void setLessonsCompleted(int lessonsCompleted) {
        this.lessonsCompleted = lessonsCompleted;
    }

    // ==================== OVERRIDE toString() ====================
    
    @Override
    public String toString() {
        return "Certificate{" +
                "id=" + id +
                ", userId=" + userId +
                ", category='" + category + '\'' +
                ", certificateName='" + certificateName + '\'' +
                ", issueDate=" + issueDate +
                '}';
    }
}
