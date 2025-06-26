import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class tutor extends JFrame {

    private JPanel mainContentPanel;
    // Assuming "defaultTutor" is the tutor for this profile.
    // In a real app, this would come from a login session.
    private String currentTutorUsername = "defaultTutor";

    // Define file paths for consistency
    private static final String TEACHER_PROFILE_FILE = "src/teacherProfile.txt";
    private static final String SUBJECTS_FILE = "src/subjects.txt";
    private static final String ENROLMENT_FILE = "src/enrolment.txt";


    public tutor() {
        setTitle("User Profile & Tutor Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(850, 680));

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(new Color(240, 242, 245));

        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        mainContentPanel.setBackground(new Color(240, 242, 245));

        showProfileSection(); // Display the initial profile section

        rootPanel.add(mainContentPanel, BorderLayout.CENTER);
        add(rootPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Displays the initial profile page content with user details and main navigation buttons.
     */
    private void showProfileSection() {
        mainContentPanel.removeAll(); // Clear previous content

        // Panel to hold the profile information and the quit button
        JPanel topSectionPanel = new JPanel(new BorderLayout());
        topSectionPanel.setBackground(new Color(240, 242, 245)); // Match background or set as desired

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
            protected void paintBorder(Graphics g) {}

            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.setMaximumSize(new Dimension(525, 525));

        RoundImageLabel profilePicLabel = null;
        int imageSize = 128;

        try {
            // Attempt to load from resources first (recommended for deployment)
            URL imageUrl = getClass().getResource("/xiaochou.png");
            File imageFile = null;

            if (imageUrl == null) {
                // Fallback to direct file path if not found as resource (for development)
                // This path assumes xiaochou.png is directly in your project root or accessible path
                String userDir = System.getProperty("user.dir");
                imageFile = new File(userDir, "xiaochou.png");
                if (!imageFile.exists()) {
                    System.err.println("Local image 'xiaochou.png' not found in classpath or project root. Using placeholder.");
                    imageFile = createPlaceholderImage(imageSize, imageSize, new Color(108, 99, 255), Color.WHITE, "Profile");
                }
            } else {
                // If found as a resource, convert URL to URI then to File
                imageFile = new File(imageUrl.toURI());
            }

            if (imageFile != null) {
                profilePicLabel = new RoundImageLabel(imageFile.getAbsolutePath(), imageSize);
            } else {
                // Should not happen if createPlaceholderImage is always successful
                profilePicLabel = new RoundImageLabel(null, imageSize);
            }

            profilePicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            profilePicLabel.setBorder(new RoundedBorder(imageSize / 2, new Color(173, 216, 230), 2));

            RoundImageLabel finalProfilePicLabel = profilePicLabel;
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
            e.printStackTrace(); // Print full stack trace for debugging
            profilePicLabel = new RoundImageLabel(null, imageSize);
            profilePicLabel.setText("Image Error");
            profilePicLabel.setHorizontalAlignment(SwingConstants.CENTER);
            profilePicLabel.setVerticalAlignment(SwingConstants.CENTER);
            profilePicLabel.setPreferredSize(new Dimension(imageSize, imageSize));
            profilePicLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        }

        // Get tutor profile data to display
        String[] tutorData = getTutorProfileData(currentTutorUsername);
        String displayName = currentTutorUsername; // Default
        String displayEmail = currentTutorUsername + "@gmail.com"; // Default
        if (tutorData != null && tutorData.length > 1) {
            displayName = tutorData[1].trim(); // Assuming parts[1] is username/display name
            displayEmail = tutorData[3].trim(); // Assuming parts[3] is email
        }


        JLabel nameLabel = new JLabel(displayName);
        nameLabel.setFont(new Font("Inter", Font.BOLD, 28));
        nameLabel.setForeground(new Color(55, 65, 81));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel(displayEmail);
        emailLabel.setFont(new Font("Inter", Font.PLAIN, 18));
        emailLabel.setForeground(new Color(75, 85, 99));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        profilePanel.add(Box.createVerticalStrut(15));
        if (profilePicLabel != null) {
            profilePanel.add(profilePicLabel);
        }
        profilePanel.add(Box.createVerticalStrut(20));
        profilePanel.add(nameLabel);
        profilePanel.add(Box.createVerticalStrut(8));
        profilePanel.add(emailLabel);
        profilePanel.add(Box.createVerticalStrut(15));

        // Add profile panel to the center of the topSectionPanel
        topSectionPanel.add(profilePanel, BorderLayout.CENTER);

        // --- Quit Button Section (TOP-RIGHT) ---
        JButton quitButton = null;
        try {
            // Load the quit.png image
            URL quitImageUrl = getClass().getResource("/quit.png");
            if (quitImageUrl == null) {
                // Fallback to direct file path if not found as resource (for development)
                String userDir = System.getProperty("user.dir");
                File quitImageFile = new File(userDir, "quit.png");
                if (quitImageFile.exists()) {
                    quitImageUrl = quitImageFile.toURI().toURL();
                }
            }

            if (quitImageUrl != null) {
                ImageIcon quitIcon = new ImageIcon(ImageIO.read(quitImageUrl));
                // Scale the image if necessary, e.g., to 32x32 pixels
                Image scaledQuitImage = quitIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                quitButton = new JButton(new ImageIcon(scaledQuitImage));
            } else {
                quitButton = new JButton("Quit"); // Fallback to text if image not found
                System.err.println("quit.png not found. Using text 'Quit' for button.");
            }

            if (quitButton != null) {
                quitButton.setBorderPainted(false); // Remove button border
                quitButton.setContentAreaFilled(false); // Remove background fill
                quitButton.setFocusPainted(false); // Remove focus border
                quitButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
                quitButton.setToolTipText("Exit Application"); // Tooltip

                quitButton.addActionListener(e -> { // Renamed parameter 'e' to avoid conflict in catch block
                    int confirm = JOptionPane.showConfirmDialog(tutor.this,
                            "Are you sure you want to quit the application?",
                            "Confirm Quit", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        System.exit(0); // Exit the application
                    }
                });

                JPanel quitButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10)); // Align right with padding
                quitButtonPanel.setBackground(new Color(240, 242, 245)); // Match background
                quitButtonPanel.add(quitButton);
                topSectionPanel.add(quitButtonPanel, BorderLayout.EAST); // Add to the right of topSectionPanel
            }

        } catch (IOException ioe) { // Changed 'e' to 'ioe' to resolve conflict
            System.err.println("Error loading quit.png: " + ioe.getMessage());
            ioe.printStackTrace();
            // Handle error, e.g., fall back to a text button
            quitButton = new JButton("Quit"); // Ensure quitButton is initialized even on error
            // Add action listener and style for the fallback text button as well
            quitButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(tutor.this,
                        "Are you sure you want to quit the application?",
                        "Confirm Quit", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0); // Exit the application
                }
            });
            JPanel quitButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            quitButtonPanel.setBackground(new Color(240, 242, 245));
            quitButtonPanel.add(quitButton);
            topSectionPanel.add(quitButtonPanel, BorderLayout.EAST);
        }

        // -------------------- Buttons Section --------------------
        // Use BoxLayout for the main buttons panel to stack rows of buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        buttonsPanel.setBackground(new Color(240, 242, 245));
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the panel itself

        // Create a sub-panel for the top row (Course and Student buttons)
        JPanel topRowButtonsPanel = new JPanel(new GridLayout(1, 2, 20, 20)); // 1 row, 2 columns
        topRowButtonsPanel.setBackground(new Color(240, 242, 245));
        topRowButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topRowButtonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // Allow to grow horizontally

        // 1st button: Courses -> Class Management
        JButton coursesButton = createStyledButton("Course", new Color(24, 144, 255));
        coursesButton.addActionListener(e -> showClassManagementOptions());
        topRowButtonsPanel.add(coursesButton);

        // 2nd button: Messages -> Enrolled Students (Changed from Messages)
        JButton enrolledStudentsButton = createStyledButton("Student", new Color(72, 160, 220));
        enrolledStudentsButton.addActionListener(e -> showViewStudentsPanel()); // Call method to show student view
        topRowButtonsPanel.add(enrolledStudentsButton);

        // Add the top row panel to the main buttons panel
        buttonsPanel.add(topRowButtonsPanel);
        buttonsPanel.add(Box.createVerticalStrut(20)); // Spacing between rows

        // My Profile button now opens Setting frame and spans full width
        JButton myProfileButton = createStyledButton("My Profile", new Color(72, 160, 220));
        myProfileButton.addActionListener(e -> new Setting(currentTutorUsername).setVisible(true)); // Pass currentTutorUsername
        myProfileButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button
        myProfileButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, myProfileButton.getPreferredSize().height)); // Make it span horizontally

        buttonsPanel.add(myProfileButton);

        // Add the combined top section to the main content panel first
        mainContentPanel.add(topSectionPanel);
        mainContentPanel.add(Box.createVerticalStrut(30));
        mainContentPanel.add(buttonsPanel);

        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void showClassManagementOptions() {
        mainContentPanel.removeAll();

        JPanel classManagementPanel = new JPanel(new BorderLayout(20, 20));
        classManagementPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        classManagementPanel.setBackground(new Color(240, 242, 245));

        JLabel titleLabel = new JLabel("Class Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 32));
        titleLabel.setForeground(new Color(55, 65, 81));
        classManagementPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonGridPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        buttonGridPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonGridPanel.setBackground(new Color(240, 242, 245));

        JButton addClassButton = createStyledButton("Add New Class", new Color(51, 153, 255));
        addClassButton.addActionListener(e -> new AddClassFrame(currentTutorUsername).setVisible(true));
        buttonGridPanel.add(addClassButton);

        JButton viewClassesButton = createStyledButton("View My Classes", new Color(51, 153, 255));
        viewClassesButton.addActionListener(e -> showViewClassesPanel());
        buttonGridPanel.add(viewClassesButton);

        JButton updateClassButton = createStyledButton("Update Class", new Color(51, 153, 255));
        updateClassButton.addActionListener(e -> new UpdateClassFrame(currentTutorUsername).setVisible(true));
        buttonGridPanel.add(updateClassButton);

        JButton deleteClassButton = createStyledButton("Delete Class", new Color(51, 153, 255));
        deleteClassButton.addActionListener(e -> new DeleteClassFrame(currentTutorUsername).setVisible(true));
        buttonGridPanel.add(deleteClassButton);

        classManagementPanel.add(buttonGridPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Profile");
        backButton.setFont(new Font("Inter", Font.PLAIN, 16));
        backButton.setBackground(new Color(100, 116, 139));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setUI(new ButtonUIWithAnimation());
        backButton.addActionListener(e -> showProfileSection());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(240, 242, 245));
        bottomPanel.add(backButton);
        classManagementPanel.add(bottomPanel, BorderLayout.SOUTH);

        mainContentPanel.add(classManagementPanel);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    /**
     * Displays the View Classes panel within the main content area.
     */
    private void showViewClassesPanel() {
        mainContentPanel.removeAll();

        JPanel viewClassesContainerPanel = new JPanel(new BorderLayout(10, 10));
        viewClassesContainerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        viewClassesContainerPanel.setBackground(new Color(240, 242, 245));

        JLabel titleLabel = new JLabel("My Classes", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 28));
        titleLabel.setForeground(new Color(55, 65, 81));
        viewClassesContainerPanel.add(titleLabel, BorderLayout.NORTH);

        ViewClassesPanel viewClassesPanel = new ViewClassesPanel(currentTutorUsername);
        viewClassesContainerPanel.add(viewClassesPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Class Management Options");
        backButton.setFont(new Font("Inter", Font.PLAIN, 14));
        backButton.setBackground(new Color(100, 116, 139));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setUI(new ButtonUIWithAnimation());
        backButton.addActionListener(e -> showClassManagementOptions());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(240, 242, 245));
        bottomPanel.add(backButton);
        viewClassesContainerPanel.add(bottomPanel, BorderLayout.SOUTH);

        mainContentPanel.add(viewClassesContainerPanel);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void showViewStudentsPanel() {
        mainContentPanel.removeAll(); // Clear current content

        JPanel viewStudentsContainerPanel = new JPanel(new BorderLayout(10, 10));
        viewStudentsContainerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        viewStudentsContainerPanel.setBackground(new Color(240, 242, 245));

        JLabel titleLabel = new JLabel("Enrolled Students", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 28));
        titleLabel.setForeground(new Color(55, 65, 81));
        viewStudentsContainerPanel.add(titleLabel, BorderLayout.NORTH);

        // Add the new ViewStudentsPanel
        ViewStudentsPanel viewStudentsPanel = new ViewStudentsPanel(currentTutorUsername);
        viewStudentsContainerPanel.add(viewStudentsPanel, BorderLayout.CENTER);

        // Back button
        JButton backButton = new JButton("Back to Profile");
        backButton.setFont(new Font("Inter", Font.PLAIN, 14));
        backButton.setBackground(new Color(100, 116, 139));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setUI(new ButtonUIWithAnimation());
        backButton.addActionListener(e -> showProfileSection()); // Return to main profile

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(240, 242, 245));
        bottomPanel.add(backButton);
        viewStudentsContainerPanel.add(bottomPanel, BorderLayout.SOUTH);

        mainContentPanel.add(viewStudentsContainerPanel);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Inter", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setUI(new ButtonUIWithAnimation()); // Apply custom UI for animation
        return button;
    }

    /**
     * Helper method to read tutor profile data from teacherProfile.txt.
     * @param username The username to search for.
     * @return An array of strings representing the tutor's data, or null if not found.
     * Format: ID,Username,ContactNumber,Email,Address,Subject
     */
    private String[] getTutorProfileData(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(TEACHER_PROFILE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1); // -1 to keep trailing empty strings
                if (parts.length >= 2 && parts[1].trim().equals(username)) { // Match by username (parts[1])
                    return parts;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading teacher profile data: " + e.getMessage());
            // In a real application, you might show a dialog or log this more formally
        }
        return null;
    }


    /**
     * Helper method to create a temporary placeholder image file.
     * This is used if the local image resource is not found.
     */
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

    /**
     * Inner class to extend BasicButtonUI and provide animation capabilities.
     */
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

    // --- Provided RoundedBorder class ---
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

    // --- Provided RoundImageLabel class ---
    private static class RoundImageLabel extends JLabel {
        private BufferedImage image;
        private float alpha = 0.0f;

        public RoundImageLabel(String imagePath, int size) {
            if (imagePath != null) {
                try {
                    // Try to load as resource first
                    URL imageUrl = getClass().getResource(imagePath.startsWith("/") ? imagePath : "/" + imagePath);
                    BufferedImage originalImage = null;

                    if (imageUrl != null) {
                        originalImage = ImageIO.read(imageUrl);
                    } else {
                        // Fallback to direct file path
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new tutor();
        });
    }

    // =====================================================================
    // CLASSES EXTRACTED/ADAPTED FROM tutor.java (continued)
    // NOTE: These are now static inner classes for better encapsulation.
    // =====================================================================

    static class TutorData {
        private String username;
        private String password;
        private List<String> subjects;

        public TutorData(String username, String password) {
            this.username = username;
            this.password = password;
            this.subjects = new ArrayList<>();
        }

        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public List<String> getSubjects() { return subjects; }

        public void addSubject(String subject) {
            subjects.add(subject);
        }
    }

    static class ViewClassesPanel extends JPanel {
        private String currentTutor;
        private JComboBox<String> classFormLevelSelector;
        private JTable classTable;
        private DefaultTableModel classTableModel;

        public ViewClassesPanel(String tutorUsername) {
            this.currentTutor = tutorUsername;
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
            filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Classes"));
            String[] formLevels = {"All Forms", "Form 1", "Form 2", "Form 3", "Form 4", "Form 5"};
            classFormLevelSelector = new JComboBox<>(formLevels);
            classFormLevelSelector.setSelectedItem("All Forms");
            JButton applyFilterButton = new JButton("Apply Filter");

            filterPanel.add(new JLabel("Select Form Level:"));
            filterPanel.add(classFormLevelSelector);
            filterPanel.add(applyFilterButton);

            add(filterPanel, BorderLayout.NORTH);

            String[] columnNames = {"Subject", "Form Level", "Charges (RM)", "Schedule"};
            classTableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            classTable = new JTable(classTableModel);
            classTable.setFillsViewportHeight(true);
            JScrollPane scrollPane = new JScrollPane(classTable);

            add(scrollPane, BorderLayout.CENTER);

            applyFilterButton.addActionListener(e -> displayFilteredClasses((String) classFormLevelSelector.getSelectedItem()));

            displayFilteredClasses("All Forms");
        }

        private void displayFilteredClasses(String filterFormLevel) {
            classTableModel.setRowCount(0);

            try (BufferedReader br = new BufferedReader(new FileReader(SUBJECTS_FILE))) {
                String line;
                boolean found = false;

                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5 && parts[4].trim().equals(currentTutor)) {
                        if (filterFormLevel.equals("All Forms") || parts[1].trim().equals(filterFormLevel)) {
                            classTableModel.addRow(new Object[]{parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()});
                            found = true;
                        }
                    }
                }

                if (!found && classTableModel.getRowCount() == 0) {
                    classTableModel.addRow(new Object[]{"No classes found for this tutor.", "", "", ""});
                }

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading class data: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                classTableModel.addRow(new Object[]{"Error loading data.", "", "", ""});
                e.printStackTrace();
            }
        }
    }

    static class ViewStudentsPanel extends JPanel {
        private String currentTutor;
        private JTable studentTable;
        private DefaultTableModel studentTableModel;
        private JComboBox<String> formLevelFilterComboBox;

        public ViewStudentsPanel(String tutorUsername) {
            this.currentTutor = tutorUsername;
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
            filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Enrolled Students"));
            String[] formLevels = {"All Forms", "Form 1", "Form 2", "Form 3", "Form 4", "Form 5"};
            formLevelFilterComboBox = new JComboBox<>(formLevels);
            formLevelFilterComboBox.setSelectedItem("All Forms");
            JButton applyFilterButton = new JButton("Apply Filter");

            filterPanel.add(new JLabel("Select Form Level:"));
            filterPanel.add(formLevelFilterComboBox);
            filterPanel.add(applyFilterButton);

            add(filterPanel, BorderLayout.NORTH);

            String[] columnNames = {"Student ID", "Student Name", "Form", "Subjects", "Tutor Name"};
            studentTableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            studentTable = new JTable(studentTableModel) {
                @Override
                public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                    Component component = super.prepareRenderer(renderer, row, column);
                    if (component instanceof JLabel) {
                        ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    return component;
                }
            };

            // Center align header
            DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) studentTable.getTableHeader().getDefaultRenderer();
            headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

            // Set word wrap and row height for multi-line content
            studentTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus, int row, int column) {
                    JTextArea textArea = new JTextArea();
                    textArea.setText(value != null ? value.toString() : "");
                    textArea.setWrapStyleWord(true);
                    textArea.setLineWrap(true);
                    textArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
                    textArea.setOpaque(true);

                    if (isSelected) {
                        textArea.setBackground(table.getSelectionBackground());
                        textArea.setForeground(table.getSelectionForeground());
                    } else {
                        textArea.setBackground(table.getBackground());
                        textArea.setForeground(table.getForeground());
                    }

                    // Center align text
                    textArea.setAlignmentX(Component.CENTER_ALIGNMENT);

                    return textArea;
                }
            });

            // Set row height to accommodate multiple lines
            studentTable.setRowHeight(60);

            // Auto-resize columns to fit content
            studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            for (int i = 0; i < studentTable.getColumnCount(); i++) {
                TableColumn column = studentTable.getColumnModel().getColumn(i);
                column.setPreferredWidth(150); // Default width
            }
            // Make Subjects column wider
            studentTable.getColumnModel().getColumn(3).setPreferredWidth(300);

            JScrollPane scrollPane = new JScrollPane(studentTable);
            add(scrollPane, BorderLayout.CENTER);

            applyFilterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    displayStudents((String) formLevelFilterComboBox.getSelectedItem());
                }
            });

            displayStudents("All Forms");
        }

        private String[] parseCsvLine(String line) {
            List<String> parts = new ArrayList<>();
            StringBuilder currentPart = new StringBuilder();
            boolean inQuote = false;

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);

                if (c == '"') {
                    inQuote = !inQuote;
                } else if (c == ',' && !inQuote) {
                    parts.add(currentPart.toString().trim());
                    currentPart = new StringBuilder();
                } else {
                    currentPart.append(c);
                }
            }
            parts.add(currentPart.toString().trim());
            return parts.toArray(new String[0]);
        }

        private void displayStudents(String filterFormLevel) {
            studentTableModel.setRowCount(0);

            try (BufferedReader br = new BufferedReader(new FileReader(ENROLMENT_FILE))) {
                String line;
                boolean found = false;

                while ((line = br.readLine()) != null) {
                    String[] parts = parseCsvLine(line);

                    if (parts.length >= 9 && parts[8].equals(currentTutor)) {
                        String studentForm = parts[5];

                        if (filterFormLevel.equals("All Forms") || studentForm.equals(filterFormLevel)) {
                            studentTableModel.addRow(new Object[]{
                                    parts[0], // Student ID
                                    parts[1], // Student Name
                                    parts[5], // Form
                                    parts[6], // Subjects
                                    parts[8]  // Tutor Name
                            });
                            found = true;
                        }
                    }
                }

                if (!found && studentTableModel.getRowCount() == 0) {
                    studentTableModel.addRow(new Object[]{"No students found for this tutor and form.", "", "", "", ""});
                }

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading student data: " + e.getMessage(),
                        "File Read Error", JOptionPane.ERROR_MESSAGE);
                studentTableModel.addRow(new Object[]{"Error loading data.", "", "", "", ""});
                e.printStackTrace();
            }
        }
    }

    static class AddClassFrame extends JFrame {
        private JTextField subjectField, chargesField, scheduleField;
        private JComboBox<String> formLevelComboBox;
        private String tutorUsername;

        public AddClassFrame(String tutorUsername) {
            this.tutorUsername = tutorUsername;
            setTitle("Add New Class");
            setSize(450, 350);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.WEST;

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            JLabel titleLabel = new JLabel("Add New Class", JLabel.CENTER);
            titleLabel.setFont(new Font("Inter", Font.BOLD, 20));
            panel.add(titleLabel, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            panel.add(new JLabel("Subject Name:"), gbc);

            gbc.gridx = 1;
            subjectField = new JTextField(15);
            panel.add(subjectField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(new JLabel("Form Level:"), gbc);

            gbc.gridx = 1;
            String[] formLevels = {"Form 1", "Form 2", "Form 3", "Form 4", "Form 5"};
            formLevelComboBox = new JComboBox<>(formLevels);
            panel.add(formLevelComboBox, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(new JLabel("Charges (RM):"), gbc);

            gbc.gridx = 1;
            chargesField = new JTextField(15);
            panel.add(chargesField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            panel.add(new JLabel("Schedule:"), gbc);

            gbc.gridx = 1;
            scheduleField = new JTextField(15);
            panel.add(scheduleField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            JButton addButton = new JButton("Add Class");
            addButton.addActionListener(e -> addClass());
            panel.add(addButton, gbc);

            add(panel);
        }

        private void addClass() {
            String subject = subjectField.getText().trim();
            String formLevel = (String) formLevelComboBox.getSelectedItem();
            String charges = chargesField.getText().trim();
            String schedule = scheduleField.getText().trim();

            if (subject.isEmpty() || formLevel == null || charges.isEmpty() || schedule.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Double.parseDouble(charges);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Charges must be a valid number",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (FileWriter fw = new FileWriter(SUBJECTS_FILE, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(subject + "," + formLevel + "," + charges + "," + schedule + "," + tutorUsername + "\n");
                JOptionPane.showMessageDialog(this, "Class added successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving class data: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    static class UpdateClassFrame extends JFrame {
        private JComboBox<String> classComboBox;
        private JTextField subjectField, chargesField, scheduleField;
        private JComboBox<String> formLevelComboBox;
        private String tutorUsername;
        private List<String[]> classes;
        private JPanel panel;

        // Store the original subject and form level of the currently selected class
        private String originalSelectedSubject;
        private String originalSelectedFormLevel;


        public UpdateClassFrame(String tutorUsername) {
            this.tutorUsername = tutorUsername;
            setTitle("Update Class");
            setSize(500, 450);
            setLocationRelativeTo(null);

            classes = new ArrayList<>();

            panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.WEST;

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            JLabel titleLabel = new JLabel("Update Class", JLabel.CENTER);
            titleLabel.setFont(new Font("Inter", Font.BOLD, 20));
            panel.add(titleLabel, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            panel.add(new JLabel("Select Class:"), gbc);

            gbc.gridx = 1;
            classComboBox = new JComboBox<>();
            // Removed the immediate action listener addition here, add it after all components are initialized
            panel.add(classComboBox, gbc);


            // Initialize text fields and form level combo box first
            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(new JLabel("Subject Name:"), gbc);

            gbc.gridx = 1;
            subjectField = new JTextField(15);
            panel.add(subjectField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(new JLabel("Form Level:"), gbc);

            gbc.gridx = 1;
            String[] formLevels = {"Form 1", "Form 2", "Form 3", "Form 4", "Form 5"};
            formLevelComboBox = new JComboBox<>(formLevels);
            panel.add(formLevelComboBox, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            panel.add(new JLabel("Charges (RM):"), gbc);

            gbc.gridx = 1;
            chargesField = new JTextField(15);
            panel.add(chargesField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            panel.add(new JLabel("Schedule:"), gbc);

            gbc.gridx = 1;
            scheduleField = new JTextField(15);
            panel.add(scheduleField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            JButton updateButton = new JButton("Update Class");
            updateButton.addActionListener(e -> updateClass());
            panel.add(updateButton, gbc);

            add(panel);

            // NOW add the action listener to the JComboBox, AFTER all fields are initialized
            classComboBox.addActionListener(e -> loadClassDetails());

            // Load classes initially
            loadClasses();

            // Load details for the first class if available, after components are added
            // And only if there are actual classes loaded (not "No classes found")
            if (classComboBox.getItemCount() > 0 && classComboBox.getSelectedItem() != null && !classComboBox.getSelectedItem().equals("No classes found")) {
                loadClassDetails();
            }
        }

        private void loadClasses() {
            classComboBox.removeAllItems();
            classes.clear();

            try (BufferedReader br = new BufferedReader(new FileReader(SUBJECTS_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    // Ensure line has enough parts and belongs to the current tutor
                    if (parts.length >= 5 && parts[4].trim().equals(tutorUsername)) {
                        classComboBox.addItem(parts[0].trim() + " (" + parts[1].trim() + ")");
                        classes.add(parts); // Store the entire parts array for later use
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading class data for loading: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

            JButton updateButton = null;
            for (Component comp : panel.getComponents()) {
                if (comp instanceof JButton && ((JButton)comp).getText().equals("Update Class")) {
                    updateButton = (JButton)comp;
                    break;
                }
            }

            // Handle case where no classes are found
            if (classComboBox.getItemCount() == 0) {
                classComboBox.addItem("No classes found");
                // Only attempt to set text/selection if fields are not null (already initialized)
                if (subjectField != null) subjectField.setText("");
                if (formLevelComboBox != null) formLevelComboBox.setSelectedIndex(-1); // Clear selection
                if (chargesField != null) chargesField.setText("");
                if (scheduleField != null) scheduleField.setText("");

                // Disable input fields and update button (check for null before enabling/disabling)
                if (subjectField != null) subjectField.setEnabled(false);
                if (formLevelComboBox != null) formLevelComboBox.setEnabled(false);
                if (chargesField != null) chargesField.setEnabled(false);
                if (scheduleField != null) scheduleField.setEnabled(false);

                if (updateButton != null) {
                    updateButton.setEnabled(false);
                }
            } else {
                // Enable fields and button if classes are present (check for null before enabling/disabling)
                if (subjectField != null) subjectField.setEnabled(true);
                if (formLevelComboBox != null) formLevelComboBox.setEnabled(true);
                if (chargesField != null) chargesField.setEnabled(true);
                if (scheduleField != null) scheduleField.setEnabled(true);
                if (updateButton != null) {
                    updateButton.setEnabled(true);
                }
            }
        }

        private void loadClassDetails() {
            int selectedIndex = classComboBox.getSelectedIndex();
            // If "No classes found" is selected or no item is selected
            if (selectedIndex < 0 || selectedIndex >= classes.size() || (classComboBox.getSelectedItem() != null && classComboBox.getSelectedItem().equals("No classes found"))) {
                // Only attempt to set text/selection if fields are not null
                if (subjectField != null) subjectField.setText("");
                if (formLevelComboBox != null) formLevelComboBox.setSelectedIndex(-1);
                if (chargesField != null) chargesField.setText("");
                if (scheduleField != null) scheduleField.setText("");
                originalSelectedSubject = null; // Clear original selection markers
                originalSelectedFormLevel = null;
                return;
            }

            String[] classData = classes.get(selectedIndex);
            // Only attempt to set text/selection if fields are not null
            if (subjectField != null) subjectField.setText(classData[0].trim());
            if (formLevelComboBox != null) formLevelComboBox.setSelectedItem(classData[1].trim());
            if (chargesField != null) chargesField.setText(classData[2].trim());
            if (scheduleField != null) scheduleField.setText(classData[3].trim());

            // Store the original subject and form level for identification during update
            originalSelectedSubject = classData[0].trim();
            originalSelectedFormLevel = classData[1].trim();
        }

        private void updateClass() {
            int selectedIndex = classComboBox.getSelectedIndex();
            if (selectedIndex < 0 || selectedIndex >= classes.size() || (classComboBox.getSelectedItem() != null && classComboBox.getSelectedItem().equals("No classes found"))) {
                JOptionPane.showMessageDialog(this, "Please select a valid class to update.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String newSubject = subjectField.getText().trim();
            String newFormLevel = (String) formLevelComboBox.getSelectedItem();
            String newCharges = chargesField.getText().trim();
            String newSchedule = scheduleField.getText().trim();

            if (newSubject.isEmpty() || newFormLevel == null || newCharges.isEmpty() || newSchedule.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Double.parseDouble(newCharges);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Charges must be a valid number.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Use the stored original values to find the exact line to update
            if (originalSelectedSubject == null || originalSelectedFormLevel == null) {
                JOptionPane.showMessageDialog(this, "Error: Could not determine original class details for update.",
                        "Internal Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String updatedClassLine = newSubject + "," + newFormLevel + "," + newCharges + "," + newSchedule + "," + tutorUsername;

            try {
                // Initialize allLines here
                List<String> allFileLines = new ArrayList<>();
                try (BufferedReader br = new BufferedReader(new FileReader(SUBJECTS_FILE))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        allFileLines.add(line);
                    }
                }

                List<String> finalLinesToWrite = new ArrayList<>();
                boolean updated = false;

                for (String fileLine : allFileLines) {
                    String[] parts = fileLine.split(",");
                    // Check if this line matches the ORIGINAL selected class by subject, form level, AND tutor
                    if (parts.length >= 5 &&
                            parts[0].trim().equals(originalSelectedSubject) &&
                            parts[1].trim().equals(originalSelectedFormLevel) &&
                            parts[4].trim().equals(tutorUsername) &&
                            !updated) { // Only update the first matching line
                        finalLinesToWrite.add(updatedClassLine);
                        updated = true;
                    } else {
                        finalLinesToWrite.add(fileLine);
                    }
                }

                if (!updated) {
                    JOptionPane.showMessageDialog(this, "Original class not found in file. Update failed.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (FileWriter fw = new FileWriter(SUBJECTS_FILE);
                     BufferedWriter bw = new BufferedWriter(fw)) {
                    for (String line : finalLinesToWrite) {
                        bw.write(line + "\n");
                    }
                }

                JOptionPane.showMessageDialog(this, "Class updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Close the update frame after successful update
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error updating class data: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    static class DeleteClassFrame extends JFrame {
        private JComboBox<String> classComboBox;
        private String tutorUsername;
        private List<String[]> classes;
        private JPanel panel;

        public DeleteClassFrame(String tutorUsername) {
            this.tutorUsername = tutorUsername;
            setTitle("Delete Class");
            setSize(400, 200);
            setLocationRelativeTo(null);

            classes = new ArrayList<>();

            panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.WEST;

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            JLabel titleLabel = new JLabel("Delete Class", JLabel.CENTER);
            titleLabel.setFont(new Font("Inter", Font.BOLD, 20));
            panel.add(titleLabel, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            panel.add(new JLabel("Select Class:"), gbc);

            gbc.gridx = 1;
            classComboBox = new JComboBox<>();
            loadClasses();
            panel.add(classComboBox, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            JButton deleteButton = new JButton("Delete Class");
            deleteButton.addActionListener(e -> deleteClass());
            panel.add(deleteButton, gbc);

            add(panel);
        }

        private void loadClasses() {
            classComboBox.removeAllItems();
            classes.clear();

            try (BufferedReader br = new BufferedReader(new FileReader(SUBJECTS_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5 && parts[4].trim().equals(tutorUsername)) {
                        classComboBox.addItem(parts[0].trim() + " (" + parts[1].trim() + ")");
                        classes.add(parts);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading class data for deletion: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

            JButton deleteButton = null;
            for (Component comp : panel.getComponents()) {
                if (comp instanceof JButton && ((JButton)comp).getText().equals("Delete Class")) {
                    deleteButton = (JButton)comp;
                    break;
                }
            }

            if (classComboBox.getItemCount() == 0) {
                classComboBox.addItem("No classes found");
                if (deleteButton != null) {
                    deleteButton.setEnabled(false);
                }
            } else {
                if (deleteButton != null) {
                    deleteButton.setEnabled(true);
                }
            }
        }

        private void deleteClass() {
            int selectedIndex = classComboBox.getSelectedIndex();
            if (selectedIndex < 0 || selectedIndex >= classes.size() || (classComboBox.getSelectedItem() != null && classComboBox.getSelectedItem().equals("No classes found"))) {
                JOptionPane.showMessageDialog(this, "Please select a valid class to delete.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this class?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                String[] classToDelete = classes.get(selectedIndex);
                String subjectToDelete = classToDelete[0].trim();
                String formToDelete = classToDelete[1].trim();
                String tutorOfClass = classToDelete[4].trim(); // This is the tutor of the class entry in the file

                List<String> remainingLines = new ArrayList<>();
                try (BufferedReader br = new BufferedReader(new FileReader(SUBJECTS_FILE))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 5) {
                            // Only remove the line if ALL identifiers match: subject, form, AND tutor
                            if (!(parts[0].trim().equals(subjectToDelete) &&
                                    parts[1].trim().equals(formToDelete) &&
                                    parts[4].trim().equals(tutorOfClass))) {
                                remainingLines.add(line);
                            }
                        } else {
                            // Add lines that don't conform to expected format (though ideally all should)
                            remainingLines.add(line);
                        }
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error reading class data during deletion: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return;
                }

                try (FileWriter fw = new FileWriter(SUBJECTS_FILE);
                     BufferedWriter bw = new BufferedWriter(fw)) {
                    for (String line : remainingLines) {
                        bw.write(line + "\n");
                    }
                    JOptionPane.showMessageDialog(this, "Class deleted successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error writing class data after deletion: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * The Setting class allows a tutor to view and update their profile details.
     * It loads initial data from 'teacherProfile.txt' and saves changes back to it.
     */
    private static class Setting extends JFrame {
        // --- JLabels for field names ---
        JLabel nameLabel = new JLabel("Name:");
        JLabel usernameLabel = new JLabel("Username:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel contactNumberLabel = new JLabel("Contact Number:");
        JLabel addressLabel = new JLabel("Address:");
        JLabel subjectLabel = new JLabel("Subject:"); // Non-editable

        // --- JTextFields for editable fields and JLabels for display ---
        JLabel nameValueLabel; // Displays tutor's actual name/username
        JTextField usernameField; // Editable username
        JLabel emailValueLabel; // Displays tutor's email
        JTextField contactNumberField; // Editable contact number
        JTextField addressField; // Editable address
        JLabel subjectValueLabel; // Displayed from teacherProfile.txt

        RoundImageLabel profilePictureLabel;
        private String tutorUsername; // Store the original username passed

        // Store original data to manage updates
        private String originalId;
        private String originalUsername;
        private String originalContact;
        private String originalEmail;
        private String originalAddress;
        private String originalSubject;


        public Setting(String tutorUsername) {
            this.tutorUsername = tutorUsername; // Store the passed username

            setTitle("User Profile Settings");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
            setSize(400, 500); // Adjusted size for fewer fields
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            // --- Load existing tutor data ---
            loadTutorData();

            // Initialize fields using the loaded data
            nameValueLabel = new JLabel(originalUsername != null ? originalUsername : "N/A");
            usernameField = new JTextField(originalUsername != null ? originalUsername : "", 20);
            emailValueLabel = new JLabel(originalEmail != null ? originalEmail : (this.tutorUsername + "@gmail.com"));
            contactNumberField = new JTextField(originalContact != null ? originalContact : "", 20);
            addressField = new JTextField(originalAddress != null ? originalAddress : "", 20);
            subjectValueLabel = new JLabel(originalSubject != null ? originalSubject : "N/A");


            // --- Profile Picture Section (NORTH) ---
            try {
                URL imageUrl = getClass().getResource("/xiaochou.png");
                File imageFile = null;

                if (imageUrl == null) {
                    String userDir = System.getProperty("user.dir");
                    imageFile = new File(userDir, "xiaochou.png");
                    if (!imageFile.exists()) {
                        System.err.println("Local image 'xiaochou.png' not found in classpath or project root. Using placeholder.");
                        imageFile = createPlaceholderImage(160, 160, new Color(108, 99, 255), Color.WHITE, "Settings");
                    }
                } else {
                    imageFile = new File(imageUrl.toURI());
                }

                if (imageFile != null) {
                    profilePictureLabel = new RoundImageLabel(imageFile.getAbsolutePath(), 160);
                } else {
                    profilePictureLabel = new RoundImageLabel(null, 160);
                    profilePictureLabel.setText("Image Error");
                }
            } catch (Exception e) {
                System.err.println("Error initializing settings profile image: " + e.getMessage());
                e.printStackTrace();
                profilePictureLabel = new RoundImageLabel(null, 160);
                profilePictureLabel.setText("Image Error");
            }

            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            headerPanel.add(profilePictureLabel);
            add(headerPanel, BorderLayout.NORTH);

            // --- Form Section (CENTER) ---
            JPanel innerFormPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            innerFormPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Add components to the panel based on teacherProfile.txt format
            innerFormPanel.add(nameLabel);
            innerFormPanel.add(nameValueLabel); // Non-editable display (maps to originalUsername)

            innerFormPanel.add(usernameLabel);
            innerFormPanel.add(usernameField); // Editable

            innerFormPanel.add(emailLabel);
            innerFormPanel.add(emailValueLabel); // Non-editable display (maps to originalEmail)

            innerFormPanel.add(contactNumberLabel);
            innerFormPanel.add(contactNumberField); // Editable

            innerFormPanel.add(addressLabel);
            innerFormPanel.add(addressField); // Editable

            innerFormPanel.add(subjectLabel);
            innerFormPanel.add(subjectValueLabel); // Non-editable display (maps to originalSubject)

            // No JScrollPane needed as the content is now shorter and directly fits
            add(innerFormPanel, BorderLayout.CENTER); // Add the form panel directly to the frame's center

            // --- Button Section (SOUTH) ---
            JButton saveButton = new JButton("Save Profile");
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(saveButton);
            add(buttonPanel, BorderLayout.SOUTH);

            saveButton.addActionListener(e -> saveProfile());

            setVisible(true);
        }

        /**
         * Loads the current tutor's profile data from teacherProfile.txt.
         */
        private void loadTutorData() {
            try (BufferedReader br = new BufferedReader(new FileReader(TEACHER_PROFILE_FILE))) {
                String line;
                boolean found = false;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",", -1); // -1 to keep trailing empty strings
                    if (parts.length >= 6 && parts[1].trim().equals(tutorUsername)) {
                        originalId = parts[0].trim();
                        originalUsername = parts[1].trim();
                        originalContact = parts[2].trim();
                        originalEmail = parts[3].trim();
                        originalAddress = parts[4].trim();
                        originalSubject = parts[5].trim();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    JOptionPane.showMessageDialog(this, "Profile data for '" + tutorUsername + "' not found.",
                            "Load Error", JOptionPane.WARNING_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading profile data: " + e.getMessage() +
                                "\nPlease ensure 'teacherProfile.txt' exists in the 'src/' directory.",
                        "File Load Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        /**
         * Saves the updated profile data back to teacherProfile.txt.
         */
        private void saveProfile() {
            String newUsername = usernameField.getText().trim();
            String newContact = contactNumberField.getText().trim();
            String newAddress = addressField.getText().trim();
            String newEmail = newUsername + "@gmail.com"; // Email is derived from username

            // Validate editable fields
            if (newUsername.isEmpty() || newContact.isEmpty() || newAddress.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username, Contact Number, and Address cannot be empty.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<String> allLines = new ArrayList<>();
            boolean updated = false;

            try (BufferedReader br = new BufferedReader(new FileReader(TEACHER_PROFILE_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 6 && parts[1].trim().equals(tutorUsername) && !updated) {
                        // This is the line to update
                        // Format: ID,Username,ContactNumber,Email,Address,Subject
                        // Use originalId and originalSubject (non-editable)
                        String updatedLine = originalId + "," +
                                newUsername + "," +
                                newContact + "," +
                                newEmail + "," + // Update email based on new username
                                newAddress + "," +
                                originalSubject; // Keep original subject
                        allLines.add(updatedLine);
                        updated = true;
                    } else {
                        allLines.add(line);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading profile data during save: " + e.getMessage(),
                        "File Access Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }

            if (!updated) {
                JOptionPane.showMessageDialog(this, "Original profile for '" + tutorUsername + "' not found. Cannot save.",
                        "Save Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (FileWriter fw = new FileWriter(TEACHER_PROFILE_FILE); // Overwrite the file
                 BufferedWriter bw = new BufferedWriter(fw)) {
                for (String line : allLines) {
                    bw.write(line + "\n");
                }
                JOptionPane.showMessageDialog(this, "Profile updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                // Update the currentTutorUsername in the main GUI if username changed
                if (!newUsername.equals(tutorUsername)) {
                    // This is a bit tricky as currentTutorUsername is in the outer class.
                    // For simplicity, we'll just close the settings frame and let the main GUI refresh if needed.
                    // A more robust solution might involve callbacks or a shared model.
                    // For now, prompt the user to restart or notify them the change will appear on next load.
                    JOptionPane.showMessageDialog(this, "Username changed. Please restart the application to see full changes reflected.",
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                }
                this.dispose(); // Close the settings window
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error writing profile data: " + e.getMessage(),
                        "File Write Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }


        // Helper method (copied from ProfilePageGUI, needs to be static if inside Setting)
        private static File createPlaceholderImage(int width, int height, Color bgColor, Color textColor, String text) throws IOException {
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
    }
}
