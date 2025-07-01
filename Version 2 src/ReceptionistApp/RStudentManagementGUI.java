package ReceptionistApp;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import ReceptionistApp.controller.ReceptionistFunctionality; // To manage student data
import common.model.User;
import common.util.FileHandler;

public class RStudentManagementGUI {
    public static void showRegisterStudentDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Register Student", true);
        dialog.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel lblName = new JLabel("Student Name:");
        JTextField txtName = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField();
        JButton btnRegister = new JButton("Register");
        JButton btnCancel = new JButton("Cancel");

        dialog.add(lblName);
        dialog.add(txtName);
        dialog.add(lblPassword);
        dialog.add(txtPassword);
        dialog.add(btnRegister);
        dialog.add(btnCancel);

        btnRegister.addActionListener(e -> {
                    String name = txtName.getText();
                    String password = new String(txtPassword.getPassword());
                    if (name.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Please enter both Name and Password!");
                        return;
                    }
                    try {
                        ReceptionistFunctionality.registerStudent(name, password);
                        String newUsername = String.format("S%03d", ReceptionistFunctionality.getStudentCounter() -1);
                        JOptionPane.showMessageDialog(dialog, "Student " + newUsername + " registered successfully!");
                        dialog.dispose();
                    }
                    catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, "Register student error: " + ex.getMessage());
                    }
                }
        );

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private static void setKeepOldDataTip(JTextField... fields) {
        for (JTextField field : fields) {
            field.setToolTipText("Blank = keep current");
        }
    }

    public static void showUpdateEnrollmentDialog(JFrame parent) {
        List<String> students = FileHandler.readAllLines("students.txt");
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No student found.");
            return;
        }

        JDialog dialog = new JDialog(parent, "Update Student's Enrollment", true);
        dialog.setLayout(new GridLayout(10, 2, 10, 10));
        dialog.setSize(500, 600);

        // To select student
        JComboBox<String> studentDropDown = new JComboBox<>(
                students.stream().map(s -> s.split(",")[0]).toArray(String[]::new));

        JTextField txtName = new JTextField();
        JTextField txtIC = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtContact = new JTextField();
        JTextField txtAddress = new JTextField();
        JComboBox<String> cmbLevel = new JComboBox<>(new String[]{"Form 1", "Form 2", "Form 3", "Form 4", "Form 5"});
        JTextField txtSubjects = new JTextField(); // Comma-separated (e.g., "Math,Science")
        JComboBox<String> cmbMonth = new JComboBox<>(new String[]{"January", "February", "March", "April", "May",
                "June", "July", "August", "September", "October", "November", "December"});

        setKeepOldDataTip(txtName, txtEmail, txtContact, txtAddress, txtSubjects);

        dialog.add(new JLabel("Enter TP Number:"));
        dialog.add(txtName);
        dialog.add(new JLabel("Select Student:"));
        dialog.add(studentDropDown);
        dialog.add(new JLabel("Enter IC/Passport:"));
        dialog.add(txtIC);
        dialog.add(new JLabel("Enter Email:"));
        dialog.add(txtEmail);
        dialog.add(new JLabel("Enter Contact Number:"));
        dialog.add(txtContact);
        dialog.add(new JLabel("Enter Address:"));
        dialog.add(txtAddress);
        dialog.add(new JLabel("Select Level:"));
        dialog.add(cmbLevel);
        dialog.add(new JLabel("Enter Subjects:"));
        dialog.add(txtSubjects);
        dialog.add(new JLabel("Select Enrollment Month:"));
        dialog.add(cmbMonth);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> {
            String username = (String) studentDropDown.getSelectedItem();
            String name = txtName.getText();
            String ic = txtIC.getText();
            String email = txtEmail.getText();
            String contact = txtContact.getText();
            String address = txtAddress.getText();
            String level = (String) cmbLevel.getSelectedItem();
            String subjects = txtSubjects.getText();
            String month = (String) cmbMonth.getSelectedItem();

            try {
                ReceptionistFunctionality.updateStudentEnrollment(username, name, ic, email, contact, address, level, subjects, month);
                JOptionPane.showMessageDialog(dialog, "Enrollment updated successfully!");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Update error: " + ex.getMessage());
            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.add(btnUpdate);
        dialog.add(btnCancel);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    public static void showDeleteStudentDialog(JFrame parent) {
        List<String> students = FileHandler.readAllLines("students.txt");
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No student exist.");
            return;
        }
        JDialog dialog = new JDialog(parent, "Delete Student", true);
        dialog.setLayout(new BorderLayout());

        JComboBox<String> studentDropDown = new JComboBox<>(
                students.stream().map(s -> s.split(",")[0]).toArray(String[]::new));

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> {
                    String username = (String) studentDropDown.getSelectedItem();
                    if (ReceptionistFunctionality.deleteStudent(username)) {
                        JOptionPane.showMessageDialog(dialog, "Student deleted successfully.");
                        dialog.dispose();
                    }
                    else {
                        JOptionPane.showMessageDialog(dialog, "Student delete failed.");
                    }
                }
        );

        dialog.add(studentDropDown, BorderLayout.CENTER);
        dialog.add(btnDelete, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}