package model;

import java.time.LocalDate;

/**
 * Lesson - Represents a single learning module.
 * 
 * ENCAPSULATION EXAMPLE:
 * - ALL fields are PRIVATE (cannot be accessed directly from outside)
 * - Access is controlled through public getters and setters
 * - The checkAnswer() method encapsulates the grading logic
 * 
 * CSV COLUMNS: id, title, youtube_id, date_created, correct_answer_keyword, theory_text
 */
public class Lesson {

    // ==================== PRIVATE FIELDS (ENCAPSULATION) ====================
    
    private int id;
    private String title;
    private String youtubeId;          // YouTube video ID (e.g., "Hl-zzrqQoSE")
    private LocalDate dateCreated;     // Using Java's modern date API
    private String correctAnswer;      // The keyword to check in student's code
    private String theoryText;         // The lesson content/theory

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Full constructor for creating a Lesson.
     * 
     * @param id            Unique lesson identifier
     * @param title         Lesson title displayed to user
     * @param youtubeId     YouTube video ID for the tutorial
     * @param dateCreated   When the lesson was created
     * @param correctAnswer The keyword that must appear in student's answer
     * @param theoryText    The theory/explanation text
     */
    public Lesson(int id, String title, String youtubeId, 
                  LocalDate dateCreated, String correctAnswer, String theoryText) {
        this.id = id;
        this.title = title;
        this.youtubeId = youtubeId;
        this.dateCreated = dateCreated;
        this.correctAnswer = correctAnswer;
        this.theoryText = theoryText;
    }
    
    /**
     * Alternative constructor that accepts date as String.
     * Useful when reading from CSV where dates are stored as text.
     * 
     * @param dateString Date in format "yyyy-MM-dd" (e.g., "2023-10-01")
     */
    public Lesson(int id, String title, String youtubeId, 
                  String dateString, String correctAnswer, String theoryText) {
        this.id = id;
        this.title = title;
        this.youtubeId = youtubeId;
        // LocalDate.parse() converts "2023-10-01" to a LocalDate object
        this.dateCreated = LocalDate.parse(dateString);
        this.correctAnswer = correctAnswer;
        this.theoryText = theoryText;
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
                ", title='" + title + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
