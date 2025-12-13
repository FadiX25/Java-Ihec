package utils;

import java.awt.Color;
import java.awt.Font;

/**
 * StyleUtils - Contains all design constants for the application.
 * 
 * WHY THIS CLASS?
 * Instead of writing Color.decode("#0056D2") everywhere in our code,
 * we define it once here. If we want to change the theme later,
 * we only change it in ONE place!
 * 
 * All fields are 'static final' because:
 * - static: We don't need to create an object to use them
 * - final: These values should never change (they're constants)
 */
public class StyleUtils {

    // ==================== COLOR PALETTE ====================
    
    /** Primary Blue - Used for headers, primary buttons, navigation */
    public static final Color PRIMARY_BLUE = Color.decode("#0056D2");
    
    /** Background Gray - Used for dashboard/main background */
    public static final Color BACKGROUND_GRAY = Color.decode("#F0F2F5");
    
    /** Card White - Used for content containers/cards */
    public static final Color CARD_WHITE = Color.decode("#FFFFFF");
    
    /** Editor Background - Dark mode for code areas */
    public static final Color EDITOR_DARK = Color.decode("#2D2D2D");
    
    /** Success Green - Used for submit buttons, progress bars */
    public static final Color SUCCESS_GREEN = Color.decode("#28A745");
    
    /** Error Red - Used for error messages */
    public static final Color ERROR_RED = Color.decode("#DC3545");
    
    /** Text Dark - Primary text color */
    public static final Color TEXT_DARK = Color.decode("#212529");
    
    /** Text Light - Text on dark backgrounds */
    public static final Color TEXT_LIGHT = Color.decode("#FFFFFF");
    
    /** Text Muted - Secondary/hint text */
    public static final Color TEXT_MUTED = Color.decode("#6C757D");
    
    // ==================== ADMIN THEME COLORS ====================
    
    /** Admin Accent - Orange/Amber for admin-specific elements */
    public static final Color ADMIN_ACCENT = Color.decode("#F59E0B");
    
    /** Admin Accent Light - Lighter shade for hover/backgrounds */
    public static final Color ADMIN_ACCENT_LIGHT = Color.decode("#FEF3C7");
    
    /** Warning Yellow - For caution messages */
    public static final Color WARNING_YELLOW = Color.decode("#FFC107");
    
    /** Info Blue - For informational elements */
    public static final Color INFO_BLUE = Color.decode("#17A2B8");
    
    /** Table Row Alternate - Subtle gray for alternating rows */
    public static final Color TABLE_ROW_ALT = Color.decode("#F8F9FA");
    
    /** Border Light - Light border color */
    public static final Color BORDER_LIGHT = Color.decode("#DEE2E6");
    
    /** Shadow Color - For card shadows (with alpha) */
    public static final Color SHADOW_COLOR = new Color(0, 0, 0, 20);

    // ==================== FONTS ====================
    
    /** Header font - Large titles */
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 24);
    
    /** Subheader font - Section titles */
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 18);
    
    /** Body font - Regular text */
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    
    /** Code font - For code editor areas (monospaced) */
    public static final Font FONT_CODE = new Font("Consolas", Font.PLAIN, 14);
    
    /** Button font */
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    
    /** Small font - For labels and hints */
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    
    /** Large number font - For statistics */
    public static final Font FONT_STAT_NUMBER = new Font("Segoe UI", Font.BOLD, 32);
    
    /** Table header font */
    public static final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 13);

    // ==================== DIMENSIONS ====================
    
    /** Standard padding for components */
    public static final int PADDING = 20;
    
    /** Standard border radius (for reference, Swing doesn't use CSS) */
    public static final int BORDER_RADIUS = 8;

    // ==================== PRIVATE CONSTRUCTOR ====================
    
    /**
     * Private constructor prevents instantiation.
     * This class should only be used for its static constants.
     */
    private StyleUtils() {
        // This class cannot be instantiated
    }
}
