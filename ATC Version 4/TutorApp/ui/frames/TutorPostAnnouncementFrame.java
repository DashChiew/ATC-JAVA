package TutorApp.ui.frames;

import TutorApp.utils.FileUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

public class TutorPostAnnouncementFrame extends JFrame {

    private JComboBox<String> studentComboBox;
    private JTextArea announcementTextArea;
    private JButton postButton;
    private Map<String, String> studentNameToIdMap;

    private static final String ENROLMENT_FILE = "student_enrollment.txt";
    private static final String NOTIFICATION_FILE = "notification.txt";

    public TutorPostAnnouncementFrame(String tutorUsername) {
        setTitle("Post Announcement");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        studentNameToIdMap = new HashMap<>();

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 242, 245));

        JLabel titleLabel = new JLabel("Post Announcement to Student", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 24));
        titleLabel.setForeground(new Color(55, 65, 81));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.putClientProperty("JComponent.roundRect", true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Select Student:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        studentComboBox = new JComboBox<>();
        studentComboBox.setFont(new Font("Inter", Font.PLAIN, 14));
        formPanel.add(studentComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Announcement:"), gbc);

        gbc.gridy = 2;
        gbc.weighty = 1.0;
        announcementTextArea = new JTextArea(10, 40);
        announcementTextArea.setLineWrap(true);
        announcementTextArea.setWrapStyleWord(true);
        announcementTextArea.setFont(new Font("Inter", Font.PLAIN, 14));
        announcementTextArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        JScrollPane scrollPane = new JScrollPane(announcementTextArea);
        formPanel.add(scrollPane, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 242, 245));
        postButton = new JButton("Post Announcement");
        postButton.setFont(new Font("Inter", Font.BOLD, 16));
        postButton.setBackground(new Color(24, 144, 255));
        postButton.setForeground(Color.WHITE);
        postButton.setFocusPainted(false);
        postButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        postButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                postAnnouncement();
            }
        });
        buttonPanel.add(postButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        populateStudentComboBox();
    }

    private void populateStudentComboBox() {
        studentComboBox.removeAllItems();
        studentNameToIdMap.clear();
        Vector<String> studentDisplayNames = new Vector<>();

        try {
            List<String> lines = FileUtil.readAllLines(ENROLMENT_FILE);

            int startIndex = 0;

            if (lines.isEmpty() || (lines.size() == 1 && lines.get(0).trim().isEmpty())) {
                studentDisplayNames.add("No students enrolled");
                studentComboBox.setEnabled(false);
                postButton.setEnabled(false);
            } else {
                for (int i = startIndex; i < lines.size(); i++) {
                    String line = lines.get(i);
                    if (!line.trim().isEmpty()) {
                        String[] parts = FileUtil.parseCsvLine(line);
                        if (parts.length >= 2) {
                            String studentName = parts[0].trim().replace("\"", "");
                            String studentId = parts[1].trim().replace("\"", "");

                            if (!studentName.isEmpty() && !studentId.isEmpty()) {
                                studentDisplayNames.add(studentName);
                                studentNameToIdMap.put(studentName, studentId);
                            }
                        }
                    }
                }

                if (studentDisplayNames.isEmpty()) {
                    studentDisplayNames.add("No students found after processing");
                    studentComboBox.setEnabled(false);
                    postButton.setEnabled(false);
                } else {
                    studentComboBox.setEnabled(true);
                    postButton.setEnabled(true);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading student enrollment data: " + e.getMessage(),
                    "File Read Error", JOptionPane.ERROR_MESSAGE);
            studentDisplayNames.add("Error loading students");
            studentComboBox.setEnabled(false);
            postButton.setEnabled(false);
            e.printStackTrace();
        }
        studentComboBox.setModel(new DefaultComboBoxModel<>(studentDisplayNames));
    }

    private void postAnnouncement() {
        String selectedStudentName = (String) studentComboBox.getSelectedItem();
        String announcementText = announcementTextArea.getText().trim();

        if (selectedStudentName == null || selectedStudentName.equals("No students enrolled") || selectedStudentName.equals("Error loading students") || selectedStudentName.equals("No students found after processing")) {
            JOptionPane.showMessageDialog(this, "Please select a valid student.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (announcementText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Announcement cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // We still need the studentId if other parts of your system rely on it for notifications
        // or if the notification file format mandates it.
        // If you ONLY want the name in notification.txt, then just use selectedStudentName.
        // For now, let's include the name as the identifier in the notification.txt file.
        // If you still need the ID internally for other processes, keep the lookup.

        // Get current date and time
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Format: StudentName,Timestamp,AnnouncementText
        // Changed from studentId to selectedStudentName
        String notificationEntry = selectedStudentName + "," + timestamp + "," + escapeCsv(announcementText);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(NOTIFICATION_FILE, true))) {
            bw.write(notificationEntry);
            bw.newLine();
            JOptionPane.showMessageDialog(this, "Announcement posted successfully to " + selectedStudentName + "!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            announcementTextArea.setText("");
            this.dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing announcement to file: " + e.getMessage(),
                    "File Write Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String escapeCsv(String text) {
        if (text.contains(",") || text.contains("\"") || text.contains("\n")) {
            return "\"" + text.replace("\"", "\"\"") + "\"";
        }
        return text;
    }
}