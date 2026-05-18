package com.ihec.service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ihec.model.Lesson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Firebase Lesson Service
 * Handles lesson operations with Firebase Realtime Database
 */
@Service
@Slf4j
public class FirebaseLessonService {

    @Autowired
    private FirebaseDatabase firebaseDatabase;

    private static final long TIMEOUT_SECONDS = 10;

    public void saveLesson(Lesson lesson) {
        try {
            DatabaseReference lessonRef = firebaseDatabase.getReference("lessons/" + lesson.getId());
            lessonRef.setValue(lesson, (error, ref) -> {
                if (error != null) {
                    log.error("Error saving lesson: " + error.getMessage());
                } else {
                    log.info("Lesson saved: " + lesson.getId());
                }
            });
        } catch (Exception e) {
            log.error("Error saving lesson: " + e.getMessage());
        }
    }

    public Lesson getLessonById(String lessonId) {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Lesson[] lessonHolder = new Lesson[1];

            DatabaseReference lessonRef = firebaseDatabase.getReference("lessons/" + lessonId);
            lessonRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        lessonHolder[0] = dataSnapshot.getValue(Lesson.class);
                    }
                    latch.countDown();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    log.error("Error reading lesson: " + databaseError.getMessage());
                    latch.countDown();
                }
            });

            latch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return lessonHolder[0];
        } catch (Exception e) {
            log.error("Error getting lesson: " + e.getMessage());
            return null;
        }
    }

    public List<Lesson> getAllLessons() {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            List<Lesson> lessons = new ArrayList<>();

            DatabaseReference lessonsRef = firebaseDatabase.getReference("lessons");
            lessonsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Lesson lesson = snapshot.getValue(Lesson.class);
                        if (lesson != null) {
                            lessons.add(lesson);
                        }
                    }
                    latch.countDown();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    log.error("Error reading lessons: " + databaseError.getMessage());
                    latch.countDown();
                }
            });

            latch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return lessons;
        } catch (Exception e) {
            log.error("Error getting lessons: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Lesson> getLessonsByCategory(String category) {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            List<Lesson> lessons = new ArrayList<>();

            DatabaseReference lessonsRef = firebaseDatabase.getReference("lessons");
            lessonsRef.orderByChild("category").equalTo(category)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Lesson lesson = snapshot.getValue(Lesson.class);
                                if (lesson != null) {
                                    lessons.add(lesson);
                                }
                            }
                            latch.countDown();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            log.error("Error reading lessons: " + databaseError.getMessage());
                            latch.countDown();
                        }
                    });

            latch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return lessons;
        } catch (Exception e) {
            log.error("Error getting lessons by category: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void updateLesson(String lessonId, Lesson lesson) {
        try {
            DatabaseReference lessonRef = firebaseDatabase.getReference("lessons/" + lessonId);
            lessonRef.setValue(lesson, (error, ref) -> {
                if (error != null) {
                    log.error("Error updating lesson: " + error.getMessage());
                } else {
                    log.info("Lesson updated: " + lessonId);
                }
            });
        } catch (Exception e) {
            log.error("Error updating lesson: " + e.getMessage());
        }
    }

    public void deleteLesson(String lessonId) {
        try {
            DatabaseReference lessonRef = firebaseDatabase.getReference("lessons/" + lessonId);
            lessonRef.removeValue((error, ref) -> {
                if (error != null) {
                    log.error("Error deleting lesson: " + error.getMessage());
                } else {
                    log.info("Lesson deleted: " + lessonId);
                }
            });
        } catch (Exception e) {
            log.error("Error deleting lesson: " + e.getMessage());
        }
    }

    public boolean updateLessonYoutubeIds(Map<String, String> lessonVideoIds) {
        try {
            if (lessonVideoIds == null || lessonVideoIds.isEmpty()) {
                return false;
            }

            CountDownLatch latch = new CountDownLatch(lessonVideoIds.size());
            lessonVideoIds.forEach((lessonId, youtubeId) -> {
                DatabaseReference videoRef = firebaseDatabase
                        .getReference("lessons")
                        .child(lessonId)
                        .child("youtubeId");
                videoRef.setValue(youtubeId, (error, ref) -> {
                    if (error != null) {
                        log.error("Error updating lesson video {}: {}", lessonId, error.getMessage());
                    }
                    latch.countDown();
                });
            });

            return latch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error updating lesson videos: " + e.getMessage());
            return false;
        }
    }

    public boolean updateLessonQuizzes(Map<String, Map<String, Object>> lessonQuizData) {
        try {
            if (lessonQuizData == null || lessonQuizData.isEmpty()) {
                return false;
            }

            CountDownLatch latch = new CountDownLatch(lessonQuizData.size());
            lessonQuizData.forEach((lessonId, quizFields) -> {
                DatabaseReference lessonRef = firebaseDatabase
                        .getReference("lessons")
                        .child(lessonId);
                lessonRef.updateChildren(quizFields, (error, ref) -> {
                    if (error != null) {
                        log.error("Error updating lesson quiz {}: {}", lessonId, error.getMessage());
                    }
                    latch.countDown();
                });
            });

            return latch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error updating lesson quizzes: " + e.getMessage());
            return false;
        }
    }
}
