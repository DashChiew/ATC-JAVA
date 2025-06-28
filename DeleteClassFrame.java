// File: src/main/java/com/tutorapp/ui/frames/DeleteClassFrame.java
package com.tutorapp.ui.frames;

import com.tutorapp.utils.FileUtil; // Using FileUtil for file operations

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeleteClassFrame extends JFrame {
    private JComboBox<String> classComboBox;
    private String tutorUsername;
    private List<String[]> classes;
    private JPanel panel;

    private static final String SUBJECTS_FILE = "src/subjects.txt"; // Defined here for this class's scope

    public DeleteClassFrame(String tutorUsername) {
        this.tutorUsername = tutorUsername;
        setTitle("Delete Class");
        setSize(400, 200);
        setLocationRelativeTo(null);

        classes = new ArrayList<>();

        panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Delete Class", JLabel.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 20));
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Select Class:"), gbc);

        gbc.gridx = 1;
        classComboBox = new JComboBox<>();
        loadClasses();
        panel.add(classComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton deleteButton = new JButton("Delete Class");
        deleteButton.addActionListener(e -> deleteClass());
        panel.add(deleteButton, gbc);

        add(panel);
    }

    private void loadClasses() {
        classComboBox.removeAllItems();
        classes.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(SUBJECTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[4].trim().equals(tutorUsername)) {
                    classComboBox.addItem(parts[0].trim() + " (" + parts[1].trim() + ")");
                    classes.add(parts);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading class data for deletion: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        JButton deleteButton = null;
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton && ((JButton)comp).getText().equals("Delete Class")) {
                deleteButton = (JButton)comp;
                break;
            }
        }

        if (classComboBox.getItemCount() == 0) {
            classComboBox.addItem("No classes found");
            if (deleteButton != null) {
                deleteButton.setEnabled(false);
            }
        } else {
            if (deleteButton != null) {
                deleteButton.setEnabled(true);
            }
        }
    }

    private void deleteClass() {
        int selectedIndex = classComboBox.getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= classes.size() || (classComboBox.getSelectedItem() != null && classComboBox.getSelectedItem().equals("No classes found"))) {
            JOptionPane.showMessageDialog(this, "Please select a valid class to delete.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this class?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String[] classToDelete = classes.get(selectedIndex);
            String subjectToDelete = classToDelete[0].trim();
            String formToDelete = classToDelete[1].trim();
            String tutorOfClass = classToDelete[4].trim();

            try {
                List<String> remainingLines = new ArrayList<>();
                List<String> allFileLines = FileUtil.readAllLines(SUBJECTS_FILE);

                for (String line : allFileLines) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        if (!(parts[0].trim().equals(subjectToDelete) &&
                                parts[1].trim().equals(formToDelete) &&
                                parts[4].trim().equals(tutorOfClass))) {
                            remainingLines.add(line);
                        }
                    } else {
                        remainingLines.add(line);
                    }
                }

                FileUtil.writeAllLines(SUBJECTS_FILE, remainingLines);

                JOptionPane.showMessageDialog(this, "Class deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error deleting class data: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}