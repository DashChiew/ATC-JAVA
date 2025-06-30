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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects; // Needed for User class if not already imported

// Assuming RoundImageLabel class is already defined in RoundImageLabel.java or accessible.
// import RoundImageLabel; // If it's in a separate package, you might need this.

public class SettingForAdminReceptionist extends JFrame { // Class name changed

    private User currentUser; // Changed from Student to User

    // --- Fonts and Colors for a ClassIn-like aesthetic ---
    private static final Font LABEL_FONT = new Font("Inter", Font.BOLD, 14); // Bold for field names
    private static final Font VALUE_FONT = new Font("Inter", Font.PLAIN, 14); // Regular for non-editable values
    private static final Font TEXT_FIELD_FONT = new Font("Inter", Font.PLAIN, 14); // For input fields
    private static final Font BUTTON_FONT = new Font("Inter", Font.BOLD, 16); // For save button

    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Very light gray for JFrame background
    private static final Color PANEL_BACKGROUND_COLOR = Color.WHITE; // White for inner panels
    private static final Color TEXT_COLOR = new Color(50, 50, 50); // Darker gray for text
    private static final Color TEXT_FIELD_BACKGROUND = new Color(240, 240, 240); // Light gray for text field background
    private static final Color BORDER_COLOR = new Color(220, 220, 220); // Light gray for borders/separators
    private static final Color PRIMARY_BUTTON_COLOR = new Color(24, 144, 255); // Original blue
    private static final Color HOVER_BUTTON_COLOR = new Color(50, 168, 255); // Slightly brighter blue for hover

    // --- JLabels for field names ---
    JLabel nameLabel = new JLabel("Name:");
    JLabel usernameLabel = new JLabel("Username:");
    JLabel passwordLabel = new JLabel("Password:");
    JLabel icNumberLabel = new JLabel("IC/Passport:");
    JLabel emailLabel = new JLabel("Email:");
    JLabel contactNumberLabel = new JLabel("Contact Number:");
    JLabel addressLabel = new JLabel("Address:");
    JLabel roleLabel = new JLabel("Role:"); // New label for role

    // --- JTextFields/JLabels for editable/display fields ---
    JLabel nameValueLabel = new JLabel(); // Non-editable now
    JTextField usernameField = new JTextField(20); // Editable
    JPasswordField passwordField = new JPasswordField(20); // Editable
    JLabel icNumberValueLabel = new JLabel(); // Non-editable now
    JTextField emailField = new JTextField(20); // Editable
    JTextField contactNumberField = new JTextField(20); // Editable
    JTextField addressField = new JTextField(20); // Editable
    JLabel roleValueLabel = new JLabel(); // Non-editable now


    public SettingForAdminReceptionist(User user) { // Constructor name changed
        this.currentUser = user; // Store the current user

        // Set up the main JFrame properties
        setTitle("User Profile Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 600); // Slightly increased size for better spacing
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR); // Set the overall background color of the frame


        // --- Form Section (CENTER) ---
        JPanel innerFormPanel = new JPanel(new GridLayout(0, 2, 15, 12)); // Increased horizontal (15) and vertical (12) gaps
        innerFormPanel.setBackground(PANEL_BACKGROUND_COLOR); // White background for the inner form panel
        innerFormPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Increased padding around the form

        // Apply styles to labels and fields using helper methods
        setupLabel(nameLabel);
        setupValueLabel(nameValueLabel); // Use setupValueLabel for non-editable
        setupLabel(usernameLabel);
        setupTextField(usernameField); // Use setupTextField for editable
        setupLabel(passwordLabel);
        setupPasswordField(passwordField); // Use JPasswordField setup for editable
        setupLabel(icNumberLabel);
        setupValueLabel(icNumberValueLabel); // Use setupValueLabel for non-editable
        setupLabel(emailLabel);
        setupTextField(emailField); // Use setupTextField for editable
        setupLabel(contactNumberLabel);
        setupTextField(contactNumberField); // Use setupTextField for editable
        setupLabel(addressLabel);
        setupTextField(addressField); // Use setupTextField for editable
        setupLabel(roleLabel); // Setup for new role label
        setupValueLabel(roleValueLabel); // Use setupValueLabel for non-editable role

        // Add fields to the inner form panel in desired order
        innerFormPanel.add(nameLabel);
        innerFormPanel.add(nameValueLabel);

        innerFormPanel.add(usernameLabel);
        innerFormPanel.add(usernameField);

        innerFormPanel.add(passwordLabel);
        innerFormPanel.add(passwordField);

        innerFormPanel.add(icNumberLabel);
        innerFormPanel.add(icNumberValueLabel);

        innerFormPanel.add(emailLabel);
        innerFormPanel.add(emailField);

        innerFormPanel.add(contactNumberLabel);
        innerFormPanel.add(contactNumberField);

        innerFormPanel.add(addressLabel);
        innerFormPanel.add(addressField);

        innerFormPanel.add(roleLabel); // Add role label
        innerFormPanel.add(roleValueLabel); // Add role value label

        // Populate fields with current user's data
        populateFields(currentUser);

        // Use a wrapping panel for the form to manage its size within the BorderLayout.CENTER.
        JPanel contentPanelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contentPanelWrapper.setBackground(PANEL_BACKGROUND_COLOR);
        contentPanelWrapper.add(innerFormPanel);
        add(contentPanelWrapper, BorderLayout.CENTER);

        // --- Button Section (SOUTH) ---
        JButton saveButton = new JButton("Save Profile");
        saveButton.setFont(BUTTON_FONT);
        saveButton.setBackground(PRIMARY_BUTTON_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false); // Remove focus border
        saveButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25)); // More padding
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor on hover

        // Add MouseListener for hover effect
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                saveButton.setBackground(HOVER_BUTTON_COLOR); // Change to brighter color on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                saveButton.setBackground(PRIMARY_BUTTON_COLOR); // Revert to original color when mouse exits
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(PANEL_BACKGROUND_COLOR); // White background for button panel
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0)); // Increased bottom padding
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Add ActionListener to the Save Button ---
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the currentUser object with the new data from fields
                // Non-editable fields like name, icPassport, role are not changed here, as per requirement
                currentUser.setUsername(usernameField.getText());
                currentUser.setPassword(new String(passwordField.getPassword())); // Get password from JPasswordField
                currentUser.setEmail(emailField.getText());
                currentUser.setContactNumber(contactNumberField.getText());
                currentUser.setAddress(addressField.getText());

                // --- IMPORTANT: Save changes back to the file ---
                Map<String, User> allUsers = readUserData(USERS_FILE); // Re-read to get latest data
                // Update the specific current user in the map
                allUsers.put(currentUser.getUsername(), currentUser);

                // Now write the updated map back to the file
                writeUserData(USERS_FILE, allUsers);

                System.out.println("--- Profile Data Saved ---");
                System.out.println(currentUser.toString()); // Print updated user object

                JOptionPane.showMessageDialog(SettingForAdminReceptionist.this, "Profile saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close the settings window after saving
            }
        });

        setVisible(true);
    }

    // Helper method to style JLabels acting as field names
    private void setupLabel(JLabel label) {
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR);
    }

    // Helper method to style JLabels displaying non-editable values
    private void setupValueLabel(JLabel label) {
        label.setFont(VALUE_FONT);
        label.setForeground(TEXT_COLOR);
        label.setBackground(TEXT_FIELD_BACKGROUND); // Light background for display fields
        label.setOpaque(true); // Make background visible
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8) // Internal padding
        ));
    }

    // Helper method to style JTextFields
    private void setupTextField(JTextField textField) {
        textField.setFont(TEXT_FIELD_FONT);
        textField.setForeground(TEXT_COLOR);
        textField.setBackground(TEXT_FIELD_BACKGROUND);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8) // Internal padding
        ));
        textField.setCaretColor(TEXT_COLOR); // Color of the blinking cursor
    }

    // Helper method to style JPasswordFields
    private void setupPasswordField(JPasswordField passwordField) {
        passwordField.setFont(TEXT_FIELD_FONT);
        passwordField.setForeground(TEXT_COLOR);
        passwordField.setBackground(TEXT_FIELD_BACKGROUND);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8) // Internal padding
        ));
        passwordField.setCaretColor(TEXT_COLOR);
    }

    // Method to populate the UI fields with data from the User object
    private void populateFields(User user) {
        if (user != null) {
            nameValueLabel.setText(user.getName()); // Non-editable
            usernameField.setText(user.getUsername()); // Editable
            passwordField.setText(user.getPassword()); // Editable
            icNumberValueLabel.setText(user.getIcPassport()); // Non-editable
            emailField.setText(user.getEmail()); // Editable
            contactNumberField.setText(user.getContactNumber()); // Editable
            addressField.setText(user.getAddress()); // Editable
            roleValueLabel.setText(user.getRole()); // Non-editable
        } else {
            // Clear fields or show default message if user is null
            nameValueLabel.setText("");
            usernameField.setText("");
            passwordField.setText("");
            icNumberValueLabel.setText("");
            emailField.setText("");
            contactNumberField.setText("");
            addressField.setText("");
            roleValueLabel.setText("");
            System.err.println("No user data provided to populate settings.");
        }
    }

    // --- File Path (Update this to your actual path) ---
    // IMPORTANT: Make sure this path is correct for your system!
    private static final String USERS_FILE = "users.txt";

    // --- Static methods for reading/writing user data from/to files ---

    // Reads users.txt and returns a map of Username to User objects
    private static Map<String, User> readUserData(String filename) {
        Map<String, User> userData = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) { // Expect 8 parts for the User object
                    String name = parts[0].trim();
                    String username = parts[1].trim();
                    String password = parts[2].trim();
                    String icPassport = parts[3].trim();
                    String email = parts[4].trim();
                    String contactNumber = parts[5].trim();
                    String address = parts[6].trim();
                    String role = parts[7].trim();

                    User user = new User(name, username, password, icPassport, email, contactNumber, address, role);
                    userData.put(username, user); // Use username as the key
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

    // Writes the map of User objects back to users.txt
    private static void writeUserData(String filename, Map<String, User> userData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (User user : userData.values()) {
                // Ensure the User.toString() method correctly formats the line
                bw.write(user.toString());
                bw.newLine();
            }
            System.out.println("Successfully wrote updated user data to: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to " + filename + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving user data: " + e.getMessage(), "File Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}