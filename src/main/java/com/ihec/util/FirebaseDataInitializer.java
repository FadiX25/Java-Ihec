package com.ihec.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.ihec.model.Admin;
import com.ihec.model.Certificate;
import com.ihec.model.Lesson;
import com.ihec.model.SavedCourse;
import com.ihec.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Firebase Data Initialization
 * Initializes Firebase Realtime Database with table structures and sample data
 * Runs automatically on application startup
 */
@Component
@Slf4j
public class FirebaseDataInitializer {

    @Autowired
    private FirebaseDatabase firebaseDatabase;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final long TIMEOUT_SECONDS = 10;

    /**
     * Initialize Firebase data on application startup
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        try {
            log.info("🔥 Starting Firebase database initialization...");
            
            // Check if data already exists
            if (isDatabaseEmpty()) {
                log.info("📦 Database is empty. Creating tables and sample data...");
                
                // Create tables (collections)
                createUserTable();
                createLessonTable();
                createCertificateTable();
                createProgressTable();
                
                log.info("✅ Firebase database initialization completed successfully!");
            } else {
                log.info("ℹ️  Database already contains data. Skipping initialization.");
            }
        } catch (Exception e) {
            log.error("❌ Error initializing Firebase database: " + e.getMessage(), e);
        }
    }

    /**
     * Force re-seed lesson data only.
     */
    public void seedLessons() {
        try {
            log.info("📚 Forcing lesson data seeding...");
            createLessonTable();
        } catch (Exception e) {
            log.error("❌ Error seeding lessons: " + e.getMessage(), e);
        }
    }

    /**
     * Check if database is empty
     */
    private boolean isDatabaseEmpty() {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            boolean[] emptyHolder = new boolean[] { true };

            DatabaseReference usersRef = firebaseDatabase.getReference("users");
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    emptyHolder[0] = !(dataSnapshot.exists() && dataSnapshot.hasChildren());
                    latch.countDown();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    log.error("Error checking database state: " + databaseError.getMessage());
                    latch.countDown();
                }
            });

            latch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return emptyHolder[0];
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Create Users Table with sample data
     */
    private void createUserTable() {
        log.info("Creating Users table...");
        
        DatabaseReference usersRef = firebaseDatabase.getReference("users");
        
        // Admin User
        Admin admin = new Admin();
        admin.setId("admin1");
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123")); // Password: admin123
        admin.setRole("ADMIN");
        usersRef.child("admin1").setValue(admin, (error, ref) -> {
            if (error != null) {
                log.error("Error creating admin: " + error.getMessage());
            } else {
                log.debug("✓ Admin user created");
            }
        });
        
        // Sample Students
        List<Student> students = createSampleStudents();
        for (Student student : students) {
            usersRef.child(student.getId()).setValue(student, (error, ref) -> {
                if (error != null) {
                    log.error("Error creating student: " + error.getMessage());
                } else {
                    log.debug("✓ Student created: " + student.getUsername());
                }
            });
        }
        
        log.info("✅ Users table created with " + (1 + students.size()) + " records");
    }

    /**
     * Create Lesson Table with sample lessons
     */
    private void createLessonTable() {
        log.info("Creating Lessons table...");
        
        DatabaseReference lessonsRef = firebaseDatabase.getReference("lessons");
        
        List<Lesson> lessons = createSampleLessons();
        for (Lesson lesson : lessons) {
            lessonsRef.child(lesson.getId()).setValue(lesson, (error, ref) -> {
                if (error != null) {
                    log.error("Error creating lesson: " + error.getMessage());
                } else {
                    log.debug("✓ Lesson created: " + lesson.getTitle());
                }
            });
        }
        
        log.info("✅ Lessons table created with " + lessons.size() + " records");
    }

    /**
     * Create Certificate Table with sample certificates
     */
    private void createCertificateTable() {
        log.info("Creating Certificates table...");
        
        DatabaseReference certsRef = firebaseDatabase.getReference("certificates");
        
        // Sample certificate 1: OOP Fundamentals
        Certificate cert1 = new Certificate();
        cert1.setId(UUID.randomUUID().toString());
        cert1.setUserId("student1");
        cert1.setCategory("OOP Fundamentals");
        cert1.setCertificateName("OOP Fundamentals Completion");
        cert1.setIssueDate(java.time.LocalDate.now().minusDays(5).toString());
        cert1.setLessonsCompleted(3);
        certsRef.child(cert1.getId()).setValue(cert1, (error, ref) -> {
            if (error != null) log.error("Error creating certificate 1: " + error.getMessage());
        });
        
        // Sample certificate 2: Advanced OOP
        Certificate cert2 = new Certificate();
        cert2.setId(UUID.randomUUID().toString());
        cert2.setUserId("student2");
        cert2.setCategory("OOP Concepts");
        cert2.setCertificateName("Advanced OOP Mastery");
        cert2.setIssueDate(java.time.LocalDate.now().minusDays(3).toString());
        cert2.setLessonsCompleted(5);
        certsRef.child(cert2.getId()).setValue(cert2, (error, ref) -> {
            if (error != null) log.error("Error creating certificate 2: " + error.getMessage());
        });
        
        // Sample certificate 3: Beginner Certificate
        Certificate cert3 = new Certificate();
        cert3.setId(UUID.randomUUID().toString());
        cert3.setUserId("student3");
        cert3.setCategory("OOP Fundamentals");
        cert3.setCertificateName("Java Basics Certificate");
        cert3.setIssueDate(java.time.LocalDate.now().minusDays(1).toString());
        cert3.setLessonsCompleted(2);
        certsRef.child(cert3.getId()).setValue(cert3, (error, ref) -> {
            if (error != null) log.error("Error creating certificate 3: " + error.getMessage());
        });
        
        log.info("✅ Certificates table created with 3 sample records");
    }

    /**
     * Create Student Progress Table
     */
    private void createProgressTable() {
        log.info("Creating Student Progress table...");
        
        DatabaseReference progressRef = firebaseDatabase.getReference("studentProgress");
        
        // Sample progress for student1
        Map<String, Object> progress1 = new HashMap<>();
        progress1.put("studentId", "student1");
        progress1.put("totalXp", 150);
        progress1.put("lessonsCompleted", 3);
        progress1.put("certificatesEarned", 1);
        progress1.put("lastUpdated", LocalDate.now().toString());
        progressRef.child("student1").setValue(progress1, (error, ref) -> {
            if (error != null) log.error("Error creating progress for student1: " + error.getMessage());
        });
        
        // Sample progress for student2
        Map<String, Object> progress2 = new HashMap<>();
        progress2.put("studentId", "student2");
        progress2.put("totalXp", 200);
        progress2.put("lessonsCompleted", 5);
        progress2.put("certificatesEarned", 2);
        progress2.put("lastUpdated", LocalDate.now().toString());
        progressRef.child("student2").setValue(progress2, (error, ref) -> {
            if (error != null) log.error("Error creating progress for student2: " + error.getMessage());
        });
        
        log.info("✅ Student Progress table created");
    }

    /**
     * Create sample student data
     */
    private List<Student> createSampleStudents() {
        List<Student> students = new ArrayList<>();
        
        // Student 1
        Student student1 = new Student();
        student1.setId("student1");
        student1.setUsername("ahmed");
        student1.setPassword(passwordEncoder.encode("password123")); // Password: password123
        student1.setFirstName("Ahmed");
        student1.setLastName("Hassan");
        student1.setEmail("ahmed@example.com");
        student1.setRole("STUDENT");
        student1.setXpScore(150);
        student1.setCompletedLessonIds(List.of("lesson1", "lesson2", "lesson3"));
        student1.setSavedLessonIds(List.of("lesson4", "lesson5"));
        students.add(student1);
        
        // Student 2
        Student student2 = new Student();
        student2.setId("student2");
        student2.setUsername("fatima");
        student2.setPassword(passwordEncoder.encode("password123")); // Password: password123
        student2.setFirstName("Fatima");
        student2.setLastName("Ali");
        student2.setEmail("fatima@example.com");
        student2.setRole("STUDENT");
        student2.setXpScore(200);
        student2.setCompletedLessonIds(List.of("lesson1", "lesson2", "lesson3", "lesson4", "lesson5"));
        student2.setSavedLessonIds(List.of("lesson6"));
        students.add(student2);
        
        // Student 3
        Student student3 = new Student();
        student3.setId("student3");
        student3.setUsername("mohammed");
        student3.setPassword(passwordEncoder.encode("password123")); // Password: password123
        student3.setFirstName("Mohammed");
        student3.setLastName("Ibrahim");
        student3.setEmail("mohammed@example.com");
        student3.setRole("STUDENT");
        student3.setXpScore(75);
        student3.setCompletedLessonIds(List.of("lesson1", "lesson2"));
        student3.setSavedLessonIds(List.of("lesson3", "lesson4", "lesson5"));
        students.add(student3);
        
        return students;
    }

    /**
     * Create sample lesson data
     */
    private List<Lesson> createSampleLessons() {
        List<Lesson> lessons = new ArrayList<>();
        
        // Lesson 1: OOP Basics
        Lesson lesson1 = new Lesson();
        lesson1.setId("lesson1");
        lesson1.setCategory("OOP Fundamentals");
        lesson1.setTitle("Object-Oriented Programming Basics");
        lesson1.setYoutubeId("SiBw7os-_zI");
        lesson1.setDateCreated(LocalDate.now().minusDays(30).toString());
        lesson1.setTheoryText("Object-Oriented Programming (OOP) is a programming paradigm based on the concept of objects...");
        lesson1.setCorrectAnswer("Object");
        lesson1.setDifficulty("BEGINNER");
        lesson1.setXpReward(10);
        lesson1.setQuizQuestion("What does OOP stand for?");
        lesson1.setQuizOptions(List.of(
            "A. Open Operating Program",
            "B. Object-Oriented Programming",
            "C. Object Operating Process",
            "D. Online Object Program"
        ));
        lesson1.setCorrectOption("B");
        lessons.add(lesson1);
        
        // Lesson 2: Classes and Objects
        Lesson lesson2 = new Lesson();
        lesson2.setId("lesson2");
        lesson2.setCategory("OOP Fundamentals");
        lesson2.setTitle("Classes and Objects in Java");
        lesson2.setYoutubeId("IUqKuGNasdM");
        lesson2.setDateCreated(LocalDate.now().minusDays(25).toString());
        lesson2.setTheoryText("A class is a blueprint for creating objects. An object is an instance of a class...");
        lesson2.setCorrectAnswer("class");
        lesson2.setDifficulty("BEGINNER");
        lesson2.setXpReward(15);
        lesson2.setQuizQuestion("What is the purpose of a class in Java?");
        lesson2.setQuizOptions(List.of(
            "A. To store database tables",
            "B. To define object structure and behavior",
            "C. To create networks",
            "D. To execute SQL"
        ));
        lesson2.setCorrectOption("B");
        lessons.add(lesson2);
        
        // Lesson 3: Inheritance
        Lesson lesson3 = new Lesson();
        lesson3.setId("lesson3");
        lesson3.setCategory("OOP Concepts");
        lesson3.setTitle("Inheritance in Java");
        lesson3.setYoutubeId("GTP5lVEKXaU");
        lesson3.setDateCreated(LocalDate.now().minusDays(20).toString());
        lesson3.setTheoryText("Inheritance is a mechanism where a new class is derived from an existing class...");
        lesson3.setCorrectAnswer("extends");
        lesson3.setDifficulty("INTERMEDIATE");
        lesson3.setXpReward(20);
        lesson3.setQuizQuestion("What is inheritance in Java?");
        lesson3.setQuizOptions(List.of(
            "A. Copying files",
            "B. Reusing properties and methods from another class",
            "C. Creating arrays",
            "D. Using databases"
        ));
        lesson3.setCorrectOption("B");
        lessons.add(lesson3);
        
        // Lesson 4: Polymorphism
        Lesson lesson4 = new Lesson();
        lesson4.setId("lesson4");
        lesson4.setCategory("OOP Concepts");
        lesson4.setTitle("Polymorphism in Java");
        lesson4.setYoutubeId("i3aKOm76nDo");
        lesson4.setDateCreated(LocalDate.now().minusDays(15).toString());
        lesson4.setTheoryText("Polymorphism allows objects to take multiple forms...");
        lesson4.setCorrectAnswer("override");
        lesson4.setDifficulty("INTERMEDIATE");
        lesson4.setXpReward(25);
        lesson4.setQuizQuestion("What is polymorphism in Java?");
        lesson4.setQuizOptions(List.of(
            "A. Using many databases",
            "B. One method behaving differently in different situations",
            "C. Creating multiple classes",
            "D. Hiding variables"
        ));
        lesson4.setCorrectOption("B");
        lessons.add(lesson4);
        
        // Lesson 5: Encapsulation
        Lesson lesson5 = new Lesson();
        lesson5.setId("lesson5");
        lesson5.setCategory("OOP Concepts");
        lesson5.setTitle("Encapsulation in Java");
        lesson5.setYoutubeId("3qhoAYfk7cM");
        lesson5.setDateCreated(LocalDate.now().minusDays(10).toString());
        lesson5.setTheoryText("Encapsulation is a mechanism of wrapping data and methods together...");
        lesson5.setCorrectAnswer("private");
        lesson5.setDifficulty("BEGINNER");
        lesson5.setXpReward(15);
        lesson5.setQuizQuestion("What is encapsulation in Java?");
        lesson5.setQuizOptions(List.of(
            "A. Combining data and methods into one unit",
            "B. Creating loops",
            "C. Writing comments",
            "D. Using databases"
        ));
        lesson5.setCorrectOption("A");
        lessons.add(lesson5);
        
        // Lesson 6: Abstraction
        Lesson lesson6 = new Lesson();
        lesson6.setId("lesson6");
        lesson6.setCategory("OOP Concepts");
        lesson6.setTitle("Abstraction in Java");
        lesson6.setYoutubeId("HvPlEJ3LHgE");
        lesson6.setDateCreated(LocalDate.now().minusDays(5).toString());
        lesson6.setTheoryText("Abstraction is a process of hiding the implementation details...");
        lesson6.setCorrectAnswer("abstract");
        lesson6.setDifficulty("INTERMEDIATE");
        lesson6.setXpReward(20);
        lesson6.setQuizQuestion("What is abstraction in Java?");
        lesson6.setQuizOptions(List.of(
            "A. Hiding implementation details and showing essentials",
            "B. Creating databases",
            "C. Writing loops",
            "D. Deleting methods"
        ));
        lesson6.setCorrectOption("A");
        lessons.add(lesson6);
        
        return lessons;
    }
}
