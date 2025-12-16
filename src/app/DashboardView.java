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
 */
public class DashboardView extends JPanel {

    // ==================== REFERENCES ====================

    private MainApplication parentApp;
    private CsvDataManager dataManager;
    private User currentUser;

    // UI Components that need updating
    private JLabel xpLabel;
    private JLabel welcomeLabel;
    private JPanel mainContentPanel;
    private JPanel statsPanel;
    private JPanel coursesGridPanel; // Le panel pour les cours

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

        // Main content area with sidebar
        add(createMainContentArea(), BorderLayout.CENTER);
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

        // Center: Welcome message
        welcomeLabel = new JLabel("Bonjour, Étudiant IHEC !");
        welcomeLabel.setFont(StyleUtils.FONT_SUBHEADER);
        welcomeLabel.setForeground(StyleUtils.TEXT_LIGHT);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        navBar.add(welcomeLabel, BorderLayout.CENTER);

        // Right side: User info and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);  // Transparent background

        // XP display avec badge moderne
        JPanel xpBadge = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        xpBadge.setBackground(new Color(255, 215, 0, 150)); // Or transparent
        xpBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 215, 0), 1),
                new EmptyBorder(3, 10, 3, 10)
        ));

        JLabel xpIcon = new JLabel("⭐");
        xpIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        xpBadge.add(xpIcon);

        xpLabel = new JLabel("0 XP");
        xpLabel.setFont(StyleUtils.FONT_BODY);
        xpLabel.setForeground(new Color(139, 69, 19)); // Marron foncé pour contraste
        xpBadge.add(xpLabel);

        rightPanel.add(xpBadge);

        // Logout button avec meilleur contraste
        JButton logoutBtn = createModernButton("Déconnexion",
                new Color(255, 107, 107), // Rouge clair
                new Color(220, 80, 80),   // Rouge foncé (hover)
                Color.WHITE);
        logoutBtn.addActionListener(e -> parentApp.logout());
        rightPanel.add(logoutBtn);

        navBar.add(rightPanel, BorderLayout.EAST);

        return navBar;
    }

    /**
     * Create the main content area with sidebar and main content.
     */
    private JPanel createMainContentArea() {
        JPanel contentArea = new JPanel(new BorderLayout(20, 0));
        contentArea.setBackground(StyleUtils.BACKGROUND_GRAY);
        contentArea.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Sidebar with statistics
        contentArea.add(createStatsSidebar(), BorderLayout.WEST);

        // Main content panel
        mainContentPanel = createMainContent();
        contentArea.add(mainContentPanel, BorderLayout.CENTER);

        return contentArea;
    }

    /**
     * Create the sidebar with statistics (Continuer l'apprentissage).
     */
    private JPanel createStatsSidebar() {
        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(StyleUtils.CARD_WHITE);
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleUtils.BORDER_LIGHT, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        statsPanel.setPreferredSize(new Dimension(300, 0));

        // Title avec icône
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(StyleUtils.CARD_WHITE);

        JLabel iconLabel = new JLabel("📊");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        titlePanel.add(iconLabel);

        JLabel title = new JLabel("Continuer l'apprentissage");
        title.setFont(StyleUtils.FONT_SUBHEADER);
        title.setForeground(StyleUtils.TEXT_DARK);
        titlePanel.add(title);

        statsPanel.add(titlePanel);
        statsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Divider
        JSeparator separator = new JSeparator();
        separator.setForeground(StyleUtils.BORDER_LIGHT);
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.add(separator);

        statsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Statistics cards avec couleurs variées
        String[] stats = {"Cours suivis", "Certifications", "Objectifs atteints", "Jours consécutifs"};
        String[] values = {"12", "3", "8", "15"};
        Color[] colors = {
                new Color(70, 130, 180),    // Bleu acier
                new Color(60, 179, 113),    // Vert moyen
                new Color(255, 140, 0),     // Orange foncé
                new Color(186, 85, 211)     // Violet moyen
        };

        for (int i = 0; i < stats.length; i++) {
            statsPanel.add(createStatCard(stats[i], values[i], colors[i]));
            if (i < stats.length - 1) {
                statsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        statsPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // See all button en BLEU
        JButton seeAllBtn = createModernButton("📋 Voir tout les statistiques",
                new Color(0, 123, 255), // BLEU vif
                new Color(0, 86, 179),  // BLEU foncé (hover)
                Color.WHITE);
        seeAllBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        seeAllBtn.setMaximumSize(new Dimension(280, 40));
        statsPanel.add(seeAllBtn);

        return statsPanel;
    }

    /**
     * Create a single statistic card avec couleur.
     */
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(StyleUtils.BACKGROUND_GRAY);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        card.setMaximumSize(new Dimension(280, 80));

        // Value (large number) avec couleur spécifique
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(StyleUtils.FONT_STAT_NUMBER);
        valueLabel.setForeground(color);
        card.add(valueLabel, BorderLayout.WEST);

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(StyleUtils.FONT_BODY);
        titleLabel.setForeground(StyleUtils.TEXT_DARK);
        titleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        card.add(titleLabel, BorderLayout.CENTER);

        return card;
    }

    /**
     * Create the main content panel.
     */
    private JPanel createMainContent() {
        // Créer un JTabbedPane pour les différentes sections
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(StyleUtils.FONT_SUBHEADER);
        tabbedPane.setBackground(StyleUtils.BACKGROUND_GRAY);
        tabbedPane.setForeground(StyleUtils.TEXT_DARK);

        // Onglet "Parcours de compétences"
        tabbedPane.addTab("🏆 Parcours", createSkillPathsSection());

        // Onglet "Tous les cours"
        tabbedPane.addTab("📚 Tous les cours", createAllCoursesSection());

        // Onglet "Recommandations"
        tabbedPane.addTab("⭐ Recommandations", createRecommendationsSection());

        // Envelopper dans un panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(StyleUtils.BACKGROUND_GRAY);
        contentPanel.add(tabbedPane, BorderLayout.CENTER);

        return contentPanel;
    }

    /**
     * Create the "Parcours de compétences" section.
     */
    private JPanel createSkillPathsSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(StyleUtils.BACKGROUND_GRAY);
        section.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyleUtils.BACKGROUND_GRAY);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("🎯 Parcours de compétences");
        title.setFont(StyleUtils.FONT_SUBHEADER);
        title.setForeground(new Color(30, 144, 255)); // Bleu dodger
        header.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Suivez un parcours structuré pour maîtriser une compétence");
        subtitle.setFont(StyleUtils.FONT_SMALL);
        subtitle.setForeground(StyleUtils.TEXT_MUTED);
        header.add(subtitle, BorderLayout.EAST);

        section.add(header, BorderLayout.NORTH);

        // Paths grid
        JPanel pathsGrid = new JPanel(new GridLayout(0, 2, 20, 20));
        pathsGrid.setBackground(StyleUtils.BACKGROUND_GRAY);

        // Sample paths (would normally come from database)
        String[] pathTitles = {"Parcours Data Analyst", "Parcours Machine Learning Engineer", "Parcours Développeur Java"};
        String[] descriptions = {
                "Devenez expert en analyse de données et visualisation",
                "Construisez et déployez des modèles de ML en production",
                "Maîtrisez Java de A à Z, du débutant à l'expert"
        };
        String[] progress = {"4/6", "1/7", "0/8"};
        Color[] pathColors = {
                new Color(70, 130, 180),  // Bleu acier
                new Color(60, 179, 113),  // Vert moyen
                new Color(255, 140, 0)    // Orange foncé
        };

        for (int i = 0; i < pathTitles.length; i++) {
            pathsGrid.add(createSkillPathCard(pathTitles[i], descriptions[i], progress[i], pathColors[i]));
        }

        section.add(pathsGrid, BorderLayout.CENTER);

        return section;
    }

    /**
     * Create a single skill path card avec couleur.
     */
    private JPanel createSkillPathCard(String title, String description, String progress, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.brighter(), 2),
                new EmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Progress indicator avec badge coloré
        JPanel progressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        progressPanel.setBackground(Color.WHITE);

        JLabel progressLabel = new JLabel(progress);
        progressLabel.setFont(StyleUtils.FONT_SUBHEADER);
        progressLabel.setForeground(color);

        JLabel progressText = new JLabel(" complété");
        progressText.setFont(StyleUtils.FONT_SMALL);
        progressText.setForeground(StyleUtils.TEXT_MUTED);

        progressPanel.add(progressLabel);
        progressPanel.add(progressText);
        card.add(progressPanel, BorderLayout.NORTH);

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(StyleUtils.FONT_BODY);
        titleLabel.setForeground(StyleUtils.TEXT_DARK);
        titleLabel.setBorder(new EmptyBorder(10, 0, 5, 0));
        card.add(titleLabel, BorderLayout.CENTER);

        // Description
        JLabel descLabel = new JLabel("<html><p style='width:250px'>" + description + "</p></html>");
        descLabel.setFont(StyleUtils.FONT_SMALL);
        descLabel.setForeground(StyleUtils.TEXT_MUTED);
        descLabel.setBorder(new EmptyBorder(5, 0, 10, 0));
        card.add(descLabel, BorderLayout.SOUTH);

        // Ajouter un effet de survol
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(240, 248, 255)); // Alice blue
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    /**
     * Create the "Tous les cours" section.
     */
    private JPanel createAllCoursesSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(StyleUtils.BACKGROUND_GRAY);
        section.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header avec filter/sort
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyleUtils.BACKGROUND_GRAY);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(StyleUtils.BACKGROUND_GRAY);

        JLabel titleIcon = new JLabel("📖");
        titleIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        titlePanel.add(titleIcon);

        JLabel title = new JLabel("Tous les cours");
        title.setFont(StyleUtils.FONT_SUBHEADER);
        title.setForeground(new Color(30, 144, 255)); // Bleu dodger
        titlePanel.add(title);

        header.add(titlePanel, BorderLayout.WEST);

        // Filter and sort buttons avec couleurs vives
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setBackground(StyleUtils.BACKGROUND_GRAY);

        JButton filterBtn = createModernButton("🔍 Filtrer",
                new Color(0, 123, 255), // BLEU vif
                new Color(0, 86, 179),  // BLEU foncé (hover)
                Color.WHITE);
        filterBtn.setPreferredSize(new Dimension(100, 35));

        JButton sortBtn = createModernButton("↕️ Trier",
                new Color(0, 123, 255), // BLEU vif
                new Color(0, 86, 179),  // BLEU foncé (hover)
                Color.WHITE);
        sortBtn.setPreferredSize(new Dimension(100, 35));

        filterPanel.add(filterBtn);
        filterPanel.add(sortBtn);
        header.add(filterPanel, BorderLayout.EAST);

        section.add(header, BorderLayout.NORTH);

        // Courses grid
        coursesGridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        coursesGridPanel.setBackground(StyleUtils.BACKGROUND_GRAY);

        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(coursesGridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(StyleUtils.BACKGROUND_GRAY);

        section.add(scrollPane, BorderLayout.CENTER);

        return section;
    }

    /**
     * Create the "Recommandé pour vous" section.
     */
    private JPanel createRecommendationsSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(StyleUtils.BACKGROUND_GRAY);
        section.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyleUtils.BACKGROUND_GRAY);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(StyleUtils.BACKGROUND_GRAY);

        JLabel titleIcon = new JLabel("💡");
        titleIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        titlePanel.add(titleIcon);

        JLabel title = new JLabel("Recommandé pour vous");
        title.setFont(StyleUtils.FONT_SUBHEADER);
        title.setForeground(new Color(255, 105, 180)); // Rose vif
        titlePanel.add(title);

        header.add(titlePanel, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Basé sur vos cours et objectifs");
        subtitle.setFont(StyleUtils.FONT_SMALL);
        subtitle.setForeground(StyleUtils.TEXT_MUTED);
        header.add(subtitle, BorderLayout.EAST);

        section.add(header, BorderLayout.NORTH);

        // Recommendations grid
        JPanel recGrid = new JPanel(new GridLayout(0, 2, 20, 20));
        recGrid.setBackground(StyleUtils.BACKGROUND_GRAY);

        // Sample recommendations
        String[] recTitles = {"Architecture Logicielle avec Java", "Big Data avec Hadoop et Spark"};
        String[] recDescriptions = {
                "Approfondissez vos connaissances en Java avec les patterns d'architecture",
                "Complétez vos compétences en Data Science avec le Big Data"
        };
        Color[] recColors = {
                new Color(135, 206, 250), // Bleu ciel clair
                new Color(152, 251, 152)  // Vert pâle
        };

        for (int i = 0; i < recTitles.length; i++) {
            recGrid.add(createRecommendationCard(recTitles[i], recDescriptions[i], recColors[i]));
        }

        section.add(recGrid, BorderLayout.CENTER);

        return section;
    }

    /**
     * Create a single recommendation card.
     */
    private JPanel createRecommendationCard(String title, String description, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(StyleUtils.FONT_BODY);
        titleLabel.setForeground(new Color(25, 25, 112)); // Bleu nuit
        card.add(titleLabel, BorderLayout.NORTH);

        // Description
        JLabel descLabel = new JLabel("<html><p style='width:300px'>" + description + "</p></html>");
        descLabel.setFont(StyleUtils.FONT_SMALL);
        descLabel.setForeground(new Color(47, 79, 79)); // Gris ardoise
        descLabel.setBorder(new EmptyBorder(10, 0, 15, 0));
        card.add(descLabel, BorderLayout.CENTER);

        // Learn more button avec contraste
        JButton learnMoreBtn = createModernButton("✨ En savoir plus →",
                new Color(0, 123, 255),   // BLEU vif
                new Color(0, 86, 179),    // BLEU foncé (hover)
                Color.WHITE);
        learnMoreBtn.setHorizontalAlignment(SwingConstants.LEFT);
        card.add(learnMoreBtn, BorderLayout.SOUTH);

        return card;
    }

    /**
     * Create a modern button avec couleurs personnalisées.
     */
    private JButton createModernButton(String text, Color bgColor, Color hoverColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(StyleUtils.FONT_BUTTON);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Effet de survol
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setBorder(BorderFactory.createLineBorder(hoverColor.darker(), 1));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            }
        });

        return button;
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
        welcomeLabel.setText("Bonjour, " + user.getUsername() + " !");

        // Update XP display (only for students)
        if (user instanceof Student) {
            Student student = (Student) user;
            xpLabel.setText(student.getXpScore() + " XP");
        } else {
            xpLabel.setText("Admin");
        }

        // Load and display courses in the "Tous les cours" section
        loadCourses();
    }

    /**
     * Load courses from CSV and create cards in the main content area.
     */
    private void loadCourses() {
        // Clear existing cards
        if (coursesGridPanel != null) {
            coursesGridPanel.removeAll();
        }

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
            if (coursesGridPanel != null) {
                coursesGridPanel.add(card);
            }
        }

        // If no lessons, show a message
        if (lessons.isEmpty()) {
            JLabel emptyLabel = new JLabel("Aucun cours disponible pour le moment.");
            emptyLabel.setFont(StyleUtils.FONT_BODY);
            emptyLabel.setForeground(StyleUtils.TEXT_MUTED);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            if (coursesGridPanel != null) {
                coursesGridPanel.add(emptyLabel);
            }
        }

        // Refresh the panel
        if (coursesGridPanel != null) {
            coursesGridPanel.revalidate();
            coursesGridPanel.repaint();
        }
    }

    /**
     * Create a single course card for the "Tous les cours" grid.
     */
    private JPanel createCourseCard(Lesson lesson, boolean isCompleted) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Course title avec icône
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);

        JLabel iconLabel = new JLabel("📘");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        titlePanel.add(iconLabel, BorderLayout.WEST);

        JLabel titleLabel = new JLabel(lesson.getTitle());
        titleLabel.setFont(StyleUtils.FONT_BODY);
        titleLabel.setForeground(new Color(30, 30, 30));
        titleLabel.setBorder(new EmptyBorder(0, 5, 0, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        card.add(titlePanel, BorderLayout.NORTH);

        // Course description (truncated)
        String theoryPreview = lesson.getTheoryText();
        if (theoryPreview.length() > 80) {
            theoryPreview = theoryPreview.substring(0, 80) + "...";
        }
        JLabel descLabel = new JLabel("<html><p style='width:200px'>" + theoryPreview + "</p></html>");
        descLabel.setFont(StyleUtils.FONT_SMALL);
        descLabel.setForeground(new Color(100, 100, 100));
        descLabel.setBorder(new EmptyBorder(10, 0, 15, 0));
        card.add(descLabel, BorderLayout.CENTER);

        // Bottom: Progress and action button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);

        // Progress or status
        if (isCompleted) {
            JPanel completedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            completedPanel.setBackground(Color.WHITE);

            JLabel checkIcon = new JLabel("✅");
            checkIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
            completedPanel.add(checkIcon);

            JLabel completedLabel = new JLabel("Terminé");
            completedLabel.setFont(StyleUtils.FONT_SMALL);
            completedLabel.setForeground(new Color(34, 139, 34)); // Vert forest
            completedPanel.add(completedLabel);

            bottomPanel.add(completedPanel, BorderLayout.WEST);
        } else {
            // Bouton "Continuer" en VERT
            JButton continueBtn = createModernButton("▶ Continuer",
                    new Color(40, 167, 69), // VERT vif
                    new Color(33, 136, 56), // VERT foncé (hover)
                    Color.WHITE);
            continueBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            continueBtn.addActionListener(e -> parentApp.openLesson(lesson));
            bottomPanel.add(continueBtn, BorderLayout.WEST);
        }

        // Duration avec icône
        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        durationPanel.setBackground(Color.WHITE);

        JLabel clockIcon = new JLabel("⏱️");
        clockIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        durationPanel.add(clockIcon);

        String durationText;
        try {
            // Essayer d'obtenir la durée
            durationText = lesson.getDuration() + "h";
        } catch (Exception e) {
            // Si la méthode n'existe pas encore, utiliser une valeur par défaut
            durationText = "25h";
        }
        JLabel durationLabel = new JLabel(durationText);
        durationLabel.setFont(StyleUtils.FONT_SMALL);
        durationLabel.setForeground(new Color(128, 128, 128));
        durationPanel.add(durationLabel);

        bottomPanel.add(durationPanel, BorderLayout.EAST);

        card.add(bottomPanel, BorderLayout.SOUTH);

        // Click handler - open the lesson
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parentApp.openLesson(lesson);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(240, 248, 255)); // Alice blue
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(30, 144, 255), 2),
                        new EmptyBorder(15, 15, 15, 15)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        new EmptyBorder(15, 15, 15, 15)
                ));
            }
        });

        return card;
    }
}