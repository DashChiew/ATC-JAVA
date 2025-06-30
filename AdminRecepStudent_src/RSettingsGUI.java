import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RSettingsGUI {
    private static final String USERS_FILE = "users.txt";

    public static void showSettingsDialog(JFrame parent, String currentUsername) {
        // Read all the data
        List<String> allUsers = FileHandler.readAllLines(USERS_FILE);
        String currentUserData = null;

        // Find data and check role
        for (String line : allUsers) {
            String[] parts = line.split(",");
            if (parts.length >= 8 && parts[1].equals(currentUsername)) {
                if (!parts[7].equals("receptionist")) {
                    JOptionPane.showMessageDialog(parent,
                            "Only receptionists can update profiles here.",
                            "Access Denied", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                currentUserData = line;
                break;
            }
        }

        if (currentUserData == null) {
            JOptionPane.showMessageDialog(parent,
                    "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] data = currentUserData.split(",");
        String name = data[0];
        String username = data[1];
        String password = data[2];
        String icPassport = data[3];
        String email = data[4];
        String contact = data[5];
        String address = data[6];

        JDialog dialog = new JDialog(parent, "Update My Profile", true);
        dialog.setLayout(new GridLayout(9, 2, 10, 10));
        dialog.setSize(500, 450);

        JTextField txtName = new JTextField(name);
        JLabel lblUsername = new JLabel(username);
        JPasswordField txtPassword = new JPasswordField(password);
        JTextField txtIcPassport = new JTextField(icPassport);
        JTextField txtEmail = new JTextField(email);
        JTextField txtContact = new JTextField(contact);
        JTextField txtAddress = new JTextField(address);
        JLabel lblRole = new JLabel("receptionist");

        dialog.add(new JLabel("Name:"));
        dialog.add(txtName);
        dialog.add(new JLabel("Username:"));
        dialog.add(lblUsername);
        dialog.add(new JLabel("Password:"));
        dialog.add(txtPassword);
        dialog.add(new JLabel("IC/Passport:"));
        dialog.add(txtIcPassport);
        dialog.add(new JLabel("Email:"));
        dialog.add(txtEmail);
        dialog.add(new JLabel("Contact Number:"));
        dialog.add(txtContact);
        dialog.add(new JLabel("Address:"));
        dialog.add(txtAddress);
        dialog.add(new JLabel("Role:"));
        dialog.add(lblRole);

        JButton btnSave = new JButton("Save Changes");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> {
            // Get updated values
            String newName = txtName.getText().trim();
            String newPassword = new String(txtPassword.getPassword()).trim();
            String newIcPassport = txtIcPassport.getText().trim();
            String newEmail = txtEmail.getText().trim();
            String newContact = txtContact.getText().trim();
            String newAddress = txtAddress.getText().trim();

            if (newName.isEmpty() || newPassword.isEmpty() || newIcPassport.isEmpty() ||
                    newEmail.isEmpty() || newContact.isEmpty() || newAddress.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String updatedLine = String.join(",",
                    newName, username, newPassword, newIcPassport, newEmail, newContact, newAddress, "receptionist"
            );

            List<String> updatedLines = new ArrayList<>();
            for (String line : allUsers) {
                updatedLines.add(
                        line.split(",")[1].equals(username) ? updatedLine : line
                );
            }

            try {
                FileHandler.writeAllLines(USERS_FILE, updatedLines);
                JOptionPane.showMessageDialog(dialog,
                        "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Failed to save changes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.add(btnSave);
        dialog.add(btnCancel);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}
