package common.ui;

import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedCornerBorder extends EmptyBorder {
    private final int radius;

    public RoundedCornerBorder(int radius) {
        super(radius, radius, radius, radius);
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(200, 200, 200)); // Light grey border color
        g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius * 2, radius * 2));
        g2.dispose();
    }
}