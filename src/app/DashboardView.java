package app;

import services.CsvDataManager;
import model.Lesson;
import model.Student;
import model.User;
import utils.StyleUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * DashboardView - The main dashboard showing courses and progress.
 * 
 * LAYOUT STRUCTURE:
 * - NORTH: Navigation bar (blue header with user info)
 * - CENTER: Scrollable grid of course cards
 * 
 * COMPONENTS LEARNED:
 * - JScrollPane: Makes content scrollable
 * - JProgressBar: Shows completion progress
 * - GridLayout: Arranges components in a grid
 */
public class DashboardView extends JPanel {

    // ==================== REFERENCES ====================
    
    private MainApplication parentApp;
    private CsvDataManager dataManager;
    private User currentUser;
    
    // UI Components that need updating
    private JLabel xpLabel;
    private JPanel coursesPanel;

    // ==================== CONSTRUCTOR ====================
    
    public DashboardView(MainApplication parentApp) {
        this.parentApp = parentApp;
        this.dataManager = new CsvDataManager();
        
        initializeUI();
    }

    // ==================== UI INITIALIZATION ====================
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(StyleUtils.BACKGROUND_GRAY);
        
        // Navigation bar at top
        add(createNavBar(), BorderLayout.NORTH);
        
        // Main content area
        add(createContentArea(), BorderLayout.CENTER);
    }
    
    /**
     * Create the top navigation bar.
     */
    private JPanel createNavBar() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(StyleUtils.PRIMARY_BLUE);
        navBar.setPreferredSize(new Dimension(0, 70));
        navBar.setBorder(new EmptyBorder(15, 25, 15, 25));
        
        // Left side: App title
        JLabel titleLabel = new JLabel("IHEC-JLearn");
        titleLabel.setFont(StyleUtils.FONT_SUBHEADER);
        titleLabel.setForeground(StyleUtils.TEXT_LIGHT);
        navBar.add(titleLabel, BorderLayout.WEST);
        
        // Right side: User info and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);  // Transparent background
        
        // XP display
        xpLabel = new JLabel("XP: 0");
        xpLabel.setFont(StyleUtils.FONT_BODY);
        xpLabel.setForeground(StyleUtils.TEXT_LIGHT);
        rightPanel.add(xpLabel);
        
        // Logout button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(StyleUtils.FONT_BODY);
        logoutBtn.setBackground(StyleUtils.CARD_WHITE);
        logoutBtn.setForeground(StyleUtils.PRIMARY_BLUE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> parentApp.logout());
        rightPanel.add(logoutBtn);
        
        navBar.add(rightPanel, BorderLayout.EAST);
        
        return navBar;
    }
    
    /**
     * Create the main content area with courses.
     */
    private JPanel createContentArea() {
        JPanel contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(StyleUtils.BACKGROUND_GRAY);
        contentArea.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("My Courses");
        welcomeLabel.setFont(StyleUtils.FONT_HEADER);
        welcomeLabel.setForeground(StyleUtils.TEXT_DARK);
        welcomeLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        contentArea.add(welcomeLabel, BorderLayout.NORTH);
        
        // Courses grid (will be populated when user logs in)
        coursesPanel = new JPanel();
        coursesPanel.setLayout(new GridLayout(0, 2, 20, 20));  // 2 columns, auto rows
        coursesPanel.setBackground(StyleUtils.BACKGROUND_GRAY);
        
        // Wrap in scroll pane for many courses
        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(StyleUtils.BACKGROUND_GRAY);
        scrollPane.getViewport().setBackground(StyleUtils.BACKGROUND_GRAY);
        
        contentArea.add(scrollPane, BorderLayout.CENTER);
        
        return contentArea;
    }
    
    /**
     * Create a single course card.
     * 
     * @param lesson The lesson to display
     * @param isCompleted Whether the student completed this lesson
     */
    private JPanel createCourseCard(Lesson lesson, boolean isCompleted) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(StyleUtils.CARD_WHITE);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(300, 180));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Course title
        JLabel titleLabel = new JLabel(lesson.getTitle());
        titleLabel.setFont(StyleUtils.FONT_SUBHEADER);
        titleLabel.setForeground(StyleUtils.TEXT_DARK);
        card.add(titleLabel, BorderLayout.NORTH);
        
        // Course description (truncated theory)
        String theoryPreview = lesson.getTheoryText();
        if (theoryPreview.length() > 100) {
            theoryPreview = theoryPreview.substring(0, 100) + "...";
        }
        JLabel descLabel = new JLabel("<html><p style='width:250px'>" + theoryPreview + "</p></html>");
        descLabel.setFont(StyleUtils.FONT_BODY);
        descLabel.setForeground(StyleUtils.TEXT_MUTED);
        descLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        card.add(descLabel, BorderLayout.CENTER);
        
        // Bottom: Progress bar and status
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(StyleUtils.CARD_WHITE);
        
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(isCompleted ? 100 : 0);
        progressBar.setStringPainted(true);
        progressBar.setString(isCompleted ? "Completed" : "Not Started");
        progressBar.setForeground(isCompleted ? StyleUtils.SUCCESS_GREEN : StyleUtils.PRIMARY_BLUE);
        progressBar.setBackground(StyleUtils.BACKGROUND_GRAY);
        
        bottomPanel.add(progressBar, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);
        
        // Click handler - open the lesson
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parentApp.openLesson(lesson);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(StyleUtils.BACKGROUND_GRAY);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(StyleUtils.CARD_WHITE);
            }
        });
        
        return card;
    }

    // ==================== PUBLIC METHODS ====================
    
    /**
     * Refresh the dashboard for the current user.
     * Called when user logs in or returns from a lesson.
     * 
     * @param user The logged-in user
     */
    public void refreshForUser(User user) {
        this.currentUser = user;
        
        // Update XP display (only for students)
        if (user instanceof Student) {
            Student student = (Student) user;
            xpLabel.setText("XP: " + student.getXpScore());
        } else {
            xpLabel.setText("Admin");
        }
        
        // Load and display courses
        loadCourses();
    }
    
    /**
     * Load courses from CSV and create cards.
     */
    private void loadCourses() {
        // Clear existing cards
        coursesPanel.removeAll();
        
        // Load lessons
        List<Lesson> lessons = dataManager.loadLessons();
        
        // Create a card for each lesson
        for (Lesson lesson : lessons) {
            boolean isCompleted = false;
            
            // Check if student completed this lesson
            if (currentUser instanceof Student) {
                Student student = (Student) currentUser;
                isCompleted = student.hasCompletedLesson(lesson.getId());
            }
            
            JPanel card = createCourseCard(lesson, isCompleted);
            coursesPanel.add(card);
        }
        
        // If no lessons, show a message
        if (lessons.isEmpty()) {
            JLabel emptyLabel = new JLabel("No courses available yet.");
            emptyLabel.setFont(StyleUtils.FONT_BODY);
            emptyLabel.setForeground(StyleUtils.TEXT_MUTED);
            coursesPanel.add(emptyLabel);
        }
        
        // Refresh the panel
        coursesPanel.revalidate();
        coursesPanel.repaint();
    }
}
