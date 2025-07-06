package ReceptionistApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import ReceptionistApp.controller.ReceptionistFunctionality;
import common.auth.MainLoginPageTest;
import common.ui.RoundedCornerBorder;

public class RPaymentManagementGUI {
    public static void showManagementDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Payment Management", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(new Color(240, 242, 245));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 242, 245));

        JPanel formPanel = new RoundedPanel(20, false);
        formPanel.setLayout(new GridLayout(4, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> studentDropDown = new JComboBox<>(ReceptionistFunctionality.getStudentUsername());
        styleComboBox(studentDropDown);

        JTextField txtAmount = createRoundedTextField();
        JComboBox<String> cmbPayStatus = new JComboBox<>(new String[]{"PAID", "UNPAID"});
        styleComboBox(cmbPayStatus);

        JTextField txtDueDate = createRoundedTextField();
        txtDueDate.setText(LocalDate.now().plusMonths(1).toString());

        formPanel.add(new JLabel("Student:"));
        formPanel.add(studentDropDown);
        formPanel.add(new JLabel("Amount (RM):"));
        formPanel.add(txtAmount);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(cmbPayStatus);
        formPanel.add(new JLabel("Due Date:"));
        formPanel.add(txtDueDate);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        JButton btnSave = createStyledButton("Save", new Color(70, 130, 180));
        JButton btnCancel = createStyledButton("Cancel", new Color(100, 100, 100));

        btnSave.addActionListener(e -> savePayment(
                (String) studentDropDown.getSelectedItem(),
                txtAmount.getText(),
                (String) cmbPayStatus.getSelectedItem(),
                txtDueDate.getText(),
                dialog
        ));
        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private static JTextField createRoundedTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Inter", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                new MainLoginPageTest.RoundedCornerBorder(10),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setBackground(new Color(248, 249, 250));
        return field;
    }

    private static void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Inter", Font.PLAIN, 16));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new MainLoginPageTest.RoundedCornerBorder(10),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        comboBox.setBackground(new Color(248, 249, 250));
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

    private static void savePayment(String username, String amountStr, String status, String dueDate, JDialog dialog) {
        if (username == null || amountStr.isEmpty() || status == null || dueDate.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            LocalDate.parse(dueDate); // Basic validation for date format

            boolean success = ReceptionistFunctionality.saveNewPayment(username, amount, status, dueDate);

            if (success) {
                JOptionPane.showMessageDialog(dialog, "Payment record saved successfully!");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to save payment record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Please enter a valid amount (e.g., 123.45).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(dialog, "Please enter due date in YYYY-MM-DD format.", "Invalid Date Format", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
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