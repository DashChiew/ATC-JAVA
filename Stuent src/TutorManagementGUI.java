import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List; // Correctly imports java.util.List
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Vector; // For JComboBox model

// Assuming User, AdminFunctionality, and AssignmentFunctionality classes are defined in their respective .java files
// and are correctly imported into the project build path.
// Placeholders for these classes are no longer needed here to avoid type conflicts.

public class TutorManagementGUI {
    private static final String[] LEVELS = {"Form 1", "Form 2", "Form 3", "Form 4", "Form 5"};
    private static final String[] SUBJECTS = {"Math", "Science", "English", "History", "Physics", "Chemistry", "Biology"};


    /**
     * Displays a dialog for registering a new tutor with comprehensive details.
     * @param parent The parent JFrame.
     */
    public static void showRegisterTutorDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Register New Tutor", true);
        dialog.setSize(500, 500); // Increased size for more fields
        dialog.setLayout(new GridLayout(9, 2, 10, 10)); // Adjusted grid layout
        dialog.getContentPane().setBackground(new Color(40, 40, 40)); // Dark background for dialog

        // Name
        JLabel lblName = new JLabel("Full Name:");
        lblName.setForeground(Color.WHITE);
        JTextField txtName = new JTextField();
        txtName.setBackground(new Color(60, 60, 60));
        txtName.setForeground(Color.WHITE);

        // Username
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.WHITE);
        JTextField txtUsername = new JTextField();
        txtUsername.setBackground(new Color(60, 60, 60));
        txtUsername.setForeground(Color.WHITE);

        // Password
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.WHITE);
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBackground(new Color(60, 60, 60));
        txtPassword.setForeground(Color.WHITE);

        // IC/Passport
        JLabel lblIcPassport = new JLabel("IC/Passport:");
        lblIcPassport.setForeground(Color.WHITE);
        JTextField txtIcPassport = new JTextField();
        txtIcPassport.setBackground(new Color(60, 60, 60));
        txtIcPassport.setForeground(Color.WHITE);

        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.WHITE);
        JTextField txtEmail = new JTextField();
        txtEmail.setBackground(new Color(60, 60, 60));
        txtEmail.setForeground(Color.WHITE);

        // Contact Number
        JLabel lblContactNumber = new JLabel("Contact Number:");
        lblContactNumber.setForeground(Color.WHITE);
        JTextField txtContactNumber = new JTextField();
        txtContactNumber.setBackground(new Color(60, 60, 60));
        txtContactNumber.setForeground(Color.WHITE);

        // Address
        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setForeground(Color.WHITE);
        JTextField txtAddress = new JTextField();
        txtAddress.setBackground(new Color(60, 60, 60));
        txtAddress.setForeground(Color.WHITE);


        JButton btnRegister = new JButton("Register");
        btnRegister.setBackground(new Color(0, 150, 0)); // Green
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(200, 50, 50)); // Red
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);

        // Add components to dialog
        dialog.add(lblName);
        dialog.add(txtName);
        dialog.add(lblUsername);
        dialog.add(txtUsername);
        dialog.add(lblPassword);
        dialog.add(txtPassword);
        dialog.add(lblIcPassport);
        dialog.add(txtIcPassport);
        dialog.add(lblEmail);
        dialog.add(txtEmail);
        dialog.add(lblContactNumber);
        dialog.add(txtContactNumber);
        dialog.add(lblAddress);
        dialog.add(txtAddress);
        dialog.add(btnRegister);
        dialog.add(btnCancel);

        // Action Listeners
        btnRegister.addActionListener(e -> {
            String name = txtName.getText().trim();
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            String icPassport = txtIcPassport.getText().trim();
            String email = txtEmail.getText().trim();
            String contactNumber = txtContactNumber.getText().trim();
            String address = txtAddress.getText().trim();

            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || icPassport.isEmpty() ||
                    email.isEmpty() || contactNumber.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields!");
                return;
            }

            // Check if username already exists
            List<User> existingUsers = AdminFunctionality.readAllUsers();
            if (existingUsers.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username))) {
                JOptionPane.showMessageDialog(dialog, "Username already exists. Please choose a different one.");
                return;
            }

            // Create new User with all fields, role "tutor"
            User newTutor = new User(name, username, password, icPassport, email, contactNumber, address, "tutor");
            AdminFunctionality.saveUser(newTutor); // Use AdminFunctionality
            JOptionPane.showMessageDialog(dialog, "Tutor registered successfully!");
            dialog.dispose();
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * Displays a dialog for deleting an existing tutor.
     * @param parent The parent JFrame.
     */
    public static void showDeleteTutorDialog(JFrame parent) {
        List<User> tutors = AdminFunctionality.getUsersByRole("tutor"); // Use AdminFunctionality

        if (tutors.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No tutors available to delete!");
            return;
        }

        JDialog dialog = new JDialog(parent, "Delete Tutor", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(new Color(40, 40, 40));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        tutors.forEach(tutor -> listModel.addElement(tutor.getUsername()));

        JList<String> tutorList = new JList<>(listModel);
        tutorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tutorList.setBackground(new Color(60, 60, 60));
        tutorList.setForeground(Color.WHITE);

        JButton btnDelete = new JButton("Delete Selected Tutor");
        btnDelete.setBackground(new Color(200, 50, 50));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(100, 100, 100));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(40, 40, 40));
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnCancel);

        dialog.add(new JScrollPane(tutorList), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        btnDelete.addActionListener(e -> {
            int selectedIndex = tutorList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select a tutor!");
                return;
            }

            User selectedTutor = tutors.get(selectedIndex);
            if (AdminFunctionality.deleteUser(selectedTutor)) { // Use AdminFunctionality
                JOptionPane.showMessageDialog(dialog, "Tutor deleted successfully!");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to delete tutor!");
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * Displays a dialog showing information about all registered tutors.
     * @param parent The parent JFrame.
     */
    public static void showAllTutorInfoDialog(JFrame parent) {
        List<User> tutors = AdminFunctionality.getUsersByRole("tutor"); // Use AdminFunctionality

        if (tutors.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No tutors available to display!");
            return;
        }

        JDialog dialog = new JDialog(parent, "All Tutor Information", true);
        dialog.setSize(600, 400); // Increased size for more info
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(new Color(40, 40, 40));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        // Now using the User's toString() which includes all 8 fields
        tutors.forEach(tutor -> listModel.addElement(tutor.toString()));

        JList<String> tutorInfoList = new JList<>(listModel);
        tutorInfoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tutorInfoList.setEnabled(false); // Make list unselectable for display purposes
        tutorInfoList.setBackground(new Color(60, 60, 60));
        tutorInfoList.setForeground(Color.WHITE);

        JButton btnClose = new JButton("Close");
        btnClose.setBackground(new Color(100, 100, 100));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(40, 40, 40));
        buttonPanel.add(btnClose);

        dialog.add(new JScrollPane(tutorInfoList), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        btnClose.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * Displays a dialog to assign a tutor to a level and subject.
     * @param parent The parent JFrame.
     */
    public static void showAssignTutorDialog(JFrame parent) {
        List<User> tutors = AdminFunctionality.getUsersByRole("tutor");
        if (tutors.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No tutors registered to assign!", "Assignment Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(parent, "Assign Tutor to Level/Subject", true);
        dialog.setSize(450, 350);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));
        dialog.getContentPane().setBackground(new Color(40, 40, 40));

        // Components
        JLabel lblTutor = new JLabel("Select Tutor:");
        lblTutor.setForeground(Color.WHITE);
        // Corrected JComboBox constructor to use a Vector for older Java versions or direct array
        JComboBox<String> cmbTutors = new JComboBox<>(new Vector<>(tutors.stream().map(User::getUsername).collect(Collectors.toList())));
        cmbTutors.setBackground(new Color(60, 60, 60));
        cmbTutors.setForeground(Color.WHITE);

        JLabel lblLevel = new JLabel("Select Level:");
        lblLevel.setForeground(Color.WHITE);
        JComboBox<String> cmbLevels = new JComboBox<>(LEVELS);
        cmbLevels.setBackground(new Color(60, 60, 60));
        cmbLevels.setForeground(Color.WHITE);

        JLabel lblSubject = new JLabel("Select Subject:");
        lblSubject.setForeground(Color.WHITE);
        JComboBox<String> cmbSubjects = new JComboBox<>(SUBJECTS);
        cmbSubjects.setBackground(new Color(60, 60, 60));
        cmbSubjects.setForeground(Color.WHITE);

        JButton btnAssign = new JButton("Assign Tutor");
        btnAssign.setBackground(new Color(0, 150, 0));
        btnAssign.setForeground(Color.WHITE);
        btnAssign.setFocusPainted(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(200, 50, 50));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);

        // Add components
        dialog.add(lblTutor);
        dialog.add(cmbTutors);
        dialog.add(lblLevel);
        dialog.add(cmbLevels);
        dialog.add(lblSubject);
        dialog.add(cmbSubjects);
        dialog.add(new JLabel()); // Spacer
        dialog.add(new JLabel()); // Spacer
        dialog.add(btnAssign);
        dialog.add(btnCancel);

        // Action Listeners
        btnAssign.addActionListener(e -> {
            String selectedTutor = (String) cmbTutors.getSelectedItem();
            String selectedLevel = (String) cmbLevels.getSelectedItem();
            String selectedSubject = (String) cmbSubjects.getSelectedItem();

            if (selectedTutor == null || selectedLevel == null || selectedSubject == null) {
                JOptionPane.showMessageDialog(dialog, "Please select all fields for assignment.", "Missing Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            TutorAssignment newAssignment = new TutorAssignment(selectedTutor, selectedLevel, selectedSubject);
            List<TutorAssignment> existingAssignments = AssignmentFunctionality.readAllAssignments();

            // Check for duplicate to avoid redundant messages
            if (existingAssignments.contains(newAssignment)) {
                JOptionPane.showMessageDialog(dialog, "This assignment already exists for this tutor.", "Duplicate Assignment", JOptionPane.INFORMATION_MESSAGE);
            } else {
                AssignmentFunctionality.saveAssignment(newAssignment);
                JOptionPane.showMessageDialog(dialog, "Tutor assigned successfully!");
            }
            dialog.dispose();
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}