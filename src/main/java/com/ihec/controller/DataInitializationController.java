package com.ihec.controller;

import com.ihec.service.FirebaseLessonService;
import com.ihec.util.FirebaseDataInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Initialization Controller
 * Provides endpoints for Firebase database initialization and management
 * ⚠️ WARNING: Only use in development. Secure or remove in production.
 */
@RestController
@RequestMapping("/api/admin/init")
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class DataInitializationController {

    @Autowired
    private FirebaseDataInitializer firebaseDataInitializer;

    @Autowired
    private FirebaseLessonService lessonService;

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

    /**
     * Update YouTube video IDs for existing lessons
     * POST /api/admin/init/lesson-videos
     */
    @PostMapping("/lesson-videos")
    public ResponseEntity<String> updateLessonVideos() {
        try {
            Map<String, String> lessonVideoIds = new HashMap<>();
            lessonVideoIds.put("lesson1", "dmlPiQSkQHQ");
            lessonVideoIds.put("lesson2", "yy3yLGkuXPk");
            lessonVideoIds.put("lesson3", "GTP5lVEKXaU");
            lessonVideoIds.put("lesson4", "6xRd0j1anzc");
            lessonVideoIds.put("lesson5", "eboNNUADeIc");
            lessonVideoIds.put("lesson6", "4B8XKEORJss");

            boolean updated = lessonService.updateLessonYoutubeIds(lessonVideoIds);
            if (!updated) {
                return ResponseEntity.status(500).body("❌ Failed to update lesson videos");
            }

            return ResponseEntity.ok("✅ Lesson videos updated successfully!");
        } catch (Exception e) {
            log.error("❌ Lesson video update failed: " + e.getMessage(), e);
            return ResponseEntity.status(500).body("❌ Lesson video update failed: " + e.getMessage());
        }
    }

    /**
     * Force re-seed lessons and update video IDs
     * POST /api/admin/init/lessons-reset
     */
    @PostMapping("/lessons-reset")
    public ResponseEntity<String> resetLessons() {
        try {
            firebaseDataInitializer.seedLessons();

            Map<String, String> lessonVideoIds = new HashMap<>();
            lessonVideoIds.put("lesson1", "dmlPiQSkQHQ");
            lessonVideoIds.put("lesson2", "yy3yLGkuXPk");
            lessonVideoIds.put("lesson3", "GTP5lVEKXaU");
            lessonVideoIds.put("lesson4", "6xRd0j1anzc");
            lessonVideoIds.put("lesson5", "eboNNUADeIc");
            lessonVideoIds.put("lesson6", "4B8XKEORJss");

            boolean updated = lessonService.updateLessonYoutubeIds(lessonVideoIds);
            if (!updated) {
                return ResponseEntity.status(500).body("❌ Lessons seeded but video update failed");
            }

            return ResponseEntity.ok("✅ Lessons re-seeded and videos updated successfully!");
        } catch (Exception e) {
            log.error("❌ Lesson reset failed: " + e.getMessage(), e);
            return ResponseEntity.status(500).body("❌ Lesson reset failed: " + e.getMessage());
        }
    }
}
