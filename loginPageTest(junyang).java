package com.tutorapp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Import statements for classes that are still needed
import com.tutorapp.data.User;
import com.tutorapp.utils.FileHandler;

public class loginPageTest extends JFrame implements ActionListener {

    JPasswordField password;
    JLabel labelPassword, labelUsername, attemptLabel;
    JTextField username;
    JButton button, resetButton;
    JCheckBox showPassword;
    private static final String USERS_FILE = "users.txt";
    private int loginAttempts = 0;
    private final int MAX_ATTEMPTS = 3;

    public loginPageTest() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Java GUI Login Form");
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.getContentPane().setBackground(new Color(0x222222)); // Darker background
        this.setVisible(true);

        initializeUsersFile(); // Ensure the users.txt file is set up

        // --- Username Label and Field ---
        labelUsername = new JLabel("Username:");
        labelUsername.setBounds(80, 180, 200, 40);
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
                    // Only handle 'tutor' and 'receptionist' roles, or a general success
                    if (userRole.equals("tutor")) {
                        JOptionPane.showMessageDialog(this, "Login Successful!\nRole: Tutor", "Success", JOptionPane.INFORMATION_MESSAGE);
                        TutorDashboardGUI tutorDashboard = new TutorDashboardGUI(userText, this); // Pass username and loginPageTest instance
                        tutorDashboard.setVisible(true);
                        this.setVisible(false); // Hide login page
                        clearFields(); // Prepare login page for next use
                    } else if (userRole.equals("receptionist")) {
                        JOptionPane.showMessageDialog(this, "Login Successful!\nRole: Receptionist", "Success", JOptionPane.INFORMATION_MESSAGE);
                        // Placeholder for Receptionist Dashboard
                        JOptionPane.showMessageDialog(this, "Receptionist Dashboard Coming Soon!");
                        this.setVisible(false);
                        clearFields();
                    } else {
                        // For any other role not explicitly handled, or if you want a generic message
                        JOptionPane.showMessageDialog(this, "Login Successful!\nRole: " + userRole + ". No specific dashboard implemented for this role yet.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        this.setVisible(false); // Still hide the login page
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
            if (parts.length == 3) { // Expecting username,password,role
                users.add(new User(parts[0].trim(), parts[1].trim(), parts[2].trim()));
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

        // Add a default tutor user if not present
        if (!lines.contains("tutor1,tutorpass,tutor")) {
            defaultUsersToAdd.add("tutor1,tutorpass,tutor");
        }
        // Add a default receptionist user if not present
        if (!lines.contains("receptionist1,recpass,receptionist")) {
            defaultUsersToAdd.add("receptionist1,recpass,receptionist");
        }

        // Append only if default users are found
        if (!defaultUsersToAdd.isEmpty()) {
            List<String> combinedLines = new ArrayList<>(lines);
            for (String newUserLine : defaultUsersToAdd) {
                if (!combinedLines.contains(newUserLine)) { // Avoid adding duplicates if they somehow got in during runtime
                    combinedLines.add(newUserLine);
                }
            }
            FileHandler.writeAllLines(USERS_FILE, combinedLines);
        }
    }

    // This helper is no longer strictly needed if we don't care about a default admin
    // but keeping it won't hurt, just ensure it's not used to *add* an admin.
    private boolean containsDefaultAdmin(List<String> lines) {
        for (String line : lines) {
            if (line.startsWith("CYChang,admin123,admin")) {
                return true;
            }
        }
        return false;
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(loginPageTest::new);
    }
}
