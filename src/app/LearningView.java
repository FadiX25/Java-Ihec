package app;

import services.CsvDataManager;
import model.Lesson;
import model.Student;
import model.User;
import utils.StyleUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

/**
 * LearningView - The core learning interface with split-pane layout.
 */
public class LearningView extends JPanel {

    // ==================== REFERENCES ====================

    private MainApplication parentApp;
    private CsvDataManager dataManager;
    private Lesson currentLesson;

    // ==================== UI COMPONENTS ====================

    // Header
    private JLabel lessonTitleLabel;

    // Left panel components
    private JTextArea theoryTextArea;
    private JButton watchVideoButton;

    // Right panel components
    private JTextArea codeEditorArea;
    private JButton submitButton;
    private JLabel feedbackLabel;

    // ==================== CONSTANTS ====================

    /** XP awarded for completing a lesson correctly */
    private static final int XP_REWARD = 25;

    // ==================== CONSTRUCTOR ====================

    public LearningView(MainApplication parentApp) {
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

        // Main content: Split pane with left (theory) and right (code)
        JSplitPane splitPane = createSplitPane();
        add(splitPane, BorderLayout.CENTER);
    }

    /**
     * Create the top navigation bar.
     */
    private JPanel createNavBar() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(StyleUtils.PRIMARY_BLUE);
        navBar.setPreferredSize(new Dimension(0, 70));
        navBar.setBorder(new EmptyBorder(15, 25, 15, 25));

        // Left side: Back button en BLEU
        JButton backButton = createModernButton("← Back to Dashboard",
                new Color(0, 123, 255), // BLEU vif
                new Color(0, 86, 179),  // BLEU foncé (hover)
                Color.WHITE);
        backButton.addActionListener(e -> parentApp.returnToDashboard());
        navBar.add(backButton, BorderLayout.WEST);

        // Center: Lesson title
        lessonTitleLabel = new JLabel("Lesson Title", SwingConstants.CENTER);
        lessonTitleLabel.setFont(StyleUtils.FONT_SUBHEADER);
        lessonTitleLabel.setForeground(StyleUtils.TEXT_LIGHT);
        navBar.add(lessonTitleLabel, BorderLayout.CENTER);

        // Right side: spacer (for symmetry)
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(180, 0));
        navBar.add(spacer, BorderLayout.EAST);

        return navBar;
    }

    /**
     * Create the main split pane layout.
     */
    private JSplitPane createSplitPane() {
        // Create left and right panels
        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();

        // Create split pane
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,  // Left/Right split
                leftPanel,
                rightPanel
        );

        // Configure split pane
        splitPane.setDividerLocation(0.45);      // Start at 45% from left
        splitPane.setResizeWeight(0.45);          // When resizing, give 45% to left
        splitPane.setDividerSize(8);              // Divider width in pixels
        splitPane.setContinuousLayout(true);      // Smooth resizing
        splitPane.setBorder(null);

        return splitPane;
    }

    /**
     * Create the left panel with theory and video button.
     */
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(StyleUtils.CARD_WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 10));

        // Header avec icône
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        headerPanel.setBackground(StyleUtils.CARD_WHITE);

        JLabel iconLabel = new JLabel("📖");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        headerPanel.add(iconLabel);

        JLabel headerLabel = new JLabel("Lesson Theory");
        headerLabel.setFont(StyleUtils.FONT_SUBHEADER);
        headerLabel.setForeground(StyleUtils.TEXT_DARK);
        headerPanel.add(headerLabel);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Theory text area (scrollable)
        theoryTextArea = new JTextArea();
        theoryTextArea.setFont(StyleUtils.FONT_BODY);
        theoryTextArea.setLineWrap(true);           // Wrap long lines
        theoryTextArea.setWrapStyleWord(true);      // Wrap at word boundaries
        theoryTextArea.setEditable(false);          // Read-only
        theoryTextArea.setBackground(StyleUtils.CARD_WHITE);
        theoryTextArea.setForeground(StyleUtils.TEXT_DARK);
        theoryTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane theoryScroll = new JScrollPane(theoryTextArea);
        theoryScroll.setBorder(BorderFactory.createLineBorder(StyleUtils.BACKGROUND_GRAY));
        theoryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(theoryScroll, BorderLayout.CENTER);

        // Bottom: Watch Video button en BLEU
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(StyleUtils.CARD_WHITE);
        bottomPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        watchVideoButton = createModernButton("🎬 Watch Video Tutorial",
                new Color(0, 123, 255),     // BLEU vif
                new Color(0, 86, 179),      // BLEU foncé (hover)
                Color.WHITE);
        watchVideoButton.setPreferredSize(new Dimension(0, 50));
        watchVideoButton.addActionListener(e -> openYoutubeVideo());
        bottomPanel.add(watchVideoButton, BorderLayout.CENTER);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Create the right panel with code editor and submit button.
     */
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(StyleUtils.CARD_WHITE);
        panel.setBorder(new EmptyBorder(20, 10, 20, 20));

        // Header with instructions
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleUtils.CARD_WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(StyleUtils.CARD_WHITE);

        JLabel codeIcon = new JLabel("💻");
        codeIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        titlePanel.add(codeIcon);

        JLabel headerLabel = new JLabel("Practice Exercise");
        headerLabel.setFont(StyleUtils.FONT_SUBHEADER);
        headerLabel.setForeground(StyleUtils.TEXT_DARK);
        titlePanel.add(headerLabel);

        headerPanel.add(titlePanel, BorderLayout.NORTH);

        JLabel instructionLabel = new JLabel("<html>Write your code below. Your answer should contain the <b>correct keyword</b> from the lesson.</html>");
        instructionLabel.setFont(StyleUtils.FONT_BODY);
        instructionLabel.setForeground(StyleUtils.TEXT_MUTED);
        instructionLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
        headerPanel.add(instructionLabel, BorderLayout.CENTER);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Code editor (dark theme)
        codeEditorArea = new JTextArea();
        codeEditorArea.setFont(StyleUtils.FONT_CODE);
        codeEditorArea.setBackground(StyleUtils.EDITOR_DARK);
        codeEditorArea.setForeground(StyleUtils.TEXT_LIGHT);
        codeEditorArea.setCaretColor(StyleUtils.TEXT_LIGHT);  // Cursor color
        codeEditorArea.setLineWrap(true);
        codeEditorArea.setWrapStyleWord(true);
        codeEditorArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        codeEditorArea.setText("// Write your Java code here...\n\n");

        JScrollPane codeScroll = new JScrollPane(codeEditorArea);
        codeScroll.setBorder(BorderFactory.createLineBorder(StyleUtils.EDITOR_DARK, 2));
        panel.add(codeScroll, BorderLayout.CENTER);

        // Bottom panel: Feedback + Submit button
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        bottomPanel.setBackground(StyleUtils.CARD_WHITE);
        bottomPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Feedback label (shows success/error messages)
        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(StyleUtils.FONT_BODY);
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(feedbackLabel, BorderLayout.NORTH);

        // Submit button en VERT
        submitButton = createModernButton("✓ Submit Answer",
                new Color(40, 167, 69),     // VERT vif (success)
                new Color(33, 136, 56),     // VERT foncé (hover)
                Color.WHITE);
        submitButton.setPreferredSize(new Dimension(0, 50));
        submitButton.addActionListener(e -> checkAnswer());
        bottomPanel.add(submitButton, BorderLayout.CENTER);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Create a modern styled button with hover effects.
     */
    private JButton createModernButton(String text, Color bgColor, Color hoverColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(StyleUtils.FONT_BUTTON);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 25, 12, 25));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
                button.setBorder(BorderFactory.createLineBorder(hoverColor.darker(), 1));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
                button.setBorder(new EmptyBorder(12, 25, 12, 25));
            }
        });

        return button;
    }

    // ==================== PUBLIC METHODS ====================

    /**
     * Load a lesson into the view.
     */
    public void loadLesson(Lesson lesson) {
        this.currentLesson = lesson;

        // Update UI with lesson content
        lessonTitleLabel.setText(lesson.getTitle());
        theoryTextArea.setText(lesson.getTheoryText());

        // Reset the code editor
        codeEditorArea.setText("// Write your Java code here...\n" +
                "// Hint: Your answer should include the correct keyword.\n\n");

        // Clear any previous feedback
        feedbackLabel.setText(" ");
        feedbackLabel.setForeground(StyleUtils.TEXT_DARK);

        // Re-enable submit button (in case it was disabled)
        submitButton.setEnabled(true);
        submitButton.setBackground(new Color(40, 167, 69)); // Vert vif

        // Scroll theory to top
        theoryTextArea.setCaretPosition(0);
    }

    // ==================== EVENT HANDLERS ====================

    /**
     * Open the YouTube video in the system browser.
     */
    private void openYoutubeVideo() {
        if (currentLesson == null) {
            return;
        }

        try {
            // Get the YouTube URL from the lesson
            String url = currentLesson.getYoutubeUrl();

            // Check if Desktop is supported on this system
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();

                // Check if browsing is supported
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(url));
                    System.out.println("Opening video: " + url);
                } else {
                    showVideoError("Browser not supported on this system.");
                }
            } else {
                showVideoError("Desktop not supported on this system.");
            }

        } catch (Exception e) {
            System.err.println("Error opening video: " + e.getMessage());
            showVideoError("Could not open video: " + e.getMessage());
        }
    }

    /**
     * Show an error message for video issues.
     */
    private void showVideoError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message + "\n\nYou can manually open: " + currentLesson.getYoutubeUrl(),
                "Video Error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Check if the student's answer is correct.
     */
    private void checkAnswer() {
        if (currentLesson == null) {
            return;
        }

        // Get user's answer
        String userAnswer = codeEditorArea.getText();

        // Check if it's correct
        boolean isCorrect = currentLesson.checkAnswer(userAnswer);

        if (isCorrect) {
            handleCorrectAnswer();
        } else {
            handleWrongAnswer();
        }
    }

    /**
     * Handle a correct answer.
     */
    private void handleCorrectAnswer() {
        // Show success feedback
        feedbackLabel.setText("✓ Correct! Well done! +" + XP_REWARD + " XP");
        feedbackLabel.setForeground(new Color(40, 167, 69)); // Vert vif

        // Get current user
        User user = parentApp.getCurrentUser();

        // Award XP if the user is a student
        if (user instanceof Student) {
            Student student = (Student) user;

            // Only award XP if this lesson wasn't already completed
            if (!student.hasCompletedLesson(currentLesson.getId())) {
                student.addXp(XP_REWARD);
                student.completeLesson(currentLesson.getId());

                // Save progress to CSV
                dataManager.saveUserProgress(student);

                System.out.println("Student " + student.getUsername() +
                        " completed lesson " + currentLesson.getId() +
                        " - Total XP: " + student.getXpScore());
            } else {
                // Already completed - update message
                feedbackLabel.setText("✓ Correct! (Already completed - no extra XP)");
            }
        }

        // Disable submit button to prevent re-submission
        submitButton.setEnabled(false);
        submitButton.setBackground(new Color(108, 117, 125)); // Gris
        submitButton.setText("✓ Lesson Complete");

        // Show success dialog
        JOptionPane.showMessageDialog(
                this,
                "Congratulations! You've completed this lesson.\n" +
                        "Click OK to return to the dashboard.",
                "Lesson Complete!",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Return to dashboard
        parentApp.returnToDashboard();
    }

    /**
     * Handle a wrong answer.
     */
    private void handleWrongAnswer() {
        // Show error feedback
        feedbackLabel.setText("✗ Not quite right. Check your code and try again!");
        feedbackLabel.setForeground(new Color(220, 53, 69)); // Rouge vif

        // Briefly flash the submit button red
        submitButton.setBackground(new Color(220, 53, 69));

        // Reset button color after a short delay
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitButton.setBackground(new Color(40, 167, 69)); // Vert vif
            }
        });
        timer.setRepeats(false);  // Only run once
        timer.start();
    }
}