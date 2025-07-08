package ReceptionistApp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent; // Not directly used but often helpful for UI
import java.util.List;
import java.util.ArrayList; // Used for placeholder AdminFunctionality in previous contexts, not strictly needed here if AdminFunctionality is external.
import java.util.stream.Collectors; // Used for placeholder AdminFunctionality in previous contexts, not strictly needed here if AdminFunctionality is external.
import common.model.User; // Import for the User class
import AdminApp.controller.AdminFunctionality;

// Assuming User, AdminFunctionality, and FileHandler classes are defined in their respective .java files
// and are correctly imported into the project build path.
// The placeholders are removed from this file to resolve the "incompatible types" error.

public class ReceptionistManagementGUI {
    /**
     * Displays a dialog for registering a new receptionist with comprehensive details.
     * @param parent The parent JFrame.
     */
    public static void showRegisterReceptionistDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Register New Receptionist", true);
        dialog.setSize(500, 500); // Increased size to accommodate more fields
        dialog.setLayout(new GridLayout(9, 2, 10, 10)); // Adjusted grid layout
        dialog.getContentPane().setBackground(new Color(40, 40, 40));

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
        btnRegister.setBackground(new Color(0, 150, 0));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(200, 50, 50));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);

        // Add components to the dialog
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

            // Correctly create a new User object with all 8 arguments
            User newReceptionist = new User(name, username, password, icPassport, email, contactNumber, address, "receptionist");
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
        // Retrieve the list of receptionists using your AdminFunctionality class.
        List<User> receptionists = AdminFunctionality.getUsersByRole("receptionist");

        // If no receptionists are found, display a message and return.
        if (receptionists.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No receptionists available to display!");
            return;
        }

        // Create the JDialog instance.
        JDialog dialog = new JDialog(parent, "All Receptionist Information", true);
        dialog.setSize(800, 500); // Increased size to accommodate the table better
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(new Color(40, 40, 40)); // Dark background for the content pane

        // Define table column headers.
        String[] columnNames = {"Fullname", "Username", "Password","Email", "Contact Number", "Address", "Role"};

        // Create a DefaultTableModel to hold the table data.
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable for display purposes.
                return false;
            }
        };

        // Populate the table model with data from the receptionists list.
        for (User r : receptionists) {
            // Using getIcPassport() for "ID" column as User class does not have getId()
            // Using getContactNumber() for "Contact Info" column as User class does not have getContactInfo()
            tableModel.addRow(new Object[]{
                    r.getName(),
                    r.getUsername(),
                    r.getPassword(),
                    r.getEmail(),
                    r.getContactNumber(),
                    r.getAddress(),
                    r.getRole()
            });
        }

        // Create the JTable using the populated table model.
        JTable receptionistInfoTable = new JTable(tableModel);
        receptionistInfoTable.setFillsViewportHeight(true); // Make the table fill the height of its scroll pane
        receptionistInfoTable.setRowHeight(25); // Set a comfortable row height
        receptionistInfoTable.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Set font for table content
        receptionistInfoTable.setBackground(new Color(60, 60, 60)); // Dark background for table cells
        receptionistInfoTable.setForeground(Color.WHITE); // White text for table cells
        receptionistInfoTable.getTableHeader().setBackground(new Color(80, 80, 80)); // Darker background for table header
        receptionistInfoTable.getTableHeader().setForeground(Color.WHITE); // White text for table header
        receptionistInfoTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14)); // Bold font for header

        // Add the table to a JScrollPane to enable scrolling if content overflows.
        JScrollPane scrollPane = new JScrollPane(receptionistInfoTable);
        scrollPane.setBackground(new Color(40, 40, 40)); // Match scroll pane background
        scrollPane.getViewport().setBackground(new Color(60, 60, 60)); // Match table background for viewport

        // Create the close button.
        JButton btnClose = new JButton("Close");
        btnClose.setBackground(new Color(100, 100, 100)); // Button background color
        btnClose.setForeground(Color.WHITE); // Button text color
        btnClose.setFocusPainted(false); // Remove focus border

        // Create a panel for the button and add the button to it.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(40, 40, 40)); // Match dialog background
        buttonPanel.add(btnClose);

        // Add the scroll pane (containing the table) to the center of the dialog.
        dialog.add(scrollPane, BorderLayout.CENTER);
        // Add the button panel to the south of the dialog.
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Add an ActionListener to the close button to dispose of the dialog.
        btnClose.addActionListener(e -> dialog.dispose());

        // Set the dialog's location relative to the parent frame and make it visible.
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

}