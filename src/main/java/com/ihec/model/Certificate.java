package com.ihec.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

/**
 * Certificate class - Represents a student's earned certificate
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("userId")
    private String userId;
    
    @JsonProperty("category")
    private String category;
    
    @JsonProperty("certificateName")
    private String certificateName;
    
    @JsonProperty("issueDate")
    private LocalDate issueDate;
    
    @JsonProperty("lessonsCompleted")
    private int lessonsCompleted;

    public Certificate(String id, String userId, String category, String certificateName) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.certificateName = certificateName;
        this.issueDate = LocalDate.now();
        this.lessonsCompleted = 0;
    }
}
