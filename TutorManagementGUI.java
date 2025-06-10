import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Vector; // For JComboBox model

class TutorManagementGUI {
    private static final String[] LEVELS = {"Beginner", "Intermediate", "Advanced"};
    private static final String[] SUBJECTS = {"Math", "Science", "English", "History", "Physics", "Chemistry", "Biology"};


    /**
     * Displays a dialog for registering a new tutor.
     * @param parent The parent JFrame.
     */
    public static void showRegisterTutorDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Register New Tutor", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(5, 2, 10, 10)); // Grid layout with gaps
        dialog.getContentPane().setBackground(new Color(40, 40, 40)); // Dark background for dialog

        // Components
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.WHITE);
        JTextField txtUsername = new JTextField();
        txtUsername.setBackground(new Color(60, 60, 60));
        txtUsername.setForeground(Color.WHITE);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.WHITE);
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBackground(new Color(60, 60, 60));
        txtPassword.setForeground(Color.WHITE);

        JButton btnRegister = new JButton("Register");
        btnRegister.setBackground(new Color(0, 150, 0)); // Green
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(200, 50, 50)); // Red
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);

        // Add components to dialog
        dialog.add(lblUsername);
        dialog.add(txtUsername);
        dialog.add(lblPassword);
        dialog.add(txtPassword);
        dialog.add(new JLabel()); // Spacer
        dialog.add(new JLabel()); // Spacer
        dialog.add(btnRegister);
        dialog.add(btnCancel);

        // Action Listeners
        btnRegister.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields!");
                return;
            }

            // Check if username already exists
            List<User> existingUsers = AdminFunctionality.readAllUsers();
            if (existingUsers.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username))) {
                JOptionPane.showMessageDialog(dialog, "Username already exists. Please choose a different one.");
                return;
            }

            User newTutor = new User(username, password, "tutor");
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
        dialog.setSize(450, 350);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(new Color(40, 40, 40));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        tutors.forEach(tutor -> listModel.addElement(tutor.toString())); // Uses User.toString()

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
        JComboBox<String> cmbTutors = new JComboBox<>(tutors.stream().map(User::getUsername).toArray(String[]::new));
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