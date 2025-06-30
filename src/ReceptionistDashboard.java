//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import javax.swing.border.EmptyBorder;
//import javax.swing.plaf.basic.BasicButtonUI;
//import java.awt.geom.RoundRectangle2D; // For custom button painting
//
//public class ReceptionistDashboard extends JFrame implements ActionListener {
//
//    private final loginPageTest loginPage;
//
//    private final JButton btnStudentOperations;
//    private final JButton btnPaymentOperations;
//    private final JButton btnSettings;
//    private final JButton btnLogout;
//
//    public ReceptionistDashboard(loginPageTest loginPage) {
//        this.loginPage = loginPage;
//        setTitle("Receptionist Dashboard");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setPreferredSize(new Dimension(550, 400)); // Adjusted size for dashboard
//
//        JPanel mainPanel = new JPanel();
//        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
//        mainPanel.setBackground(new Color(240, 242, 245)); // Light gray background
//
//        JLabel welcomeLabel = new JLabel("Welcome, Receptionist!");
//        welcomeLabel.setFont(new Font("Inter", Font.BOLD, 28));
//        welcomeLabel.setForeground(new Color(55, 65, 81));
//        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        welcomeLabel.setBorder(new EmptyBorder(0, 0, 30, 0)); // Padding below welcome message
//
//
//        // -------------------- Buttons Section --------------------
//        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 20, 20)); // 2 rows, 2 columns, with increased gaps
//        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//        buttonsPanel.setBackground(new Color(240, 242, 245)); // Match main background
//        buttonsPanel.setMaximumSize(new Dimension(500, 250)); // Limit size
//
//        btnStudentOperations = createStyledButton("Student Operations", new Color(24, 144, 255));
//        buttonsPanel.add(btnStudentOperations);
//        btnStudentOperations.addActionListener(e -> RStudentMenu.showStudentMenu(this));
//
//        btnPaymentOperations = createStyledButton("Payment Operations", new Color(72, 160, 220));
//        buttonsPanel.add(btnPaymentOperations);
//        btnPaymentOperations.addActionListener(e -> RPaymentMenuGUI.showPaymentMenu(this));
//
//        btnSettings = createStyledButton("Settings", new Color(100, 116, 139));
//        buttonsPanel.add(btnSettings);
//        btnSettings.addActionListener(this);
//
//        btnLogout = createStyledButton("Logout", new Color(200, 50, 50)); // Red for logout
//        buttonsPanel.add(btnLogout);
//        btnLogout.addActionListener(this);
//
//        // -------------------- Add sections to main panel --------------------
//        mainPanel.add(welcomeLabel);
//        mainPanel.add(buttonsPanel);
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
//        button.setFont(new Font("Inter", Font.BOLD, 16));
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
//        if (e.getSource() == btnSettings) {
//            JOptionPane.showMessageDialog(this, "Settings functionality coming soon!", "Settings", JOptionPane.INFORMATION_MESSAGE);
//        } else if (e.getSource() == btnLogout) {
//            if (this.loginPage != null) {
//                LogoutHandler.logout(this, this.loginPage);
//            } else {
//                JOptionPane.showMessageDialog(this,
//                        "Error: The login page reference is missing. Cannot log out.",
//                        "Logout Error",
//                        JOptionPane.ERROR_MESSAGE);
//                System.err.println("ReceptionistDashboard: loginPage reference is null during logout attempt.");
//            }
//        }
//    }
//}



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.geom.RoundRectangle2D; // For custom button painting

// Ensure User and SettingForAdminReceptionist are accessible (e.g., in the same package or imported)
// import User; // If User is in a different package
// import SettingForAdminReceptionist; // If SettingForAdminReceptionist is in a different package

public class ReceptionistDashboard extends JFrame implements ActionListener {

    private final loginPageTest loginPage;
    private User currentUser; // Add this field to store the logged-in User

    private final JButton btnStudentOperations;
    private final JButton btnPaymentOperations;
    private final JButton btnSettings;
    private final JButton btnLogout;

    // Modified constructor to accept the User object
    public ReceptionistDashboard(loginPageTest loginPage, User currentUser) {
        this.loginPage = loginPage;
        this.currentUser = currentUser; // Assign the current user

        setTitle("Receptionist Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(550, 400)); // Adjusted size for dashboard

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        mainPanel.setBackground(new Color(240, 242, 245)); // Light gray background

        JLabel welcomeLabel = new JLabel("Welcome, Receptionist!");
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getName() + "!"); // Personalize welcome message
        }
        welcomeLabel.setFont(new Font("Inter", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(55, 65, 81));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(new EmptyBorder(0, 0, 30, 0)); // Padding below welcome message


        // -------------------- Buttons Section --------------------
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 20, 20)); // 2 rows, 2 columns, with increased gaps
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonsPanel.setBackground(new Color(240, 242, 245)); // Match main background
        buttonsPanel.setMaximumSize(new Dimension(500, 250)); // Limit size

        btnStudentOperations = createStyledButton("Student Operations", new Color(24, 144, 255));
        buttonsPanel.add(btnStudentOperations);
        btnStudentOperations.addActionListener(e -> RStudentMenu.showStudentMenu(this));

        btnPaymentOperations = createStyledButton("Payment Operations", new Color(72, 160, 220));
        buttonsPanel.add(btnPaymentOperations);
        btnPaymentOperations.addActionListener(e -> RPaymentMenuGUI.showPaymentMenu(this));

        btnSettings = createStyledButton("Settings", new Color(100, 116, 139));
        buttonsPanel.add(btnSettings);
        btnSettings.addActionListener(this); // Action listener is defined below

        btnLogout = createStyledButton("Logout", new Color(200, 50, 50)); // Red for logout
        buttonsPanel.add(btnLogout);
        btnLogout.addActionListener(this);

        // -------------------- Add sections to main panel --------------------
        mainPanel.add(welcomeLabel);
        mainPanel.add(buttonsPanel);

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
        button.setFont(new Font("Inter", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Use the custom UI from RProfilePageGUI for animation
        // Ensure RProfilePageGUI.ButtonUIWithAnimation is accessible.
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
        if (e.getSource() == btnSettings) {
            // INTEGRATION: Open SettingForAdminReceptionist frame
            if (currentUser != null) {
                new SettingForAdminReceptionist(currentUser); // Pass the current user object
            } else {
                JOptionPane.showMessageDialog(this, "User data not available for profile settings.", "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("ReceptionistDashboard: currentUser is null when attempting to open settings.");
            }
        } else if (e.getSource() == btnLogout) {
            if (this.loginPage != null) {
                LogoutHandler.logout(this, this.loginPage);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error: The login page reference is missing. Cannot log out.",
                        "Logout Error",
                        JOptionPane.ERROR_MESSAGE);
                System.err.println("ReceptionistDashboard: loginPage reference is null during logout attempt.");
            }
        }
    }
}