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

    /** Primary Blue Light - Lighter shade for hover effects */
    public static final Color PRIMARY_BLUE_LIGHT = Color.decode("#E8F1FF");

    /** Background Gray - Used for dashboard/main background */
    public static final Color BACKGROUND_GRAY = Color.decode("#F0F2F5");

    /** Card White - Used for content containers/cards */
    public static final Color CARD_WHITE = Color.decode("#FFFFFF");

    /** Editor Background - Dark mode for code areas */
    public static final Color EDITOR_DARK = Color.decode("#2D2D2D");

    /** Success Green - Used for submit buttons, progress bars, completed items */
    public static final Color SUCCESS_GREEN = Color.decode("#28A745");

    /** Success Green Light - Light background for success states */
    public static final Color SUCCESS_GREEN_LIGHT = Color.decode("#D4EDDA");

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

    /** Border Light Blue - Light blue border for cards */
    public static final Color BORDER_LIGHT_BLUE = Color.decode("#E9ECEF");

    /** Shadow Color - For card shadows (with alpha) */
    public static final Color SHADOW_COLOR = new Color(0, 0, 0, 20);

    /** Hover Blue - For hover states on interactive elements */
    public static final Color HOVER_BLUE = Color.decode("#007BFF");

    /** Secondary Gray - For less important UI elements */
    public static final Color SECONDARY_GRAY = Color.decode("#6C757D");

    /** Light Blue Background - For highlighting */
    public static final Color LIGHT_BLUE_BG = Color.decode("#E7F1FF");

    /** Light Green Background - For success states */
    public static final Color LIGHT_GREEN_BG = Color.decode("#E8F5E8");

    // ==================== FONTS ====================

    /** Header font - Large titles */
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 28);

    /** Subheader font - Section titles */
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 20);

    /** Body font - Regular text */
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 16);

    /** Body Bold font - Regular bold text */
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 16);

    /** Code font - For code editor areas (monospaced) */
    public static final Font FONT_CODE = new Font("Consolas", Font.PLAIN, 14);

    /** Button font */
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);

    /** Small font - For labels and hints */
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 14);

    /** Extra Small font - For very small text */
    public static final Font FONT_XSMALL = new Font("Segoe UI", Font.PLAIN, 12);

    /** Large number font - For statistics */
    public static final Font FONT_STAT_NUMBER = new Font("Segoe UI", Font.BOLD, 36);

    /** Table header font */
    public static final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 13);

    // ==================== DIMENSIONS ====================

    /** Standard padding for components */
    public static final int PADDING = 20;

    /** Standard border radius (for reference, Swing doesn't use CSS) */
    public static final int BORDER_RADIUS = 12;

    /** Card border radius */
    public static final int CARD_BORDER_RADIUS = 10;

    /** Small padding */
    public static final int PADDING_SMALL = 10;

    /** Large padding */
    public static final int PADDING_LARGE = 30;

    // ==================== SHADOWS ====================

    /** Card shadow offset */
    public static final int SHADOW_OFFSET = 2;

    /** Card shadow blur */
    public static final int SHADOW_BLUR = 6;

    // ==================== PRIVATE CONSTRUCTOR ====================

    /**
     * Private constructor prevents instantiation.
     * This class should only be used for its static constants.
     */
    private StyleUtils() {
        // This class cannot be instantiated
    }
}
