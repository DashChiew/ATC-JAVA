package ReceptionistApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import ReceptionistApp.controller.ReceptionistFunctionality;
import common.auth.MainLoginPageTest; // Assuming this class exists

public class RPaymentAcceptGUI {
    public static void showAcceptDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Accept Payment", true);
        dialog.setSize(500, 260);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(new Color(240, 242, 245));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 242, 245));

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
            // Detailed payment.txt format: username,paymentMethod,paymentDate,amount,status
            if (parts.length >= 5) {
                String studentUsername = parts[0].trim();
                String paymentMethod = parts[1].trim();
                String paymentDate = parts[2].trim();
                double amount = Double.parseDouble(parts[3].trim());
                String status = parts[4].trim();

                paymentDisplayLines.add(String.format("Student: %s, Method: %s, Date: %s, Amount: %.2f, Status: %s",
                        studentUsername, paymentMethod, paymentDate, amount, status));
            } else {
                System.err.println("Skipping malformed payment line in RPaymentAcceptGUI: " + line);
            }
        }

        if (paymentDisplayLines.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "No valid unpaid payment records found to accept.");
            dialog.dispose();
            return;
        }

        JComboBox<String> paymentDropDown = new JComboBox<>(paymentDisplayLines.toArray(new String[0]));
        styleComboBox(paymentDropDown);

        // --- Start of "ClassIn" styling integration for the content panel ---
        // Create a RoundedPanel to hold the content
        RoundedPanel contentRoundedPanel = new RoundedPanel(20, false); // 20 is a good radius, false for no shadow
        contentRoundedPanel.setLayout(new BorderLayout(10, 10));
        contentRoundedPanel.setBackground(Color.WHITE); // White background for the inner panel
        contentRoundedPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Inner padding

        JLabel selectLabel = new JLabel("Select Payment to Accept:");
        selectLabel.setFont(new Font("Inter", Font.BOLD, 15)); // Using Inter font, bold
        selectLabel.setForeground(new Color(50, 50, 50)); // Dark grey color

        contentRoundedPanel.add(selectLabel, BorderLayout.NORTH);
        contentRoundedPanel.add(paymentDropDown, BorderLayout.CENTER);
        // --- End of "ClassIn" styling integration ---


        JButton acceptButton = createStyledButton("Accept Selected Payment", new Color(46, 204, 113));
        acceptButton.addActionListener(e -> {
            int selectedIndex = paymentDropDown.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedPaymentLineDisplay = paymentDisplayLines.get(selectedIndex);
                String originalFullDetailedLine = unpaidPaymentLines.get(selectedIndex); // Get the original full line

                try {
                    boolean updated = ReceptionistFunctionality.updatePaymentStatus(originalFullDetailedLine, "PAID");
                    if (updated) {
                        JOptionPane.showMessageDialog(dialog, "Payment successfully accepted and status updated to PAID!");
                        String[] parts = originalFullDetailedLine.split(",");
                        if (parts.length >= 5) {
                            String receipt = ReceptionistFunctionality.generateReceipt(parts);
                            System.out.println("Receipt generated:\n" + receipt);
                        } else {
                            System.err.println("Could not generate receipt due to malformed payment line: " + originalFullDetailedLine);
                        }

                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to update payment status.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error accepting payment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a payment to accept.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton cancelButton = createStyledButton("Cancel", new Color(231, 76, 60));
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(acceptButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(contentRoundedPanel); // Add the new rounded panel here
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(buttonPanel);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private static void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Inter", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(50, 50, 50));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        comboBox.setFocusable(false);
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