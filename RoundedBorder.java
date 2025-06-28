package com.tutorapp.ui.components;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedBorder extends AbstractBorder {
    private final int radius;
    private final Color borderColor;
    private final int borderWidth;

    public RoundedBorder(int radius, Color borderColor, int borderWidth) {
        this.radius = radius;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(borderWidth));
        g2d.draw(new RoundRectangle2D.Double(x + borderWidth / 2.0, y + borderWidth / 2.0,
                width - borderWidth, height - borderWidth, radius * 2, radius * 2));
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        int inset = radius + borderWidth;
        return new Insets(inset, inset, inset, inset);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = radius + borderWidth;
        return insets;
    }
}
