package common.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedPanel extends JPanel {
    private int arcRadius;
    private boolean hasShadow;

    public RoundedPanel(int arcRadius, boolean hasShadow) {
        this.arcRadius = arcRadius;
        this.hasShadow = hasShadow;
        setOpaque(false); // Crucial for transparent background to see rounded corners and shadow
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        if (hasShadow) {
            // Draw shadow slightly offset and semi-transparent
            g2.setColor(new Color(0, 0, 0, 20)); // Light black shadow
            g2.fillRoundRect(5, 5, width - 5, height - 5, arcRadius * 2, arcRadius * 2);
        }

        // Draw the main rounded rectangle for the panel background
        g2.setColor(getBackground()); // Use the panel's background color
        g2.fillRoundRect(0, 0, width, height, arcRadius * 2, arcRadius * 2);

        g2.dispose();

        super.paintComponent(g); // Ensure child components are painted on top
    }
}