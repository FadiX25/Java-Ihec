package com.ihec.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

/**
 * SavedCourse class - Represents a student's saved/bookmarked lesson
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedCourse {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("userId")
    private String userId;
    
    @JsonProperty("lessonId")
    private String lessonId;
    
    @JsonProperty("saveDate")
    private LocalDate saveDate;

    public SavedCourse(String userId, String lessonId) {
        this.id = userId + "_" + lessonId;
        this.userId = userId;
        this.lessonId = lessonId;
        this.saveDate = LocalDate.now();
    }
}
