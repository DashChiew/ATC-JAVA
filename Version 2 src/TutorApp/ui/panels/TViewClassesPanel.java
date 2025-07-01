// File: src/main/java/com/tutorapp/ui/panels/ViewClassesPanel.java
package TutorApp.ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TViewClassesPanel extends JPanel {
    private String currentTutor;
    private JComboBox<String> classFormLevelSelector;
    private JTable classTable;
    private DefaultTableModel classTableModel;

    private static final String SUBJECTS_FILE = "src/subjects.txt"; // Defined here for this class's scope

    public TViewClassesPanel(String tutorUsername) {
        this.currentTutor = tutorUsername;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Classes"));
        String[] formLevels = {"All Forms", "Form 1", "Form 2", "Form 3", "Form 4", "Form 5"};
        classFormLevelSelector = new JComboBox<>(formLevels);
        classFormLevelSelector.setSelectedItem("All Forms");
        JButton applyFilterButton = new JButton("Apply Filter");

        filterPanel.add(new JLabel("Select Form Level:"));
        filterPanel.add(classFormLevelSelector);
        filterPanel.add(applyFilterButton);

        add(filterPanel, BorderLayout.NORTH);

        String[] columnNames = {"Subject", "Form Level", "Charges (RM)", "Schedule"};
        classTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        classTable = new JTable(classTableModel);
        classTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(classTable);

        add(scrollPane, BorderLayout.CENTER);

        applyFilterButton.addActionListener(e -> displayFilteredClasses((String) classFormLevelSelector.getSelectedItem()));

        displayFilteredClasses("All Forms");
    }

    private void displayFilteredClasses(String filterFormLevel) {
        classTableModel.setRowCount(0);

        try (BufferedReader br = new BufferedReader(new FileReader(SUBJECTS_FILE))) {
            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[4].trim().equals(currentTutor)) {
                    if (filterFormLevel.equals("All Forms") || parts[1].trim().equals(filterFormLevel)) {
                        classTableModel.addRow(new Object[]{parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()});
                        found = true;
                    }
                }
            }

            if (!found && classTableModel.getRowCount() == 0) {
                classTableModel.addRow(new Object[]{"No classes found for this tutor.", "", "", ""});
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading class data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            classTableModel.addRow(new Object[]{"Error loading data.", "", "", ""});
            e.printStackTrace();
        }
    }
}