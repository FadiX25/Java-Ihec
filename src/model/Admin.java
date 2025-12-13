package model;

/**
 * Admin - Represents an administrator who manages content.
 * 
 * INHERITANCE:
 * - 'extends User' means Admin IS-A User
 * - Admins can add/manage lessons (content management)
 * 
 * NOTE: Admin is simpler than Student because admins don't
 * have XP or progress tracking - they just manage content.
 */
public class Admin extends User {

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor for Admin.
     * 
     * @param id       Unique identifier
     * @param username Admin's login name
     * @param password Admin's password
     */
    public Admin(int id, String username, String password) {
        // Call parent constructor with role = "ADMIN"
        super(id, username, password, "ADMIN");
    }

    // ==================== POLYMORPHISM: OVERRIDE showDashboard() ====================
    
    /**
     * Implementation of the abstract method from User.
     * Admins see a different dashboard than students.
     */
    @Override
    public void showDashboard() {
        System.out.println("=== ADMIN DASHBOARD ===");
        System.out.println("Welcome, Administrator " + username + "!");
        System.out.println("You can manage lessons and view statistics.");
    }

    // ==================== ADMIN-SPECIFIC METHODS ====================
    
    /**
     * Add a new lesson to the system.
     * 
     * In a real implementation, this would call CsvDataManager
     * to save the lesson to lessons.csv.
     * 
     * @param lesson The lesson to add
     */
    public void addLesson(Lesson lesson) {
        // For now, just print confirmation
        // Later, this will integrate with CsvDataManager
        System.out.println("Lesson added: " + lesson.getTitle());
    }

    // ==================== OVERRIDE toString() ====================
    
    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
