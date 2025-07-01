import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL; // Still needed for resource loading
import javax.imageio.ImageIO; // For ImageIO.read
import javax.swing.plaf.basic.BasicButtonUI;


public class RProfilePageGUI extends JFrame implements ActionListener {

    private final loginPageTest loginPage;

    private final JButton btnStudentOperations;
    private final JButton btnPaymentOperations;
    private final JButton btnSettings;
    private final JButton btnLogout;

    public RProfilePageGUI(loginPageTest loginPage) {
        this.loginPage = loginPage;
        // Set the title of the window
        setTitle("User Profile Application");
        // Set the default close operation
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Changed to DISPOSE_ON_CLOSE for secondary window
        // Set preferred size for the frame to make it wider
        setPreferredSize(new Dimension(550, 630)); // Increased width from 450 to 550

        // Main content panel with a BoxLayout for vertical stacking
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Stack elements vertically
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40)); // Increased padding around the main content
        mainPanel.setBackground(new Color(240, 242, 245)); // Light gray background, clean and modern

        // -------------------- Profile Section --------------------
        // Custom JPanel for rounded corners
        JPanel profilePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int width = getWidth();
                int height = getHeight();
                int arc = 20; // Increased arc for more pronounced rounded corners

                // Draw background with rounded corners
                g2.setColor(getBackground()); // Use the panel's background color
                g2.fillRoundRect(0, 0, width, height, arc, arc);

                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Do not paint the default border here
            }

            @Override
            public boolean isOpaque() {
                // Return false so paintComponent is always called
                return false;
            }
        };
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS)); // Stack profile elements vertically
        profilePanel.setBackground(Color.WHITE); // White background for the profile section
        profilePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Increased padding inside the profile panel
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the panel itself
        profilePanel.setMaximumSize(new Dimension(500, 320)); // Adjusted max width to match wider frame
        // The putClientProperty("JComponent.roundedCorners", true) is now handled by custom paintComponent
        // profilePanel.putClientProperty("JComponent.roundedCorners", true);

        // Profile Picture using RoundImageLabel
        RoundImageLabel profilePicLabel;
        int imageSize = 128; // Define desired image size

        try {
            // Path to your local image resource.
            // Ensure 'chill.jpeg' is in your classpath (e.g., in a 'resources' folder)
            URL imageUrl = getClass().getResource("/chill.jpeg");
            String imagePath;

            if (imageUrl == null) {
                // Fallback if resource not found, provide a default placeholder image path
                System.err.println("Local image 'chill.jpeg' not found in classpath. Using placeholder.");
                // Create a temporary placeholder image file
                File tempPlaceholder = createPlaceholderImage(imageSize, imageSize, new Color(108, 99, 255), Color.WHITE, "Profile");
                imagePath = tempPlaceholder.getAbsolutePath();
            } else {
                imagePath = new File(imageUrl.toURI()).getAbsolutePath();
            }

            profilePicLabel = new RoundImageLabel(imagePath, imageSize);
            profilePicLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the image horizontally

            // Add a subtle border around the image using RoundedBorder
            profilePicLabel.setBorder(new RoundedBorder(imageSize / 2, new Color(173, 216, 230), 2)); // Light blue border, radius set to half size for circular

            // Start fade-in animation for the image
            // Note: The 'profilePicLabel' is effectively final here, so it can be used in the lambda
            RProfilePageGUI.RoundImageLabel finalProfilePicLabel = profilePicLabel;
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
            fadeInTimer.setInitialDelay(100); // Small delay before starting fade-in
            fadeInTimer.start();

        } catch (Exception e) {
            System.err.println("Error initializing profile image: " + e.getMessage());
            // Fallback for label if image cannot be loaded
            profilePicLabel = new RoundImageLabel(null, imageSize); // Create with null image
            profilePicLabel.setText("Image Error");
            profilePicLabel.setHorizontalAlignment(SwingConstants.CENTER);
            profilePicLabel.setVerticalAlignment(SwingConstants.CENTER);
            profilePicLabel.setPreferredSize(new Dimension(imageSize, imageSize));
            profilePicLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        }


        // Name Label
        JLabel nameLabel = new JLabel("Alex Johnson"); // Updated name
        nameLabel.setFont(new Font("Inter", Font.BOLD, 28)); // Larger, bolder font for name
        nameLabel.setForeground(new Color(55, 65, 81)); // Dark gray text
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally

        // Email Label
        JLabel emailLabel = new JLabel("alex.johnson@example.com"); // Updated email
        emailLabel.setFont(new Font("Inter", Font.PLAIN, 18)); // Slightly larger font for email
        emailLabel.setForeground(new Color(75, 85, 99)); // Gray text
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally

        // Add components to the profile panel
        profilePanel.add(Box.createVerticalStrut(15)); // Spacer
        profilePanel.add(profilePicLabel);
        profilePanel.add(Box.createVerticalStrut(20)); // Spacer
        profilePanel.add(nameLabel);
        profilePanel.add(Box.createVerticalStrut(8)); // Spacer
        profilePanel.add(emailLabel);
        profilePanel.add(Box.createVerticalStrut(15)); // Spacer

        // -------------------- Buttons Section --------------------
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 20, 20)); // 2 rows, 2 columns, with increased gaps
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0)); // Increased top padding
        buttonsPanel.setBackground(new Color(240, 242, 245)); // Match main background

        btnStudentOperations = createStyledButton("Student Operations", new Color(24, 144, 255));
        buttonsPanel.add(btnStudentOperations);
        btnStudentOperations.addActionListener(e -> RStudentMenu.showStudentMenu(this));

        btnPaymentOperations = createStyledButton("Payment Operations", new Color(72, 160, 220));
        buttonsPanel.add(btnPaymentOperations);
        btnPaymentOperations.addActionListener(e -> RPaymentMenuGUI.showPaymentMenu(this));

        btnSettings = createStyledButton("Settings", new Color(100, 116, 139));
        buttonsPanel.add(btnSettings);
        btnSettings.addActionListener(this);

        btnLogout = createStyledButton("Logout", new Color(24, 144, 255)); // Changed to "Help" in prev req, now back to Logout context.
        buttonsPanel.add(btnLogout);
        btnLogout.addActionListener(this);

        // -------------------- Add sections to main panel --------------------
        mainPanel.add(profilePanel);
        mainPanel.add(Box.createVerticalStrut(30)); // Increased spacer between profile and buttons
        mainPanel.add(buttonsPanel);

        // Add the main panel to the frame
        add(mainPanel);

        // Pack the components to their preferred sizes (now effective for auto-sizing)
        pack();
        // Center the window on the screen
        setLocationRelativeTo(null);
        // Make the window visible
        setVisible(true);
    }

    /**
     * Helper method to create a styled JButton for a cleaner, Classin-like look,
     * now including a subtle lift animation on hover.
     *
     * @param text The text to display on the button.
     * @param bgColor The background color of the button.
     * @return A styled JButton instance.
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Inter", Font.BOLD, 16)); // Slightly larger, bolder font for buttons
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false); // Remove focus border
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25)); // Increased internal padding for a spacious look
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor on hover

        // Custom painting for rounded corners and shadow, now incorporating animation offset
        button.setUI(new ButtonUIWithAnimation()); // Assign our custom UI directly

        // Add mouse listeners for hover animation
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Get the UI object and cast it to our specific ButtonUIWithAnimation class
                Object ui = ((JButton) evt.getSource()).getUI();
                if (ui instanceof ButtonUIWithAnimation customUI) {
                    customUI.startAnimation((JComponent) evt.getSource(), customUI.currentLift, customUI.LIFT_AMOUNT);
                }
                // Also revert background color for a combined effect
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Get the UI object and cast it to our specific ButtonUIWithAnimation class
                Object ui = ((JButton) evt.getSource()).getUI();
                if (ui instanceof ButtonUIWithAnimation customUI) {
                    customUI.startAnimation((JComponent) evt.getSource(), customUI.currentLift, 0); // Return to original position
                }
                button.setBackground(bgColor); // Revert to original background color
            }
        });
        return button;
    }

    /**
     * Helper method to create a temporary placeholder image file.
     * This is used if the local image resource is not found.
     */
    private File createPlaceholderImage(int width, int height, Color bgColor, Color textColor, String text) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Draw background
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, width, height);

        // Draw text
        g2d.setColor(textColor);
        g2d.setFont(new Font("Inter", Font.BOLD, Math.min(width, height) / 4));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (fm.getAscent() + (height - (fm.getAscent() + fm.getDescent())) / 2);
        g2d.drawString(text, x, y);

        g2d.dispose();

        File tempFile = File.createTempFile("placeholder", ".png");
        ImageIO.write(image, "png", tempFile);
        tempFile.deleteOnExit(); // Delete when JVM exits
        return tempFile;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // btnStudentOperations and btnPaymentOperations now use lambda expressions directly,
        // so their actions are handled at their creation and not here.
        if (e.getSource() == btnSettings) {
            // Placeholder for Settings functionality
            JOptionPane.showMessageDialog(this, "Settings functionality coming soon!", "Settings", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == btnLogout) {
            if (this.loginPage != null) {
                LogoutHandler.logout(this, this.loginPage);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error: The login page reference is missing. Cannot log out.",
                        "Logout Error",
                        JOptionPane.ERROR_MESSAGE);
                System.err.println("ProfilePageGUI: loginPage reference is null during logout attempt.");
            }
        }
    }

    /**
     * Inner class to extend BasicButtonUI and provide animation capabilities.
     * This helps in encapsulating the animation state and logic per button.
     * Making it a static nested class to avoid implicit reference to ProfilePageGUI instance.
     * We pass the component (button) explicitly to the startAnimation method.
     */
    public static class ButtonUIWithAnimation extends BasicButtonUI {
        public float currentLift = 0; // Current vertical offset for the lift animation
        public Timer animationTimer; // Timer for smooth animation

        // Animation constants
        public final int LIFT_AMOUNT = -5; // Pixels to lift upwards
        public final int ANIMATION_DURATION = 150; // Milliseconds for animation
        public final int FRAME_RATE = 30; // Frames per second

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = c.getWidth();
            int height = c.getHeight();
            // Increased arc for more rounded buttons
            int arc = 25; // Adjusted from 15 to 25

            // Apply the animation lift
            g2.translate(0, currentLift);

            // Draw subtle shadow - adjust shadow position based on lift
            g2.setColor(new Color(0, 0, 0, 30)); // Light black, semi-transparent
            g2.fillRoundRect(2, 2 - (int)currentLift, width - 4, height - 4, arc, arc);

            // Draw button background
            g2.setColor(c.getBackground());
            g2.fillRoundRect(0, 0, width, height, arc, arc);

            // Draw text
            super.paint(g2, c);
            g2.dispose();
        }

        // Method to start the animation
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
                    // Simple linear interpolation
                    currentLift = startValue + (endValue - startValue) * progress;
                }
                component.repaint(); // Request a repaint for the animation frame
            });
            animationTimer.setInitialDelay(0);
            animationTimer.start();
        }
    }

    // --- Provided RoundedBorder class ---
    public static class RoundedBorder extends AbstractBorder {
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
            // Adjust coordinates to paint border correctly around the round image
            g2d.draw(new RoundRectangle2D.Double(x + borderWidth / 2.0, y + borderWidth / 2.0,
                    width - borderWidth, height - borderWidth, radius * 2, radius * 2)); // Use radius * 2 for arc width/height
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            // Provide sufficient insets for the border
            int inset = radius + borderWidth;
            return new Insets(inset, inset, inset, inset);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = radius + borderWidth;
            return insets;
        }
    }

    // --- Provided RoundImageLabel class ---
    public static class RoundImageLabel extends JLabel {
        private BufferedImage image;
        private float alpha = 0.0f; // For fade-in animation

        public RoundImageLabel(String imagePath, int size) {
            if (imagePath != null) {
                try {
                    BufferedImage originalImage = ImageIO.read(new File(imagePath));
                    if (originalImage != null) {
                        this.image = createRoundImage(originalImage, size);
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error initializing profile image: " + e.getMessage(), "Image Error", JOptionPane.ERROR_MESSAGE);
                    setText("Image Error");
                    setHorizontalAlignment(SwingConstants.CENTER);
                    setVerticalAlignment(SwingConstants.CENTER);
                }
            } else {
                setText("No Image"); // Set text for placeholder case
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

            // Calculate source rectangle to crop the image to a square before scaling
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

            // Create a circular clip
            Area clip = new Area(new Ellipse2D.Double(0, 0, size, size));
            g2d.setComposite(AlphaComposite.SrcOver);
            g2d.setClip(clip);

            // Draw the cropped and scaled image into the circular clip
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
                super.paintComponent(g); // Draw default if no image
            }
        }
    }
}