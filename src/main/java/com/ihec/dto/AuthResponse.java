package com.ihec.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ihec.model.User;

/**
 * Authentication Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String message;
    private User user;

    public AuthResponse(String message) {
        this.message = message;
    }

    public AuthResponse(String token, User user) {
        this.token = token;
        this.user = user;
        this.message = "Login successful";
    }
}
