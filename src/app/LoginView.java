package app;

import services.CsvDataManager;
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
 * LoginView - The login screen of the application.
 * 
 * FEATURES:
 * - Sign In: Login with existing credentials
 * - Sign Up: Register a new student account
 * - Continue as Guest: Browse without saving progress
 * 
// * SWING BASICS:
 * - JPanel: A container that holds other components
 * - JLabel: Displays text
 * - JTextField: Input field for text
 * - JPasswordField: Input field that hides characters
 * - JButton: A clickable button
 */
public class LoginView extends JPanel {

    // ==================== UI COMPONENTS ====================
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;  // For signup mode
    private JButton loginButton;
    private JButton signupButton;
    private JButton guestButton;
    private JButton switchModeButton;  // Toggle between login/signup
    private JLabel errorLabel;
    private JLabel cardTitleLabel;
    private JLabel confirmPasswordLabel;
    
    // Reference to parent application (for switching views)
    private MainApplication parentApp;
    
    // Data manager for authentication
    private CsvDataManager dataManager;
    
    // Track current mode: LOGIN or SIGNUP
    private boolean isSignupMode = false;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Create the login view.
     * 
     * @param parentApp Reference to the main application window
     */
    public LoginView(MainApplication parentApp) {
        this.parentApp = parentApp;
        this.dataManager = new CsvDataManager();
        
        // Initialize the UI
        initializeUI();
    }

    // ==================== UI INITIALIZATION ====================
    
    /**
     * Set up the login form UI.
     */
    private void initializeUI() {
        // Set panel layout and background
        setLayout(new BorderLayout());
        setBackground(StyleUtils.BACKGROUND_GRAY);
        
        // Create the center login card
        JPanel loginCard = createLoginCard();
        
        // Add the card to the center of this panel
        // We use a wrapper panel to center it properly
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(StyleUtils.BACKGROUND_GRAY);
        centerWrapper.add(loginCard);
        
        add(centerWrapper, BorderLayout.CENTER);
        
        // Add a header at the top
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);
    }
    
    /**
     * Create the header panel with the app title.
     */
    private JPanel createHeader() {
        JPanel header = StyleUtils.createGradientHeader(90);
        header.setBorder(new EmptyBorder(0, 0, 0, 0));
        header.setLayout(new GridBagLayout());
        
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        
        JLabel title = new JLabel("IHEC-JLearn");
        title.setFont(StyleUtils.FONT_HEADER);
        title.setForeground(StyleUtils.TEXT_LIGHT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(title);
        
        JLabel subtitle = new JLabel("Master Java Programming");
        subtitle.setFont(StyleUtils.FONT_SMALL);
        subtitle.setForeground(new Color(255, 255, 255, 200));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);
        
        header.add(titlePanel);
        return header;
    }
    
    /**
     * Create the white login card with form fields.
     */
    private JPanel createLoginCard() {
        // Main card panel with modern shadow effect
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2.setColor(new Color(15, 23, 42, 20));
                g2.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 20, 20);
                
                // Draw card background
                g2.setColor(StyleUtils.CARD_WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 20, 20);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(420, 520));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(30, 45, 30, 45));
        
        // Card title (changes based on mode)
        cardTitleLabel = new JLabel("Welcome Back!", SwingConstants.CENTER);
        cardTitleLabel.setFont(StyleUtils.FONT_SUBHEADER);
        cardTitleLabel.setForeground(StyleUtils.TEXT_DARK);
        cardTitleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        card.add(cardTitleLabel, BorderLayout.NORTH);
        
        // Form panel (center) - using BoxLayout for flexible sizing
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        
        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(StyleUtils.FONT_BODY);
        usernameLabel.setForeground(StyleUtils.TEXT_DARK);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(8));
        
        usernameField = StyleUtils.createModernTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, StyleUtils.INPUT_HEIGHT));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(16));
        formPanel.add(Box.createVerticalStrut(16));
        
        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(StyleUtils.FONT_BODY);
        passwordLabel.setForeground(StyleUtils.TEXT_DARK);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(8));
        
        passwordField = StyleUtils.createModernPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, StyleUtils.INPUT_HEIGHT));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(16));
        
        // Confirm Password field (only visible in signup mode)
        confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(StyleUtils.FONT_BODY);
        confirmPasswordLabel.setForeground(StyleUtils.TEXT_DARK);
        confirmPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmPasswordLabel.setVisible(false);  // Hidden by default
        formPanel.add(confirmPasswordLabel);
        formPanel.add(Box.createVerticalStrut(8));
        
        confirmPasswordField = StyleUtils.createModernPasswordField();
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, StyleUtils.INPUT_HEIGHT));
        confirmPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmPasswordField.setVisible(false);  // Hidden by default
        formPanel.add(confirmPasswordField);
        formPanel.add(Box.createVerticalStrut(12));
        
        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(StyleUtils.FONT_BODY);
        errorLabel.setForeground(StyleUtils.ERROR_RED);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(errorLabel);
        
        card.add(formPanel, BorderLayout.CENTER);
        
        // Button panel (bottom)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        // Sign In button (primary action) - Modern styled
        loginButton = StyleUtils.createModernButton("Sign In", StyleUtils.PRIMARY_BLUE);
        loginButton.addActionListener(e -> handleLogin());
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, StyleUtils.BUTTON_HEIGHT));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        
        // Sign Up button (shown in signup mode, hidden initially)
        signupButton = StyleUtils.createModernButton("Create Account", StyleUtils.SUCCESS_GREEN);
        signupButton.addActionListener(e -> handleSignup());
        signupButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, StyleUtils.BUTTON_HEIGHT));
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.setVisible(false);
        buttonPanel.add(signupButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        
        // Continue as Guest button - subtle style
        guestButton = StyleUtils.createModernButton("Continue as Guest", StyleUtils.TEXT_MUTED);
        guestButton.addActionListener(e -> handleGuestLogin());
        guestButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, StyleUtils.BUTTON_HEIGHT));
        guestButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(guestButton);
        buttonPanel.add(Box.createVerticalStrut(16));
        
        // Switch mode link ("Don't have an account? Sign up")
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
        
        // Hover underline effect
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
        
        // Allow Enter key to submit
        passwordField.addActionListener(e -> {
            if (isSignupMode) {
                confirmPasswordField.requestFocus();
            } else {
                handleLogin();
            }
        });
        confirmPasswordField.addActionListener(e -> handleSignup());
        
        card.add(buttonPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    /**
     * Helper method to create a styled button.
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(StyleUtils.FONT_BUTTON);
        button.setBackground(bgColor);
        button.setForeground(StyleUtils.TEXT_LIGHT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
    
    /**
     * Toggle between Login and Signup modes.
     */
    private void toggleMode() {
        isSignupMode = !isSignupMode;
        clearError();
        clearFields();
        
        if (isSignupMode) {
            // Switch to Signup mode
            cardTitleLabel.setText("Create Account");
            confirmPasswordLabel.setVisible(true);
            confirmPasswordField.setVisible(true);
            loginButton.setVisible(false);
            signupButton.setVisible(true);
            guestButton.setVisible(false);
            switchModeButton.setText("Already have an account? Sign in");
        } else {
            // Switch to Login mode
            cardTitleLabel.setText("Welcome Back!");
            confirmPasswordLabel.setVisible(false);
            confirmPasswordField.setVisible(false);
            loginButton.setVisible(true);
            signupButton.setVisible(false);
            guestButton.setVisible(true);
            switchModeButton.setText("Don't have an account? Sign up");
        }
        
        // Refresh the layout
        revalidate();
        repaint();
    }

    // ==================== EVENT HANDLERS ====================
    
    /**
     * Handle the login button click.
     */
    private void handleLogin() {
        // Get input values
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }
        
        // Attempt authentication
        User user = dataManager.authenticate(username, password);
        
        if (user != null) {
            // Success! Clear error and switch to dashboard
            clearError();
            clearFields();
            parentApp.onLoginSuccess(user);
        } else {
            // Failed - show error
            showError("Invalid username or password.");
            passwordField.setText("");
        }
    }
    
    /**
     * Handle the signup button click.
     * Creates a new student account.
     */
    private void handleSignup() {
        // Get input values
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Validate input
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }
        
        // Check username length (reasonable limits)
        if (username.length() < 3) {
            showError("Username must be at least 3 characters.");
            return;
        }
        
        if (username.length() > 20) {
            showError("Username must be 20 characters or less.");
            return;
        }
        
        // Check password length
        if (password.length() < 4) {
            showError("Password must be at least 4 characters.");
            return;
        }
        
        // Check passwords match
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            confirmPasswordField.setText("");
            return;
        }
        
        // Check if username is taken
        if (dataManager.isUsernameTaken(username)) {
            showError("Username is already taken.");
            return;
        }
        
        // Attempt registration
        Student newUser = dataManager.registerUser(username, password);
        
        if (newUser != null) {
            // Success! Show message and log them in
            clearError();
            clearFields();
            
            JOptionPane.showMessageDialog(
                this,
                "Account created successfully!\nWelcome, " + username + "!",
                "Registration Complete",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Switch back to login mode for cleaner UX
            toggleMode();
            
            // Automatically log them in
            parentApp.onLoginSuccess(newUser);
        } else {
            showError("Registration failed. Please try again.");
        }
    }
    
    /**
     * Handle the "Continue as Guest" button click.
     * Creates a temporary guest user who can browse but not save progress.
     */
    private void handleGuestLogin() {
        // Create guest user
        Student guestUser = dataManager.createGuestUser();
        
        // Clear fields and show info message
        clearFields();
        clearError();
        
        // Notify user about guest limitations
        JOptionPane.showMessageDialog(
            this,
            "You're browsing as a guest.\n\n" +
            "• You can view all lessons\n" +
            "• Your progress will NOT be saved\n" +
            "• Create an account to track XP!",
            "Guest Mode",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        // Log in as guest
        parentApp.onLoginSuccess(guestUser);
    }
    
    /**
     * Display an error message.
     */
    private void showError(String message) {
        errorLabel.setText(message);
    }
    
    /**
     * Clear the error message.
     */
    private void clearError() {
        errorLabel.setText(" ");
    }
    
    /**
     * Clear input fields.
     */
    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
}
