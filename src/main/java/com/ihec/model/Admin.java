package com.ihec.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Admin class - Represents an admin user
 * Extends User and adds admin-specific fields
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends User {
    
    @JsonProperty("adminLevel")
    private int adminLevel = 1;
    
    @JsonProperty("permissions")
    private String permissions = "ALL";

    public Admin(String id, String username, String password) {
        super(id, username, password, "ADMIN");
        this.adminLevel = 1;
        this.permissions = "ALL";
    }

    public boolean canManageLessons() {
        return permissions.contains("MANAGE_LESSONS");
    }

    public boolean canManageUsers() {
        return permissions.contains("MANAGE_USERS");
    }

    public boolean canViewReports() {
        return permissions.contains("VIEW_REPORTS");
    }
}
