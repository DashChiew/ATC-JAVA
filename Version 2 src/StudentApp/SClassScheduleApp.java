package StudentApp;

import StudentApp.model.SStudent; // For SStudent class

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import java.util.Set;
import java.util.TreeSet;
import java.util.Comparator;

// IMPORTANT: Correct import for your custom Subject class
import common.model.Subject; // This is the correct import for your custom Subject class

// Main class for the Class Schedule GUI application
public class SClassScheduleApp extends JFrame {

    // --- ClassIn Style Color Palette ---
    private static final Color CLASSIN_PRIMARY_BLUE = new Color(59, 130, 246);
    private static final Color CLASSIN_ACCENT_BLUE = new Color(37, 99, 235);
    private static final Color CLASSIN_BACKGROUND_WHITE = new Color(255, 255, 255);
    private static final Color CLASSIN_LIGHT_GRAY = new Color(240, 240, 240);
    private static final Color CLASSIN_DARK_TEXT = new Color(51, 51, 51);
    private static final Color CLASSIN_LIGHT_BORDER = new Color(220, 220, 220);

    private SStudent currentSStudent;
    private JFrame parentFrame;
    private DefaultTableModel tableModel;
    private JComboBox<String> dayFilterComboBox;
    private List<Subject> allSubjects; // List to hold all subjects read from file

    // Constructor updated to accept SStudent and parent JFrame
    public SClassScheduleApp(SStudent sStudent, JFrame parentFrame) {
        this.currentSStudent = sStudent;
        this.parentFrame = parentFrame;
        this.allSubjects = loadSubjectsFromFile("subjects.txt"); // Load subjects when the app starts

        setTitle("Class Schedule for " + currentSStudent.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600); // Increased size for better layout
        setLocationRelativeTo(parentFrame); // Center relative to parent

        // Set layout and background
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(CLASSIN_BACKGROUND_WHITE);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(CLASSIN_PRIMARY_BLUE);
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JLabel titleLabel = new JLabel("My Class Schedule");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(CLASSIN_LIGHT_GRAY);
        filterPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        filterPanel.add(new JLabel("Filter by Day:"));
        dayFilterComboBox = new JComboBox<>();
        dayFilterComboBox.addActionListener(e -> filterSubjects());
        populateDayFilterComboBox(); // Populate the combo box
        filterPanel.add(dayFilterComboBox);
        add(filterPanel, BorderLayout.NORTH); // Changed to NORTH to be below header if header is present

        // Table setup
        String[] columnNames = {"Subject Name", "Form Level", "Time", "Tutor"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        JTable scheduleTable = new JTable(tableModel);
        scheduleTable.setFont(new Font("Arial", Font.PLAIN, 14));
        scheduleTable.setRowHeight(30);
        scheduleTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        scheduleTable.getTableHeader().setBackground(CLASSIN_ACCENT_BLUE);
        scheduleTable.getTableHeader().setForeground(Color.WHITE);
        scheduleTable.setGridColor(CLASSIN_LIGHT_BORDER);
        scheduleTable.setBackground(CLASSIN_BACKGROUND_WHITE);
        scheduleTable.setForeground(CLASSIN_DARK_TEXT);
        scheduleTable.setSelectionBackground(new Color(190, 220, 255)); // Light blue selection
        scheduleTable.setSelectionForeground(CLASSIN_DARK_TEXT);

        // Center align table headers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < scheduleTable.getColumnCount(); i++) {
            scheduleTable.getColumnModel().getColumn(i).setHeaderRenderer(centerRenderer);
            scheduleTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default scroll pane border
        add(scrollPane, BorderLayout.CENTER);

        filterSubjects(); // Initial population of the table

        // Back Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(CLASSIN_BACKGROUND_WHITE);
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(CLASSIN_ACCENT_BLUE);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.addActionListener(e -> {
            dispose(); // Close current window
            parentFrame.setVisible(true); // Show parent frame
        });
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private List<Subject> loadSubjectsFromFile(String filename) {
        List<Subject> subjects = new ArrayList<>();
        // Ensure the file exists before attempting to read
        // You might need a FileHandler.createFileIfNotExists method here
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) { // Ensure enough parts for Subject constructor
                    try {
                        String name = parts[0].trim();
                        String form = parts[1].trim();
                        double price = Double.parseDouble(parts[2].trim());
                        String time = parts[3].trim();
                        String tutor = parts[4].trim();
                        subjects.add(new Subject(name, form, price, time, tutor));
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping malformed line (price error): " + line + " - " + e.getMessage());
                    }
                } else {
                    System.err.println("Skipping malformed line (incorrect parts count): " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading subjects file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return subjects;
    }

    private void populateDayFilterComboBox() {
        Set<String> uniqueDays = new TreeSet<>(new Comparator<String>() {
            private final List<String> DAY_ORDER = List.of(
                    "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
            );
            @Override
            public int compare(String s1, String s2) {
                if (s1.equals("All Days")) return -1;
                if (s2.equals("All Days")) return 1;
                return Integer.compare(DAY_ORDER.indexOf(s1), DAY_ORDER.indexOf(s2));
            }
        });

        uniqueDays.add("All Days");

        for (Subject subject : allSubjects) {
            if (subject.getForm().equals(currentSStudent.getFormLevel())) {
                uniqueDays.add(subject.getDayOfWeek());
            }
        }

        for (String day : uniqueDays) {
            dayFilterComboBox.addItem(day);
        }
    }

    private void filterSubjects() {
        tableModel.setRowCount(0);
        String studentFormLevel = currentSStudent.getFormLevel();
        String selectedDay = (String) dayFilterComboBox.getSelectedItem();

        for (Subject subject : allSubjects) {
            if (subject.getForm().equals(studentFormLevel) &&
                    (selectedDay.equals("All Days") || subject.getDayOfWeek().equals(selectedDay))) {
                tableModel.addRow(new Object[]{
                        subject.getName(),
                        subject.getForm(),
                        subject.getLessonTimeOnly(),
                        subject.getTutor()
                });
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SStudent dummySStudent = new SStudent(
                        "S001", "Dummy Student", "123456-07-8901", "dummy@example.com",
                        "012-3456789", "123 Dummy Street", "Form 4", "Math,Science", "January"
                );
                JFrame dummyParent = new JFrame();
                dummyParent.setTitle("Dummy Parent Frame");
                dummyParent.setSize(400, 300);
                dummyParent.setLocationRelativeTo(null);
                dummyParent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                new SClassScheduleApp(dummySStudent, dummyParent).setVisible(true);
            }
        });
    }
}