import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * Utility class: UIUtils
 *
 * Purpose:
 * Provides central color palettes, typography, custom border factories, and portable image loaders
 * for SALIKA's modern Java Swing GUI.
 *
 * Student Code Style & Viva Notes:
 * - Simple static methods for theme consistency.
 * - Resource safety: Loads PNG icons via ClassLoader resources. If icon file is missing,
 *   automatically generates a sleek vector badge icon so the application NEVER crashes in NetBeans.
 */
public class UIUtils {

    // Modern Kitchen Theme Color Palette
    public static final Color COLOR_PRIMARY = new Color(0x2E, 0x6F, 0x40);      // Deep Forest Emerald
    public static final Color COLOR_PRIMARY_HOVER = new Color(0x3A, 0x87, 0x4F);// Bright Emerald
    public static final Color COLOR_BG = new Color(0xF5, 0xF1, 0xE8);             // Warm Soft Almond
    public static final Color COLOR_SIDEBAR = new Color(0x1F, 0x3E, 0x29);        // Dark Charcoal Forest
    public static final Color COLOR_CARD_BG = Color.WHITE;                       // Pure White Card
    public static final Color COLOR_CARD_BORDER = new Color(0xE2, 0xDC, 0xD0);   // Subtle Card Outline
    public static final Color COLOR_ACCENT_ORANGE = new Color(0xD9, 0x6B, 0x43); // Warm Terracotta
    public static final Color COLOR_ACCENT_AMBER = new Color(0xE6, 0xA1, 0x00);  // Warm Golden Amber
    public static final Color COLOR_SAGE_LIGHT = new Color(0xE8, 0xF3, 0xE8);    // Soft Sage Badge Fill
    public static final Color COLOR_TEXT_DARK = new Color(0x2C, 0x3E, 0x35);     // Dark Slate Charcoal
    public static final Color COLOR_TEXT_MUTED = new Color(0x7A, 0x8A, 0x82);    // Muted Subtitle Gray

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BADGE = new Font("Segoe UI", Font.BOLD, 11);

    /**
     * Portable Resource Icon Loader with Safe Vector Fallback.
     * Prevents NullPointerException crashes if icons are missing when imported into NetBeans.
     */
    public static ImageIcon getSafeIcon(String resourceName, String fallbackText, int width, int height) {
        try {
            URL imgUrl = UIUtils.class.getResource("/icons/" + resourceName);
            if (imgUrl != null) {
                ImageIcon original = new ImageIcon(imgUrl);
                Image scaled = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (Exception ignored) {
            // Ignore error and generate vector fallback
        }

        // Generate vector badge image dynamically
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw soft circle background
        g2.setColor(COLOR_SAGE_LIGHT);
        g2.fillOval(0, 0, width, height);
        g2.setColor(COLOR_PRIMARY);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawOval(1, 1, width - 2, height - 2);

        // Draw initial character inside
        g2.setColor(COLOR_PRIMARY);
        g2.setFont(new Font("Segoe UI", Font.BOLD, (int) (height * 0.5)));
        FontMetrics fm = g2.getFontMetrics();
        String symbol = (fallbackText != null && !fallbackText.isEmpty()) ? fallbackText.substring(0, 1).toUpperCase() : "🍲";
        int textX = (width - fm.stringWidth(symbol)) / 2;
        int textY = (height - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(symbol, textX, textY);
        g2.dispose();

        return new ImageIcon(img);
    }

    /**
     * Creates a styled modern button with rounded corners, hover transitions, and colors.
     */
    public static JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(bg.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(COLOR_PRIMARY_HOVER);
                } else {
                    g2.setColor(bg);
                }
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BODY_BOLD);
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }

    /**
     * Creates a modern category pill badge panel.
     */
    public static JPanel createBadge(String text, Color bg, Color fg) {
        JPanel badge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
            }
        };
        badge.setOpaque(false);
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_BADGE);
        lbl.setForeground(fg);
        badge.add(lbl);
        return badge;
    }
}
