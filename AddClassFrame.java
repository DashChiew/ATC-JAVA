// File: src/main/java/com/tutorapp/ui/frames/AddClassFrame.java
package com.tutorapp.ui.frames;

import com.tutorapp.utils.FileUtil; // Using FileUtil for file operations

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class AddClassFrame extends JFrame {
    private JTextField subjectField, chargesField, scheduleField;
    private JComboBox<String> formLevelComboBox;
    private String tutorUsername;

    private static final String SUBJECTS_FILE = "src/subjects.txt"; // Defined here for this class's scope

    public AddClassFrame(String tutorUsername) {
        this.tutorUsername = tutorUsername;
        setTitle("Add New Class");
        setSize(450, 350);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Add New Class", JLabel.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 20));
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Subject Name:"), gbc);

        gbc.gridx = 1;
        subjectField = new JTextField(15);
        panel.add(subjectField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Form Level:"), gbc);

        gbc.gridx = 1;
        String[] formLevels = {"Form 1", "Form 2", "Form 3", "Form 4", "Form 5"};
        formLevelComboBox = new JComboBox<>(formLevels);
        panel.add(formLevelComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Charges (RM):"), gbc);

        gbc.gridx = 1;
        chargesField = new JTextField(15);
        panel.add(chargesField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Schedule:"), gbc);

        gbc.gridx = 1;
        scheduleField = new JTextField(15);
        panel.add(scheduleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton addButton = new JButton("Add Class");
        addButton.addActionListener(e -> addClass());
        panel.add(addButton, gbc);

        add(panel);
    }

    private void addClass() {
        String subject = subjectField.getText().trim();
        String formLevel = (String) formLevelComboBox.getSelectedItem();
        String charges = chargesField.getText().trim();
        String schedule = scheduleField.getText().trim();

        if (subject.isEmpty() || formLevel == null || charges.isEmpty() || schedule.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Double.parseDouble(charges);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Charges must be a valid number",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (FileWriter fw = new FileWriter(SUBJECTS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(subject + "," + formLevel + "," + charges + "," + schedule + "," + tutorUsername + "\n");
            JOptionPane.showMessageDialog(this, "Class added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving class data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
