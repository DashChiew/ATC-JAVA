package ReceptionistApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import common.auth.MainLoginPageTest;
import common.util.FileHandler;

public class RStudentRequestForm {
    private static final String REQUEST_FILE = "change_subject.txt";

    public static void showRequestMenu(JFrame parent) {
        JDialog menuDialog = new JDialog(parent, "Student Request Management", true);
        menuDialog.setSize(350, 300);
        menuDialog.setLocationRelativeTo(parent);
        menuDialog.getContentPane().setBackground(new Color(240, 242, 245));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 242, 245));

        JButton btnViewRequests = createStyledButton("View Requests", new Color(70, 130, 180));
        JButton btnUpdateRequests = createStyledButton("Update Requests", new Color(70, 130, 180));
        JButton btnClose = createStyledButton("Close", new Color(100, 100, 100));

        btnViewRequests.addActionListener(e -> displayRequests(parent));
        btnUpdateRequests.addActionListener(e -> updateRequests(parent));
        btnClose.addActionListener(e -> menuDialog.dispose());

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnViewRequests);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnUpdateRequests);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnClose);

        menuDialog.add(mainPanel);
        menuDialog.setVisible(true);
    }

    private static void displayRequests(JFrame parent) {
        List<String> requests = readRequests();
        if (requests.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No subject change requests found.");
            return;
        }

        JDialog displayDialog = new JDialog(parent, "Subject Change Requests", true);
        displayDialog.setSize(600, 400);
        displayDialog.setLocationRelativeTo(parent);
        displayDialog.getContentPane().setBackground(new Color(240, 242, 245));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 242, 245));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Inter", Font.PLAIN, 14));
        textArea.setBackground(Color.WHITE);
        textArea.setText(String.join("\n", requests));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JButton closeButton = createStyledButton("Close", new Color(100, 100, 100));
        closeButton.addActionListener(e -> displayDialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        displayDialog.add(mainPanel);
        displayDialog.setVisible(true);
    }

    private static void updateRequests(JFrame parent) {
        List<String> requests = readRequests();
        if (requests.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No subject change requests found.");
            return;
        }

        JDialog updateDialog = new JDialog(parent, "Update Requests", true);
        updateDialog.setSize(800, 600);
        updateDialog.setLocationRelativeTo(parent);
        updateDialog.getContentPane().setBackground(new Color(240, 242, 245));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 242, 245));

        String[] columnNames = {"Username", "Date", "Current Subject", "Requested Subject", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        for (String request : requests) {
            String[] parts = request.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            if (parts.length >= 5) {
                tableModel.addRow(new Object[]{
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim().replaceAll("^\"|\"$", ""),
                        parts[3].trim().replaceAll("^\"|\"$", ""),
                        parts[4].trim().replace("Status: ", "")
                });
            }
        }

        JTable requestTable = new JTable(tableModel);
        requestTable.setFont(new Font("Inter", Font.PLAIN, 14));
        requestTable.setRowHeight(30);
        requestTable.getTableHeader().setFont(new Font("Inter", Font.BOLD, 14));
        requestTable.setShowGrid(false);
        requestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(requestTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        controlPanel.setOpaque(false);

        JComboBox<String> statusCombo = createRoundedComboBox();
        statusCombo.addItem("Pending");
        statusCombo.addItem("Completed");

        JButton updateButton = createStyledButton("Update Status", new Color(70, 130, 180));
        JButton closeButton = createStyledButton("Close", new Color(100, 100, 100));

        updateButton.addActionListener(e -> {
            int selectedRow = requestTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(updateDialog, "Please select a request to update.");
                return;
            }
            String newStatus = (String) statusCombo.getSelectedItem();
            String username = (String) tableModel.getValueAt(selectedRow, 0);
            String date = (String) tableModel.getValueAt(selectedRow, 1);

            tableModel.setValueAt(newStatus, selectedRow, 4);
            String updatedRequest = String.format("%s,%s,\"%s\",\"%s\",Status: %s",
                    username, date,
                    tableModel.getValueAt(selectedRow, 2),
                    tableModel.getValueAt(selectedRow, 3),
                    newStatus);

            requests.set(selectedRow, updatedRequest);
            saveRequests(requests);
            JOptionPane.showMessageDialog(updateDialog, "Status updated successfully!");
        });

        closeButton.addActionListener(e -> updateDialog.dispose());

        controlPanel.add(new JLabel("New Status:"));
        controlPanel.add(statusCombo);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(updateButton);
        controlPanel.add(closeButton);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        updateDialog.add(mainPanel);
        updateDialog.setVisible(true);
    }

    private static List<String> readRequests() {
        FileHandler.createFileIfNotExists(REQUEST_FILE);
        List<String> requests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(REQUEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                requests.add(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading request file: " + e.getMessage());
        }
        return requests;
    }

    private static void saveRequests(List<String> requests) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REQUEST_FILE))) {
            for (String request : requests) {
                writer.write(request);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving request file: " + e.getMessage());
        }
    }

    public static void openRequestManagement(JFrame parent) {
        showRequestMenu(parent);
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

    private static JComboBox<String> createRoundedComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(new Font("Inter", Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new MainLoginPageTest.RoundedCornerBorder(10),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        comboBox.setBackground(new Color(248, 249, 250));
        return comboBox;
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