package ReceptionistApp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import common.util.FileHandler;

public class RStudentRequestForm {
    private static final String REQUEST_FILE = "change_subject.txt";

    public static void showRequestMenu(JFrame parent) {
        JDialog menuDialog = new JDialog(parent, "Student Request Management", true);
        menuDialog.setLayout(new GridLayout(3, 1, 10, 10));
        menuDialog.setSize(300, 200);

        JButton btnViewRequests = new JButton("View Subject Change Requests");
        JButton btnUpdateRequests = new JButton("Update Pending Requests");
        JButton btnClose = new JButton("Close");

        btnViewRequests.addActionListener(e -> displayRequests(parent));
        btnUpdateRequests.addActionListener(e -> updateRequests(parent));
        btnClose.addActionListener(e -> menuDialog.dispose());

        menuDialog.add(btnViewRequests);
        menuDialog.add(btnUpdateRequests);
        menuDialog.add(btnClose);
        menuDialog.setLocationRelativeTo(parent);
        menuDialog.setVisible(true);
    }

    private static void displayRequests(JFrame parent) {
        List<String> requests = readRequests();
        if (requests.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No subject change requests found.");
            return;
        }

        JDialog displayDialog = new JDialog(parent, "Subject Change Requests", true);
        displayDialog.setLayout(new BorderLayout());
        displayDialog.setSize(600, 400);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setText(String.join("\n", requests));

        JScrollPane scrollPane = new JScrollPane(textArea);
        displayDialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> displayDialog.dispose());
        displayDialog.add(closeButton, BorderLayout.SOUTH);
        displayDialog.setLocationRelativeTo(parent);
        displayDialog.setVisible(true);
    }

    private static void updateRequests(JFrame parent) {
        List<String> requests = readRequests();
        if (requests.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No subject change requests found.");
            return;
        }

        JDialog updateDialog = new JDialog(parent, "Update Subject Change Requests", true);
        updateDialog.setLayout(new BorderLayout(10, 10));
        updateDialog.setSize(800, 600);

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
            } else {
                System.err.println("Malformed request line skipped: " + request);
            }
        }

        JTable requestTable = new JTable(tableModel);
        requestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(requestTable);
        scrollPane.setPreferredSize(new Dimension(750, 400)); // Set scroll pane size

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Pending", "Completed"});
        JButton updateButton = new JButton("Update Status");
        JButton closeButton = new JButton("Close");

        updateButton.setBackground(new Color(70, 130, 180));
        updateButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(100, 100, 100));
        closeButton.setForeground(Color.WHITE);

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
                    username,
                    date,
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
        updateDialog.add(scrollPane, BorderLayout.CENTER);
        updateDialog.add(controlPanel, BorderLayout.SOUTH);
        updateDialog.setLocationRelativeTo(parent);
        updateDialog.setVisible(true);
    }

    private static List<String> readRequests() {
        // Ensure file exists before reading
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
}