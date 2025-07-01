package AdminApp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;
import AdminApp.controller.AdminPaymentReportFunction;

public class AdminPaymentHistory {
    /**
     * Displays a dialog showing the payment history for a specific student.
     * This includes payment details, form level, and subjects.
     *
     * @param username The username of the student whose payment history is to be displayed.
     * @param parent The parent JFrame for the dialog.
     */
    public static void showPaymentHistory(String username, JFrame parent) {
        JDialog dialog = new JDialog(parent, "Payment History for " + username, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(800, 400); // Increased width to accommodate new columns

        // Define the column names for the payment history table
        String[] columnNames = {"Student Username", "Form Level", "Subjects", "Amount (RM)", "Status", "Due Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable paymentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(paymentTable);

        // Load and display payment data for the selected student
        loadPaymentData(username, tableModel);

        dialog.add(scrollPane, BorderLayout.CENTER);

        // Create and add a close button
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnClose);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Center the dialog relative to its parent and make it visible
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * Loads payment data for a specific student into the provided table model.
     * It retrieves all payment records and filters them by the target username.
     * It also fetches the student's enrollment details (form level and subjects)
     * using AdminPaymentReportFunction.
     *
     * @param targetUsername The username of the student to load data for.
     * @param tableModel The DefaultTableModel to populate with data.
     */
    private static void loadPaymentData(String targetUsername, DefaultTableModel tableModel) {
        // Clear any existing data in the table
        tableModel.setRowCount(0);

        // Retrieve all payment records
        List<String[]> allPaymentRecords = AdminPaymentReportFunction.getAllPaymentRecords();

        boolean paymentsFound = false;
        for (String[] record : allPaymentRecords) {
            String username = record[0]; // Student username from the payment record
            String amount = record[1];
            String status = record[2];
            String dueDate = record[3];

            // Filter records by the target username
            if (username.equals(targetUsername)) {
                // Get student's enrollment details (Form Level and Subjects) using the new function
                String[] enrollmentDetails = AdminPaymentReportFunction.getStudentEnrollmentDetails(username);
                String formLevel = enrollmentDetails[0];
                String subjects = enrollmentDetails[1];

                // Create a new row vector with all the required data
                Vector<String> row = new Vector<>();
                row.add(username);
                row.add(formLevel);
                row.add(subjects);
                row.add(amount);
                row.add(status);
                row.add(dueDate);
                tableModel.addRow(row); // Add the row to the table model
                paymentsFound = true;
            }
        }
        // Display a message if no payment records are found for the student
        if (!paymentsFound) {
            JOptionPane.showMessageDialog(null, "No payment records found for " + targetUsername + ".");
        }
    }
}