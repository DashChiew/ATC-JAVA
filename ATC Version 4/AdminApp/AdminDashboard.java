package AdminApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter; // Import for WindowListener
import java.awt.event.WindowEvent;   // Import for WindowEvent
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.List;

import ReceptionistApp.ReceptionistManagementGUI;
import common.auth.MainLoginPageTest;
import common.model.User;
import common.ui.SettingForAdminReceptionist;
import common.util.SessionManager;
import AdminApp.controller.AdminFunctionality; // Assuming this class exists
import ReceptionistApp.ReceptionistDashboard;
import StudentApp.SProfilePageGUI;
import common.util.LogoutHandler;
import AdminApp.AdminPaymentReport;
import AdminApp.AdminPaymentHistory;
import AdminApp.TutorManagementGUI;


public class AdminDashboard extends JFrame implements ActionListener {

    private final MainLoginPageTest loginPage;
    private User currentUser;

    private JButton btnRegisterTutor;
    private JButton btnDeleteTutor;
    private JButton btnAssignTutorToLevelSubject;
    private JButton btnDisplayTutor;

    private JButton btnRegisterReceptionist;
    private JButton btnDeleteReceptionist;
    private JButton btnDisplayReceptionist;

    private JButton btnViewMonthlyIncomeReport;
    private JButton btnUpdateMyProfile;
    // private JButton btnLogout; // Removed btnLogout
    private JButton btnViewSessions;
    private JButton btnTerminateSession;

    // --- Aesthetic Constants for ClassIn Theme (Refined for Neatness) ---
    private static final Font GENERAL_FONT = new Font("Inter", Font.PLAIN, 15);
    private static final Font TITLE_FONT = new Font("Inter", Font.BOLD, 30);
    private static final Font SECTION_TITLE_FONT = new Font("Inter", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Inter", Font.BOLD, 15);

    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250); // Light blue-gray background
    private static final Color PANEL_BACKGROUND_COLOR = Color.WHITE; // White for cards/panels
    private static final Color TITLE_TEXT_COLOR = new Color(36, 41, 46); // Dark gray for titles
    private static final Color GENERAL_TEXT_COLOR = new Color(108, 117, 125); // Muted gray for general text
    private static final Color ACCENT_BUTTON_COLOR = new Color(40, 120, 255); // ClassIn blue
    private static final Color DANGER_BUTTON_COLOR = new Color(220, 53, 69); // Red for danger
    private static final Color SECONDARY_BUTTON_COLOR = new Color(108, 117, 125); // Muted gray for secondary buttons
    private static final Color BORDER_COLOR = new Color(223, 227, 232); // Light gray for borders

    private static final int PANEL_RADIUS = 15; // Slightly rounded panels
    private static final int COMPONENT_RADIUS = 8; // More rounded components/buttons
    private static final int BORDER_WIDTH = 1;

    // Standardized width for button panels for neatness
    private static final int BUTTON_PANEL_MAX_WIDTH = 650;


    public AdminDashboard(MainLoginPageTest loginPage, User currentUser) {
        this.loginPage = loginPage;
        this.currentUser = currentUser;

        setTitle("Advance Tuition Center (ATC) - Admin Dashboard");
        // Changed default close operation to dispose the window only
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(750, 800));

        // Add WindowListener to handle closing event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Perform logout action when the window is closed
                if (AdminDashboard.this.loginPage != null) {
                    LogoutHandler.logout(AdminDashboard.this, AdminDashboard.this.loginPage);
                } else {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                            "Error: The login page reference is missing. Cannot log out properly.",
                            "Logout Error",
                            JOptionPane.ERROR_MESSAGE);
                    System.err.println("AdminDashboard: loginPage reference is null during window closing.");
                }
            }
        });


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        mainPanel.setBackground(BACKGROUND_COLOR);

        JLabel welcomeLabel = new JLabel("Welcome, Administrator!");
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getName() + "!");
        }
        welcomeLabel.setFont(TITLE_FONT);
        welcomeLabel.setForeground(TITLE_TEXT_COLOR);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(new EmptyBorder(0, 0, 40, 0)); // Increased bottom padding for title

        // -------------------- Tutor Management Section --------------------
        JLabel tutorLabel = new JLabel("Tutor Management");
        tutorLabel.setFont(SECTION_TITLE_FONT);
        tutorLabel.setForeground(TITLE_TEXT_COLOR);
        tutorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tutorLabel.setBorder(new EmptyBorder(25, 0, 15, 0)); // Consistent top and bottom padding

        JPanel tutorButtonsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        tutorButtonsPanel.setBackground(BACKGROUND_COLOR); // Use background color
        tutorButtonsPanel.setMaximumSize(new Dimension(BUTTON_PANEL_MAX_WIDTH, 150)); // Standardized max width
        tutorButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnRegisterTutor = createStyledButton("🖋️ Register Tutor", ACCENT_BUTTON_COLOR);
        tutorButtonsPanel.add(btnRegisterTutor);
        btnRegisterTutor.addActionListener(e -> TutorManagementGUI.showRegisterTutorDialog(this));

        btnDeleteTutor = createStyledButton("❌ Delete Tutor", DANGER_BUTTON_COLOR);
        tutorButtonsPanel.add(btnDeleteTutor);
        btnDeleteTutor.addActionListener(e -> TutorManagementGUI.showDeleteTutorDialog(this));

        btnAssignTutorToLevelSubject = createStyledButton("🏹 Assign Tutor to Level/Subject", ACCENT_BUTTON_COLOR); // Changed to accent color
        tutorButtonsPanel.add(btnAssignTutorToLevelSubject);
        btnAssignTutorToLevelSubject.addActionListener(e -> TutorManagementGUI.showAssignTutorDialog(this));

        btnDisplayTutor = createStyledButton("👩‍🏫 Display Tutors", SECONDARY_BUTTON_COLOR); // Changed to secondary color
        tutorButtonsPanel.add(btnDisplayTutor);
        btnDisplayTutor.addActionListener(e -> TutorManagementGUI.showAllTutorInfoDialog(this));


        // -------------------- Receptionist Management Section --------------------
        JLabel receptionistLabel = new JLabel("Receptionist Management");
        receptionistLabel.setFont(SECTION_TITLE_FONT);
        receptionistLabel.setForeground(TITLE_TEXT_COLOR);
        receptionistLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        receptionistLabel.setBorder(new EmptyBorder(35, 0, 15, 0)); // More vertical space from previous section

        JPanel receptionistButtonsContainer = new JPanel();
        receptionistButtonsContainer.setLayout(new BoxLayout(receptionistButtonsContainer, BoxLayout.Y_AXIS));
        receptionistButtonsContainer.setBackground(BACKGROUND_COLOR);
        receptionistButtonsContainer.setMaximumSize(new Dimension(BUTTON_PANEL_MAX_WIDTH, 150)); // Standardized max width
        receptionistButtonsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);


        JPanel firstRowReceptionistButtons = new JPanel(new GridLayout(1, 2, 20, 20));
        firstRowReceptionistButtons.setBackground(BACKGROUND_COLOR);
        firstRowReceptionistButtons.setMaximumSize(new Dimension(BUTTON_PANEL_MAX_WIDTH, 70)); // Standardized max width
        firstRowReceptionistButtons.setAlignmentX(Component.CENTER_ALIGNMENT);


        btnRegisterReceptionist = createStyledButton("🖋️ Register Receptionist", ACCENT_BUTTON_COLOR);
        firstRowReceptionistButtons.add(btnRegisterReceptionist);
        btnRegisterReceptionist.addActionListener(e -> ReceptionistManagementGUI.showRegisterReceptionistDialog(this));

        btnDeleteReceptionist = createStyledButton("❌ Delete Receptionist", DANGER_BUTTON_COLOR);
        firstRowReceptionistButtons.add(btnDeleteReceptionist);
        btnDeleteReceptionist.addActionListener(e -> ReceptionistManagementGUI.showDeleteReceptionistDialog(this));

        receptionistButtonsContainer.add(firstRowReceptionistButtons);


        JPanel secondRowReceptionistButton = new JPanel();
        secondRowReceptionistButton.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        secondRowReceptionistButton.setBackground(BACKGROUND_COLOR);
        secondRowReceptionistButton.setBorder(new EmptyBorder(20, 0, 0, 0));
        secondRowReceptionistButton.setAlignmentX(Component.CENTER_ALIGNMENT);


        btnDisplayReceptionist = createStyledButton("📙 Display Receptionists", SECONDARY_BUTTON_COLOR);
        secondRowReceptionistButton.add(btnDisplayReceptionist);
        btnDisplayReceptionist.addActionListener(e -> ReceptionistManagementGUI.showAllReceptionistInfoDialog(this));

        receptionistButtonsContainer.add(secondRowReceptionistButton);

        // -------------------- Security Section --------------------
        JLabel securityLabel = new JLabel("🛡️ Security");
        securityLabel.setFont(SECTION_TITLE_FONT);
        securityLabel.setForeground(TITLE_TEXT_COLOR);
        securityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        securityLabel.setBorder(new EmptyBorder(35, 0, 15, 0)); // More vertical space from previous section

        JPanel securityButtonsPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        securityButtonsPanel.setBackground(BACKGROUND_COLOR);
        securityButtonsPanel.setMaximumSize(new Dimension(BUTTON_PANEL_MAX_WIDTH, 75)); // Standardized max width
        securityButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnViewSessions = createStyledButton("View Active Sessions", SECONDARY_BUTTON_COLOR); // Using secondary color
        securityButtonsPanel.add(btnViewSessions);
        btnViewSessions.addActionListener(e -> viewSessions());

        btnTerminateSession = createStyledButton("Terminate Session", DANGER_BUTTON_COLOR);
        securityButtonsPanel.add(btnTerminateSession);
        btnTerminateSession.addActionListener(e -> terminateSession());

        // -------------------- Reports & Account Section --------------------
        JLabel generalLabel = new JLabel("Reports & Account");
        generalLabel.setFont(SECTION_TITLE_FONT);
        generalLabel.setForeground(TITLE_TEXT_COLOR);
        generalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        generalLabel.setBorder(new EmptyBorder(35, 0, 15, 0)); // More vertical space from previous section

        JPanel generalButtonsPanel = new JPanel(new GridLayout(1, 2, 20, 20)); // Changed to 1x2 grid
        generalButtonsPanel.setBackground(BACKGROUND_COLOR);
        generalButtonsPanel.setMaximumSize(new Dimension(BUTTON_PANEL_MAX_WIDTH, 75)); // Standardized max width
        generalButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnViewMonthlyIncomeReport = createStyledButton("🪙 View Income Report", SECONDARY_BUTTON_COLOR);
        generalButtonsPanel.add(btnViewMonthlyIncomeReport);
        btnViewMonthlyIncomeReport.addActionListener(this);

        btnUpdateMyProfile = createStyledButton("Update Profile", SECONDARY_BUTTON_COLOR);
        generalButtonsPanel.add(btnUpdateMyProfile);
        btnUpdateMyProfile.addActionListener(this);

        // btnLogout = createStyledButton("🔚 Logout", DANGER_BUTTON_COLOR); // Removed
        // generalButtonsPanel.add(btnLogout); // Removed
        // btnLogout.addActionListener(this); // Removed


        // -------------------- Add sections to main panel --------------------
        mainPanel.add(welcomeLabel);

        mainPanel.add(tutorLabel);
        mainPanel.add(tutorButtonsPanel);

        mainPanel.add(receptionistLabel);
        mainPanel.add(receptionistButtonsContainer);

        mainPanel.add(securityLabel);
        mainPanel.add(securityButtonsPanel);

        mainPanel.add(generalLabel);
        mainPanel.add(generalButtonsPanel);


        add(mainPanel);

        pack();
        setLocationRelativeTo(null);
        // setVisible(true); // Call this externally if you want to control visibility from MainLoginPageTest
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
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        // Use a consistent padding for text inside buttons
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setUI(new ButtonUIWithAnimation());

        // Mouse listeners for custom hover effect (brightness change and animation)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            private Color originalBg = bgColor; // Store original background color

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Brighten the button color on hover
                button.setBackground(originalBg.brighter());

                Object ui = ((JButton) evt.getSource()).getUI();
                if (ui instanceof ButtonUIWithAnimation customUI) {
                    customUI.startAnimation((JComponent) evt.getSource(), customUI.currentLift, customUI.LIFT_AMOUNT);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Restore original button color on exit
                button.setBackground(originalBg);

                Object ui = ((JButton) evt.getSource()).getUI();
                if (ui instanceof ButtonUIWithAnimation customUI) {
                    customUI.startAnimation((JComponent) evt.getSource(), customUI.currentLift, 0);
                }
            }
        });
        return button;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnViewMonthlyIncomeReport) {
            AdminPaymentReport.showPaymentMenu(this);
        }
        else if (e.getSource() == btnUpdateMyProfile) {
            if (currentUser != null) {
                new SettingForAdminReceptionist(currentUser);
            }
            else {
                JOptionPane.showMessageDialog(this, "User data not available for profile update.", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("AdminDashboard: currentUser is null when attempting to open profile settings.");
            }
        }
        // Removed the btnLogout handling
    }

    private void viewSessions() {
        try {
            List<String[]> sessions = SessionManager.listActiveSessions();
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

    // --- Nested Class for Custom Button UI with Animation ---
    class ButtonUIWithAnimation extends BasicButtonUI {
        // Changed arc to use COMPONENT_RADIUS for consistent rounding
        private final int arc = COMPONENT_RADIUS;
        private Timer animationTimer;
        public float currentLift = 0;
        public final int LIFT_AMOUNT = -5; // Subtle lift effect on hover
        private final int ANIMATION_DURATION = 150; // Milliseconds for animation

        ButtonUIWithAnimation() {
            // No-arg constructor
        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            // Mouse listeners are handled in createStyledButton
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            JButton button = (JButton) c;
            int width = button.getWidth();
            int height = button.getHeight();

            // Apply the lift for hover effect
            g2.translate(0, currentLift);

            // Draw a subtle shadow below the button (adjusted for lift)
            g2.setColor(new Color(0, 0, 0, 30)); // Slightly lighter shadow
            g2.fillRoundRect(2, 2 - (int)currentLift, width - 4, height - 4, arc, arc);

            // Draw the button's main shape
            g2.setColor(button.getBackground());
            g2.fillRoundRect(0, 0, width, height, arc, arc);

            // Paint the text and icon
            super.paint(g2, c);
            g2.dispose();
        }

        public void startAnimation(final JComponent component, final float startValue, final float endValue) {
            if (animationTimer != null && animationTimer.isRunning()) {
                animationTimer.stop();
            }

            long startTime = System.currentTimeMillis();
            animationTimer = new Timer(1000 / 30, e -> { // ~30 frames per second
                long elapsed = System.currentTimeMillis() - startTime;
                float progress = (float) elapsed / ANIMATION_DURATION;

                if (progress >= 1.0f) {
                    currentLift = endValue;
                    animationTimer.stop();
                } else {
                    // Smooth interpolation
                    currentLift = startValue + (endValue - startValue) * progress;
                }
                component.repaint(); // Request repaint to show animation frame
            });
            animationTimer.setInitialDelay(0); // Start immediately
            animationTimer.start();
        }
    }
}