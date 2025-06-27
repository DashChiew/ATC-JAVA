import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ReceptionistManagementGUI {
    /**
     * Displays a dialog for registering a new receptionist.
     * @param parent The parent JFrame.
     */
    public static void showRegisterReceptionistDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Register New Receptionist", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));
        dialog.getContentPane().setBackground(new Color(40, 40, 40));

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
        btnRegister.setBackground(new Color(0, 150, 0));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(200, 50, 50));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);

        dialog.add(lblUsername);
        dialog.add(txtUsername);
        dialog.add(lblPassword);
        dialog.add(txtPassword);
        dialog.add(new JLabel()); // spacer
        dialog.add(new JLabel()); // spacer
        dialog.add(btnRegister);
        dialog.add(btnCancel);

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

            User newReceptionist = new User(username, password, "receptionist");
            AdminFunctionality.saveUser(newReceptionist); // Uses AdminFunctionality
            JOptionPane.showMessageDialog(dialog, "Receptionist registered successfully!");
            dialog.dispose();
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * Displays a dialog for deleting an existing receptionist.
     * @param parent The parent JFrame.
     */
    public static void showDeleteReceptionistDialog(JFrame parent) {
        List<User> receptionists = AdminFunctionality.getUsersByRole("receptionist"); // Uses AdminFunctionality

        if (receptionists.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No receptionists available to delete!");
            return;
        }

        JDialog dialog = new JDialog(parent, "Delete Receptionist", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(new Color(40, 40, 40));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        receptionists.forEach(receptionist -> listModel.addElement(receptionist.getUsername()));

        JList<String> receptionistList = new JList<>(listModel);
        receptionistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        receptionistList.setBackground(new Color(60, 60, 60));
        receptionistList.setForeground(Color.WHITE);

        JButton btnDelete = new JButton("Delete Selected Receptionist");
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

        dialog.add(new JScrollPane(receptionistList), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        btnDelete.addActionListener(e -> {
            int selectedIndex = receptionistList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select a receptionist!");
                return;
            }

            User selectedReceptionist = receptionists.get(selectedIndex);
            if (AdminFunctionality.deleteUser(selectedReceptionist)) { // Uses AdminFunctionality
                JOptionPane.showMessageDialog(dialog, "Receptionist deleted successfully!");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to delete receptionist!");
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * Displays a dialog showing information about all registered receptionists.
     * @param parent The parent JFrame.
     */
    public static void showAllReceptionistInfoDialog(JFrame parent) {
        List<User> receptionists = AdminFunctionality.getUsersByRole("receptionist"); // Uses AdminFunctionality

        if (receptionists.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No receptionists available to display!");
            return;
        }

        JDialog dialog = new JDialog(parent, "All Receptionist Information", true);
        dialog.setSize(450, 350);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(new Color(40, 40, 40));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        receptionists.forEach(r -> listModel.addElement(r.toString())); // Uses User.toString()

        JList<String> receptionistInfoList = new JList<>(listModel);
        receptionistInfoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        receptionistInfoList.setEnabled(false); // Make list unselectable for display purposes
        receptionistInfoList.setBackground(new Color(60, 60, 60));
        receptionistInfoList.setForeground(Color.WHITE);

        JButton btnClose = new JButton("Close");
        btnClose.setBackground(new Color(100, 100, 100));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(40, 40, 40));
        buttonPanel.add(btnClose);

        dialog.add(new JScrollPane(receptionistInfoList), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        btnClose.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}