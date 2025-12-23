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
import java.awt.geom.RoundRectangle2D;
import java.net.URI;

/**
 * LearningView - The core learning interface with split-pane layout.
 * 
 * LAYOUT STRUCTURE:
 * ┌─────────────────────────────────────────────────────────────┐
 * │                    TOP NAVIGATION BAR                       │
 * ├────────────────────────┬────────────────────────────────────┤
 * │                        │                                    │
 * │   LEFT PANEL           │    RIGHT PANEL                     │
 * │   ┌──────────────┐     │    ┌────────────────────────────┐  │
 * │   │ Theory Text  │     │    │                            │  │
 * │   │ (JTextArea)  │     │    │   Code Editor              │  │
 * │   │              │     │    │   (Dark Theme JTextArea)   │  │
 * │   └──────────────┘     │    │                            │  │
 * │   ┌──────────────┐     │    └────────────────────────────┘  │
 * │   │ Watch Video  │     │    ┌────────────────────────────┐  │
 * │   │   Button     │     │    │      Submit Answer         │  │
 * │   └──────────────┘     │    └────────────────────────────┘  │
 * └────────────────────────┴────────────────────────────────────┘
 * 
 * KEY COMPONENTS:
 * - JSplitPane: Creates the resizable left/right division
 * - JTextArea: For multi-line text (theory and code)
 * - JScrollPane: Makes text areas scrollable
 * - Desktop.browse(): Opens YouTube in system browser
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
        
        // Center: Lesson title
        lessonTitleLabel = new JLabel("Lesson Title", SwingConstants.CENTER);
        lessonTitleLabel.setFont(StyleUtils.FONT_SUBHEADER);
        lessonTitleLabel.setForeground(StyleUtils.TEXT_LIGHT);
        navBar.add(lessonTitleLabel, BorderLayout.CENTER);
        
        // Right side: spacer (for symmetry)
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(170, 0));
        navBar.add(spacer, BorderLayout.EAST);
        
        return navBar;
    }
    
    /**
     * Create the main split pane layout.
     * 
     * JSplitPane EXPLAINED:
     * - Divides the area into two resizable sections
     * - HORIZONTAL_SPLIT = left and right
     * - VERTICAL_SPLIT = top and bottom
     * - setDividerLocation(0.5) = starts at 50%
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
        // Card-style panel
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(StyleUtils.CARD_WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(24, 24, 24, 12));
        
        // Header with icon
        JLabel headerLabel = new JLabel("📖 Lesson Theory");
        headerLabel.setFont(StyleUtils.FONT_SUBHEADER);
        headerLabel.setForeground(StyleUtils.TEXT_DARK);
        headerLabel.setBorder(new EmptyBorder(0, 0, 18, 0));
        panel.add(headerLabel, BorderLayout.NORTH);
        
        // Theory text area (scrollable) - modern styling
        theoryTextArea = new JTextArea();
        theoryTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        theoryTextArea.setLineWrap(true);
        theoryTextArea.setWrapStyleWord(true);
        theoryTextArea.setEditable(false);
        theoryTextArea.setBackground(StyleUtils.CARD_WHITE);
        theoryTextArea.setForeground(StyleUtils.TEXT_DARK);
        theoryTextArea.setBorder(new EmptyBorder(12, 12, 12, 12));
        theoryTextArea.setSelectionColor(StyleUtils.PRIMARY_BLUE);
        theoryTextArea.setSelectedTextColor(StyleUtils.TEXT_LIGHT);
        
        JScrollPane theoryScroll = new JScrollPane(theoryTextArea);
        theoryScroll.setBorder(BorderFactory.createLineBorder(StyleUtils.BORDER_LIGHT, 1));
        theoryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        theoryScroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(theoryScroll, BorderLayout.CENTER);
        
        // Bottom: Watch Video button - modern style
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(18, 0, 0, 0));
        
        watchVideoButton = StyleUtils.createModernButton("▶ Watch Video Tutorial", StyleUtils.PRIMARY_BLUE);
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
        // Card-style panel
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(StyleUtils.CARD_WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(24, 12, 24, 24));
        
        // Header with instructions
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 18, 0));
        
        JLabel headerLabel = new JLabel("💻 Practice Exercise");
        headerLabel.setFont(StyleUtils.FONT_SUBHEADER);
        headerLabel.setForeground(StyleUtils.TEXT_DARK);
        headerPanel.add(headerLabel, BorderLayout.NORTH);
        
        JLabel instructionLabel = new JLabel("<html>Write your code below. Your answer should contain the <b>correct keyword</b> from the lesson.</html>");
        instructionLabel.setFont(StyleUtils.FONT_BODY);
        instructionLabel.setForeground(StyleUtils.TEXT_MUTED);
        instructionLabel.setBorder(new EmptyBorder(8, 0, 0, 0));
        headerPanel.add(instructionLabel, BorderLayout.CENTER);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Code editor with enhanced styling
        codeEditorArea = new JTextArea() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        codeEditorArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        codeEditorArea.setBackground(StyleUtils.EDITOR_DARK);
        codeEditorArea.setForeground(new Color(166, 227, 161));
        codeEditorArea.setCaretColor(StyleUtils.TEXT_LIGHT);
        codeEditorArea.setSelectionColor(new Color(69, 133, 136));
        codeEditorArea.setSelectedTextColor(StyleUtils.TEXT_LIGHT);
        codeEditorArea.setLineWrap(true);
        codeEditorArea.setWrapStyleWord(true);
        codeEditorArea.setBorder(new EmptyBorder(20, 20, 20, 20));
        codeEditorArea.setText("// Write your code here...\n\n");
        
        // Styled scroll pane with rounded corners
        JScrollPane codeScroll = new JScrollPane(codeEditorArea) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(StyleUtils.EDITOR_DARK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        codeScroll.setOpaque(false);
        codeScroll.setBorder(null);
        codeScroll.getVerticalScrollBar().setUnitIncrement(16);
        
        // Wrap in panel for rounded effect
        JPanel editorWrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(StyleUtils.EDITOR_DARK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        editorWrapper.setOpaque(false);
        editorWrapper.add(codeScroll, BorderLayout.CENTER);
        panel.add(editorWrapper, BorderLayout.CENTER);
        
        // Bottom panel with enhanced feedback
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 12));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(18, 0, 0, 0));
        
        // Feedback panel with icon support
        JPanel feedbackPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        feedbackPanel.setOpaque(false);
        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        feedbackPanel.add(feedbackLabel);
        bottomPanel.add(feedbackPanel, BorderLayout.NORTH);
        
        // Submit button with icon
        submitButton = StyleUtils.createModernButton("✓ Submit Answer", StyleUtils.SUCCESS_GREEN);
        submitButton.setPreferredSize(new Dimension(0, 50));
        submitButton.addActionListener(e -> checkAnswer());
        bottomPanel.add(submitButton, BorderLayout.CENTER);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    // ==================== PUBLIC METHODS ====================
    
    public void loadLesson(Lesson lesson) {
        this.currentLesson = lesson;
        
        // Update UI with lesson content
        lessonTitleLabel.setText(lesson.getTitle());
        theoryTextArea.setText(lesson.getTheoryText());
        
        // Reset the code editor with styled placeholder
        codeEditorArea.setText("// Write your code here...\n" +
                              "// Hint: Your answer should include the correct keyword.\n\n");
        
        // Clear feedback
        feedbackLabel.setText(" ");
        feedbackLabel.setForeground(StyleUtils.TEXT_DARK);
        
        // Re-enable submit button
        submitButton.setEnabled(true);
        submitButton.setBackground(StyleUtils.SUCCESS_GREEN);
        submitButton.setText("✓ Submit Answer");
        
        // Scroll to top
        theoryTextArea.setCaretPosition(0);
    }

    // ==================== EVENT HANDLERS ====================
    
    private void openYoutubeVideo() {
        if (currentLesson == null) return;
        
        try {
            String url = currentLesson.getYoutubeUrl();
            
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(url));
                    
                    // Show toast
                    SwingUtilities.invokeLater(() -> {
                        StyleUtils.showToast((JFrame) SwingUtilities.getWindowAncestor(this), 
                            "Opening video in browser...", StyleUtils.ToastType.INFO);
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Error opening video: " + e.getMessage());
            StyleUtils.showToast((JFrame) SwingUtilities.getWindowAncestor(this), 
                "Could not open video", StyleUtils.ToastType.ERROR);
        }
    }
    
    private void checkAnswer() {
        if (currentLesson == null) return;
        
        String userAnswer = codeEditorArea.getText();
        boolean isCorrect = currentLesson.checkAnswer(userAnswer);
        
        if (isCorrect) {
            handleCorrectAnswer();
        } else {
            handleWrongAnswer();
        }
    }
    
    private void handleCorrectAnswer() {
        feedbackLabel.setText("🎉 Correct! Well done! +" + XP_REWARD + " XP");
        feedbackLabel.setForeground(StyleUtils.SUCCESS_GREEN);
        
        User user = parentApp.getCurrentUser();
        
        if (user instanceof Student) {
            Student student = (Student) user;
            
            if (!student.hasCompletedLesson(currentLesson.getId())) {
                student.addXp(XP_REWARD);
                student.completeLesson(currentLesson.getId());
                dataManager.saveUserProgress(student);
            } else {
                feedbackLabel.setText("✓ Correct! (Already completed)");
            }
        }
        
        submitButton.setEnabled(false);
        submitButton.setBackground(StyleUtils.TEXT_MUTED);
        submitButton.setText("✓ Lesson Complete");
        
        // Show success toast
        SwingUtilities.invokeLater(() -> {
            StyleUtils.showToast((JFrame) SwingUtilities.getWindowAncestor(this), 
                "Lesson completed! +" + XP_REWARD + " XP earned!", StyleUtils.ToastType.SUCCESS);
        });
        
        // Delayed return to dashboard
        Timer returnTimer = new Timer(1500, e -> parentApp.returnToDashboard());
        returnTimer.setRepeats(false);
        returnTimer.start();
    }
    
    private void handleWrongAnswer() {
        feedbackLabel.setText("❌ Not quite right. Check your code and try again!");
        feedbackLabel.setForeground(StyleUtils.ERROR_RED);
        
        // Shake the submit button
        Point original = submitButton.getLocation();
        Timer shakeTimer = new Timer(30, null);
        final int[] step = {0};
        final int[] offsets = {5, -5, 4, -4, 3, -3, 0};
        
        submitButton.setBackground(StyleUtils.ERROR_RED);
        
        shakeTimer.addActionListener(e -> {
            if (step[0] < offsets.length) {
                submitButton.setLocation(original.x + offsets[step[0]], original.y);
                step[0]++;
            } else {
                submitButton.setLocation(original);
                submitButton.setBackground(StyleUtils.SUCCESS_GREEN);
                shakeTimer.stop();
            }
        });
        shakeTimer.start();
        
        // Show toast
        SwingUtilities.invokeLater(() -> {
            StyleUtils.showToast((JFrame) SwingUtilities.getWindowAncestor(this), 
                "Try again! Check your answer.", StyleUtils.ToastType.WARNING);
        });
    }
}
