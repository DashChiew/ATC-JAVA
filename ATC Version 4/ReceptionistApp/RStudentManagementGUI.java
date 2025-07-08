package ReceptionistApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import ReceptionistApp.controller.ReceptionistFunctionality;
import common.auth.MainLoginPageTest;
import common.util.FileHandler;
import common.model.User;

public class RStudentManagementGUI {
    private static void setKeepOldDataTip(JTextField... fields) {
        for (JTextField field : fields) {
            field.setToolTipText("Blank = keep current");
        }
    }

    public static void showRegisterStudentDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Register Student", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(new Color(240, 242, 245));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 242, 245));

        JPanel formPanel = new RoundedPanel(20, false);
        formPanel.setLayout(new GridLayout(3, 2, 10, 15));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblName = new JLabel("Student Name:");
        lblName.setFont(new Font("Inter", Font.PLAIN, 14));
        JTextField txtName = createRoundedTextField();

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Inter", Font.PLAIN, 14));
        JPasswordField txtPassword = createRoundedPasswordField();

        formPanel.add(lblName);
        formPanel.add(txtName);
        formPanel.add(lblPassword);
        formPanel.add(txtPassword);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        JButton btnRegister = createStyledButton("Register", new Color(70, 130, 180));
        JButton btnCancel = createStyledButton("Cancel", new Color(100, 100, 100));

        btnRegister.addActionListener(e -> {
            String name = txtName.getText();
            String password = new String(txtPassword.getPassword());
            if (name.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter both Name and Password!");
                return;
            }
            try {
                ReceptionistFunctionality.registerStudent(name, password);
                String newUsername = String.format("S%03d", ReceptionistFunctionality.getStudentCounter() - 1);
                JOptionPane.showMessageDialog(dialog, "Student " + newUsername + " registered successfully!");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Register student error: " + ex.getMessage());
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        dialog.add(scrollPane);
        dialog.setVisible(true);
    }

    public static void showUpdateEnrollmentDialog(JFrame parent) {
        List<String> students = FileHandler.readAllLines("students.txt");
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No student found.");
            return;
        }

        JDialog dialog = new JDialog(parent, "Update Enrollment", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(new Color(240, 242, 245));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 242, 245));

        JPanel formPanel = new RoundedPanel(20, false);
        formPanel.setLayout(new GridLayout(10, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JComboBox<String> studentDropDown = createRoundedComboBox();
        students.stream().map(s -> s.split(",")[1]).forEach(studentDropDown::addItem);

        JTextField txtName = createRoundedTextField();
        JTextField txtIC = createRoundedTextField();
        JTextField txtEmail = createRoundedTextField();
        JTextField txtContact = createRoundedTextField();
        JTextField txtAddress = createRoundedTextField();
        JComboBox<String> cmbLevel = createRoundedComboBox();
        for (String level : new String[]{"Form 1", "Form 2", "Form 3", "Form 4", "Form 5"}) {
            cmbLevel.addItem(level);
        }
        JTextField txtSubjects = createRoundedTextField();
        JComboBox<String> cmbMonth = createRoundedComboBox();
        for (String month : new String[]{"January", "February", "March", "April", "May",
                "June", "July", "August", "September", "October", "November", "December"}) {
            cmbMonth.addItem(month);
        }

        setKeepOldDataTip(txtName, txtEmail, txtContact, txtAddress, txtSubjects);

        formPanel.add(new JLabel("Select Student:"));
        formPanel.add(studentDropDown);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("IC/Passport:"));
        formPanel.add(txtIC);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Contact:"));
        formPanel.add(txtContact);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(txtAddress);
        formPanel.add(new JLabel("Level:"));
        formPanel.add(cmbLevel);
        formPanel.add(new JLabel("Subjects:"));
        formPanel.add(txtSubjects);
        formPanel.add(new JLabel("Enrollment Month:"));
        formPanel.add(cmbMonth);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        JButton btnUpdate = createStyledButton("Update", new Color(70, 130, 180));
        JButton btnCancel = createStyledButton("Cancel", new Color(100, 100, 100));

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

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnCancel);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        dialog.add(scrollPane);
        dialog.setVisible(true);
    }

    public static void showDeleteStudentDialog(JFrame parent) {
        List<String> students = FileHandler.readAllLines("students.txt");
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No student exist.");
            return;
        }

        JDialog dialog = new JDialog(parent, "Delete Student", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(new Color(240, 242, 245));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 242, 245));

        JPanel formPanel = new RoundedPanel(20, false);
        formPanel.setLayout(new BorderLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JComboBox<String> studentDropDown = createRoundedComboBox();
        students.stream().map(s -> s.split(",")[1]).forEach(studentDropDown::addItem);
        formPanel.add(studentDropDown, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        JButton btnDelete = createStyledButton("Delete", new Color(200, 50, 50));
        JButton btnCancel = createStyledButton("Cancel", new Color(100, 100, 100));

        btnDelete.addActionListener(e -> {
            String username = (String) studentDropDown.getSelectedItem();
            if (ReceptionistFunctionality.deleteStudent(username)) {
                JOptionPane.showMessageDialog(dialog, "Student deleted successfully.");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Student delete failed.");
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnDelete);
        buttonPanel.add(btnCancel);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);

        dialog.add(mainPanel);
        dialog.setVisible(true);
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

    private static JTextField createRoundedTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Inter", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                new MainLoginPageTest.RoundedCornerBorder(10),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setBackground(new Color(248, 249, 250));
        return field;
    }

    private static JPasswordField createRoundedPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Inter", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                new MainLoginPageTest.RoundedCornerBorder(10),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setBackground(new Color(248, 249, 250));
        return field;
    }

    private static JComboBox<String> createRoundedComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(new Font("Inter", Font.PLAIN, 16));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new MainLoginPageTest.RoundedCornerBorder(10),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        comboBox.setBackground(new Color(248, 249, 250));
        return comboBox;
    }

    static class RoundedPanel extends JPanel {
        private int arcRadius;
        private boolean hasShadow;

        public RoundedPanel(int arcRadius, boolean hasShadow) {
            this.arcRadius = arcRadius;
            this.hasShadow = hasShadow;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            if (hasShadow) {
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(5, 5, width - 5, height - 5, arcRadius * 2, arcRadius * 2);
            }

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, width, height, arcRadius * 2, arcRadius * 2);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}


