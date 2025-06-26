import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class RPaymentHistoryGUI {
    public static void showPaymentHistory(String username, JFrame parent) {
        JDialog dialog = new JDialog(parent, "Payment History for " + username, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(600, 400);

        String[] columnNames = {"Student Username", "Amount (RM)", "Status", "Due Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable paymentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(paymentTable);

        // Load payment data for the selected student
        loadPaymentData(username, tableModel);

        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnClose);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private static void loadPaymentData(String targetUsername, DefaultTableModel tableModel) {
        List<String[]> allPaymentRecords = ReceptionistFunctionality.getAllPaymentRecords();
        tableModel.setRowCount(0); // Clear existing data

        boolean paymentsFound = false;
        for (String[] record : allPaymentRecords) {
            String username = record[0];
            String amount = record[1];
            String status = record[2];
            String dueDate = record[3];

            if (username.equals(targetUsername)) { // Filter by target username
                Vector<String> row = new Vector<>();
                row.add(username);
                row.add(amount);
                row.add(status);
                row.add(dueDate);
                tableModel.addRow(row);
                paymentsFound = true;
            }
        }
        if (!paymentsFound) {
            JOptionPane.showMessageDialog(null, "No payment records found for " + targetUsername + ".");
        }
    }
}
