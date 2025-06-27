// ClassScheduleApp.java
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.border.EmptyBorder;
import java.util.Set;
import java.util.TreeSet;
import java.util.Comparator;


// Main class for the Class Schedule GUI application
public class ClassScheduleApp extends JFrame {

    // --- ClassIn Style Color Palette ---
    private static final Color CLASSIN_PRIMARY_BLUE = new Color(59, 130, 246);
    private static final Color CLASSIN_ACCENT_BLUE = new Color(37, 99, 235);
    private static final Color CLASSIN_BACKGROUND_WHITE = new Color(255, 255, 255);
    private static final Color CLASSIN_LIGHT_GRAY = new Color(240, 240, 240);
    private static final Color CLASSIN_DARK_TEXT = new Color(51, 51, 51);
    private static final Color CLASSIN_LIGHT_BORDER = new Color(200, 200, 200);

    private JTable scheduleTable;
    private DefaultTableModel tableModel;
    private List<Subject> allSubjects;
    private Student currentStudent;
    private JComboBox<String> dayFilterComboBox;
    private JFrame parentFrame;

    // Constructor for the ClassScheduleApp GUI
    public ClassScheduleApp(Student student, JFrame parentFrame) {
        if (student == null) {
            throw new IllegalArgumentException("Student object cannot be null for ClassScheduleApp.");
        }
        this.currentStudent = student;
        this.parentFrame = parentFrame;

        setTitle("Class Schedule");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(CLASSIN_BACKGROUND_WHITE);

        // --- Main Panel with BorderLayout ---
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(CLASSIN_BACKGROUND_WHITE);
        // REVERTED: Main panel border back to original padding
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Top Control Panel (for Return button and Header) ---
        JPanel topControlPanel = new JPanel(new BorderLayout());
        topControlPanel.setBackground(CLASSIN_BACKGROUND_WHITE);

        // --- Return Button ---
        JButton returnButton = new JButton("Return");
        returnButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        returnButton.setBackground(CLASSIN_PRIMARY_BLUE);
        returnButton.setForeground(CLASSIN_DARK_TEXT);
        returnButton.setFocusPainted(false);
        returnButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        returnButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                }
            }
        });

        JPanel returnButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        returnButtonPanel.setBackground(CLASSIN_BACKGROUND_WHITE);
        // CORRECTED: Syntax error (comma to dot)
        returnButtonPanel.setForeground(CLASSIN_DARK_TEXT);
        returnButtonPanel.add(returnButton);
        returnButtonPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        topControlPanel.add(returnButtonPanel, BorderLayout.WEST);

        // --- Header Panel for Title, Student's Form Level, and Day Filter ---
        JPanel headerContentPanel = new JPanel();
        headerContentPanel.setLayout(new BoxLayout(headerContentPanel, BoxLayout.Y_AXIS));
        headerContentPanel.setBackground(CLASSIN_BACKGROUND_WHITE);
        // REVERTED: Header content panel border back to original padding
        headerContentPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        headerContentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel mainTitleLabel = new JLabel("Class Schedule Overview");
        mainTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        mainTitleLabel.setForeground(CLASSIN_DARK_TEXT);
        // REVERTED: Align title to the center
        mainTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel formLevelLabel = new JLabel("For: " + currentStudent.getFormLevel());
        formLevelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        formLevelLabel.setForeground(CLASSIN_DARK_TEXT);
        // REVERTED: Align form level to the center
        formLevelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerContentPanel.add(mainTitleLabel);
        headerContentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerContentPanel.add(formLevelLabel);
        headerContentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- Day Filter Panel ---
        // REVERTED: Changed FlowLayout back to CENTER
        JPanel filterDayPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filterDayPanel.setBackground(CLASSIN_BACKGROUND_WHITE);

        JLabel filterDayLabel = new JLabel("Filter by Day:");
        filterDayLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterDayLabel.setForeground(CLASSIN_DARK_TEXT);
        filterDayPanel.add(filterDayLabel);

        allSubjects = loadSubjectsFromFile("subjects.txt");

        dayFilterComboBox = new JComboBox<>();
        dayFilterComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dayFilterComboBox.setBackground(CLASSIN_BACKGROUND_WHITE);
        dayFilterComboBox.setForeground(CLASSIN_DARK_TEXT);
        dayFilterComboBox.setBorder(BorderFactory.createLineBorder(CLASSIN_LIGHT_BORDER));

        populateDayFilterComboBox();

        dayFilterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterSubjects();
            }
        });
        filterDayPanel.add(dayFilterComboBox);

        // REMOVED/REVERTED: No longer need explicit alignment if FlowLayout is CENTER
        // filterDayPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerContentPanel.add(filterDayPanel);

        topControlPanel.add(headerContentPanel, BorderLayout.CENTER);

        mainPanel.add(topControlPanel, BorderLayout.NORTH);

        // --- Table Setup ---
        String[] columnNames = {"Subject", "Form", "Time", "Tutor"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scheduleTable = new JTable(tableModel);

        scheduleTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        scheduleTable.setRowHeight(30);
        scheduleTable.setForeground(CLASSIN_DARK_TEXT);
        scheduleTable.setBackground(CLASSIN_BACKGROUND_WHITE);
        scheduleTable.setSelectionBackground(new Color(200, 220, 255));
        scheduleTable.setGridColor(CLASSIN_LIGHT_BORDER);

        scheduleTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        scheduleTable.getTableHeader().setBackground(CLASSIN_LIGHT_GRAY);
        scheduleTable.getTableHeader().setForeground(CLASSIN_DARK_TEXT);
        ((DefaultTableCellRenderer) scheduleTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // FIX STARTS HERE
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer(); // This line was correct
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        for (int i = 0; i < scheduleTable.getColumnCount(); i++) {
            scheduleTable.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }
        // FIX ENDS HERE

        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        scrollPane.setBackground(CLASSIN_BACKGROUND_WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(CLASSIN_LIGHT_BORDER, 1));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        filterSubjects();
    }

    private List<Subject> loadSubjectsFromFile(String filename) {
        List<Subject> subjects = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length == 5) {
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
            if (subject.getForm().equals(currentStudent.getFormLevel())) {
                uniqueDays.add(subject.getDayOfWeek());
            }
        }

        for (String day : uniqueDays) {
            dayFilterComboBox.addItem(day);
        }
    }

    private void filterSubjects() {
        tableModel.setRowCount(0);
        String studentFormLevel = currentStudent.getFormLevel();
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
                Student dummyStudent = new Student(
                        "S001", "Dummy Student", "123456-07-8901", "dummy@example.com",
                        "012-3456789", "123 Dummy Street", "Form 4", "Math,Science", "January"
                );
                JFrame dummyParent = new JFrame();
                dummyParent.setTitle("Dummy Parent Frame");
                dummyParent.setSize(400, 300);
                dummyParent.setLocationRelativeTo(null);
                dummyParent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                new ClassScheduleApp(dummyStudent, dummyParent).setVisible(true);
            }
        });
    }
}