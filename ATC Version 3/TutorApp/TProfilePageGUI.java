package TutorApp;

import common.ui.ButtonUIWithAnimation;
import common.ui.SRoundImageLabel;
import common.ui.RoundedBorder;

import TutorApp.ui.frames.TAddClassFrame;
import TutorApp.ui.frames.TDeleteClassFrame;
import TutorApp.ui.frames.SettingForTutor;
import TutorApp.ui.frames.TUpdateClassFrame;
import TutorApp.ui.frames.TutorPostAnnouncementFrame; // NEW IMPORT
import TutorApp.ui.panels.TViewClassesPanel;
import TutorApp.ui.panels.TViewStudentsPanel;
import TutorApp.utils.FileUtil;
import common.auth.MainLoginPageTest;
import common.model.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TProfilePageGUI extends JFrame {

    private JPanel mainContentPanel;
    private User currentUser;
    private MainLoginPageTest loginPage;

    private static final String USERS_FILE = "users.txt";

    public TProfilePageGUI(MainLoginPageTest loginPage, String tutorUsername) {
        this.currentUser = getUserFromUsername(tutorUsername);
        if (this.currentUser == null) {
            JOptionPane.showMessageDialog(null, "Error: User profile not found for username: " + tutorUsername + "\nPlease contact support.", "Profile Load Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        this.loginPage = loginPage;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("User Profile & Tutor Dashboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(850, 680));

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(new Color(240, 242, 245));

        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        mainContentPanel.setBackground(new Color(240, 242, 245));

        showProfileSection();

        rootPanel.add(mainContentPanel, BorderLayout.CENTER);
        add(rootPanel);

        pack();
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (loginPage != null) {
                    loginPage.setVisible(true);
                    loginPage.clearFields();
                }
            }
        });
    }

    private void showProfileSection() {
        mainContentPanel.removeAll();

        JPanel topSectionPanel = new JPanel(new BorderLayout());
        topSectionPanel.setBackground(new Color(240, 242, 245));

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

        SRoundImageLabel profilePicLabel = null;
        int imageSize = 128;

        try {
            URL imageUrl = getClass().getResource("/xiaochou.png");
            File imageFile = null;

            if (imageUrl == null) {
                String userDir = System.getProperty("user.dir");
                imageFile = new File(userDir, "xiaochou.png");
                if (!imageFile.exists()) {
                    System.err.println("Local image 'xiaochou.png' not found in classpath or project root. Using placeholder.");
                    imageFile = FileUtil.createPlaceholderImage(imageSize, imageSize, new Color(108, 99, 255), Color.WHITE, "Profile");
                }
            } else {
                imageFile = new File(imageUrl.toURI());
            }

            if (imageFile != null) {
                profilePicLabel = new SRoundImageLabel(imageFile.getAbsolutePath(), imageSize);
            } else {
                profilePicLabel = new SRoundImageLabel(null, imageSize);
            }

            profilePicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            profilePicLabel.setBorder(new RoundedBorder(imageSize / 2, new Color(173, 216, 230), 2));

            SRoundImageLabel finalProfilePicLabel = profilePicLabel;
            Timer fadeInTimer = new Timer(15, e -> {
                float alpha = finalProfilePicLabel.getAlpha();
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
            e.printStackTrace();
            profilePicLabel = new SRoundImageLabel(null, imageSize);
            profilePicLabel.setText("Image Error");
            profilePicLabel.setHorizontalAlignment(SwingConstants.CENTER);
            profilePicLabel.setVerticalAlignment(SwingConstants.CENTER);
            profilePicLabel.setPreferredSize(new Dimension(imageSize, imageSize));
            profilePicLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        }

        String displayName = currentUser.getName();
        String displayEmail = currentUser.getEmail() + "@gmail.com";

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

        topSectionPanel.add(profilePanel, BorderLayout.CENTER);

        // -------------------- Buttons Section --------------------
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        buttonsPanel.setBackground(new Color(240, 242, 245));
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel topRowButtonsPanel = new JPanel(new GridLayout(1, 2, 20, 20)); // Changed to 1 row, 2 columns
        topRowButtonsPanel.setBackground(new Color(240, 242, 245));
        topRowButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topRowButtonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JButton coursesButton = createStyledButton("Course", new Color(24, 144, 255));
        coursesButton.addActionListener(e -> showClassManagementOptions());
        topRowButtonsPanel.add(coursesButton);

        JButton enrolledStudentsButton = createStyledButton("Student", new Color(72, 160, 220));
        enrolledStudentsButton.addActionListener(e -> showViewStudentsPanel());
        topRowButtonsPanel.add(enrolledStudentsButton);

        buttonsPanel.add(topRowButtonsPanel);
        buttonsPanel.add(Box.createVerticalStrut(20));

        // NEW: Panel for "Post Announcement" and "My Profile" buttons
        JPanel bottomRowButtonsPanel = new JPanel(new GridLayout(1, 2, 20, 20)); // 1 row, 2 columns for side-by-side
        bottomRowButtonsPanel.setBackground(new Color(240, 242, 245));
        bottomRowButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomRowButtonsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JButton postAnnouncementButton = createStyledButton("Post Announcement", new Color(72, 160, 220)); // A new color for this button
        postAnnouncementButton.addActionListener(e -> new TutorPostAnnouncementFrame(currentUser.getUsername()).setVisible(true));
        bottomRowButtonsPanel.add(postAnnouncementButton);

        JButton myProfileButton = createStyledButton("My Profile", new Color(72, 160, 220));
        myProfileButton.addActionListener(e -> {
            User latestUser = getUserFromUsername(currentUser.getUsername());
            if (latestUser != null) {
                new SettingForTutor(latestUser).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Could not load latest profile data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        bottomRowButtonsPanel.add(myProfileButton);

        buttonsPanel.add(bottomRowButtonsPanel); // Add the new panel to the main buttonsPanel

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

        String tutorUsernameForClasses = currentUser.getUsername();

        JButton addClassButton = createStyledButton("Add New Class", new Color(51, 153, 255));
        addClassButton.addActionListener(e -> new TAddClassFrame(tutorUsernameForClasses).setVisible(true));
        buttonGridPanel.add(addClassButton);

        JButton viewClassesButton = createStyledButton("View My Classes", new Color(51, 153, 255));
        viewClassesButton.addActionListener(e -> showViewClassesPanel());
        buttonGridPanel.add(viewClassesButton);

        JButton updateClassButton = createStyledButton("Update Class", new Color(51, 153, 255));
        updateClassButton.addActionListener(e -> new TUpdateClassFrame(tutorUsernameForClasses).setVisible(true));
        buttonGridPanel.add(updateClassButton);

        JButton deleteClassButton = createStyledButton("Delete Class", new Color(51, 153, 255));
        deleteClassButton.addActionListener(e -> new TDeleteClassFrame(tutorUsernameForClasses).setVisible(true));
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

    private void showViewClassesPanel() {
        mainContentPanel.removeAll();

        JPanel viewClassesContainerPanel = new JPanel(new BorderLayout(10, 10));
        viewClassesContainerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        viewClassesContainerPanel.setBackground(new Color(240, 242, 245));

        JLabel titleLabel = new JLabel("My Classes", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 28));
        titleLabel.setForeground(new Color(55, 65, 81));
        viewClassesContainerPanel.add(titleLabel, BorderLayout.NORTH);

        TViewClassesPanel viewClassesPanel = new TViewClassesPanel(currentUser.getUsername());
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
        mainContentPanel.removeAll();

        JPanel viewStudentsContainerPanel = new JPanel(new BorderLayout(10, 10));
        viewStudentsContainerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        viewStudentsContainerPanel.setBackground(new Color(240, 242, 245));

        JLabel titleLabel = new JLabel("Enrolled Students", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 28));
        titleLabel.setForeground(new Color(55, 65, 81));
        viewStudentsContainerPanel.add(titleLabel, BorderLayout.NORTH);

        TViewStudentsPanel viewStudentsPanel = new TViewStudentsPanel(currentUser.getUsername());
        viewStudentsContainerPanel.add(viewStudentsPanel);

        JButton backButton = new JButton("Back to Profile");
        backButton.setFont(new Font("Inter", Font.PLAIN, 14));
        backButton.setBackground(new Color(100, 116, 139));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setUI(new ButtonUIWithAnimation());
        backButton.addActionListener(e -> showProfileSection());

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
        button.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setUI(new ButtonUIWithAnimation());
        return button;
    }

    private User getUserFromUsername(String username) {
        Map<String, User> allUsers = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = FileUtil.parseCsvLine(line);
                if (parts.length == 8) {
                    String name = parts[0].trim();
                    String userUsername = parts[1].trim();
                    String password = parts[2].trim();
                    String icPassport = parts[3].trim();
                    String email = parts[4].trim();
                    String contactNumber = parts[5].trim();
                    String address = parts[6].trim();
                    String role = parts[7].trim();

                    User user = new User(name, userUsername, password, icPassport, email, contactNumber, address, role);
                    allUsers.put(userUsername, user);
                } else {
                    System.err.println("Skipping malformed line in " + USERS_FILE + " (expected 8 parts): " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + USERS_FILE + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage(), "File Read Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return allUsers.get(username);
    }
}
