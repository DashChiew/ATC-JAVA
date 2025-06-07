import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class loginPageTest extends JFrame implements ActionListener {

    JPasswordField password;
    JLabel labelPassword, labelUsername, title, attemptLabel;
    JTextField username;
    JButton button, resetButton;
    JCheckBox showPassword;
    private static final String USERS_FILE = "C:\\Users\\WhyNo\\IdeaProjects\\Testing\\src\\users.txt";
    private int loginAttempts = 0;
    private final int MAX_ATTEMPTS = 3;

    loginPageTest() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Java GUI Login Form");
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.getContentPane().setBackground(new Color(23));
        this.setVisible(true);

        labelUsername = new JLabel("Username");
        labelUsername.setBounds(80, 180, 200, 40);
        labelUsername.setForeground(new Color(255, 230, 0));
        username = new JTextField();
        username.setBounds(150, 180, 200, 40);

        labelPassword = new JLabel("Password");
        labelPassword.setBounds(80, 250, 100, 40);
        labelPassword.setForeground(new Color(255, 230, 0));
        password = new JPasswordField();
        password.setBounds(150, 250, 200, 40);

        button = new JButton("Sign in");
        button.setBounds(150, 350, 200, 40);
        button.addActionListener(this);

        resetButton = new JButton("Reset");
        resetButton.setBounds(150, 400, 200, 40);
        resetButton.addActionListener(this);

        showPassword = new JCheckBox("Show Password");
        showPassword.setBounds(150, 300, 150, 30);
        showPassword.addActionListener(this);

        attemptLabel = new JLabel("Attempts remaining: " + (MAX_ATTEMPTS - loginAttempts));
        attemptLabel.setBounds(150, 420, 200, 30);
        attemptLabel.setForeground(Color.WHITE);

        this.add(labelUsername);
        this.add(labelPassword);
        this.add(password);
        this.add(username);
        this.add(button);
        this.add(resetButton);
        this.add(showPassword);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            if (loginAttempts >= MAX_ATTEMPTS) {
                JOptionPane.showMessageDialog(this, "Maximum login attempts reached. System will exit.");
                System.exit(0);
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
                    JOptionPane.showMessageDialog(this, "Login Successful\nRole: " + userRole);
                    // Open appropriate dashboard based on role
                    if (userRole.equals("admin")) {
                        new AdminDashboard().setVisible(true);
                        this.dispose();
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
                        Timer timer = new Timer(3000, ev -> System.exit(0));
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Invalid username or password\nAttempts remaining: " + (MAX_ATTEMPTS - loginAttempts),
                                "Login Failed",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error reading user data");
                ex.printStackTrace();
            }
        }

        if (e.getSource() == resetButton) {
            username.setText("");
            password.setText("");
        }

        if (e.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                password.setEchoChar((char) 0);
            } else {
                password.setEchoChar('*');
            }
        }
    }

    private List<User> readUsers() throws IOException {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    users.add(new User(parts[0], parts[1], parts[2]));
                }
            }
        }
        return users;
    }

}

class User {
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username; }
    public String getPassword() {
        return password; }
    public String getRole() {
        return role; }
}

// Simple example of what an admin dashboard might look like
class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new JLabel("Welcome Admin!", SwingConstants.CENTER));
    }
}