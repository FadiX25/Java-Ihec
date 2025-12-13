package view;

import controller.CsvDataManager;
import model.User;
import utils.StyleUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginView - The login screen of the application.
 * 
 * SWING BASICS:
 * - JPanel: A container that holds other components
 * - JLabel: Displays text
 * - JTextField: Input field for text
 * - JPasswordField: Input field that hides characters
 * - JButton: A clickable button
 * 
 * LAYOUT USED: 
 * - BorderLayout for overall structure
 * - GridBagLayout for the centered form
 */
public class LoginView extends JPanel {

    // ==================== UI COMPONENTS ====================
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorLabel;
    
    // Reference to parent application (for switching views)
    private MainApplication parentApp;
    
    // Data manager for authentication
    private CsvDataManager dataManager;

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
        JPanel header = new JPanel();
        header.setBackground(StyleUtils.PRIMARY_BLUE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 25));
        
        JLabel title = new JLabel("IHEC-JLearn");
        title.setFont(StyleUtils.FONT_HEADER);
        title.setForeground(StyleUtils.TEXT_LIGHT);
        
        header.add(title);
        return header;
    }
    
    /**
     * Create the white login card with form fields.
     */
    private JPanel createLoginCard() {
        // Main card panel
        JPanel card = new JPanel();
        card.setBackground(StyleUtils.CARD_WHITE);
        card.setPreferredSize(new Dimension(400, 350));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        // Card title
        JLabel cardTitle = new JLabel("Welcome Back!", SwingConstants.CENTER);
        cardTitle.setFont(StyleUtils.FONT_SUBHEADER);
        cardTitle.setForeground(StyleUtils.TEXT_DARK);
        cardTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        card.add(cardTitle, BorderLayout.NORTH);
        
        // Form panel (center)
        JPanel formPanel = new JPanel();
        formPanel.setBackground(StyleUtils.CARD_WHITE);
        formPanel.setLayout(new GridLayout(5, 1, 10, 15));
        
        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(StyleUtils.FONT_BODY);
        usernameLabel.setForeground(StyleUtils.TEXT_DARK);
        formPanel.add(usernameLabel);
        
        usernameField = new JTextField();
        usernameField.setFont(StyleUtils.FONT_BODY);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleUtils.BACKGROUND_GRAY, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        formPanel.add(usernameField);
        
        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(StyleUtils.FONT_BODY);
        passwordLabel.setForeground(StyleUtils.TEXT_DARK);
        formPanel.add(passwordLabel);
        
        passwordField = new JPasswordField();
        passwordField.setFont(StyleUtils.FONT_BODY);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleUtils.BACKGROUND_GRAY, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        formPanel.add(passwordField);
        
        // Error label (hidden by default)
        errorLabel = new JLabel(" ", SwingConstants.CENTER);
        errorLabel.setFont(StyleUtils.FONT_BODY);
        errorLabel.setForeground(StyleUtils.ERROR_RED);
        formPanel.add(errorLabel);
        
        card.add(formPanel, BorderLayout.CENTER);
        
        // Login button (bottom)
        loginButton = new JButton("Sign In");
        loginButton.setFont(StyleUtils.FONT_BUTTON);
        loginButton.setBackground(StyleUtils.PRIMARY_BLUE);
        loginButton.setForeground(StyleUtils.TEXT_LIGHT);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(0, 45));
        
        // Add click listener to button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        // Also allow Enter key to submit
        passwordField.addActionListener(e -> handleLogin());
        
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(StyleUtils.CARD_WHITE);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        buttonPanel.add(loginButton, BorderLayout.CENTER);
        
        card.add(buttonPanel, BorderLayout.SOUTH);
        
        return card;
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
    }
}
