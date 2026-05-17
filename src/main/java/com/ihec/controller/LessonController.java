package com.ihec.controller;

import com.ihec.model.Lesson;
import com.ihec.service.FirebaseLessonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Lesson Controller
 * Handles lesson operations
 */
@RestController
@RequestMapping("/api/lessons")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class LessonController {

    @Autowired
    private FirebaseLessonService lessonService;

    @GetMapping
    public ResponseEntity<List<Lesson>> getAllLessons() {
        try {
            List<Lesson> lessons = lessonService.getAllLessons();
            return ResponseEntity.ok(lessons);
        } catch (Exception e) {
            log.error("Error fetching lessons: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable String lessonId) {
        try {
            Lesson lesson = lessonService.getLessonById(lessonId);
            if (lesson != null) {
                return ResponseEntity.ok(lesson);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching lesson: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Lesson>> getLessonsByCategory(@PathVariable String category) {
        try {
            List<Lesson> lessons = lessonService.getLessonsByCategory(category);
            return ResponseEntity.ok(lessons);
        } catch (Exception e) {
            log.error("Error fetching lessons by category: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createLesson(@RequestBody Lesson lesson) {
        try {
            lessonService.saveLesson(lesson);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Lesson created successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating lesson: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<Map<String, String>> updateLesson(@PathVariable String lessonId, @RequestBody Lesson lesson) {
        try {
            lesson.setId(lessonId);
            lessonService.updateLesson(lessonId, lesson);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Lesson updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating lesson: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Map<String, String>> deleteLesson(@PathVariable String lessonId) {
        try {
            lessonService.deleteLesson(lessonId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Lesson deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error deleting lesson: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/{lessonId}/check-answer")
    public ResponseEntity<Map<String, Object>> checkAnswer(@PathVariable String lessonId, @RequestBody Map<String, String> body) {
        try {
            Lesson lesson = lessonService.getLessonById(lessonId);
            if (lesson == null) {
                return ResponseEntity.notFound().build();
            }

            String userAnswer = body.get("answer");
            boolean isCorrect = lesson.checkAnswer(userAnswer);
            
            Map<String, Object> response = new HashMap<>();
            response.put("correct", isCorrect);
            response.put("xpReward", isCorrect ? lesson.getXpReward() : 0);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error checking answer: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
