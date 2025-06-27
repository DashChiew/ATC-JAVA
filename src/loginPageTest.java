import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class loginPageTest extends JFrame implements ActionListener {

    JPasswordField password;
    JLabel labelPassword, labelUsername, attemptLabel;
    JTextField username;
    JButton button, resetButton;
    JCheckBox showPassword;
    private static final String USERS_FILE = "users.txt";
    private int loginAttempts = 0;
    private final int MAX_ATTEMPTS = 3;

    loginPageTest() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Java GUI Login Form");
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.getContentPane().setBackground(new Color(0x222222)); // Darker background
        this.setVisible(true);

        initializeUsersFile(); // Ensure the users.txt file is set up

        // --- Username Label and Field ---
        labelUsername = new JLabel("Username/TP Number:");
        labelUsername.setBounds(10, 180, 200, 40);
        labelUsername.setForeground(new Color(0, 246, 51)); // Greenish color
        labelUsername.setFont(new Font("Arial", Font.BOLD, 16));
        username = new JTextField();
        username.setBounds(190, 180, 200, 40);
        username.setBackground(new Color(0x333333)); // Darker field background
        username.setForeground(Color.WHITE); // White text
        username.setCaretColor(Color.WHITE); // White caret

        // --- Password Label and Field ---
        labelPassword = new JLabel("Password:");
        labelPassword.setBounds(80, 250, 100, 40);
        labelPassword.setForeground(new Color(0, 246, 51));
        labelPassword.setFont(new Font("Arial", Font.BOLD, 16));
        password = new JPasswordField();
        password.setBounds(190, 250, 200, 40);
        password.setBackground(new Color(0x333333));
        password.setForeground(Color.WHITE);
        password.setCaretColor(Color.WHITE);


        // --- Show Password Checkbox ---
        showPassword = new JCheckBox("Show Password");
        showPassword.setBounds(190, 300, 150, 30);
        showPassword.addActionListener(this);
        showPassword.setForeground(Color.WHITE);
        showPassword.setBackground(new Color(0x222222)); // Match frame background
        showPassword.setFocusPainted(false); // Remove focus border

        // --- Sign In Button ---
        button = new JButton("Sign In");
        button.setBounds(150, 350, 200, 40);
        button.addActionListener(this);
        button.setBackground(new Color(0, 150, 0)); // Greenish background
        button.setForeground(Color.WHITE); // White text
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);

        // --- Reset Button ---
        resetButton = new JButton("Reset");
        resetButton.setBounds(150, 400, 200, 40);
        resetButton.addActionListener(this);
        resetButton.setBackground(new Color(200, 50, 50)); // Reddish background
        resetButton.setForeground(Color.WHITE);
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.setFocusPainted(false);

        // --- Attempts Label ---
        attemptLabel = new JLabel("Attempts remaining: " + (MAX_ATTEMPTS - loginAttempts));
        attemptLabel.setBounds(150, 450, 250, 30); // Adjusted position
        attemptLabel.setForeground(Color.WHITE);
        attemptLabel.setFont(new Font("Arial", Font.PLAIN, 14));


        // --- Add Components to Frame ---
        this.add(labelUsername);
        this.add(labelPassword);
        this.add(password);
        this.add(username);
        this.add(button);
        this.add(resetButton);
        this.add(showPassword);
        this.add(attemptLabel); // Ensure attemptLabel is added
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            // Check if max attempts reached BEFORE processing login
            if (loginAttempts >= MAX_ATTEMPTS) {
                JOptionPane.showMessageDialog(this, "Maximum login attempts reached. System will exit.", "Login Locked", JOptionPane.ERROR_MESSAGE);
                // Use a Timer to exit after a delay, giving user time to read message
                Timer timer = new Timer(2000, ev -> System.exit(0));
                timer.setRepeats(false);
                timer.start();
                return; // Prevent further action
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
                    JOptionPane.showMessageDialog(this, "Login Successful!\nRole: " + userRole, "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Open appropriate dashboard based on role
                    if (userRole.equals("admin")) {
                        AdminDashboard dashboard = new AdminDashboard(this);
                        dashboard.setVisible(true);
                        this.setVisible(false);
                        clearFields();
                    }
                    else if (userRole.equals("tutor")) {
                        // Placeholder for Tutor Dashboard
                        JOptionPane.showMessageDialog(this, "Tutor Dashboard Coming Soon!");
                        this.setVisible(false);
                        clearFields();
                    }
                    else if (userRole.equals("receptionist")) {
                        // Launch the Receptionist Dashboard (RProfilePageGUI)
                        RProfilePageGUI receptionistDashboard = new RProfilePageGUI(this);
                        receptionistDashboard.setVisible(true);
                        this.setVisible(false); // Hide the login page
                        clearFields(); // Clear fields and reset attempts for next potential login (after logout)
                    }
                    else if (userRole.equals("student")) {
                        // Placeholder for Student Dashboard
                        JOptionPane.showMessageDialog(this, "Student Dashboard Coming Soon!");
                        this.setVisible(false);
                        clearFields();
                    }
                } else {
                    loginAttempts++; // Increment only on failed attempts
                    attemptLabel.setText("Attempts remaining: " + (MAX_ATTEMPTS - loginAttempts));

                    if (loginAttempts >= MAX_ATTEMPTS) {
                        button.setEnabled(false); // Disable login button
                        JOptionPane.showMessageDialog(this,
                                "Maximum login attempts reached. System will exit.",
                                "Login Failed",
                                JOptionPane.ERROR_MESSAGE);
                        // Use a Timer to exit after a delay
                        Timer timer = new Timer(2000, ev -> System.exit(0)); // 2 second delay
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Invalid username or password.\nAttempts remaining: " + (MAX_ATTEMPTS - loginAttempts),
                                "Login Failed",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error reading user data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        if (e.getSource() == resetButton) {
            clearFields(); // Reset fields and attempts
            button.setEnabled(true); // Re-enable login button on reset
            // Do not reset loginAttempts to 0 here if you want login attempts to persist across "resets" within the same session.
            // If you want reset to clear attempts, uncomment the line below.
            // loginAttempts = 0;
            // attemptLabel.setText("Attempts remaining: " + (MAX_ATTEMPTS - loginAttempts));
        }

        if (e.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                password.setEchoChar((char) 0); // Show characters
            } else {
                password.setEchoChar('*'); // Hide characters
            }
        }
    }

    /**
     * Clears username and password fields and resets login attempts.
     * This method is called upon successful login and upon logout to prepare
     * the login page for the next user.
     */
    public void clearFields() {
        username.setText("");
        password.setText("");
        loginAttempts = 0; // Reset attempts when fields are cleared/login page is shown again
        attemptLabel.setText("Attempts remaining: " + (MAX_ATTEMPTS - loginAttempts));
        button.setEnabled(true); // Re-enable login button
        password.setEchoChar('*'); // Ensure password is hidden by default
        showPassword.setSelected(false); // Uncheck show password
    }

    // Reads user data from the USERS_FILE.
    private List<User> readUsers() throws IOException {
        List<String> lines = FileHandler.readAllLines(USERS_FILE);
        List<User> users = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            // Ensure that the line has exactly 8 parts to match the User constructor
            if (parts.length == 8) {
                users.add(new User(
                        parts[0].trim(), // Name
                        parts[1].trim(), // Username
                        parts[2].trim(), // Password
                        parts[3].trim(), // IC/Passport
                        parts[4].trim(), // Email
                        parts[5].trim(), // Contact Number
                        parts[6].trim(), // Address
                        parts[7].trim()  // Role
                ));
            } else {
                System.err.println("Skipping malformed user line (expected 8 parts, found " + parts.length + "): " + line);
            }
        }
        return users;
    }

    // Initializes the users file with default users if it's empty or doesn't exist.
    private void initializeUsersFile() {
        if (!FileHandler.createFileIfNotExists(USERS_FILE)) {
            JOptionPane.showMessageDialog(this, "Failed to create users file! Application may not function correctly.", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> lines = FileHandler.readAllLines(USERS_FILE);
        List<String> defaultUsersToAdd = new ArrayList<>();

        // Define the default admin user with all fields in the new format
        // Format: Name,Username,Password,IC/Passport,Email,ContactNumber,Address,Role
        String defaultAdminUserLine = "Admin User,CYChang,admin123,123456-78-9012,admin@example.com,012-3456789,123 Admin Street,admin";

        // Check if the new format admin user line is not present
        if (!lines.contains(defaultAdminUserLine)) {
            // Also check for the old format admin user line to prevent re-adding if only format changed
            if (!lines.contains("CYChang,admin123,admin")) {
                defaultUsersToAdd.add(defaultAdminUserLine);
            } else {
                // If old format exists, but new one doesn't, this implies an upgrade scenario.
                // In a real application, you might want to remove the old line and add the new one.
                // For simplicity here, we'll just add the new one if the old one is there but not the new one.
                // This will result in two admin entries if old one isn't manually removed, but ensures new format is present.
                // A more robust solution would involve parsing and updating existing entries.
                defaultUsersToAdd.add(defaultAdminUserLine);
            }
        }

        // Append new default users only if there are any to add
        if (!defaultUsersToAdd.isEmpty()) {
            List<String> combinedLines = new ArrayList<>(lines);
            for(String userLine : defaultUsersToAdd) {
                if (!combinedLines.contains(userLine)) { // Double check to avoid adding if already present from other methods/previous runs
                    combinedLines.add(userLine);
                }
            }
            FileHandler.writeAllLines(USERS_FILE, combinedLines);
        }
    }

    // Helper to check if default admin user already exists
    private boolean containsDefaultAdmin(List<String> lines) {
        for (String line : lines) {
            if (line.startsWith("CYChang,admin123,admin") ||
                    line.startsWith("Roxanne,receptionist123,receptionist") ||
                    line.startsWith("JohnDoe,tutor123,tutor") ||
                    line.startsWith("JaneDoe,student123,student")) { // Added check for student
                return true;
            }
        }
        return false;
    }
}
