package utils;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 * StyleUtils - Modern UI Design System for IHEC-JLearn
 * 
 * DESIGN PHILOSOPHY:
 * - Clean, minimalist aesthetic with purposeful use of color
 * - Smooth animations and micro-interactions
 * - Consistent spacing and visual hierarchy
 * - Accessibility-friendly contrast ratios
 */
public class StyleUtils {

    // ==================== MODERN COLOR PALETTE ====================
    
    /** Primary Blue - Vibrant, modern blue for primary actions */
    public static final Color PRIMARY_BLUE = Color.decode("#4F46E5");
    
    /** Primary Blue Hover - Slightly darker for hover states */
    public static final Color PRIMARY_BLUE_HOVER = Color.decode("#4338CA");
    
    /** Primary Blue Light - For subtle backgrounds */
    public static final Color PRIMARY_BLUE_LIGHT = Color.decode("#EEF2FF");
    
    /** Background Gray - Soft, warm gray for backgrounds */
    public static final Color BACKGROUND_GRAY = Color.decode("#F8FAFC");
    
    /** Card White - Pure white for cards with subtle warmth */
    public static final Color CARD_WHITE = Color.decode("#FFFFFF");
    
    /** Editor Background - Modern dark theme for code */
    public static final Color EDITOR_DARK = Color.decode("#1E1E2E");
    
    /** Editor Dark Lighter - For line numbers, gutters */
    public static final Color EDITOR_DARK_LIGHT = Color.decode("#313244");
    
    /** Success Green - Fresh, vibrant green */
    public static final Color SUCCESS_GREEN = Color.decode("#10B981");
    
    /** Success Green Hover */
    public static final Color SUCCESS_GREEN_HOVER = Color.decode("#059669");
    
    /** Success Green Light - For success backgrounds */
    public static final Color SUCCESS_GREEN_LIGHT = Color.decode("#D1FAE5");
    
    /** Error Red - Modern, accessible red */
    public static final Color ERROR_RED = Color.decode("#EF4444");
    
    /** Error Red Light - For error backgrounds */
    public static final Color ERROR_RED_LIGHT = Color.decode("#FEE2E2");
    
    /** Text Dark - Rich, readable dark text */
    public static final Color TEXT_DARK = Color.decode("#1E293B");
    
    /** Text Light - Pure white for dark backgrounds */
    public static final Color TEXT_LIGHT = Color.decode("#FFFFFF");
    
    /** Text Muted - Subtle gray for secondary text */
    public static final Color TEXT_MUTED = Color.decode("#64748B");
    
    /** Text Placeholder - Even lighter for placeholders */
    public static final Color TEXT_PLACEHOLDER = Color.decode("#94A3B8");
    
    // ==================== ADMIN THEME COLORS ====================
    
    /** Admin Accent - Modern amber/orange */
    public static final Color ADMIN_ACCENT = Color.decode("#F59E0B");
    
    /** Admin Accent Light - Subtle amber background */
    public static final Color ADMIN_ACCENT_LIGHT = Color.decode("#FEF3C7");
    
    /** Warning Yellow - Bright attention yellow */
    public static final Color WARNING_YELLOW = Color.decode("#FBBF24");
    
    /** Info Blue - Informational cyan-blue */
    public static final Color INFO_BLUE = Color.decode("#0EA5E9");
    
    /** Table Row Alternate - Very subtle stripe */
    public static final Color TABLE_ROW_ALT = Color.decode("#F8FAFC");
    
    /** Border Light - Subtle, modern border */
    public static final Color BORDER_LIGHT = Color.decode("#E2E8F0");
    
    /** Border Focus - Focus ring color */
    public static final Color BORDER_FOCUS = Color.decode("#818CF8");
    
    /** Shadow Color - Soft shadow with transparency */
    public static final Color SHADOW_COLOR = new Color(15, 23, 42, 25);
    
    // ==================== GRADIENT COLORS ====================
    
    /** Gradient Start - For header gradients */
    public static final Color GRADIENT_START = Color.decode("#4F46E5");
    
    /** Gradient End - For header gradients */
    public static final Color GRADIENT_END = Color.decode("#7C3AED");
    
    // ==================== MODERN FONTS ====================
    
    /** Header font - Bold, impactful titles */
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 28);
    
    /** Subheader font - Section titles */
    public static final Font FONT_SUBHEADER = new Font("Segoe UI Semibold", Font.PLAIN, 20);
    
    /** Body font - Comfortable reading size */
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 15);
    
    /** Code font - Modern monospace */
    public static final Font FONT_CODE = new Font("JetBrains Mono", Font.PLAIN, 14);
    
    /** Code font fallback */
    public static final Font FONT_CODE_FALLBACK = new Font("Consolas", Font.PLAIN, 14);
    
    /** Button font - Clear, readable */
    public static final Font FONT_BUTTON = new Font("Segoe UI Semibold", Font.PLAIN, 14);
    
    /** Small font - Captions and hints */
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    
    /** Large number font - Statistics display */
    public static final Font FONT_STAT_NUMBER = new Font("Segoe UI", Font.BOLD, 36);
    
    /** Table header font */
    public static final Font FONT_TABLE_HEADER = new Font("Segoe UI Semibold", Font.PLAIN, 13);
    
    /** Title font - App branding */
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);

    // ==================== DIMENSIONS ====================
    
    /** Standard padding for components */
    public static final int PADDING = 24;
    
    /** Small padding */
    public static final int PADDING_SMALL = 12;
    
    /** Large padding */
    public static final int PADDING_LARGE = 32;
    
    /** Standard border radius */
    public static final int BORDER_RADIUS = 12;
    
    /** Small border radius */
    public static final int BORDER_RADIUS_SMALL = 8;
    
    /** Large border radius */
    public static final int BORDER_RADIUS_LARGE = 16;
    
    /** Button height */
    public static final int BUTTON_HEIGHT = 48;
    
    /** Input field height */
    public static final int INPUT_HEIGHT = 48;
    
    /** Animation duration in ms */
    public static final int ANIMATION_DURATION = 150;

    // ==================== COMPONENT FACTORY METHODS ====================
    
    /**
     * Create a modern styled button with hover effects.
     */
    public static JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;
            private float alpha = 1.0f;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Calculate hover color
                Color bg = isHovered ? darkenColor(bgColor, 0.1f) : bgColor;
                
                // Draw rounded background
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 
                        BORDER_RADIUS_SMALL, BORDER_RADIUS_SMALL));
                
                // Draw text
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                
                g2.dispose();
            }
            
            @Override
            public void setEnabled(boolean enabled) {
                super.setEnabled(enabled);
                alpha = enabled ? 1.0f : 0.5f;
            }
        };
        
        button.setFont(FONT_BUTTON);
        button.setForeground(TEXT_LIGHT);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, BUTTON_HEIGHT));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.putClientProperty("hovered", true);
                button.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.putClientProperty("hovered", false);
                button.repaint();
            }
        });
        
        return button;
    }
    
    /**
     * Create a modern text field with focus effects.
     */
    public static JTextField createModernTextField() {
        JTextField field = new JTextField() {
            private boolean focused = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 
                        BORDER_RADIUS_SMALL, BORDER_RADIUS_SMALL));
                
                g2.dispose();
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (focused) {
                    g2.setColor(BORDER_FOCUS);
                    g2.setStroke(new BasicStroke(2));
                } else {
                    g2.setColor(BORDER_LIGHT);
                    g2.setStroke(new BasicStroke(1));
                }
                
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 
                        BORDER_RADIUS_SMALL, BORDER_RADIUS_SMALL));
                g2.dispose();
            }
        };
        
        field.setFont(FONT_BODY);
        field.setBackground(CARD_WHITE);
        field.setForeground(TEXT_DARK);
        field.setCaretColor(PRIMARY_BLUE);
        field.setBorder(new EmptyBorder(12, 16, 12, 16));
        field.setOpaque(false);
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.putClientProperty("focused", true);
                field.repaint();
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.putClientProperty("focused", false);
                field.repaint();
            }
        });
        
        return field;
    }
    
    /**
     * Create a modern password field with focus effects.
     */
    public static JPasswordField createModernPasswordField() {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 
                        BORDER_RADIUS_SMALL, BORDER_RADIUS_SMALL));
                
                g2.dispose();
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                boolean focused = isFocusOwner();
                if (focused) {
                    g2.setColor(BORDER_FOCUS);
                    g2.setStroke(new BasicStroke(2));
                } else {
                    g2.setColor(BORDER_LIGHT);
                    g2.setStroke(new BasicStroke(1));
                }
                
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 
                        BORDER_RADIUS_SMALL, BORDER_RADIUS_SMALL));
                g2.dispose();
            }
        };
        
        field.setFont(FONT_BODY);
        field.setBackground(CARD_WHITE);
        field.setForeground(TEXT_DARK);
        field.setCaretColor(PRIMARY_BLUE);
        field.setBorder(new EmptyBorder(12, 16, 12, 16));
        field.setOpaque(false);
        
        return field;
    }
    
    /**
     * Create a card panel with shadow effect.
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2.setColor(SHADOW_COLOR);
                g2.fill(new RoundRectangle2D.Float(3, 3, getWidth() - 4, getHeight() - 4, 
                        BORDER_RADIUS, BORDER_RADIUS));
                
                // Draw card background
                g2.setColor(CARD_WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 4, getHeight() - 4, 
                        BORDER_RADIUS, BORDER_RADIUS));
                
                g2.dispose();
            }
        };
        
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING + 4, PADDING + 4));
        
        return panel;
    }
    
    /**
     * Create a gradient header panel.
     */
    public static JPanel createGradientHeader(int height) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient
                GradientPaint gradient = new GradientPaint(
                    0, 0, GRADIENT_START,
                    getWidth(), 0, GRADIENT_END
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.dispose();
            }
        };
        
        panel.setPreferredSize(new Dimension(0, height));
        panel.setLayout(new BorderLayout());
        
        return panel;
    }
    
    /**
     * Create a modern progress bar.
     */
    public static JProgressBar createModernProgressBar() {
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setUI(new BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = c.getWidth();
                int height = c.getHeight();
                int arc = height;
                
                // Background track
                g2.setColor(BORDER_LIGHT);
                g2.fill(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));
                
                // Progress fill
                int fillWidth = (int) (width * ((double) progressBar.getValue() / progressBar.getMaximum()));
                if (fillWidth > 0) {
                    g2.setColor(progressBar.getForeground());
                    g2.fill(new RoundRectangle2D.Float(0, 0, fillWidth, height, arc, arc));
                }
                
                // Text
                if (progressBar.isStringPainted()) {
                    g2.setColor(TEXT_DARK);
                    g2.setFont(FONT_SMALL);
                    String text = progressBar.getString();
                    FontMetrics fm = g2.getFontMetrics();
                    int x = (width - fm.stringWidth(text)) / 2;
                    int y = (height + fm.getAscent() - fm.getDescent()) / 2;
                    g2.drawString(text, x, y);
                }
                
                g2.dispose();
            }
        });
        
        progressBar.setBackground(BORDER_LIGHT);
        progressBar.setForeground(SUCCESS_GREEN);
        progressBar.setBorder(null);
        progressBar.setPreferredSize(new Dimension(0, 8));
        
        return progressBar;
    }
    
    /**
     * Apply hover effect to a panel (for course cards, etc.)
     */
    public static void addHoverEffect(JPanel panel, Color normalBg, Color hoverBg) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(hoverBg);
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_FOCUS, 2),
                    new EmptyBorder(18, 18, 18, 18)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(normalBg);
                panel.setBorder(new EmptyBorder(20, 20, 20, 20));
            }
        });
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Darken a color by a percentage.
     */
    public static Color darkenColor(Color color, float factor) {
        return new Color(
            Math.max((int)(color.getRed() * (1 - factor)), 0),
            Math.max((int)(color.getGreen() * (1 - factor)), 0),
            Math.max((int)(color.getBlue() * (1 - factor)), 0),
            color.getAlpha()
        );
    }
    
    /**
     * Lighten a color by a percentage.
     */
    public static Color lightenColor(Color color, float factor) {
        return new Color(
            Math.min((int)(color.getRed() + (255 - color.getRed()) * factor), 255),
            Math.min((int)(color.getGreen() + (255 - color.getGreen()) * factor), 255),
            Math.min((int)(color.getBlue() + (255 - color.getBlue()) * factor), 255),
            color.getAlpha()
        );
    }
    
    /**
     * Create a compound border with rounded corners appearance.
     */
    public static Border createRoundedBorder(Color color, int thickness) {
        return new CompoundBorder(
            new LineBorder(color, thickness, true),
            new EmptyBorder(PADDING_SMALL, PADDING_SMALL, PADDING_SMALL, PADDING_SMALL)
        );
    }
    
    /**
     * Set up anti-aliasing for the entire application.
     */
    public static void setupAntiAliasing() {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }
    
    // ==================== PRIVATE CONSTRUCTOR ====================
    
    private StyleUtils() {
        // This class cannot be instantiated
    }
}
