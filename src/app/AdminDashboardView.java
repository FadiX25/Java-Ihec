package app;

import services.CsvDataManager;
import services.DataManager;
import services.FirebaseDataManager;
import model.Admin;
import model.Lesson;
import model.Student;
import model.User;
import utils.StyleUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * AdminDashboardView - A modern, clean admin control panel.
 * 
 * DESIGN PRINCIPLES:
 * 1. Clear Visual Hierarchy - Important info stands out
 * 2. Consistent Spacing - 20px padding, 15px gaps
 * 3. Subtle Shadows - Depth without clutter
 * 4. Color Coding - Green=Add, Red=Delete, Blue=Info
 *
 * LAYOUT:
 * ┌─────────────────────────────────────────────────────┐
 * │  🎓 IHEC-JLearn   [ADMIN]              👤 [Logout]  │
 * ├─────────────────────────────────────────────────────┤
 * │  ┌─────────┐  ┌─────────┐  ┌─────────┐              │
 * │  │ 👥  2   │  │ 📚  3   │  │ ⭐ 170  │  Stats      │
 * │  │Students │  │Courses  │  │Total XP │              │
 * │  └─────────┘  └─────────┘  └─────────┘              │
 * │  ┌─────────────────────────────────────────────┐    │
 * │  │ [👥 Members] [📚 Courses]                   │    │
 * │  │  ┌─────────────────────────────────────┐    │    │
 * │  │  │ ID │ Username │ Role    │ XP       │    │    │
 * │  │  │  1 │ ahmed    │ STUDENT │ 50       │    │    │
 * │  │  └─────────────────────────────────────┘    │    │
 * │  │  [🔄 Refresh]  [➕ Add]  [🗑️ Delete]        │    │
 * │  └─────────────────────────────────────────────┘    │
 * └─────────────────────────────────────────────────────┘
 */
public class AdminDashboardView extends JPanel {

    // ==================== REFERENCES ====================
    
    private MainApplication parentApp;
    private DataManager dataManager;

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
        
        // Main content with tabs
        add(createMainContent(), BorderLayout.CENTER);
    }
    
    /**
     * Create the top navigation bar with modern styling.
     */
    private JPanel createNavBar() {
        JPanel navBar = StyleUtils.createGradientHeader(80);
        navBar.setBorder(new EmptyBorder(0, 30, 0, 30));
        
        // Left side: App title + Admin badge
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 18));
        leftPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(StyleUtils.APP_NAME);
        titleLabel.setFont(StyleUtils.FONT_SUBHEADER);
        titleLabel.setForeground(StyleUtils.TEXT_LIGHT);
        leftPanel.add(titleLabel);
        
        // Admin badge with accent color and rounded corners
        JLabel adminBadge = new JLabel("ADMIN") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(StyleUtils.ADMIN_ACCENT);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        adminBadge.setFont(StyleUtils.FONT_SMALL.deriveFont(Font.BOLD));
        adminBadge.setForeground(StyleUtils.TEXT_DARK);
        adminBadge.setOpaque(false);
        adminBadge.setBorder(new EmptyBorder(4, 10, 4, 10));
        leftPanel.add(adminBadge);
        
        navBar.add(leftPanel, BorderLayout.WEST);
        
        // Right side: User icon + Logout button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        rightPanel.setOpaque(false);
        
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        rightPanel.add(userIcon);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(StyleUtils.FONT_BODY);
        logoutBtn.setBackground(StyleUtils.CARD_WHITE);
        logoutBtn.setForeground(StyleUtils.PRIMARY_BLUE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(new CompoundBorder(
            new LineBorder(StyleUtils.CARD_WHITE, 1),
            new EmptyBorder(8, 20, 8, 20)
        ));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> parentApp.logout());
        
        // Hover effect
        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(StyleUtils.BACKGROUND_GRAY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(StyleUtils.CARD_WHITE);
            }
        });
        
        rightPanel.add(logoutBtn);
        navBar.add(rightPanel, BorderLayout.EAST);
        
        return navBar;
    }
    
    /**
     * Create the main content area with styled tabs.
     */
    private JPanel createMainContent() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(StyleUtils.BACKGROUND_GRAY);
        contentPanel.setBorder(new EmptyBorder(20, 25, 25, 25));
        
        // Statistics panel at top
        contentPanel.add(createStatsPanel(), BorderLayout.NORTH);
        
        // Custom styled tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(StyleUtils.FONT_BUTTON);
        tabbedPane.setBackground(StyleUtils.CARD_WHITE);
        tabbedPane.setForeground(StyleUtils.TEXT_DARK);
        
        // Remove default border, add custom styling
        tabbedPane.setBorder(new CompoundBorder(
            new LineBorder(StyleUtils.BORDER_LIGHT, 1),
            new EmptyBorder(0, 0, 0, 0)
        ));
        
        // Add tabs with clear labels
        tabbedPane.addTab("  👥  Members  ", createMembersPanel());
        tabbedPane.addTab("  📚  Courses  ", createCoursesPanel());
        
        // Wrap in a card-like container
        JPanel tabWrapper = new JPanel(new BorderLayout());
        tabWrapper.setBackground(StyleUtils.CARD_WHITE);
        tabWrapper.add(tabbedPane, BorderLayout.CENTER);
        
        contentPanel.add(tabWrapper, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    /**
     * Create the statistics panel showing quick stats.
     */
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(StyleUtils.BACKGROUND_GRAY);
        statsPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Total Students card (Blue theme)
        totalStudentsLabel = new JLabel("0");
        statsPanel.add(createStatCard("Total Students", totalStudentsLabel, "👥", 
                       StyleUtils.PRIMARY_BLUE, new Color(0, 86, 210, 20)));
        
        // Total Courses card (Green theme)
        totalCoursesLabel = new JLabel("0");
        statsPanel.add(createStatCard("Total Courses", totalCoursesLabel, "📚", 
                       StyleUtils.SUCCESS_GREEN, new Color(40, 167, 69, 20)));
        
        // Total XP earned card (Amber theme)
        totalXpLabel = new JLabel("0");
        statsPanel.add(createStatCard("Total XP Earned", totalXpLabel, "⭐", 
                       StyleUtils.ADMIN_ACCENT, new Color(245, 158, 11, 20)));
        
        return statsPanel;
    }
    
    /**
     * Create a modern stat card with accent color.
     */
    private JPanel createStatCard(String title, JLabel valueLabel, String emoji, 
                                   Color accentColor, Color bgTint) {
        // Outer card with subtle shadow effect
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw subtle left accent bar
                g.setColor(accentColor);
                g.fillRect(0, 0, 4, getHeight());
            }
        };
        card.setBackground(StyleUtils.CARD_WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(StyleUtils.BORDER_LIGHT, 1),
            new EmptyBorder(20, 24, 20, 20)
        ));
        
        // Left: Large emoji with colored background circle
        JPanel iconPanel = new JPanel(new GridBagLayout());
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(60, 60));
        
        JLabel emojiLabel = new JLabel(emoji) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgTint);
                g2.fillOval(0, 0, 50, 50);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        emojiLabel.setPreferredSize(new Dimension(50, 50));
        emojiLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconPanel.add(emojiLabel);
        card.add(iconPanel, BorderLayout.WEST);
        
        // Right: Text content (value + title)
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(StyleUtils.CARD_WHITE);
        textPanel.setBorder(new EmptyBorder(5, 15, 5, 0));
        
        // Large value number
        valueLabel.setFont(StyleUtils.FONT_STAT_NUMBER);
        valueLabel.setForeground(StyleUtils.TEXT_DARK);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(valueLabel);
        
        // Title below
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(StyleUtils.FONT_SMALL);
        titleLabel.setForeground(StyleUtils.TEXT_MUTED);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(titleLabel);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }

    // ==================== MEMBERS TAB ====================
    
    /**
     * Create the Members management panel with improved styling.
     */
    private JPanel createMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(StyleUtils.CARD_WHITE);
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        // Header section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleUtils.CARD_WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 25, 15, 25));
        
        JLabel sectionTitle = new JLabel("Member Management");
        sectionTitle.setFont(StyleUtils.FONT_SUBHEADER);
        sectionTitle.setForeground(StyleUtils.TEXT_DARK);
        headerPanel.add(sectionTitle, BorderLayout.WEST);
        
        JLabel sectionHint = new JLabel("View and manage all registered users");
        sectionHint.setFont(StyleUtils.FONT_SMALL);
        sectionHint.setForeground(StyleUtils.TEXT_MUTED);
        headerPanel.add(sectionHint, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Table columns
        String[] columns = {"ID", "Username", "Role", "XP Score"};
        membersTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        membersTable = createStyledTable(membersTableModel);
        
        JScrollPane scrollPane = new JScrollPane(membersTable);
        scrollPane.setBorder(new MatteBorder(1, 0, 1, 0, StyleUtils.BORDER_LIGHT));
        scrollPane.getViewport().setBackground(StyleUtils.CARD_WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel with modern styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 15));
        buttonPanel.setBackground(StyleUtils.CARD_WHITE);
        buttonPanel.setBorder(new EmptyBorder(5, 15, 10, 15));
        
        JButton refreshBtn = createModernButton("🔄  Refresh", StyleUtils.PRIMARY_BLUE, false);
        refreshBtn.addActionListener(e -> loadMembers());
        buttonPanel.add(refreshBtn);
        
        JButton deleteBtn = createModernButton("🗑️  Delete User", StyleUtils.ERROR_RED, false);
        deleteBtn.addActionListener(e -> deleteSelectedMember());
        buttonPanel.add(deleteBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Create a styled JTable with alternating rows and better formatting.
     */
    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                
                // Alternating row colors
                if (!isRowSelected(row)) {
                    comp.setBackground(row % 2 == 0 ? StyleUtils.CARD_WHITE : StyleUtils.TABLE_ROW_ALT);
                } else {
                    comp.setBackground(new Color(0, 86, 210, 40));  // Light blue selection
                }
                
                return comp;
            }
        };
        
        // Table styling
        table.setFont(StyleUtils.FONT_BODY);
        table.setRowHeight(45);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(StyleUtils.BORDER_LIGHT);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(0, 86, 210, 40));
        table.setSelectionForeground(StyleUtils.TEXT_DARK);
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(StyleUtils.FONT_TABLE_HEADER);
        header.setBackground(StyleUtils.BACKGROUND_GRAY);
        header.setForeground(StyleUtils.TEXT_DARK);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setBorder(new MatteBorder(0, 0, 2, 0, StyleUtils.PRIMARY_BLUE));
        
        // Center-align certain columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);  // ID
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);  // XP
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(200);  // Username
        table.getColumnModel().getColumn(2).setPreferredWidth(100);  // Role
        table.getColumnModel().getColumn(3).setPreferredWidth(100);  // XP
        
        return table;
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
     * Create the Courses management panel with improved styling.
     */
    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(StyleUtils.CARD_WHITE);
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        // Header section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleUtils.CARD_WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 25, 15, 25));
        
        JLabel sectionTitle = new JLabel("Course Management");
        sectionTitle.setFont(StyleUtils.FONT_SUBHEADER);
        sectionTitle.setForeground(StyleUtils.TEXT_DARK);
        headerPanel.add(sectionTitle, BorderLayout.WEST);
        
        JLabel sectionHint = new JLabel("Add, view, and manage learning content");
        sectionHint.setFont(StyleUtils.FONT_SMALL);
        sectionHint.setForeground(StyleUtils.TEXT_MUTED);
        headerPanel.add(sectionHint, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Table columns
        String[] columns = {"ID", "Title", "Date Created", "Answer Keyword"};
        coursesTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        coursesTable = createStyledCoursesTable(coursesTableModel);
        
        JScrollPane scrollPane = new JScrollPane(coursesTable);
        scrollPane.setBorder(new MatteBorder(1, 0, 1, 0, StyleUtils.BORDER_LIGHT));
        scrollPane.getViewport().setBackground(StyleUtils.CARD_WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel with modern styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 15));
        buttonPanel.setBackground(StyleUtils.CARD_WHITE);
        buttonPanel.setBorder(new EmptyBorder(5, 15, 10, 15));
        
        JButton refreshBtn = createModernButton("🔄  Refresh", StyleUtils.PRIMARY_BLUE, false);
        refreshBtn.addActionListener(e -> loadCourses());
        buttonPanel.add(refreshBtn);
        
        JButton addBtn = createModernButton("➕  Add Course", StyleUtils.SUCCESS_GREEN, true);
        addBtn.addActionListener(e -> showAddCourseDialog());
        buttonPanel.add(addBtn);
        
        JButton deleteBtn = createModernButton("🗑️  Delete Course", StyleUtils.ERROR_RED, false);
        deleteBtn.addActionListener(e -> deleteSelectedCourse());
        buttonPanel.add(deleteBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Create a styled JTable for courses.
     */
    private JTable createStyledCoursesTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                
                if (!isRowSelected(row)) {
                    comp.setBackground(row % 2 == 0 ? StyleUtils.CARD_WHITE : StyleUtils.TABLE_ROW_ALT);
                } else {
                    comp.setBackground(new Color(40, 167, 69, 30));  // Light green selection
                }
                
                return comp;
            }
        };
        
        // Table styling
        table.setFont(StyleUtils.FONT_BODY);
        table.setRowHeight(45);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(StyleUtils.BORDER_LIGHT);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(40, 167, 69, 30));
        table.setSelectionForeground(StyleUtils.TEXT_DARK);
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(StyleUtils.FONT_TABLE_HEADER);
        header.setBackground(StyleUtils.BACKGROUND_GRAY);
        header.setForeground(StyleUtils.TEXT_DARK);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setBorder(new MatteBorder(0, 0, 2, 0, StyleUtils.SUCCESS_GREEN));
        
        // Center-align ID column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(250);  // Title
        table.getColumnModel().getColumn(2).setPreferredWidth(120);  // Date
        table.getColumnModel().getColumn(3).setPreferredWidth(150);  // Answer
        
        return table;
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
     * Show a modern dialog to add a new course.
     */
    private void showAddCourseDialog() {
        // Create dialog
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                     "Add New Course", true);
        dialog.setSize(550, 520);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(StyleUtils.BACKGROUND_GRAY);
        
        // Header panel with title
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleUtils.PRIMARY_BLUE);
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        JLabel headerTitle = new JLabel("📚  Create New Course");
        headerTitle.setFont(StyleUtils.FONT_SUBHEADER);
        headerTitle.setForeground(StyleUtils.TEXT_LIGHT);
        headerPanel.add(headerTitle, BorderLayout.WEST);
        
        dialog.add(headerPanel, BorderLayout.NORTH);
        
        // Form panel with card styling
        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(StyleUtils.CARD_WHITE);
        formCard.setBorder(new CompoundBorder(
            new EmptyBorder(20, 25, 20, 25),
            new EmptyBorder(0, 0, 0, 0)
        ));
        
        // Course Title field
        JTextField titleField = createFormField(formCard, "Course Title", 
                                                 "Enter a descriptive course title");
        
        // YouTube Video ID field
        JTextField youtubeField = createFormField(formCard, "YouTube Video ID", 
                                                   "e.g., dQw4w9WgXcQ");
        
        // Answer Keyword field
        JTextField answerField = createFormField(formCard, "Correct Answer Keyword", 
                                                  "The keyword students must include");
        
        // Theory Text area
        formCard.add(Box.createVerticalStrut(15));
        JLabel theoryLabel = new JLabel("Theory Text");
        theoryLabel.setFont(StyleUtils.FONT_BUTTON);
        theoryLabel.setForeground(StyleUtils.TEXT_DARK);
        theoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(theoryLabel);
        formCard.add(Box.createVerticalStrut(5));
        
        JTextArea theoryArea = new JTextArea(4, 20);
        theoryArea.setFont(StyleUtils.FONT_BODY);
        theoryArea.setLineWrap(true);
        theoryArea.setWrapStyleWord(true);
        theoryArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane theoryScroll = new JScrollPane(theoryArea);
        theoryScroll.setBorder(new LineBorder(StyleUtils.BORDER_LIGHT, 1));
        theoryScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        theoryScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        formCard.add(theoryScroll);
        
        // Wrap form in scroll pane
        JScrollPane formScroll = new JScrollPane(formCard);
        formScroll.setBorder(new EmptyBorder(15, 15, 15, 15));
        formScroll.getViewport().setBackground(StyleUtils.BACKGROUND_GRAY);
        dialog.add(formScroll, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setBackground(StyleUtils.BACKGROUND_GRAY);
        buttonPanel.setBorder(new EmptyBorder(0, 25, 15, 25));
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(StyleUtils.FONT_BODY);
        cancelBtn.setBackground(StyleUtils.CARD_WHITE);
        cancelBtn.setForeground(StyleUtils.TEXT_DARK);
        cancelBtn.setBorder(new CompoundBorder(
            new LineBorder(StyleUtils.BORDER_LIGHT, 1),
            new EmptyBorder(10, 25, 10, 25)
        ));
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelBtn);
        
        JButton saveBtn = new JButton("Save Course");
        saveBtn.setFont(StyleUtils.FONT_BUTTON);
        saveBtn.setBackground(StyleUtils.SUCCESS_GREEN);
        saveBtn.setForeground(StyleUtils.TEXT_LIGHT);
        saveBtn.setBorder(new EmptyBorder(10, 30, 10, 30));
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
            
            // Generate new ID
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
                "Course \"" + title + "\" added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPanel.add(saveBtn);
        
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Show dialog
        dialog.setVisible(true);
    }
    
    /**
     * Helper to create a styled form field.
     */
    private JTextField createFormField(JPanel parent, String labelText, String placeholder) {
        parent.add(Box.createVerticalStrut(15));
        
        JLabel label = new JLabel(labelText);
        label.setFont(StyleUtils.FONT_BUTTON);
        label.setForeground(StyleUtils.TEXT_DARK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(label);
        parent.add(Box.createVerticalStrut(5));
        
        JTextField field = new JTextField();
        field.setFont(StyleUtils.FONT_BODY);
        field.setBorder(new CompoundBorder(
            new LineBorder(StyleUtils.BORDER_LIGHT, 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Placeholder hint
        field.setForeground(StyleUtils.TEXT_MUTED);
        field.setText(placeholder);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(StyleUtils.TEXT_DARK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setForeground(StyleUtils.TEXT_MUTED);
                    field.setText(placeholder);
                }
            }
        });
        
        parent.add(field);
        return field;
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
     * Create a modern styled button with hover effects.
     */
    private JButton createModernButton(String text, Color bgColor, boolean isPrimary) {
        JButton button = new JButton(text);
        button.setFont(StyleUtils.FONT_BUTTON);
        button.setBackground(bgColor);
        button.setForeground(StyleUtils.TEXT_LIGHT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        // Make primary buttons slightly larger
        if (isPrimary) {
            button.setBorder(new EmptyBorder(12, 25, 12, 25));
        }
        
        // Hover effect
        Color hoverColor = bgColor.darker();
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * Create a styled action button (legacy support).
     */
    private JButton createActionButton(String text, Color bgColor) {
        return createModernButton(text, bgColor, false);
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
