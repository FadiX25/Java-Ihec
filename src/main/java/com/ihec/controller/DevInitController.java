package com.ihec.controller;

import com.ihec.util.FirebaseDataInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Development-only initialization controller.
 * Provides a public endpoint to seed the database if empty. ONLY for dev/test environments.
 */
@RestController
@RequestMapping("/api/public/init")
@Slf4j
public class DevInitController {

    @Autowired
    private FirebaseDataInitializer firebaseDataInitializer;

    @PostMapping("/database")
    public ResponseEntity<String> publicInitializeDatabase() {
        try {
            log.warn("Public DB init triggered - dev-only endpoint");
            firebaseDataInitializer.initializeData();
            return ResponseEntity.ok("✅ Firebase database initialization attempted (dev public endpoint).");
        } catch (Exception e) {
            log.error("Public initialization failed: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("❌ Initialization failed: " + e.getMessage());
        }
    }
}

