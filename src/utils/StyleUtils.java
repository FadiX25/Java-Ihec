package utils;

import java.awt.*;
import javax.swing.border.Border;

/**
 * StyleUtils - Modern IHEC LearnHub color palette and styling.
 * Inspired by modern learning platforms with clean, professional design.
 */
public class StyleUtils {

    // ==================== PRIMARY BRAND COLORS ====================

    /** Primary gradient start - Vibrant blue */
    public static final Color PRIMARY_BLUE = new Color(59, 130, 246);  // #3b82f6

    /** Primary gradient end - Purple */
    public static final Color PRIMARY_PURPLE = new Color(139, 92, 246);  // #8b5cf6

    /** Accent blue for links and highlights */
    public static final Color ACCENT_BLUE = new Color(37, 99, 235);  // #2563eb

    // ==================== BACKGROUND COLORS ====================

    /** Main background - Soft gray */
    public static final Color BACKGROUND_GRAY = new Color(245, 247, 250);  // #f5f7fa

    /** Card background - Pure white */
    public static final Color CARD_WHITE = new Color(255, 255, 255);

    /** Hover background */
    public static final Color CARD_HOVER = new Color(250, 251, 252);  // #fafbfc

    /** Border color */
    public static final Color BORDER_LIGHT = new Color(226, 232, 240);  // #e2e8f0

    // ==================== TEXT COLORS ====================

    /** Primary text - Rich dark */
    public static final Color TEXT_DARK = new Color(30, 41, 59);  // #1e293b

    /** Secondary text - Medium gray */
    public static final Color TEXT_MUTED = new Color(100, 116, 139);  // #64748b

    /** Light text for dark backgrounds */
    public static final Color TEXT_LIGHT = new Color(255, 255, 255);

    // ==================== STATUS COLORS ====================

    /** Success green */
    public static final Color SUCCESS_GREEN = new Color(16, 185, 129);  // #10b981

    /** Success dark (hover) */
    public static final Color SUCCESS_DARK = new Color(5, 150, 105);  // #059669

    /** Error red */
    public static final Color ERROR_RED = new Color(239, 68, 68);  // #ef4444

    /** Warning orange */
    public static final Color WARNING_ORANGE = new Color(245, 158, 11);  // #f59e0b

    /** Info blue */
    public static final Color INFO_BLUE = new Color(6, 182, 212);  // #06b6d4

    // ==================== STAT CARD COLORS ====================

    /** Blue stat background */
    public static final Color STAT_BLUE_BG = new Color(219, 234, 254);  // #dbeafe
    public static final Color STAT_BLUE_TEXT = new Color(59, 130, 246);  // #3b82f6

    /** Yellow stat background */
    public static final Color STAT_YELLOW_BG = new Color(254, 243, 199);  // #fef3c7
    public static final Color STAT_YELLOW_TEXT = new Color(245, 158, 11);  // #f59e0b

    /** Green stat background */
    public static final Color STAT_GREEN_BG = new Color(209, 250, 229);  // #d1fae5
    public static final Color STAT_GREEN_TEXT = new Color(16, 185, 129);  // #10b981

    /** Orange stat background */
    public static final Color STAT_ORANGE_BG = new Color(254, 215, 170);  // #fed7aa
    public static final Color STAT_ORANGE_TEXT = new Color(249, 115, 22);  // #f97316

    // ==================== CODE EDITOR COLORS ====================

    /** Editor background - Dark comfortable */
    public static final Color EDITOR_DARK = new Color(40, 44, 52);  // #282c34

    /** Code syntax color */
    public static final Color CODE_GREEN = new Color(152, 195, 121);  // #98c379

    // ==================== PROGRESS BAR ====================

    /** Progress bar background */
    public static final Color PROGRESS_BG = new Color(226, 232, 240);  // #e2e8f0

    // ==================== FONTS ====================

    /** Large title (28pt bold) */
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 28);

    /** Section headers (20pt bold) */
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 20);

    /** Card titles (16pt bold) */
    public static final Font FONT_CARD_TITLE = new Font("Segoe UI", Font.BOLD, 16);

    /** Body text (14pt) */
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);

    /** Button text (15pt bold) */
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 15);

    /** Code editor (14pt monospace) */
    public static final Font FONT_CODE = new Font("Consolas", Font.PLAIN, 14);

    /** Small text (12pt) */
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    /** Stat numbers (32pt bold) */
    public static final Font FONT_STAT_NUMBER = new Font("Segoe UI", Font.BOLD, 32);

    // ==================== SPACING ====================

    public static final int SPACING_XS = 5;
    public static final int SPACING_SM = 10;
    public static final int SPACING_MD = 15;
    public static final int SPACING_LG = 20;
    public static final int SPACING_XL = 30;

    // ==================== BORDER RADIUS ====================

    public static final int RADIUS_SM = 8;
    public static final int RADIUS_MD = 12;
    public static final int RADIUS_LG = 16;
    public static final int RADIUS_XL = 20;

    // ==================== UTILITY METHODS ====================

    /**
     * Add alpha transparency to a color.
     */
    public static Color withAlpha(Color color, int alpha) {
        return new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                Math.max(0, Math.min(255, alpha))
        );
    }

    /**
     * Darken a color.
     */
    public static Color darken(Color color, float factor) {
        return new Color(
                Math.max((int)(color.getRed() * factor), 0),
                Math.max((int)(color.getGreen() * factor), 0),
                Math.max((int)(color.getBlue() * factor), 0),
                color.getAlpha()
        );
    }

    /**
     * Lighten a color.
     */
    public static Color lighten(Color color, float factor) {
        return new Color(
                Math.min((int)(color.getRed() + (255 - color.getRed()) * factor), 255),
                Math.min((int)(color.getGreen() + (255 - color.getGreen()) * factor), 255),
                Math.min((int)(color.getBlue() + (255 - color.getBlue()) * factor), 255),
                color.getAlpha()
        );
    }
}
