package StudentApp;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.plaf.basic.BasicButtonUI;
import java.util.Map;
import StudentApp.model.SStudent;
import common.auth.MainLoginPageTest; // Ensure this import is present
import common.ui.RoundedBorder;
import StudentApp.SReadNotification; // NEW: Import SReadNotification

public class SProfilePageGUI extends JFrame {

    private SStudent loggedInSStudent;
    private MainLoginPageTest mainLoginPage; // NEW: Field to store MainLoginPageTest reference

    // File constants (copied from Setting.java for consistency)
    private static final String STUDENTS_FILE = "students.txt";
    private static final String STUDENT_ENROLLMENT_FILE = "student_enrollment.txt";


    // JLabels that need to be updated
    private JLabel nameLabel;
    private JLabel emailLabel;
    private RoundImageLabel profilePicLabel; // Also keep a reference to update if needed

    // MODIFIED: Constructor now accepts MainLoginPageTest AND studentLoginUsername
    public SProfilePageGUI(MainLoginPageTest mainLoginPage, String studentLoginUsername) {
        this.mainLoginPage = mainLoginPage; // Store the reference
        setTitle("User Profile Application");
        // NEW: Set default close operation to DO_NOTHING_ON_CLOSE initially.
        // This gives us full control within the windowClosing event.
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(550, 630));

        // Add a WindowListener to handle the close operation
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        SProfilePageGUI.this,
                        "Are you sure you want to quit the program?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    dispose(); // Close the current profile frame
                    if (mainLoginPage != null) { // Check if the login page reference exists
                        mainLoginPage.setVisible(true); // Make the login page visible
                        System.out.println("SProfilePageGUI: Returning to MainLoginPageTest."); // Debug print
                    } else {
                        System.out.println("SProfilePageGUI: MainLoginPageTest reference is null, exiting application.");
                        System.exit(0); // Fallback: exit if no parent login page
                    }
                }
            }
        });


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        // MODIFIED: Adjusted top border to give space for the notification button
        mainPanel.setBorder(new EmptyBorder(10, 40, 40, 40));
        mainPanel.setBackground(new Color(240, 242, 245));

        // Initialize labels (will be updated by loadAndRefreshStudentData)
        nameLabel = new JLabel("Loading Name...");
        emailLabel = new JLabel("Loading Email...");

        // -------------------- Notification Button & Profile Section Wrapper (NEW) --------------------
        // This panel will contain the notification button (top-right) and the profile panel (center)
        JPanel topWrapperPanel = new JPanel(new BorderLayout());
        topWrapperPanel.setOpaque(false); // Make it transparent
        // Add some internal padding at the bottom of this wrapper to separate from buttonsPanel
        topWrapperPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Create the notification button
        JButton notificationButton = new JButton(); // Create button without text
        notificationButton.setFocusPainted(false);
        notificationButton.setBorderPainted(false);
        notificationButton.setContentAreaFilled(false);
        notificationButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        notificationButton.setToolTipText("View Notifications");

        // Load and set the image for the notification bell
        try {
            // Try to load the image from the classpath (e.g., /resources/notification_bell.png or /notification_bell.png)
            URL bellIconUrl = getClass().getResource("/notification_bell.png");
            BufferedImage bellImage;

            if (bellIconUrl == null) {
                System.err.println("Notification bell image 'notification_bell.png' not found in classpath. Using fallback text.");
                // Fallback to text if image not found
                notificationButton.setText("<html>&#128276;</html>"); // Unicode bell
                notificationButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
            } else {
                bellImage = ImageIO.read(bellIconUrl);
                // Scale the image to a suitable size (e.g., 28x28 pixels)
                int iconSize = 28;
                Image scaledBellImage = bellImage.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
                notificationButton.setIcon(new ImageIcon(scaledBellImage));
            }
        } catch (IOException e) {
            System.err.println("Error loading notification bell image: " + e.getMessage());
            // Fallback to text on error
            notificationButton.setText("<html>&#128276;</html>"); // Unicode bell
            notificationButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        }

        // ActionListener for Notification Button
        notificationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SProfilePageGUI.this.setVisible(false); // Hide the profile page

                // Corrected: Instantiate SReadNotification, passing the current profile frame AND the loggedInSStudent
                SReadNotification notificationFrame = new SReadNotification(SProfilePageGUI.this, loggedInSStudent);
                notificationFrame.setVisible(true);

                // Add a WindowListener to the notification frame
                notificationFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        // When SReadNotification closes, show the profile page again
                        SProfilePageGUI.this.setVisible(true);
                    }
                });
            }
        });

        // Panel to hold the notification button and push it to the right
        JPanel notificationButtonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)); // Use FlowLayout for alignment
        notificationButtonWrapper.setOpaque(false);
        notificationButtonWrapper.add(notificationButton);
        topWrapperPanel.add(notificationButtonWrapper, BorderLayout.NORTH); // Add button wrapper to the top of topWrapperPanel

        // -------------------- Profile Section (existing code, now added to topWrapperPanel) --------------------
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
            profilePicLabel = new RoundImageLabel(null, imageSize);
            profilePicLabel.setText("Image Error");
            profilePicLabel.setHorizontalAlignment(SwingConstants.CENTER);
            profilePicLabel.setVerticalAlignment(SwingConstants.CENTER);
            profilePicLabel.setPreferredSize(new Dimension(imageSize, imageSize));
            profilePicLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        }

        nameLabel.setFont(new Font("Inter", Font.BOLD, 28));
        nameLabel.setForeground(new Color(55, 65, 81));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        // Add the profilePanel to the CENTER of the topWrapperPanel
        topWrapperPanel.add(profilePanel, BorderLayout.CENTER);
        // Add the topWrapperPanel to the mainPanel (which uses BoxLayout.Y_AXIS)
        mainPanel.add(topWrapperPanel);


        // -------------------- Buttons Section (existing code) --------------------
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        buttonsPanel.setBackground(new Color(240, 242, 245));

        JButton classScheduleButton = createStyledButton("Class Schedule", new Color(24, 144, 255));
        buttonsPanel.add(classScheduleButton);

        JButton changeSubjectButton = createStyledButton("Change Subject", new Color(72, 160, 220));
        buttonsPanel.add(changeSubjectButton);

        JButton settingsButton = createStyledButton("Settings", new Color(100, 116, 139));
        buttonsPanel.add(settingsButton);

        JButton feesButton = createStyledButton("Fees", new Color(24, 144, 255));
        buttonsPanel.add(feesButton);

        // ActionListener for Class Schedule Button
        classScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SProfilePageGUI.this.setVisible(false);
                SClassScheduleApp classScheduleFrame = new SClassScheduleApp(loggedInSStudent, SProfilePageGUI.this);
                classScheduleFrame.setVisible(true);
                classScheduleFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        SProfilePageGUI.this.setVisible(true);
                    }
                });
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SProfilePageGUI.this.setVisible(false);
                SettingForStudent settingForStudentFrame = new SettingForStudent(loggedInSStudent, SProfilePageGUI.this);
                settingForStudentFrame.setVisible(true);
                settingForStudentFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        if (loggedInSStudent != null && loggedInSStudent.getLoginUsername() != null) {
                            loadAndRefreshStudentData(loggedInSStudent.getLoginUsername());
                        } else {
                            System.err.println("Could not refresh student data: loggedInSStudent or its username is null.");
                        }
                        SProfilePageGUI.this.setVisible(true);
                    }
                });
            }
        });

        changeSubjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SProfilePageGUI.this.setVisible(false);
                SSubjectUpdateForm subjectForm = new SSubjectUpdateForm(loggedInSStudent);
                subjectForm.setVisible(true);
                subjectForm.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        SProfilePageGUI.this.setVisible(true);
                    }
                });
            }
        });

        feesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SProfilePageGUI.this.setVisible(false);
                SMainPayment SMainPaymentFrame = new SMainPayment(SProfilePageGUI.this, loggedInSStudent);
                SMainPaymentFrame.setVisible(true);
                SMainPaymentFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        SProfilePageGUI.this.setVisible(true);
                    }
                });
            }
        });

        // The rest of the mainPanel additions remain the same
        mainPanel.add(Box.createVerticalStrut(30)); // Spacer between topWrapperPanel and buttonsPanel
        mainPanel.add(buttonsPanel);
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Load initial student data based on the provided username
        if (studentLoginUsername != null) {
            loadAndRefreshStudentData(studentLoginUsername);
        } else {
            System.err.println("SProfilePageGUI: No student login username provided during initialization.");
            nameLabel.setText("Login Error");
            emailLabel.setText("Please log in again.");
        }
    }

    /**
     * Loads student data from files and refreshes the displayed profile information.
     * @param identifier The identifier of the student whose data needs to be loaded and displayed.
     * This can be either studentId (from student_enrollment.txt) or loginUsername (from students.txt).
     * Since we're using "S001" in main, we'll treat it as loginUsername for lookup.
     */
    public void loadAndRefreshStudentData(String identifier) {
        Map<String, SStudent> allStudents = SettingForStudent.readEnrollmentData(STUDENT_ENROLLMENT_FILE); // Populates by studentId
        SettingForStudent.readStudentLoginData(STUDENTS_FILE, allStudents); // Overlays login data, including loginUsername

        loggedInSStudent = null; // Reset

        // Iterate through the map values to find the student by loginUsername
        for (SStudent student : allStudents.values()) {
            if (student.getLoginUsername() != null && student.getLoginUsername().equals(identifier)) {
                loggedInSStudent = student;
                break;
            }
        }

        if (loggedInSStudent != null) {
            nameLabel.setText(loggedInSStudent.getName());
            emailLabel.setText(loggedInSStudent.getEmail());
            // You might also want to update the profile picture if it's dynamic
            // For now, assuming the image doesn't change based on saved data.
        } else {
            nameLabel.setText("Student Not Found");
            emailLabel.setText("N/A");
            System.err.println("Error: Student with identifier " + identifier + " not found after refresh.");
            JOptionPane.showMessageDialog(this, "Could not load profile for student with identifier: " + identifier, "Data Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Inter", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Assuming ButtonUIWithAnimation is in common.ui package
        button.setUI(new common.ui.ButtonUIWithAnimation());
        return button;
    }

    private File createPlaceholderImage(int width, int height, Color bgColor, Color textColor, String text) throws IOException {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        g2d.setColor(bgColor);
        g2d.fillOval(0, 0, width, height);

        // Draw text
        g2d.setColor(textColor);
        g2d.setFont(new Font("Arial", Font.BOLD, width / 4));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (fm.getAscent() + (height - fm.getHeight()) / 2);
        g2d.drawString(text, x, y);
        g2d.dispose();

        // Save to a temporary file
        File tempFile = File.createTempFile("placeholder", ".png");
        ImageIO.write(img, "png", tempFile);
        return tempFile;
    }


    // Nested class for RoundImageLabel - ensure this is within SProfilePageGUI or its own file
    class RoundImageLabel extends JLabel {
        private BufferedImage image;
        private int size;
        private float alpha = 0.0f; // For fade-in effect

        public RoundImageLabel(String imagePath, int size) {
            this.size = size;
            setPreferredSize(new Dimension(size, size));
            setMinimumSize(new Dimension(size, size));
            setMaximumSize(new Dimension(size, size));
            setOpaque(false); // Make sure the label itself is not opaque

            if (imagePath != null) {
                try {
                    // Try to load from classpath first (for packaged JARs)
                    URL imageUrl = getClass().getResource(imagePath);
                    if (imageUrl == null) {
                        // If not in classpath, try loading from file system
                        File imgFile = new File(imagePath);
                        if (imgFile.exists()) {
                            image = ImageIO.read(imgFile);
                        } else {
                            System.err.println("Image file not found: " + imagePath);
                        }
                    } else {
                        image = ImageIO.read(imageUrl);
                    }

                    if (image != null) {
                        image = getRoundedImage(image, size);
                    }
                } catch (IOException e) {
                    System.err.println("Error loading or processing image: " + e.getMessage());
                    image = null;
                }
            }
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
            repaint();
        }

        private BufferedImage getRoundedImage(BufferedImage originalImage, int size) {
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

    // Default constructor (if needed, but usually the parameterized one is preferred after login)
    public SProfilePageGUI() {
        this(null, null); // Call the main constructor with null for both
    }

    // MODIFIED: main method for direct testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // When running SProfilePageGUI directly for testing, pass null for MainLoginPageTest
            // and a specific student username for testing (e.g., "S001" or "S002")
            new SProfilePageGUI(null, "S002"); // Pass "S002" to test directly with Mimi Chia's account
        });
    }
}