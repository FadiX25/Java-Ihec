package com.ihec.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Lesson class - Represents a single learning module
 * Contains theory, video link, and exercise information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("category")
    private String category;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("youtubeId")
    private String youtubeId;
    
    @JsonProperty("dateCreated")
    private String dateCreated;
    
    @JsonProperty("correctAnswer")
    private String correctAnswer;

    @JsonProperty("quizQuestion")
    private String quizQuestion;

    @JsonProperty("quizOptions")
    private java.util.List<String> quizOptions;

    @JsonProperty("correctOption")
    private String correctOption;
    
    @JsonProperty("theoryText")
    private String theoryText;
    
    @JsonProperty("difficulty")
    private String difficulty = "BEGINNER";
    
    @JsonProperty("xpReward")
    private int xpReward = 10;

    public Lesson(String id, String category, String title, String youtubeId, 
                  String dateCreated, String correctAnswer, String theoryText) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.youtubeId = youtubeId;
        this.dateCreated = dateCreated;
        this.correctAnswer = correctAnswer;
        this.theoryText = theoryText;
        this.difficulty = "BEGINNER";
        this.xpReward = 10;
    }

    public boolean checkAnswer(String userAnswer) {
        if (userAnswer == null || correctAnswer == null) {
            return false;
        }
        return userAnswer.toLowerCase().contains(correctAnswer.toLowerCase());
    }

    public boolean checkAnswerOption(String option) {
        if (option == null || correctOption == null) {
            return false;
        }
        return option.trim().equalsIgnoreCase(correctOption.trim());
    }

    public String getVideoUrl() {
        return "https://www.youtube.com/embed/" + youtubeId;
    }
}
