package com.ihec.controller;

import com.ihec.service.FirebaseLessonService;
import com.ihec.util.FirebaseDataInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private FirebaseLessonService lessonService;

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

        @PostMapping("/lesson-quizzes")
        public ResponseEntity<String> publicUpdateLessonQuizzes() {
        try {
            log.warn("Public lesson quiz update triggered - dev-only endpoint");
            boolean updated = lessonService.updateLessonQuizzes(buildLessonQuizData());
            if (!updated) {
            return ResponseEntity.status(500).body("❌ Quiz update failed");
            }
            return ResponseEntity.ok("✅ Lesson quizzes updated (dev public endpoint).");
        } catch (Exception e) {
            log.error("Public quiz update failed: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("❌ Quiz update failed: " + e.getMessage());
        }
        }

        private Map<String, Map<String, Object>> buildLessonQuizData() {
        Map<String, Map<String, Object>> quizData = new HashMap<>();

        quizData.put("lesson1", Map.of(
            "quizQuestion", "What does OOP stand for?",
            "quizOptions", List.of(
                "A. Open Operating Program",
                "B. Object-Oriented Programming",
                "C. Object Operating Process",
                "D. Online Object Program"
            ),
            "correctOption", "B"
        ));

        quizData.put("lesson2", Map.of(
            "quizQuestion", "What is the purpose of a class in Java?",
            "quizOptions", List.of(
                "A. To store database tables",
                "B. To define object structure and behavior",
                "C. To create networks",
                "D. To execute SQL"
            ),
            "correctOption", "B"
        ));

        quizData.put("lesson3", Map.of(
            "quizQuestion", "What is inheritance in Java?",
            "quizOptions", List.of(
                "A. Copying files",
                "B. Reusing properties and methods from another class",
                "C. Creating arrays",
                "D. Using databases"
            ),
            "correctOption", "B"
        ));

        quizData.put("lesson4", Map.of(
            "quizQuestion", "What is polymorphism in Java?",
            "quizOptions", List.of(
                "A. Using many databases",
                "B. One method behaving differently in different situations",
                "C. Creating multiple classes",
                "D. Hiding variables"
            ),
            "correctOption", "B"
        ));

        quizData.put("lesson5", Map.of(
            "quizQuestion", "What is encapsulation in Java?",
            "quizOptions", List.of(
                "A. Combining data and methods into one unit",
                "B. Creating loops",
                "C. Writing comments",
                "D. Using databases"
            ),
            "correctOption", "A"
        ));

        quizData.put("lesson6", Map.of(
            "quizQuestion", "What is abstraction in Java?",
            "quizOptions", List.of(
                "A. Hiding implementation details and showing essentials",
                "B. Creating databases",
                "C. Writing loops",
                "D. Deleting methods"
            ),
            "correctOption", "A"
        ));

        return quizData;
        }
}

