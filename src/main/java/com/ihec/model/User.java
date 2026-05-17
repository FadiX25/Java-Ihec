package com.ihec.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Abstract User class - Base class for all user types
 * Uses Jackson polymorphic serialization for JSON compatibility
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "role"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Student.class, name = "STUDENT"),
    @JsonSubTypes.Type(value = Admin.class, name = "ADMIN")
})
public abstract class User {
    
    @JsonProperty("id")
    protected String id;
    
    @JsonProperty("username")
    protected String username;
    
    @JsonProperty("password")
    protected String password;
    
    @JsonProperty("role")
    protected String role;

    public User(String id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
