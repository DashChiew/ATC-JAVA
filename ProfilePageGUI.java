import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ProfilePageGUI extends JFrame {

    public ProfilePageGUI() {
        // Set the title of the window
        setTitle("User Profile Application");
        // Set the default close operation for the ProfilePageGUI
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set preferred size for the frame to make it wider
        setPreferredSize(new Dimension(550, 630));

        // Main content panel with a BoxLayout for vertical stacking
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        mainPanel.setBackground(new Color(240, 242, 245));

        // -------------------- Profile Section --------------------
        JPanel profilePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int width = getWidth();
                int height = getHeight();
                int arc = 20;

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, width, height, arc, arc);

                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Do not paint the default border here
            }

            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.setMaximumSize(new Dimension(500, 320));

        // Profile Picture using RoundImageLabel
        RoundImageLabel profilePicLabel = null;
        int imageSize = 128;

        try {
            URL imageUrl = getClass().getResource("/chill.jpeg");
            String imagePath;

            if (imageUrl == null) {
                System.err.println("Local image 'chill.jpeg' not found in classpath. Using placeholder.");
                File tempPlaceholder = createPlaceholderImage(imageSize, imageSize, new Color(108, 99, 255), Color.WHITE, "Profile");
                imagePath = tempPlaceholder.getAbsolutePath();
            } else {
                imagePath = new File(imageUrl.toURI()).getAbsolutePath();
            }

            profilePicLabel = new RoundImageLabel(imagePath, imageSize);
            profilePicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            profilePicLabel.setBorder(new RoundedBorder(imageSize / 2, new Color(173, 216, 230), 2));

            ProfilePageGUI.RoundImageLabel finalProfilePicLabel = profilePicLabel;
            Timer fadeInTimer = new Timer(15, e -> {
                float alpha = finalProfilePicLabel.alpha;
                if (alpha < 1.0f) {
                    alpha += 0.05f;
                    if (alpha > 1.0f) alpha = 1.0f;
                    finalProfilePicLabel.setAlpha(alpha);
                } else {
                    ((Timer)e.getSource()).stop();
                }
            });
            fadeInTimer.setInitialDelay(100);
            fadeInTimer.start();

        } catch (Exception e) {
            System.err.println("Error initializing profile image: " + e.getMessage());
            profilePicLabel = new RoundImageLabel(null, imageSize);
            profilePicLabel.setText("Image Error");
            profilePicLabel.setHorizontalAlignment(SwingConstants.CENTER);
            profilePicLabel.setVerticalAlignment(SwingConstants.CENTER);
            profilePicLabel.setPreferredSize(new Dimension(imageSize, imageSize));
            profilePicLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        }

        // Name Label
        JLabel nameLabel = new JLabel("Alex Johnson");
        nameLabel.setFont(new Font("Inter", Font.BOLD, 28));
        nameLabel.setForeground(new Color(55, 65, 81));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Email Label
        JLabel emailLabel = new JLabel("alex.johnson@example.com");
        emailLabel.setFont(new Font("Inter", Font.PLAIN, 18));
        emailLabel.setForeground(new Color(75, 85, 99));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to the profile panel
        profilePanel.add(Box.createVerticalStrut(15));
        if (profilePicLabel != null) {
            profilePanel.add(profilePicLabel);
        }
        profilePanel.add(Box.createVerticalStrut(20));
        profilePanel.add(nameLabel);
        profilePanel.add(Box.createVerticalStrut(8));
        profilePanel.add(emailLabel);
        profilePanel.add(Box.createVerticalStrut(15));

        // -------------------- Buttons Section --------------------
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        buttonsPanel.setBackground(new Color(240, 242, 245));

        // Create and add buttons
        buttonsPanel.add(createStyledButton("Class Schedule", new Color(24, 144, 255)));

        // Store the "Change Subject" button in a variable to add an action listener
        JButton changeSubjectButton = createStyledButton("Change Subject", new Color(72, 160, 220)); // Button for changing subject
        buttonsPanel.add(changeSubjectButton);

        // Store the "Settings" button in a variable to add an action listener
        JButton settingsButton = createStyledButton("Settings", new Color(100, 116, 139));
        buttonsPanel.add(settingsButton);

        buttonsPanel.add(createStyledButton("Help", new Color(24, 144, 255)));

        // Add action listener to the Settings button
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hide the ProfilePageGUI before showing the Setting window
                ProfilePageGUI.this.setVisible(false);

                // Create and display the Settings window
                Setting settingFrame = new Setting();
                settingFrame.setVisible(true);

                // Add a WindowListener to the Setting frame
                settingFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        // When the Setting frame is closed (disposed), make the ProfilePageGUI visible again
                        ProfilePageGUI.this.setVisible(true);
                    }
                });
            }
        });

        // Add action listener to the Change Subject button
        changeSubjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hide the ProfilePageGUI before showing the SubjectUpdateForm window
                ProfilePageGUI.this.setVisible(false);

                // Create and display the SubjectUpdateForm window
                SubjectUpdateForm subjectForm = new SubjectUpdateForm(); // Changed instantiation
                subjectForm.setVisible(true);

                // Add a WindowListener to the SubjectUpdateForm frame
                subjectForm.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        // When the SubjectUpdateForm frame is closed (disposed), make the ProfilePageGUI visible again
                        ProfilePageGUI.this.setVisible(true);
                    }
                });
            }
        });


        // -------------------- Add sections to main panel --------------------
        mainPanel.add(profilePanel);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(buttonsPanel);

        // Add the main panel to the frame
        add(mainPanel);

        // Pack the components to their preferred sizes
        pack();
        // Center the window on the screen
        setLocationRelativeTo(null);
        // Make the window visible
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Inter", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setUI(new ButtonUIWithAnimation());

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Object ui = ((JButton) evt.getSource()).getUI();
                if (ui instanceof ButtonUIWithAnimation) {
                    ButtonUIWithAnimation customUI = (ButtonUIWithAnimation) ui;
                    customUI.startAnimation((JComponent) evt.getSource(), customUI.currentLift, customUI.LIFT_AMOUNT);
                }
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Object ui = ((JButton) evt.getSource()).getUI();
                if (ui instanceof ButtonUIWithAnimation) {
                    ButtonUIWithAnimation customUI = (ButtonUIWithAnimation) ui;
                    customUI.startAnimation((JComponent) evt.getSource(), customUI.currentLift, 0);
                }
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private File createPlaceholderImage(int width, int height, Color bgColor, Color textColor, String text) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(textColor);
        g2d.setFont(new Font("Inter", Font.BOLD, Math.min(width, height) / 4));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (fm.getAscent() + (height - (fm.getAscent() + fm.getDescent())) / 2);
        g2d.drawString(text, x, y);

        g2d.dispose();

        File tempFile = File.createTempFile("placeholder", ".png");
        ImageIO.write(image, "png", tempFile);
        tempFile.deleteOnExit();
        return tempFile;
    }

    private static class ButtonUIWithAnimation extends BasicButtonUI {
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

    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color borderColor;
        private final int borderWidth;

        RoundedBorder(int radius, Color borderColor, int borderWidth) {
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

    private static class RoundImageLabel extends JLabel {
        private BufferedImage image;
        private float alpha = 0.0f;

        public RoundImageLabel(String imagePath, int size) {
            if (imagePath != null) {
                try {
                    BufferedImage originalImage = ImageIO.read(new File(imagePath));
                    if (originalImage != null) {
                        this.image = createRoundImage(originalImage, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    setText("Image Error");
                    setHorizontalAlignment(SwingConstants.CENTER);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ProfilePageGUI();
        });
    }
}