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
import java.awt.geom.RoundRectangle2D;
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
        JPanel navBar = StyleUtils.createGradientHeader(80);
        navBar.setBorder(new EmptyBorder(0, 30, 0, 30));
        
        // Left side: App title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 20));
        leftPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(StyleUtils.APP_NAME);
        titleLabel.setFont(StyleUtils.FONT_SUBHEADER);
        titleLabel.setForeground(StyleUtils.TEXT_LIGHT);
        leftPanel.add(titleLabel);
        
        navBar.add(leftPanel, BorderLayout.WEST);
        
        // Right side: User info, profile, and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        rightPanel.setOpaque(false);
        
        // XP display with badge style
        xpLabel = new JLabel("⭐ XP: 0");
        xpLabel.setFont(StyleUtils.FONT_BUTTON);
        xpLabel.setForeground(StyleUtils.TEXT_LIGHT);
        xpLabel.setBackground(new Color(255, 255, 255, 40));
        xpLabel.setOpaque(true);
        xpLabel.setBorder(new EmptyBorder(8, 16, 8, 16));
        rightPanel.add(xpLabel);
        
        // Profile button
        JButton profileBtn = StyleUtils.createModernButton("👤 Profile", StyleUtils.CARD_WHITE);
        profileBtn.setForeground(StyleUtils.PRIMARY_BLUE);
        profileBtn.setPreferredSize(new Dimension(110, 40));
        profileBtn.addActionListener(e -> parentApp.showProfile());
        rightPanel.add(profileBtn);
        
        // Logout button - modern style
        JButton logoutBtn = StyleUtils.createModernButton("Logout", StyleUtils.CARD_WHITE);
        logoutBtn.setForeground(StyleUtils.PRIMARY_BLUE);
        logoutBtn.setPreferredSize(new Dimension(100, 40));
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
        contentArea.setBorder(new EmptyBorder(35, 35, 35, 35));
        
        // Header with welcome message
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 25, 0));
        
        JLabel welcomeLabel = new JLabel("📚 My Learning Path");
        welcomeLabel.setFont(StyleUtils.FONT_HEADER);
        welcomeLabel.setForeground(StyleUtils.TEXT_DARK);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        
        JLabel subtitleLabel = new JLabel("Select a skill to start learning");
        subtitleLabel.setFont(StyleUtils.FONT_BODY);
        subtitleLabel.setForeground(StyleUtils.TEXT_MUTED);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        contentArea.add(headerPanel, BorderLayout.NORTH);
        
        // Courses grid (will be populated when user logs in)
        coursesPanel = new JPanel();
        coursesPanel.setLayout(new GridLayout(0, 2, 25, 25));  // 2 columns, more spacing
        coursesPanel.setBackground(StyleUtils.BACKGROUND_GRAY);
        
        // Wrap in scroll pane for many courses
        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
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
        // Modern card with shadow effect
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2.setColor(new Color(15, 23, 42, 15));
                g2.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 16, 16);
                
                // Draw card background
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 16, 16);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBackground(StyleUtils.CARD_WHITE);
        card.setBorder(new EmptyBorder(24, 24, 24, 24));
        card.setPreferredSize(new Dimension(320, 220));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Top section with category badge and status
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        // Category badge (e.g., "Java", "Python")
        JLabel categoryBadge = new JLabel(lesson.getCategory()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(StyleUtils.PRIMARY_BLUE_LIGHT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        categoryBadge.setFont(StyleUtils.FONT_SMALL.deriveFont(Font.BOLD));
        categoryBadge.setForeground(StyleUtils.PRIMARY_BLUE);
        categoryBadge.setOpaque(false);
        categoryBadge.setBorder(new EmptyBorder(4, 10, 4, 10));
        
        // Status icon
        JLabel statusIcon = new JLabel(isCompleted ? "✅" : "📖");
        statusIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        badgePanel.setOpaque(false);
        badgePanel.add(categoryBadge);
        badgePanel.add(statusIcon);
        topPanel.add(badgePanel, BorderLayout.WEST);
        
        card.add(topPanel, BorderLayout.NORTH);
        
        // Middle section with title and description
        JPanel middlePanel = new JPanel();
        middlePanel.setOpaque(false);
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setBorder(new EmptyBorder(12, 0, 12, 0));
        
        // Lesson title
        JLabel titleLabel = new JLabel(lesson.getTitle());
        titleLabel.setFont(StyleUtils.FONT_SUBHEADER);
        titleLabel.setForeground(StyleUtils.TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        middlePanel.add(titleLabel);
        
        middlePanel.add(Box.createVerticalStrut(8));
        
        // Lesson description (truncated theory)
        String theoryPreview = lesson.getTheoryText();
        if (theoryPreview.length() > 80) {
            theoryPreview = theoryPreview.substring(0, 80) + "...";
        }
        JLabel descLabel = new JLabel("<html><p style='width:250px;'>" + theoryPreview + "</p></html>");
        descLabel.setFont(StyleUtils.FONT_BODY);
        descLabel.setForeground(StyleUtils.TEXT_MUTED);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        middlePanel.add(descLabel);
        
        card.add(middlePanel, BorderLayout.CENTER);
        
        // Bottom: Progress bar, status, and save button
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 8));
        bottomPanel.setOpaque(false);
        
        // Top row: Status and save button
        JPanel statusRow = new JPanel(new BorderLayout());
        statusRow.setOpaque(false);
        
        // Status label
        JLabel statusLabel = new JLabel(isCompleted ? "Completed" : "Not Started");
        statusLabel.setFont(StyleUtils.FONT_SMALL);
        statusLabel.setForeground(isCompleted ? StyleUtils.SUCCESS_GREEN : StyleUtils.TEXT_MUTED);
        statusRow.add(statusLabel, BorderLayout.WEST);
        
        // Save/Bookmark button
        boolean isSaved = currentUser != null && dataManager.isCourseSaved(currentUser.getId(), lesson.getId());
        JButton saveBtn = new JButton(isSaved ? "★" : "☆");
        saveBtn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        saveBtn.setForeground(isSaved ? StyleUtils.WARNING_YELLOW : StyleUtils.TEXT_MUTED);
        saveBtn.setBackground(StyleUtils.CARD_WHITE);
        saveBtn.setBorderPainted(false);
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.setToolTipText(isSaved ? "Remove from saved" : "Save for later");
        saveBtn.addActionListener(e -> {
            if (currentUser != null) {
                if (dataManager.isCourseSaved(currentUser.getId(), lesson.getId())) {
                    dataManager.unsaveCourseForUser(currentUser.getId(), lesson.getId());
                    saveBtn.setText("☆");
                    saveBtn.setForeground(StyleUtils.TEXT_MUTED);
                } else {
                    dataManager.saveCourseForUser(currentUser.getId(), lesson.getId());
                    saveBtn.setText("★");
                    saveBtn.setForeground(StyleUtils.WARNING_YELLOW);
                }
            }
        });
        statusRow.add(saveBtn, BorderLayout.EAST);
        
        bottomPanel.add(statusRow, BorderLayout.NORTH);
        
        // Modern progress bar
        JProgressBar progressBar = StyleUtils.createModernProgressBar();
        progressBar.setValue(isCompleted ? 100 : 0);
        progressBar.setForeground(isCompleted ? StyleUtils.SUCCESS_GREEN : StyleUtils.PRIMARY_BLUE);
        progressBar.setPreferredSize(new Dimension(0, 6));
        bottomPanel.add(progressBar, BorderLayout.CENTER);
        
        card.add(bottomPanel, BorderLayout.SOUTH);
        
        // Click handler - open the lesson
        card.addMouseListener(new MouseAdapter() {
            private Color originalBg = StyleUtils.CARD_WHITE;
            
            @Override
            public void mouseClicked(MouseEvent e) {
                parentApp.openLesson(lesson);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(StyleUtils.BACKGROUND_GRAY);
                card.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(originalBg);
                card.repaint();
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
