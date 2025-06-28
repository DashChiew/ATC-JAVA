package com.tutorapp.ui.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class ButtonUIWithAnimation extends BasicButtonUI {
    public float currentLift = 0;
    public Timer animationTimer;

    public final int LIFT_AMOUNT = -5;
    public final int ANIMATION_DURATION = 150;
    public final int FRAME_RATE = 30;

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = c.getWidth();
        int height = c.getHeight();
        int arc = 25;

        g2.translate(0, currentLift);

        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillRoundRect(2, 2 - (int)currentLift, width - 4, height - 4, arc, arc);

        g2.setColor(c.getBackground());
        g2.fillRoundRect(0, 0, width, height, arc, arc);

        super.paint(g2, c);
        g2.dispose();
    }

    // This method is not called automatically by the UI.
    // It would need to be triggered by a MouseListener on the button.
    // For simplicity, the original code only set the UI, not explicitly started animation.
    // I'll keep it as it was in the original, but for actual animation, you'd add:
    /*
    @Override
    public void installListeners(AbstractButton b) {
        super.installListeners(b);
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startAnimation(b, currentLift, LIFT_AMOUNT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startAnimation(b, currentLift, 0);
            }
        });
    }
    */
    // The original code only assigned the UI, so I'm not adding mouse listeners here
    // as it would change the current behavior. If you want the animation, you'd add
    // the mouse listeners to the button itself or within this UI's installListeners.

    public void startAnimation(final JComponent component, final float startValue, final float endValue) {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        long startTime = System.currentTimeMillis();
        animationTimer = new Timer(1000 / FRAME_RATE, e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = (float) elapsed / ANIMATION_DURATION;

            if (progress >= 1.0f) {
                currentLift = endValue;
                animationTimer.stop();
            } else {
                currentLift = startValue + (endValue - startValue) * progress;
            }
            component.repaint();
        });
        animationTimer.setInitialDelay(0);
        animationTimer.start();
    }
}
