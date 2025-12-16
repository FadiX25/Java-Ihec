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
 * CSV COLUMNS: id, title, youtube_id, date_created, correct_answer_keyword, theory_text, duration, difficulty, category, tags
 */
public class Lesson {

    // ==================== PRIVATE FIELDS (ENCAPSULATION) ====================

    private int id;
    private String title;
    private String youtubeId;          // YouTube video ID (e.g., "Hl-zzrqQoSE")
    private LocalDate dateCreated;     // Using Java's modern date API
    private String correctAnswer;      // The keyword to check in student's code
    private String theoryText;         // The lesson content/theory
    private int duration;              // Durée estimée en heures (valeur par défaut 25)
    private String difficulty;         // Niveau de difficulté
    private String category;           // Catégorie du cours
    private String tags;               // Mots-clés associés

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
     * @param duration      Durée estimée en heures
     * @param difficulty    Niveau de difficulté
     * @param category      Catégorie du cours
     * @param tags          Mots-clés associés
     */
    public Lesson(int id, String title, String youtubeId,
                  LocalDate dateCreated, String correctAnswer, String theoryText,
                  int duration, String difficulty, String category, String tags) {
        this.id = id;
        this.title = title;
        this.youtubeId = youtubeId;
        this.dateCreated = dateCreated;
        this.correctAnswer = correctAnswer;
        this.theoryText = theoryText;
        this.duration = duration;
        this.difficulty = difficulty;
        this.category = category;
        this.tags = tags;
    }

    /**
     * Constructor with default values for duration and other fields.
     * For backward compatibility.
     */
    public Lesson(int id, String title, String youtubeId,
                  LocalDate dateCreated, String correctAnswer, String theoryText) {
        this(id, title, youtubeId, dateCreated, correctAnswer, theoryText,
                25, "Intermédiaire", "Java", "");
    }

    /**
     * Alternative constructor that accepts date as String.
     * Useful when reading from CSV where dates are stored as text.
     *
     * @param dateString Date in format "yyyy-MM-dd" (e.g., "2023-10-01")
     */
    public Lesson(int id, String title, String youtubeId,
                  String dateString, String correctAnswer, String theoryText,
                  int duration, String difficulty, String category, String tags) {
        this.id = id;
        this.title = title;
        this.youtubeId = youtubeId;
        // LocalDate.parse() converts "2023-10-01" to a LocalDate object
        this.dateCreated = LocalDate.parse(dateString);
        this.correctAnswer = correctAnswer;
        this.theoryText = theoryText;
        this.duration = duration;
        this.difficulty = difficulty;
        this.category = category;
        this.tags = tags;
    }

    /**
     * Simplified constructor for backward compatibility.
     */
    public Lesson(int id, String title, String youtubeId,
                  String dateString, String correctAnswer, String theoryText) {
        this(id, title, youtubeId, dateString, correctAnswer, theoryText,
                25, "Intermédiaire", "Java", "");
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    // ==================== OVERRIDE toString() ====================

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", duration=" + duration + "h" +
                ", difficulty='" + difficulty + '\'' +
                ", category='" + category + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }
}