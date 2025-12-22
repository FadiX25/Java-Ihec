package model;

import java.time.LocalDate;

/**
 * Lesson - Represents a single learning module/skill.
 * 
 * ENCAPSULATION EXAMPLE:
 * - ALL fields are PRIVATE (cannot be accessed directly from outside)
 * - Access is controlled through public getters and setters
 * - The checkAnswer() method encapsulates the grading logic
 * 
 * CSV COLUMNS: id, category, title, youtube_id, date_created, correct_answer_keyword, theory_text
 */
public class Lesson {

    // ==================== PRIVATE FIELDS (ENCAPSULATION) ====================
    
    private int id;
    private String category;           // Skill domain (e.g., "Java", "Python", "Web Dev")
    private String title;
    private String youtubeId;          // YouTube video ID (e.g., "Hl-zzrqQoSE")
    private LocalDate dateCreated;     // Using Java's modern date API
    private String correctAnswer;      // The keyword to check in student's code
    private String theoryText;         // The lesson content/theory

    // ==================== CONSTRUCTORS ====================
    
    /**
     * Full constructor for creating a Lesson with category.
     */
    public Lesson(int id, String category, String title, String youtubeId, 
                  LocalDate dateCreated, String correctAnswer, String theoryText) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.youtubeId = youtubeId;
        this.dateCreated = dateCreated;
        this.correctAnswer = correctAnswer;
        this.theoryText = theoryText;
    }
    
    /**
     * Constructor with date as String and category.
     */
    public Lesson(int id, String category, String title, String youtubeId, 
                  String dateString, String correctAnswer, String theoryText) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.youtubeId = youtubeId;
        this.dateCreated = LocalDate.parse(dateString);
        this.correctAnswer = correctAnswer;
        this.theoryText = theoryText;
    }
    
    /**
     * Legacy constructor without category (defaults to "Java").
     */
    public Lesson(int id, String title, String youtubeId, 
                  LocalDate dateCreated, String correctAnswer, String theoryText) {
        this(id, "Java", title, youtubeId, dateCreated, correctAnswer, theoryText);
    }
    
    /**
     * Legacy constructor with date as String without category (defaults to "Java").
     */
    public Lesson(int id, String title, String youtubeId, 
                  String dateString, String correctAnswer, String theoryText) {
        this(id, "Java", title, youtubeId, dateString, correctAnswer, theoryText);
    }

    // ==================== BUSINESS LOGIC ====================
    
    /**
     * Check if the student's answer is correct.
     * 
     * ENCAPSULATION: The grading logic is hidden inside this method.
     * Outside code doesn't need to know HOW we check - they just call this.
     * 
     * @param userInput The code/text entered by the student
     * @return true if the answer contains the correct keyword
     */
    public boolean checkAnswer(String userInput) {
        if (userInput == null || userInput.isEmpty()) {
            return false;
        }
        // Check if user's input contains the correct keyword
        // Using toLowerCase() for case-insensitive matching
        return userInput.toLowerCase().contains(correctAnswer.toLowerCase());
    }
    
    /**
     * Get the full YouTube URL for this lesson's video.
     * 
     * @return Complete YouTube URL
     */
    public String getYoutubeUrl() {
        return "https://www.youtube.com/watch?v=" + youtubeId;
    }

    // ==================== GETTERS & SETTERS ====================
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getTheoryText() {
        return theoryText;
    }

    public void setTheoryText(String theoryText) {
        this.theoryText = theoryText;
    }

    // ==================== OVERRIDE toString() ====================
    
    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
