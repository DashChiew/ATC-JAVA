package ReceptionistApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import common.auth.MainLoginPageTest;
import common.model.User;
import common.ui.SettingForAdminReceptionist;
import java.awt.event.WindowAdapter; // NEW IMPORT: For handling window events
import java.awt.event.WindowEvent;  // NEW IMPORT: For handling window events

public class ReceptionistDashboard extends JFrame implements ActionListener {
    private final MainLoginPageTest loginPage;
    private User currentUser;

    private final JButton btnStudentOperations;
    private final JButton btnPaymentOperations;
    private final JButton btnSettings;

    public ReceptionistDashboard(MainLoginPageTest loginPage, User currentUser) {
        this.loginPage = loginPage;
        this.currentUser = currentUser;

        setTitle("Receptionist Dashboard");
        // CHANGED: Use DISPOSE_ON_CLOSE instead of EXIT_ON_CLOSE
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 630);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 242, 245));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        mainPanel.setBackground(new Color(240, 242, 245));

        // Welcome Panel
        JPanel welcomePanel = new RoundedPanel(20, false);
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        welcomePanel.setMaximumSize(new Dimension(500, 120));

        JLabel welcomeLabel = new JLabel("Welcome, " + (currentUser != null ? currentUser.getName() : "Receptionist") + "!");
        welcomeLabel.setFont(new Font("Inter", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(41, 47, 54));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomePanel.add(welcomeLabel);

        // Buttons Panel
        JPanel buttonsPanel = new RoundedPanel(20, false);
        buttonsPanel.setLayout(new GridLayout(3, 1, 15, 15));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        buttonsPanel.setMaximumSize(new Dimension(500, 350));

        btnStudentOperations = createStyledButton("Student Operations", new Color(24, 144, 255));
        btnPaymentOperations = createStyledButton("Payment Operations", new Color(72, 160, 220));
        btnSettings = createStyledButton("Settings", new Color(100, 116, 139));

        btnStudentOperations.addActionListener(e -> RStudentMenu.showStudentMenu(this));
        btnPaymentOperations.addActionListener(e -> RPaymentMenuGUI.showPaymentMenu(this));
        btnSettings.addActionListener(this);

        buttonsPanel.add(btnStudentOperations);
        buttonsPanel.add(btnPaymentOperations);
        buttonsPanel.add(btnSettings);

        mainPanel.add(welcomePanel);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(buttonsPanel);

        add(mainPanel);

        // NEW: Add a WindowListener to handle closing the frame
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // When this frame is closed, show the login page
                if (loginPage != null) {
                    loginPage.setVisible(true);
                    loginPage.clearFields(); // Clear fields for the next login attempt
                }
            }
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Inter", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setUI(new MainLoginPageTest.ClassInButtonUI(25));
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSettings) {
            if (currentUser != null) {
                new SettingForAdminReceptionist(currentUser);
            } else {
                JOptionPane.showMessageDialog(this, "User data not available for settings.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class RoundedPanel extends JPanel {
        private int arcRadius;
        private boolean hasShadow;

        public RoundedPanel(int arcRadius, boolean hasShadow) {
            this.arcRadius = arcRadius;
            this.hasShadow = hasShadow;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            if (hasShadow) {
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(5, 5, width - 5, height - 5, arcRadius * 2, arcRadius * 2);
            }

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, width, height, arcRadius * 2, arcRadius * 2);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}