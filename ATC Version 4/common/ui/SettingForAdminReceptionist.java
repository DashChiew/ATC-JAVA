package common.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import common.model.User;
// REMOVED SRoundImageLabel - not explicitly used for the main profile pic in the new layout

public class SettingForAdminReceptionist extends JFrame {

    private User currentUser;

    // --- Fonts and Colors (Copied from SettingForTutor for consistency) ---
    private static final Font LABEL_FONT = new Font("Inter", Font.BOLD, 14);
    private static final Font VALUE_FONT = new Font("Inter", Font.PLAIN, 14); // Not directly used with addFormField but kept for reference
    private static final Font TEXT_FIELD_FONT = new Font("Inter", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Inter", Font.BOLD, 16);
    private static final Font BACK_BUTTON_FONT = new Font("Inter", Font.PLAIN, 14); // Added for consistency

    private static final Color BACKGROUND_COLOR = new Color(240, 242, 245); // Consistent with TProfilePageGUI
    private static final Color PANEL_BACKGROUND_COLOR = Color.WHITE;
    private static final Color TITLE_TEXT_COLOR = new Color(55, 65, 81);
    private static final Color GENERAL_TEXT_COLOR = new Color(75, 85, 99);
    private static final Color ACCENT_BUTTON_COLOR = new Color(24, 144, 255);
    private static final Color RETURN_BUTTON_BG_COLOR = new Color(100, 116, 139);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);

    // Define radius for rounded corners (consistent with SSettingForTutor)
    private static final int PANEL_RADIUS = 20;
    private static final int COMPONENT_RADIUS = 8;
    private static final int BORDER_WIDTH = 1;

    // File path for user data
    private static final String USERS_FILE = "users.txt";

    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField contactNumberField;
    private JTextArea addressArea;

    private JTextField usernameField;

    public SettingForAdminReceptionist(User user) {
        this.currentUser = user;

        setTitle("User Profile Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(BACKGROUND_COLOR);
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(rootPanel);

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainContentPanel.setBackground(BACKGROUND_COLOR);
        rootPanel.add(mainContentPanel, BorderLayout.CENTER);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JLabel headerLabel = new JLabel("User Profile Settings", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Inter", Font.BOLD, 28));
        headerLabel.setForeground(TITLE_TEXT_COLOR);
        headerPanel.add(headerLabel);
        mainContentPanel.add(headerPanel);

        common.ui.RoundedPanel formCardPanel = new common.ui.RoundedPanel(PANEL_RADIUS, false);
        formCardPanel.setLayout(new GridBagLayout());
        formCardPanel.setBackground(PANEL_BACKGROUND_COLOR);
        formCardPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        formCardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCardPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));

        JScrollPane scrollPane = new JScrollPane(formCardPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainContentPanel.add(scrollPane);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        addFormField(formCardPanel, gbc, "Name:", currentUser.getName(), false, row++);

        usernameField = addFormField(formCardPanel, gbc, "Username:", currentUser.getUsername(), true, row++);

        passwordField = new JPasswordField(currentUser.getPassword());
        passwordField.setFont(TEXT_FIELD_FONT);
        passwordField.setBorder(new common.ui.RoundedBorder(COMPONENT_RADIUS, BORDER_COLOR, BORDER_WIDTH));
        passwordField.setPreferredSize(new Dimension(200, 30));

        passwordField.addMouseListener(new MouseAdapter() {
            private char defaultEchoChar = passwordField.getEchoChar();
            @Override
            public void mouseEntered(MouseEvent e) {
                passwordField.setEchoChar((char) 0);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                passwordField.setEchoChar(defaultEchoChar);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formCardPanel.add(new JLabel("Password:", JLabel.LEFT), gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formCardPanel.add(passwordField, gbc);
        row++;

        addFormField(formCardPanel, gbc, "IC/Passport:", currentUser.getIcPassport(), false, row++);

        emailField = addFormField(formCardPanel, gbc, "Email:", currentUser.getEmail(), true, row++);

        contactNumberField = addFormField(formCardPanel, gbc, "Contact Number:", currentUser.getContactNumber(), true, row++);

        addressArea = addTextAreaField(formCardPanel, gbc, "Address:", currentUser.getAddress(), true, row++);

        addFormField(formCardPanel, gbc, "Role:", currentUser.getRole(), false, row++);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        formCardPanel.add(Box.createVerticalGlue(), gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        mainContentPanel.add(buttonPanel);

        JButton saveButton = new JButton("Save Changes");
        saveButton.setFont(BUTTON_FONT);
        saveButton.setBackground(ACCENT_BUTTON_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(new common.ui.RoundedBorder(COMPONENT_RADIUS, ACCENT_BUTTON_COLOR, BORDER_WIDTH));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setUI(new ButtonUIWithAnimation());

        JButton returnButton = new JButton("Return");
        returnButton.setFont(BACK_BUTTON_FONT);
        returnButton.setBackground(RETURN_BUTTON_BG_COLOR);
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusPainted(false);
        returnButton.setBorder(new common.ui.RoundedBorder(COMPONENT_RADIUS, RETURN_BUTTON_BG_COLOR, BORDER_WIDTH));
        returnButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        returnButton.setUI(new ButtonUIWithAnimation());

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUsername = usernameField.getText().trim();
                String newPassword = new String(passwordField.getPassword()).trim();
                String newEmail = emailField.getText().trim();
                String newContact = contactNumberField.getText().trim();
                String newAddress = addressArea.getText().trim();

                if (newUsername.isEmpty() || newPassword.isEmpty() || newEmail.isEmpty() || newContact.isEmpty() || newAddress.isEmpty()) {
                    JOptionPane.showMessageDialog(SettingForAdminReceptionist.this, "All editable fields must be filled.",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String originalUsername = currentUser.getUsername();
                if (!originalUsername.equals(newUsername)) {
                    Map<String, User> allUsersCheck = readAllUserData(USERS_FILE);
                    if (allUsersCheck.containsKey(newUsername)) {
                        JOptionPane.showMessageDialog(SettingForAdminReceptionist.this, "Username '" + newUsername + "' already exists. Please choose a different username.",
                                "Validation Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                currentUser.setUsername(newUsername);
                currentUser.setPassword(newPassword);
                currentUser.setEmail(newEmail);
                currentUser.setContactNumber(newContact);
                currentUser.setAddress(newAddress);

                saveUserData(USERS_FILE, currentUser, originalUsername);
                JOptionPane.showMessageDialog(SettingForAdminReceptionist.this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(returnButton);
        buttonPanel.add(saveButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JTextField addFormField(JPanel parent, GridBagConstraints gbc, String labelText, String value, boolean editable, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setForeground(GENERAL_TEXT_COLOR);

        JTextField textField = new JTextField(value);
        textField.setFont(TEXT_FIELD_FONT);
        textField.setEditable(editable);

        if (editable) {
            textField.setBorder(new common.ui.RoundedBorder(COMPONENT_RADIUS, BORDER_COLOR, BORDER_WIDTH));
            textField.setPreferredSize(new Dimension(200, 30));
        } else {
            textField.setBorder(null);
            textField.setBackground(parent.getBackground());
            textField.setOpaque(true);
            textField.setFocusable(false);
        }

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        parent.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        parent.add(textField, gbc);

        return textField;
    }

    private JTextArea addTextAreaField(JPanel parent, GridBagConstraints gbc, String labelText, String value, boolean editable, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setForeground(GENERAL_TEXT_COLOR);

        JTextArea textArea = new JTextArea(value);
        textArea.setFont(TEXT_FIELD_FONT);
        textArea.setEditable(editable);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(200, 80));

        if (editable) {
            textArea.setBorder(new common.ui.RoundedBorder(COMPONENT_RADIUS, BORDER_COLOR, BORDER_WIDTH));
            scrollPane.getViewport().setBorder(null);
        } else {
            textArea.setBorder(null);
            textArea.setBackground(parent.getBackground());
            textArea.setOpaque(true);
            textArea.setFocusable(false);

            scrollPane.setBorder(null);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.getViewport().setBackground(parent.getBackground());
        }

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        parent.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        parent.add(scrollPane, gbc);

        return textArea;
    }

    private static Map<String, User> readAllUserData(String filename) {
        Map<String, User> userData = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = parseCsvLine(line);
                if (parts.length == 8) {
                    String name = parts[0].trim();
                    String username = parts[1].trim();
                    String password = parts[2].trim();
                    String icPassport = parts[3].trim();
                    String email = parts[4].trim();
                    String contactNumber = parts[5].trim();
                    String address = parts[6].trim();
                    String role = parts[7].trim();

                    User user = new User(name, username, password, icPassport, email, contactNumber, address, role);
                    userData.put(username, user);
                } else {
                    System.err.println("Skipping malformed line in " + filename + " (expected 8 parts): " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + filename + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading user data: " + e.getMessage(), "File Read Error", JOptionPane.ERROR_MESSAGE);
        }
        return userData;
    }

    private static void saveUserData(String filename, User updatedUser, String originalUsername) {
        Map<String, User> allUsers = readAllUserData(filename);

        if (!originalUsername.equals(updatedUser.getUsername())) {
            allUsers.remove(originalUsername);
        }
        allUsers.put(updatedUser.getUsername(), updatedUser);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (User user : allUsers.values()) {
                // Manually construct the CSV string for the User object
                String csvLine = String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                        escapeCsv(user.getName()),
                        escapeCsv(user.getUsername()),
                        escapeCsv(user.getPassword()),
                        escapeCsv(user.getIcPassport()),
                        escapeCsv(user.getEmail()),
                        escapeCsv(user.getContactNumber()),
                        escapeCsv(user.getAddress()),
                        escapeCsv(user.getRole()));
                bw.write(csvLine);
                bw.newLine();
            }
            System.out.println("Successfully wrote updated user data to: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to " + filename + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving user data: " + e.getMessage(), "File Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to parse CSV lines (copied from FileUtil or common.util.FileHandler if it exists)
    private static String[] parseCsvLine(String line) {
        List<String> parts = new ArrayList<>();
        boolean inQuote = false;
        StringBuilder currentPart = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuote = !inQuote;
            } else if (c == ',' && !inQuote) {
                parts.add(currentPart.toString());
                currentPart = new StringBuilder();
            } else {
                currentPart.append(c);
            }
        }
        parts.add(currentPart.toString());
        return parts.toArray(new String[0]);
    }

    // Helper method to escape strings for CSV output
    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}