package TutorApp.ui.frames;

import TutorApp.data.TutorProfileManager;
import common.ui.SRoundImageLabel;
import TutorApp.utils.FileUtil; // Using FileUtil for image placeholder

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class SettingForTutor extends JFrame {
    JLabel nameLabel = new JLabel("Name:");
    JLabel usernameLabel = new JLabel("Username:");
    JLabel emailLabel = new JLabel("Email:");
    JLabel contactNumberLabel = new JLabel("Contact Number:");
    JLabel addressLabel = new JLabel("Address:");
    JLabel subjectLabel = new JLabel("Subject:");

    JLabel nameValueLabel;
    JTextField usernameField;
    JLabel emailValueLabel;
    JTextField contactNumberField;
    JTextField addressField;
    JLabel subjectValueLabel;

    SRoundImageLabel profilePictureLabel;
    private String tutorUsername;

    private String originalId;
    private String originalUsername;
    private String originalContact;
    private String originalEmail;
    private String originalAddress;
    private String originalSubject;

    public SettingForTutor(String tutorUsername) {
        this.tutorUsername = tutorUsername;

        setTitle("User Profile Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        loadTutorData();

        nameValueLabel = new JLabel(originalUsername != null ? originalUsername : "N/A");
        usernameField = new JTextField(originalUsername != null ? originalUsername : "", 20);
        emailValueLabel = new JLabel(originalEmail != null ? originalEmail : (this.tutorUsername + "@gmail.com"));
        contactNumberField = new JTextField(originalContact != null ? originalContact : "", 20);
        addressField = new JTextField(originalAddress != null ? originalAddress : "", 20);
        subjectValueLabel = new JLabel(originalSubject != null ? originalSubject : "N/A");

        try {
            URL imageUrl = getClass().getResource("/xiaochou.png");
            File imageFile = null;

            if (imageUrl == null) {
                String userDir = System.getProperty("user.dir");
                imageFile = new File(userDir, "xiaochou.png");
                if (!imageFile.exists()) {
                    System.err.println("Local image 'xiaochou.png' not found in classpath or project root. Using placeholder.");
                    imageFile = FileUtil.createPlaceholderImage(160, 160, new Color(108, 99, 255), Color.WHITE, "Settings");
                }
            } else {
                imageFile = new File(imageUrl.toURI());
            }

            if (imageFile != null) {
                profilePictureLabel = new SRoundImageLabel(imageFile.getAbsolutePath(), 160);
            } else {
                profilePictureLabel = new SRoundImageLabel(null, 160);
                profilePictureLabel.setText("Image Error");
            }
        } catch (Exception e) {
            System.err.println("Error initializing settings profile image: " + e.getMessage());
            e.printStackTrace();
            profilePictureLabel = new SRoundImageLabel(null, 160);
            profilePictureLabel.setText("Image Error");
        }

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerPanel.add(profilePictureLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel innerFormPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        innerFormPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        innerFormPanel.add(nameLabel);
        innerFormPanel.add(nameValueLabel);

        innerFormPanel.add(usernameLabel);
        innerFormPanel.add(usernameField);

        innerFormPanel.add(emailLabel);
        innerFormPanel.add(emailValueLabel);

        innerFormPanel.add(contactNumberLabel);
        innerFormPanel.add(contactNumberField);

        innerFormPanel.add(addressLabel);
        innerFormPanel.add(addressField);

        innerFormPanel.add(subjectLabel);
        innerFormPanel.add(subjectValueLabel);

        add(innerFormPanel, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save Profile");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> saveProfile());

        setVisible(true);
    }

    private void loadTutorData() {
        String[] data = TutorProfileManager.getTutorProfileData(tutorUsername);
        if (data != null && data.length >= 6) {
            originalId = data[0].trim();
            originalUsername = data[1].trim();
            originalContact = data[2].trim();
            originalEmail = data[3].trim();
            originalAddress = data[4].trim();
            originalSubject = data[5].trim();
        } else {
            JOptionPane.showMessageDialog(this, "Profile data for '" + tutorUsername + "' not found.",
                    "Load Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveProfile() {
        String newUsername = usernameField.getText().trim();
        String newContact = contactNumberField.getText().trim();
        String newAddress = addressField.getText().trim();
        String newEmail = newUsername + "@gmail.com";

        if (newUsername.isEmpty() || newContact.isEmpty() || newAddress.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username, Contact Number, and Address cannot be empty.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = TutorProfileManager.updateTutorProfile(
                tutorUsername, originalId, newUsername, newContact, newEmail, newAddress, originalSubject);

        if (success) {
            JOptionPane.showMessageDialog(this, "Profile updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            if (!newUsername.equals(tutorUsername)) {
                JOptionPane.showMessageDialog(this, "Username changed. Please restart the application to see full changes reflected.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update profile. Original profile for '" + tutorUsername + "' might not be found.",
                    "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
