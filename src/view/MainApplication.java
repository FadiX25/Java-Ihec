package view;

import model.Lesson;
import model.User;
import utils.StyleUtils;

import javax.swing.*;
import java.awt.*;

/**
 * MainApplication - The main entry point and navigation controller.
 * 
 * KEY CONCEPTS:
 * 
 * 1. CARDLAYOUT:
 *    - Think of it like a deck of cards stacked on top of each other
 *    - Only ONE card (screen) is visible at a time
 *    - We can "flip" to any card by name
 *    - Perfect for switching between Login, Dashboard, and Learning views!
 * 
 * 2. JFRAME:
 *    - The main window of a desktop application
 *    - Contains all other components (panels, buttons, etc.)
 *    - setDefaultCloseOperation() controls what happens when you click X
 * 
 * 3. SINGLETON PATTERN (simplified):
 *    - We only want ONE MainApplication window
 *    - All views get a reference to this main window
 *    - Views can ask MainApplication to switch screens
 */
public class MainApplication extends JFrame {

    // ==================== CONSTANTS (Screen Names) ====================
    // These are the "names" of our cards in the CardLayout
    
    private static final String LOGIN_SCREEN = "LOGIN";
    private static final String DASHBOARD_SCREEN = "DASHBOARD";
    private static final String LEARNING_SCREEN = "LEARNING";

    // ==================== LAYOUT & PANELS ====================
    
    /** CardLayout lets us switch between screens */
    private CardLayout cardLayout;
    
    /** The main container that holds all our screens */
    private JPanel mainPanel;

    // ==================== VIEW REFERENCES ====================
    // We keep references so we can update/refresh them
    
    private LoginView loginView;
    private DashboardView dashboardView;
    private LearningView learningView;

    // ==================== USER STATE ====================
    
    /** The currently logged-in user (null if not logged in) */
    private User currentUser;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Create the main application window.
     */
    public MainApplication() {
        // Set up the window
        setupWindow();
        
        // Initialize the screens
        initializeViews();
        
        // Start on the login screen
        showLogin();
    }

    // ==================== WINDOW SETUP ====================
    
    /**
     * Configure the main window properties.
     */
    private void setupWindow() {
        // Window title (shown in title bar)
        setTitle("IHEC-JLearn - Learn Java Programming");
        
        // Window size
        setSize(1200, 800);
        
        // Center on screen
        setLocationRelativeTo(null);
        
        // What happens when user clicks X?
        // EXIT_ON_CLOSE = close the app completely
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Minimum size (can't resize smaller than this)
        setMinimumSize(new Dimension(800, 600));
        
        // Use the system look and feel for a native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // If it fails, just use the default look
            System.out.println("Could not set system look and feel: " + e.getMessage());
        }
    }

    // ==================== VIEW INITIALIZATION ====================
    
    /**
     * Create all the view panels and add them to the CardLayout.
     */
    private void initializeViews() {
        // Create the CardLayout and main container
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(StyleUtils.BACKGROUND_GRAY);
        
        // Create each view, passing 'this' so they can communicate back
        loginView = new LoginView(this);
        dashboardView = new DashboardView(this);
        learningView = new LearningView(this);
        
        // Add each view as a "card" with a name
        // The name is how we'll switch to that card later
        mainPanel.add(loginView, LOGIN_SCREEN);
        mainPanel.add(dashboardView, DASHBOARD_SCREEN);
        mainPanel.add(learningView, LEARNING_SCREEN);
        
        // Add the main panel to the window
        add(mainPanel);
    }

    // ==================== NAVIGATION METHODS ====================
    // These methods are called by the views to switch screens
    
    /**
     * Show the login screen.
     */
    public void showLogin() {
        cardLayout.show(mainPanel, LOGIN_SCREEN);
    }
    
    /**
     * Show the dashboard screen.
     */
    public void showDashboard() {
        // Refresh the dashboard with current user data
        if (currentUser != null) {
            dashboardView.refreshForUser(currentUser);
        }
        cardLayout.show(mainPanel, DASHBOARD_SCREEN);
    }
    
    /**
     * Show the learning screen with a specific lesson.
     * 
     * @param lesson The lesson to display
     */
    public void showLearning(Lesson lesson) {
        learningView.loadLesson(lesson);
        cardLayout.show(mainPanel, LEARNING_SCREEN);
    }

    // ==================== USER SESSION MANAGEMENT ====================
    
    /**
     * Called by LoginView when login is successful.
     * 
     * @param user The authenticated user
     */
    public void onLoginSuccess(User user) {
        this.currentUser = user;
        
        // Show a welcome message (optional)
        System.out.println("Welcome, " + user.getUsername() + "!");
        
        // Navigate to dashboard
        showDashboard();
    }
    
    /**
     * Log out the current user and return to login.
     */
    public void logout() {
        this.currentUser = null;
        showLogin();
    }
    
    /**
     * Open a specific lesson.
     * Called when a course card is clicked in the dashboard.
     * 
     * @param lesson The lesson to open
     */
    public void openLesson(Lesson lesson) {
        showLearning(lesson);
    }
    
    /**
     * Return to dashboard after completing/exiting a lesson.
     */
    public void returnToDashboard() {
        showDashboard();
    }
    
    /**
     * Get the currently logged-in user.
     * 
     * @return The current user, or null if not logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    // ==================== MAIN METHOD (ENTRY POINT) ====================
    
    /**
     * The main method - where the program starts!
     * 
     * SWING THREADING:
     * Swing is NOT thread-safe, so we must create our GUI
     * on the Event Dispatch Thread (EDT) using invokeLater.
     * 
     * Don't worry if this looks complex - just remember:
     * - All Swing GUIs should be created inside invokeLater
     * - This ensures the GUI runs on the correct thread
     */
    public static void main(String[] args) {
        // Schedule GUI creation on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and show the application
                MainApplication app = new MainApplication();
                app.setVisible(true);
                
                System.out.println("===========================================");
                System.out.println("   IHEC-JLearn Application Started!");
                System.out.println("===========================================");
                System.out.println("Test credentials:");
                System.out.println("  Student: ahmed / 12345");
                System.out.println("  Admin:   admin / admin123");
                System.out.println("===========================================");
            }
        });
    }
}
