package common.auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import TutorApp.TProfilePageGUI;
import common.model.User;
import common.util.SessionManager;
import common.util.FileHandler;
import AdminApp.AdminDashboard;
import ReceptionistApp.ReceptionistDashboard;
import StudentApp.SProfilePageGUI;


public class MainLoginPageTest extends JFrame implements ActionListener {

    JPasswordField password;
    JLabel labelPassword, labelUsername, attemptLabel;
    JTextField username;
    JButton button, resetButton;
    JCheckBox showPassword;
    private static final String USERS_FILE = "users.txt";
    private int loginAttempts = 0;
    private final int MAX_ATTEMPTS = 3;

    public MainLoginPageTest() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Advance Tuition Center (ATC)");
        this.setSize(500, 650);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(new Color(240, 242, 245));

        initializeUsersFile();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        mainPanel.setBackground(new Color(240, 242, 245));

        // Title Label
        JLabel titleLabel = new JLabel("Welcome to ATC");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 32));
        titleLabel.setForeground(new Color(41, 47, 54));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(40));

        // Login Panel (now a custom RoundedPanel)
        RoundedPanel loginPanel = new RoundedPanel(20, true);
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.setMaximumSize(new Dimension(400, 450));


        // --- Username Label and Field ---
        labelUsername = new JLabel("Username/TP Number");
        labelUsername.setFont(new Font("Inter", Font.PLAIN, 15));
        labelUsername.setForeground(new Color(75, 85, 99));
        labelUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        username = new JTextField();
        username.setFont(new Font("Inter", Font.PLAIN, 16));
        username.setBorder(BorderFactory.createCompoundBorder(
                new RoundedCornerBorder(10),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        username.setBackground(new Color(248, 249, 250));
        username.setForeground(new Color(41, 47, 54));
        username.setCaretColor(new Color(64, 158, 255));
        username.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // --- Password Label and Field ---
        labelPassword = new JLabel("Password");
        labelPassword.setFont(new Font("Inter", Font.PLAIN, 15));
        labelPassword.setForeground(new Color(75, 85, 99));
        labelPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        password = new JPasswordField();
        password.setFont(new Font("Inter", Font.PLAIN, 16));
        password.setBorder(BorderFactory.createCompoundBorder(
                new RoundedCornerBorder(10),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        password.setBackground(new Color(248, 249, 250));
        password.setForeground(new Color(41, 47, 54));
        password.setCaretColor(new Color(64, 158, 255));
        password.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // --- Show Password Checkbox ---
        showPassword = new JCheckBox("Show Password");
        showPassword.addActionListener(this);
        showPassword.setForeground(new Color(75, 85, 99));
        showPassword.setBackground(Color.WHITE);
        showPassword.setFont(new Font("Inter", Font.PLAIN, 13));
        showPassword.setFocusPainted(false);
        showPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        showPassword.setIconTextGap(8);
        // NEW: Ensure checkbox does not stretch and aligns properly to the left
        showPassword.setMaximumSize(showPassword.getPreferredSize());


        // --- Sign In Button ---
        button = new JButton("Sign In");
        button.addActionListener(this);
        button.setBackground(new Color(64, 158, 255));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Inter", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setUI(new ClassInButtonUI(25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- Reset Button ---
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        resetButton.setBackground(new Color(144, 160, 175));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFont(new Font("Inter", Font.BOLD, 18));
        resetButton.setFocusPainted(false);
        resetButton.setUI(new ClassInButtonUI(25));
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));


        // Panel to hold buttons for horizontal centering
        JPanel buttonContainerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonContainerPanel.setOpaque(false);
        buttonContainerPanel.add(button);
        buttonContainerPanel.add(resetButton);


        // --- Attempts Label ---
        attemptLabel = new JLabel("Attempts remaining: " + (MAX_ATTEMPTS - loginAttempts));
        attemptLabel.setForeground(new Color(153, 153, 153));
        attemptLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        attemptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        // Add components to loginPanel
        loginPanel.add(labelUsername);
        loginPanel.add(Box.createVerticalStrut(8));
        loginPanel.add(username);
        loginPanel.add(Box.createVerticalStrut(20));

        loginPanel.add(labelPassword);
        loginPanel.add(Box.createVerticalStrut(8));
        loginPanel.add(password);
        loginPanel.add(Box.createVerticalStrut(10));

        loginPanel.add(showPassword);
        loginPanel.add(Box.createVerticalStrut(30));

        loginPanel.add(buttonContainerPanel);
        loginPanel.add(Box.createVerticalStrut(20));

        loginPanel.add(attemptLabel);


        // Add loginPanel to mainPanel
        mainPanel.add(loginPanel);

        // Add mainPanel to frame
        this.add(mainPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            if (loginAttempts >= MAX_ATTEMPTS) {
                JOptionPane.showMessageDialog(this, "Maximum login attempts reached. System will exit.", "Login Locked", JOptionPane.ERROR_MESSAGE);
                Timer timer = new Timer(2000, ev -> System.exit(0));
                timer.setRepeats(false);
                timer.start();
                return;
            }

            String userText = username.getText();
            String pwdText = new String(password.getPassword());

            try {
                List<User> users = readUsers();
                boolean loginSuccess = false;
                String userRole = "";

                for (User user : users) {
                    if (user.getUsername().equals(userText) && user.getPassword().equals(pwdText)) {
                        loginSuccess = true;
                        userRole = user.getRole();
                        break;
                    }
                }

                if (loginSuccess) {
                    try {
                        String sessionId = SessionManager.createSession(userText, userRole);
                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(this, "Warning: Failed to create session entry.", "Session Warning", JOptionPane.WARNING_MESSAGE);
                    }


                    JOptionPane.showMessageDialog(this, "Login Successful!\nRole: " + userRole, "Success", JOptionPane.INFORMATION_MESSAGE);
                    if (userRole.equals("admin")) {
                        User loggedInUser = null;
                        for (User user : users) {
                            if (user.getUsername().equals(userText) && user.getPassword().equals(pwdText)) {
                                loggedInUser = user;
                                break;
                            }
                        }

                        if (loggedInUser != null) {
                            AdminDashboard dashboard = new AdminDashboard(this, loggedInUser);
                            dashboard.setVisible(true);
                            this.setVisible(false);
                            clearFields();
                        } else {
                            JOptionPane.showMessageDialog(this, "Error: Logged-in user data not found.", "Internal Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else if (userRole.equals("tutor")) {
                        TutorApp.TProfilePageGUI tutorDashboard = new TProfilePageGUI(this, userText);
                        tutorDashboard.setVisible(true);
                        this.setVisible(false);
                        clearFields();
                    }
                    else if (userRole.equals("receptionist")) {
                        User loggedInUser = null;
                        for (User user : users) {
                            if (user.getUsername().equals(userText) && user.getPassword().equals(pwdText)) {
                                loggedInUser = user;
                                break;
                            }
                        }

                        if (loggedInUser != null) {
                            ReceptionistDashboard dashboard = new ReceptionistDashboard(this, loggedInUser);
                            dashboard.setVisible(true);
                            this.setVisible(false);
                            clearFields();
                        } else {
                            JOptionPane.showMessageDialog(this, "Error: Logged-in receptionist data not found.", "Internal Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else if (userRole.equals("student")) {
                        // MODIFIED: Pass the actual loginUsername (userText) to SProfilePageGUI
                        SProfilePageGUI studentDashboard = new SProfilePageGUI(this, userText);
                        studentDashboard.setVisible(true);
                        this.setVisible(false);
                        clearFields();
                    }
                }
                else {
                    loginAttempts++;
                    attemptLabel.setText("Attempts remaining: " + (MAX_ATTEMPTS - loginAttempts));

                    if (loginAttempts >= MAX_ATTEMPTS) {
                        button.setEnabled(false);
                        JOptionPane.showMessageDialog(this,
                                "Maximum login attempts reached. System will exit.",
                                "Login Failed",
                                JOptionPane.ERROR_MESSAGE);
                        Timer timer = new Timer(2000, ev -> System.exit(0));
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Invalid username or password.\nAttempts remaining: " + (MAX_ATTEMPTS - loginAttempts),
                                "Login Failed",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error reading user data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        if (e.getSource() == resetButton) {
            clearFields();
            button.setEnabled(true);
        }

        if (e.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                password.setEchoChar((char) 0);
            } else {
                password.setEchoChar('*');
            }
        }
    }

    public void clearFields() {
        username.setText("");
        password.setText("");
        loginAttempts = 0;
        attemptLabel.setText("Attempts remaining: " + (MAX_ATTEMPTS - loginAttempts));
        button.setEnabled(true);
        password.setEchoChar('*');
        showPassword.setSelected(false);
    }

    private List<User> readUsers() throws IOException {
        List<String> lines = FileHandler.readAllLines(USERS_FILE);
        List<User> users = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 8) {
                users.add(new User(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim(),
                        parts[5].trim(),
                        parts[6].trim(),
                        parts[7].trim()
                ));
            } else {
                System.err.println("Skipping malformed user line (expected 8 parts, found " + parts.length + "): " + line);
            }
        }

        List<String> studentLines = FileHandler.readAllLines("students.txt");
        for (String line : studentLines) {
            String[] parts = line.split(",");
            if (parts.length >= 8) { // Ensure minimum fields
                users.add(new User(
                        parts[0].trim(), // Name
                        parts[1].trim(), // Username (S001)
                        parts[2].trim(), // Password
                        parts[3].trim(), // IC
                        parts[4].trim(), // Email
                        parts[5].trim(), // Contact
                        parts[6].trim(), // Address
                        "student"        // Hardcoded role
                ));
            }
        }

        return users;
    }

    private void initializeUsersFile() {
        if (!FileHandler.createFileIfNotExists(USERS_FILE)) {
            JOptionPane.showMessageDialog(this, "Failed to create users file! Application may not function correctly.", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> lines = FileHandler.readAllLines(USERS_FILE);
        List<String> defaultUsersToAdd = new ArrayList<>();

        String defaultAdminUserLine = "Admin User,CYChang,admin123,123456-78-9012,admin@example.com,012-3456789,123 Admin Street,admin";

        if (!lines.contains(defaultAdminUserLine)) {
            defaultUsersToAdd.add(defaultAdminUserLine);
        }

        if (!defaultUsersToAdd.isEmpty()) {
            List<String> combinedLines = new ArrayList<>(lines);
            for(String userLine : defaultUsersToAdd) {
                if (!combinedLines.contains(userLine)) {
                    combinedLines.add(userLine);
                }
            }
            FileHandler.writeAllLines(USERS_FILE, combinedLines);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainLoginPageTest::new);
    }

    // --- Custom UI Classes for ClassIn Design ---

    // Custom Border for rounded text fields
    public static class RoundedCornerBorder extends EmptyBorder {
        private final int radius;

        public RoundedCornerBorder(int radius) {
            super(radius, radius, radius, radius);
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getBackground().darker());
            g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius * 2, radius * 2));
            g2.dispose();
        }
    }

    // Custom Button UI for rounded corners, shadow, and hover animation
    public static class ClassInButtonUI extends BasicButtonUI {
        private final int arc;
        private Timer animationTimer;
        private float currentLift = 0;
        private final int LIFT_AMOUNT = -5;
        private final int ANIMATION_DURATION = 150;

        public ClassInButtonUI(int arc) {
            this.arc = arc;
        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            c.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    startAnimation((JButton) e.getSource(), currentLift, LIFT_AMOUNT);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    startAnimation((JButton) e.getSource(), currentLift, 0);
                }
            });
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            JButton button = (JButton) c;
            int width = button.getWidth();
            int height = button.getHeight();

            g2.translate(0, currentLift);

            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillRoundRect(2, 2 - (int)currentLift, width - 4, height - 4, arc, arc);

            g2.setColor(button.getBackground());
            g2.fillRoundRect(0, 0, width, height, arc, arc);

            super.paint(g2, c);
            g2.dispose();
        }

        private void startAnimation(final JComponent component, final float startValue, final float endValue) {
            if (animationTimer != null && animationTimer.isRunning()) {
                animationTimer.stop();
            }

            long startTime = System.currentTimeMillis();
            animationTimer = new Timer(1000 / 30, e -> {
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

    // Custom JPanel for rounded corners and optional shadow
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