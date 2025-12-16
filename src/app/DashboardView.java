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
    private JPanel coursesGridPanel;

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
        rightPanel.setOpaque(false);

        // XP display avec badge amélioré
        JPanel xpBadge = createXpBadge();
        rightPanel.add(xpBadge);

        // Logout button
        JButton logoutBtn = createModernButton("Déconnexion",
                new Color(255, 107, 107),
                new Color(220, 80, 80),
                Color.WHITE);
        logoutBtn.addActionListener(e -> parentApp.logout());
        rightPanel.add(logoutBtn);

        navBar.add(rightPanel, BorderLayout.EAST);

        return navBar;
    }

    /**
     * Create XP badge with icon.
     */
    private JPanel createXpBadge() {
        JPanel xpBadge = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        xpBadge.setBackground(new Color(255, 215, 0, 30)); // Or transparent avec transparence
        xpBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 215, 0, 100), 2),
                new EmptyBorder(5, 15, 5, 15)
        ));

        // XP icon avec emoji
        JLabel xpIcon = new JLabel("⚡");
        xpIcon.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        xpBadge.add(xpIcon);

        // XP value
        xpLabel = new JLabel("0 XP");
        xpLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        xpLabel.setForeground(new Color(218, 165, 32)); // Goldenrod
        xpBadge.add(xpLabel);

        return xpBadge;
    }

    /**
     * Create the main content area with sidebar and main content.
     */
    private JPanel createMainContentArea() {
        JPanel contentArea = new JPanel(new BorderLayout(20, 0));
        contentArea.setBackground(StyleUtils.BACKGROUND_GRAY);
        contentArea.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Sidebar with statistics (AMÉLIORÉE)
        contentArea.add(createEnhancedStatsSidebar(), BorderLayout.WEST);

        // Main content panel
        mainContentPanel = createMainContent();
        contentArea.add(mainContentPanel, BorderLayout.CENTER);

        return contentArea;
    }

    /**
     * Create enhanced sidebar with statistics.
     */
    private JPanel createEnhancedStatsSidebar() {
        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(new Color(255, 255, 255)); // Blanc pur
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(25, 20, 25, 20)
        ));
        statsPanel.setPreferredSize(new Dimension(320, 0));

        // Header avec icône et titre amélioré
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Icon and title
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        titleContainer.setBackground(Color.WHITE);

        JLabel iconLabel = new JLabel("📈");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        titleContainer.add(iconLabel);

        JLabel title = new JLabel("Vos Statistiques");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(30, 30, 30));
        titleContainer.add(title);

        headerPanel.add(titleContainer, BorderLayout.WEST);

        // Subtitle
        JLabel subtitle = new JLabel("Suivez votre progression");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(120, 120, 120));
        subtitle.setHorizontalAlignment(SwingConstants.RIGHT);
        headerPanel.add(subtitle, BorderLayout.EAST);

        statsPanel.add(headerPanel);
        statsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Divider élégant
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(230, 230, 230));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.add(separator);
        statsPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Statistics cards améliorées avec icônes
        String[] stats = {"Cours suivis", "Certifications", "Objectifs atteints", "Jours consécutifs", "XP Total", "Niveau actuel"};
        String[] values = {"12", "3", "8", "15", "450", "Débutant"};
        String[] icons = {"📚", "🏆", "✅", "🔥", "⭐", "📊"};
        Color[] colors = {
                new Color(70, 130, 180),    // Bleu acier
                new Color(60, 179, 113),    // Vert moyen
                new Color(255, 140, 0),     // Orange foncé
                new Color(186, 85, 211),    // Violet moyen
                new Color(255, 215, 0),     // Or
                new Color(30, 144, 255)     // Bleu dodger
        };

        for (int i = 0; i < stats.length; i++) {
            statsPanel.add(createEnhancedStatCard(stats[i], values[i], icons[i], colors[i]));
            if (i < stats.length - 1) {
                statsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        statsPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Progress section
        JPanel progressSection = createProgressSection();
        statsPanel.add(progressSection);

        statsPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Quick actions
        JPanel quickActions = createQuickActions();
        statsPanel.add(quickActions);

        return statsPanel;
    }

    /**
     * Create enhanced statistic card with icon.
     */
    private JPanel createEnhancedStatCard(String title, String value, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        card.setMaximumSize(new Dimension(280, 85));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Left: Icon with colored background
        JPanel iconPanel = new JPanel(new GridBagLayout());
        iconPanel.setPreferredSize(new Dimension(50, 50));
        iconPanel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
        iconPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100), 1),
                new EmptyBorder(5, 5, 5, 5)
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        iconLabel.setForeground(color);
        iconPanel.add(iconLabel);

        card.add(iconPanel, BorderLayout.WEST);

        // Center: Text content
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        // Value (large number)
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valueLabel.setForeground(new Color(40, 40, 40));
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(valueLabel);

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        textPanel.add(titleLabel);

        card.add(textPanel, BorderLayout.CENTER);

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(245, 247, 250));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    /**
     * Create progress section with bar.
     */
    private JPanel createProgressSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(new EmptyBorder(0, 10, 0, 10));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Title
        JLabel progressTitle = new JLabel("📊 Progression du mois");
        progressTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        progressTitle.setForeground(new Color(30, 30, 30));
        progressTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(progressTitle);

        section.add(Box.createRigidArea(new Dimension(0, 10)));

        // Progress bar container
        JPanel barContainer = new JPanel(new BorderLayout());
        barContainer.setBackground(Color.WHITE);
        barContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Progress bar
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(75); // 75% de progression
        progressBar.setForeground(new Color(0, 123, 255)); // Bleu
        progressBar.setBackground(new Color(230, 230, 230));
        progressBar.setBorderPainted(false);
        progressBar.setStringPainted(true);
        progressBar.setString("75%");
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        progressBar.setPreferredSize(new Dimension(280, 25));
        barContainer.add(progressBar, BorderLayout.CENTER);

        section.add(barContainer);

        // Progress info
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel currentLabel = new JLabel("15/20 cours");
        currentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        currentLabel.setForeground(new Color(100, 100, 100));

        JLabel targetLabel = new JLabel("Objectif: 20 cours");
        targetLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        targetLabel.setForeground(new Color(0, 123, 255));
        targetLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        infoPanel.add(currentLabel, BorderLayout.WEST);
        infoPanel.add(targetLabel, BorderLayout.EAST);

        section.add(infoPanel);

        return section;
    }

    /**
     * Create quick actions panel.
     */
    private JPanel createQuickActions() {
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setBackground(Color.WHITE);
        actionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Title
        JLabel actionsTitle = new JLabel("⚡ Actions rapides");
        actionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        actionsTitle.setForeground(new Color(30, 30, 30));
        actionsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionsPanel.add(actionsTitle);

        actionsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Action buttons
        String[] actions = {"📝 Reprendre le dernier cours", "🎯 Définir un objectif", "📥 Télécharger certificat", "👥 Inviter un ami"};
        Color[] actionColors = {
                new Color(0, 123, 255),  // Bleu
                new Color(40, 167, 69),  // Vert
                new Color(255, 193, 7),  // Jaune
                new Color(111, 66, 193)  // Violet
        };

        for (int i = 0; i < actions.length; i++) {
            JButton actionBtn = createActionButton(actions[i], actionColors[i]);
            actionBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            actionBtn.setMaximumSize(new Dimension(280, 40));
            actionsPanel.add(actionBtn);
            if (i < actions.length - 1) {
                actionsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        return actionsPanel;
    }

    /**
     * Create action button.
     */
    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 10));
        button.setForeground(color);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50), 1),
                new EmptyBorder(10, 15, 10, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 10));
            }
        });

        return button;
    }

    /**
     * Create the main content panel.
     */
    private JPanel createMainContent() {
        // Créer un JTabbedPane pour les différentes sections
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(StyleUtils.BACKGROUND_GRAY);
        tabbedPane.setForeground(new Color(30, 30, 30));

        // Style des onglets
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // Onglet "Parcours de compétences"
        tabbedPane.addTab("🏆 Parcours", createSkillPathsSection());

        // Onglet "Tous les cours"
        tabbedPane.addTab("📚 Tous les cours", createAllCoursesSection());

        // Onglet "Recommandations"
        tabbedPane.addTab("⭐ Recommandations", createRecommendationsSection());

        // Onglet "Historique"
        tabbedPane.addTab("📅 Historique", createHistorySection());

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
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(30, 144, 255));
        header.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Suivez un parcours structuré pour maîtriser une compétence");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 100, 100));
        header.add(subtitle, BorderLayout.EAST);

        section.add(header, BorderLayout.NORTH);

        // Paths grid
        JPanel pathsGrid = new JPanel(new GridLayout(0, 2, 20, 20));
        pathsGrid.setBackground(StyleUtils.BACKGROUND_GRAY);

        // Sample paths
        String[] pathTitles = {"Parcours Data Analyst", "Parcours Machine Learning Engineer", "Parcours Développeur Java", "Parcours DevOps"};
        String[] descriptions = {
                "Devenez expert en analyse de données et visualisation",
                "Construisez et déployez des modèles de ML en production",
                "Maîtrisez Java de A à Z, du débutant à l'expert",
                "Automatisez le déploiement et la gestion des infrastructures"
        };
        String[] progress = {"4/6", "1/7", "0/8", "3/5"};
        Color[] pathColors = {
                new Color(70, 130, 180),  // Bleu acier
                new Color(60, 179, 113),  // Vert moyen
                new Color(255, 140, 0),   // Orange foncé
                new Color(111, 66, 193)   // Violet
        };

        for (int i = 0; i < pathTitles.length; i++) {
            pathsGrid.add(createSkillPathCard(pathTitles[i], descriptions[i], progress[i], pathColors[i]));
        }

        section.add(pathsGrid, BorderLayout.CENTER);

        return section;
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
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(30, 144, 255));
        titlePanel.add(title);

        header.add(titlePanel, BorderLayout.WEST);

        // Filter and sort buttons
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setBackground(StyleUtils.BACKGROUND_GRAY);

        JButton filterBtn = createModernButton("🔍 Filtrer",
                new Color(0, 123, 255),
                new Color(0, 86, 179),
                Color.WHITE);
        filterBtn.setPreferredSize(new Dimension(100, 35));

        JButton sortBtn = createModernButton("↕️ Trier",
                new Color(0, 123, 255),
                new Color(0, 86, 179),
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
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(255, 105, 180));
        titlePanel.add(title);

        header.add(titlePanel, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Basé sur vos cours et objectifs");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 100, 100));
        header.add(subtitle, BorderLayout.EAST);

        section.add(header, BorderLayout.NORTH);

        // Recommendations grid
        JPanel recGrid = new JPanel(new GridLayout(0, 2, 20, 20));
        recGrid.setBackground(StyleUtils.BACKGROUND_GRAY);

        // Sample recommendations
        String[] recTitles = {"Architecture Logicielle avec Java", "Big Data avec Hadoop et Spark", "Microservices avec Spring Boot", "Cloud Computing avec AWS"};
        String[] recDescriptions = {
                "Approfondissez vos connaissances en Java avec les patterns d'architecture",
                "Complétez vos compétences en Data Science avec le Big Data",
                "Apprenez à construire des applications microservices modernes",
                "Maîtrisez les services cloud d'Amazon Web Services"
        };
        Color[] recColors = {
                new Color(135, 206, 250), // Bleu ciel clair
                new Color(152, 251, 152),  // Vert pâle
                new Color(255, 228, 196),  // Bisque
                new Color(221, 160, 221)   // Plum
        };

        for (int i = 0; i < recTitles.length; i++) {
            recGrid.add(createRecommendationCard(recTitles[i], recDescriptions[i], recColors[i]));
        }

        section.add(recGrid, BorderLayout.CENTER);

        return section;
    }

    /**
     * Create the "Historique" section.
     */
    private JPanel createHistorySection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(StyleUtils.BACKGROUND_GRAY);
        section.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyleUtils.BACKGROUND_GRAY);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(StyleUtils.BACKGROUND_GRAY);

        JLabel titleIcon = new JLabel("📅");
        titleIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        titlePanel.add(titleIcon);

        JLabel title = new JLabel("Votre historique d'apprentissage");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(139, 0, 139)); // Magenta foncé
        titlePanel.add(title);

        header.add(titlePanel, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Retracez votre parcours d'apprentissage");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 100, 100));
        header.add(subtitle, BorderLayout.EAST);

        section.add(header, BorderLayout.NORTH);

        // History content
        JTextArea historyArea = new JTextArea();
        historyArea.setText("📊 Historique de vos activités:\n\n" +
                "• Aujourd'hui: Introduction à Java (30 min)\n" +
                "• Hier: Variables et types de données (45 min)\n" +
                "• Il y a 2 jours: Programmation Orientée Objet (60 min)\n" +
                "• Il y a 3 jours: Structures de contrôle (40 min)\n" +
                "• Il y a 5 jours: Tableaux et collections (55 min)\n\n" +
                "⏱️ Temps total d'apprentissage ce mois: 12h 30min\n" +
                "📈 Progression moyenne: 85%\n" +
                "🏆 Cours complétés: 8/12");
        historyArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        historyArea.setBackground(Color.WHITE);
        historyArea.setForeground(new Color(60, 60, 60));
        historyArea.setEditable(false);
        historyArea.setBorder(new EmptyBorder(20, 20, 20, 20));
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        section.add(scrollPane, BorderLayout.CENTER);

        return section;
    }

    // ==================== RESTE DU CODE INCHANGÉ ====================
    // (Les méthodes suivantes restent identiques à la version précédente)

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

    /**
     * Refresh the dashboard for the current user.
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
            completedLabel.setForeground(new Color(34, 139, 34));
            completedPanel.add(completedLabel);

            bottomPanel.add(completedPanel, BorderLayout.WEST);
        } else {
            // Bouton "Continuer" en VERT
            JButton continueBtn = createModernButton("▶ Continuer",
                    new Color(40, 167, 69),
                    new Color(33, 136, 56),
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
            durationText = lesson.getDuration() + "h";
        } catch (Exception e) {
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
                card.setBackground(new Color(240, 248, 255));
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
                card.setBackground(new Color(240, 248, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
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
        titleLabel.setForeground(new Color(25, 25, 112));
        card.add(titleLabel, BorderLayout.NORTH);

        // Description
        JLabel descLabel = new JLabel("<html><p style='width:300px'>" + description + "</p></html>");
        descLabel.setFont(StyleUtils.FONT_SMALL);
        descLabel.setForeground(new Color(47, 79, 79));
        descLabel.setBorder(new EmptyBorder(10, 0, 15, 0));
        card.add(descLabel, BorderLayout.CENTER);

        // Learn more button avec contraste
        JButton learnMoreBtn = createModernButton("✨ En savoir plus →",
                new Color(0, 123, 255),
                new Color(0, 86, 179),
                Color.WHITE);
        learnMoreBtn.setHorizontalAlignment(SwingConstants.LEFT);
        card.add(learnMoreBtn, BorderLayout.SOUTH);

        return card;
    }
}