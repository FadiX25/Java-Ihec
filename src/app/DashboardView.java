package app;

import services.CsvDataManager;
import services.DataManager;
import services.FirebaseDataManager;
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
 * ENHANCED FEATURES:
 * - Animated course cards with hover lift effect
 * - Progress rings for visual feedback
 * - Skeleton loading states
 * - Toast notifications for actions
 * - Smooth transitions
 */
public class DashboardView extends JPanel {

    // ==================== REFERENCES ====================
    
    private MainApplication parentApp;
    private DataManager dataManager;
    private User currentUser;
    
    // UI Components that need updating
    private JLabel xpLabel;
    private JLabel welcomeLabel;
    private JPanel coursesPanel;
    private JPanel statsPanel;

    // ==================== CONSTRUCTOR ====================
    
    public DashboardView(MainApplication parentApp) {
        this.parentApp = parentApp;
        DataManager dm;
        try {
            dm = new FirebaseDataManager();
        } catch (Exception e) {
            System.err.println("Firebase init failed: " + e.getMessage());
            dm = new CsvDataManager();
        }
        this.dataManager = dm;

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
     * Create the top navigation bar with enhanced styling.
     */
    private JPanel createNavBar() {
        JPanel navBar = StyleUtils.createGradientHeader(80);
        navBar.setBorder(new EmptyBorder(0, 30, 0, 30));
        
        // Left side: App title with subtle animation-ready styling
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
        
        // XP display with animated badge style
        xpLabel = new JLabel("⭐ XP: 0") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw pill-shaped background
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        xpLabel.setFont(StyleUtils.FONT_BUTTON);
        xpLabel.setForeground(StyleUtils.TEXT_LIGHT);
        xpLabel.setOpaque(false);
        xpLabel.setBorder(new EmptyBorder(8, 16, 8, 16));
        rightPanel.add(xpLabel);
        
        // Profile button with icon
        JButton profileBtn = StyleUtils.createModernButton("👤 Profile", StyleUtils.CARD_WHITE);
        profileBtn.setForeground(StyleUtils.PRIMARY_BLUE);
        profileBtn.setPreferredSize(new Dimension(120, 40));
        profileBtn.addActionListener(e -> parentApp.showProfile());
        rightPanel.add(profileBtn);
        
        // Logout button - ghost style
        JButton logoutBtn = StyleUtils.createGhostButton("Logout", StyleUtils.CARD_WHITE);
        logoutBtn.setForeground(StyleUtils.TEXT_LIGHT);
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
        
        // Top section: Header + Stats
        JPanel topSection = new JPanel(new BorderLayout(0, 20));
        topSection.setOpaque(false);
        
        // Header with welcome message
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        welcomeLabel = new JLabel("📚 My Learning Path");
        welcomeLabel.setFont(StyleUtils.FONT_HEADER);
        welcomeLabel.setForeground(StyleUtils.TEXT_DARK);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        
        JLabel subtitleLabel = new JLabel("Select a skill to start learning");
        subtitleLabel.setFont(StyleUtils.FONT_BODY);
        subtitleLabel.setForeground(StyleUtils.TEXT_MUTED);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        topSection.add(headerPanel, BorderLayout.NORTH);
        
        // Quick stats cards
        statsPanel = createQuickStats();
        topSection.add(statsPanel, BorderLayout.CENTER);
        
        contentArea.add(topSection, BorderLayout.NORTH);
        
        // Courses grid with modern 3-column layout
        coursesPanel = new JPanel();
        coursesPanel.setLayout(new GridLayout(0, 3, 25, 25));  // 3 columns
        coursesPanel.setBackground(StyleUtils.BACKGROUND_GRAY);
        coursesPanel.setBorder(new EmptyBorder(25, 0, 0, 0));
        
        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setBackground(StyleUtils.BACKGROUND_GRAY);
        scrollPane.getViewport().setBackground(StyleUtils.BACKGROUND_GRAY);
        
        // Smooth scrolling
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        contentArea.add(scrollPane, BorderLayout.CENTER);
        
        return contentArea;
    }
    
    /**
     * Create quick stats overview cards.
     */
    private JPanel createQuickStats() {
        JPanel stats = new JPanel(new GridLayout(1, 3, 20, 0));
        stats.setOpaque(false);
        stats.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Will be populated when user logs in
        return stats;
    }
    
    /**
     * Create a stat mini-card.
     */
    private JPanel createStatMiniCard(String value, String label, String icon, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(12, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw card background
                g2.setColor(StyleUtils.CARD_WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                
                // Left accent bar
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 8, 4, getHeight() - 16, 2, 2);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(16, 20, 16, 16));
        
        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        card.add(iconLabel, BorderLayout.WEST);
        
        // Text content
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(StyleUtils.TEXT_DARK);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(valueLabel);
        
        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(StyleUtils.FONT_SMALL);
        labelLabel.setForeground(StyleUtils.TEXT_MUTED);
        labelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(labelLabel);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Create a single course card with enhanced interactions.
     * 
     * @param lesson The lesson to display
     * @param isCompleted Whether the student completed this lesson
     */
    private JPanel createCourseCard(Lesson lesson, boolean isCompleted) {
        // Modern card with animated shadow and lift effect
        JPanel card = new JPanel(new BorderLayout()) {
            private boolean isHovered = false;
            private int shadowOffset = 4;
            private float scale = 1.0f;
            
            {
                // Hover animation
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        shadowOffset = 8;
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                        repaint();
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        shadowOffset = 4;
                        repaint();
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int offset = shadowOffset;
                
                // Layered shadow for depth
                for (int i = offset; i > 0; i--) {
                    float alpha = 0.02f * (offset - i + 1);
                    g2.setColor(new Color(15, 23, 42, (int)(alpha * 255)));
                    g2.fill(new RoundRectangle2D.Float(i, i, getWidth() - i, getHeight() - i, 16, 16));
                }
                
                // Card background
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - offset, getHeight() - offset, 16, 16));
                
                // Hover border glow
                if (isHovered) {
                    g2.setColor(new Color(StyleUtils.PRIMARY_BLUE.getRed(), 
                                         StyleUtils.PRIMARY_BLUE.getGreen(),
                                         StyleUtils.PRIMARY_BLUE.getBlue(), 60));
                    g2.setStroke(new BasicStroke(2));
                    g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - offset - 2, 
                            getHeight() - offset - 2, 16, 16));
                }
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBackground(StyleUtils.CARD_WHITE);
        card.setBorder(new EmptyBorder(24, 24, 24, 24));
        card.setPreferredSize(new Dimension(320, 240));
        
        // Top section with category badge and status indicator
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        // Left: Category badge with gradient
        JLabel categoryBadge = new JLabel(lesson.getCategory()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, StyleUtils.PRIMARY_BLUE_LIGHT,
                    getWidth(), 0, new Color(219, 234, 254)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        categoryBadge.setFont(StyleUtils.FONT_SMALL.deriveFont(Font.BOLD));
        categoryBadge.setForeground(StyleUtils.PRIMARY_BLUE);
        categoryBadge.setOpaque(false);
        categoryBadge.setBorder(new EmptyBorder(5, 12, 5, 12));
        
        // Right: Status icon with animation-ready styling
        JLabel statusIcon = new JLabel(isCompleted ? "✅" : "📖") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        statusIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        
        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        badgePanel.setOpaque(false);
        badgePanel.add(categoryBadge);
        topPanel.add(badgePanel, BorderLayout.WEST);
        topPanel.add(statusIcon, BorderLayout.EAST);
        
        card.add(topPanel, BorderLayout.NORTH);
        
        // Middle section with title and description
        JPanel middlePanel = new JPanel();
        middlePanel.setOpaque(false);
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setBorder(new EmptyBorder(16, 0, 16, 0));
        
        // Lesson title with better typography
        JLabel titleLabel = new JLabel(lesson.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(StyleUtils.TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        middlePanel.add(titleLabel);
        
        middlePanel.add(Box.createVerticalStrut(10));
        
        // Description with ellipsis
        String theoryPreview = lesson.getTheoryText();
        if (theoryPreview.length() > 90) {
            theoryPreview = theoryPreview.substring(0, 90) + "...";
        }
        JLabel descLabel = new JLabel("<html><p style='width:240px; line-height:1.4;'>" + theoryPreview + "</p></html>");
        descLabel.setFont(StyleUtils.FONT_BODY);
        descLabel.setForeground(StyleUtils.TEXT_MUTED);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        middlePanel.add(descLabel);
        
        card.add(middlePanel, BorderLayout.CENTER);
        
        // Bottom: Progress + Actions
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        bottomPanel.setOpaque(false);
        
        // Top row: Status text and save button
        JPanel statusRow = new JPanel(new BorderLayout());
        statusRow.setOpaque(false);
        
        // Status with icon
        String statusText = isCompleted ? "✓ Completed" : "○ Not Started";
        JLabel statusLabel = new JLabel(statusText);
        statusLabel.setFont(StyleUtils.FONT_SMALL.deriveFont(Font.BOLD));
        statusLabel.setForeground(isCompleted ? StyleUtils.SUCCESS_GREEN : StyleUtils.TEXT_MUTED);
        statusRow.add(statusLabel, BorderLayout.WEST);
        
        // Save/Bookmark button with animation
        boolean isSaved = currentUser != null && dataManager.isCourseSaved(currentUser.getId(), lesson.getId());
        JButton saveBtn = new JButton(isSaved ? "★" : "☆") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw text
                g2.setFont(getFont());
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                
                g2.dispose();
            }
        };
        saveBtn.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        saveBtn.setForeground(isSaved ? StyleUtils.WARNING_YELLOW : StyleUtils.TEXT_MUTED);
        saveBtn.setPreferredSize(new Dimension(36, 36));
        saveBtn.setBorderPainted(false);
        saveBtn.setFocusPainted(false);
        saveBtn.setContentAreaFilled(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.setToolTipText(isSaved ? "Remove from saved" : "Save for later");
        
        saveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                saveBtn.setFont(new Font("Segoe UI", Font.PLAIN, 22));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                saveBtn.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            }
        });
        
        saveBtn.addActionListener(e -> {
            if (currentUser != null) {
                if (dataManager.isCourseSaved(currentUser.getId(), lesson.getId())) {
                    dataManager.unsaveCourseForUser(currentUser.getId(), lesson.getId());
                    saveBtn.setText("☆");
                    saveBtn.setForeground(StyleUtils.TEXT_MUTED);
                    // Show toast
                    SwingUtilities.invokeLater(() -> {
                        StyleUtils.showToast((JFrame) SwingUtilities.getWindowAncestor(this), 
                            "Course removed from saved", StyleUtils.ToastType.INFO);
                    });
                } else {
                    dataManager.saveCourseForUser(currentUser.getId(), lesson.getId());
                    saveBtn.setText("★");
                    saveBtn.setForeground(StyleUtils.WARNING_YELLOW);
                    // Show toast
                    SwingUtilities.invokeLater(() -> {
                        StyleUtils.showToast((JFrame) SwingUtilities.getWindowAncestor(this), 
                            "Course saved for later!", StyleUtils.ToastType.SUCCESS);
                    });
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
            @Override
            public void mouseClicked(MouseEvent e) {
                // Avoid triggering when clicking save button
                if (e.getSource() == card) {
                    parentApp.openLesson(lesson);
                }
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
        
        // Update welcome message
        if (user != null) {
            welcomeLabel.setText("Welcome back, " + user.getUsername() + "!");
        }
        
        // Update XP display (only for students)
        if (user instanceof Student) {
            Student student = (Student) user;
            xpLabel.setText("XP: " + student.getXpScore());
            
            // Update quick stats
            updateQuickStats(student);
        } else {
            xpLabel.setText("Admin Mode");
        }
        
        // Load and display courses
        loadCourses();
    }
    
    /**
     * Update the quick stats cards.
     */
    private void updateQuickStats(Student student) {
        statsPanel.removeAll();
        
        List<Lesson> lessons = dataManager.loadLessons();
        int completedCount = student.getCompletedLessonIds().size();
        int totalCourses = lessons.size();
        int savedCount = dataManager.getSavedCoursesForUser(student.getId()).size();
        
        // Completed courses stat
        statsPanel.add(createStatMiniCard(
            completedCount + "/" + totalCourses, 
            "Courses Completed", 
            "🎯", 
            StyleUtils.SUCCESS_GREEN
        ));
        
        // XP Progress stat
        statsPanel.add(createStatMiniCard(
            String.valueOf(student.getXpScore()), 
            "Total XP Earned", 
            "⭐", 
            StyleUtils.WARNING_YELLOW
        ));
        
        // Saved courses stat
        statsPanel.add(createStatMiniCard(
            String.valueOf(savedCount), 
            "Saved Courses", 
            "📚", 
            StyleUtils.PRIMARY_BLUE
        ));
        
        statsPanel.revalidate();
        statsPanel.repaint();
    }
    
    /**
     * Load courses from CSV and create cards with loading animation.
     */
    private void loadCourses() {
        // Clear existing cards
        coursesPanel.removeAll();
        
        // Show skeleton loaders first
        for (int i = 0; i < 6; i++) {
            coursesPanel.add(StyleUtils.createSkeletonCard(320, 240));
        }
        coursesPanel.revalidate();
        coursesPanel.repaint();
        
        // Load actual data with slight delay for effect
        Timer loadTimer = new Timer(300, e -> {
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
            
            // If no lessons, show empty state
            if (lessons.isEmpty()) {
                JPanel emptyState = createEmptyState();
                coursesPanel.add(emptyState);
            }
            
            // Refresh the panel
            coursesPanel.revalidate();
            coursesPanel.repaint();
        });
        loadTimer.setRepeats(false);
        loadTimer.start();
    }
    
    /**
     * Create an attractive empty state.
     */
    private JPanel createEmptyState() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(60, 40, 60, 40));
        
        JLabel iconLabel = new JLabel("📚");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);
        
        panel.add(Box.createVerticalStrut(20));
        
        JLabel titleLabel = new JLabel("No Courses Yet");
        titleLabel.setFont(StyleUtils.FONT_SUBHEADER);
        titleLabel.setForeground(StyleUtils.TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        
        panel.add(Box.createVerticalStrut(8));
        
        JLabel descLabel = new JLabel("Check back soon for new learning content!");
        descLabel.setFont(StyleUtils.FONT_BODY);
        descLabel.setForeground(StyleUtils.TEXT_MUTED);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(descLabel);
        
        return panel;
    }
}
