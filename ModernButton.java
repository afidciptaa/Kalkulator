import javax.swing.JButton;
import javax.swing.Icon;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Ultra-Modern Glassmorphic Button with rounded squircle corners,
 * subtle tactile top highlights, gradient support, and smooth hover feedback.
 */
public class ModernButton extends JButton {
    private Color defaultBg;
    private Color defaultFg;
    private Color hoverBg;
    private Color hoverFg;
    private Color pressedBg;

    // Optional gradient support (used for Equals button)
    private Color gradientStart = null;
    private Color gradientEnd = null;
    private Color gradientHoverStart = null;
    private Color gradientHoverEnd = null;

    private boolean isHovered = false;
    private boolean isPressed = false;
    private int cornerRadius = 16;
    private boolean drawTopHighlight = true;
    private Color borderColor = null;

    public ModernButton(String text, Color bg, Color fg, Color hoverBg, Color hoverFg, Font font) {
        super(text);
        this.defaultBg = bg;
        this.defaultFg = fg;
        this.hoverBg = hoverBg;
        this.hoverFg = hoverFg != null ? hoverFg : fg;
        this.pressedBg = hoverBg.darker();

        setFont(font);
        setForeground(fg);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    isPressed = true;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    public ModernButton(String text, Color bg, Color fg, Color hoverBg, Font font) {
        this(text, bg, fg, hoverBg, fg, font);
    }

    public ModernButton(Icon icon, Color bg, Color hoverBg) {
        this("", bg, Color.WHITE, hoverBg, Color.WHITE, new Font("Segoe UI", Font.PLAIN, 12));
        setIcon(icon);
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    public void setDrawTopHighlight(boolean draw) {
        this.drawTopHighlight = draw;
        repaint();
    }

    public void setGradient(Color start, Color end, Color hoverStart, Color hoverEnd) {
        this.gradientStart = start;
        this.gradientEnd = end;
        this.gradientHoverStart = hoverStart;
        this.gradientHoverEnd = hoverEnd;
        repaint();
    }

    public void clearGradient() {
        this.gradientStart = null;
        this.gradientEnd = null;
        repaint();
    }

    public void updateColors(Color bg, Color fg, Color hoverBg, Color hoverFg) {
        this.defaultBg = bg;
        this.defaultFg = fg;
        this.hoverBg = hoverBg;
        this.hoverFg = hoverFg != null ? hoverFg : fg;
        this.pressedBg = hoverBg.darker();
        setForeground(fg);
        repaint();
    }

    public void updateColors(Color bg, Color hoverBg) {
        updateColors(bg, defaultFg, hoverBg, hoverFg);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        float r = cornerRadius;

        // 1. Paint Background (Gradient or Solid)
        if (gradientStart != null && gradientEnd != null) {
            Color g1 = isPressed ? gradientStart.darker() : (isHovered ? gradientHoverStart : gradientStart);
            Color g2Color = isPressed ? gradientEnd.darker() : (isHovered ? gradientHoverEnd : gradientEnd);
            GradientPaint gp = new GradientPaint(0, 0, g1, width, height, g2Color);
            g2.setPaint(gp);
        } else {
            if (!isEnabled()) {
                g2.setColor(new Color(defaultBg.getRed(), defaultBg.getGreen(), defaultBg.getBlue(), 120));
            } else if (isPressed) {
                g2.setColor(pressedBg);
            } else if (isHovered) {
                g2.setColor(hoverBg);
            } else {
                g2.setColor(defaultBg);
            }
        }
        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, r, r));

        // 2. Subtle Top Highlight Line (Glass tactile feel)
        if (drawTopHighlight && !isPressed) {
            g2.setColor(new Color(255, 255, 255, isHovered ? 35 : 18));
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(new RoundRectangle2D.Float(1, 1, width - 2, height - 2, r - 1, r - 1));
        }

        // 3. Subtle outer border (if set)
        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.0f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, width - 1, height - 1, r, r));
        }

        // 4. Content (Icon and/or Text)
        Icon icon = getIcon();
        String text = getText();
        Color currentFg = (!isEnabled()) ? new Color(defaultFg.getRed(), defaultFg.getGreen(), defaultFg.getBlue(), 100)
                : (isHovered ? hoverFg : defaultFg);
        g2.setColor(currentFg);
        g2.setFont(getFont());

        FontMetrics fm = g2.getFontMetrics();
        int textW = (text != null && !text.isEmpty()) ? fm.stringWidth(text) : 0;
        int textH = (fm.getAscent() - fm.getDescent());

        int iconW = (icon != null) ? icon.getIconWidth() : 0;
        int iconH = (icon != null) ? icon.getIconHeight() : 0;
        int gap = (iconW > 0 && textW > 0) ? 7 : 0;

        int totalContentW = iconW + gap + textW;
        int startX = (width - totalContentW) / 2;

        if (icon != null) {
            int iconY = (height - iconH) / 2;
            icon.paintIcon(this, g2, startX, iconY);
            startX += iconW + gap;
        }

        if (text != null && !text.isEmpty()) {
            int textY = (height + textH) / 2;
            g2.drawString(text, startX, textY);
        }

        g2.dispose();
    }
}
