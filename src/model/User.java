package model;

/**
 * User - Abstract parent class for all user types.
 * 
 * WHY ABSTRACT?
 * - We never want to create a plain "User" object
 * - Every user must be either a Student OR an Admin
 * - The abstract method showDashboard() FORCES child classes to implement it
 * 
 * ENCAPSULATION:
 * - All fields are 'protected' (accessible to child classes)
 * - We provide public getters/setters for controlled access
 */
public abstract class User {
    
    // ==================== FIELDS ====================
    // 'protected' means: accessible in this class AND child classes
    
    protected int id;
    protected String username;
    protected String password;
    protected String role;  // "STUDENT" or "ADMIN"

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor to initialize a User.
     * 
     * @param id       Unique identifier from CSV
     * @param username User's login name
     * @param password User's password (in real apps, this would be hashed!)
     * @param role     Either "STUDENT" or "ADMIN"
     */
    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // ==================== ABSTRACT METHOD ====================
    
    /**
     * Abstract method that each user type must implement.
     * 
     * WHY ABSTRACT?
     * - A Student sees their courses and XP
     * - An Admin sees management tools
     * - Each role needs a DIFFERENT dashboard!
     * 
     * This is POLYMORPHISM: same method name, different behavior.
     */
    public abstract void showDashboard();

    // ==================== GETTERS & SETTERS ====================
    // These provide controlled access to our private fields
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // ==================== UTILITY METHODS ====================
    
    /**
     * toString() - Useful for debugging.
     * When you print a User object, this is what you'll see.
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
