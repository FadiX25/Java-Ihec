package com.ihec.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Student class - Represents a student user
 * Extends User and adds student-specific fields
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {
    
    @JsonProperty("firstName")
    private String firstName;
    
    @JsonProperty("lastName")
    private String lastName;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("bio")
    private String bio;
    
    @JsonProperty("xpScore")
    private int xpScore = 0;
    
    @JsonProperty("completedLessonIds")
    private List<String> completedLessonIds = new ArrayList<>();
    
    @JsonProperty("savedLessonIds")
    private List<String> savedLessonIds = new ArrayList<>();

    public Student(String id, String username, String password, String firstName, String lastName, String email) {
        super(id, username, password, "STUDENT");
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.xpScore = 0;
        this.completedLessonIds = new ArrayList<>();
        this.savedLessonIds = new ArrayList<>();
    }

    public void addXP(int points) {
        this.xpScore += points;
    }

    public void completeLesson(String lessonId) {
        if (!this.completedLessonIds.contains(lessonId)) {
            this.completedLessonIds.add(lessonId);
        }
    }

    public void saveLesson(String lessonId) {
        if (!this.savedLessonIds.contains(lessonId)) {
            this.savedLessonIds.add(lessonId);
        }
    }

    public void removeSavedLesson(String lessonId) {
        this.savedLessonIds.remove(lessonId);
    }
}
