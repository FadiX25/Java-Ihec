package com.ihec.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Data Migration Utility
 * DEPRECATED: Use FirebaseDataInitializer instead for automatic database initialization.
 * This class is kept for backward compatibility only.
 */
@Component
@Slf4j
public class DataMigrationUtil {

    /**
     * Migrate all data from CSV files to Firebase
     * NOTE: This is a placeholder. Use FirebaseDataInitializer for automatic initialization.
     */
    public void migrateAllData() {
        try {
            log.info("??  Using FirebaseDataInitializer instead of CSV migration...");
            log.info("? Firebase database will be automatically initialized on startup.");
        } catch (Exception e) {
            log.error("Error during data migration: " + e.getMessage(), e);
        }
    }
}
