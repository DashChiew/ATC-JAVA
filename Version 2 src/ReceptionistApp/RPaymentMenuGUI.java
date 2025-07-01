package ReceptionistApp;

import ReceptionistApp.controller.ReceptionistFunctionality;

import javax.swing.*;
import java.awt.*;

public class RPaymentMenuGUI {
    public static void showPaymentMenu(JFrame parent) {
        JDialog menuDialog = new JDialog(parent, "Payment Operations", true);
        menuDialog.setLayout(new GridLayout(4, 1, 10, 10));
        menuDialog.setSize(350, 300);

        JButton btnManage = createMenuButton("Record New Payment");
        JButton btnAccept = createMenuButton("Accept Payment & Generate Receipt");
        JButton btnView = createMenuButton("View All Payments");

        btnView.addActionListener(e -> {
            String[] students = ReceptionistFunctionality.getStudentUsername();
            String username = (String) JOptionPane.showInputDialog(parent,
                    "Select Student:",
                    "View Payments",
                    JOptionPane.PLAIN_MESSAGE, null,
                    students, students[0]);

            if (username != null) {
                RPaymentHistoryGUI.showPaymentHistory(username, parent);
            }
        });

        btnManage.addActionListener(e -> RPaymentManagementGUI.showManagementDialog(parent));

        btnAccept.addActionListener(e -> RPaymentAcceptGUI.showAcceptDialog(parent));

        JButton btnClose = createMenuButton("Close");
        btnClose.addActionListener(e -> menuDialog.dispose());

        menuDialog.add(btnView);
        menuDialog.add(btnManage);
        menuDialog.add(btnAccept);
        menuDialog.add(btnClose);
        menuDialog.setLocationRelativeTo(parent);
        menuDialog.setVisible(true);
    }

    private static JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(70, 130, 180)); // Steel blue
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        return btn;
    }
}