package common.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ClassInButtonUI extends BasicButtonUI {
    private final int arc;
    private Timer animationTimer;
    private float currentLift = 0;
    private final int LIFT_AMOUNT = -5; // Amount to lift on hover
    private final int ANIMATION_DURATION = 150; // Milliseconds for animation

    public ClassInButtonUI(int arc) {
        this.arc = arc;
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startAnimation((JButton) e.getSource(), currentLift, LIFT_AMOUNT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startAnimation((JButton) e.getSource(), currentLift, 0);
            }
        });
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        JButton button = (JButton) c;
        int width = button.getWidth();
        int height = button.getHeight();

        g2.translate(0, currentLift); // Apply lift effect

        // Draw shadow
        g2.setColor(new Color(0, 0, 0, 40)); // Semi-transparent black for shadow
        g2.fillRoundRect(2, 2 - (int)currentLift, width - 4, height - 4, arc, arc);

        // Draw button background
        g2.setColor(button.getBackground());
        g2.fillRoundRect(0, 0, width, height, arc, arc);

        // Paint text and icon
        super.paint(g2, c);
        g2.dispose();
    }

    private void startAnimation(final JComponent component, final float startValue, final float endValue) {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        long startTime = System.currentTimeMillis();
        animationTimer = new Timer(1000 / 30, e -> { // ~30 FPS animation
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = (float) elapsed / ANIMATION_DURATION;

            if (progress >= 1.0f) {
                currentLift = endValue;
                animationTimer.stop();
            } else {
                currentLift = startValue + (endValue - startValue) * progress;
            }
            component.repaint(); // Repaint button to show animation frame
        });
        animationTimer.setInitialDelay(0);
        animationTimer.start();
    }
}