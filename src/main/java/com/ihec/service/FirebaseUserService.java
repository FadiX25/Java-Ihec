package com.ihec.service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ihec.model.Admin;
import com.ihec.model.Student;
import com.ihec.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Firebase User Service
 * Handles user operations with Firebase Realtime Database
 */
@Service
@Slf4j
public class FirebaseUserService {

    @Autowired
    private FirebaseDatabase firebaseDatabase;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final long TIMEOUT_SECONDS = 10;

    public void saveUser(User user) {
        try {
            DatabaseReference usersRef = firebaseDatabase.getReference("users/" + user.getId());
            usersRef.setValue(user);
            log.info("User saved: " + user.getId());
        } catch (Exception e) {
            log.error("Error saving user: " + e.getMessage());
        }
    }

    public User getUserById(String userId) {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            User[] userHolder = new User[1];

            DatabaseReference userRef = firebaseDatabase.getReference("users/" + userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String role = dataSnapshot.child("role").getValue(String.class);
                        if ("STUDENT".equals(role)) {
                            userHolder[0] = dataSnapshot.getValue(Student.class);
                        } else if ("ADMIN".equals(role)) {
                            userHolder[0] = dataSnapshot.getValue(Admin.class);
                        }
                    }
                    latch.countDown();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    log.error("Error reading user: " + databaseError.getMessage());
                    latch.countDown();
                }
            });

            latch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return userHolder[0];
        } catch (Exception e) {
            log.error("Error getting user: " + e.getMessage());
            return null;
        }
    }

    public User getUserByUsername(String username) {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            User[] userHolder = new User[1];

            DatabaseReference usersRef = firebaseDatabase.getReference("users");
            usersRef.orderByChild("username").equalTo(username)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String role = snapshot.child("role").getValue(String.class);
                                if ("STUDENT".equals(role)) {
                                    userHolder[0] = snapshot.getValue(Student.class);
                                } else if ("ADMIN".equals(role)) {
                                    userHolder[0] = snapshot.getValue(Admin.class);
                                }
                            }
                            latch.countDown();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            log.error("Error reading users: " + databaseError.getMessage());
                            latch.countDown();
                        }
                    });

            latch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return userHolder[0];
        } catch (Exception e) {
            log.error("Error getting user by username: " + e.getMessage());
            return null;
        }
    }

    public void createStudent(Student student) {
        try {
            // Hash password before saving
            student.setPassword(passwordEncoder.encode(student.getPassword()));
            saveUser(student);
        } catch (Exception e) {
            log.error("Error creating student: " + e.getMessage());
        }
    }

    public void updateStudent(String userId, Student student) {
        try {
            DatabaseReference studentRef = firebaseDatabase.getReference("users/" + userId);
            studentRef.setValue(student);
            log.info("Student updated: " + userId);
        } catch (Exception e) {
            log.error("Error updating student: " + e.getMessage());
        }
    }

    public boolean authenticateUser(String username, String password) {
        try {
            User user = getUserByUsername(username);
            if (user != null) {
                return passwordEncoder.matches(password, user.getPassword());
            }
            return false;
        } catch (Exception e) {
            log.error("Error authenticating user: " + e.getMessage());
            return false;
        }
    }

    public void deleteUser(String userId) {
        try {
            DatabaseReference userRef = firebaseDatabase.getReference("users/" + userId);
            userRef.removeValue();
            log.info("User deleted: " + userId);
        } catch (Exception e) {
            log.error("Error deleting user: " + e.getMessage());
        }
    }
}
