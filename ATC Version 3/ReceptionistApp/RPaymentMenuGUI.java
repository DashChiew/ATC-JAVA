package ReceptionistApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import ReceptionistApp.controller.ReceptionistFunctionality;
import common.auth.MainLoginPageTest;

public class RPaymentMenuGUI {
    public static void showPaymentMenu(JFrame parent) {
        JDialog menuDialog = new JDialog(parent, "Payment Operations", true);
        menuDialog.setSize(350, 300);
        menuDialog.setLocationRelativeTo(parent);
        menuDialog.getContentPane().setBackground(new Color(240, 242, 245));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 242, 245));

        JButton btnManage = createStyledButton("Record New Payment", new Color(70, 130, 180));
        JButton btnAccept = createStyledButton("Accept Payment", new Color(70, 130, 180));
        JButton btnView = createStyledButton("View Payments", new Color(70, 130, 180));
        JButton btnClose = createStyledButton("Close", new Color(100, 100, 100));

        btnView.addActionListener(e -> {
            String[] students = ReceptionistFunctionality.getStudentUsername();
            if (students.length == 0) {
                JOptionPane.showMessageDialog(menuDialog, "No students found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String username = (String) JOptionPane.showInputDialog(parent,
                    "Select Student:", "View Payments",
                    JOptionPane.PLAIN_MESSAGE, null, students, students[0]);

            if (username != null) {
                RPaymentHistoryGUI.showPaymentHistory(username, parent);
            }
        });

        btnManage.addActionListener(e -> RPaymentManagementGUI.showManagementDialog(parent));
        btnAccept.addActionListener(e -> RPaymentAcceptGUI.showAcceptDialog(parent));
        btnClose.addActionListener(e -> menuDialog.dispose());

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnView);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnManage);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnAccept);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnClose);

        menuDialog.add(mainPanel);
        menuDialog.setVisible(true);
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
}