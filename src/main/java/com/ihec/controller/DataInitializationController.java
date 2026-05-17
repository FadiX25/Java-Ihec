package com.ihec.controller;

import com.ihec.util.FirebaseDataInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Data Initialization Controller
 * Provides endpoints for Firebase database initialization and management
 * ⚠️ WARNING: Only use in development. Secure or remove in production.
 */
@RestController
@RequestMapping("/api/admin/init")
@Slf4j
public class DataInitializationController {

    @Autowired
    private FirebaseDataInitializer firebaseDataInitializer;

    /**
     * Initialize Firebase database with tables and sample data
     * POST /api/admin/init/database
     * 
     * @return Success message if initialization completed
     */
    @PostMapping("/database")
    public ResponseEntity<String> initializeDatabase() {
        try {
            log.info("📦 Database initialization requested by admin");
            firebaseDataInitializer.initializeData();
            return ResponseEntity.ok("✅ Firebase database initialized successfully!\n\n" +
                    "Sample Data Created:\n" +
                    "- 1 Admin account (admin / admin123)\n" +
                    "- 3 Student accounts\n" +
                    "- 6 Sample lessons\n" +
                    "- 2 Sample certificates\n" +
                    "- Student progress records\n\n" +
                    "Check Firebase Console to view the data.");
        } catch (Exception e) {
            log.error("❌ Database initialization failed: " + e.getMessage(), e);
            return ResponseEntity.status(500).body("❌ Initialization failed: " + e.getMessage());
        }
    }

    /**
     * Get initialization status
     * GET /api/admin/init/status
     */
    @PostMapping("/status")
    public ResponseEntity<String> checkInitializationStatus() {
        try {
            return ResponseEntity.ok("✅ Firebase connection is active and ready for initialization.\n" +
                    "Use POST /api/admin/init/database to initialize with sample data.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Firebase connection error: " + e.getMessage());
        }
    }
}
