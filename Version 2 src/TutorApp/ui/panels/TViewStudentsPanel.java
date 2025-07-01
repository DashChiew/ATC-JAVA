// File: src/main/java/com/tutorapp/ui/panels/ViewStudentsPanel.java
package TutorApp.ui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import TutorApp.utils.FileUtil;

public class TViewStudentsPanel extends JPanel {
    private String currentTutor;
    private JTable studentTable;
    private DefaultTableModel studentTableModel;
    private JComboBox<String> formLevelFilterComboBox;

    private static final String ENROLMENT_FILE = "src/enrolment.txt"; // Defined here for this class's scope

    public TViewStudentsPanel(String tutorUsername) {
        this.currentTutor = tutorUsername;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Enrolled Students"));
        String[] formLevels = {"All Forms", "Form 1", "Form 2", "Form 3", "Form 4", "Form 5"};
        formLevelFilterComboBox = new JComboBox<>(formLevels);
        formLevelFilterComboBox.setSelectedItem("All Forms");
        JButton applyFilterButton = new JButton("Apply Filter");

        filterPanel.add(new JLabel("Select Form Level:"));
        filterPanel.add(formLevelFilterComboBox);
        filterPanel.add(applyFilterButton);

        add(filterPanel, BorderLayout.NORTH);

        String[] columnNames = {"Student ID", "Student Name", "Form", "Subjects", "Tutor Name"};
        studentTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(studentTableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (component instanceof JLabel) {
                    ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
                }
                return component;
            }
        };

        // Center align header
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) studentTable.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Set word wrap and row height for multi-line content
        studentTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JTextArea textArea = new JTextArea();
                textArea.setText(value != null ? value.toString() : "");
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                textArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
                textArea.setOpaque(true);

                if (isSelected) {
                    textArea.setBackground(table.getSelectionBackground());
                    textArea.setForeground(table.getSelectionForeground());
                } else {
                    textArea.setBackground(table.getBackground());
                    textArea.setForeground(table.getForeground());
                }

                // Center align text
                textArea.setAlignmentX(Component.CENTER_ALIGNMENT);

                return textArea;
            }
        });

        // Set row height to accommodate multiple lines
        studentTable.setRowHeight(60);

        // Auto-resize columns to fit content
        studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < studentTable.getColumnCount(); i++) {
            TableColumn column = studentTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(150); // Default width
        }
        // Make Subjects column wider
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(300);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

        applyFilterButton.addActionListener(e -> displayStudents((String) formLevelFilterComboBox.getSelectedItem()));

        displayStudents("All Forms");
    }

    private void displayStudents(String filterFormLevel) {
        studentTableModel.setRowCount(0);

        try (BufferedReader br = new BufferedReader(new FileReader(ENROLMENT_FILE))) {
            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {
                String[] parts = FileUtil.parseCsvLine(line); // Using FileUtil for CSV parsing

                if (parts.length >= 9 && parts[8].equals(currentTutor)) {
                    String studentForm = parts[5];

                    if (filterFormLevel.equals("All Forms") || studentForm.equals(filterFormLevel)) {
                        studentTableModel.addRow(new Object[]{
                                parts[0], // Student ID
                                parts[1], // Student Name
                                parts[5], // Form
                                parts[6], // Subjects
                                parts[8]  // Tutor Name
                        });
                        found = true;
                    }
                }
            }

            if (!found && studentTableModel.getRowCount() == 0) {
                studentTableModel.addRow(new Object[]{"No students found for this tutor and form.", "", "", "", ""});
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading student data: " + e.getMessage(),
                    "File Read Error", JOptionPane.ERROR_MESSAGE);
            studentTableModel.addRow(new Object[]{"Error loading data.", "", "", "", ""});
            e.printStackTrace();
        }
    }
}