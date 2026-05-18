package com.ihec.controller;

import com.ihec.dto.AuthRequest;
import com.ihec.dto.AuthResponse;
import com.ihec.model.Student;
import com.ihec.model.User;
import com.ihec.security.JwtUtil;
import com.ihec.service.FirebaseUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


/**
 * Authentication Controller
 * Handles user login and registration
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class AuthController {

    @Autowired
    private FirebaseUserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            User user = userService.getUserByUsername(authRequest.getUsername());
            
            if (user != null && userService.authenticateUser(authRequest.getUsername(), authRequest.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
                return ResponseEntity.ok(new AuthResponse(token, user));
            } else {
                return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials"));
            }
        } catch (Exception e) {
            log.error("Login error: " + e.getMessage());
            return ResponseEntity.status(500).body(new AuthResponse("Login failed"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody Student student) {
        try {
            // Check if user already exists
            User existingUser = userService.getUserByUsername(student.getUsername());
            if (existingUser != null) {
                return ResponseEntity.status(400).body(new AuthResponse("User already exists"));
            }

            // Create new student
            student.setId(UUID.randomUUID().toString());
            student.setRole("STUDENT");
            userService.createStudent(student);

            String token = jwtUtil.generateToken(student.getUsername(), student.getRole());
            return ResponseEntity.ok(new AuthResponse(token, student));
        } catch (Exception e) {
            log.error("Registration error: " + e.getMessage());
            return ResponseEntity.status(500).body(new AuthResponse("Registration failed"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout() {
        return ResponseEntity.ok(new AuthResponse("Logout successful"));
    }
}
