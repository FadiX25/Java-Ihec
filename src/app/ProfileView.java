package app;

import model.Certificate;
import model.Lesson;
import model.SavedCourse;
import model.Student;
import model.User;
import services.CsvDataManager;
import services.DataManager;
import services.FirebaseDataManager;
import utils.StyleUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ProfileView - User profile with saved courses and certificates.
 * 
 * LAYOUT:
 * ┌─────────────────────────────────────────────────────────────┐
 * │                    TOP NAVIGATION BAR                       │
 * ├─────────────────────────────────────────────────────────────┤
 * │  ┌─────────────────────────────────────────────────────┐    │
 * │  │   PROFILE HEADER (Avatar, Name, Stats)              │    │
 * │  └─────────────────────────────────────────────────────┘    │
 * │  ┌─────────────────────────────────────────────────────┐    │
 * │  │   TABS: [Profile] [Saved Courses] [Certificates]    │    │
 * │  └─────────────────────────────────────────────────────┘    │
 * │  ┌─────────────────────────────────────────────────────┐    │
 * │  │   TAB CONTENT                                       │    │
 * │  └─────────────────────────────────────────────────────┘    │
 * └─────────────────────────────────────────────────────────────┘
 */
public class ProfileView extends JPanel {

    // ==================== REFERENCES ====================
    
    private MainApplication parentApp;
    private DataManager dataManager;
    private Student currentStudent;
    
    // ==================== UI COMPONENTS ====================
    
    private JPanel contentPanel;
    private JPanel profileTab;
    private JPanel savedCoursesTab;
    private JPanel certificatesTab;
    private CardLayout tabCardLayout;
    private JPanel tabContent;
    
    // Profile form fields
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextArea bioArea;

    // ==================== CONSTRUCTOR ====================
    
    public ProfileView(MainApplication parentApp) {
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
        
        // Top navigation bar
        add(createNavBar(), BorderLayout.NORTH);
        
        // Main content area with profile
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(StyleUtils.BACKGROUND_GRAY);
        contentPanel.setBorder(new EmptyBorder(30, 50, 30, 50));
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Create the top navigation bar.
     */
    private JPanel createNavBar() {
        JPanel navBar = StyleUtils.createGradientHeader(80);
        navBar.setBorder(new EmptyBorder(0, 25, 0, 25));
        
        // Left side: Back button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 18));
        leftPanel.setOpaque(false);
        
        JButton backButton = StyleUtils.createModernButton("← Back", StyleUtils.CARD_WHITE);
        backButton.setForeground(StyleUtils.PRIMARY_BLUE);
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.addActionListener(e -> parentApp.returnToDashboard());
        leftPanel.add(backButton);
        
        navBar.add(leftPanel, BorderLayout.WEST);
        
        // Center: Title
        JLabel titleLabel = new JLabel("My Profile", SwingConstants.CENTER);
        titleLabel.setFont(StyleUtils.FONT_SUBHEADER);
        titleLabel.setForeground(StyleUtils.TEXT_LIGHT);
        navBar.add(titleLabel, BorderLayout.CENTER);
        
        // Right side: spacer
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(100, 0));
        navBar.add(rightPanel, BorderLayout.EAST);
        
        return navBar;
    }
    
    /**
     * Load and display the user's profile.
     */
    public void loadProfile(User user) {
        if (!(user instanceof Student)) {
            return;
        }
        
        this.currentStudent = (Student) user;
        contentPanel.removeAll();
        
        // Create main scroll container
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);
        
        // Profile header card
        mainPanel.add(createProfileHeader());
        mainPanel.add(Box.createVerticalStrut(25));
        
        // Tab buttons
        mainPanel.add(createTabButtons());
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Tab content area
        tabCardLayout = new CardLayout();
        tabContent = new JPanel(tabCardLayout);
        tabContent.setOpaque(false);
        
        tabContent.add(createProfileInfoTab(), "PROFILE");
        tabContent.add(createSavedCoursesTab(), "SAVED");
        tabContent.add(createCertificatesTab(), "CERTIFICATES");
        
        mainPanel.add(tabContent);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setBackground(StyleUtils.BACKGROUND_GRAY);
        scrollPane.getViewport().setBackground(StyleUtils.BACKGROUND_GRAY);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * Create the profile header with avatar and stats.
     */
    private JPanel createProfileHeader() {
        JPanel header = new JPanel(new BorderLayout(20, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2.setColor(new Color(30, 58, 95, 15));
                g2.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 16, 16);
                
                // Draw card
                g2.setColor(StyleUtils.CARD_WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 16, 16);
                
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(30, 30, 30, 30));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        
        // Left: Avatar
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw circle background
                g2.setColor(StyleUtils.PRIMARY_BLUE);
                g2.fillOval(0, 0, getWidth(), getHeight());
                
                // Draw initial
                String initial = currentStudent.getUsername().substring(0, 1).toUpperCase();
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 48));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(initial)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(initial, x, y);
                
                g2.dispose();
            }
        };
        avatarPanel.setPreferredSize(new Dimension(100, 100));
        avatarPanel.setOpaque(false);
        header.add(avatarPanel, BorderLayout.WEST);
        
        // Center: Name and username
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        JLabel nameLabel = new JLabel(currentStudent.getFullName());
        nameLabel.setFont(StyleUtils.FONT_HEADER);
        nameLabel.setForeground(StyleUtils.TEXT_DARK);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(nameLabel);
        
        JLabel usernameLabel = new JLabel("@" + currentStudent.getUsername());
        usernameLabel.setFont(StyleUtils.FONT_BODY);
        usernameLabel.setForeground(StyleUtils.TEXT_MUTED);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(usernameLabel);
        
        if (!currentStudent.getBio().isEmpty()) {
            infoPanel.add(Box.createVerticalStrut(10));
            JLabel bioLabel = new JLabel(currentStudent.getBio());
            bioLabel.setFont(StyleUtils.FONT_BODY);
            bioLabel.setForeground(StyleUtils.TEXT_MUTED);
            bioLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(bioLabel);
        }
        
        header.add(infoPanel, BorderLayout.CENTER);
        
        // Right: Stats
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false);
        
        int completedCount = currentStudent.getCompletedLessonIds().size();
        int savedCount = dataManager.getSavedCoursesForUser(currentStudent.getId()).size();
        int certCount = dataManager.getCertificatesForUser(currentStudent.getId()).size();
        
        statsPanel.add(createStatBox("⭐", String.valueOf(currentStudent.getXpScore()), "XP"));
        statsPanel.add(createStatBox("✅", String.valueOf(completedCount), "Completed"));
        statsPanel.add(createStatBox("🏆", String.valueOf(certCount), "Certificates"));
        
        header.add(statsPanel, BorderLayout.EAST);
        
        return header;
    }
    
    /**
     * Create a stat display box.
     */
    private JPanel createStatBox(String emoji, String value, String label) {
        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        
        JLabel emojiLabel = new JLabel(emoji, SwingConstants.CENTER);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        emojiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(emojiLabel);
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(StyleUtils.FONT_STAT_NUMBER);
        valueLabel.setForeground(StyleUtils.PRIMARY_BLUE);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(valueLabel);
        
        JLabel labelLabel = new JLabel(label, SwingConstants.CENTER);
        labelLabel.setFont(StyleUtils.FONT_SMALL);
        labelLabel.setForeground(StyleUtils.TEXT_MUTED);
        labelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(labelLabel);
        
        return box;
    }
    
    /**
     * Create the tab button bar.
     */
    private JPanel createTabButtons() {
        JPanel tabBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        tabBar.setOpaque(false);
        tabBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JButton profileBtn = createTabButton("👤 Profile", true);
        JButton savedBtn = createTabButton("📚 Saved Courses", false);
        JButton certsBtn = createTabButton("🏆 Certificates", false);
        
        profileBtn.addActionListener(e -> {
            tabCardLayout.show(tabContent, "PROFILE");
            updateTabStyles(profileBtn, savedBtn, certsBtn);
        });
        
        savedBtn.addActionListener(e -> {
            tabCardLayout.show(tabContent, "SAVED");
            updateTabStyles(savedBtn, profileBtn, certsBtn);
        });
        
        certsBtn.addActionListener(e -> {
            tabCardLayout.show(tabContent, "CERTIFICATES");
            updateTabStyles(certsBtn, profileBtn, savedBtn);
        });
        
        tabBar.add(profileBtn);
        tabBar.add(savedBtn);
        tabBar.add(certsBtn);
        
        return tabBar;
    }
    
    /**
     * Create a styled tab button.
     */
    private JButton createTabButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(StyleUtils.FONT_BUTTON);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty("active", active);
        
        if (active) {
            btn.setBackground(StyleUtils.PRIMARY_BLUE);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(StyleUtils.CARD_WHITE);
            btn.setForeground(StyleUtils.TEXT_DARK);
        }
        
        return btn;
    }
    
    /**
     * Update tab button styles when switching tabs.
     */
    private void updateTabStyles(JButton active, JButton... inactive) {
        active.setBackground(StyleUtils.PRIMARY_BLUE);
        active.setForeground(Color.WHITE);
        active.putClientProperty("active", true);
        
        for (JButton btn : inactive) {
            btn.setBackground(StyleUtils.CARD_WHITE);
            btn.setForeground(StyleUtils.TEXT_DARK);
            btn.putClientProperty("active", false);
        }
    }
    
    /**
     * Create the Profile Info tab content.
     */
    private JPanel createProfileInfoTab() {
        JPanel panel = createTabCard();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel title = new JLabel("Edit Profile");
        title.setFont(StyleUtils.FONT_SUBHEADER);
        title.setForeground(StyleUtils.TEXT_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        
        // First Name
        panel.add(createFormLabel("First Name"));
        firstNameField = StyleUtils.createModernTextField();
        firstNameField.setText(currentStudent.getFirstName());
        firstNameField.setMaximumSize(new Dimension(400, 48));
        firstNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(firstNameField);
        panel.add(Box.createVerticalStrut(15));
        
        // Last Name
        panel.add(createFormLabel("Last Name"));
        lastNameField = StyleUtils.createModernTextField();
        lastNameField.setText(currentStudent.getLastName());
        lastNameField.setMaximumSize(new Dimension(400, 48));
        lastNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lastNameField);
        panel.add(Box.createVerticalStrut(15));
        
        // Email
        panel.add(createFormLabel("Email"));
        emailField = StyleUtils.createModernTextField();
        emailField.setText(currentStudent.getEmail());
        emailField.setMaximumSize(new Dimension(400, 48));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(15));
        
        // Bio
        panel.add(createFormLabel("Bio"));
        bioArea = new JTextArea(currentStudent.getBio());
        bioArea.setFont(StyleUtils.FONT_BODY);
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        bioArea.setRows(3);
        bioArea.setBorder(new EmptyBorder(12, 16, 12, 16));
        
        JScrollPane bioScroll = new JScrollPane(bioArea);
        bioScroll.setMaximumSize(new Dimension(400, 100));
        bioScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        bioScroll.setBorder(BorderFactory.createLineBorder(StyleUtils.BORDER_LIGHT));
        panel.add(bioScroll);
        panel.add(Box.createVerticalStrut(25));
        
        // Save button
        JButton saveBtn = StyleUtils.createModernButton("Save Changes", StyleUtils.PRIMARY_BLUE);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setPreferredSize(new Dimension(150, 45));
        saveBtn.setMaximumSize(new Dimension(150, 45));
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveBtn.addActionListener(e -> saveProfile());
        panel.add(saveBtn);
        
        return panel;
    }
    
    /**
     * Create a form label.
     */
    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(StyleUtils.FONT_BODY);
        label.setForeground(StyleUtils.TEXT_DARK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 5, 0));
        return label;
    }
    
    /**
     * Save profile changes.
     */
    private void saveProfile() {
        currentStudent.setFirstName(firstNameField.getText().trim());
        currentStudent.setLastName(lastNameField.getText().trim());
        currentStudent.setEmail(emailField.getText().trim());
        currentStudent.setBio(bioArea.getText().trim());
        
        dataManager.updateStudentProfile(currentStudent);
        
        JOptionPane.showMessageDialog(this, 
            "Profile updated successfully!", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
        
        // Refresh the header
        loadProfile(currentStudent);
    }
    
    /**
     * Create the Saved Courses tab content.
     */
    private JPanel createSavedCoursesTab() {
        JPanel panel = createTabCard();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel title = new JLabel("Saved Courses");
        title.setFont(StyleUtils.FONT_SUBHEADER);
        title.setForeground(StyleUtils.TEXT_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        
        List<SavedCourse> savedCourses = dataManager.getSavedCoursesForUser(currentStudent.getId());
        
        if (savedCourses.isEmpty()) {
            JLabel emptyLabel = new JLabel("📚 No saved courses yet. Start exploring!");
            emptyLabel.setFont(StyleUtils.FONT_BODY);
            emptyLabel.setForeground(StyleUtils.TEXT_MUTED);
            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(emptyLabel);
        } else {
            for (SavedCourse sc : savedCourses) {
                Lesson lesson = dataManager.getLessonById(sc.getLessonId());
                if (lesson != null) {
                    panel.add(createSavedCourseCard(lesson, sc));
                    panel.add(Box.createVerticalStrut(10));
                }
            }
        }
        
        return panel;
    }
    
    /**
     * Create a saved course card.
     */
    private JPanel createSavedCourseCard(Lesson lesson, SavedCourse sc) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleUtils.BORDER_LIGHT, 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Left: Category badge and title
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        
        JLabel categoryLabel = new JLabel(lesson.getCategory());
        categoryLabel.setFont(StyleUtils.FONT_SMALL);
        categoryLabel.setForeground(StyleUtils.PRIMARY_BLUE);
        leftPanel.add(categoryLabel);
        
        JLabel titleLabel = new JLabel(lesson.getTitle());
        titleLabel.setFont(StyleUtils.FONT_BODY.deriveFont(Font.BOLD));
        titleLabel.setForeground(StyleUtils.TEXT_DARK);
        leftPanel.add(titleLabel);
        
        JLabel dateLabel = new JLabel("Saved on " + sc.getSaveDate().format(
            DateTimeFormatter.ofPattern("MMM d, yyyy")));
        dateLabel.setFont(StyleUtils.FONT_SMALL);
        dateLabel.setForeground(StyleUtils.TEXT_MUTED);
        leftPanel.add(dateLabel);
        
        card.add(leftPanel, BorderLayout.CENTER);
        
        // Right: Actions
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);
        
        JButton viewBtn = StyleUtils.createModernButton("View", StyleUtils.PRIMARY_BLUE);
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setPreferredSize(new Dimension(80, 35));
        viewBtn.addActionListener(e -> parentApp.openLesson(lesson));
        rightPanel.add(viewBtn);
        
        JButton removeBtn = StyleUtils.createModernButton("Remove", StyleUtils.ERROR_RED);
        removeBtn.setForeground(Color.WHITE);
        removeBtn.setPreferredSize(new Dimension(90, 35));
        removeBtn.addActionListener(e -> {
            dataManager.unsaveCourseForUser(currentStudent.getId(), lesson.getId());
            loadProfile(currentStudent);
        });
        rightPanel.add(removeBtn);
        
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
    
    /**
     * Create the Certificates tab content.
     */
    private JPanel createCertificatesTab() {
        JPanel panel = createTabCard();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel title = new JLabel("Earned Certificates");
        title.setFont(StyleUtils.FONT_SUBHEADER);
        title.setForeground(StyleUtils.TEXT_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        
        List<Certificate> certificates = dataManager.getCertificatesForUser(currentStudent.getId());
        
        if (certificates.isEmpty()) {
            JLabel emptyLabel = new JLabel("🏆 No certificates yet. Complete courses to earn certificates!");
            emptyLabel.setFont(StyleUtils.FONT_BODY);
            emptyLabel.setForeground(StyleUtils.TEXT_MUTED);
            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(emptyLabel);
        } else {
            for (Certificate cert : certificates) {
                panel.add(createCertificateCard(cert));
                panel.add(Box.createVerticalStrut(15));
            }
        }
        
        return panel;
    }
    
    /**
     * Create a certificate display card.
     */
    private JPanel createCertificateCard(Certificate cert) {
        JPanel card = new JPanel(new BorderLayout(20, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Golden gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 215, 0, 30),
                    getWidth(), 0, new Color(255, 193, 7, 50));
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Border
                g2.setColor(new Color(255, 193, 7));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Left: Trophy icon
        JLabel trophyLabel = new JLabel("🏆");
        trophyLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        card.add(trophyLabel, BorderLayout.WEST);
        
        // Center: Certificate info
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        JLabel nameLabel = new JLabel(cert.getCertificateName());
        nameLabel.setFont(StyleUtils.FONT_SUBHEADER);
        nameLabel.setForeground(StyleUtils.TEXT_DARK);
        infoPanel.add(nameLabel);
        
        JLabel categoryLabel = new JLabel("Category: " + cert.getCategory());
        categoryLabel.setFont(StyleUtils.FONT_BODY);
        categoryLabel.setForeground(StyleUtils.TEXT_MUTED);
        infoPanel.add(categoryLabel);
        
        JLabel dateLabel = new JLabel("Earned on " + cert.getIssueDate().format(
            DateTimeFormatter.ofPattern("MMMM d, yyyy")));
        dateLabel.setFont(StyleUtils.FONT_SMALL);
        dateLabel.setForeground(StyleUtils.TEXT_MUTED);
        infoPanel.add(dateLabel);
        
        JLabel lessonsLabel = new JLabel(cert.getLessonsCompleted() + " lessons completed");
        lessonsLabel.setFont(StyleUtils.FONT_SMALL);
        lessonsLabel.setForeground(StyleUtils.SUCCESS_GREEN);
        infoPanel.add(lessonsLabel);
        
        card.add(infoPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Create a styled tab content card.
     */
    private JPanel createTabCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(StyleUtils.CARD_WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(25, 25, 25, 25));
        return card;
    }
}
