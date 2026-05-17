package app;

import services.CsvDataManager;
import services.DataManager;
import services.FirebaseDataManager;
import model.Student;
import model.User;
import utils.StyleUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * LoginView - Modern, polished login screen with animations.
 * 
 * FEATURES:
 * - Sign In: Login with existing credentials
 * - Sign Up: Register a new student account
 * - Continue as Guest: Browse without saving progress
 * - Animated transitions and micro-interactions
 * - Toast notifications for feedback
 */
public class LoginView extends JPanel {

    // ==================== UI COMPONENTS ====================
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton loginButton;
    private JButton signupButton;
    private JButton guestButton;
    private JButton switchModeButton;
    private JLabel errorLabel;
    private JLabel cardTitleLabel;
    private JLabel cardSubtitleLabel;
    private JLabel confirmPasswordLabel;
    private JPanel cardPanel;
    
    // Reference to parent application
    private MainApplication parentApp;
    
    // Data manager for authentication
    private DataManager dataManager;

    // Track current mode: LOGIN or SIGNUP
    private boolean isSignupMode = false;

    // ==================== CONSTRUCTOR ====================
    
    public LoginView(MainApplication parentApp) {
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
        
        // Create the center login card
        JPanel loginCard = createLoginCard();
        
        // Center wrapper with animated background
        JPanel centerWrapper = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Subtle decorative circles in background
                g2.setColor(new Color(37, 99, 235, 8));
                g2.fillOval(-100, -100, 400, 400);
                g2.fillOval(getWidth() - 200, getHeight() - 300, 500, 500);
                
                g2.setColor(new Color(37, 99, 235, 5));
                g2.fillOval(getWidth() / 2 - 150, -200, 300, 300);
                
                g2.dispose();
            }
        };
        centerWrapper.setBackground(StyleUtils.BACKGROUND_GRAY);
        centerWrapper.add(loginCard);
        
        add(centerWrapper, BorderLayout.CENTER);
        
        // Header at top
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);
        
        // Footer
        add(createFooter(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeader() {
        return StyleUtils.createBrandedHeader(100, true);
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 0, 20, 0));
        
        JLabel footerText = new JLabel("© 2024 IHEC Skills • Learn. Grow. Succeed.");
        footerText.setFont(StyleUtils.FONT_SMALL);
        footerText.setForeground(StyleUtils.TEXT_MUTED);
        footer.add(footerText);
        
        return footer;
    }
    
    private JPanel createLoginCard() {
        // Main card with glass morphism effect
        cardPanel = new JPanel() {
            private float glowIntensity = 0f;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Outer glow
                for (int i = 12; i > 0; i--) {
                    float alpha = 0.01f * (12 - i);
                    g2.setColor(new Color(37, 99, 235, (int)(alpha * 255)));
                    g2.fill(new RoundRectangle2D.Float(
                        -i, -i, getWidth() + i * 2, getHeight() + i * 2, 24 + i, 24 + i));
                }
                
                // Card shadow
                g2.setColor(new Color(15, 23, 42, 25));
                g2.fill(new RoundRectangle2D.Float(6, 6, getWidth() - 6, getHeight() - 6, 24, 24));
                
                // Card background with subtle gradient
                GradientPaint gradient = new GradientPaint(
                    0, 0, StyleUtils.CARD_WHITE,
                    0, getHeight(), new Color(248, 250, 252)
                );
                g2.setPaint(gradient);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 6, getHeight() - 6, 24, 24));
                
                // Top accent line
                GradientPaint accentGradient = new GradientPaint(
                    0, 0, StyleUtils.PRIMARY_BLUE,
                    getWidth(), 0, new Color(96, 165, 250)
                );
                g2.setPaint(accentGradient);
                g2.fillRoundRect(0, 0, getWidth() - 6, 4, 4, 4);
                
                g2.dispose();
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setPreferredSize(new Dimension(440, 560));
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(new EmptyBorder(40, 50, 35, 50));
        
        // Header section
        JPanel headerSection = new JPanel();
        headerSection.setOpaque(false);
        headerSection.setLayout(new BoxLayout(headerSection, BoxLayout.Y_AXIS));
        
        // Welcome icon
        JLabel welcomeIcon = new JLabel("👋");
        welcomeIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        welcomeIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerSection.add(welcomeIcon);
        headerSection.add(Box.createVerticalStrut(12));
        
        cardTitleLabel = new JLabel("Welcome Back!", SwingConstants.CENTER);
        cardTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        cardTitleLabel.setForeground(StyleUtils.TEXT_DARK);
        cardTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerSection.add(cardTitleLabel);
        
        cardSubtitleLabel = new JLabel("Sign in to continue learning", SwingConstants.CENTER);
        cardSubtitleLabel.setFont(StyleUtils.FONT_BODY);
        cardSubtitleLabel.setForeground(StyleUtils.TEXT_MUTED);
        cardSubtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardSubtitleLabel.setBorder(new EmptyBorder(6, 0, 20, 0));
        headerSection.add(cardSubtitleLabel);
        
        cardPanel.add(headerSection, BorderLayout.NORTH);
        
        // Form section
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        
        // Username field with icon
        formPanel.add(createFieldLabel("Username", "👤"));
        formPanel.add(Box.createVerticalStrut(8));
        
        usernameField = StyleUtils.createModernTextField();
        usernameField.setPreferredSize(new Dimension(350, 42));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        usernameField.setMinimumSize(new Dimension(200, 42));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(10));
        
        // Password field
        formPanel.add(createFieldLabel("Password", "🔒"));
        formPanel.add(Box.createVerticalStrut(6));
        
        passwordField = StyleUtils.createModernPasswordField();
        passwordField.setPreferredSize(new Dimension(350, 42));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        passwordField.setMinimumSize(new Dimension(200, 42));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(10));
        
        // Confirm Password (signup only)
        confirmPasswordLabel = createFieldLabel("Confirm Password", "🔒");
        confirmPasswordLabel.setVisible(false);
        formPanel.add(confirmPasswordLabel);
        formPanel.add(Box.createVerticalStrut(6));
        
        confirmPasswordField = StyleUtils.createModernPasswordField();
        confirmPasswordField.setPreferredSize(new Dimension(350, 42));
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        confirmPasswordField.setMinimumSize(new Dimension(200, 42));
        confirmPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmPasswordField.setVisible(false);
        formPanel.add(confirmPasswordField);
        formPanel.add(Box.createVerticalStrut(8));
        
        // Error label with icon
        errorLabel = new JLabel(" ");
        errorLabel.setFont(StyleUtils.FONT_SMALL);
        errorLabel.setForeground(StyleUtils.ERROR_RED);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(errorLabel);
        
        cardPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button section
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(new EmptyBorder(25, 0, 0, 0));
        
        // Sign In button - smaller height
        loginButton = StyleUtils.createModernButton("Sign In", StyleUtils.PRIMARY_BLUE);
        loginButton.addActionListener(e -> handleLogin());
        loginButton.setPreferredSize(new Dimension(200, 40));
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        
        // Sign Up button (hidden initially) - smaller height
        signupButton = StyleUtils.createModernButton("Create Account", StyleUtils.SUCCESS_GREEN);
        signupButton.addActionListener(e -> handleSignup());
        signupButton.setPreferredSize(new Dimension(200, 40));
        signupButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.setVisible(false);
        buttonPanel.add(signupButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        
        // Guest button - ghost style, smaller height
        guestButton = StyleUtils.createGhostButton("Continue as Guest", StyleUtils.TEXT_MUTED);
        guestButton.addActionListener(e -> handleGuestLogin());
        guestButton.setPreferredSize(new Dimension(200, 38));
        guestButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        guestButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(guestButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        
        // Divider
        JPanel divider = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(StyleUtils.BORDER_LIGHT);
                int y = getHeight() / 2;
                g2.drawLine(0, y, getWidth() / 3 - 20, y);
                g2.drawLine(getWidth() * 2 / 3 + 20, y, getWidth(), y);
                g2.dispose();
            }
        };
        divider.setOpaque(false);
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        divider.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel orLabel = new JLabel("or", SwingConstants.CENTER);
        orLabel.setFont(StyleUtils.FONT_SMALL);
        orLabel.setForeground(StyleUtils.TEXT_MUTED);
        divider.setLayout(new GridBagLayout());
        divider.add(orLabel);
        
        buttonPanel.add(divider);
        buttonPanel.add(Box.createVerticalStrut(16));
        
        // Switch mode link
        switchModeButton = new JButton("Don't have an account? Sign up");
        switchModeButton.setFont(StyleUtils.FONT_BODY);
        switchModeButton.setForeground(StyleUtils.PRIMARY_BLUE);
        switchModeButton.setBackground(null);
        switchModeButton.setBorderPainted(false);
        switchModeButton.setContentAreaFilled(false);
        switchModeButton.setFocusPainted(false);
        switchModeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchModeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        switchModeButton.addActionListener(e -> toggleMode());
        
        switchModeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                switchModeButton.setText("<html><u>" + switchModeButton.getText().replaceAll("<[^>]*>", "") + "</u></html>");
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (isSignupMode) {
                    switchModeButton.setText("Already have an account? Sign in");
                } else {
                    switchModeButton.setText("Don't have an account? Sign up");
                }
            }
        });
        
        buttonPanel.add(switchModeButton);
        
        // Enter key handlers
        passwordField.addActionListener(e -> {
            if (isSignupMode) {
                confirmPasswordField.requestFocus();
            } else {
                handleLogin();
            }
        });
        confirmPasswordField.addActionListener(e -> handleSignup());
        usernameField.addActionListener(e -> passwordField.requestFocus());
        
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return cardPanel;
    }
    
    private JLabel createFieldLabel(String text, String icon) {
        JLabel label = new JLabel(icon + "  " + text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(StyleUtils.TEXT_DARK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    /**
     * Toggle between Login and Signup modes with animation.
     */
    private void toggleMode() {
        isSignupMode = !isSignupMode;
        clearError();
        clearFields();
        
        if (isSignupMode) {
            cardTitleLabel.setText("Create Account");
            cardSubtitleLabel.setText("Join us and start learning today");
            confirmPasswordLabel.setVisible(true);
            confirmPasswordField.setVisible(true);
            loginButton.setVisible(false);
            signupButton.setVisible(true);
            guestButton.setVisible(false);
            switchModeButton.setText("Already have an account? Sign in");
            
            // Update card size for extra field
            cardPanel.setPreferredSize(new Dimension(440, 620));
        } else {
            cardTitleLabel.setText("Welcome Back!");
            cardSubtitleLabel.setText("Sign in to continue learning");
            confirmPasswordLabel.setVisible(false);
            confirmPasswordField.setVisible(false);
            loginButton.setVisible(true);
            signupButton.setVisible(false);
            guestButton.setVisible(true);
            switchModeButton.setText("Don't have an account? Sign up");
            
            // Reset card size
            cardPanel.setPreferredSize(new Dimension(440, 560));
        }
        
        revalidate();
        repaint();
    }

    // ==================== EVENT HANDLERS ====================
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            shakeComponent(usernameField.getText().isEmpty() ? usernameField : passwordField);
            return;
        }
        
        // Show loading state
        loginButton.setText("Signing in...");
        loginButton.setEnabled(false);
        
        // Simulate brief delay for UX
        Timer loginTimer = new Timer(400, e -> {
            User user = dataManager.authenticate(username, password);
            
            if (user != null) {
                clearError();
                clearFields();
                loginButton.setText("Sign In");
                loginButton.setEnabled(true);
                
                // Show success toast
                SwingUtilities.invokeLater(() -> {
                    StyleUtils.showToast((JFrame) SwingUtilities.getWindowAncestor(this), 
                        "Welcome back, " + user.getUsername() + "!", StyleUtils.ToastType.SUCCESS);
                });
                
                parentApp.onLoginSuccess(user);
            } else {
                showError("Invalid username or password.");
                passwordField.setText("");
                loginButton.setText("Sign In →");
                loginButton.setEnabled(true);
                shakeComponent(cardPanel);
            }
        });
        loginTimer.setRepeats(false);
        loginTimer.start();
    }
    
    private void handleSignup() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }
        
        if (username.length() < 3) {
            showError("Username must be at least 3 characters.");
            shakeComponent(usernameField);
            return;
        }
        
        if (username.length() > 20) {
            showError("Username must be 20 characters or less.");
            shakeComponent(usernameField);
            return;
        }
        
        if (password.length() < 4) {
            showError("Password must be at least 4 characters.");
            shakeComponent(passwordField);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            confirmPasswordField.setText("");
            shakeComponent(confirmPasswordField);
            return;
        }
        
        if (dataManager.isUsernameTaken(username)) {
            showError("Username is already taken.");
            shakeComponent(usernameField);
            return;
        }
        
        // Show loading state
        signupButton.setText("Creating account...");
        signupButton.setEnabled(false);
        
        Timer signupTimer = new Timer(500, e -> {
            Student newUser = dataManager.registerUser(username, password);
            
            if (newUser != null) {
                clearError();
                clearFields();
                signupButton.setText("Create Account");
                signupButton.setEnabled(true);
                
                // Show success toast
                SwingUtilities.invokeLater(() -> {
                    StyleUtils.showToast((JFrame) SwingUtilities.getWindowAncestor(this), 
                        "Account created successfully! Welcome, " + username + "!", 
                        StyleUtils.ToastType.SUCCESS);
                });
                
                toggleMode();
                parentApp.onLoginSuccess(newUser);
            } else {
                showError("Registration failed. Please try again.");
                signupButton.setText("Create Account");
                signupButton.setEnabled(true);
            }
        });
        signupTimer.setRepeats(false);
        signupTimer.start();
    }
    
    private void handleGuestLogin() {
        guestButton.setText("Loading...");
        guestButton.setEnabled(false);
        
        Timer guestTimer = new Timer(300, e -> {
            Student guestUser = dataManager.createGuestUser();
            clearFields();
            clearError();
            
            guestButton.setText("Continue as Guest");
            guestButton.setEnabled(true);
            
            // Show info toast
            SwingUtilities.invokeLater(() -> {
                StyleUtils.showToast((JFrame) SwingUtilities.getWindowAncestor(this), 
                    "Browsing as guest. Progress won't be saved.", StyleUtils.ToastType.INFO);
            });
            
            parentApp.onLoginSuccess(guestUser);
        });
        guestTimer.setRepeats(false);
        guestTimer.start();
    }
    
    /**
     * Shake animation for error feedback.
     */
    private void shakeComponent(JComponent component) {
        Point original = component.getLocation();
        Timer shakeTimer = new Timer(30, null);
        final int[] step = {0};
        final int[] offsets = {10, -10, 8, -8, 5, -5, 2, -2, 0};
        
        shakeTimer.addActionListener(e -> {
            if (step[0] < offsets.length) {
                component.setLocation(original.x + offsets[step[0]], original.y);
                step[0]++;
            } else {
                component.setLocation(original);
                shakeTimer.stop();
            }
        });
        shakeTimer.start();
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setForeground(StyleUtils.ERROR_RED);
    }
    
    private void clearError() {
        errorLabel.setText(" ");
    }
    
    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
}
