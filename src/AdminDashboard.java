//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.IOException;
//import java.util.List;
//
//
//
//
//public class AdminDashboard extends JFrame implements ActionListener {
//
//    private final loginPageTest loginPage;
//
//    // Use more descriptive names for button groups
//    private JButton btnRegisterTutor;
//    private JButton btnDeleteTutor;
//    private JButton btnAssignTutorToLevelSubject;
//    private JButton btnDisplayTutor;
//
//    private JButton btnRegisterReceptionist;
//    private JButton btnDeleteReceptionist;
//    private JButton btnDisplayReceptionist;
//
//    private JButton btnViewMonthlyIncomeReport;
//    private JButton btnUpdateMyProfile;
//    private JButton btnLogout;
//    private JButton btnViewSessions;
//    private JButton btnTerminateSession;
//
//    public AdminDashboard(loginPageTest loginPage) {
//        this.loginPage = loginPage;
//        setTitle("Admin Dashboard");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setPreferredSize(new Dimension(900, 900)); // Adjusted size for more buttons
//
//        JPanel mainPanel = new JPanel();
//        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
//        mainPanel.setBackground(new Color(240, 242, 245)); // Light gray background
//
//        JLabel welcomeLabel = new JLabel("Welcome, Administrator!");
//        welcomeLabel.setFont(new Font("Inter", Font.BOLD, 30));
//        welcomeLabel.setForeground(new Color(55, 65, 81));
//        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        welcomeLabel.setBorder(new EmptyBorder(0, 0, 30, 0)); // Padding below welcome message
//
//        // -------------------- Tutor Management Section --------------------
//        JLabel tutorLabel = new JLabel("Tutor Management");
//        tutorLabel.setFont(new Font("Inter", Font.BOLD, 20));
//        tutorLabel.setForeground(new Color(55, 65, 81));
//        tutorLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Changed to CENTER_ALIGNMENT
//        tutorLabel.setBorder(new EmptyBorder(10, 0, 15, 0));
//
//        JPanel tutorButtonsPanel = new JPanel(new GridLayout(2, 2, 20, 20)); // 2 rows, 2 columns
//        tutorButtonsPanel.setBackground(new Color(240, 242, 245));
//        tutorButtonsPanel.setMaximumSize(new Dimension(550, 150)); // Limit size
//        tutorButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the panel itself
//
//        btnRegisterTutor = createStyledButton("Register Tutor", new Color(24, 144, 255)); // Blue
//        tutorButtonsPanel.add(btnRegisterTutor);
//        btnRegisterTutor.addActionListener(e -> TutorManagementGUI.showRegisterTutorDialog(this));
//
//        btnDeleteTutor = createStyledButton("Delete Tutor", new Color(245, 120, 0)); // Orange
//        tutorButtonsPanel.add(btnDeleteTutor);
//        btnDeleteTutor.addActionListener(e -> TutorManagementGUI.showDeleteTutorDialog(this));
//
//        btnAssignTutorToLevelSubject = createStyledButton("Assign Tutor to Level/Subject", new Color(72, 160, 220)); // Lighter Blue
//        tutorButtonsPanel.add(btnAssignTutorToLevelSubject);
//        btnAssignTutorToLevelSubject.addActionListener(e -> TutorManagementGUI.showAssignTutorDialog(this));
//
//        btnDisplayTutor = createStyledButton("Display Tutors", new Color(60, 179, 113)); // Medium Sea Green
//        tutorButtonsPanel.add(btnDisplayTutor);
//        btnDisplayTutor.addActionListener(e -> TutorManagementGUI.showAllTutorInfoDialog(this));
//
//
//        // -------------------- Receptionist Management Section --------------------
//        JLabel receptionistLabel = new JLabel("Receptionist Management");
//        receptionistLabel.setFont(new Font("Inter", Font.BOLD, 20));
//        receptionistLabel.setForeground(new Color(55, 65, 81));
//        receptionistLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Changed to CENTER_ALIGNMENT
//        receptionistLabel.setBorder(new EmptyBorder(25, 0, 15, 0)); // More padding above
//
//        JPanel receptionistButtonsContainer = new JPanel();
//        receptionistButtonsContainer.setLayout(new BoxLayout(receptionistButtonsContainer, BoxLayout.Y_AXIS));
//        receptionistButtonsContainer.setBackground(new Color(240, 242, 245));
//        receptionistButtonsContainer.setMaximumSize(new Dimension(550, 150));
//        receptionistButtonsContainer.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the container itself
//
//
//        JPanel firstRowReceptionistButtons = new JPanel(new GridLayout(1, 2, 20, 20)); // 1 row, 2 columns
//        firstRowReceptionistButtons.setBackground(new Color(240, 242, 245));
//        firstRowReceptionistButtons.setMaximumSize(new Dimension(550, 70)); // Adjusted height
//        firstRowReceptionistButtons.setAlignmentX(Component.CENTER_ALIGNMENT); // Center this panel
//
//
//        btnRegisterReceptionist = createStyledButton("Register Receptionist", new Color(24, 144, 255)); // Blue
//        firstRowReceptionistButtons.add(btnRegisterReceptionist);
//        btnRegisterReceptionist.addActionListener(e -> ReceptionistManagementGUI.showRegisterReceptionistDialog(this));
//
//        btnDeleteReceptionist = createStyledButton("Delete Receptionist", new Color(245, 120, 0)); // Orange
//        firstRowReceptionistButtons.add(btnDeleteReceptionist);
//        btnDeleteReceptionist.addActionListener(e -> ReceptionistManagementGUI.showDeleteReceptionistDialog(this));
//
//        receptionistButtonsContainer.add(firstRowReceptionistButtons);
//
//
//        JPanel secondRowReceptionistButton = new JPanel();
//        secondRowReceptionistButton.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); // Center the button horizontally
//        secondRowReceptionistButton.setBackground(new Color(240, 242, 245));
//        secondRowReceptionistButton.setBorder(new EmptyBorder(20, 0, 0, 0)); // Add padding above the button
//        secondRowReceptionistButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center this panel
//
//
//        btnDisplayReceptionist = createStyledButton("Display Receptionists", new Color(60, 179, 113)); // Medium Sea Green
//        secondRowReceptionistButton.add(btnDisplayReceptionist);
//        btnDisplayReceptionist.addActionListener(e -> ReceptionistManagementGUI.showAllReceptionistInfoDialog(this));
//
//        receptionistButtonsContainer.add(secondRowReceptionistButton);
//
//        // -------------------- Security Section --------------------
//        JLabel securityLabel = new JLabel("Security");
//        securityLabel.setFont(new Font("Inter", Font.BOLD, 20));
//        securityLabel.setForeground(new Color(55, 65, 81));
//        securityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        securityLabel.setBorder(new EmptyBorder(25, 0, 15, 0));
//
//        JPanel securityButtonsPanel = new JPanel(new GridLayout(1, 2, 20, 20)); // 1 row, 2 columns
//        securityButtonsPanel.setBackground(new Color(240, 242, 245));
//        securityButtonsPanel.setMaximumSize(new Dimension(550, 75));
//        securityButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        btnViewSessions = createStyledButton("View Active Sessions", new Color(23, 162, 184)); // Teal
//        securityButtonsPanel.add(btnViewSessions);
//        btnViewSessions.addActionListener(e -> viewSessions());
//
//        btnTerminateSession = createStyledButton("Terminate Session", new Color(220, 53, 69)); // Red
//        securityButtonsPanel.add(btnTerminateSession);
//        btnTerminateSession.addActionListener(e -> terminateSession());
//
//        // -------------------- Reports & Account Section --------------------
//        JLabel generalLabel = new JLabel("Reports & Account");
//        generalLabel.setFont(new Font("Inter", Font.BOLD, 20));
//        generalLabel.setForeground(new Color(55, 65, 81));
//        generalLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Changed to CENTER_ALIGNMENT
//        generalLabel.setBorder(new EmptyBorder(25, 0, 15, 0)); // More padding above
//
//        JPanel generalButtonsPanel = new JPanel(new GridLayout(1, 3, 20, 20)); // 1 row, 3 columns
//        generalButtonsPanel.setBackground(new Color(240, 242, 245));
//        generalButtonsPanel.setMaximumSize(new Dimension(550, 75));
//        generalButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the panel itself
//
//        btnViewMonthlyIncomeReport = createStyledButton("View Income Report", new Color(100, 116, 139)); // Grayish
//        generalButtonsPanel.add(btnViewMonthlyIncomeReport);
//        btnViewMonthlyIncomeReport.addActionListener(this); // Add appropriate action if available
//
//        btnUpdateMyProfile = createStyledButton("Update Profile", new Color(100, 116, 139)); // Grayish
//        generalButtonsPanel.add(btnUpdateMyProfile);
//        btnUpdateMyProfile.addActionListener(this); // Add appropriate action if available
//
//        btnLogout = createStyledButton("Logout", new Color(200, 50, 50)); // Red for logout
//        generalButtonsPanel.add(btnLogout);
//        btnLogout.addActionListener(this);
//
//
//        // -------------------- Add sections to main panel --------------------
//        mainPanel.add(welcomeLabel);
//
//        mainPanel.add(tutorLabel);
//        mainPanel.add(tutorButtonsPanel);
//
//        mainPanel.add(receptionistLabel);
//        mainPanel.add(receptionistButtonsContainer); // Add the container here
//
//        mainPanel.add(securityLabel); // Add the new Security label
//        mainPanel.add(securityButtonsPanel); // Add the new Security buttons panel
//
//        mainPanel.add(generalLabel);
//        mainPanel.add(generalButtonsPanel);
//
//
//        add(mainPanel);
//
//        pack();
//        setLocationRelativeTo(null);
//        setVisible(true);
//    }
//
//    /**
//     * Helper method to create a styled JButton.
//     * Reuses the ButtonUIWithAnimation for consistent styling and hover effects.
//     *
//     * @param text The text to display on the button.
//     * @param bgColor The background color of the button.
//     * @return A styled JButton instance.
//     */
//    private JButton createStyledButton(String text, Color bgColor) {
//        JButton button = new JButton(text);
//        button.setFont(new Font("Inter", Font.BOLD, 15));
//        button.setBackground(bgColor);
//        button.setForeground(Color.WHITE);
//        button.setFocusPainted(false);
//        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
//        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
//
//        // Use the custom UI from RProfilePageGUI for animation
//        button.setUI(new RProfilePageGUI.ButtonUIWithAnimation());
//
//        button.addMouseListener(new java.awt.event.MouseAdapter() {
//            @Override
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                Object ui = ((JButton) evt.getSource()).getUI();
//                if (ui instanceof RProfilePageGUI.ButtonUIWithAnimation customUI) {
//                    customUI.startAnimation((JComponent) evt.getSource(), customUI.currentLift, customUI.LIFT_AMOUNT);
//                }
//                button.setBackground(bgColor.brighter());
//            }
//
//            @Override
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                Object ui = ((JButton) evt.getSource()).getUI();
//                if (ui instanceof RProfilePageGUI.ButtonUIWithAnimation customUI) {
//                    customUI.startAnimation((JComponent) evt.getSource(), customUI.currentLift, 0);
//                }
//                button.setBackground(bgColor);
//            }
//        });
//        return button;
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == btnViewMonthlyIncomeReport) {
//            JOptionPane.showMessageDialog(this, "Monthly Income Report functionality coming soon!", "Report", JOptionPane.INFORMATION_MESSAGE);
//        } else if (e.getSource() == btnUpdateMyProfile) {
//            JOptionPane.showMessageDialog(this, "Update My Profile functionality coming soon!", "Profile Update", JOptionPane.INFORMATION_MESSAGE);
//        } else if (e.getSource() == btnLogout) {
//            if (this.loginPage != null) {
//                LogoutHandler.logout(this, this.loginPage);
//            } else {
//                JOptionPane.showMessageDialog(this,
//                        "Error: The login page reference is missing. Cannot log out.",
//                        "Logout Error",
//                        JOptionPane.ERROR_MESSAGE);
//                System.err.println("AdminDashboard: loginPage reference is null during logout attempt.");
//            }
//        }
//    }
//
//    private void viewSessions() {
//        try {
//            List<String[]> sessions = SessionManager.listActiveSessions(); // Make sure this is java.util.List
//            if (sessions.isEmpty()) {
//                JOptionPane.showMessageDialog(this, "No active sessions.", "Sessions", JOptionPane.INFORMATION_MESSAGE);
//                return;
//            }
//            StringBuilder sb = new StringBuilder("Active Sessions:\n\n");
//            for (String[] s : sessions) {
//                sb.append("ID: ").append(s[0])
//                        .append(" | User: ").append(s[1])
//                        .append(" | Role: ").append(s[2])
//                        .append(" | Time: ").append(s[3])
//                        .append("\n");
//            }
//            JOptionPane.showMessageDialog(this, sb.toString(), "Active Sessions", JOptionPane.INFORMATION_MESSAGE);
//        } catch (IOException ex) {
//            JOptionPane.showMessageDialog(this, "Error reading sessions: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//
//    private void terminateSession() {
//        String sessionId = JOptionPane.showInputDialog(this, "Enter session ID to terminate:", "Terminate Session", JOptionPane.QUESTION_MESSAGE);
//        if (sessionId != null && !sessionId.trim().isEmpty()) {
//            try {
//                if (SessionManager.removeSession(sessionId.trim())) {
//                    JOptionPane.showMessageDialog(this, "Session terminated.", "Success", JOptionPane.INFORMATION_MESSAGE);
//                } else {
//                    JOptionPane.showMessageDialog(this, "Session ID not found.", "Failure", JOptionPane.WARNING_MESSAGE);
//                }
//            } catch (IOException ex) {
//                JOptionPane.showMessageDialog(this, "Error terminating session: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }
//
//}


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

// Import the User and SettingForAdminReceptionist classes
// Ensure these are correctly placed in your project or are in the same package
// import User; // If User is in a different package
// import SettingForAdminReceptionist; // If SettingForAdminReceptionist is in a different package


public class AdminDashboard extends JFrame implements ActionListener {

    private final loginPageTest loginPage;
    private User currentUser; // Add this field to store the logged-in user

    // Use more descriptive names for button groups
    private JButton btnRegisterTutor;
    private JButton btnDeleteTutor;
    private JButton btnAssignTutorToLevelSubject;
    private JButton btnDisplayTutor;

    private JButton btnRegisterReceptionist;
    private JButton btnDeleteReceptionist;
    private JButton btnDisplayReceptionist;

    private JButton btnViewMonthlyIncomeReport;
    private JButton btnUpdateMyProfile;
    private JButton btnLogout;
    private JButton btnViewSessions;
    private JButton btnTerminateSession;

    // Modified constructor to accept the User object
    public AdminDashboard(loginPageTest loginPage, User currentUser) {
        this.loginPage = loginPage;
        this.currentUser = currentUser; // Assign the current user

        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(900, 900)); // Adjusted size for more buttons

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        mainPanel.setBackground(new Color(240, 242, 245)); // Light gray background

        JLabel welcomeLabel = new JLabel("Welcome, Administrator!");
        if (currentUser != null) { // Customize welcome message with user's name
            welcomeLabel.setText("Welcome, " + currentUser.getName() + "!");
        }
        welcomeLabel.setFont(new Font("Inter", Font.BOLD, 30));
        welcomeLabel.setForeground(new Color(55, 65, 81));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(new EmptyBorder(0, 0, 30, 0)); // Padding below welcome message

        // -------------------- Tutor Management Section --------------------
        JLabel tutorLabel = new JLabel("Tutor Management");
        tutorLabel.setFont(new Font("Inter", Font.BOLD, 20));
        tutorLabel.setForeground(new Color(55, 65, 81));
        tutorLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Changed to CENTER_ALIGNMENT
        tutorLabel.setBorder(new EmptyBorder(10, 0, 15, 0));

        JPanel tutorButtonsPanel = new JPanel(new GridLayout(2, 2, 20, 20)); // 2 rows, 2 columns
        tutorButtonsPanel.setBackground(new Color(240, 242, 245));
        tutorButtonsPanel.setMaximumSize(new Dimension(550, 150)); // Limit size
        tutorButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the panel itself

        btnRegisterTutor = createStyledButton("Register Tutor", new Color(24, 144, 255)); // Blue
        tutorButtonsPanel.add(btnRegisterTutor);
        btnRegisterTutor.addActionListener(e -> TutorManagementGUI.showRegisterTutorDialog(this));

        btnDeleteTutor = createStyledButton("Delete Tutor", new Color(245, 120, 0)); // Orange
        tutorButtonsPanel.add(btnDeleteTutor);
        btnDeleteTutor.addActionListener(e -> TutorManagementGUI.showDeleteTutorDialog(this));

        btnAssignTutorToLevelSubject = createStyledButton("Assign Tutor to Level/Subject", new Color(72, 160, 220)); // Lighter Blue
        tutorButtonsPanel.add(btnAssignTutorToLevelSubject);
        btnAssignTutorToLevelSubject.addActionListener(e -> TutorManagementGUI.showAssignTutorDialog(this));

        btnDisplayTutor = createStyledButton("Display Tutors", new Color(60, 179, 113)); // Medium Sea Green
        tutorButtonsPanel.add(btnDisplayTutor);
        btnDisplayTutor.addActionListener(e -> TutorManagementGUI.showAllTutorInfoDialog(this));


        // -------------------- Receptionist Management Section --------------------
        JLabel receptionistLabel = new JLabel("Receptionist Management");
        receptionistLabel.setFont(new Font("Inter", Font.BOLD, 20));
        receptionistLabel.setForeground(new Color(55, 65, 81));
        receptionistLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Changed to CENTER_ALIGNMENT
        receptionistLabel.setBorder(new EmptyBorder(25, 0, 15, 0)); // More padding above

        JPanel receptionistButtonsContainer = new JPanel();
        receptionistButtonsContainer.setLayout(new BoxLayout(receptionistButtonsContainer, BoxLayout.Y_AXIS));
        receptionistButtonsContainer.setBackground(new Color(240, 242, 245));
        receptionistButtonsContainer.setMaximumSize(new Dimension(550, 150));
        receptionistButtonsContainer.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the container itself


        JPanel firstRowReceptionistButtons = new JPanel(new GridLayout(1, 2, 20, 20)); // 1 row, 2 columns
        firstRowReceptionistButtons.setBackground(new Color(240, 242, 245));
        firstRowReceptionistButtons.setMaximumSize(new Dimension(550, 70)); // Adjusted height
        firstRowReceptionistButtons.setAlignmentX(Component.CENTER_ALIGNMENT); // Center this panel


        btnRegisterReceptionist = createStyledButton("Register Receptionist", new Color(24, 144, 255)); // Blue
        firstRowReceptionistButtons.add(btnRegisterReceptionist);
        btnRegisterReceptionist.addActionListener(e -> ReceptionistManagementGUI.showRegisterReceptionistDialog(this));

        btnDeleteReceptionist = createStyledButton("Delete Receptionist", new Color(245, 120, 0)); // Orange
        firstRowReceptionistButtons.add(btnDeleteReceptionist);
        btnDeleteReceptionist.addActionListener(e -> ReceptionistManagementGUI.showDeleteReceptionistDialog(this));

        receptionistButtonsContainer.add(firstRowReceptionistButtons);


        JPanel secondRowReceptionistButton = new JPanel();
        secondRowReceptionistButton.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); // Center the button horizontally
        secondRowReceptionistButton.setBackground(new Color(240, 242, 245));
        secondRowReceptionistButton.setBorder(new EmptyBorder(20, 0, 0, 0)); // Add padding above the button
        secondRowReceptionistButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center this panel


        btnDisplayReceptionist = createStyledButton("Display Receptionists", new Color(60, 179, 113)); // Medium Sea Green
        secondRowReceptionistButton.add(btnDisplayReceptionist);
        btnDisplayReceptionist.addActionListener(e -> ReceptionistManagementGUI.showAllReceptionistInfoDialog(this));

        receptionistButtonsContainer.add(secondRowReceptionistButton);

        // -------------------- Security Section --------------------
        JLabel securityLabel = new JLabel("Security");
        securityLabel.setFont(new Font("Inter", Font.BOLD, 20));
        securityLabel.setForeground(new Color(55, 65, 81));
        securityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        securityLabel.setBorder(new EmptyBorder(25, 0, 15, 0));

        JPanel securityButtonsPanel = new JPanel(new GridLayout(1, 2, 20, 20)); // 1 row, 2 columns
        securityButtonsPanel.setBackground(new Color(240, 242, 245));
        securityButtonsPanel.setMaximumSize(new Dimension(550, 75));
        securityButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnViewSessions = createStyledButton("View Active Sessions", new Color(23, 162, 184)); // Teal
        securityButtonsPanel.add(btnViewSessions);
        btnViewSessions.addActionListener(e -> viewSessions());

        btnTerminateSession = createStyledButton("Terminate Session", new Color(220, 53, 69)); // Red
        securityButtonsPanel.add(btnTerminateSession);
        btnTerminateSession.addActionListener(e -> terminateSession());

        // -------------------- Reports & Account Section --------------------
        JLabel generalLabel = new JLabel("Reports & Account");
        generalLabel.setFont(new Font("Inter", Font.BOLD, 20));
        generalLabel.setForeground(new Color(55, 65, 81));
        generalLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Changed to CENTER_ALIGNMENT
        generalLabel.setBorder(new EmptyBorder(25, 0, 15, 0)); // More padding above

        JPanel generalButtonsPanel = new JPanel(new GridLayout(1, 3, 20, 20)); // 1 row, 3 columns
        generalButtonsPanel.setBackground(new Color(240, 242, 245));
        generalButtonsPanel.setMaximumSize(new Dimension(550, 75));
        generalButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the panel itself

        btnViewMonthlyIncomeReport = createStyledButton("View Income Report", new Color(100, 116, 139)); // Grayish
        generalButtonsPanel.add(btnViewMonthlyIncomeReport);
        btnViewMonthlyIncomeReport.addActionListener(this); // Add appropriate action if available

        btnUpdateMyProfile = createStyledButton("Update Profile", new Color(100, 116, 139)); // Grayish
        generalButtonsPanel.add(btnUpdateMyProfile);
        btnUpdateMyProfile.addActionListener(this); // This is where the change will happen

        btnLogout = createStyledButton("Logout", new Color(200, 50, 50)); // Red for logout
        generalButtonsPanel.add(btnLogout);
        btnLogout.addActionListener(this);


        // -------------------- Add sections to main panel --------------------
        mainPanel.add(welcomeLabel);

        mainPanel.add(tutorLabel);
        mainPanel.add(tutorButtonsPanel);

        mainPanel.add(receptionistLabel);
        mainPanel.add(receptionistButtonsContainer); // Add the container here

        mainPanel.add(securityLabel); // Add the new Security label
        mainPanel.add(securityButtonsPanel); // Add the new Security buttons panel

        mainPanel.add(generalLabel);
        mainPanel.add(generalButtonsPanel);


        add(mainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Helper method to create a styled JButton.
     * Reuses the ButtonUIWithAnimation for consistent styling and hover effects.
     *
     * @param text The text to display on the button.
     * @param bgColor The background color of the button.
     * @return A styled JButton instance.
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Inter", Font.BOLD, 15));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Use the custom UI from RProfilePageGUI for animation
        // Ensure RProfilePageGUI.ButtonUIWithAnimation is accessible,
        // or copy its definition into AdminDashboard if it's not a common utility class.
        button.setUI(new RProfilePageGUI.ButtonUIWithAnimation());

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Object ui = ((JButton) evt.getSource()).getUI();
                if (ui instanceof RProfilePageGUI.ButtonUIWithAnimation customUI) {
                    customUI.startAnimation((JComponent) evt.getSource(), customUI.currentLift, customUI.LIFT_AMOUNT);
                }
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Object ui = ((JButton) evt.getSource()).getUI();
                if (ui instanceof RProfilePageGUI.ButtonUIWithAnimation customUI) {
                    customUI.startAnimation((JComponent) evt.getSource(), customUI.currentLift, 0);
                }
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnViewMonthlyIncomeReport) {
            JOptionPane.showMessageDialog(this, "Monthly Income Report functionality coming soon!", "Report", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == btnUpdateMyProfile) {
            // INTEGRATION: Open SettingForAdminReceptionist frame
            if (currentUser != null) {
                new SettingForAdminReceptionist(currentUser);
            } else {
                JOptionPane.showMessageDialog(this, "User data not available for profile update.", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("AdminDashboard: currentUser is null when attempting to open profile settings.");
            }
        } else if (e.getSource() == btnLogout) {
            if (this.loginPage != null) {
                LogoutHandler.logout(this, this.loginPage);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error: The login page reference is missing. Cannot log out.",
                        "Logout Error",
                        JOptionPane.ERROR_MESSAGE);
                System.err.println("AdminDashboard: loginPage reference is null during logout attempt.");
            }
        }
    }

    private void viewSessions() {
        try {
            List<String[]> sessions = SessionManager.listActiveSessions(); // Make sure this is java.util.List
            if (sessions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No active sessions.", "Sessions", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            StringBuilder sb = new StringBuilder("Active Sessions:\n\n");
            for (String[] s : sessions) {
                sb.append("ID: ").append(s[0])
                        .append(" | User: ").append(s[1])
                        .append(" | Role: ").append(s[2])
                        .append(" | Time: ").append(s[3])
                        .append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Active Sessions", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading sessions: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void terminateSession() {
        String sessionId = JOptionPane.showInputDialog(this, "Enter session ID to terminate:", "Terminate Session", JOptionPane.QUESTION_MESSAGE);
        if (sessionId != null && !sessionId.trim().isEmpty()) {
            try {
                if (SessionManager.removeSession(sessionId.trim())) {
                    JOptionPane.showMessageDialog(this, "Session terminated.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Session ID not found.", "Failure", JOptionPane.WARNING_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error terminating session: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}