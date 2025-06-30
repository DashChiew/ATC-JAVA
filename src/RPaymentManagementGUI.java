import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class RPaymentManagementGUI {
    public static void showManagementDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Payment Management", true);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));

        JComboBox<String> studentDropDown = new JComboBox<>(ReceptionistFunctionality.getStudentUsername());
        JTextField txtAmount = new JTextField();
        JComboBox<String> cmbPayStatus = new JComboBox<>(new String[]{"PAID", "UNPAID"});
        JTextField txtDueDate = new JTextField(LocalDate.now().plusMonths(1).toString());

        dialog.add(new JLabel("Student:"));
        dialog.add(studentDropDown);
        dialog.add(new JLabel("Amount (RM):"));
        dialog.add(txtAmount);
        dialog.add(new JLabel("Status:"));
        dialog.add(cmbPayStatus);
        dialog.add(new JLabel("Due Date (YYYY-MM-DD):"));
        dialog.add(txtDueDate);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> savePayment(
                (String) studentDropDown.getSelectedItem(),
                txtAmount.getText(),
                (String) cmbPayStatus.getSelectedItem(),
                txtDueDate.getText(),
                dialog
        ));

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.add(btnCancel);
        dialog.add(btnSave);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private static void savePayment(String username, String amountStr, String status, String dueDate, JDialog dialog) {
        if (username == null || amountStr.isEmpty() || status == null || dueDate.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            LocalDate.parse(dueDate); // Basic validation for date format

            // This is the key change: Call the method in ReceptionistFunctionality
            boolean success = ReceptionistFunctionality.saveNewPayment(username, amount, status, dueDate);

            if (success) {
                JOptionPane.showMessageDialog(dialog, "Payment record saved successfully!");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to save payment record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Please enter a valid amount (e.g., 123.45).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) { // Catch specific date parsing exception
            JOptionPane.showMessageDialog(dialog, "Please enter due date in YYYY-MM-DD format.", "Invalid Date Format", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) { // Catch any other unexpected exceptions
            JOptionPane.showMessageDialog(dialog, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); // Print stack trace for debugging
        }
    }
}