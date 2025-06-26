import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RStudentMenu {

    private static final String STUDENTS_FILE = "students.txt";
    private static final String ENROLLMENT_FILE = "student_enrollment.txt";

    public static void showStudentMenu(JFrame parent) {
        JDialog menuDialog = new JDialog(parent, "Student Operations", true); // Modal dialog
        menuDialog.setSize(350, 350); // Set a fixed size for the menu
        menuDialog.setLayout(new GridLayout(5, 1, 10, 10)); // Grid layout for buttons with gaps
        menuDialog.getContentPane().setBackground(new Color(40, 40, 40)); // Dark background for the dialog

        JButton btnRegister = new JButton("Register New Student");
        styleMenuButton(btnRegister, new Color(0, 150, 0)); // Green color
        btnRegister.addActionListener(e -> {
            // Call the existing method from StudentManagementGUI
            RStudentManagementGUI.showRegisterStudentDialog(parent);
        });

        JButton btnUpdateEnrollment = new JButton("Update Student Enrollment");
        styleMenuButton(btnUpdateEnrollment, new Color(0, 150, 0)); // Green color
        btnUpdateEnrollment.addActionListener(e -> {
            // Call the existing method from StudentManagementGUI
            RStudentManagementGUI.showUpdateEnrollmentDialog(parent);
        });

        JButton btnDeleteStudent = new JButton("Delete Student");
        styleMenuButton(btnDeleteStudent, new Color(200, 50, 50)); // Red color
        btnDeleteStudent.addActionListener(e -> {
            // Call the existing method from StudentManagementGUI
            RStudentManagementGUI.showDeleteStudentDialog(parent);
        });

        JButton btnViewStudents = new JButton("View Student Data");
        styleMenuButton(btnViewStudents, new Color(0, 100, 200)); // Blue color
        btnViewStudents.addActionListener(e -> {
            // Options for the user to choose which file to view
            String[] options = {"View students", "View student enrollments"};
            // Show a dialog to get user's choice
            int choice = JOptionPane.showOptionDialog(menuDialog,
                    "Which student data file would you like to view?",
                    "View Student Data",
                    JOptionPane.YES_NO_OPTION, // This will create buttons for YES (first option) and NO (second option)
                    JOptionPane.QUESTION_MESSAGE, null, // No icon
                    options, options[0]);

            if (choice == JOptionPane.YES_OPTION) { // If user chose "View students.txt"
                displayFileContentInDialog(STUDENTS_FILE, "Student Basic Information", menuDialog);
            } else if (choice == JOptionPane.NO_OPTION) { // If user chose "View student_enrollment.txt"
                displayFileContentInDialog(ENROLLMENT_FILE, "Student Enrollment Details", menuDialog);
            }
            // If user closes the dialog without selecting (choice is JOptionPane.CLOSED_OPTION), do nothing.
        });

        JButton btnClose = new JButton("Close");
        styleMenuButton(btnClose, new Color(100, 100, 100)); // Gray color
        btnClose.addActionListener(e -> menuDialog.dispose()); // Close the current dialog

        menuDialog.add(btnRegister);
        menuDialog.add(btnUpdateEnrollment);
        menuDialog.add(btnDeleteStudent);
        menuDialog.add(btnViewStudents);
        menuDialog.add(btnClose);
        menuDialog.setLocationRelativeTo(parent);
        menuDialog.setVisible(true);
    }

    private static void styleMenuButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40)); // Consistent size with dashboard buttons
    }

    private static void displayFileContentInDialog(String filePath, String dialogTitle, JDialog parent) {
        JDialog displayDialog = new JDialog(parent, dialogTitle, true);
        displayDialog.setSize(600, 400);
        displayDialog.setLayout(new BorderLayout());
        displayDialog.getContentPane().setBackground(new Color(40, 40, 40)); // Dark background

        List<String> lines = FileHandler.readAllLines(filePath);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(new Color(60, 60, 60)); // Darker background
        textArea.setForeground(Color.WHITE); // White text
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        if (lines.isEmpty()) {
            textArea.setText("No data found in " + filePath);
        } else {
            textArea.setText(String.join("\n", lines));
        }

        JScrollPane scrollPane = new JScrollPane(textArea); // Add scroll bars if content exceeds view
        displayDialog.add(scrollPane, BorderLayout.CENTER); // Place the scrollable text area in the center

        JButton closeButton = new JButton("Close");
        closeButton.setBackground(new Color(100, 100, 100)); // Gray color
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> displayDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(40, 40, 40));
        buttonPanel.add(closeButton);
        displayDialog.add(buttonPanel, BorderLayout.SOUTH);

        displayDialog.setLocationRelativeTo(parent);
        displayDialog.setVisible(true);
    }
}