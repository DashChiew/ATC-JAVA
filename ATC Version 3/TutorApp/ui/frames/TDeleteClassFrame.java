//// File: src/main/java/com/tutorapp/ui/frames/TDeleteClassFrame.java
//package TutorApp.ui.frames;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import TutorApp.utils.FileUtil; // Using FileUtil for file operations
//
//public class TDeleteClassFrame extends JFrame {
//    private JComboBox<String> classComboBox;
//    private List<String[]> classes;
//    private JPanel panel;
//
//    private static final String SUBJECTS_FILE = "subjects.txt";
//
//    public TDeleteClassFrame(String tutorUsername) {
//        setTitle("Delete Class");
//        setSize(400, 200);
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
//        JLabel titleLabel = new JLabel("Delete Class", JLabel.CENTER);
//        titleLabel.setFont(new Font("Inter", Font.BOLD, 20));
//        panel.add(titleLabel, gbc);
//
//        gbc.gridwidth = 1;
//        gbc.gridy = 1;
//        panel.add(new JLabel("Select Class:"), gbc);
//
//        gbc.gridx = 1;
//        classComboBox = new JComboBox<>();
//        loadClasses();
//        panel.add(classComboBox, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        gbc.gridwidth = 2;
//        gbc.anchor = GridBagConstraints.CENTER;
//        JButton deleteButton = new JButton("Delete Class");
//        deleteButton.addActionListener(e -> deleteClass());
//        panel.add(deleteButton, gbc);
//
//        add(panel);
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
//            JOptionPane.showMessageDialog(this, "Error reading class data for deletion: " + e.getMessage(),
//                    "Error", JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
//        }
//
//        JButton deleteButton = null;
//        for (Component comp : panel.getComponents()) {
//            if (comp instanceof JButton && ((JButton)comp).getText().equals("Delete Class")) {
//                deleteButton = (JButton)comp;
//                break;
//            }
//        }
//
//        if (classComboBox.getItemCount() == 0) {
//            classComboBox.addItem("No classes found");
//            if (deleteButton != null) {
//                deleteButton.setEnabled(false);
//            }
//        } else {
//            if (deleteButton != null) {
//                deleteButton.setEnabled(true);
//            }
//        }
//    }
//
//    private void deleteClass() {
//        int selectedIndex = classComboBox.getSelectedIndex();
//        if (selectedIndex < 0 || selectedIndex >= classes.size() || (classComboBox.getSelectedItem() != null && classComboBox.getSelectedItem().equals("No classes found"))) {
//            JOptionPane.showMessageDialog(this, "Please select a valid class to delete.",
//                    "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        int confirm = JOptionPane.showConfirmDialog(this,
//                "Are you sure you want to delete this class?",
//                "Confirm Delete", JOptionPane.YES_NO_OPTION);
//
//        if (confirm == JOptionPane.YES_OPTION) {
//            String[] classToDelete = classes.get(selectedIndex);
//            String subjectToDelete = classToDelete[0].trim();
//            String formToDelete = classToDelete[1].trim();
//
//            try {
//                List<String> remainingLines = new ArrayList<>();
//                List<String> allFileLines = FileUtil.readAllLines(SUBJECTS_FILE);
//
//                for (String line : allFileLines) {
//                    String[] parts = line.split(",");
//                    if (parts.length >= 4) {
//                        if (!(parts[0].trim().equals(subjectToDelete) &&
//                                parts[1].trim().equals(formToDelete))) {
//                            remainingLines.add(line);
//                        }
//                    } else {
//                        remainingLines.add(line);
//                    }
//                }
//
//                FileUtil.writeAllLines(SUBJECTS_FILE, remainingLines);
//
//                JOptionPane.showMessageDialog(this, "Class deleted successfully!",
//                        "Success", JOptionPane.INFORMATION_MESSAGE);
//                this.dispose();
//            } catch (IOException e) {
//                JOptionPane.showMessageDialog(this, "Error deleting class data: " + e.getMessage(),
//                        "Error", JOptionPane.ERROR_MESSAGE);
//                e.printStackTrace();
//            }
//        }
//    }
//}


// File: src/main/java/com/tutorapp/ui/frames/TDeleteClassFrame.java
package TutorApp.ui.frames;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import TutorApp.utils.FileUtil; // Using FileUtil for file operations

public class TDeleteClassFrame extends JFrame {
    private JComboBox<String> classComboBox;
    private List<String[]> classes;
    private JPanel panel;

    private static final String SUBJECTS_FILE = "subjects.txt";

    public TDeleteClassFrame(String tutorUsername) {
        setTitle("Delete Class");
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
        JLabel titleLabel = new JLabel("Delete Class", JLabel.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 20));
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Select Class:"), gbc);

        gbc.gridx = 1;
        classComboBox = new JComboBox<>();
        populateClassComboBox();
        panel.add(classComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton deleteButton = new JButton("Delete Selected Class");
        deleteButton.addActionListener(e -> deleteClass());
        panel.add(deleteButton, gbc);

        add(panel);
        // pack(); // No need for pack if setSize is used.
        // setVisible(true); // This will be called by the caller
    }

    private void populateClassComboBox() {
        classes.clear();
        classComboBox.removeAllItems(); // Clear existing items

        try (BufferedReader br = new BufferedReader(new FileReader(SUBJECTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    // Assuming format: subject,formLevel,charges,schedule
                    String subject = parts[0].trim();
                    String formLevel = parts[1].trim();
                    classes.add(parts); // Add the full array for later use
                    classComboBox.addItem(subject + " (Form " + formLevel + ")");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading class data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        if (classes.isEmpty()) {
            classComboBox.addItem("No classes available");
            classComboBox.setEnabled(false);
        } else {
            classComboBox.setEnabled(true);
        }
    }

    private void deleteClass() {
        int selectedIndex = classComboBox.getSelectedIndex();
        if (selectedIndex == -1 || classes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a class to delete.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this class?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String[] classToDelete = classes.get(selectedIndex);
            String subjectToDelete = classToDelete[0].trim();
            String formToDelete = classToDelete[1].trim();

            try {
                List<String> remainingLines = new ArrayList<>();
                List<String> allFileLines = FileUtil.readAllLines(SUBJECTS_FILE);

                for (String line : allFileLines) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        if (!(parts[0].trim().equals(subjectToDelete) &&
                                parts[1].trim().equals(formToDelete))) {
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