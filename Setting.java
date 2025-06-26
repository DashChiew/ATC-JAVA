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
import java.io.IOException; // Important: Make sure this is imported!
import java.util.HashMap;
import java.util.Map;

// Assuming RoundImageLabel class is already defined in RoundImageLabel.java or accessible.
// import RoundImageLabel; // If it's in a separate package, you might need this.

public class Setting extends JFrame {

    private Student currentStudent;

    // --- Fonts and Colors for a ClassIn-like aesthetic ---
    private static final Font LABEL_FONT = new Font("Inter", Font.BOLD, 14); // Bold for field names
    private static final Font VALUE_FONT = new Font("Inter", Font.PLAIN, 14); // Regular for values
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
    JLabel levelLabel = new JLabel("Form Level:");
    JLabel subjectLabel = new JLabel("Subjects:");

    // --- JTextFields/JLabels for editable/display fields ---
    JLabel nameValueField = new JLabel(); // Now dynamic
    JTextField usernameField = new JTextField(20); // Now dynamic
    JPasswordField passwordField = new JPasswordField(20); // Changed to JPasswordField for security
    JTextField icNumberField = new JTextField(20); // Now dynamic
    JLabel emailValueField = new JLabel(); // Now dynamic
    JTextField contactNumberField = new JTextField(20); // Now dynamic
    JTextField addressField = new JTextField(20); // Now dynamic

    // --- JLabels for display-only fields (static data) ---
    JLabel levelValueLabel = new JLabel(); // Now dynamic
    JLabel subjectValueLabel = new JLabel(); // Now dynamic

    roundImageLabel profilePictureLabel;

    public Setting(Student student) { // Constructor now accepts a Student object
        this.currentStudent = student; // Store the current student

        // Set up the main JFrame properties
        setTitle("User Profile Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Set new frame size
        setSize(550, 600); // Slightly increased size for better spacing
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR); // Set the overall background color of the frame

        // --- Profile Picture Section (NORTH) ---
        // Assuming fei.png is the default profile pic and is accessible.
        // You might need to adjust the path if it's not in the project root.
        profilePictureLabel = new roundImageLabel("fei.png", 160);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(PANEL_BACKGROUND_COLOR); // White background for header panel
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0)); // More padding at top
        headerPanel.add(profilePictureLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- Form Section (CENTER) ---
        JPanel innerFormPanel = new JPanel(new GridLayout(0, 2, 15, 12)); // Increased horizontal (15) and vertical (12) gaps
        innerFormPanel.setBackground(PANEL_BACKGROUND_COLOR); // White background for the inner form panel
        innerFormPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Increased padding around the form

        // Apply styles to labels and fields using helper methods
        setupLabel(nameLabel);
        setupValueLabel(nameValueField);
        setupLabel(usernameLabel);
        setupTextField(usernameField);
        setupLabel(passwordLabel);
        setupPasswordField(passwordField); // Use JPasswordField setup
        setupLabel(icNumberLabel);
        setupTextField(icNumberField);
        setupLabel(emailLabel);
        setupValueLabel(emailValueField);
        setupLabel(contactNumberLabel);
        setupTextField(contactNumberField);
        setupLabel(addressLabel);
        setupTextField(addressField);
        setupLabel(levelLabel);
        setupValueLabel(levelValueLabel);
        setupLabel(subjectLabel);
        setupValueLabel(subjectValueLabel);

        // Add fields to the inner form panel
        innerFormPanel.add(nameLabel);
        innerFormPanel.add(nameValueField);

        innerFormPanel.add(usernameLabel);
        innerFormPanel.add(usernameField);

        innerFormPanel.add(passwordLabel);
        innerFormPanel.add(passwordField);

        innerFormPanel.add(icNumberLabel);
        innerFormPanel.add(icNumberField);

        innerFormPanel.add(emailLabel);
        innerFormPanel.add(emailValueField);

        innerFormPanel.add(contactNumberLabel);
        innerFormPanel.add(contactNumberField);

        innerFormPanel.add(addressLabel);
        innerFormPanel.add(addressField);

        innerFormPanel.add(levelLabel);
        innerFormPanel.add(levelValueLabel);

        innerFormPanel.add(subjectLabel);
        innerFormPanel.add(subjectValueLabel);

        // Populate fields with current student's data
        populateFields(currentStudent);

        // Use a wrapping panel for the form to manage its size within the BorderLayout.CENTER.
        // Set its background to match the main content area for a seamless look.
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
                // Update the currentStudent object with the new data from fields
                currentStudent.setName(nameValueField.getText()); // Still gets the current text
                currentStudent.setIcPassport(icNumberField.getText());
                currentStudent.setEmail(emailValueField.getText()); // Still gets the current text
                currentStudent.setContactNumber(contactNumberField.getText());
                currentStudent.setAddress(addressField.getText());
                currentStudent.setPassword(new String(passwordField.getPassword())); // Get password from JPasswordField

                // --- IMPORTANT: Save changes back to the files ---
                Map<String, Student> allStudents = readEnrollmentData(STUDENT_ENROLLMENT_FILE); // Re-read to get latest
                readStudentLoginData(STUDENTS_FILE, allStudents); // Re-read login data too

                // Update the specific current student in the map
                allStudents.put(currentStudent.getStudentId(), currentStudent);

                // Now write the updated map back to the files
                writeEnrollmentData(STUDENT_ENROLLMENT_FILE, allStudents);
                writeStudentLoginData(STUDENTS_FILE, allStudents);

                System.out.println("--- Profile Data Saved ---");
                System.out.println(currentStudent.toString()); // Print updated student object

                JOptionPane.showMessageDialog(Setting.this, "Profile saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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

    // Method to populate the UI fields with data from the Student object
    private void populateFields(Student student) {
        if (student != null) {
            nameValueField.setText(student.getName());
            usernameField.setText(student.getStudentId()); // Using studentId as username for now
            passwordField.setText(student.getPassword()); // Set password in JPasswordField
            icNumberField.setText(student.getIcPassport());
            emailValueField.setText(student.getEmail());
            contactNumberField.setText(student.getContactNumber());
            addressField.setText(student.getAddress());
            levelValueLabel.setText(student.getFormLevel());
            subjectValueLabel.setText(student.getSubjects());
        } else {
            // Clear fields or show default message if student is null
            nameValueField.setText("");
            usernameField.setText("");
            passwordField.setText("");
            icNumberField.setText("");
            emailValueField.setText("");
            contactNumberField.setText("");
            addressField.setText("");
            levelValueLabel.setText("");
            subjectValueLabel.setText("");
            System.err.println("No student data provided to populate settings.");
        }
    }

    // --- File Paths (Update these to your actual paths) ---
    // IMPORTANT: Make sure these paths are correct for your system!
    private static final String STUDENT_ENROLLMENT_FILE = "C:\\Users\\kaimi\\OneDrive\\Documents\\JAVA SEM 3\\Intellij Java\\ATC\\src\\student_enrollment.txt";
    private static final String STUDENTS_FILE = "C:\\Users\\kaimi\\OneDrive\\Documents\\JAVA SEM 3\\Intellij Java\\ATC\\src\\students.txt";

    // --- Static methods for reading student data from files ---

    // Reads student_enrollment.txt and returns a map of StudentId to Student objects
    private static Map<String, Student> readEnrollmentData(String filename) {
        Map<String, Student> studentData = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\"", ""); // Remove all quotes
                String[] parts = line.split(",");
                if (parts.length == 9) {
                    String studentId = parts[0].trim();
                    String name = parts[1].trim();
                    String icPassport = parts[2].trim();
                    String email = parts[3].trim();
                    String contactNumber = parts[4].trim();
                    String address = parts[5].trim();
                    String formLevel = parts[6].trim();
                    String subjects = parts[7].trim();
                    String enrollmentMonth = parts[8].trim();

                    Student student = new Student(studentId, name, icPassport, email,
                            contactNumber, address, formLevel,
                            subjects, enrollmentMonth);
                    studentData.put(studentId, student);
                } else {
                    System.err.println("Skipping malformed line in " + filename + ": " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + filename + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading student enrollment data: " + e.getMessage(), "File Read Error", JOptionPane.ERROR_MESSAGE);
        }
        return studentData;
    }

    // Reads students.txt and updates existing Student objects with password and role
    private static void readStudentLoginData(String filename, Map<String, Student> studentData) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String studentId = parts[0].trim();
                    String password = parts[1].trim();
                    String role = parts[2].trim();

                    Student student = studentData.get(studentId);
                    if (student != null) {
                        student.setPassword(password);
                        student.setRole(role);
                    } else {
                        System.err.println("Login data for unknown student ID: " + studentId + " in " + filename);
                    }
                } else {
                    System.err.println("Skipping malformed line in " + filename + ": " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + filename + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading student login data: " + e.getMessage(), "File Read Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Static methods for writing student data to files ---

    // Writes the map of Student objects back to student_enrollment.txt
    private static void writeEnrollmentData(String filename, Map<String, Student> studentData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Student student : studentData.values()) {
                // Format the line as it was read, including quotes around certain fields if necessary
                String line = String.format("%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                        student.getStudentId(),
                        student.getName(),
                        student.getIcPassport(),
                        student.getEmail(),
                        student.getContactNumber(),
                        student.getAddress(),
                        student.getFormLevel(),
                        student.getSubjects(),
                        student.getEnrollmentMonth());
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Successfully wrote updated enrollment data to: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to " + filename + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving enrollment data: " + e.getMessage(), "File Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Writes the password and role data back to students.txt
    private static void writeStudentLoginData(String filename, Map<String, Student> studentData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Student student : studentData.values()) {
                // Only write if password and role are available (i.e., student was in students.txt initially)
                // This prevents writing incomplete student data to the login file.
                if (student.getPassword() != null && student.getRole() != null) {
                    String line = String.format("%s,%s,%s",
                            student.getStudentId(),
                            student.getPassword(),
                            student.getRole());
                    bw.write(line);
                    bw.newLine();
                }
            }
            System.out.println("Successfully wrote updated login data to: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to " + filename + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving login data: " + e.getMessage(), "File Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        // Step 1: Read enrollment data
        Map<String, Student> allStudents = readEnrollmentData(STUDENT_ENROLLMENT_FILE);

        // Step 2: Read login data and merge it
        readStudentLoginData(STUDENTS_FILE, allStudents);

        // Step 3: Get a specific student (e.g., S001) to display in the settings
        Student studentToDisplay = allStudents.get("S001");

        if (studentToDisplay != null) {
            System.out.println("Loading settings for student: " + studentToDisplay.getName() + " (" + studentToDisplay.getStudentId() + ")");
            SwingUtilities.invokeLater(() -> {
                new Setting(studentToDisplay); // Pass the student object to the Setting frame
            });
        } else {
            System.err.println("Student S001 not found or data could not be loaded.");
            JOptionPane.showMessageDialog(null, "Student S001 not found or data could not be loaded. Please ensure 'S001' exists in your data files.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
