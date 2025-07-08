package ReceptionistApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;
import ReceptionistApp.controller.ReceptionistFunctionality;
import common.auth.MainLoginPageTest; // Assuming this class exists

public class RPaymentHistoryGUI {
    public static void showPaymentHistory(String username, JFrame parent) {
        JDialog dialog = new JDialog(parent, "Payment History for " + username, true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(new Color(240, 242, 245)); // Consistent background

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 242, 245)); // Consistent background

        // Updated column names (already done in previous step, confirming it's here)
        String[] columnNames = {"Student Username", "Amount (RM)", "Status", "Payment Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row,  int column) {
                return false;
            }
        };

        JTable paymentTable = new JTable(tableModel);
        paymentTable.setFont(new Font("Inter", Font.PLAIN, 14)); // Consistent font
        paymentTable.setRowHeight(25);
        paymentTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 15)); // Consistent font
        paymentTable.getTableHeader().setBackground(new Color(70, 130, 180)); // Consistent header color
        paymentTable.getTableHeader().setForeground(Color.WHITE); // Consistent header font color
        paymentTable.setSelectionBackground(new Color(173, 216, 230)); // Consistent selection color
        paymentTable.setSelectionForeground(Color.BLACK);
        paymentTable.setFillsViewportHeight(true);

        // --- Start of "ClassIn" styling integration for the table area ---
        // Wrap the JScrollPane in a RoundedPanel
        RoundedPanel tableRoundedPanel = new RoundedPanel(20, false); // Arc radius 20, no shadow
        tableRoundedPanel.setLayout(new BorderLayout());
        tableRoundedPanel.setBackground(Color.WHITE); // White background for the table area
        tableRoundedPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Padding inside rounded panel

        JScrollPane scrollPane = new JScrollPane(paymentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default scroll pane border
        scrollPane.setBackground(Color.WHITE);

        tableRoundedPanel.add(scrollPane, BorderLayout.CENTER);
        // --- End of "ClassIn" styling integration ---

        // Load data into the table
        loadPaymentData(username, tableModel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        JButton closeButton = createStyledButton("Close", new Color(100, 100, 100)); // Consistent button style
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);

        JLabel titleLabel = new JLabel("Payment Records for " + username, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 18)); // Consistent font style for titles
        titleLabel.setForeground(new Color(50, 50, 50)); // Consistent text color
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Add some space below title

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(tableRoundedPanel, BorderLayout.CENTER); // Add the new rounded panel here
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
        tableModel.setRowCount(0); // Clear existing rows

        boolean paymentsFound = false;
        for (String[] record : allPaymentRecords) {
            // Ensure the record is for the target username and has enough parts
            // Detailed payment.txt format: username,paymentMethod,paymentDate,amount,status
            if (record.length >= 5 && record[0].equals(targetUsername)) {
                Vector<String> row = new Vector<>();
                row.add(record[0]); // Student Username (username)
                row.add(record[3]); // Amount (RM) (amount)
                row.add(record[4]); // Status (status)
                row.add(record[2]); // Payment Date (paymentDate)
                tableModel.addRow(row);
                paymentsFound = true;
            }
        }

        if (!paymentsFound) {
            System.out.println("No payment records found for " + targetUsername + ".");
        }
    }

    // Include the RoundedPanel class here for self-containment
    static class RoundedPanel extends JPanel {
        private int arcRadius;
        private boolean hasShadow;

        public RoundedPanel(int arcRadius, boolean hasShadow) {
            this.arcRadius = arcRadius;
            this.hasShadow = hasShadow;
            setOpaque(false); // Ensure the panel itself is transparent so paintComponent can draw the rounded rect
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            if (hasShadow) {
                // Draw a slight shadow behind the main shape
                g2.setColor(new Color(0, 0, 0, 20)); // Semi-transparent black
                g2.fillRoundRect(5, 5, width - 5, height - 5, arcRadius * 2, arcRadius * 2);
            }

            g2.setColor(getBackground()); // Use the background color set for this panel
            g2.fillRoundRect(0, 0, width, height, arcRadius, arcRadius);
            g2.dispose();
        }
    }
}