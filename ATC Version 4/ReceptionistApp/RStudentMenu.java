package ReceptionistApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import common.auth.MainLoginPageTest;
import common.util.FileHandler;

public class RStudentMenu {
    private static final String STUDENTS_FILE = "students.txt";
    private static final String ENROLLMENT_FILE = "student_enrollment.txt";

    public static void showStudentMenu(JFrame parent) {
        JDialog menuDialog = new JDialog(parent, "Student Operations", true);
        menuDialog.setSize(350, 450);
        menuDialog.setLocationRelativeTo(parent);
        menuDialog.getContentPane().setBackground(new Color(240, 242, 245));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 242, 245));

        JButton btnRegister = createStyledButton("Register Student", new Color(0, 150, 0));
        JButton btnUpdate = createStyledButton("Update Enrollment", new Color(0, 150, 0));
        JButton btnDelete = createStyledButton("Delete Student", new Color(200, 50, 50));
        JButton btnView = createStyledButton("View Students", new Color(0, 100, 200));
        JButton btnRequests = createStyledButton("Student Requests", new Color(0, 100, 200));
        JButton btnClose = createStyledButton("Close", new Color(100, 100, 100));

        btnRegister.addActionListener(e -> RStudentManagementGUI.showRegisterStudentDialog(parent));
        btnUpdate.addActionListener(e -> RStudentManagementGUI.showUpdateEnrollmentDialog(parent));
        btnDelete.addActionListener(e -> RStudentManagementGUI.showDeleteStudentDialog(parent));
        btnView.addActionListener(e -> showViewOptions(menuDialog));
        btnRequests.addActionListener(e -> RStudentRequestForm.openRequestManagement(parent));
        btnClose.addActionListener(e -> menuDialog.dispose());

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnRegister);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnUpdate);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnDelete);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnView);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnRequests);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnClose);

        menuDialog.add(mainPanel);
        menuDialog.setVisible(true);
    }

    private static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Inter", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setUI(new MainLoginPageTest.ClassInButtonUI(25));
        return button;
    }

    private static void showViewOptions(JDialog parent) {
        String[] options = {"Basic Data", "Enrollment Details"};
        int choice = JOptionPane.showOptionDialog(parent,
                "Which data would you like to view?",
                "View Student Data",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                options, options[0]);

        if (choice == JOptionPane.YES_OPTION) {
            displayFileContent(STUDENTS_FILE, "Student Basic Information", parent);
        } else if (choice == JOptionPane.NO_OPTION) {
            displayFileContent(ENROLLMENT_FILE, "Student Enrollment Details", parent);
        }
    }

    private static void displayFileContent(String filePath, String title, JDialog parent) {
        JDialog displayDialog = new JDialog(parent, title, true);
        displayDialog.setSize(600, 400);
        displayDialog.setLocationRelativeTo(parent);
        displayDialog.getContentPane().setBackground(new Color(240, 242, 245));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 242, 245));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBackground(new Color(248, 249, 250));

        List<String> lines = FileHandler.readAllLines(filePath);
        textArea.setText(((java.util.List<?>) lines).isEmpty() ? "No data found" : String.join("\n", lines));

        JScrollPane scrollPane = new JScrollPane(textArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = createStyledButton("Close", new Color(100, 100, 100));
        closeButton.addActionListener(e -> displayDialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 242, 245));
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        displayDialog.add(mainPanel);
        displayDialog.setVisible(true);
    }
}