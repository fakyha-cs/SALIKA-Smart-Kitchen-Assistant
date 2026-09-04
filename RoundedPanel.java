import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Class: RoundedPanel
 *
 * Purpose:
 * Custom Swing JPanel implementation with smooth anti-aliased rounded corners,
 * subtle drop shadows, and hover elevation highlights.
 *
 * Why this class exists:
 * Eliminates the default flat/notepad appearance of standard Swing JPanels,
 * providing modern card containers.
 */
public class RoundedPanel extends JPanel {
    private Color backgroundColor;
    private Color hoverColor;
    private Color borderColor;
    private int cornerRadius;
    private boolean isHovered;
    private boolean enableHoverEffect;

    public RoundedPanel(int radius, Color bg) {
        this(radius, bg, bg.darker(), new Color(0xE2, 0xDC, 0xD0), false);
    }

    public RoundedPanel(int radius, Color bg, boolean enableHover) {
        this(radius, bg, new Color(0xFA, 0xF8, 0xF2), new Color(0xD2, 0xCC, 0xC0), enableHover);
    }

    public RoundedPanel(int radius, Color bg, Color hoverBg, Color borderC, boolean enableHover) {
        super();
        this.cornerRadius = radius;
        this.backgroundColor = bg;
        this.hoverColor = hoverBg;
        this.borderColor = borderC;
        this.enableHoverEffect = enableHover;
        this.isHovered = false;
        setOpaque(false);

        if (enableHoverEffect) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // 1. Draw subtle drop shadow below card
        g2.setColor(new Color(0, 0, 0, isHovered ? 25 : 12));
        g2.fill(new RoundRectangle2D.Float(2, 3, width - 4, height - 3, cornerRadius, cornerRadius));

        // 2. Draw card background fill
        g2.setColor(isHovered ? hoverColor : backgroundColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, width - 2, height - 3, cornerRadius, cornerRadius));

        // 3. Draw border outline
        if (borderColor != null) {
            g2.setColor(isHovered ? UIUtils.COLOR_PRIMARY : borderColor);
            g2.setStroke(new BasicStroke(isHovered ? 1.8f : 1.0f));
            g2.draw(new RoundRectangle2D.Float(0, 0, width - 3, height - 4, cornerRadius, cornerRadius));
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
