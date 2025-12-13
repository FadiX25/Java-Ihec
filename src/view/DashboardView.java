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
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/**
 * DashboardView - Modern dashboard optimized for space with rounded corners.
 */
public class DashboardView extends JPanel {

    // ==================== REFERENCES ====================

    private MainApplication parentApp;
    private CsvDataManager dataManager;
    private User currentUser;

    // UI Components
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

        add(createNavBar(), BorderLayout.NORTH);
        add(createContentArea(), BorderLayout.CENTER);
    }

    /**
     * Create navigation bar.
     */
    private JPanel createNavBar() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(Color.WHITE);
        navBar.setPreferredSize(new Dimension(0, 65));
        navBar.setBorder(new EmptyBorder(12, 30, 12, 30));

        // Left: Logo
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftPanel.setOpaque(false);

        JLabel logoIcon = new JLabel("📚");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));

        JLabel titleLabel = new JLabel("Ihec-LearnHub");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        titleLabel.setForeground(StyleUtils.TEXT_DARK);

        leftPanel.add(logoIcon);
        leftPanel.add(titleLabel);
        navBar.add(leftPanel, BorderLayout.WEST);

        // Right: XP and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        xpLabel = new JLabel("0 XP");
        xpLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        xpLabel.setForeground(StyleUtils.PRIMARY_BLUE);
        rightPanel.add(xpLabel);

        JButton logoutBtn = createModernButton("Déconnexion",
                StyleUtils.PRIMARY_BLUE, Color.WHITE);
        logoutBtn.setPreferredSize(new Dimension(110, 36));
        logoutBtn.addActionListener(e -> parentApp.logout());
        rightPanel.add(logoutBtn);

        navBar.add(rightPanel, BorderLayout.EAST);

        return navBar;
    }

    /**
     * Create main content area.
     */
    private JPanel createContentArea() {
        JPanel contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(StyleUtils.BACKGROUND_GRAY);
        contentArea.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(StyleUtils.BACKGROUND_GRAY);

        // Hero Banner
        mainContent.add(createHeroBanner());
        mainContent.add(Box.createVerticalStrut(20));

        // Stats Grid
        mainContent.add(createStatsGrid());
        mainContent.add(Box.createVerticalStrut(25));

        // Section Header
        mainContent.add(createSectionHeader());
        mainContent.add(Box.createVerticalStrut(15));

        // Courses grid
        coursesPanel = new JPanel();
        coursesPanel.setLayout(new GridLayout(0, 3, 18, 18));
        coursesPanel.setBackground(StyleUtils.BACKGROUND_GRAY);
        mainContent.add(coursesPanel);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(StyleUtils.BACKGROUND_GRAY);
        scrollPane.getViewport().setBackground(StyleUtils.BACKGROUND_GRAY);

        contentArea.add(scrollPane, BorderLayout.CENTER);

        return contentArea;
    }

    /**
     * Create hero banner with gradient.
     */
    private JPanel createHeroBanner() {
        JPanel hero = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, StyleUtils.PRIMARY_BLUE,
                        getWidth(), getHeight(), StyleUtils.PRIMARY_PURPLE
                );
                g2d.setPaint(gradient);

                RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), 20, 20
                );
                g2d.fill(roundedRectangle);
            }
        };

        hero.setLayout(new BorderLayout());
        hero.setPreferredSize(new Dimension(0, 180));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        hero.setBorder(new EmptyBorder(30, 35, 30, 35));
        hero.setOpaque(false);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JLabel title = new JLabel("Bonjour, Étudiant IHEC ! 👋");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Continuez votre apprentissage là où vous vous êtes arrêté");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(new Color(255, 255, 255, 240));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton exploreBtn = createHeroButton("Explorer les cours", true);
        JButton progressBtn = createHeroButton("Voir mes progrès", false);

        buttonPanel.add(exploreBtn);
        buttonPanel.add(progressBtn);

        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(20));
        content.add(buttonPanel);

        hero.add(content, BorderLayout.WEST);

        return hero;
    }

    /**
     * Create hero button.
     */
    private JButton createHeroButton(String text, boolean solid) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                if (solid) {
                    g2d.setColor(Color.WHITE);
                    RoundRectangle2D rect = new RoundRectangle2D.Float(
                            0, 0, getWidth(), getHeight(), 10, 10
                    );
                    g2d.fill(rect);
                } else {
                    if (getModel().isPressed() || getModel().isRollover()) {
                        g2d.setColor(new Color(255, 255, 255, 25));
                        RoundRectangle2D rect = new RoundRectangle2D.Float(
                                0, 0, getWidth(), getHeight(), 10, 10
                        );
                        g2d.fill(rect);
                    }
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(2));
                    RoundRectangle2D rect = new RoundRectangle2D.Float(
                            1, 1, getWidth() - 2, getHeight() - 2, 10, 10
                    );
                    g2d.draw(rect);
                }
                g2d.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(solid ? StyleUtils.PRIMARY_BLUE : Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(170, 42));

        return button;
    }

    /**
     * Create stats grid.
     */
    private JPanel createStatsGrid() {
        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 15, 0));
        statsGrid.setBackground(StyleUtils.BACKGROUND_GRAY);
        statsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        statsGrid.add(createStatCard("📚", "Cours suivis", "12",
                StyleUtils.STAT_BLUE_BG, StyleUtils.STAT_BLUE_TEXT));
        statsGrid.add(createStatCard("🏆", "Certifications", "3",
                StyleUtils.STAT_YELLOW_BG, StyleUtils.STAT_YELLOW_TEXT));
        statsGrid.add(createStatCard("🎯", "Objectifs atteints", "8",
                StyleUtils.STAT_GREEN_BG, StyleUtils.STAT_GREEN_TEXT));
        statsGrid.add(createStatCard("🔥", "Jours consécutifs", "15",
                StyleUtils.STAT_ORANGE_BG, StyleUtils.STAT_ORANGE_TEXT));

        return statsGrid;
    }

    /**
     * Create stat card.
     */
    private JPanel createStatCard(String icon, String label, String value,
                                  Color bgColor, Color iconColor) {
        JPanel card = new JPanel(new BorderLayout(12, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(getBackground());
                RoundRectangle2D rect = new RoundRectangle2D.Float(
                        0, 0, getWidth() - 1, getHeight() - 1, 16, 16
                );
                g2d.fill(rect);

                g2d.setColor(new Color(0, 0, 0, 5));
                g2d.draw(rect);
            }
        };
        card.setBackground(StyleUtils.CARD_WHITE);
        card.setBorder(new EmptyBorder(18, 18, 18, 18));
        card.setOpaque(false);

        // Icon panel
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                RoundRectangle2D rect = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), 12, 12
                );
                g2d.fill(rect);
            }
        };
        iconPanel.setPreferredSize(new Dimension(55, 55));
        iconPanel.setBackground(bgColor);
        iconPanel.setLayout(new GridBagLayout());
        iconPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        iconLabel.setForeground(iconColor);
        iconPanel.add(iconLabel);

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelText.setForeground(StyleUtils.TEXT_MUTED);

        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueText.setForeground(StyleUtils.TEXT_DARK);

        infoPanel.add(labelText);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(valueText);

        card.add(iconPanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(StyleUtils.CARD_HOVER);
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(StyleUtils.CARD_WHITE);
                card.repaint();
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
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(StyleUtils.BACKGROUND_GRAY);

        JLabel title = new JLabel("Continuer l'apprentissage");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(StyleUtils.TEXT_DARK);

        JLabel subtitle = new JLabel("Reprenez vos cours en cours");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(StyleUtils.TEXT_MUTED);

        leftPanel.add(title);
        leftPanel.add(Box.createVerticalStrut(3));
        leftPanel.add(subtitle);

        JLabel viewAll = new JLabel("Voir tout →");
        viewAll.setFont(new Font("Segoe UI", Font.BOLD, 14));
        viewAll.setForeground(StyleUtils.PRIMARY_BLUE);
        viewAll.setCursor(new Cursor(Cursor.HAND_CURSOR));

        header.add(leftPanel, BorderLayout.WEST);
        header.add(viewAll, BorderLayout.EAST);

        return header;
    }

    /**
     * Create course card.
     */
    private JPanel createCourseCard(Lesson lesson, boolean isCompleted) {
        // Create wrapper to handle hover state
        final boolean[] isHovered = {false};

        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(getBackground());
                RoundRectangle2D rect = new RoundRectangle2D.Float(
                        0, 0, getWidth() - 1, getHeight() - 1, 16, 16
                );
                g2d.fill(rect);

                // Border
                g2d.setColor(isHovered[0] ? StyleUtils.PRIMARY_BLUE : StyleUtils.BORDER_LIGHT);
                g2d.draw(rect);
            }
        };

        card.setBackground(StyleUtils.CARD_WHITE);
        card.setOpaque(false);

        // Image panel
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(67, 67, 67),
                        getWidth(), getHeight(), new Color(0, 0, 0)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        imagePanel.setPreferredSize(new Dimension(0, 160));
        imagePanel.setLayout(new BorderLayout());

        // Course tag
        JPanel tagWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        tagWrapper.setOpaque(false);

        JLabel tag = new JLabel("Java") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                RoundRectangle2D rect = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), 20, 20
                );
                g2d.fill(rect);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        tag.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tag.setForeground(StyleUtils.TEXT_DARK);
        tag.setBackground(Color.WHITE);
        tag.setOpaque(false);
        tag.setBorder(new EmptyBorder(5, 14, 5, 14));

        tagWrapper.add(tag);
        imagePanel.add(tagWrapper, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(StyleUtils.CARD_WHITE);
        content.setBorder(new EmptyBorder(18, 18, 18, 18));
        content.setOpaque(false);

        JLabel title = new JLabel(lesson.getTitle());
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(StyleUtils.TEXT_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        String desc = lesson.getTheoryText();
        if (desc.length() > 70) desc = desc.substring(0, 70) + "...";
        JLabel description = new JLabel("<html>" + desc + "</html>");
        description.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        description.setForeground(StyleUtils.TEXT_MUTED);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel metaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        metaPanel.setOpaque(false);
        metaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel timeLabel = new JLabel("⏱️ 40h");
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(StyleUtils.TEXT_MUTED);

        JLabel levelLabel = new JLabel("📊 Avancé");
        levelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        levelLabel.setForeground(StyleUtils.TEXT_MUTED);

        metaPanel.add(timeLabel);
        metaPanel.add(levelLabel);

        int progress = isCompleted ? 100 : 0;
        JPanel progressPanel = createProgressSection(progress);
        progressPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton button = createModernButton(
                isCompleted ? "✓ Cours terminé" : "Continuer le cours",
                isCompleted ? StyleUtils.SUCCESS_GREEN : StyleUtils.PRIMARY_BLUE,
                Color.WHITE
        );
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.addActionListener(e -> parentApp.openLesson(lesson));

        content.add(title);
        content.add(Box.createVerticalStrut(8));
        content.add(description);
        content.add(Box.createVerticalStrut(12));
        content.add(metaPanel);
        content.add(Box.createVerticalStrut(12));
        content.add(progressPanel);
        content.add(Box.createVerticalStrut(12));
        content.add(button);

        card.add(imagePanel, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);

        // Hover
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered[0] = true;
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered[0] = false;
                card.repaint();
            }
        });

        return card;
    }

    /**
     * Create progress section.
     */
    private JPanel createProgressSection(int percentage) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));

        JLabel label = new JLabel("Progression");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(StyleUtils.TEXT_MUTED);

        JLabel value = new JLabel(percentage + "%");
        value.setFont(new Font("Segoe UI", Font.BOLD, 12));
        value.setForeground(StyleUtils.PRIMARY_BLUE);

        header.add(label, BorderLayout.WEST);
        header.add(value, BorderLayout.EAST);

        JProgressBar progressBar = new JProgressBar(0, 100) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(StyleUtils.PROGRESS_BG);
                RoundRectangle2D bg = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), 10, 10
                );
                g2d.fill(bg);

                if (getValue() > 0) {
                    g2d.setColor(StyleUtils.PRIMARY_BLUE);
                    int width = (int)(getWidth() * (getValue() / 100.0));
                    RoundRectangle2D fill = new RoundRectangle2D.Float(
                            0, 0, width, getHeight(), 10, 10
                    );
                    g2d.fill(fill);
                }
            }
        };
        progressBar.setValue(percentage);
        progressBar.setStringPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 8));
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        progressBar.setBorderPainted(false);
        progressBar.setOpaque(false);

        panel.add(header);
        panel.add(Box.createVerticalStrut(6));
        panel.add(progressBar);

        return panel;
    }

    /**
     * Create modern button.
     */
    private JButton createModernButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                Color color = bgColor;
                if (getModel().isPressed()) {
                    color = StyleUtils.darken(bgColor, 0.9f);
                } else if (getModel().isRollover()) {
                    color = StyleUtils.darken(bgColor, 0.95f);
                }

                g2d.setColor(color);
                RoundRectangle2D rect = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), 10, 10
                );
                g2d.fill(rect);
                g2d.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);

        return button;
    }

    // ==================== PUBLIC METHODS ====================

    public void refreshForUser(User user) {
        this.currentUser = user;

        if (user instanceof Student) {
            Student student = (Student) user;
            xpLabel.setText(student.getXpScore() + " XP");
        } else {
            xpLabel.setText("Admin");
        }

        loadCourses();
    }

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
