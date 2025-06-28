package com.tutorapp.ui.components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class RoundImageLabel extends JLabel {
    private BufferedImage image;
    public float alpha = 0.0f; // Made public for direct access from TutorDashboardGUI Timer

    public RoundImageLabel(String imagePath, int size) {
        if (imagePath != null) {
            try {
                URL imageUrl = null;
                // Try to load as resource first (e.g., /xiaochou.png)
                if (imagePath.startsWith("/")) {
                    imageUrl = getClass().getResource(imagePath);
                } else { // Handle paths like "xiaochou.png" directly
                    imageUrl = getClass().getResource("/" + imagePath);
                }

                BufferedImage originalImage = null;

                if (imageUrl != null) {
                    originalImage = ImageIO.read(imageUrl);
                } else {
                    // Fallback to direct file path if not found as resource
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        originalImage = ImageIO.read(imageFile);
                    } else {
                        System.err.println("Image not found at path: " + imagePath);
                    }
                }

                if (originalImage != null) {
                    this.image = createRoundImage(originalImage, size);
                } else {
                    setText("Image Not Found");
                    setHorizontalAlignment(SwingConstants.CENTER);
                    setVerticalAlignment(SwingConstants.CENTER);
                    setBackground(Color.LIGHT_GRAY);
                    setOpaque(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
                setText("Image Error");
                setHorizontalAlignment(SwingConstants.CENTER);
                setVerticalAlignment(SwingConstants.CENTER);
            }
        } else {
            setText("No Image");
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
            setBackground(Color.LIGHT_GRAY);
            setOpaque(true);
        }
        setPreferredSize(new Dimension(size, size));
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }

    private BufferedImage createRoundImage(BufferedImage originalImage, int size) {
        BufferedImage scaledImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int imgWidth = originalImage.getWidth();
        int imgHeight = originalImage.getHeight();
        int drawX = 0;
        int drawY = 0;
        int cropSize = Math.min(imgWidth, imgHeight);

        if (imgWidth > imgHeight) {
            drawX = (imgWidth - imgHeight) / 2;
        } else if (imgHeight > imgWidth) {
            drawY = (imgHeight - imgWidth) / 2;
        }

        Area clip = new Area(new Ellipse2D.Double(0, 0, size, size));
        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.setClip(clip);

        g2d.drawImage(originalImage, 0, 0, size, size, drawX, drawY, drawX + cropSize, drawY + cropSize, null);

        g2d.dispose();
        return scaledImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            g2d.dispose();
        } else {
            super.paintComponent(g);
        }
    }
}
