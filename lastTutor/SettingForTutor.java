package TutorApp.ui.frames;
import common.model.User;

// Removed TutorApp.data.TutorProfileManager
// Removed common.ui.SRoundImageLabel (will use a simple JLabel or keep SRoundImageLabel if it's generally useful for profile pics)

import TutorApp.utils.FileUtil; // Using FileUtil for image placeholder, can be replaced by common.util.FileHandler if it includes image utils later

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.model.User; // Import the User class
import common.ui.SRoundImageLabel; // Assuming this is needed for the profile picture

public class SettingForTutor extends JFrame {

    private User currentTutorUser; // Now holding the User object instead of just username

    // --- Fonts and Colors (copied from SettingForAdminReceptionist for consistency) ---
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

    // UI Components
    JLabel nameLabel = new JLabel("Name:");
    JLabel usernameLabel = new JLabel("Username:");
    JLabel passwordLabel = new JLabel("Password:");
    JLabel icNumberLabel = new JLabel("IC/Passport:");
    JLabel emailLabel = new JLabel("Email:");
    JLabel contactNumberLabel = new JLabel("Contact Number:");
    JLabel addressLabel = new JLabel("Address:");
    JLabel roleLabel = new JLabel("Role:");

    JLabel nameValueLabel;          // Display only
    JTextField usernameField;       // Editable
    JPasswordField passwordField;   // Editable
    JLabel icNumberValueLabel;      // Display only
    JTextField emailField;          // Editable
    JTextField contactNumberField;  // Editable
    JTextField addressField;        // Editable
    JLabel roleValueLabel;          // Display only

    SRoundImageLabel profilePictureLabel; // Keeping this for the image display

    // File path for user data
    private static final String USERS_FILE = "users.txt";

    /**
     * Constructor for the SettingForTutor frame.
     * @param tutorUser The User object of the currently logged-in tutor.
     */
    public SettingForTutor(User tutorUser) {
        this.currentTutorUser = tutorUser;

        setTitle("Tutor Profile Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // --- Profile Picture Section (NORTH) ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerPanel.setBackground(BACKGROUND_COLOR); // Match frame background

        int imageSize = 160;
        try {
            URL imageUrl = getClass().getResource("/xiaochou.png");
            File imageFile = null;

            if (imageUrl == null) {
                String userDir = System.getProperty("user.dir");
                imageFile = new File(userDir, "xiaochou.png");
                if (!imageFile.exists()) {
                    System.err.println("Local image 'xiaochou.png' not found in classpath or project root. Using placeholder.");
                    imageFile = FileUtil.createPlaceholderImage(imageSize, imageSize, new Color(108, 99, 255), Color.WHITE, "Tutor");
                }
            } else {
                imageFile = new File(imageUrl.toURI());
            }

            if (imageFile != null) {
                profilePictureLabel = new SRoundImageLabel(imageFile.getAbsolutePath(), imageSize);
            } else {
                profilePictureLabel = new SRoundImageLabel(null, imageSize);
                profilePictureLabel.setText("Image Error");
            }
        } catch (Exception e) {
            System.err.println("Error initializing settings profile image: " + e.getMessage());
            e.printStackTrace();
            profilePictureLabel = new SRoundImageLabel(null, imageSize);
            profilePictureLabel.setText("Image Error");
        }
        headerPanel.add(profilePictureLabel);
        add(headerPanel, BorderLayout.NORTH);


        // --- Initialize UI Components with data from currentTutorUser ---
        nameValueLabel = new JLabel();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        icNumberValueLabel = new JLabel();
        emailField = new JTextField();
        contactNumberField = new JTextField();
        addressField = new JTextField();
        roleValueLabel = new JLabel();

        populateFields(currentTutorUser); // Populate fields with the passed User object

        // --- Form Section (CENTER) ---
        JPanel innerFormPanel = new JPanel(new GridLayout(0, 2, 15, 12));
        innerFormPanel.setBackground(PANEL_BACKGROUND_COLOR);
        innerFormPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Apply styles to labels and fields using helper methods
        setupLabel(nameLabel);
        setupValueLabel(nameValueLabel);
        setupLabel(usernameLabel);
        setupTextField(usernameField);
        setupLabel(passwordLabel);
        setupPasswordField(passwordField);
        setupLabel(icNumberLabel);
        setupValueLabel(icNumberValueLabel);
        setupLabel(emailLabel);
        setupTextField(emailField);
        setupLabel(contactNumberLabel);
        setupTextField(contactNumberField);
        setupLabel(addressLabel);
        setupTextField(addressField);
        setupLabel(roleLabel);
        setupValueLabel(roleValueLabel);

        // Add fields to the inner form panel
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
        innerFormPanel.add(roleLabel);
        innerFormPanel.add(roleValueLabel);

        // --- Wrap the innerFormPanel in a JScrollPane ---
        JScrollPane scrollPane = new JScrollPane(innerFormPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Typically you don't want horizontal scroll for forms
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default scroll pane border
        scrollPane.getViewport().setBackground(PANEL_BACKGROUND_COLOR); // Match inner panel background

        JPanel contentPanelWrapper = new JPanel(new BorderLayout()); // Change to BorderLayout
        contentPanelWrapper.setBackground(BACKGROUND_COLOR); // Match frame background
        contentPanelWrapper.add(scrollPane, BorderLayout.CENTER); // Add scrollPane to the wrapper
        contentPanelWrapper.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); // Add some padding around the scroll pane

        add(contentPanelWrapper, BorderLayout.CENTER);


        // --- Button Section (SOUTH) ---
        JButton saveButton = new JButton("Save Profile");
        saveButton.setFont(BUTTON_FONT);
        saveButton.setBackground(PRIMARY_BUTTON_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                saveButton.setBackground(HOVER_BUTTON_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                saveButton.setBackground(PRIMARY_BUTTON_COLOR);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR); // Match frame background
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Add ActionListener to the Save Button ---
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProfile();
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
        label.setBackground(TEXT_FIELD_BACKGROUND);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    // Helper method to style JTextFields
    private void setupTextField(JTextField textField) {
        textField.setFont(TEXT_FIELD_FONT);
        textField.setForeground(TEXT_COLOR);
        textField.setBackground(TEXT_FIELD_BACKGROUND);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        textField.setCaretColor(TEXT_COLOR);
    }

    // Helper method to style JPasswordFields
    private void setupPasswordField(JPasswordField passwordField) {
        passwordField.setFont(TEXT_FIELD_FONT);
        passwordField.setForeground(TEXT_COLOR);
        passwordField.setBackground(TEXT_FIELD_BACKGROUND);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        passwordField.setCaretColor(TEXT_COLOR);
    }

    /**
     * Populates the UI fields with data from the current User object.
     * @param user The User object containing profile data.
     */
    private void populateFields(User user) {
        if (user != null) {
            nameValueLabel.setText(user.getName());
            usernameField.setText(user.getUsername());
            passwordField.setText(user.getPassword());
            icNumberValueLabel.setText(user.getIcPassport());
            emailField.setText(user.getEmail());
            contactNumberField.setText(user.getContactNumber());
            addressField.setText(user.getAddress());
            roleValueLabel.setText(user.getRole());
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
            System.err.println("No user data provided to populate tutor settings.");
        }
    }

    /**
     * Handles the saving of the tutor's updated profile to the users.txt file.
     */
    private void saveProfile() {
        String originalUsername = currentTutorUser.getUsername(); // Store original username for lookup

        // Get new values from editable fields
        String newUsername = usernameField.getText().trim();
        String newPassword = new String(passwordField.getPassword()).trim();
        String newEmail = emailField.getText().trim();
        String newContact = contactNumberField.getText().trim();
        String newAddress = addressField.getText().trim();

        // Basic validation
        if (newUsername.isEmpty() || newPassword.isEmpty() || newEmail.isEmpty() || newContact.isEmpty() || newAddress.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All editable fields must be filled.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Update the currentTutorUser object with new values
        currentTutorUser.setUsername(newUsername);
        currentTutorUser.setPassword(newPassword);
        currentTutorUser.setEmail(newEmail);
        currentTutorUser.setContactNumber(newContact);
        currentTutorUser.setAddress(newAddress);

        // Read all users, update the specific user, and write back
        Map<String, User> allUsers = readUserData(USERS_FILE);

        // Remove the old entry if username changed, then add/update with the new object
        if (!originalUsername.equals(newUsername)) {
            allUsers.remove(originalUsername); // Remove old entry
        }
        allUsers.put(newUsername, currentTutorUser); // Add/update with potentially new username

        writeUserData(USERS_FILE, allUsers);

        JOptionPane.showMessageDialog(this, "Profile updated successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);

        if (!originalUsername.equals(newUsername)) {
            JOptionPane.showMessageDialog(this, "Username changed. Please restart the application or re-login to see full changes reflected.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }
        this.dispose(); // Close the settings window
    }

    /**
     * Reads all lines from the USERS_FILE and parses them into a Map of User objects.
     * @param filename The path to the users.txt file.
     * @return A Map where keys are usernames and values are User objects.
     */
    private static Map<String, User> readUserData(String filename) {
        Map<String, User> userData = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Use FileUtil.parseCsvLine if common.util.FileHandler is not directly available here
                // or if FileUtil already handles this better.
                // Assuming parseCsvLine from FileUtil.java, if not, copy it here or from SettingForAdminReceptionist.
                String[] parts = FileUtil.parseCsvLine(line); // Changed to use TutorApp.utils.FileUtil

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

    /**
     * Writes the map of User objects back to the USERS_FILE, overwriting its content.
     * @param filename The path to the users.txt file.
     * @param userData The Map of User objects to write.
     */
    private static void writeUserData(String filename, Map<String, User> userData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (User user : userData.values()) {
                // The User.toString() method should correctly format the line for writing
                // Example: Name,Username,Password,IC/Passport,Email,ContactNumber,Address,Role
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