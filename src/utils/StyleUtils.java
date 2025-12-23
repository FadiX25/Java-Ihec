package utils;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 * StyleUtils - Modern UI Design System for IHEC E-Learn
 * 
 * DESIGN PHILOSOPHY:
 * - Clean, minimalist aesthetic with purposeful use of color
 * - Smooth animations and micro-interactions
 * - Glass morphism and depth effects
 * - Consistent spacing and visual hierarchy
 * - Accessibility-friendly contrast ratios
 * 
 * NEW FEATURES v2.0:
 * - Ripple effect animations
 * - Glass morphism panels
 * - Toast notifications
 * - Animated progress rings
 * - Skeleton loading states
 * - Enhanced micro-interactions
 */
public class StyleUtils {

    // ==================== APPLICATION BRANDING ====================
    
    /** Application Name */
    public static final String APP_NAME = "IHEC Skills";
    
    /** Application Tagline */
    public static final String APP_TAGLINE = "Learn. Grow. Succeed.";
    
    /** Application Logo Path */
    private static final String APP_LOGO_PATH = "images/Application Logo.png";
    
    /** College Logo Path */
    private static final String COLLEGE_LOGO_PATH = "images/College Logo.svg";
    
    /** Cached logo images */
    private static ImageIcon cachedAppLogo = null;
    private static ImageIcon cachedAppLogoSmall = null;
    private static ImageIcon cachedAppLogoMedium = null;

    // ==================== BLUE & WHITE COLOR PALETTE ====================
    
    /** Primary Blue - Clean, professional blue for primary actions */
    public static final Color PRIMARY_BLUE = Color.decode("#2563EB");
    
    /** Primary Blue Hover - Slightly darker for hover states */
    public static final Color PRIMARY_BLUE_HOVER = Color.decode("#1D4ED8");
    
    /** Primary Blue Dark - Deep blue for emphasis */
    public static final Color PRIMARY_BLUE_DARK = Color.decode("#1E40AF");
    
    /** Primary Blue Light - Soft blue tint for backgrounds */
    public static final Color PRIMARY_BLUE_LIGHT = Color.decode("#EFF6FF");
    
    /** Background White - Clean white base */
    public static final Color BACKGROUND_GRAY = Color.decode("#F8FAFC");
    
    /** Card White - Pure white for cards and panels */
    public static final Color CARD_WHITE = Color.decode("#FFFFFF");
    
    /** Editor Background - Modern dark theme for code */
    public static final Color EDITOR_DARK = Color.decode("#1E293B");
    
    /** Editor Dark Lighter - For line numbers, gutters */
    public static final Color EDITOR_DARK_LIGHT = Color.decode("#334155");
    
    /** Success Blue - Using blue tones for success states */
    public static final Color SUCCESS_GREEN = Color.decode("#0EA5E9");
    
    /** Success Blue Hover */
    public static final Color SUCCESS_GREEN_HOVER = Color.decode("#0284C7");
    
    /** Success Blue Light - Soft blue background for success */
    public static final Color SUCCESS_GREEN_LIGHT = Color.decode("#E0F2FE");
    
    /** Error Red - Clean red for errors (keeping for contrast) */
    public static final Color ERROR_RED = Color.decode("#DC2626");
    
    /** Error Red Light - Soft red background */
    public static final Color ERROR_RED_LIGHT = Color.decode("#FEE2E2");;
    
    /** Text Dark - Rich, readable dark blue-gray text */
    public static final Color TEXT_DARK = Color.decode("#1E3A5F");
    
    /** Text Light - Pure white for dark backgrounds */
    public static final Color TEXT_LIGHT = Color.decode("#FFFFFF");
    
    /** Text Muted - Subtle blue-gray for secondary text */
    public static final Color TEXT_MUTED = Color.decode("#64748B");
    
    /** Text Placeholder - Light blue-gray for placeholders */
    public static final Color TEXT_PLACEHOLDER = Color.decode("#94A3B8");
    
    // ==================== ADMIN THEME COLORS ====================
    
    /** Admin Accent - Blue accent for admin features */
    public static final Color ADMIN_ACCENT = Color.decode("#60A5FA");
    
    /** Admin Accent Light - Soft blue background for admin */
    public static final Color ADMIN_ACCENT_LIGHT = Color.decode("#DBEAFE");
    
    /** Warning Yellow - Amber for warnings (keeping for visibility) */
    public static final Color WARNING_YELLOW = Color.decode("#F59E0B");
    
    /** Info Blue - Lighter informational blue */
    public static final Color INFO_BLUE = Color.decode("#38BDF8");
    
    /** Table Row Alternate - Very subtle blue stripe */
    public static final Color TABLE_ROW_ALT = Color.decode("#F0F9FF");
    
    /** Border Light - Subtle blue-tinted border */
    public static final Color BORDER_LIGHT = Color.decode("#CBD5E1");
    
    /** Border Focus - Blue focus ring */
    public static final Color BORDER_FOCUS = Color.decode("#3B82F6");
    
    /** Shadow Color - Soft blue shadow */
    public static final Color SHADOW_COLOR = new Color(30, 58, 95, 20);
    
    // ==================== GRADIENT COLORS ====================
    
    /** Gradient Start - Blue gradient start */
    public static final Color GRADIENT_START = Color.decode("#2563EB");
    
    /** Gradient End - Blue gradient end */
    public static final Color GRADIENT_END = Color.decode("#1E40AF");
    
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
    
    /** Ripple animation duration */
    public static final int RIPPLE_DURATION = 400;
    
    /** Toast display duration */
    public static final int TOAST_DURATION = 3000;

    // ==================== RIPPLE EFFECT SYSTEM ====================
    
    /**
     * Data class to hold ripple animation state.
     */
    private static class RippleEffect {
        int x, y;
        long startTime;
        int maxRadius;
        
        RippleEffect(int x, int y, int maxRadius) {
            this.x = x;
            this.y = y;
            this.startTime = System.currentTimeMillis();
            this.maxRadius = maxRadius;
        }
        
        float getProgress() {
            return Math.min(1f, (System.currentTimeMillis() - startTime) / (float) RIPPLE_DURATION);
        }
        
        boolean isComplete() {
            return getProgress() >= 1f;
        }
    }
    
    /**
     * Add ripple effect to any component.
     */
    public static void addRippleEffect(JComponent component, Color rippleColor) {
        List<RippleEffect> ripples = new CopyOnWriteArrayList<>();
        
        Timer rippleTimer = new Timer(16, e -> {
            ripples.removeIf(RippleEffect::isComplete);
            if (ripples.isEmpty()) {
                ((Timer) e.getSource()).stop();
            }
            component.repaint();
        });
        
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int maxRadius = (int) Math.sqrt(component.getWidth() * component.getWidth() + 
                                                 component.getHeight() * component.getHeight());
                ripples.add(new RippleEffect(e.getX(), e.getY(), maxRadius));
                if (!rippleTimer.isRunning()) {
                    rippleTimer.start();
                }
            }
        });
        
        component.putClientProperty("ripples", ripples);
        component.putClientProperty("rippleColor", rippleColor);
    }
    
    /**
     * Paint ripple effects for a component.
     */
    public static void paintRipples(Graphics2D g2, JComponent component) {
        @SuppressWarnings("unchecked")
        List<RippleEffect> ripples = (List<RippleEffect>) component.getClientProperty("ripples");
        Color rippleColor = (Color) component.getClientProperty("rippleColor");
        
        if (ripples == null || rippleColor == null) return;
        
        for (RippleEffect ripple : ripples) {
            float progress = ripple.getProgress();
            float eased = 1f - (1f - progress) * (1f - progress); // Ease out
            int radius = (int) (ripple.maxRadius * eased);
            int alpha = (int) (80 * (1f - progress));
            
            g2.setColor(new Color(rippleColor.getRed(), rippleColor.getGreen(), 
                                   rippleColor.getBlue(), alpha));
            g2.fillOval(ripple.x - radius, ripple.y - radius, radius * 2, radius * 2);
        }
    }

    // ==================== COMPONENT FACTORY METHODS ====================
    
    /**
     * Create a modern styled button with hover effects and ripple animation.
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
                Color bg = isHovered ? darkenColor(bgColor, 0.08f) : bgColor;
                
                // Create clipping shape for ripple
                Shape clip = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 
                        BORDER_RADIUS_SMALL, BORDER_RADIUS_SMALL);
                g2.setClip(clip);
                
                // Draw rounded background with subtle gradient
                GradientPaint gradient = new GradientPaint(
                    0, 0, bg,
                    0, getHeight(), darkenColor(bg, 0.05f)
                );
                g2.setPaint(gradient);
                g2.fill(clip);
                
                // Draw ripples
                paintRipples(g2, this);
                
                // Draw subtle top highlight
                g2.setColor(new Color(255, 255, 255, isHovered ? 30 : 20));
                g2.fillRect(0, 0, getWidth(), getHeight() / 3);
                
                // Draw text with shadow for depth
                g2.setClip(null);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                
                // Text shadow
                g2.setColor(new Color(0, 0, 0, 30));
                g2.drawString(getText(), x + 1, y + 1);
                
                // Main text
                g2.setColor(getForeground());
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
        
        // Add ripple effect
        addRippleEffect(button, new Color(255, 255, 255, 100));
        
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
     * Create a ghost/outline button style.
     */
    public static JButton createGhostButton(String text, Color accentColor) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Shape shape = new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 
                        BORDER_RADIUS_SMALL, BORDER_RADIUS_SMALL);
                
                // Fill on hover
                if (isHovered) {
                    g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), 
                                          accentColor.getBlue(), 20));
                    g2.fill(shape);
                }
                
                // Draw border
                g2.setColor(accentColor);
                g2.setStroke(new BasicStroke(isHovered ? 2f : 1.5f));
                g2.draw(shape);
                
                // Draw text
                g2.setFont(getFont());
                g2.setColor(accentColor);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                
                g2.dispose();
            }
        };
        
        button.setFont(FONT_BUTTON);
        button.setForeground(accentColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, BUTTON_HEIGHT));
        
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
     * Fixed dimensions ensure consistent sizing across layouts.
     */
    public static JTextField createModernTextField() {
        JTextField field = new JTextField() {
            private boolean focused = false;
            
            {
                // Ensure consistent dimensions
                setPreferredSize(new Dimension(200, INPUT_HEIGHT));
                setMinimumSize(new Dimension(100, INPUT_HEIGHT));
            }
            
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
                
                // Use isFocusOwner() for reliable focus state
                if (isFocusOwner()) {
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
            
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                return new Dimension(d.width, INPUT_HEIGHT);
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
                field.repaint();
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.repaint();
            }
        });
        
        return field;
    }
    
    /**
     * Create a modern password field with focus effects.
     * Fixed dimensions ensure consistent sizing across layouts.
     */
    public static JPasswordField createModernPasswordField() {
        JPasswordField field = new JPasswordField() {
            {
                // Ensure consistent dimensions
                setPreferredSize(new Dimension(200, INPUT_HEIGHT));
                setMinimumSize(new Dimension(100, INPUT_HEIGHT));
            }
            
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
            
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                return new Dimension(d.width, INPUT_HEIGHT);
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
    
    // ==================== GLASS MORPHISM COMPONENTS ====================
    
    /**
     * Create a glass morphism panel with blur effect simulation.
     */
    public static JPanel createGlassPanel(float opacity) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Glass background with transparency
                g2.setColor(new Color(255, 255, 255, (int)(255 * opacity)));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 
                        BORDER_RADIUS_LARGE, BORDER_RADIUS_LARGE));
                
                // Subtle gradient overlay for glass effect
                GradientPaint glassGradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 40),
                    0, getHeight(), new Color(255, 255, 255, 10)
                );
                g2.setPaint(glassGradient);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight() / 2, 
                        BORDER_RADIUS_LARGE, BORDER_RADIUS_LARGE));
                
                // Border glow
                g2.setColor(new Color(255, 255, 255, 60));
                g2.setStroke(new BasicStroke(1));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 
                        BORDER_RADIUS_LARGE, BORDER_RADIUS_LARGE));
                
                g2.dispose();
            }
        };
    }
    
    // ==================== TOAST NOTIFICATION SYSTEM ====================
    
    /**
     * Toast notification types
     */
    public enum ToastType {
        SUCCESS(SUCCESS_GREEN, "✓"),
        ERROR(ERROR_RED, "✕"),
        INFO(INFO_BLUE, "ℹ"),
        WARNING(WARNING_YELLOW, "⚠");
        
        final Color color;
        final String icon;
        
        ToastType(Color color, String icon) {
            this.color = color;
            this.icon = icon;
        }
    }
    
    /**
     * Show a toast notification at the top of the window.
     */
    public static void showToast(JFrame frame, String message, ToastType type) {
        JPanel toast = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0)) {
            private float opacity = 0f;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2.setColor(new Color(0, 0, 0, (int)(30 * opacity)));
                g2.fill(new RoundRectangle2D.Float(2, 2, getWidth() - 4, getHeight() - 4, 12, 12));
                
                // Draw background
                Color bgColor = new Color(
                    CARD_WHITE.getRed(), CARD_WHITE.getGreen(), CARD_WHITE.getBlue(),
                    (int)(255 * opacity)
                );
                g2.setColor(bgColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 4, getHeight() - 4, 12, 12));
                
                // Draw left accent
                g2.setColor(new Color(type.color.getRed(), type.color.getGreen(), 
                                      type.color.getBlue(), (int)(255 * opacity)));
                g2.fillRoundRect(0, 0, 4, getHeight() - 4, 4, 4);
                
                g2.dispose();
            }
            
            public void setOpacity(float opacity) {
                this.opacity = opacity;
                repaint();
            }
        };
        
        toast.setOpaque(false);
        toast.setBorder(new EmptyBorder(12, 16, 12, 24));
        
        // Icon
        JLabel iconLabel = new JLabel(type.icon);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        iconLabel.setForeground(type.color);
        toast.add(iconLabel);
        
        // Message
        JLabel msgLabel = new JLabel(message);
        msgLabel.setFont(FONT_BODY);
        msgLabel.setForeground(TEXT_DARK);
        toast.add(msgLabel);
        
        // Close button
        JLabel closeBtn = new JLabel("×");
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        closeBtn.setForeground(TEXT_MUTED);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toast.add(Box.createHorizontalStrut(20));
        toast.add(closeBtn);
        
        // Position toast
        JLayeredPane layeredPane = frame.getLayeredPane();
        toast.setSize(toast.getPreferredSize());
        int x = (frame.getWidth() - toast.getWidth()) / 2;
        toast.setLocation(x, -toast.getHeight());
        layeredPane.add(toast, JLayeredPane.POPUP_LAYER);
        
        // Animate in
        Timer slideIn = new Timer(16, null);
        final int targetY = 20;
        slideIn.addActionListener(e -> {
            int y = toast.getY();
            if (y < targetY) {
                int newY = Math.min(targetY, y + 8);
                toast.setLocation(toast.getX(), newY);
                float progress = (float)(newY + toast.getHeight()) / (targetY + toast.getHeight());
                ((JPanel)toast).putClientProperty("opacity", progress);
                toast.repaint();
            } else {
                slideIn.stop();
            }
        });
        slideIn.start();
        
        // Auto dismiss
        Timer dismiss = new Timer(TOAST_DURATION, e -> {
            Timer slideOut = new Timer(16, null);
            slideOut.addActionListener(ev -> {
                int y = toast.getY();
                if (y > -toast.getHeight()) {
                    toast.setLocation(toast.getX(), y - 8);
                    toast.repaint();
                } else {
                    slideOut.stop();
                    layeredPane.remove(toast);
                    layeredPane.repaint();
                }
            });
            slideOut.start();
        });
        dismiss.setRepeats(false);
        dismiss.start();
        
        // Close button action
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dismiss.stop();
                layeredPane.remove(toast);
                layeredPane.repaint();
            }
        });
    }
    
    // ==================== ANIMATED PROGRESS RING ====================
    
    /**
     * Create an animated circular progress indicator.
     */
    public static JPanel createProgressRing(int size, int percentage, Color color) {
        return new JPanel() {
            private int currentValue = 0;
            private Timer animator;
            
            {
                setPreferredSize(new Dimension(size, size));
                setOpaque(false);
                
                // Animate to target percentage
                animator = new Timer(20, e -> {
                    if (currentValue < percentage) {
                        currentValue = Math.min(currentValue + 2, percentage);
                        repaint();
                    } else {
                        animator.stop();
                    }
                });
                animator.start();
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int stroke = size / 8;
                int diameter = size - stroke - 4;
                int x = (getWidth() - diameter) / 2;
                int y = (getHeight() - diameter) / 2;
                
                // Background ring
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
                g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawOval(x, y, diameter, diameter);
                
                // Progress arc
                g2.setColor(color);
                int angle = (int) (360 * (currentValue / 100.0));
                g2.drawArc(x, y, diameter, diameter, 90, -angle);
                
                // Percentage text
                g2.setFont(new Font("Segoe UI", Font.BOLD, size / 4));
                g2.setColor(TEXT_DARK);
                String text = currentValue + "%";
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(text)) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(text, textX, textY);
                
                g2.dispose();
            }
        };
    }
    
    // ==================== SKELETON LOADING COMPONENTS ====================
    
    /**
     * Create a skeleton loading placeholder.
     */
    public static JPanel createSkeletonLoader(int width, int height, int borderRadius) {
        return new JPanel() {
            private float shimmerPosition = -1f;
            private Timer shimmerTimer;
            
            {
                setPreferredSize(new Dimension(width, height));
                setOpaque(false);
                
                shimmerTimer = new Timer(30, e -> {
                    shimmerPosition += 0.05f;
                    if (shimmerPosition > 2f) shimmerPosition = -1f;
                    repaint();
                });
                shimmerTimer.start();
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Base skeleton color
                g2.setColor(new Color(226, 232, 240));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 
                        borderRadius, borderRadius));
                
                // Shimmer effect
                int shimmerWidth = getWidth() / 3;
                int shimmerX = (int) (shimmerPosition * getWidth());
                
                GradientPaint shimmer = new GradientPaint(
                    shimmerX, 0, new Color(255, 255, 255, 0),
                    shimmerX + shimmerWidth / 2, 0, new Color(255, 255, 255, 80),
                    true
                );
                g2.setPaint(shimmer);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 
                        borderRadius, borderRadius));
                
                g2.dispose();
            }
            
            @Override
            public void removeNotify() {
                super.removeNotify();
                if (shimmerTimer != null) shimmerTimer.stop();
            }
        };
    }
    
    /**
     * Create a skeleton card for loading states.
     */
    public static JPanel createSkeletonCard(int width, int height) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(width, height));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header skeleton
        card.add(createSkeletonLoader(width - 40, 20, 4));
        card.add(Box.createVerticalStrut(12));
        
        // Content skeletons
        card.add(createSkeletonLoader((int)((width - 40) * 0.8), 16, 4));
        card.add(Box.createVerticalStrut(8));
        card.add(createSkeletonLoader((int)((width - 40) * 0.6), 16, 4));
        card.add(Box.createVerticalGlue());
        
        // Button skeleton
        card.add(createSkeletonLoader(width - 40, 40, 8));
        
        return new JPanel() {
            {
                setLayout(new BorderLayout());
                setOpaque(false);
                add(card, BorderLayout.CENTER);
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(CARD_WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 
                        BORDER_RADIUS, BORDER_RADIUS));
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
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
    
    // ==================== ANIMATION UTILITIES ====================
    
    /**
     * Create a fade-in animation for a component.
     * @param component The component to animate
     * @param duration Duration in milliseconds
     */
    public static void fadeIn(JComponent component, int duration) {
        Timer timer = new Timer(16, null);
        final long startTime = System.currentTimeMillis();
        final float initialAlpha = 0f;
        final float targetAlpha = 1f;
        
        component.putClientProperty("fadeAlpha", initialAlpha);
        
        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(1f, (float) elapsed / duration);
            
            // Ease-out curve for smooth deceleration
            float easedProgress = 1f - (1f - progress) * (1f - progress);
            float alpha = initialAlpha + (targetAlpha - initialAlpha) * easedProgress;
            
            component.putClientProperty("fadeAlpha", alpha);
            component.repaint();
            
            if (progress >= 1f) {
                timer.stop();
            }
        });
        timer.start();
    }
    
    /**
     * Create an elevated card panel with shadow and hover lift effect.
     * @return A JPanel with modern elevation styling
     */
    public static JPanel createElevatedCard() {
        return new JPanel() {
            private boolean isHovered = false;
            private int shadowOffset = 4;
            
            {
                setOpaque(false);
                setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
                
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        shadowOffset = 8;
                        repaint();
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        shadowOffset = 4;
                        repaint();
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int offset = shadowOffset;
                
                // Draw layered shadows for depth
                for (int i = offset; i > 0; i--) {
                    float alpha = 0.03f * (offset - i + 1);
                    g2.setColor(new Color(30, 58, 95, (int)(alpha * 255)));
                    g2.fillRoundRect(i, i, getWidth() - i, getHeight() - i, 
                            BORDER_RADIUS, BORDER_RADIUS);
                }
                
                // Draw card background
                g2.setColor(CARD_WHITE);
                g2.fillRoundRect(0, 0, getWidth() - offset, getHeight() - offset, 
                        BORDER_RADIUS, BORDER_RADIUS);
                
                // Subtle border
                g2.setColor(isHovered ? BORDER_FOCUS : BORDER_LIGHT);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - offset - 1, getHeight() - offset - 1, 
                        BORDER_RADIUS, BORDER_RADIUS);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
    }
    
    /**
     * Create a modern tooltip manager with styled tooltips.
     */
    public static void setupModernTooltips() {
        UIManager.put("ToolTip.background", CARD_WHITE);
        UIManager.put("ToolTip.foreground", TEXT_DARK);
        UIManager.put("ToolTip.border", new CompoundBorder(
            new LineBorder(BORDER_LIGHT, 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        UIManager.put("ToolTip.font", FONT_SMALL);
    }
    
    /**
     * Create a pulsing highlight effect for drawing attention.
     * @param component The component to highlight
     */
    public static void pulseHighlight(JComponent component) {
        Timer timer = new Timer(50, null);
        final long startTime = System.currentTimeMillis();
        final int duration = 600;
        
        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = (float) elapsed / duration;
            
            if (progress >= 1f) {
                timer.stop();
                component.setBorder(null);
                return;
            }
            
            // Pulse effect using sine wave
            float intensity = (float) Math.sin(progress * Math.PI);
            int alpha = (int)(intensity * 100);
            
            component.setBorder(BorderFactory.createLineBorder(
                new Color(BORDER_FOCUS.getRed(), BORDER_FOCUS.getGreen(), 
                         BORDER_FOCUS.getBlue(), alpha), 2, true));
            component.repaint();
        });
        timer.start();
    }
    
    /**
     * Set up anti-aliasing for the entire application.
     */
    public static void setupAntiAliasing() {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        // Set up modern tooltips
        setupModernTooltips();
    }
    
    // ==================== LOGO LOADING METHODS ====================
    
    /**
     * Get the application logo scaled to the specified size.
     * @param size The desired height in pixels (width scales proportionally)
     * @return ImageIcon of the logo, or null if loading fails
     */
    public static ImageIcon getAppLogo(int size) {
        try {
            // Check cache for common sizes
            if (size == 32 && cachedAppLogoSmall != null) return cachedAppLogoSmall;
            if (size == 48 && cachedAppLogoMedium != null) return cachedAppLogoMedium;
            if (size == 64 && cachedAppLogo != null) return cachedAppLogo;
            
            // Load the image
            File logoFile = new File(APP_LOGO_PATH);
            if (!logoFile.exists()) {
                System.err.println("Logo file not found: " + APP_LOGO_PATH);
                return null;
            }
            
            BufferedImage originalImage = ImageIO.read(logoFile);
            
            // Calculate proportional width
            double ratio = (double) originalImage.getWidth() / originalImage.getHeight();
            int scaledWidth = (int) (size * ratio);
            
            // Scale with high quality
            Image scaledImage = originalImage.getScaledInstance(scaledWidth, size, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImage);
            
            // Cache common sizes
            if (size == 32) cachedAppLogoSmall = icon;
            else if (size == 48) cachedAppLogoMedium = icon;
            else if (size == 64) cachedAppLogo = icon;
            
            return icon;
            
        } catch (IOException e) {
            System.err.println("Error loading logo: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get a JLabel with the application logo.
     * @param size The desired height in pixels
     * @return JLabel containing the logo, or a text placeholder if loading fails
     */
    public static JLabel createLogoLabel(int size) {
        ImageIcon logo = getAppLogo(size);
        if (logo != null) {
            JLabel label = new JLabel(logo) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            label.setOpaque(false);
            return label;
        } else {
            // Fallback to styled text if logo can't be loaded
            JLabel label = new JLabel("📚") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, (int)(size * 0.7)));
            label.setForeground(TEXT_LIGHT);
            return label;
        }
    }
    
    /**
     * Create a logo label with a subtle glow effect for dark backgrounds.
     * @param size The desired height in pixels
     * @return JLabel with enhanced logo display
     */
    public static JLabel createGlowingLogoLabel(int size) {
        ImageIcon logo = getAppLogo(size);
        if (logo != null) {
            JLabel label = new JLabel(logo) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    
                    // Draw subtle glow behind logo
                    int glowSize = 4;
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillOval(-glowSize, -glowSize, getWidth() + glowSize * 2, getHeight() + glowSize * 2);
                    
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            label.setOpaque(false);
            label.setBorder(new EmptyBorder(4, 4, 4, 4));
            return label;
        } else {
            return createLogoLabel(size);
        }
    }
    
    /**
     * Create a branded header panel with app name.
     * @param height The height of the header
     * @param showSubtitle Whether to show the tagline
     * @return A fully styled header panel
     */
    public static JPanel createBrandedHeader(int height, boolean showSubtitle) {
        JPanel header = createGradientHeader(height);
        header.setLayout(new GridBagLayout());
        
        // Add text panel with proper vertical alignment
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        
        // App name with shadow effect
        JLabel titleLabel = new JLabel(APP_NAME) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                
                // Draw text shadow
                g2.setColor(new Color(0, 0, 0, 50));
                g2.setFont(getFont());
                g2.drawString(getText(), 2, getHeight() - 4);
                
                // Draw main text
                g2.setColor(getForeground());
                g2.drawString(getText(), 0, getHeight() - 6);
                
                g2.dispose();
            }
        };
        titleLabel.setFont(FONT_HEADER);
        titleLabel.setForeground(TEXT_LIGHT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPanel.add(titleLabel);
        
        if (showSubtitle) {
            textPanel.add(Box.createVerticalStrut(4));
            JLabel subtitleLabel = new JLabel(APP_TAGLINE);
            subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            subtitleLabel.setForeground(new Color(255, 255, 255, 180));
            subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            textPanel.add(subtitleLabel);
        }
        
        // Center the content in the header
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        header.add(textPanel, gbc);
        
        return header;
    }
    
    /**
     * Create a compact navigation bar with logo for inner pages.
     * @param height The height of the nav bar
     * @return A navigation panel with logo on the left
     */
    public static JPanel createNavBarWithLogo(int height) {
        JPanel navBar = createGradientHeader(height);
        navBar.setBorder(new EmptyBorder(0, 25, 0, 25));
        
        // Left side with logo and title - vertically centered
        JPanel leftPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                return new Dimension(d.width, height);
            }
        };
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
        
        // Vertical centering spacer
        leftPanel.add(Box.createVerticalGlue());
        
        // Logo - sized to fit nicely in navbar
        int logoSize = Math.max(32, height - 25);
        JLabel logoLabel = createLogoLabel(logoSize);
        leftPanel.add(logoLabel);
        leftPanel.add(Box.createHorizontalStrut(12));
        
        // App name
        JLabel titleLabel = new JLabel(APP_NAME);
        titleLabel.setFont(FONT_SUBHEADER);
        titleLabel.setForeground(TEXT_LIGHT);
        leftPanel.add(titleLabel);
        
        leftPanel.add(Box.createVerticalGlue());
        
        navBar.add(leftPanel, BorderLayout.WEST);
        
        return navBar;
    }
    
    // ==================== PRIVATE CONSTRUCTOR ====================
    
    private StyleUtils() {
        // This class cannot be instantiated
    }
}
