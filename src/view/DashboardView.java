package view;

import controller.CsvDataManager;
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
 * DashboardView - Modern dashboard with hero banner, stats, and course cards.
 */
public class DashboardView extends JPanel {

    // ==================== REFERENCES ====================

    private MainApplication parentApp;
    private CsvDataManager dataManager;
    private User currentUser;

    // UI Components that need updating
    private JLabel xpLabel;
    private JPanel coursesPanel;
    private JLabel statsCoursLabel;
    private JLabel statsCertLabel;
    private JLabel statsGoalsLabel;
    private JLabel statsDaysLabel;

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
        navBar.setBackground(Color.WHITE);
        navBar.setPreferredSize(new Dimension(0, 70));
        navBar.setBorder(new EmptyBorder(15, 25, 15, 25));

        // Left side: App logo
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        // Logo icon
        JLabel logoIcon = new JLabel("📚");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        JLabel titleLabel = new JLabel("Ihec-LearnHub");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(StyleUtils.TEXT_DARK);

        leftPanel.add(logoIcon);
        leftPanel.add(titleLabel);
        navBar.add(leftPanel, BorderLayout.WEST);

        // Right side: XP and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);

        // XP display with icon
        JPanel xpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        xpPanel.setOpaque(false);

        xpLabel = new JLabel("0 XP");
        xpLabel.setFont(StyleUtils.FONT_BODY);
        xpLabel.setForeground(StyleUtils.TEXT_DARK);
        xpPanel.add(xpLabel);
        rightPanel.add(xpPanel);

        // Logout button
        JButton logoutBtn = createModernButton("Déconnexion",
                StyleUtils.PRIMARY_BLUE, Color.WHITE);
        logoutBtn.setPreferredSize(new Dimension(120, 38));
        logoutBtn.addActionListener(e -> parentApp.logout());
        rightPanel.add(logoutBtn);

        navBar.add(rightPanel, BorderLayout.EAST);

        return navBar;
    }

    /**
     * Create the main content area.
     */
    private JPanel createContentArea() {
        JPanel contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(StyleUtils.BACKGROUND_GRAY);
        contentArea.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Main scroll panel
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(StyleUtils.BACKGROUND_GRAY);

        // Hero Banner
        mainContent.add(createHeroBanner());
        mainContent.add(Box.createVerticalStrut(30));

        // Stats Grid
        mainContent.add(createStatsGrid());
        mainContent.add(Box.createVerticalStrut(30));

        // Section Header
        mainContent.add(createSectionHeader());
        mainContent.add(Box.createVerticalStrut(20));

        // Courses grid (will be populated when user logs in)
        coursesPanel = new JPanel();
        coursesPanel.setLayout(new GridLayout(0, 3, 20, 20));  // 3 columns
        coursesPanel.setBackground(StyleUtils.BACKGROUND_GRAY);
        mainContent.add(coursesPanel);

        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(StyleUtils.BACKGROUND_GRAY);
        scrollPane.getViewport().setBackground(StyleUtils.BACKGROUND_GRAY);

        contentArea.add(scrollPane, BorderLayout.CENTER);

        return contentArea;
    }

    /**
     * Create the hero banner with gradient background.
     */
    private JPanel createHeroBanner() {
        JPanel hero = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                GradientPaint gradient = new GradientPaint(
                        0, 0, StyleUtils.PRIMARY_BLUE,
                        getWidth(), getHeight(), StyleUtils.PRIMARY_PURPLE
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };

        hero.setLayout(new BorderLayout());
        hero.setPreferredSize(new Dimension(0, 220));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        hero.setBorder(new EmptyBorder(40, 40, 40, 40));
        hero.setOpaque(false);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        // Title
        JLabel title = new JLabel("Bonjour, Étudiant IHEC ! 👋");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Subtitle
        JLabel subtitle = new JLabel("Continuez votre apprentissage là où vous vous êtes arrêté");
        subtitle.setFont(StyleUtils.FONT_BODY);
        subtitle.setForeground(StyleUtils.withAlpha(Color.WHITE, 240));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton exploreBtn = createHeroButton("Explorer les cours", true);
        JButton progressBtn = createHeroButton("Voir mes progrès", false);

        buttonPanel.add(exploreBtn);
        buttonPanel.add(progressBtn);

        // Add components
        content.add(title);
        content.add(Box.createVerticalStrut(10));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(25));
        content.add(buttonPanel);

        hero.add(content, BorderLayout.WEST);

        return hero;
    }

    /**
     * Create hero button (solid or outline).
     */
    private JButton createHeroButton(String text, boolean solid) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                if (solid) {
                    // Solid white button
                    g2d.setColor(Color.WHITE);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                } else {
                    // Outline button
                    if (getModel().isPressed() || getModel().isRollover()) {
                        g2d.setColor(StyleUtils.withAlpha(Color.WHITE, 25));
                        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    }
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 10, 10);
                }
                g2d.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(StyleUtils.FONT_BUTTON);
        button.setForeground(solid ? StyleUtils.PRIMARY_BLUE : Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(180, 45));

        return button;
    }

    /**
     * Create the stats grid with 4 cards.
     */
    private JPanel createStatsGrid() {
        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 20, 0));
        statsGrid.setBackground(StyleUtils.BACKGROUND_GRAY);
        statsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Stat 1: Cours suivis
        JPanel stat1 = createStatCard("📚", "Cours suivis", "12",
                StyleUtils.STAT_BLUE_BG, StyleUtils.STAT_BLUE_TEXT);
        statsCoursLabel = (JLabel) ((JPanel)stat1.getComponent(1)).getComponent(2);

        // Stat 2: Certifications
        JPanel stat2 = createStatCard("🏆", "Certifications", "3",
                StyleUtils.STAT_YELLOW_BG, StyleUtils.STAT_YELLOW_TEXT);
        statsCertLabel = (JLabel) ((JPanel)stat2.getComponent(1)).getComponent(2);

        // Stat 3: Objectifs
        JPanel stat3 = createStatCard("🎯", "Objectifs atteints", "8",
                StyleUtils.STAT_GREEN_BG, StyleUtils.STAT_GREEN_TEXT);
        statsGoalsLabel = (JLabel) ((JPanel)stat3.getComponent(1)).getComponent(2);

        // Stat 4: Jours consécutifs
        JPanel stat4 = createStatCard("🔥", "Jours consécutifs", "15",
                StyleUtils.STAT_ORANGE_BG, StyleUtils.STAT_ORANGE_TEXT);
        statsDaysLabel = (JLabel) ((JPanel)stat4.getComponent(1)).getComponent(2);

        statsGrid.add(stat1);
        statsGrid.add(stat2);
        statsGrid.add(stat3);
        statsGrid.add(stat4);

        return statsGrid;
    }

    /**
     * Create a single stat card.
     */
    private JPanel createStatCard(String icon, String label, String value,
                                  Color bgColor, Color iconColor) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(StyleUtils.CARD_WHITE);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Icon panel with rounded background
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        iconPanel.setPreferredSize(new Dimension(60, 60));
        iconPanel.setBackground(bgColor);
        iconPanel.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setForeground(iconColor);
        iconPanel.add(iconLabel);

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(StyleUtils.CARD_WHITE);

        JLabel labelText = new JLabel(label);
        labelText.setFont(StyleUtils.FONT_SMALL);
        labelText.setForeground(StyleUtils.TEXT_MUTED);

        JLabel valueText = new JLabel(value);
        valueText.setFont(StyleUtils.FONT_STAT_NUMBER);
        valueText.setForeground(StyleUtils.TEXT_DARK);

        infoPanel.add(labelText);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(valueText);

        card.add(iconPanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(StyleUtils.CARD_HOVER);
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(StyleUtils.CARD_WHITE);
            }
        });

        return card;
    }

    /**
     * Create section header.
     */
    private JPanel createSectionHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyleUtils.BACKGROUND_GRAY);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(StyleUtils.BACKGROUND_GRAY);

        JLabel title = new JLabel("Continuer l'apprentissage");
        title.setFont(StyleUtils.FONT_HEADER);
        title.setForeground(StyleUtils.TEXT_DARK);

        JLabel subtitle = new JLabel("Reprenez vos cours en cours");
        subtitle.setFont(StyleUtils.FONT_BODY);
        subtitle.setForeground(StyleUtils.TEXT_MUTED);

        leftPanel.add(title);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(subtitle);

        JLabel viewAll = new JLabel("Voir tout →");
        viewAll.setFont(StyleUtils.FONT_BUTTON);
        viewAll.setForeground(StyleUtils.PRIMARY_BLUE);
        viewAll.setCursor(new Cursor(Cursor.HAND_CURSOR));

        header.add(leftPanel, BorderLayout.WEST);
        header.add(viewAll, BorderLayout.EAST);

        return header;
    }

    /**
     * Create a modern course card.
     */
    private JPanel createCourseCard(Lesson lesson, boolean isCompleted) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(StyleUtils.CARD_WHITE);
        card.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Course image with gradient
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Different gradients based on lesson
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(67, 67, 67),
                        getWidth(), getHeight(), new Color(0, 0, 0)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        imagePanel.setPreferredSize(new Dimension(0, 180));
        imagePanel.setLayout(new BorderLayout());

        // Course tag
        JPanel tagWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        tagWrapper.setOpaque(false);

        JLabel tag = new JLabel("Java");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tag.setForeground(StyleUtils.TEXT_DARK);
        tag.setBackground(Color.WHITE);
        tag.setOpaque(true);
        tag.setBorder(new EmptyBorder(6, 15, 6, 15));

        tagWrapper.add(tag);
        imagePanel.add(tagWrapper, BorderLayout.NORTH);

        // Content panel
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(StyleUtils.CARD_WHITE);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel(lesson.getTitle());
        title.setFont(StyleUtils.FONT_CARD_TITLE);
        title.setForeground(StyleUtils.TEXT_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Description
        String desc = lesson.getTheoryText();
        if (desc.length() > 80) desc = desc.substring(0, 80) + "...";
        JLabel description = new JLabel("<html>" + desc + "</html>");
        description.setFont(StyleUtils.FONT_BODY);
        description.setForeground(StyleUtils.TEXT_MUTED);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Meta info
        JPanel metaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        metaPanel.setBackground(StyleUtils.CARD_WHITE);
        metaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel timeLabel = new JLabel("⏱️ 40h");
        timeLabel.setFont(StyleUtils.FONT_SMALL);
        timeLabel.setForeground(StyleUtils.TEXT_MUTED);

        JLabel levelLabel = new JLabel("📊 Avancé");
        levelLabel.setFont(StyleUtils.FONT_SMALL);
        levelLabel.setForeground(StyleUtils.TEXT_MUTED);

        metaPanel.add(timeLabel);
        metaPanel.add(levelLabel);

        // Progress
        int progress = isCompleted ? 100 : 0;
        JPanel progressPanel = createProgressSection(progress);
        progressPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Button
        JButton button = createModernButton(
                isCompleted ? "✓ Cours terminé" : "Continuer le cours",
                isCompleted ? StyleUtils.SUCCESS_GREEN : StyleUtils.PRIMARY_BLUE,
                Color.WHITE
        );
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.addActionListener(e -> parentApp.openLesson(lesson));

        // Add components
        content.add(title);
        content.add(Box.createVerticalStrut(10));
        content.add(description);
        content.add(Box.createVerticalStrut(15));
        content.add(metaPanel);
        content.add(Box.createVerticalStrut(15));
        content.add(progressPanel);
        content.add(Box.createVerticalStrut(15));
        content.add(button);

        card.add(imagePanel, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);

        return card;
    }

    /**
     * Create progress section.
     */
    private JPanel createProgressSection(int percentage) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(StyleUtils.CARD_WHITE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyleUtils.CARD_WHITE);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        JLabel label = new JLabel("Progression");
        label.setFont(StyleUtils.FONT_SMALL);
        label.setForeground(StyleUtils.TEXT_MUTED);

        JLabel value = new JLabel(percentage + "%");
        value.setFont(new Font("Segoe UI", Font.BOLD, 13));
        value.setForeground(StyleUtils.PRIMARY_BLUE);

        header.add(label, BorderLayout.WEST);
        header.add(value, BorderLayout.EAST);

        // Progress bar
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(percentage);
        progressBar.setStringPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 8));
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        progressBar.setBackground(StyleUtils.PROGRESS_BG);
        progressBar.setForeground(StyleUtils.PRIMARY_BLUE);
        progressBar.setBorderPainted(false);

        panel.add(header);
        panel.add(Box.createVerticalStrut(8));
        panel.add(progressBar);

        return panel;
    }

    /**
     * Create a modern button.
     */
    private JButton createModernButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(StyleUtils.darken(bgColor, 0.9f));
                } else if (getModel().isRollover()) {
                    g2d.setColor(StyleUtils.darken(bgColor, 0.95f));
                } else {
                    g2d.setColor(bgColor);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(StyleUtils.FONT_BUTTON);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);

        return button;
    }

    // ==================== PUBLIC METHODS ====================

    /**
     * Refresh the dashboard for the current user.
     */
    public void refreshForUser(User user) {
        this.currentUser = user;

        // Update XP display
        if (user instanceof Student) {
            Student student = (Student) user;
            xpLabel.setText(student.getXpScore() + " XP");
        } else {
            xpLabel.setText("Admin");
        }

        // Load courses
        loadCourses();
    }

    /**
     * Load courses from CSV.
     */
    private void loadCourses() {
        coursesPanel.removeAll();

        List<Lesson> lessons = dataManager.loadLessons();

        for (Lesson lesson : lessons) {
            boolean isCompleted = false;

            if (currentUser instanceof Student) {
                Student student = (Student) currentUser;
                isCompleted = student.hasCompletedLesson(lesson.getId());
            }

            JPanel card = createCourseCard(lesson, isCompleted);
            coursesPanel.add(card);
        }

        if (lessons.isEmpty()) {
            JLabel emptyLabel = new JLabel("Aucun cours disponible.");
            emptyLabel.setFont(StyleUtils.FONT_BODY);
            emptyLabel.setForeground(StyleUtils.TEXT_MUTED);
            coursesPanel.add(emptyLabel);
        }

        coursesPanel.revalidate();
        coursesPanel.repaint();
    }
}
