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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Setting extends JFrame {

    private Student currentStudent; // Assuming Student class is defined elsewhere
    private ProfilePageGUI profilePageGUI; // NEW: Reference to the ProfilePageGUI

    // --- Fonts and Colors for a ClassIn-like aesthetic ---
    private static final Font LABEL_FONT = new Font("Inter", Font.BOLD, 14);
    private static final Font VALUE_FONT = new Font("Inter", Font.PLAIN, 14);
    private static final Font TEXT_FIELD_FONT = new Font("Inter", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Inter", Font.BOLD, 16);
    private static final Font RETURN_BUTTON_FONT = new Font("Inter", Font.BOLD, 14); // Smaller font for return button

    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color PANEL_BACKGROUND_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(50, 50, 50);
    private static final Color TEXT_FIELD_BACKGROUND = new Color(240, 240, 240);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color PRIMARY_BUTTON_COLOR = new Color(24, 144, 255);
    private static final Color HOVER_BUTTON_COLOR = new Color(50, 168, 255);
    private static final Color RETURN_BUTTON_COLOR = new Color(230, 230, 230); // Light gray for return button
    private static final Color RETURN_BUTTON_HOVER_COLOR = new Color(200, 200, 200); // Darker gray on hover

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
    JLabel nameValueField = new JLabel();
    JLabel usernameValueField = new JLabel();
    JPasswordField passwordField = new JPasswordField(20);
    JTextField icNumberField = new JTextField(20);
    JLabel emailValueField = new JLabel();
    JTextField contactNumberField = new JTextField(20);
    JTextField addressField = new JTextField(20);

    // --- JLabels for display-only fields (static data) ---
    JLabel levelValueLabel = new JLabel();
    JLabel subjectValueLabel = new JLabel();

    roundImageLabel profilePictureLabel; // Assuming roundImageLabel class is available

    // NEW: No-argument constructor to allow ProfilePageGUI to create an instance
    public Setting() {
        // Call the more complete constructor with null values, or initialize components directly
        this(null, null); // Calls the other constructor with nulls
    }

    // ORIGINAL CONSTRUCTOR: Now accepts a ProfilePageGUI instance
    public Setting(Student student, ProfilePageGUI profilePageGUI) {
        this.currentStudent = student;
        this.profilePageGUI = profilePageGUI; // Store the reference

        setTitle("User Profile Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // setLocationRelativeTo(null); // <-- REMOVED from here
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Ensure roundImageLabel is defined or replace with a standard JLabel if not available
        // For now, assuming it's available or will be provided.
        // If 'fei.png' is not found, this will also cause an error.
        profilePictureLabel = new roundImageLabel("fei.png", 160);

        // --- Top Panel for Return Button and Profile Picture ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(PANEL_BACKGROUND_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding for the top panel

        JButton returnButton = new JButton("Return");
        returnButton.setFont(RETURN_BUTTON_FONT);
        returnButton.setBackground(RETURN_BUTTON_COLOR);
        returnButton.setForeground(TEXT_COLOR);
        returnButton.setFocusPainted(false);
        returnButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        returnButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        returnButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                returnButton.setBackground(RETURN_BUTTON_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                returnButton.setBackground(RETURN_BUTTON_COLOR);
            }
        });
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current Setting frame
                if (profilePageGUI != null) {
                    profilePageGUI.setVisible(true); // Make the ProfilePageGUI visible again
                }
            }
        });
        JPanel returnButtonWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        returnButtonWrapper.setBackground(PANEL_BACKGROUND_COLOR);
        returnButtonWrapper.add(returnButton);
        topPanel.add(returnButtonWrapper, BorderLayout.WEST);

        // Header panel for the profile picture (now placed in the center of topPanel)
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(PANEL_BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // No extra border needed here
        headerPanel.add(profilePictureLabel);
        topPanel.add(headerPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH); // Add the combined topPanel to the JFrame's NORTH

        JPanel innerFormPanel = new JPanel(new GridLayout(0, 2, 15, 12));
        innerFormPanel.setBackground(PANEL_BACKGROUND_COLOR);
        innerFormPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Apply styles to labels and fields using helper methods
        setupLabel(nameLabel);
        setupPlainLabel(nameValueField);
        setupLabel(usernameLabel);
        setupPlainLabel(usernameValueField);
        setupLabel(passwordLabel);
        setupPasswordField(passwordField);

        passwordField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                passwordField.setEchoChar((char) 0);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                passwordField.setEchoChar('*');
            }
        });

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
        innerFormPanel.add(usernameValueField);

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

        populateFields(currentStudent); // Ensure password is initially hidden when the GUI loads
        passwordField.setEchoChar('*');

        JPanel contentPanelWrapper = new JPanel(new BorderLayout());
        contentPanelWrapper.setBackground(PANEL_BACKGROUND_COLOR);
        contentPanelWrapper.add(innerFormPanel, BorderLayout.CENTER);
        add(contentPanelWrapper, BorderLayout.CENTER);

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


        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProfileChanges();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 30));
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack(); // Pack the components first to determine the frame's size
        setLocationRelativeTo(null); // THEN center the frame based on its packed size
        setVisible(true);
    }

    private void setupLabel(JLabel label) {
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR);
    }

    private void setupPlainLabel(JLabel label) {
        label.setFont(VALUE_FONT);
        label.setForeground(TEXT_COLOR);
    }

    private void setupValueLabel(JLabel label) {
        label.setFont(VALUE_FONT);
        label.setForeground(TEXT_COLOR);
    }

    private void setupTextField(JTextField textField) {
        textField.setFont(TEXT_FIELD_FONT);
        textField.setBackground(TEXT_FIELD_BACKGROUND);
        textField.setForeground(TEXT_COLOR);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    private void setupPasswordField(JPasswordField passwordField) {
        passwordField.setFont(TEXT_FIELD_FONT);
        passwordField.setBackground(TEXT_FIELD_BACKGROUND);
        passwordField.setForeground(TEXT_COLOR);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        passwordField.setEchoChar('*');
    }


    private void populateFields(Student student) {
        if (student != null) {
            nameValueField.setText(student.getName());
            usernameValueField.setText(student.getStudentId());
            passwordField.setText(student.getPassword());
            icNumberField.setText(student.getIcPassport());
            emailValueField.setText(student.getEmail());
            contactNumberField.setText(student.getContactNumber());
            addressField.setText(student.getAddress());
            levelValueLabel.setText(student.getFormLevel());
            subjectValueLabel.setText(student.getSubjects());
        } else {
            // Clear fields or set default text if no student data is provided
            nameValueField.setText("");
            usernameValueField.setText("");
            passwordField.setText("");
            icNumberField.setText("");
            emailValueField.setText("");
            contactNumberField.setText("");
            addressField.setText("");
            levelValueLabel.setText("");
            subjectValueLabel.setText("");
        }
    }

    private void saveProfileChanges() {
        if (currentStudent == null) {
            JOptionPane.showMessageDialog(this, "No student data to save. Please load a student first.", "Save Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentStudent.setPassword(new String(passwordField.getPassword()));
        currentStudent.setIcPassport(icNumberField.getText());
        currentStudent.setContactNumber(contactNumberField.getText());
        currentStudent.setAddress(addressField.getText());

        saveStudentLoginData(currentStudent, "students.txt");

        JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private static final String STUDENTS_FILE = "students.txt";
    private static final String STUDENT_ENROLLMENT_FILE = "student_enrollment.txt";


    public static Map<String, Student> readEnrollmentData(String filename) {
        Map<String, Student> students = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                Pattern pattern = Pattern.compile("^(S\\d+),\"([^\"]+)\",\"([^\"]+)\",\"([^\"]+)\",\"([^\"]+)\",\"([^\"]+)\",\"([^\"]+)\",\"([^\"]+)\",\"([^\"]+)\"$");
                Matcher matcher = pattern.matcher(line);

                if (matcher.matches()) {
                    String studentId = matcher.group(1);
                    String name = matcher.group(2);
                    String icPassport = matcher.group(3);
                    String email = matcher.group(4);
                    String contactNumber = matcher.group(5);
                    String address = matcher.group(6);
                    String formLevel = matcher.group(7);
                    String subjects = matcher.group(8);
                    String enrollmentMonth = matcher.group(9);

                    Student student = new Student(studentId, name, icPassport, email, contactNumber, address, formLevel, subjects, enrollmentMonth);
                    students.put(studentId, student);
                } else {
                    System.err.println("Skipping malformed line in " + filename + ": " + line);
                }
            }
            System.out.println("Successfully loaded enrollment data from: " + filename);
        } catch (IOException e) {
            System.err.println("Error reading " + filename + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading enrollment data: " + e.getMessage(), "File Load Error", JOptionPane.ERROR_MESSAGE);
        }
        return students;
    }

    public static void readStudentLoginData(String filename, Map<String, Student> allStudents) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // IMPORTANT: Split based on the 8-part User.toString() format
                // Name,Username,Password,IC/Passport,Email,ContactNumber,Address,Role
                String[] parts = line.split(",");
                if (parts.length >= 8) { // Expecting at least 8 parts for a full User string
                    String name = parts[0].trim();
                    String studentId = parts[1].trim(); // Username is at index 1
                    String password = parts[2].trim();
                    String icPassport = parts[3].trim();
                    String email = parts[4].trim();
                    String contactNumber = parts[5].trim();
                    String address = parts[6].trim();
                    String role = parts[7].trim();

                    Student student = allStudents.get(studentId);
                    if (student != null) {
                        // Update the existing Student object with login data
                        student.setPassword(password);
                        student.setRole(role);
                        student.setName(name); // Update name from students.txt (User object)
                        student.setEmail(email); // Update email from students.txt (User object)
                        student.setIcPassport(icPassport); // Update IC from students.txt
                        student.setContactNumber(contactNumber); // Update contact from students.txt
                        student.setAddress(address); // Update address from students.txt
                    } else {
                        // This case means a student is in students.txt but not in enrollment.txt.
                        // For consistency, it's better to ensure enrollment entries exist first.
                        // However, to prevent a crash, we can create a basic student object here.
                        // This indicates a potential data inconsistency if not handled elsewhere.
                        System.err.println("Student ID " + studentId + " from " + filename + " not found in enrollment data. Creating basic Student object.");
                        Student newStudent = new Student(studentId, name, icPassport, email, contactNumber, address, "", "", ""); // Basic init
                        newStudent.setPassword(password);
                        newStudent.setRole(role);
                        allStudents.put(studentId, newStudent);
                    }
                } else {
                    System.err.println("Skipping malformed login line in " + filename + " (expected 8 parts): " + line);
                }
            }
            System.out.println("Successfully loaded login data from: " + filename);
        } catch (IOException e) {
            System.err.println("Error reading " + filename + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading login data: " + e.getMessage(), "File Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void saveStudentLoginData(Student updatedStudent, String filename) {
        // Read all current student data (including login and enrollment)
        // This ensures you get all 8 fields for each student from students.txt
        // and populate the Student objects fully.
        Map<String, Student> allStudents = readEnrollmentData(STUDENT_ENROLLMENT_FILE);
        readStudentLoginData(filename, allStudents); // This reads the 8-part login data

        // Now, update the specific student's object in the map
        if (allStudents.containsKey(updatedStudent.getStudentId())) {
            // Retrieve the existing student object from the map
            Student existingStudent = allStudents.get(updatedStudent.getStudentId());

            // Manually update the fields that are allowed to be changed through the Setting GUI.
            // DO NOT simply replace `allStudents.put(updatedStudent.getStudentId(), updatedStudent);`
            // because `updatedStudent` from the GUI might not have all the enrollment details.
            // Instead, update the fields of the existing student object in the map.
            existingStudent.setPassword(updatedStudent.getPassword());
            existingStudent.setIcPassport(updatedStudent.getIcPassport());
            existingStudent.setContactNumber(updatedStudent.getContactNumber());
            existingStudent.setAddress(updatedStudent.getAddress());
            // Make sure name, email, role, subjects, level, enrollmentMonth are retained
            // from either enrollment file or the existing student object.
            // If the name/email/IC/contact/address can be changed via Setting,
            // ensure the `User` object (which Student inherits/represents)
            // reflects these changes for authentication and profile display.
            // If the ProfilePageGUI displays these from `loggedInStudent` which comes
            // from the merged data (enrollment + login), then updating `existingStudent` is correct.

            // The `User` class (which is the source for `students.txt` lines) needs to reflect these changes too.
            // If `User` is separate, you'll need a mechanism to update the corresponding `User` object and save `users.txt`.
            // For now, let's assume `Student` acts as the `User` for `students.txt` purpose.
            // The `toString()` of `Student` should match the expected 8-part `User` format for `students.txt`.
            // If Student does not have a `toString()` method matching `User`'s, you'll need to define it or adjust.
            // Assuming `Student` now represents the full user data for `students.txt` context.
            // Let's add a `toUserString()` to Student class to ensure correct format for students.txt.
            // And use it here.
        } else {
            System.err.println("Attempted to save data for non-existent student: " + updatedStudent.getStudentId());
            JOptionPane.showMessageDialog(null, "Error: Student not found for saving.", "File Save Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            // Write ALL student entries back, ensuring the updated one is included.
            for (Student student : allStudents.values()) {
                // Ensure the line written matches the 8-part User.toString() format
                // Name,Username,Password,IC/Passport,Email,ContactNumber,Address,Role
                // You might need a `toUserString()` method in Student.java
                // or ensure Student's toString() provides this format.
                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                        student.getName(),         // Part 0
                        student.getStudentId(),    // Part 1 (Username)
                        student.getPassword(),     // Part 2
                        student.getIcPassport(),   // Part 3
                        student.getEmail(),        // Part 4
                        student.getContactNumber(),// Part 5
                        student.getAddress(),      // Part 6
                        student.getRole());        // Part 7

                bw.write(line);
                bw.newLine();
            }
            System.out.println("Successfully wrote updated login data to: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to " + filename + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving login data: " + e.getMessage(), "File Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}