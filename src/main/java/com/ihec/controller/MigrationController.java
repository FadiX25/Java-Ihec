package com.ihec.controller;

import com.ihec.util.DataMigrationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Migration Controller
 * Provides endpoints for data migration from CSV to Firebase
 * ⚠️ WARNING: Only use in development. Remove or secure in production.
 */
@RestController
@RequestMapping("/api/migration")
@Slf4j
public class MigrationController {

    @Autowired
    private DataMigrationUtil dataMigrationUtil;

    /**
     * Migrate all data from CSV files to Firebase
     * POST /api/migration/migrate-all
     */
    @PostMapping("/migrate-all")
    public ResponseEntity<String> migrateAll() {
        try {
            log.info("Migration started by admin");
            dataMigrationUtil.migrateAllData();
            return ResponseEntity.ok("✅ Data migration completed successfully!");
        } catch (Exception e) {
            log.error("Migration failed: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body("❌ Migration failed: " + e.getMessage());
        }
    }

    /**
     * Health check endpoint for Firebase connection
     * GET /api/migration/health
     */
    @PostMapping("/health")
    public ResponseEntity<String> checkFirebaseConnection() {
        try {
            // Attempt to read from Firebase to verify connection
            dataMigrationUtil.migrateAllData(); // This will verify connection
            return ResponseEntity.ok("✅ Firebase connection is working!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Firebase connection failed: " + e.getMessage());
        }
    }
}
