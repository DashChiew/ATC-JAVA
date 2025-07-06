package ReceptionistApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import ReceptionistApp.controller.ReceptionistFunctionality;
import common.auth.MainLoginPageTest;

public class RPaymentAcceptGUI {
    public static void showAcceptDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Accept Payment", true);
        dialog.setSize(500, 300);
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
            if (parts.length >= 4) {
                paymentDisplayLines.add(String.format("%s (Amount: %.2f, Due Date: %s)",
                        parts[0].trim(), Double.parseDouble(parts[1].trim()), parts[3].trim()));
            }
        }

        JPanel formPanel = new RoundedPanel(20, false);
        formPanel.setLayout(new GridLayout(2, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> unpaidPaymentDropDown = new JComboBox<>(paymentDisplayLines.toArray(new String[0]));
        unpaidPaymentDropDown.setFont(new Font("Inter", Font.PLAIN, 14));

        formPanel.add(new JLabel("Select Unpaid Record:"));
        formPanel.add(unpaidPaymentDropDown);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        JButton btnAccept = createStyledButton("Accept Payment & Generate Receipt", new Color(70, 130, 180));
        JButton btnCancel = createStyledButton("Cancel", new Color(100, 100, 100));

        btnAccept.addActionListener(e -> {
            int selectedIndex = unpaidPaymentDropDown.getSelectedIndex();
            if (selectedIndex != -1) {
                String originalPaymentLine = unpaidPaymentLines.get(selectedIndex);
                try {
                    boolean success = ReceptionistFunctionality.updatePaymentStatus(originalPaymentLine, "PAID");
                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "Payment accepted and status updated to PAID!");

                        String[] updatedPaymentDetails = null;
                        for (String[] record : ReceptionistFunctionality.getAllPaymentRecords()) {
                            if (record[0].equals(originalPaymentLine.split(",")[0].trim()) &&
                                    record[1].equals(originalPaymentLine.split(",")[1].trim()) &&
                                    record[3].equals(originalPaymentLine.split(",")[3].trim()) &&
                                    record[2].equals("PAID")) {
                                updatedPaymentDetails = record;
                                break;
                            }
                        }

                        if (updatedPaymentDetails != null) {
                            String receiptContent = ReceptionistFunctionality.generateReceipt(updatedPaymentDetails);
                            JOptionPane.showMessageDialog(dialog, receiptContent, "Payment Receipt", JOptionPane.INFORMATION_MESSAGE);
                        }
                        dialog.dispose();
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error processing payment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnAccept);
        buttonPanel.add(btnCancel);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);

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

    static class RoundedPanel extends JPanel {
        private int arcRadius;
        private boolean hasShadow;

        public RoundedPanel(int arcRadius, boolean hasShadow) {
            this.arcRadius = arcRadius;
            this.hasShadow = hasShadow;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            if (hasShadow) {
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(5, 5, width - 5, height - 5, arcRadius * 2, arcRadius * 2);
            }

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, width, height, arcRadius * 2, arcRadius * 2);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}