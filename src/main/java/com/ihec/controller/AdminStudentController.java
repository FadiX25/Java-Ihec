package com.ihec.controller;

import com.ihec.model.Student;
import com.ihec.service.FirebaseUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Admin Student Controller
 * Admin-only endpoints to list and manage students
 */
@RestController
@RequestMapping("/api/admin/students")
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminStudentController {

    @Autowired
    private FirebaseUserService userService;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        try {
            return ResponseEntity.ok(userService.getAllStudents());
        } catch (Exception e) {
            log.error("Error fetching students: " + e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable String studentId) {
        try {
            userService.deleteUser(studentId);
            return ResponseEntity.ok("Student deleted");
        } catch (Exception e) {
            log.error("Error deleting student: " + e.getMessage(), e);
            return ResponseEntity.status(500).body("Delete failed");
        }
    }
}
