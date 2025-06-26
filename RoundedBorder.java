import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Color;
import javax.swing.border.Border;

public class RoundedBorder implements Border {

    private int radius;
    private Color borderColor;
    private int strokeWidth;

    // Constructor to create a rounded border with specified radius and color
    public RoundedBorder(int radius, Color borderColor, int strokeWidth) {
        this.radius = radius;
        this.borderColor = borderColor;
        this.strokeWidth = strokeWidth;
    }

    // Default constructor (if needed, e.g., for default styling)
    public RoundedBorder(int radius) {
        this(radius, Color.GRAY, 1); // Default to gray border, 1 pixel wide
    }

    @Override
    public Insets getBorderInsets(Component c) {
        // Adjust insets to allow space for the border
        return new Insets(radius + 1, radius + 1, radius + 2, radius + 2);
    }

    @Override
    public boolean isBorderOpaque() {
        return false; // Border is not opaque, content behind it will show through corners
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create(); // Create a copy of the Graphics object
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // For smooth edges

        // Set the color for the border
        g2.setColor(borderColor);

        // Draw the rounded rectangle
        // Adjust coordinates and dimensions to account for strokeWidth
        if (strokeWidth > 0) {
            for (int i = 0; i < strokeWidth; i++) {
                g2.drawRoundRect(x + i, y + i, width - 1 - 2 * i, height - 1 - 2 * i, radius, radius);
            }
        }

        g2.dispose(); // Release resources
    }
}