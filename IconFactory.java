import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Procedural Vector Icon Generator for Java Swing.
 * Renders 100% pure Java2D vector graphics so there are NEVER missing font
 * glyphs or tofu boxes (▯) on any Windows system.
 */
public class IconFactory {

    /**
     * Generates a sleek, modern app icon at a specified dimension.
     */
    public static BufferedImage createAppIcon(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        float s = size;
        float padding = s * 0.05f;
        float radius = s * 0.22f;

        // Gradient Background (Indigo to Royal Blue)
        GradientPaint bgGradient = new GradientPaint(
                0, 0, new Color(0x3B, 0x82, 0xF6),
                s, s, new Color(0x1D, 0x4E, 0xD8)
        );
        g2.setPaint(bgGradient);
        g2.fill(new RoundRectangle2D.Float(padding, padding, s - 2 * padding, s - 2 * padding, radius, radius));

        // Subtle outer highlight border
        g2.setColor(new Color(255, 255, 255, 60));
        g2.setStroke(new BasicStroke(Math.max(1f, s * 0.02f)));
        g2.draw(new RoundRectangle2D.Float(padding, padding, s - 2 * padding, s - 2 * padding, radius, radius));

        // Display Screen area (top dark rounded rect)
        float dispX = s * 0.16f;
        float dispY = s * 0.16f;
        float dispW = s * 0.68f;
        float dispH = s * 0.22f;
        float dispR = s * 0.08f;
        g2.setColor(new Color(0x0F, 0x17, 0x2A, 180));
        g2.fill(new RoundRectangle2D.Float(dispX, dispY, dispW, dispH, dispR, dispR));

        // Inside display: digital readout preview bar
        g2.setColor(new Color(0x67, 0xE8, 0xF9));
        float barH = Math.max(2f, s * 0.045f);
        float barW = dispW * 0.45f;
        g2.fill(new RoundRectangle2D.Float(dispX + dispW - barW - s * 0.04f, dispY + (dispH - barH) / 2f, barW, barH, barH, barH));

        // Keypad grid: 3 rows x 3 columns of mini rounded buttons
        float keyAreaY = s * 0.44f;
        float keyAreaH = s * 0.42f;
        int rows = 3;
        int cols = 3;
        float gap = s * 0.035f;
        float btnW = (dispW - (cols - 1) * gap) / cols;
        float btnH = (keyAreaH - (rows - 1) * gap) / rows;
        float btnR = s * 0.05f;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                float bx = dispX + c * (btnW + gap);
                float by = keyAreaY + r * (btnH + gap);

                if (r == rows - 1 && c == cols - 1) {
                    g2.setColor(new Color(0x10, 0xB9, 0x81)); // Equals
                } else if (c == cols - 1 || (r == 0 && c == 0)) {
                    g2.setColor(new Color(0xFB, 0xBF, 0x24)); // Operators
                } else {
                    g2.setColor(new Color(255, 255, 255, 210)); // Numbers
                }
                g2.fill(new RoundRectangle2D.Float(bx, by, btnW, btnH, btnR, btnR));
            }
        }

        g2.dispose();
        return img;
    }

    public static List<Image> getAppIconList() {
        int[] sizes = {16, 24, 32, 48, 64, 128, 256};
        List<Image> list = new ArrayList<>();
        for (int sz : sizes) {
            list.add(createAppIcon(sz));
        }
        return list;
    }

    public static void saveIconFileIfMissing(String path) {
        File file = new File(path);
        try {
            BufferedImage icon = createAppIcon(256);
            ImageIO.write(icon, "png", file);
        } catch (IOException ignored) {}
    }

    /**
     * Vector Hamburger Menu Icon (3 horizontal bars)
     */
    public static ImageIcon createMenuIcon(int size, Color color) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(color);
        float h = Math.max(2f, size * 0.12f);
        float w = size * 0.75f;
        float x = (size - w) / 2f;
        float r = h;

        float gap = size * 0.22f;
        float cy = size / 2f - h / 2f;

        g2.fill(new RoundRectangle2D.Float(x, cy - gap, w, h, r, r));
        g2.fill(new RoundRectangle2D.Float(x, cy, w, h, r, r));
        g2.fill(new RoundRectangle2D.Float(x, cy + gap, w, h, r, r));

        g2.dispose();
        return new ImageIcon(img);
    }

    /**
     * Vector Scientific / Function Icon (f(x) or geometric triangle/ruler)
     */
    public static ImageIcon createScientificIcon(int size, Color color) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(color);
        float stroke = Math.max(1.6f, size * 0.1f);
        g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        float pad = size * 0.15f;
        // Draw geometric set-square / triangle with tick marks
        Path2D.Float path = new Path2D.Float();
        path.moveTo(pad, size - pad);
        path.lineTo(size - pad, size - pad);
        path.lineTo(pad, pad);
        path.closePath();
        g2.draw(path);

        // Inner small hollow triangle
        float innerPad = size * 0.32f;
        Path2D.Float inner = new Path2D.Float();
        inner.moveTo(pad + innerPad * 0.6f, size - pad - innerPad * 0.4f);
        inner.lineTo(size - pad - innerPad * 1.1f, size - pad - innerPad * 0.4f);
        inner.lineTo(pad + innerPad * 0.6f, pad + innerPad * 1.1f);
        inner.closePath();
        g2.draw(inner);

        g2.dispose();
        return new ImageIcon(img);
    }

    /**
     * Vector Sun Icon (for Light Theme Toggle)
     */
    public static ImageIcon createSunIcon(int size, Color color) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(color);
        int cx = size / 2;
        int cy = size / 2;
        int r = (int) (size * 0.22f);

        // Center Sun Core
        g2.fillOval(cx - r, cy - r, r * 2, r * 2);

        // 8 Radiating Rays
        float stroke = Math.max(1.8f, size * 0.11f);
        g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int rayInner = (int) (size * 0.33f);
        int rayOuter = (int) (size * 0.46f);

        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4.0;
            int x1 = (int) (cx + rayInner * Math.cos(angle));
            int y1 = (int) (cy + rayInner * Math.sin(angle));
            int x2 = (int) (cx + rayOuter * Math.cos(angle));
            int y2 = (int) (cy + rayOuter * Math.sin(angle));
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.dispose();
        return new ImageIcon(img);
    }

    /**
     * Vector Moon Icon (for Dark Theme Toggle)
     */
    public static ImageIcon createMoonIcon(int size, Color color) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(color);
        float pad = size * 0.12f;
        float d = size - 2 * pad;

        // Base Moon Circle
        Area moon = new Area(new Ellipse2D.Float(pad, pad, d, d));
        // Subtracted Circle to create crescent
        Area cutter = new Area(new Ellipse2D.Float(pad + d * 0.28f, pad - d * 0.08f, d * 0.9f, d * 0.9f));
        moon.subtract(cutter);

        g2.fill(moon);
        g2.dispose();
        return new ImageIcon(img);
    }

    /**
     * Vector History / Clock Icon
     */
    public static ImageIcon createHistoryIcon(int size, Color color) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(color);
        float stroke = Math.max(1.8f, size * 0.10f);
        g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        float pad = stroke + size * 0.05f;
        float d = size - 2 * pad;
        g2.drawOval((int) pad, (int) pad, (int) d, (int) d);

        // Clock hands
        int cx = size / 2;
        int cy = size / 2;
        g2.drawLine(cx, cy, cx, (int) (cy - d * 0.32f));
        g2.drawLine(cx, cy, (int) (cx + d * 0.26f), cy);

        g2.dispose();
        return new ImageIcon(img);
    }

    /**
     * Vector Trash Can Icon
     */
    public static ImageIcon createTrashIcon(int size, Color color) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(color);
        float stroke = Math.max(1.5f, size * 0.09f);
        g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        float w = size;
        float h = size;

        // Lid handle & rim
        g2.drawLine((int)(w * 0.22f), (int)(h * 0.26f), (int)(w * 0.78f), (int)(h * 0.26f));
        g2.drawLine((int)(w * 0.40f), (int)(h * 0.16f), (int)(w * 0.60f), (int)(h * 0.16f));

        // Bin body
        int[] xPoints = {(int)(w * 0.28f), (int)(w * 0.33f), (int)(w * 0.67f), (int)(w * 0.72f)};
        int[] yPoints = {(int)(h * 0.34f), (int)(h * 0.86f), (int)(h * 0.86f), (int)(h * 0.34f)};
        g2.drawPolygon(xPoints, yPoints, 4);

        // Slits
        g2.drawLine((int)(w * 0.43f), (int)(h * 0.44f), (int)(w * 0.45f), (int)(h * 0.76f));
        g2.drawLine((int)(w * 0.57f), (int)(h * 0.44f), (int)(w * 0.55f), (int)(h * 0.76f));

        g2.dispose();
        return new ImageIcon(img);
    }

    /**
     * Vector Backspace Arrow Icon (with 'X' inside)
     */
    public static ImageIcon createBackspaceIcon(int size, Color color) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(color);
        float stroke = Math.max(1.8f, size * 0.09f);
        g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        float pad = size * 0.14f;
        float leftX = pad;
        float pointX = size * 0.38f;
        float rightX = size - pad;
        float topY = size * 0.24f;
        float bottomY = size * 0.76f;
        float midY = size / 2f;

        // Arrow Body
        Path2D.Float path = new Path2D.Float();
        path.moveTo(pointX, topY);
        path.lineTo(rightX, topY);
        path.lineTo(rightX, bottomY);
        path.lineTo(pointX, bottomY);
        path.lineTo(leftX, midY);
        path.closePath();
        g2.draw(path);

        // Inner 'X'
        float xL = size * 0.48f;
        float xR = size * 0.72f;
        float yT = size * 0.38f;
        float yB = size * 0.62f;
        g2.drawLine((int) xL, (int) yT, (int) xR, (int) yB);
        g2.drawLine((int) xL, (int) yB, (int) xR, (int) yT);

        g2.dispose();
        return new ImageIcon(img);
    }

    /**
     * Vector Copy / Clipboard Icon
     */
    public static ImageIcon createCopyIcon(int size, Color color) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(color);
        g2.setStroke(new BasicStroke(Math.max(1.6f, size * 0.09f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        float offset = size * 0.22f;
        float rectW = size * 0.52f;
        float rectH = size * 0.58f;
        float r = size * 0.12f;

        // Back page
        g2.draw(new RoundRectangle2D.Float(offset, size * 0.08f, rectW, rectH, r, r));

        // Front page
        g2.setColor(new Color(0, 0, 0, 0));
        g2.fill(new RoundRectangle2D.Float(size * 0.1f, offset, rectW, rectH, r, r));
        g2.setColor(color);
        g2.draw(new RoundRectangle2D.Float(size * 0.1f, offset, rectW, rectH, r, r));

        g2.dispose();
        return new ImageIcon(img);
    }

    public static void main(String[] args) {
        saveIconFileIfMissing("icon.png");
        System.out.println("Icon saved successfully to: " + new File("icon.png").getAbsolutePath());
    }
}
