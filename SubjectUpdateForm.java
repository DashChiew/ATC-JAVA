import javax.swing.*;
import javax.swing.border.AbstractBorder; // Needed if you had AbstractBorder in your RoundedBorder
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SubjectUpdateForm extends JFrame {

    // --- ClassIn Style Color Palette ---
    private static final Color CLASSIN_PRIMARY_BLUE = new Color(59, 130, 246);
    private static final Color CLASSIN_ACCENT_BLUE = new Color(37, 99, 235);
    private static final Color CLASSIN_BACKGROUND_WHITE = new Color(255, 255, 255);
    private static final Color CLASSIN_LIGHT_GRAY = new Color(240, 240, 240);
    private static final Color CLASSIN_DARK_TEXT = new Color(51, 51, 51);
    private static final Color CLASSIN_LIGHT_BORDER = new Color(200, 200, 200);

    // Declare components
    private JButton returnButton;
    private JLabel titleLabel;
    private JTextField subject1Field;
    private JTextField subject2Field;
    private JTextField subject3Field;
    private JButton updateButton;

    // Table components
    private JTable requestTable;
    private DefaultTableModel requestTableModel;

    // Variables to store initial subject values for comparison
    private String initialSubject1;
    private String initialSubject2;
    private String initialSubject3;

    // New field to store the current user's username
    private String currentUsername;

    /**
     * Constructor for SubjectUpdateForm.
     *
     * @param username The username of the currently logged-in student.
     */
    public SubjectUpdateForm(String username) {
        this.currentUsername = username;
        System.out.println("SubjectUpdateForm: Initialized for user: " + currentUsername);

        // Set up the JFrame (main window)
        setTitle("Update Your Profile");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 750); // <--- Adjusted size to fit all content
        setLocationRelativeTo(null);
        setResizable(false);

        getContentPane().setBackground(CLASSIN_BACKGROUND_WHITE);

        // Create the main content panel using BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(CLASSIN_BACKGROUND_WHITE);

        // --- Top Panel for Return Button ONLY ---
        JPanel topContainerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topContainerPanel.setOpaque(false);

        returnButton = new JButton("Return");
        returnButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        returnButton.setBackground(CLASSIN_PRIMARY_BLUE);
        returnButton.setForeground(Color.BLACK);
        // Assuming RoundedBorder is defined elsewhere or will be provided
        returnButton.setBorder(new RoundedBorder(15, CLASSIN_PRIMARY_BLUE, 0));
        returnButton.setPreferredSize(new Dimension(100, 40));
        returnButton.setFocusPainted(false);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        returnButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                returnButton.setBackground(CLASSIN_ACCENT_BLUE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                returnButton.setBackground(CLASSIN_PRIMARY_BLUE);
            }
        });
        topContainerPanel.add(returnButton);

        mainPanel.add(topContainerPanel, BorderLayout.NORTH);

        // --- Center Panel for Title and Subject Fields ---
        JPanel fieldsContainerPanel = new JPanel();
        fieldsContainerPanel.setLayout(new BoxLayout(fieldsContainerPanel, BoxLayout.Y_AXIS));
        fieldsContainerPanel.setOpaque(false);
        fieldsContainerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

        titleLabel = new JLabel("Update Your Subjects");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(CLASSIN_DARK_TEXT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        fieldsContainerPanel.add(titleLabel);
        fieldsContainerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        this.subject1Field = addSubjectRow(fieldsContainerPanel, "Subject 1:", "BAHASA MELAYU");
        this.subject2Field = addSubjectRow(fieldsContainerPanel, "Subject 2:", "ENGLISH");
        this.subject3Field = addSubjectRow(fieldsContainerPanel, "Subject 3:", "CHINESE");

        // Store initial values after fields are populated
        initialSubject1 = subject1Field.getText();
        initialSubject2 = subject2Field.getText();
        initialSubject3 = subject3Field.getText();

        mainPanel.add(fieldsContainerPanel, BorderLayout.CENTER);

        // --- Bottom Panel for Update Button and New Table ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS)); // Stack button and table vertically
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); // Add padding

        // Update Button Panel (reused from previous code)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        buttonPanel.setOpaque(false);
        updateButton = new JButton("Update Subjects");
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        updateButton.setBackground(CLASSIN_PRIMARY_BLUE);
        updateButton.setForeground(Color.BLACK);
        updateButton.setBorder(new RoundedBorder(15, CLASSIN_PRIMARY_BLUE, 0));
        updateButton.setPreferredSize(new Dimension(180, 45));
        updateButton.setFocusPainted(false);
        updateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                updateButton.setBackground(CLASSIN_ACCENT_BLUE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                updateButton.setBackground(CLASSIN_PRIMARY_BLUE);
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String updatedSubject1 = subject1Field.getText();
                String updatedSubject2 = subject2Field.getText();
                String updatedSubject3 = subject3Field.getText();

                boolean anySubjectChanged = false;

                if (!updatedSubject1.equals(initialSubject1)) {
                    saveChangedSubjectToFile(updatedSubject1, "Pending");
                    anySubjectChanged = true;
                }
                if (!updatedSubject2.equals(initialSubject2)) {
                    saveChangedSubjectToFile(updatedSubject2, "Pending");
                    anySubjectChanged = true;
                }
                if (!updatedSubject3.equals(initialSubject3)) {
                    saveChangedSubjectToFile(updatedSubject3, "Pending");
                    anySubjectChanged = true;
                }

                if (anySubjectChanged) {
                    JOptionPane.showMessageDialog(SubjectUpdateForm.this, "Subjects updated and saved to file (Pending).", "Update Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(SubjectUpdateForm.this, "No subjects were changed.", "No Change", JOptionPane.INFORMATION_MESSAGE);
                }

                System.out.println("Subject 1 updated to: " + updatedSubject1);
                System.out.println("Subject 2 updated to: " + updatedSubject2);
                System.out.println("Subject 3 updated to: " + updatedSubject3);

                // Optionally, re-load requests to ensure the table is up-to-date
                loadRequestsFromFile();
                // Instead of disposing, keep the form open to show the updated table
                // dispose();
            }
        });
        buttonPanel.add(updateButton);
        bottomPanel.add(buttonPanel); // Add button panel to the bottomPanel

        bottomPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer

        // History Label
        JLabel historyLabel = new JLabel("Subject Change Request History");
        historyLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        historyLabel.setForeground(CLASSIN_DARK_TEXT);
        historyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(historyLabel);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer

        // Table for change requests
        requestTableModel = new DefaultTableModel(new Object[][]{},
                new String[]{"Date", "Requested Changing Subject", "Status"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        requestTable = new JTable(requestTableModel);
        requestTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        requestTable.setRowHeight(25);
        requestTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        requestTable.getTableHeader().setBackground(CLASSIN_LIGHT_GRAY);
        requestTable.setForeground(CLASSIN_DARK_TEXT);
        requestTable.setBackground(CLASSIN_BACKGROUND_WHITE);
        requestTable.setSelectionBackground(new Color(200, 220, 255)); // Light blue selection
        requestTable.setGridColor(CLASSIN_LIGHT_BORDER);

        // Center align table headers
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        requestTable.getTableHeader().setDefaultRenderer(headerRenderer);

        // Center align "Date" and "Status" column data, left align "Requested Changing Subject"
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);

        requestTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Date
        requestTable.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);   // Requested Changing Subject
        requestTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Status


        JScrollPane tableScrollPane = new JScrollPane(requestTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 150)); // Set preferred size for the scroll pane
        tableScrollPane.setBackground(CLASSIN_BACKGROUND_WHITE);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(CLASSIN_LIGHT_BORDER, 1));
        bottomPanel.add(tableScrollPane);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        loadRequestsFromFile(); // Load history when the form initializes
    }

    public SubjectUpdateForm(Student currentStudent, ProfilePageGUI profilePageGUI) {

    }

    /**
     * Helper method to create and add a subject row (label + text field) to a panel.
     * Applies ClassIn styling.
     * Returns the JTextField instance.
     */
    private JTextField addSubjectRow(JPanel parentPanel, String labelText, String initialText) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        rowPanel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(CLASSIN_DARK_TEXT);
        rowPanel.add(label);

        JTextField textField = new JTextField(initialText, 20);
        textField.setBackground(CLASSIN_LIGHT_GRAY);
        textField.setForeground(CLASSIN_DARK_TEXT);
        textField.setFont(new Font("Segoe UI", Font.BOLD, 14));
        // Assuming RoundedBorder is defined elsewhere or will be provided
        textField.setBorder(new RoundedBorder(8, CLASSIN_LIGHT_BORDER, 1));
        textField.setMargin(new Insets(5, 10, 5, 10));
        textField.setCaretColor(CLASSIN_PRIMARY_BLUE);
        textField.setPreferredSize(new Dimension(200, 35));
        rowPanel.add(textField);

        parentPanel.add(rowPanel);
        return textField;
    }

    /**
     * Saves a changed subject to a text file named "change_subject.txt".
     * Each entry includes the student username, subject name, and its status.
     * @param subjectName The name of the subject that was changed.
     * @param status The status of the change (e.g., "Pending").
     */
    private void saveChangedSubjectToFile(String subjectName, String status) {
        // Get current date for the table entry
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

        try (PrintWriter out = new PrintWriter(new FileWriter("change_subject.txt", true))) { // true for append mode
            // Prepend username to the line
            out.println(currentUsername + ",Date: " + currentDate + ", Subject: " + subjectName + ", Status: " + status);
            System.out.println("SubjectUpdateForm: Saved changed subject to change_subject.txt: " + currentUsername + ", " + subjectName);
        } catch (IOException e) {
            System.err.println("SubjectUpdateForm: Error saving changed subject to file: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error saving changed subject details to file.",
                    "File Save Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Also add to the table model immediately after successful file save
        // No need to add username to the table view itself, as it's filtered by user
        requestTableModel.addRow(new Object[]{currentDate, subjectName, status});
    }

    /**
     * Reads subject change requests from "change_subject.txt" and populates the requestTable.
     * Only loads requests for the current user.
     */
    private void loadRequestsFromFile() {
        File file = new File("change_subject.txt");
        if (!file.exists()) {
            System.out.println("SubjectUpdateForm: change_subject.txt not found. No historical data to load.");
            return;
        }

        requestTableModel.setRowCount(0); // Clear existing table data before loading

        // Regex pattern to parse lines like "USERNAME,Date: DD MMMM YYYY, Subject: X, Status: Y"
        // It now expects the username at the beginning
        String regex = "^(.*?),(?:Date: (.*?)),(?: Subject: (.*?)),(?: Status: (.*))$";
        Pattern pattern = Pattern.compile(regex);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    try {
                        String fileUsername = matcher.group(1).trim();
                        String date = matcher.group(2).trim();
                        String subject = matcher.group(3).trim();
                        String status = matcher.group(4).trim();

                        // Only add requests relevant to the current user
                        if (fileUsername.equals(currentUsername)) {
                            requestTableModel.addRow(new Object[]{date, subject, status});
                        }
                    } catch (Exception e) {
                        System.err.println("SubjectUpdateForm: Error parsing line from file: " + line + " - " + e.getMessage());
                    }
                } else {
                    System.err.println("SubjectUpdateForm: Line did not match expected format: " + line);
                }
            }
            System.out.println("SubjectUpdateForm: Subject change requests loaded for user " + currentUsername + " from change_subject.txt successfully.");
        } catch (FileNotFoundException e) {
            System.err.println("SubjectUpdateForm: change_subject.txt not found during load: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("SubjectUpdateForm: Error reading change_subject.txt: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error loading subject change history from file.",
                    "File Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // For testing, hardcode a username that would typically come from a login or session
            new SubjectUpdateForm("S001").setVisible(true); // Example: "S001"
        });
    }

    // Placeholder for RoundedBorder class, assuming it's defined elsewhere
    // If not, you'll need to define it or remove its usage
    private static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color lineColor;
        private int thickness;

        RoundedBorder(int radius, Color lineColor, int thickness) {
            this.radius = radius;
            this.lineColor = lineColor;
            this.thickness = thickness;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            super.paintBorder(c, g, x, y, width, height);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(lineColor);
            Stroke oldStroke = g2.getStroke();
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.setStroke(oldStroke);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius, this.radius, this.radius, this.radius);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = this.radius;
            return insets;
        }
    }
}
