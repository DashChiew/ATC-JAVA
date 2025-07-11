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
import java.util.Map; // Import for Map

public class ProfilePageGUI extends JFrame {

    private Student loggedInStudent;

    // File constants (copied from Setting.java for consistency)
    private static final String STUDENTS_FILE = "students.txt";
    private static final String STUDENT_ENROLLMENT_FILE = "student_enrollment.txt";


    // JLabels that need to be updated
    private JLabel nameLabel;
    private JLabel emailLabel;
    private RoundImageLabel profilePicLabel; // Also keep a reference to update if needed

    public ProfilePageGUI(loginPageTest loginPageTest) {
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
                        ProfilePageGUI.this,
                        "Are you sure you want to quit the program?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    // Only exit if the user confirms YES
                    dispose(); // Close the frame
                    System.exit(0); // Terminate the application
                }
                // If NO, simply do nothing (due to setDefaultCloseOperation(DO_NOTHING_ON_CLOSE))
            }
        });


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        mainPanel.setBackground(new Color(240, 242, 245));

        // Initialize labels (will be updated by loadAndRefreshStudentData)
        nameLabel = new JLabel("Loading Name...");
        emailLabel = new JLabel("Loading Email...");

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

        // -------------------- Buttons Section --------------------
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        buttonsPanel.setBackground(new Color(240, 242, 245));

        // NEW: Assign the Class Schedule button to a variable and add its ActionListener
        JButton classScheduleButton = createStyledButton("🕜 Class Schedule", new Color(24, 144, 255));
        buttonsPanel.add(classScheduleButton); // Add it to the panel

        JButton changeSubjectButton = createStyledButton("📙 Change Subject", new Color(72, 160, 220));
        buttonsPanel.add(changeSubjectButton);

        JButton settingsButton = createStyledButton("🔨 Settings", new Color(100, 116, 139));
        buttonsPanel.add(settingsButton);

        JButton feesButton = createStyledButton("💰 Fees", new Color(24, 144, 255));
        buttonsPanel.add(feesButton);

        // ActionListener for Class Schedule Button
        classScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProfilePageGUI.this.setVisible(false); // Hide the current profile page
                // Create and show the ClassScheduleApp, passing the logged-in student and this frame as parent
                ClassScheduleApp classScheduleFrame = new ClassScheduleApp(loggedInStudent, ProfilePageGUI.this);
                classScheduleFrame.setVisible(true);

                // Add a WindowListener to ClassScheduleApp to show ProfilePageGUI when it closes
                classScheduleFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        ProfilePageGUI.this.setVisible(true); // Make the profile page visible again
                    }
                });
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProfilePageGUI.this.setVisible(false);
                // Pass the current loggedInStudent to the Setting frame
                Setting settingFrame = new Setting(loggedInStudent, ProfilePageGUI.this);
                settingFrame.setVisible(true);
                settingFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        // After Setting frame closes, reload data and refresh display
                        loadAndRefreshStudentData("S001"); // Assuming S001 is the current user for example
                        ProfilePageGUI.this.setVisible(true);
                    }
                });
            }
        });

        changeSubjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProfilePageGUI.this.setVisible(false);
                // CORRECTED LINE: Pass the loggedInStudent object directly
                SubjectUpdateForm subjectForm = new SubjectUpdateForm(loggedInStudent);
                subjectForm.setVisible(true);
                subjectForm.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        ProfilePageGUI.this.setVisible(true);
                    }
                });
            }
        });

        feesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProfilePageGUI.this.setVisible(false);

                // Pass 'this' (ProfilePageGUI instance) and loggedInStudent
                MainPayment mainPaymentFrame = new MainPayment(ProfilePageGUI.this, loggedInStudent);
                mainPaymentFrame.setVisible(true);

                mainPaymentFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        ProfilePageGUI.this.setVisible(true);
                    }
                });
            }
        });

        mainPanel.add(profilePanel);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(buttonsPanel);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Load initial student data and refresh display
        loadAndRefreshStudentData("S001"); // Load data for S001 on startup
    }



    /**
     * Loads student data from files and refreshes the displayed profile information.
     * @param studentId The ID of the student whose data needs to be loaded and displayed.
     */
    private void loadAndRefreshStudentData(String studentId) {
        // Ensure that Setting.java is properly compiled and accessible
        // It should contain the static methods readEnrollmentData and readStudentLoginData
        Map<String, Student> allStudents = Setting.readEnrollmentData(STUDENT_ENROLLMENT_FILE);
        Setting.readStudentLoginData(STUDENTS_FILE, allStudents);

        loggedInStudent = allStudents.get(studentId);

        if (loggedInStudent != null) {
            nameLabel.setText(loggedInStudent.getName());
            emailLabel.setText(loggedInStudent.getEmail());
            // You might also want to update the profile picture if it's dynamic
            // For now, assuming the image doesn't change based on saved data.
        } else {
            nameLabel.setText("Student Not Found");
            emailLabel.setText("N/A");
            System.err.println("Error: Student with ID " + studentId + " not found after refresh.");
            JOptionPane.showMessageDialog(this, "Could not load profile for student ID: " + studentId, "Data Load Error", JOptionPane.ERROR_MESSAGE);
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

        button.setUI(new ButtonUIWithAnimation());

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                Object ui = ((JButton) evt.getSource()).getUI();
                if (ui instanceof ButtonUIWithAnimation) {
                    ButtonUIWithAnimation customUI = (ButtonUIWithAnimation) ui;
                    customUI.startAnimation((JComponent) evt.getSource(), customUI.currentLift, customUI.LIFT_AMOUNT);
                }
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent evt) {
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
            loginPageTest loginPageTest = null;
            new ProfilePageGUI(loginPageTest);
        });
    }
}