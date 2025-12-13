package view;

import controller.CsvDataManager;
import model.Admin;
import model.Lesson;
import model.Student;
import model.User;
import utils.StyleUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * AdminDashboardView - The admin control panel.
 * 
 * FEATURES:
 * 1. Member Management - View all students and their XP
 * 2. Course Management - View, add, and delete lessons
 * 
 * SWING COMPONENTS USED:
 * - JTabbedPane: Creates tabs for different sections
 * - JTable: Displays data in rows and columns
 * - JScrollPane: Makes tables scrollable
 * - JDialog: Pop-up windows for adding lessons
 * 
 * LAYOUT:
 * ┌─────────────────────────────────────────┐
 * │           NAVIGATION BAR                │
 * ├─────────────────────────────────────────┤
 * │  [Members]  [Courses]  <- Tabs          │
 * │  ┌─────────────────────────────────┐    │
 * │  │                                 │    │
 * │  │     Table with data             │    │
 * │  │                                 │    │
 * │  └─────────────────────────────────┘    │
 * │  [Add New] [Delete] [Refresh]           │
 * └─────────────────────────────────────────┘
 */
public class AdminDashboardView extends JPanel {

    // ==================== REFERENCES ====================
    
    private MainApplication parentApp;
    private CsvDataManager dataManager;
    
    // ==================== UI COMPONENTS ====================
    
    // Tables for displaying data
    private JTable membersTable;
    private JTable coursesTable;
    
    // Table models (hold the data)
    private DefaultTableModel membersTableModel;
    private DefaultTableModel coursesTableModel;
    
    // Labels for statistics
    private JLabel totalStudentsLabel;
    private JLabel totalCoursesLabel;
    private JLabel totalXpLabel;

    // ==================== CONSTRUCTOR ====================
    
    public AdminDashboardView(MainApplication parentApp) {
        this.parentApp = parentApp;
        this.dataManager = new CsvDataManager();
        
        initializeUI();
    }

    // ==================== UI INITIALIZATION ====================
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(StyleUtils.BACKGROUND_GRAY);
        
        // Top navigation bar
        add(createNavBar(), BorderLayout.NORTH);
        
        // Main content with tabs
        add(createMainContent(), BorderLayout.CENTER);
    }
    
    /**
     * Create the top navigation bar.
     */
    private JPanel createNavBar() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(StyleUtils.PRIMARY_BLUE);
        navBar.setPreferredSize(new Dimension(0, 70));
        navBar.setBorder(new EmptyBorder(15, 25, 15, 25));
        
        // Left side: App title + Admin badge
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("IHEC-JLearn");
        titleLabel.setFont(StyleUtils.FONT_SUBHEADER);
        titleLabel.setForeground(StyleUtils.TEXT_LIGHT);
        leftPanel.add(titleLabel);
        
        JLabel adminBadge = new JLabel("ADMIN");
        adminBadge.setFont(StyleUtils.FONT_BODY);
        adminBadge.setForeground(StyleUtils.PRIMARY_BLUE);
        adminBadge.setBackground(StyleUtils.CARD_WHITE);
        adminBadge.setOpaque(true);
        adminBadge.setBorder(new EmptyBorder(5, 10, 5, 10));
        leftPanel.add(adminBadge);
        
        navBar.add(leftPanel, BorderLayout.WEST);
        
        // Right side: Logout button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(StyleUtils.FONT_BODY);
        logoutBtn.setBackground(StyleUtils.CARD_WHITE);
        logoutBtn.setForeground(StyleUtils.PRIMARY_BLUE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> parentApp.logout());
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(logoutBtn);
        navBar.add(rightPanel, BorderLayout.EAST);
        
        return navBar;
    }
    
    /**
     * Create the main content area with tabs.
     */
    private JPanel createMainContent() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(StyleUtils.BACKGROUND_GRAY);
        contentPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        // Statistics panel at top
        contentPanel.add(createStatsPanel(), BorderLayout.NORTH);
        
        // Tabbed pane for Members and Courses
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(StyleUtils.FONT_BODY);
        tabbedPane.setBackground(StyleUtils.CARD_WHITE);
        
        // Add tabs
        tabbedPane.addTab("👥 Members", createMembersPanel());
        tabbedPane.addTab("📚 Courses", createCoursesPanel());
        
        contentPanel.add(tabbedPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    /**
     * Create the statistics panel showing quick stats.
     */
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(StyleUtils.BACKGROUND_GRAY);
        statsPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Total Students card
        totalStudentsLabel = new JLabel("0");
        statsPanel.add(createStatCard("Total Students", totalStudentsLabel, "👥"));
        
        // Total Courses card
        totalCoursesLabel = new JLabel("0");
        statsPanel.add(createStatCard("Total Courses", totalCoursesLabel, "📚"));
        
        // Total XP earned card
        totalXpLabel = new JLabel("0");
        statsPanel.add(createStatCard("Total XP Earned", totalXpLabel, "⭐"));
        
        return statsPanel;
    }
    
    /**
     * Create a single stat card.
     */
    private JPanel createStatCard(String title, JLabel valueLabel, String emoji) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(StyleUtils.CARD_WHITE);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Emoji icon
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        card.add(emojiLabel, BorderLayout.WEST);
        
        // Text content
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(StyleUtils.CARD_WHITE);
        textPanel.setBorder(new EmptyBorder(0, 15, 0, 0));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(StyleUtils.FONT_BODY);
        titleLabel.setForeground(StyleUtils.TEXT_MUTED);
        textPanel.add(titleLabel);
        
        valueLabel.setFont(StyleUtils.FONT_HEADER);
        valueLabel.setForeground(StyleUtils.TEXT_DARK);
        textPanel.add(valueLabel);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }

    // ==================== MEMBERS TAB ====================
    
    /**
     * Create the Members management panel.
     */
    private JPanel createMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(StyleUtils.CARD_WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Table columns
        String[] columns = {"ID", "Username", "Role", "XP Score"};
        membersTableModel = new DefaultTableModel(columns, 0) {
            // Make table non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        membersTable = new JTable(membersTableModel);
        membersTable.setFont(StyleUtils.FONT_BODY);
        membersTable.setRowHeight(35);
        membersTable.getTableHeader().setFont(StyleUtils.FONT_BUTTON);
        membersTable.getTableHeader().setBackground(StyleUtils.PRIMARY_BLUE);
        membersTable.getTableHeader().setForeground(StyleUtils.TEXT_LIGHT);
        membersTable.setSelectionBackground(StyleUtils.BACKGROUND_GRAY);
        
        JScrollPane scrollPane = new JScrollPane(membersTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(StyleUtils.BACKGROUND_GRAY));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(StyleUtils.CARD_WHITE);
        
        JButton refreshBtn = createActionButton("🔄 Refresh", StyleUtils.PRIMARY_BLUE);
        refreshBtn.addActionListener(e -> loadMembers());
        buttonPanel.add(refreshBtn);
        
        JButton deleteBtn = createActionButton("🗑️ Delete User", StyleUtils.ERROR_RED);
        deleteBtn.addActionListener(e -> deleteSelectedMember());
        buttonPanel.add(deleteBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Load all members into the table.
     */
    private void loadMembers() {
        // Clear existing data
        membersTableModel.setRowCount(0);
        
        // Load users from CSV
        List<User> users = dataManager.loadUsers();
        
        int studentCount = 0;
        int totalXp = 0;
        
        for (User user : users) {
            int xp = 0;
            if (user instanceof Student) {
                xp = ((Student) user).getXpScore();
                studentCount++;
                totalXp += xp;
            }
            
            // Add row to table
            membersTableModel.addRow(new Object[]{
                user.getId(),
                user.getUsername(),
                user.getRole(),
                xp
            });
        }
        
        // Update stats
        totalStudentsLabel.setText(String.valueOf(studentCount));
        totalXpLabel.setText(String.valueOf(totalXp));
    }
    
    /**
     * Delete the selected member.
     */
    private void deleteSelectedMember() {
        int selectedRow = membersTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a user to delete.",
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get user info
        int userId = (int) membersTableModel.getValueAt(selectedRow, 0);
        String username = (String) membersTableModel.getValueAt(selectedRow, 1);
        String role = (String) membersTableModel.getValueAt(selectedRow, 2);
        
        // Prevent deleting admins
        if (role.equals("ADMIN")) {
            JOptionPane.showMessageDialog(this,
                "Cannot delete admin accounts!",
                "Action Denied",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete user: " + username + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dataManager.deleteUser(userId);
            loadMembers();  // Refresh table
            JOptionPane.showMessageDialog(this,
                "User deleted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ==================== COURSES TAB ====================
    
    /**
     * Create the Courses management panel.
     */
    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(StyleUtils.CARD_WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Table columns
        String[] columns = {"ID", "Title", "Date Created", "Answer Keyword"};
        coursesTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        coursesTable = new JTable(coursesTableModel);
        coursesTable.setFont(StyleUtils.FONT_BODY);
        coursesTable.setRowHeight(35);
        coursesTable.getTableHeader().setFont(StyleUtils.FONT_BUTTON);
        coursesTable.getTableHeader().setBackground(StyleUtils.PRIMARY_BLUE);
        coursesTable.getTableHeader().setForeground(StyleUtils.TEXT_LIGHT);
        coursesTable.setSelectionBackground(StyleUtils.BACKGROUND_GRAY);
        
        JScrollPane scrollPane = new JScrollPane(coursesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(StyleUtils.BACKGROUND_GRAY));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(StyleUtils.CARD_WHITE);
        
        JButton refreshBtn = createActionButton("🔄 Refresh", StyleUtils.PRIMARY_BLUE);
        refreshBtn.addActionListener(e -> loadCourses());
        buttonPanel.add(refreshBtn);
        
        JButton addBtn = createActionButton("➕ Add Course", StyleUtils.SUCCESS_GREEN);
        addBtn.addActionListener(e -> showAddCourseDialog());
        buttonPanel.add(addBtn);
        
        JButton deleteBtn = createActionButton("🗑️ Delete Course", StyleUtils.ERROR_RED);
        deleteBtn.addActionListener(e -> deleteSelectedCourse());
        buttonPanel.add(deleteBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Load all courses into the table.
     */
    private void loadCourses() {
        // Clear existing data
        coursesTableModel.setRowCount(0);
        
        // Load lessons from CSV
        List<Lesson> lessons = dataManager.loadLessons();
        
        for (Lesson lesson : lessons) {
            coursesTableModel.addRow(new Object[]{
                lesson.getId(),
                lesson.getTitle(),
                lesson.getDateCreated(),
                lesson.getCorrectAnswer()
            });
        }
        
        // Update stats
        totalCoursesLabel.setText(String.valueOf(lessons.size()));
    }
    
    /**
     * Show dialog to add a new course.
     */
    private void showAddCourseDialog() {
        // Create dialog
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                     "Add New Course", true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 15));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(StyleUtils.CARD_WHITE);
        
        // Course Title
        formPanel.add(createFormLabel("Course Title:"));
        JTextField titleField = new JTextField();
        titleField.setFont(StyleUtils.FONT_BODY);
        formPanel.add(titleField);
        
        // YouTube Video ID
        formPanel.add(createFormLabel("YouTube Video ID:"));
        JTextField youtubeField = new JTextField();
        youtubeField.setFont(StyleUtils.FONT_BODY);
        formPanel.add(youtubeField);
        
        // Answer Keyword
        formPanel.add(createFormLabel("Correct Answer Keyword:"));
        JTextField answerField = new JTextField();
        answerField.setFont(StyleUtils.FONT_BODY);
        formPanel.add(answerField);
        
        // Theory Text (larger)
        formPanel.add(createFormLabel("Theory Text:"));
        JTextArea theoryArea = new JTextArea(3, 20);
        theoryArea.setFont(StyleUtils.FONT_BODY);
        theoryArea.setLineWrap(true);
        theoryArea.setWrapStyleWord(true);
        JScrollPane theoryScroll = new JScrollPane(theoryArea);
        formPanel.add(theoryScroll);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(StyleUtils.CARD_WHITE);
        buttonPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(StyleUtils.FONT_BODY);
        cancelBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelBtn);
        
        JButton saveBtn = new JButton("Save Course");
        saveBtn.setFont(StyleUtils.FONT_BUTTON);
        saveBtn.setBackground(StyleUtils.SUCCESS_GREEN);
        saveBtn.setForeground(StyleUtils.TEXT_LIGHT);
        saveBtn.addActionListener(e -> {
            // Validate input
            String title = titleField.getText().trim();
            String youtubeId = youtubeField.getText().trim();
            String answer = answerField.getText().trim();
            String theory = theoryArea.getText().trim();
            
            if (title.isEmpty() || youtubeId.isEmpty() || answer.isEmpty() || theory.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Please fill in all fields!",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Generate new ID (max existing + 1)
            int newId = getNextLessonId();
            
            // Create lesson
            Lesson newLesson = new Lesson(
                newId,
                title,
                youtubeId,
                LocalDate.now(),
                answer,
                theory
            );
            
            // Save to CSV
            dataManager.addLesson(newLesson);
            
            // Refresh and close
            loadCourses();
            dialog.dispose();
            
            JOptionPane.showMessageDialog(this,
                "Course added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPanel.add(saveBtn);
        
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Show dialog
        dialog.setVisible(true);
    }
    
    /**
     * Get the next available lesson ID.
     */
    private int getNextLessonId() {
        List<Lesson> lessons = dataManager.loadLessons();
        int maxId = 100;  // Start from 100 for lessons
        
        for (Lesson lesson : lessons) {
            if (lesson.getId() > maxId) {
                maxId = lesson.getId();
            }
        }
        
        return maxId + 1;
    }
    
    /**
     * Delete the selected course.
     */
    private void deleteSelectedCourse() {
        int selectedRow = coursesTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a course to delete.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get course info
        int courseId = (int) coursesTableModel.getValueAt(selectedRow, 0);
        String title = (String) coursesTableModel.getValueAt(selectedRow, 1);
        
        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete: " + title + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dataManager.deleteLesson(courseId);
            loadCourses();  // Refresh table
            JOptionPane.showMessageDialog(this,
                "Course deleted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Create a styled action button.
     */
    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(StyleUtils.FONT_BUTTON);
        button.setBackground(bgColor);
        button.setForeground(StyleUtils.TEXT_LIGHT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Create a form label.
     */
    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(StyleUtils.FONT_BODY);
        label.setForeground(StyleUtils.TEXT_DARK);
        return label;
    }
    
    // ==================== PUBLIC METHODS ====================
    
    /**
     * Refresh the dashboard for the current admin.
     * Called when admin logs in.
     */
    public void refreshForAdmin(Admin admin) {
        loadMembers();
        loadCourses();
    }
}
