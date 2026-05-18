package com.ihec.controller;

import com.ihec.model.Student;
import com.ihec.model.Lesson;
import com.ihec.service.FirebaseLessonService;
import com.ihec.service.FirebaseUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Student Controller
 * Handles student profile and progress operations
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class StudentController {

    @Autowired
    private FirebaseUserService userService;

    @Autowired
    private FirebaseLessonService lessonService;

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentProfile(@PathVariable String studentId) {
        try {
            var user = userService.getUserById(studentId);
            if (user instanceof Student) {
                return ResponseEntity.ok((Student) user);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching student: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Map<String, String>> updateStudentProfile(@PathVariable String studentId, @RequestBody Student student) {
        try {
            student.setId(studentId);
            userService.updateStudent(studentId, student);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Profile updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating student: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/{studentId}/complete-lesson/{lessonId}")
    public ResponseEntity<Map<String, Object>> completeLesson(@PathVariable String studentId, @PathVariable String lessonId, @RequestBody Map<String, Integer> body) {
        try {
            var user = userService.getUserById(studentId);
            if (user instanceof Student) {
                Lesson lesson = lessonService.getLessonById(lessonId);
                if (lesson == null) {
                    return ResponseEntity.notFound().build();
                }

                Student student = (Student) user;
                int xpReward = lesson.getXpReward();
                student.addXP(xpReward);
                student.completeLesson(lessonId);
                userService.updateStudent(studentId, student);
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Lesson completed");
                response.put("newXP", student.getXpScore());
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error completing lesson: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/{studentId}/save-lesson/{lessonId}")
    public ResponseEntity<Map<String, String>> saveLesson(@PathVariable String studentId, @PathVariable String lessonId) {
        try {
            var user = userService.getUserById(studentId);
            if (user instanceof Student) {
                Student student = (Student) user;
                student.saveLesson(lessonId);
                userService.updateStudent(studentId, student);
                
                Map<String, String> response = new HashMap<>();
                response.put("message", "Lesson saved");
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error saving lesson: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{studentId}/saved-lesson/{lessonId}")
    public ResponseEntity<Map<String, String>> removeSavedLesson(@PathVariable String studentId, @PathVariable String lessonId) {
        try {
            var user = userService.getUserById(studentId);
            if (user instanceof Student) {
                Student student = (Student) user;
                student.removeSavedLesson(lessonId);
                userService.updateStudent(studentId, student);
                
                Map<String, String> response = new HashMap<>();
                response.put("message", "Lesson removed from saved");
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error removing saved lesson: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
