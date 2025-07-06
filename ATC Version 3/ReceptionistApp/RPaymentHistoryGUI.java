package ReceptionistApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;
import ReceptionistApp.controller.ReceptionistFunctionality;
import common.auth.MainLoginPageTest;

public class RPaymentHistoryGUI {
    public static void showPaymentHistory(String username, JFrame parent) {
        JDialog dialog = new JDialog(parent, "Payment History for " + username, true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(new Color(240, 242, 245));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 242, 245));

        String[] columnNames = {"Student", "Amount (RM)", "Status", "Due Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable paymentTable = new JTable(tableModel);
        paymentTable.setFont(new Font("Inter", Font.PLAIN, 14));
        paymentTable.setRowHeight(30);
        paymentTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 14));
        paymentTable.setShowGrid(false);
        paymentTable.setIntercellSpacing(new Dimension(0, 0));

        // Style the scroll pane
        JScrollPane scrollPane = new JScrollPane(paymentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Load payment data
        loadPaymentData(username, tableModel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton btnClose = createStyledButton("Close", new Color(100, 100, 100));
        btnClose.addActionListener(e -> dialog.dispose());
        buttonPanel.add(btnClose);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

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

    private static void loadPaymentData(String targetUsername, DefaultTableModel tableModel) {
        List<String[]> allPaymentRecords = ReceptionistFunctionality.getAllPaymentRecords();
        tableModel.setRowCount(0);

        boolean paymentsFound = false;
        for (String[] record : allPaymentRecords) {
            if (record[0].equals(targetUsername)) {
                Vector<String> row = new Vector<>();
                row.add(record[0]);
                row.add(record[1]);
                row.add(record[2]);
                row.add(record[3]);
                tableModel.addRow(row);
                paymentsFound = true;
            }
        }

        if (!paymentsFound) {
            JOptionPane.showMessageDialog(null, "No payment records found for " + targetUsername + ".");
        }
    }
}