//// File: src/main/java/com/tutorapp/ui/frames/TUpdateClassFrame.java
//package TutorApp.ui.frames;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import TutorApp.utils.FileUtil; // Using FileUtil for file operations
//
//public class TUpdateClassFrame extends JFrame {
//    private JComboBox<String> classComboBox;
//    private JTextField subjectField, chargesField, scheduleField;
//    private JComboBox<String> formLevelComboBox;
//    private List<String[]> classes;
//    private JPanel panel;
//
//    private String originalSelectedSubject;
//    private String originalSelectedFormLevel;
//
//    private static final String SUBJECTS_FILE = "subjects.txt";
//
//    public TUpdateClassFrame(String tutorUsername) {
//        setTitle("Update Class");
//        setSize(500, 450);
//        setLocationRelativeTo(null);
//
//        classes = new ArrayList<>();
//
//        panel = new JPanel(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 10, 10, 10);
//        gbc.anchor = GridBagConstraints.WEST;
//
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.gridwidth = 2;
//        JLabel titleLabel = new JLabel("Update Class", JLabel.CENTER);
//        titleLabel.setFont(new Font("Inter", Font.BOLD, 20));
//        panel.add(titleLabel, gbc);
//
//        gbc.gridwidth = 1;
//        gbc.gridy = 1;
//        panel.add(new JLabel("Select Class:"), gbc);
//
//        gbc.gridx = 1;
//        classComboBox = new JComboBox<>();
//        panel.add(classComboBox, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        panel.add(new JLabel("Subject Name:"), gbc);
//
//        gbc.gridx = 1;
//        subjectField = new JTextField(15);
//        panel.add(subjectField, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        panel.add(new JLabel("Form Level:"), gbc);
//
//        gbc.gridx = 1;
//        String[] formLevels = {"Form 1", "Form 2", "Form 3", "Form 4", "Form 5"};
//        formLevelComboBox = new JComboBox<>(formLevels);
//        panel.add(formLevelComboBox, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy = 4;
//        panel.add(new JLabel("Charges (RM):"), gbc);
//
//        gbc.gridx = 1;
//        chargesField = new JTextField(15);
//        panel.add(chargesField, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy = 5;
//        panel.add(new JLabel("Schedule:"), gbc);
//
//        gbc.gridx = 1;
//        scheduleField = new JTextField(15);
//        panel.add(scheduleField, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy = 6;
//        gbc.gridwidth = 2;
//        gbc.anchor = GridBagConstraints.CENTER;
//        JButton updateButton = new JButton("Update Class");
//        updateButton.addActionListener(e -> updateClass());
//        panel.add(updateButton, gbc);
//
//        add(panel);
//
//        classComboBox.addActionListener(e -> loadClassDetails());
//        loadClasses();
//
//        if (classComboBox.getItemCount() > 0 && classComboBox.getSelectedItem() != null && !classComboBox.getSelectedItem().equals("No classes found")) {
//            loadClassDetails();
//        }
//    }
//
//    private void loadClasses() {
//        classComboBox.removeAllItems();
//        classes.clear();
//
//        try {
//            // Using FileUtil.readAllLines, which should handle file not found gracefully
//            List<String> lines = FileUtil.readAllLines(SUBJECTS_FILE);
//            for (String line : lines) {
//                String[] parts = line.split(",");
//                if (parts.length >= 4) {
//                    classComboBox.addItem(parts[0].trim() + " (" + parts[1].trim() + ")");
//                    classes.add(parts);
//                }
//            }
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, "Error reading class data for loading: " + e.getMessage(),
//                    "Error", JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
//        }
//
//        JButton updateButton = null;
//        for (Component comp : panel.getComponents()) {
//            if (comp instanceof JButton && ((JButton)comp).getText().equals("Update Class")) {
//                updateButton = (JButton)comp;
//                break;
//            }
//        }
//
//        if (classComboBox.getItemCount() == 0) {
//            classComboBox.addItem("No classes found");
//            if (subjectField != null) subjectField.setText("");
//            if (formLevelComboBox != null) formLevelComboBox.setSelectedIndex(-1);
//            if (chargesField != null) chargesField.setText("");
//            if (scheduleField != null) scheduleField.setText("");
//
//            if (subjectField != null) subjectField.setEnabled(false);
//            if (formLevelComboBox != null) formLevelComboBox.setEnabled(false);
//            if (chargesField != null) chargesField.setEnabled(false);
//            if (scheduleField != null) scheduleField.setEnabled(false);
//
//            if (updateButton != null) {
//                updateButton.setEnabled(false);
//            }
//        } else {
//            if (subjectField != null) subjectField.setEnabled(true);
//            if (formLevelComboBox != null) formLevelComboBox.setEnabled(true);
//            if (chargesField != null) chargesField.setEnabled(true);
//            if (scheduleField != null) scheduleField.setEnabled(true);
//            if (updateButton != null) {
//                updateButton.setEnabled(true);
//            }
//        }
//    }
//
//    private void loadClassDetails() {
//        int selectedIndex = classComboBox.getSelectedIndex();
//        if (selectedIndex < 0 || selectedIndex >= classes.size() || (classComboBox.getSelectedItem() != null && classComboBox.getSelectedItem().equals("No classes found"))) {
//            if (subjectField != null) subjectField.setText("");
//            if (formLevelComboBox != null) formLevelComboBox.setSelectedIndex(-1);
//            if (chargesField != null) chargesField.setText("");
//            if (scheduleField != null) scheduleField.setText("");
//            originalSelectedSubject = null;
//            originalSelectedFormLevel = null;
//            return;
//        }
//
//        String[] classData = classes.get(selectedIndex);
//        if (classData.length >= 4) {
//            if (subjectField != null) subjectField.setText(classData[0].trim());
//            if (formLevelComboBox != null) formLevelComboBox.setSelectedItem(classData[1].trim());
//            if (chargesField != null) chargesField.setText(classData[2].trim());
//            if (scheduleField != null) scheduleField.setText(classData[3].trim());
//
//            originalSelectedSubject = classData[0].trim();
//            originalSelectedFormLevel = classData[1].trim();
//        } else {
//            JOptionPane.showMessageDialog(this, "Selected class data is malformed.",
//                    "Data Error", JOptionPane.WARNING_MESSAGE);
//            if (subjectField != null) subjectField.setText("");
//            if (formLevelComboBox != null) formLevelComboBox.setSelectedIndex(-1);
//            if (chargesField != null) chargesField.setText("");
//            if (scheduleField != null) scheduleField.setText("");
//            originalSelectedSubject = null;
//            originalSelectedFormLevel = null;
//        }
//    }
//
//    private void updateClass() {
//        int selectedIndex = classComboBox.getSelectedIndex();
//        if (selectedIndex < 0 || selectedIndex >= classes.size() || (classComboBox.getSelectedItem() != null && classComboBox.getSelectedItem().equals("No classes found"))) {
//            JOptionPane.showMessageDialog(this, "Please select a valid class to update.",
//                    "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        String newSubject = subjectField.getText().trim();
//        String newFormLevel = (String) formLevelComboBox.getSelectedItem();
//        String newCharges = chargesField.getText().trim();
//        String newSchedule = scheduleField.getText().trim();
//
//        if (newSubject.isEmpty() || newFormLevel == null || newCharges.isEmpty() || newSchedule.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Please fill in all fields.",
//                    "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        try {
//            Double.parseDouble(newCharges);
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "Charges must be a valid number.",
//                    "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        if (originalSelectedSubject == null || originalSelectedFormLevel == null) {
//            JOptionPane.showMessageDialog(this, "Error: Could not determine original class details for update. Please re-select the class.",
//                    "Internal Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        String updatedClassLine = newSubject + "," + newFormLevel + "," + newCharges + "," + newSchedule;
//
//        try {
//            List<String> allFileLines = FileUtil.readAllLines(SUBJECTS_FILE);
//            List<String> finalLinesToWrite = new ArrayList<>();
//            boolean updated = false;
//
//            for (String fileLine : allFileLines) {
//                String[] parts = fileLine.split(",");
//                if (parts.length >= 4) {
//                    if (parts[0].trim().equals(originalSelectedSubject) &&
//                            parts[1].trim().equals(originalSelectedFormLevel) &&
//                            !updated) {
//                        finalLinesToWrite.add(updatedClassLine);
//                        updated = true;
//                    } else {
//                        finalLinesToWrite.add(fileLine);
//                    }
//                } else {
//                    finalLinesToWrite.add(fileLine);
//                }
//            }
//
//            if (!updated) {
//                JOptionPane.showMessageDialog(this, "Original class not found in file or already updated. Update failed.",
//                        "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            FileUtil.writeAllLines(SUBJECTS_FILE, finalLinesToWrite);
//
//            JOptionPane.showMessageDialog(this, "Class updated successfully!",
//                    "Success", JOptionPane.INFORMATION_MESSAGE);
//            this.dispose();
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, "Error updating class data: " + e.getMessage(),
//                    "Error", JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
//        }
//    }
//}


// File: src/main/java/com/tutorapp/ui/frames/TUpdateClassFrame.java
package TutorApp.ui.frames;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import TutorApp.utils.FileUtil; // Using FileUtil for file operations

public class TUpdateClassFrame extends JFrame {
    private JComboBox<String> classComboBox;
    private JTextField subjectField, chargesField, scheduleField;
    private JComboBox<String> formLevelComboBox;
    private List<String[]> classes;
    private JPanel panel;

    private String originalSelectedSubject;
    private String originalSelectedFormLevel;

    private static final String SUBJECTS_FILE = "subjects.txt";

    public TUpdateClassFrame(String tutorUsername) {
        setTitle("Update Class");
        setSize(500, 350); // Changed to 550x600
        setLocationRelativeTo(null);

        classes = new ArrayList<>();

        panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Update Class", JLabel.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 20));
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Select Class:"), gbc);

        gbc.gridx = 1;
        classComboBox = new JComboBox<>();
        populateClassComboBox();
        classComboBox.addActionListener(e -> loadSelectedClassData());
        panel.add(classComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Subject Name:"), gbc);

        gbc.gridx = 1;
        subjectField = new JTextField(20);
        panel.add(subjectField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Form Level:"), gbc);

        gbc.gridx = 1;
        String[] formLevels = {"Form 1", "Form 2", "Form 3", "Form 4", "Form 5"};
        formLevelComboBox = new JComboBox<>(formLevels);
        panel.add(formLevelComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Charges (RM):"), gbc);

        gbc.gridx = 1;
        chargesField = new JTextField(20);
        panel.add(chargesField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Schedule:"), gbc);

        gbc.gridx = 1;
        scheduleField = new JTextField(20);
        panel.add(scheduleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton updateButton = new JButton("Update Class");
        updateButton.addActionListener(e -> updateClass());
        panel.add(updateButton, gbc);

        add(panel);
        // pack(); // No need for pack if setSize is used.
        // setVisible(true); // This will be called by the caller
    }

    private void populateClassComboBox() {
        classes.clear();
        classComboBox.removeAllItems();

        try (BufferedReader br = new BufferedReader(new FileReader(SUBJECTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    classes.add(parts);
                    classComboBox.addItem(parts[0].trim() + " (Form " + parts[1].trim() + ")");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading class data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        if (!classes.isEmpty()) {
            classComboBox.setSelectedIndex(0); // Select the first item by default
        } else {
            classComboBox.addItem("No classes available");
            classComboBox.setEnabled(false);
        }
    }

    private void loadSelectedClassData() {
        int selectedIndex = classComboBox.getSelectedIndex();
        if (selectedIndex != -1 && !classes.isEmpty()) {
            String[] selectedClass = classes.get(selectedIndex);
            originalSelectedSubject = selectedClass[0].trim();
            originalSelectedFormLevel = selectedClass[1].trim();

            subjectField.setText(originalSelectedSubject);
            formLevelComboBox.setSelectedItem(originalSelectedFormLevel);
            chargesField.setText(selectedClass[2].trim());
            scheduleField.setText(selectedClass[3].trim());
        } else {
            subjectField.setText("");
            formLevelComboBox.setSelectedIndex(-1);
            chargesField.setText("");
            scheduleField.setText("");
            originalSelectedSubject = null;
            originalSelectedFormLevel = null;
        }
    }

    private void updateClass() {
        if (originalSelectedSubject == null || originalSelectedFormLevel == null) {
            JOptionPane.showMessageDialog(this, "Please select a class to update.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newSubject = subjectField.getText().trim();
        String newFormLevel = (String) formLevelComboBox.getSelectedItem();
        String newCharges = chargesField.getText().trim();
        String newSchedule = scheduleField.getText().trim();

        if (newSubject.isEmpty() || newFormLevel == null || newCharges.isEmpty() || newSchedule.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Double.parseDouble(newCharges);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Charges must be a valid number",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String updatedClassLine = newSubject + "," + newFormLevel + "," + newCharges + "," + newSchedule;

        try {
            List<String> allFileLines = FileUtil.readAllLines(SUBJECTS_FILE);
            List<String> finalLinesToWrite = new ArrayList<>();
            boolean updated = false;

            for (String fileLine : allFileLines) {
                String[] parts = fileLine.split(",");
                if (parts.length >= 4) {
                    if (parts[0].trim().equals(originalSelectedSubject) &&
                            parts[1].trim().equals(originalSelectedFormLevel) &&
                            !updated) {
                        finalLinesToWrite.add(updatedClassLine);
                        updated = true;
                    } else {
                        finalLinesToWrite.add(fileLine);
                    }
                } else {
                    finalLinesToWrite.add(fileLine);
                }
            }

            if (!updated) {
                JOptionPane.showMessageDialog(this, "Original class not found in file or already updated. Update failed.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            FileUtil.writeAllLines(SUBJECTS_FILE, finalLinesToWrite);

            JOptionPane.showMessageDialog(this, "Class updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating class data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}