package com.ihec.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Firebase Configuration Class
 * Initializes Firebase Admin SDK for real-time database access
 */
@Configuration
public class FirebaseConfig {

    @Value("${firebase.database.url}")
    private String firebaseDatabaseUrl;

    @Value("${firebase.config.path:classpath:firebase-config.json}")
    private String firebaseConfigPath;

    private final ResourceLoader resourceLoader;

    public FirebaseConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public FirebaseDatabase firebaseDatabase() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            // Load credentials from configured resource path
            Resource resource = resourceLoader.getResource(firebaseConfigPath);
            InputStream serviceAccount = resource.getInputStream();
            
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setDatabaseUrl(firebaseDatabaseUrl)
                    .build();

            FirebaseApp.initializeApp(options);
        }
        return FirebaseDatabase.getInstance();
    }
}
