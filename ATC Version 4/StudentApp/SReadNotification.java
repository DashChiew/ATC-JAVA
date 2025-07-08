package StudentApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import StudentApp.model.SStudent; // Make sure this import path is correct for your SStudent model

public class SReadNotification extends JFrame {

    private JFrame parentFrame; // Reference to the SProfilePageGUI frame
    private SStudent loggedInSStudent; // The student for whom to display notifications

    private JTextArea notificationArea;
    private static final String NOTIFICATION_FILE = "notification.txt"; // The file containing notifications

    /**
     * Constructor for SReadNotification.
     * @param parentFrame The calling JFrame (SProfilePageGUI) to return to.
     * @param student The SStudent object of the currently logged-in student.
     */
    public SReadNotification(JFrame parentFrame, SStudent student) {
        this.parentFrame = parentFrame;
        this.loggedInSStudent = student;

        setTitle("Student Notifications");
        setSize(450, 400); // Set preferred size for the notification window
        setLocationRelativeTo(null); // Center the window on the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this window, but don't exit the application

        // Add a WindowListener to show the parent frame when this one is closed (e.g., via the 'X' button)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (SReadNotification.this.parentFrame != null) {
                    SReadNotification.this.parentFrame.setVisible(true); // Show the profile page again
                }
            }
        });

        // Main panel with a border layout for organizing components
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)); // 10px gap horizontally and vertically
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Padding around the edges
        mainPanel.setBackground(new Color(240, 242, 245)); // Light gray background

        // Label for the section title
        JLabel titleLabel = new JLabel("Your Notifications:");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 16));
        titleLabel.setForeground(new Color(55, 65, 81));
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0)); // Padding below the title

        // JTextArea to display notifications
        notificationArea = new JTextArea();
        notificationArea.setEditable(false); // Make it read-only
        notificationArea.setFont(new Font("Inter", Font.PLAIN, 14));
        notificationArea.setForeground(new Color(55, 65, 81));
        notificationArea.setBackground(Color.WHITE);
        notificationArea.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1)); // Simple border
        notificationArea.setLineWrap(true); // Wrap text at word boundaries
        notificationArea.setWrapStyleWord(true); // Don't break words

        JScrollPane scrollPane = new JScrollPane(notificationArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(173, 216, 230), 2)); // Slightly thicker border for scroll pane

        // Back button to return to the profile page
        JButton backButton = new JButton("Back to Profile");
        backButton.setFont(new Font("Inter", Font.BOLD, 14));
        backButton.setBackground(new Color(100, 116, 139)); // Consistent color with Settings button
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false); // No focus border
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding for the button content
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current notification frame
                if (SReadNotification.this.parentFrame != null) {
                    SReadNotification.this.parentFrame.setVisible(true); // Make the parent profile page visible
                }
            }
        });

        // Panel to hold the back button and center it
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false); // Make it transparent
        buttonPanel.add(backButton);

        // Add components to the main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH); // Title at the top
        mainPanel.add(scrollPane, BorderLayout.CENTER); // Scrollable text area in the center
        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // Back button at the bottom

        add(mainPanel); // Add the main panel to the frame

        // Load and display notifications specific to the logged-in student
        loadNotifications();
    }

    /**
     * Reads notifications from the NOTIFICATION_FILE and displays those specific
     * to the loggedInSStudent in the notificationArea.
     */
    private void loadNotifications() {
        notificationArea.setText(""); // Clear any existing text
        if (loggedInSStudent == null || loggedInSStudent.getName() == null) {
            notificationArea.setText("Error: Student information not available to load notifications.");
            System.err.println("SReadNotification: loggedInSStudent or its name is null.");
            return;
        }

        String studentName = loggedInSStudent.getName();
        StringBuilder notificationsBuilder = new StringBuilder();
        int notificationCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(NOTIFICATION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into three parts: studentName, timestamp, message
                // Limit to 3 to handle cases where the message itself contains commas
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    String fileStudentName = parts[0].trim();
                    String timestamp = parts[1].trim();
                    String message = parts[2].trim();

                    // Check if the notification is for the current logged-in student
                    if (fileStudentName.equalsIgnoreCase(studentName)) { // Case-insensitive comparison for student name
                        notificationsBuilder.append("[").append(timestamp).append("] ").append(message).append("\n\n");
                        notificationCount++;
                    }
                } else {
                    System.err.println("Skipping malformed notification line: " + line);
                }
            }
        } catch (IOException e) {
            // Display an error message if the file cannot be read
            notificationsBuilder.append("Error loading notifications: Could not read notification file.\n");
            notificationsBuilder.append("Please ensure '").append(NOTIFICATION_FILE).append("' exists and is accessible.");
            System.err.println("Error reading notification file: " + e.getMessage());
        }

        // Set the text of the notification area based on what was found
        if (notificationCount == 0) {
            notificationArea.setText("No new notifications for " + studentName + ".");
        } else {
            notificationArea.setText(notificationsBuilder.toString());
            notificationArea.setCaretPosition(0); // Scroll to the top of the text area
        }
    }

    // Main method for testing SReadNotification directly (optional)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create a dummy SStudent for testing purposes
            SStudent dummyStudent = new SStudent();
            dummyStudent.setName("DashChiew"); // This name must match a name in your notification.txt
            dummyStudent.setLoginUsername("D001"); // Example username, not strictly used in this class

            // When testing directly, pass null for the parentFrame
            new SReadNotification(null, dummyStudent).setVisible(true);
        });
    }
}