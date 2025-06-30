import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class RPaymentAcceptGUI {
    public static void showAcceptDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Accept Payment", true); // Title adjusted
        dialog.setLayout(new GridLayout(3, 2, 10, 10));
        dialog.setSize(450, 180);

        List<String> unpaidPaymentLines = ReceptionistFunctionality.getUnpaidPaymentLines();
        if (unpaidPaymentLines.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "No unpaid payment records found to accept.");
            dialog.dispose();
            return;
        }

        List<String> paymentDisplayLines = new java.util.ArrayList<>();
        for (int i = 0; i < unpaidPaymentLines.size(); i++) {
            String line = unpaidPaymentLines.get(i);
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                paymentDisplayLines.add(String.format("%s (Amount: %.2f, Due Date: %s)",
                        parts[0].trim(), Double.parseDouble(parts[1].trim()), parts[3].trim()));
            }
        }

        JComboBox<String> unpaidPaymentDropDown = new JComboBox<>(paymentDisplayLines.toArray(new String[0]));

        dialog.add(new JLabel("Select Unpaid Record:"));
        dialog.add(unpaidPaymentDropDown);

        JButton btnAccept = new JButton("Accept Payment & Generate Receipt");
        btnAccept.addActionListener(e -> {
            int selectedIndex = unpaidPaymentDropDown.getSelectedIndex();
            if (selectedIndex != -1) {
                String originalPaymentLine = unpaidPaymentLines.get(selectedIndex);
                try {
                    boolean success = ReceptionistFunctionality.updatePaymentStatus(originalPaymentLine, "PAID");
                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "Payment accepted and status updated to PAID!");

                        // Regenerate the payment details to get the updated status for receipt
                        String[] updatedPaymentDetails = null;
                        for (String[] record : ReceptionistFunctionality.getAllPaymentRecords()) {
                            // Find the updated record based on username, amount, and due date
                            if (record[0].equals(originalPaymentLine.split(",")[0].trim()) &&
                                    record[1].equals(originalPaymentLine.split(",")[1].trim()) &&
                                    record[3].equals(originalPaymentLine.split(",")[3].trim()) &&
                                    record[2].equals("PAID")) { // Make sure we get the now PAID record
                                updatedPaymentDetails = record;
                                break;
                            }
                        }

                        if (updatedPaymentDetails != null) {
                            String receiptContent = ReceptionistFunctionality.generateReceipt(updatedPaymentDetails);
                            JOptionPane.showMessageDialog(dialog, receiptContent, "Payment Receipt", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Could not retrieve updated payment details for receipt.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to accept payment.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error processing payment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select an unpaid payment record.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.add(btnAccept);
        dialog.add(btnCancel);

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}