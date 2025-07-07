//package StudentApp;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import StudentApp.model.SStudent;
//import StudentApp.SProfilePageGUI;
//
//public class SettingForStudent extends JFrame {
//
//    private SStudent currentSStudent;
//    private SProfilePageGUI parentGUI; // Add reference to parent GUI
//
//    // --- Fonts and Colors for a ClassIn-like aesthetic ---
//    private static final Font LABEL_FONT = new Font("Inter", Font.BOLD, 14);
//    private static final Font VALUE_FONT = new Font("Inter", Font.PLAIN, 14);
//    private static final Font TEXT_FIELD_FONT = new Font("Inter", Font.PLAIN, 14);
//    private static final Font BUTTON_FONT = new Font("Inter", Font.BOLD, 16);
//    private static final Font RETURN_BUTTON_FONT = new Font("Inter", Font.BOLD, 14); // Smaller font for return button
//
//    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
//    private static final Color PANEL_BACKGROUND_COLOR = Color.WHITE;
//    private static final Color TEXT_COLOR = new Color(51, 51, 51);
//    private static final Color ACCENT_COLOR = new Color(59, 130, 246);
//    private static final Color BORDER_COLOR = new Color(220, 220, 220);
//
//    public SettingForStudent(SStudent sStudent, SProfilePageGUI parentGUI) {
//        this.currentSStudent = sStudent;
//        this.parentGUI = parentGUI; // Initialize parentGUI
//
//        setTitle("Student Settings");
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setSize(700, 600);
//        setLocationRelativeTo(parentGUI); // Center relative to parent
//
//        // Main panel setup - Reverted to ClassIn style
//        JPanel mainPanel = new JPanel();
//        mainPanel.setLayout(new BorderLayout(20, 20)); // Original larger gap
//        mainPanel.setBackground(BACKGROUND_COLOR);
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Original padding
//        add(mainPanel);
//
//        // Header panel - Reverted to ClassIn style
//        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
//        headerPanel.setBackground(ACCENT_COLOR);
//        JLabel headerLabel = new JLabel("Student Profile Settings");
//        headerLabel.setFont(new Font("Inter", Font.BOLD, 28)); // Original font size
//        headerLabel.setForeground(Color.WHITE);
//        headerPanel.add(headerLabel);
//        mainPanel.add(headerPanel, BorderLayout.NORTH);
//
//        JPanel contentPanel = new JPanel();
//        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
//        contentPanel.setBackground(PANEL_BACKGROUND_COLOR);
//        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Original padding
//        mainPanel.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
//
//        contentPanel.add(Box.createVerticalStrut(20)); // Original strut size
//
//        // Use null check for currentSStudent BEFORE accessing its methods
//        if (currentSStudent == null) {
//            JOptionPane.showMessageDialog(this, "Error: Student data is missing.", "Initialization Error", JOptionPane.ERROR_MESSAGE);
//            dispose();
//            return;
//        }
//
//        // Form fields using ClassIn style
//        JTextField nameField = addFormField(contentPanel, "Name:", currentSStudent.getName(), false);
//        JTextField icPassportField = addFormField(contentPanel, "IC/Passport:", currentSStudent.getIcPassport(), true);
//        JTextField emailField = addFormField(contentPanel, "Email:", currentSStudent.getEmail(), true);
//        JTextField contactField = addFormField(contentPanel, "Contact Number:", currentSStudent.getContactNumber(), true);
//        JTextArea addressArea = addTextAreaField(contentPanel, "Address:", currentSStudent.getAddress(), true);
//        JTextField formLevelField = addFormField(contentPanel, "Form Level:", currentSStudent.getFormLevel(), false);
//        JTextField subjectsField = addFormField(contentPanel, "Subjects:", currentSStudent.getSubjects(), false);
//        JTextField enrollmentMonthField = addFormField(contentPanel, "Enrollment Month:", currentSStudent.getEnrollmentMonth(), false);
//
//        // Password Field - Reverted to ClassIn style
//        JPasswordField passwordField = new JPasswordField(currentSStudent.getPassword());
//        passwordField.setFont(TEXT_FIELD_FONT); // Custom font
//        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, passwordField.getPreferredSize().height));
//        JPanel passwordPanel = new JPanel();
//        passwordPanel.setLayout(new BorderLayout(10, 0)); // Original gap
//        passwordPanel.setBackground(PANEL_BACKGROUND_COLOR);
//        passwordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, passwordField.getPreferredSize().height + 10)); // Original size
//        JLabel passwordLabel = new JLabel("Password:");
//        passwordLabel.setFont(LABEL_FONT); // Custom font
//        passwordLabel.setForeground(TEXT_COLOR); // Custom color
//        passwordPanel.add(passwordLabel, BorderLayout.WEST);
//        passwordPanel.add(passwordField, BorderLayout.CENTER);
//        contentPanel.add(passwordPanel);
//        contentPanel.add(Box.createVerticalStrut(10)); // Original strut size
//
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10)); // Original gap
//        buttonPanel.setBackground(BACKGROUND_COLOR);
//        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
//
//        JButton saveButton = new JButton("Save Changes");
//        saveButton.setFont(BUTTON_FONT); // Custom font
//        saveButton.setBackground(ACCENT_COLOR); // Custom background
//        saveButton.setForeground(Color.WHITE); // Custom foreground
//        saveButton.setFocusPainted(false); // Custom focus paint
//        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Custom padding
//
//        JButton returnButton = new JButton("Return");
//        returnButton.setFont(RETURN_BUTTON_FONT); // Custom font
//        returnButton.setBackground(new Color(200, 200, 200)); // Custom background
//        returnButton.setForeground(TEXT_COLOR); // Custom foreground
//        returnButton.setFocusPainted(false); // Custom focus paint
//        returnButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); // Custom padding
//
//        saveButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                SStudent updatedSStudent = new SStudent(
//                        currentSStudent.getStudentId(),
//                        nameField.getText(),
//                        icPassportField.getText(),
//                        emailField.getText(),
//                        contactField.getText(),
//                        addressArea.getText(),
//                        formLevelField.getText(),
//                        subjectsField.getText(),
//                        enrollmentMonthField.getText()
//                );
//                updatedSStudent.setPassword(new String(passwordField.getPassword()));
//                updatedSStudent.setRole(currentSStudent.getRole());
//                updatedSStudent.setLoginUsername(currentSStudent.getLoginUsername());
//
//                // Save both login data (students.txt) and enrollment data (student_enrollment.txt)
//                SettingForStudent.saveLoginData(STUDENTS_FILE, updatedSStudent);
//                SettingForStudent.saveEnrollmentData(STUDENT_ENROLLMENT_FILE, updatedSStudent);
//
//
//                // Update the currentSStudent object in this class
//                // This is important so that if the user clicks settings again,
//                // the fields show the newly saved data.
//                currentSStudent.setName(updatedSStudent.getName());
//                currentSStudent.setIcPassport(updatedSStudent.getIcPassport());
//                currentSStudent.setEmail(updatedSStudent.getEmail());
//                currentSStudent.setContactNumber(updatedSStudent.getContactNumber());
//                currentSStudent.setAddress(updatedSStudent.getAddress());
//                currentSStudent.setPassword(updatedSStudent.getPassword());
//                // No need to set formLevel, subjects, enrollmentMonth if they are not editable in GUI
//                // But good practice to update currentSStudent completely if they were intended to be
//                currentSStudent.setFormLevel(updatedSStudent.getFormLevel());
//                currentSStudent.setSubjects(updatedSStudent.getSubjects());
//                currentSStudent.setEnrollmentMonth(updatedSStudent.getEnrollmentMonth());
//
//
//                // Notify parent GUI to refresh its displayed data
//                if (parentGUI != null) {
//                    parentGUI.loadAndRefreshStudentData(currentSStudent.getLoginUsername());
//                }
//
//                JOptionPane.showMessageDialog(SettingForStudent.this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
//            }
//        });
//
//        returnButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                dispose();
//                // parentGUI.setVisible(true); // Uncomment if you want the parent to become visible again
//            }
//        });
//
//        buttonPanel.add(returnButton);
//        buttonPanel.add(saveButton);
//    }
//
//    // Helper method for form fields - Reverted to ClassIn style
//    private JTextField addFormField(JPanel parent, String labelText, String value, boolean editable) {
//        JPanel panel = new JPanel(new BorderLayout(10, 0)); // Original gap
//        panel.setBackground(PANEL_BACKGROUND_COLOR);
//        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Original size
//
//        JLabel label = new JLabel(labelText);
//        label.setFont(LABEL_FONT); // Custom font
//        label.setForeground(TEXT_COLOR); // Custom color
//        panel.add(label, BorderLayout.WEST);
//
//        JTextField textField = new JTextField(value);
//        textField.setFont(TEXT_FIELD_FONT); // Custom font
//        textField.setEditable(editable);
//        textField.setBorder(BorderFactory.createCompoundBorder( // Custom border
//                BorderFactory.createLineBorder(BORDER_COLOR),
//                BorderFactory.createEmptyBorder(5, 10, 5, 10)
//        ));
//        panel.add(textField, BorderLayout.CENTER);
//
//        parent.add(panel);
//        parent.add(Box.createVerticalStrut(10)); // Original strut size
//        return textField;
//    }
//
//    // Helper method for text area fields - Reverted to ClassIn style
//    private JTextArea addTextAreaField(JPanel parent, String labelText, String value, boolean editable) {
//        JPanel panel = new JPanel(new BorderLayout(10, 0)); // Original gap
//        panel.setBackground(PANEL_BACKGROUND_COLOR);
//        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // Original size
//
//        JLabel label = new JLabel(labelText);
//        label.setFont(LABEL_FONT); // Custom font
//        label.setForeground(TEXT_COLOR); // Custom color
//        panel.add(label, BorderLayout.NORTH);
//
//        JTextArea textArea = new JTextArea(value);
//        textArea.setFont(TEXT_FIELD_FONT); // Custom font
//        textArea.setEditable(editable);
//        textArea.setLineWrap(true);
//        textArea.setWrapStyleWord(true);
//        textArea.setBorder(BorderFactory.createCompoundBorder( // Custom border
//                BorderFactory.createLineBorder(BORDER_COLOR),
//                BorderFactory.createEmptyBorder(5, 10, 5, 10)
//        ));
//        JScrollPane scrollPane = new JScrollPane(textArea);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        panel.add(scrollPane, BorderLayout.CENTER);
//
//        parent.add(panel);
//        parent.add(Box.createVerticalStrut(10)); // Original strut size
//        return textArea;
//    }
//
//    private static final String STUDENTS_FILE = "students.txt";
//    private static final String STUDENT_ENROLLMENT_FILE = "student_enrollment.txt";
//
//    // MODIFIED: Read enrollment data - CORRECTED PARSING LOGIC FOR student_enrollment.txt
//    public static Map<String, SStudent> readEnrollmentData(String filename) {
//        Map<String, SStudent> studentsMap = new HashMap<>();
//        System.out.println("DEBUG: Attempting to read enrollment data from: " + filename);
//        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Split by comma, but not inside quotes
//                // This regex handles fields like "Name" correctly
//                String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
//
//                if (parts.length >= 9) {
//                    try {
//                        // Corrected: studentId is the first part, name is the second
//                        String studentId = parts[0].trim();
//                        String name = parts[1].trim().replace("\"", ""); // Remove quotes from name
//                        String icPassport = parts[2].trim().replace("\"", "");
//                        String email = parts[3].trim().replace("\"", "");
//                        String contactNumber = parts[4].trim().replace("\"", "");
//                        String address = parts[5].trim().replace("\"", "");
//                        String formLevel = parts[6].trim().replace("\"", "");
//                        String subjects = parts[7].trim().replace("\"", "");
//                        String enrollmentMonth = parts[8].trim().replace("\"", "");
//
//                        SStudent student = new SStudent(studentId, name, icPassport, email, contactNumber, address, formLevel, subjects, enrollmentMonth);
//                        studentsMap.put(studentId, student);
//                        System.out.println("DEBUG: Successfully parsed enrollment line. Added student to map - ID: '" + studentId + "', Name: '" + name + "'");
//                    } catch (NumberFormatException e) {
//                        System.err.println("DEBUG: Skipping malformed enrollment line (number format error): '" + line + "' - " + e.getMessage());
//                    }
//                } else {
//                    System.err.println("DEBUG: Skipping malformed enrollment line (incorrect parts count - expected 9, got " + parts.length + "): '" + line + "'");
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Error loading student enrollment file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
//        }
//        System.out.println("DEBUG: Finished reading enrollment data. Map contains " + studentsMap.size() + " entries.");
//        System.out.println("DEBUG: Contents of studentsMap (Enrollment Data):");
//        for (Map.Entry<String, SStudent> entry : studentsMap.entrySet()) {
//            System.out.println("  Key: '" + entry.getKey() + "', Student Name: '" + entry.getValue().getName() + "', LoginUsername: '" + entry.getValue().getLoginUsername() + "'");
//        }
//        return studentsMap;
//    }
//
//    public static void readStudentLoginData(String filename, Map<String, SStudent> allStudents) {
//        System.out.println("DEBUG: Attempting to read login data from: " + filename);
//        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length >= 8) { // Name,Username,Password,IC/Passport,Email,ContactNumber,Address,Role
//                    String nameFromLoginFile = parts[0].trim();
//                    String loginUsername = parts[1].trim();
//                    String password = parts[2].trim();
//                    String role = parts[7].trim(); // Assuming role is the 8th part (index 7)
//
//                    System.out.println("DEBUG: Processing login entry for name: '" + nameFromLoginFile + "', loginUsername: '" + loginUsername + "'");
//
//                    SStudent studentFound = null;
//                    System.out.println("DEBUG: Searching for '" + nameFromLoginFile + "' in enrollment map values:");
//                    for (SStudent student : allStudents.values()) {
//                        System.out.println("  Comparing login file name '" + nameFromLoginFile + "' with enrollment student name: '" + student.getName() + "' (Student ID: '" + student.getStudentId() + "')");
//                        if (student.getName().equals(nameFromLoginFile)) {
//                            studentFound = student;
//                            System.out.println("DEBUG: Match found for student name: '" + nameFromLoginFile + "'");
//                            break;
//                        }
//                    }
//
//                    if (studentFound != null) {
//                        studentFound.setLoginUsername(loginUsername);
//                        studentFound.setPassword(password);
//                        studentFound.setRole(role);
//
//                        // Update other fields from students.txt if they are present and not "N/A"
//                        // These are typically for fields that can be changed by the user login profile,
//                        // even if they originate from enrollment data.
//                        if (parts.length > 3 && !parts[3].trim().isEmpty() && !"N/A".equalsIgnoreCase(parts[3].trim())) studentFound.setIcPassport(parts[3].trim());
//                        if (parts.length > 4 && !parts[4].trim().isEmpty() && !"N/A".equalsIgnoreCase(parts[4].trim())) studentFound.setEmail(parts[4].trim());
//                        if (parts.length > 5 && !parts[5].trim().isEmpty() && !"N/A".equalsIgnoreCase(parts[5].trim())) studentFound.setContactNumber(parts[5].trim());
//                        if (parts.length > 6 && !parts[6].trim().isEmpty() && !"N/A".equalsIgnoreCase(parts[6].trim())) studentFound.setAddress(parts[6].trim());
//
//                        System.out.println("DEBUG: Successfully updated login data for student: '" + nameFromLoginFile + "' with username: '" + loginUsername + "'");
//                    } else {
//                        System.err.println("DEBUG ERROR: Student with name '" + nameFromLoginFile + "' from students.txt not found in enrollment data. Skipping login data for this entry.");
//                    }
//                } else {
//                    System.err.println("DEBUG: Skipping malformed login line (incorrect parts count - expected at least 8, got " + parts.length + "): '" + line + "'");
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Error loading student login data: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
//        }
//        System.out.println("DEBUG: Finished reading login data.");
//        System.out.println("DEBUG: Contents of allStudents map after readStudentLoginData (should have updated loginUsername):");
//        for (Map.Entry<String, SStudent> entry : allStudents.entrySet()) {
//            System.out.println("  Key: '" + entry.getKey() + "', Student Name: '" + entry.getValue().getName() + "', LoginUsername: '" + entry.getValue().getLoginUsername() + "'");
//        }
//    }
//
//
//    // This method saves the data to students.txt (login details)
//    public static void saveLoginData(String filename, SStudent updatedSStudent) {
//        // First, load all existing student data (enrollment and login)
//        Map<String, SStudent> allStudents = readEnrollmentData(STUDENT_ENROLLMENT_FILE);
//        readStudentLoginData(filename, allStudents); // This populates login details for existing students
//
//        // Find the student to be updated in the map
//        SStudent existingSStudent = null;
//        if (updatedSStudent.getLoginUsername() != null && !updatedSStudent.getLoginUsername().isEmpty()) {
//            // Find by loginUsername
//            for (SStudent s : allStudents.values()) {
//                if (updatedSStudent.getLoginUsername().equals(s.getLoginUsername())) {
//                    existingSStudent = s;
//                    break;
//                }
//            }
//        } else if (updatedSStudent.getStudentId() != null && !updatedSStudent.getStudentId().isEmpty()) {
//            // Fallback: if no loginUsername, try finding by studentId (from enrollment)
//            existingSStudent = allStudents.get(updatedSStudent.getStudentId());
//        }
//
//
//        if (existingSStudent != null) {
//            // Update only the fields that can be changed via the login profile/settings
//            existingSStudent.setName(updatedSStudent.getName()); // Name can be updated
//            existingSStudent.setPassword(updatedSStudent.getPassword());
//            existingSStudent.setIcPassport(updatedSStudent.getIcPassport());
//            existingSStudent.setEmail(updatedSStudent.getEmail());
//            existingSStudent.setContactNumber(updatedSStudent.getContactNumber());
//            existingSStudent.setAddress(updatedSStudent.getAddress());
//            // Role and LoginUsername should not be changed via this settings page,
//            // they are used for identification.
//        } else {
//            System.err.println("Attempted to save login data for non-existent student: " + updatedSStudent.getLoginUsername());
//            JOptionPane.showMessageDialog(null, "Error: Student not found for saving login data.", "File Save Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
//            // Iterate through the map and write all students back to the file,
//            // ensuring the updated one is included.
//            for (SStudent SStudent : allStudents.values()) {
//                // Ensure the line written matches the 8-part format for students.txt
//                // Name,Username,Password,IC/Passport,Email,ContactNumber,Address,Role
//                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s",
//                        SStudent.getName(),         // Part 0
//                        SStudent.getLoginUsername(),// Part 1
//                        SStudent.getPassword(),     // Part 2
//                        SStudent.getIcPassport(),   // Part 3
//                        SStudent.getEmail(),        // Part 4
//                        SStudent.getContactNumber(),// Part 5
//                        SStudent.getAddress(),      // Part 6
//                        SStudent.getRole());        // Part 7
//
//                bw.write(line);
//                bw.newLine();
//            }
//            System.out.println("Successfully wrote updated login data to: " + filename);
//        } catch (IOException e) {
//            System.err.println("Error writing to " + filename + ": " + e.getMessage());
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Error saving login data: " + e.getMessage(), "File Save Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//
//    // This method saves the data to student_enrollment.txt (enrollment details)
//    public static void saveEnrollmentData(String filename, SStudent updatedSStudent) {
//        Map<String, SStudent> allStudents = readEnrollmentData(filename); // Read the current enrollment data
//
//        // Find the student to be updated in the enrollment map by studentId
//        SStudent existingEnrollmentStudent = allStudents.get(updatedSStudent.getStudentId());
//
//        if (existingEnrollmentStudent != null) {
//            // Update only the fields that come from student_enrollment.txt and are editable
//            existingEnrollmentStudent.setName(updatedSStudent.getName());
//            existingEnrollmentStudent.setIcPassport(updatedSStudent.getIcPassport());
//            existingEnrollmentStudent.setEmail(updatedSStudent.getEmail());
//            existingEnrollmentStudent.setContactNumber(updatedSStudent.getContactNumber());
//            existingEnrollmentStudent.setAddress(updatedSStudent.getAddress());
//            existingEnrollmentStudent.setFormLevel(updatedSStudent.getFormLevel());
//            existingEnrollmentStudent.setSubjects(updatedSStudent.getSubjects());
//            existingEnrollmentStudent.setEnrollmentMonth(updatedSStudent.getEnrollmentMonth());
//        } else {
//            System.err.println("Attempted to save enrollment data for non-existent student (ID): " + updatedSStudent.getStudentId());
//            JOptionPane.showMessageDialog(null, "Error: Enrollment student not found for saving.", "File Save Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
//            for (SStudent student : allStudents.values()) {
//                // Format the line exactly as expected in student_enrollment.txt
//                // StudentId,"Name",IC/Passport,Email,ContactNumber,Address,Form Level,Subjects,EnrollmentMonth
//                bw.write(String.format("%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
//                        student.getStudentId(),
//                        student.getName(),
//                        student.getIcPassport(),
//                        student.getEmail(),
//                        student.getContactNumber(),
//                        student.getAddress(),
//                        student.getFormLevel(),
//                        student.getSubjects(),
//                        student.getEnrollmentMonth()));
//                bw.newLine();
//            }
//            System.out.println("Successfully wrote updated enrollment data to: " + filename);
//        } catch (IOException e) {
//            System.err.println("Error writing to " + filename + ": " + e.getMessage());
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Error saving enrollment data: " + e.getMessage(), "File Save Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//}


package StudentApp;

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
import StudentApp.model.SStudent;
import StudentApp.SProfilePageGUI;
import common.ui.ButtonUIWithAnimation;
import common.ui.RoundedPanel; // Import RoundedPanel
import common.ui.RoundedBorder; // Import RoundedBorder

public class SettingForStudent extends JFrame {

    private SStudent currentSStudent;
    private SProfilePageGUI parentGUI;

    // --- Fonts and Colors consistent with SProfilePageGUI ---
    private static final Font HEADER_TITLE_FONT = new Font("Inter", Font.BOLD, 28);
    private static final Font LABEL_FONT = new Font("Inter", Font.BOLD, 14);
    private static final Font TEXT_FIELD_FONT = new Font("Inter", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Inter", Font.BOLD, 18);
    private static final Font BACK_BUTTON_FONT = new Font("Inter", Font.PLAIN, 14);

    private static final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    private static final Color PANEL_BACKGROUND_COLOR = Color.WHITE;
    private static final Color TITLE_TEXT_COLOR = new Color(55, 65, 81);
    private static final Color GENERAL_TEXT_COLOR = new Color(75, 85, 99);
    private static final Color ACCENT_BUTTON_COLOR = new Color(24, 144, 255);
    private static final Color RETURN_BUTTON_BG_COLOR = new Color(100, 116, 139);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);

    // Define radius for rounded corners
    private static final int PANEL_RADIUS = 20; // For the main white card panel
    private static final int COMPONENT_RADIUS = 8; // For text fields, password field, text area, and buttons
    private static final int BORDER_WIDTH = 1; // Border width for components

    public SettingForStudent(SStudent sStudent, SProfilePageGUI parentGUI) {
        this.currentSStudent = sStudent;
        this.parentGUI = parentGUI;

        setTitle("Student Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(550, 630)); // This sets a preferred size, pack will use it as a hint

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(BACKGROUND_COLOR);
        rootPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainContentPanel.setBackground(BACKGROUND_COLOR);

        rootPanel.add(mainContentPanel, BorderLayout.CENTER);
        add(rootPanel);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JLabel headerLabel = new JLabel("Student Profile Settings", SwingConstants.CENTER);
        headerLabel.setFont(HEADER_TITLE_FONT);
        headerLabel.setForeground(TITLE_TEXT_COLOR);
        headerPanel.add(headerLabel);
        mainContentPanel.add(headerPanel);

        // Use RoundedPanel for the form card panel
        RoundedPanel formCardPanel = new RoundedPanel(PANEL_RADIUS, false); // No shadow for a cleaner look
        formCardPanel.setLayout(new GridBagLayout());
        formCardPanel.setBackground(PANEL_BACKGROUND_COLOR); // Set background for RoundedPanel
        formCardPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        formCardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCardPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));

        JScrollPane scrollPane = new JScrollPane(formCardPanel);
        // The JScrollPane itself should not have a border here, as the RoundedPanel provides its own visuals.
        // If you want a border *around* the RoundedPanel within the scroll pane, you would apply it to the JScrollPane.
        // For a seamless look with the RoundedPanel, we remove the scroll pane's default border.
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default JScrollPane border
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainContentPanel.add(scrollPane);

        if (currentSStudent == null) {
            JOptionPane.showMessageDialog(this, "Error: Student data is missing.", "Initialization Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // 1. Name Field (NOT editable, no frame)
        JTextField nameField = addFormField(formCardPanel, gbc, "Name:", currentSStudent.getName(), false, row++);

        // 2. Password Field (EDITABLE)
        JPasswordField passwordField = new JPasswordField(currentSStudent.getPassword());
        passwordField.setFont(TEXT_FIELD_FONT);
        // Apply RoundedBorder only if editable
        passwordField.setBorder(new RoundedBorder(COMPONENT_RADIUS, BORDER_COLOR, BORDER_WIDTH));
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
        formCardPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formCardPanel.add(passwordField, gbc);
        row++;

        // 3. Email Field (NOT editable, no frame)
        JTextField emailField = addFormField(formCardPanel, gbc, "Email:", currentSStudent.getEmail(), false, row++);
        // 4. Contact Number Field (EDITABLE)
        JTextField contactField = addFormField(formCardPanel, gbc, "Contact Number:", currentSStudent.getContactNumber(), true, row++);
        // 5. Address Field (EDITABLE)
        JTextArea addressArea = addTextAreaField(formCardPanel, gbc, "Address:", currentSStudent.getAddress(), true, row++);
        // 6. IC/Passport Field (NOT editable, no frame)
        JTextField icPassportField = addFormField(formCardPanel, gbc, "IC/Passport:", currentSStudent.getIcPassport(), false, row++);
        // 7. Form Level Field (NOT editable, no frame)
        JTextField formLevelField = addFormField(formCardPanel, gbc, "Form Level:", currentSStudent.getFormLevel(), false, row++);
        // 8. Subjects Field (NOT editable, no frame)
        JTextField subjectsField = addFormField(formCardPanel, gbc, "Subjects:", currentSStudent.getSubjects(), false, row++);
        // 9. Enrollment Month Field (NOT editable, no frame)
        JTextField enrollmentMonthField = addFormField(formCardPanel, gbc, "Enrollment Month:", currentSStudent.getEnrollmentMonth(), false, row++);

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
        // Apply RoundedBorder to button
        saveButton.setBorder(new RoundedBorder(COMPONENT_RADIUS, ACCENT_BUTTON_COLOR, BORDER_WIDTH));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setUI(new ButtonUIWithAnimation());

        JButton returnButton = new JButton("Return");
        returnButton.setFont(BACK_BUTTON_FONT);
        returnButton.setBackground(RETURN_BUTTON_BG_COLOR);
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusPainted(false);
        // Apply RoundedBorder to button
        returnButton.setBorder(new RoundedBorder(COMPONENT_RADIUS, RETURN_BUTTON_BG_COLOR, BORDER_WIDTH));
        returnButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        returnButton.setUI(new ButtonUIWithAnimation());

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Name, Form Level, Subjects, Enrollment Month, Email, IC/Passport are now not editable by the user
                // so we don't retrieve them from the text fields for saving if they were read-only.
                // However, they are still present in the currentSStudent object, which we'll use for saving.
                // The following lines will only update the values for fields that are actually editable.

                // currentSStudent.setName(nameField.getText()); // No longer editable via UI
                currentSStudent.setPassword(new String(passwordField.getPassword())); // Editable
                currentSStudent.setEmail(emailField.getText()); // Not editable, but we might keep the current logic if the underlying data *can* be changed by other means or if you simply want to reflect the text field's (unchanged) content
                currentSStudent.setContactNumber(contactField.getText()); // Editable
                currentSStudent.setAddress(addressArea.getText()); // Editable
                // currentSStudent.setIcPassport(icPassportField.getText()); // No longer editable via UI
                // currentSStudent.setFormLevel(formLevelField.getText()); // No longer editable via UI
                // currentSStudent.setSubjects(subjectsField.getText()); // No longer editable via UI
                // currentSStudent.setEnrollmentMonth(enrollmentMonthField.getText()); // No longer editable via UI


                SettingForStudent.saveLoginData(STUDENTS_FILE, currentSStudent);
                SettingForStudent.saveEnrollmentData(STUDENT_ENROLLMENT_FILE, currentSStudent);

                if (parentGUI != null) {
                    parentGUI.loadAndRefreshStudentData(currentSStudent.getLoginUsername());
                }

                JOptionPane.showMessageDialog(SettingForStudent.this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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

        // --- IMPORTANT: Call pack() before setLocationRelativeTo() to ensure size is set ---
        pack();
        setLocationRelativeTo(null); // This will center the frame on the screen
        // setVisible(true); // Call setVisible(true) after positioning (if not already done elsewhere)
    }

    /**
     * Helper method to add a form field (JLabel + JTextField) using GridBagLayout.
     */
    private JTextField addFormField(JPanel parent, GridBagConstraints gbc, String labelText, String value, boolean editable, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setForeground(GENERAL_TEXT_COLOR);

        JTextField textField = new JTextField(value);
        textField.setFont(TEXT_FIELD_FONT);
        textField.setEditable(editable);

        if (editable) {
            // Apply RoundedBorder only if the field is editable
            textField.setBorder(new RoundedBorder(COMPONENT_RADIUS, BORDER_COLOR, BORDER_WIDTH));
            textField.setPreferredSize(new Dimension(200, 30));
        } else {
            // Remove border and make it blend in for non-editable fields
            textField.setBorder(null);
            textField.setBackground(parent.getBackground()); // Match the background of the parent panel (formCardPanel)
            textField.setOpaque(true); // Ensure background is painted
            textField.setFocusable(false); // Make sure it can't be focused with tab
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

    /**
     * Helper method to add a text area field (JLabel + JTextArea) using GridBagLayout.
     */
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
            // Apply RoundedBorder only if the field is editable
            textArea.setBorder(new RoundedBorder(COMPONENT_RADIUS, BORDER_COLOR, BORDER_WIDTH));
            // Ensure scroll pane's view does not have an extraneous border if textarea has one
            scrollPane.getViewport().setBorder(null);
        } else {
            // Remove border and make it blend in for non-editable fields
            textArea.setBorder(null);
            textArea.setBackground(parent.getBackground()); // Match the background of the parent panel (formCardPanel)
            textArea.setOpaque(true); // Ensure background is painted
            textArea.setFocusable(false); // Make sure it can't be focused with tab

            // Make the JScrollPane and its viewport transparent and borderless
            scrollPane.setBorder(null);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.getViewport().setBackground(parent.getBackground()); // Match viewport background
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

    private static final String STUDENTS_FILE = "students.txt";
    private static final String STUDENT_ENROLLMENT_FILE = "student_enrollment.txt";

    public static Map<String, SStudent> readEnrollmentData(String filename) {
        Map<String, SStudent> studentsMap = new HashMap<>();
        System.out.println("DEBUG: Attempting to read enrollment data from: " + filename);
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                if (parts.length >= 9) {
                    try {
                        String name = parts[0].trim();
                        String studentId = parts[1].trim().replace("\"", "");
                        String icPassport = parts[2].trim().replace("\"", "");
                        String email = parts[3].trim().replace("\"", "");
                        String contactNumber = parts[4].trim().replace("\"", "");
                        String address = parts[5].trim().replace("\"", "");
                        String formLevel = parts[6].trim().replace("\"", "");
                        String subjects = parts[7].trim().replace("\"", "");
                        String enrollmentMonth = parts[8].trim().replace("\"", "");

                        SStudent student = new SStudent(studentId, name, icPassport, email, contactNumber, address, formLevel, subjects, enrollmentMonth);
                        studentsMap.put(studentId, student);
                        System.out.println("DEBUG: Successfully parsed enrollment line. Added student to map - ID: '" + studentId + "', Name: '" + name + "'");
                    } catch (NumberFormatException e) {
                        System.err.println("DEBUG: Skipping malformed enrollment line (number format error): '" + line + "' - " + e.getMessage());
                    }
                } else {
                    System.err.println("DEBUG: Skipping malformed enrollment line (incorrect parts count - expected 9, got " + parts.length + "): '" + line + "'");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading student enrollment file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("DEBUG: Finished reading enrollment data. Map contains " + studentsMap.size() + " entries.");
        System.out.println("DEBUG: Contents of studentsMap (Enrollment Data):");
        for (Map.Entry<String, SStudent> entry : studentsMap.entrySet()) {
            System.out.println("  Key: '" + entry.getKey() + "', Student Name: '" + entry.getValue().getName() + "', LoginUsername: '" + entry.getValue().getLoginUsername() + "'");
        }
        return studentsMap;
    }

    public static Map<String, SStudent> readStudentLoginData(String filename, Map<String, SStudent> allStudents) {
        System.out.println("DEBUG: Attempting to read login data from: " + filename);
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) { // Name,Username,Password,IC/Passport,Email,ContactNumber,Address,Role
                    String nameFromLoginFile = parts[0].trim();
                    String loginUsername = parts[1].trim();
                    String password = parts[2].trim();
                    String role = parts[7].trim();

                    System.out.println("DEBUG: Processing login entry for name: '" + nameFromLoginFile + "', loginUsername: '" + loginUsername + "'");

                    SStudent studentFound = null;
                    System.out.println("DEBUG: Searching for '" + nameFromLoginFile + "' in enrollment map values:");
                    for (SStudent student : allStudents.values()) {
                        System.out.println("  Comparing login file name '" + nameFromLoginFile + "' with enrollment student name: '" + student.getName() + "' (Student ID: '" + student.getStudentId() + "')");
                        if (student.getName().equals(nameFromLoginFile)) {
                            studentFound = student;
                            System.out.println("DEBUG: Match found for student name: '" + nameFromLoginFile + "'");
                            break;
                        }
                    }

                    if (studentFound != null) {
                        studentFound.setLoginUsername(loginUsername);
                        studentFound.setPassword(password);
                        studentFound.setRole(role);

                        if (parts.length > 3 && !parts[3].trim().isEmpty() && !"N/A".equalsIgnoreCase(parts[3].trim()))
                            studentFound.setIcPassport(parts[3].trim());
                        if (parts.length > 4 && !parts[4].trim().isEmpty() && !"N/A".equalsIgnoreCase(parts[4].trim()))
                            studentFound.setEmail(parts[4].trim());
                        if (parts.length > 5 && !parts[5].trim().isEmpty() && !"N/A".equalsIgnoreCase(parts[5].trim()))
                            studentFound.setContactNumber(parts[5].trim());
                        if (parts.length > 6 && !parts[6].trim().isEmpty() && !"N/A".equalsIgnoreCase(parts[6].trim()))
                            studentFound.setAddress(parts[6].trim());

                        System.out.println("DEBUG: Successfully updated login data for student: '" + nameFromLoginFile + "' with username: '" + loginUsername + "'");
                    } else {
                        System.err.println("DEBUG ERROR: Student with name '" + nameFromLoginFile + "' from students.txt not found in enrollment data. Skipping login data for this entry.");
                    }
                } else {
                    System.err.println("DEBUG: Skipping malformed login line (incorrect parts count - expected at least 8, got " + parts.length + "): '" + line + "'");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading student login data: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("DEBUG: Finished reading login data.");
        System.out.println("DEBUG: Contents of allStudents map after readStudentLoginData (should have updated loginUsername):");
        for (Map.Entry<String, SStudent> entry : allStudents.entrySet()) {
            System.out.println("  Key: '" + entry.getKey() + "', Student Name: '" + entry.getValue().getName() + "', LoginUsername: '" + entry.getValue().getLoginUsername() + "'");
        }
        return allStudents;
    }

    public static void saveLoginData(String filename, SStudent updatedSStudent) {
        Map<String, SStudent> allStudents = readEnrollmentData(STUDENT_ENROLLMENT_FILE);
        readStudentLoginData(filename, allStudents);

        SStudent existingSStudent = null;
        if (updatedSStudent.getLoginUsername() != null && !updatedSStudent.getLoginUsername().isEmpty()) {
            for (SStudent s : allStudents.values()) {
                if (updatedSStudent.getLoginUsername().equals(s.getLoginUsername())) {
                    existingSStudent = s;
                    break;
                }
            }
        } else if (updatedSStudent.getStudentId() != null && !updatedSStudent.getStudentId().isEmpty()) {
            existingSStudent = allStudents.get(updatedSStudent.getStudentId());
        }

        if (existingSStudent != null) {
            existingSStudent.setName(updatedSStudent.getName());
            existingSStudent.setPassword(updatedSStudent.getPassword());
            existingSStudent.setIcPassport(updatedSStudent.getIcPassport());
            existingSStudent.setEmail(updatedSStudent.getEmail());
            existingSStudent.setContactNumber(updatedSStudent.getContactNumber());
            existingSStudent.setAddress(updatedSStudent.getAddress());
            existingSStudent.setRole(updatedSStudent.getRole());
            existingSStudent.setLoginUsername(updatedSStudent.getLoginUsername());
        } else {
            System.err.println("Attempted to save login data for non-existent student: " + updatedSStudent.getLoginUsername());
            JOptionPane.showMessageDialog(null, "Error: Student not found for saving login data.", "File Save Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (SStudent SStudent : allStudents.values()) {
                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                        SStudent.getName(),
                        SStudent.getLoginUsername(),
                        SStudent.getPassword(),
                        SStudent.getIcPassport(),
                        SStudent.getEmail(),
                        SStudent.getContactNumber(),
                        SStudent.getAddress(),
                        SStudent.getRole());

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

    public static void saveEnrollmentData(String filename, SStudent updatedSStudent) {
        Map<String, SStudent> allStudents = readEnrollmentData(filename);

        SStudent existingEnrollmentStudent = allStudents.get(updatedSStudent.getStudentId());

        if (existingEnrollmentStudent != null) {
            existingEnrollmentStudent.setName(updatedSStudent.getName());
            existingEnrollmentStudent.setIcPassport(updatedSStudent.getIcPassport());
            existingEnrollmentStudent.setEmail(updatedSStudent.getEmail());
            existingEnrollmentStudent.setContactNumber(updatedSStudent.getContactNumber());
            existingEnrollmentStudent.setAddress(updatedSStudent.getAddress());
            existingEnrollmentStudent.setFormLevel(updatedSStudent.getFormLevel());
            existingEnrollmentStudent.setSubjects(updatedSStudent.getSubjects());
            existingEnrollmentStudent.setEnrollmentMonth(updatedSStudent.getEnrollmentMonth());
        } else {
            System.err.println("Attempted to save enrollment data for non-existent student (ID): " + updatedSStudent.getStudentId());
            JOptionPane.showMessageDialog(null, "Error: Enrollment student not found for saving.", "File Save Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (SStudent student : allStudents.values()) {
                bw.write(String.format("%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                        student.getName(),
                        student.getStudentId(),
                        student.getIcPassport(),
                        student.getEmail(),
                        student.getContactNumber(),
                        student.getAddress(),
                        student.getFormLevel(),
                        student.getSubjects(),
                        student.getEnrollmentMonth()));
                bw.newLine();
            }
            System.out.println("Successfully wrote updated enrollment data to: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to " + filename + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving enrollment data: " + e.getMessage(), "File Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}