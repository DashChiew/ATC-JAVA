package StudentApp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension; // Import Dimension
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JOptionPane;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import StudentApp.SProfilePageGUI;
import StudentApp.model.SStudent;


public class SMainPayment extends javax.swing.JFrame {

    private SProfilePageGUI SProfilePageGUI;
    private String currentUsername; // New field to store the current user's username
    private String dueDateForDisplay; // Field to store the due date for display
    private double totalUnpaidAmount = 0.0; // Field to store the total unpaid amount
    private SStudent SStudentData; // NEW: Field to hold the Student object


    /**
     * Creates new form MainPayment
     *
     * @param SProfilePageGUI The instance of ProfilePageGUI that opened this frame.
     * @param SStudent The Student object for which payments are being displayed.
     */
    // MODIFIED: Constructor now accepts ProfilePageGUI and Student
    public SMainPayment(SProfilePageGUI SProfilePageGUI, SStudent SStudent) {
        System.out.println("MainPayment: Parameterized constructor (ProfilePageGUI, Student) initialized.");
        this.SProfilePageGUI = SProfilePageGUI;
        this.SStudentData = SStudent; // Store the student data
        initializeUserData(); // Set the current username based on studentData
        initComponents();
        applyTableColumnFormatting();
        loadSummaryFromPaymentsFile(); // Load summary (price and date) from payments.txt
        loadDetailedPaymentsToTable(); // Load detailed history from payment.txt
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen

        // Set preferred size and pack the frame
        setPreferredSize(new Dimension(550, 630));
        pack(); // This should be called after initComponents for accurate sizing based on content

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("MainPayment: Window Closed event received.");
                if (SMainPayment.this.SProfilePageGUI != null) {
                    System.out.println("MainPayment: Making ProfilePageGUI visible after MainPayment disposal.");
                    SMainPayment.this.SProfilePageGUI.setVisible(true);
                } else {
                    System.out.println("MainPayment: ProfilePageGUI reference is null, cannot make it visible.");
                }
            }
        });
    }

    // Default constructor (no-argument constructor) - kept for compatibility/testing if needed
    public SMainPayment() {
        System.out.println("MainPayment: Default constructor initialized.");
        // If called without a student, currentUsername will default to "S001" by initializeUserData()
        initializeUserData();
        initComponents();
        applyTableColumnFormatting();
        loadSummaryFromPaymentsFile();
        loadDetailedPaymentsToTable();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set preferred size and pack the frame
        setPreferredSize(new Dimension(550, 630));
        pack(); // This should be called after initComponents for accurate sizing based on content
    }

    /**
     * Method to get the current username.
     * Now uses the studentData if available, otherwise defaults to "S001".
     */
    private void initializeUserData() {
        // MODIFIED: Check for null or empty getLoginUsername() and fallback to getStudentId()
        if (SStudentData != null && SStudentData.getLoginUsername() != null && !SStudentData.getLoginUsername().isEmpty()) {
            this.currentUsername = SStudentData.getLoginUsername();
        } else if (SStudentData != null && SStudentData.getStudentId() != null && !SStudentData.getStudentId().isEmpty()) {
            this.currentUsername = SStudentData.getStudentId(); // Fallback to studentId if loginUsername is null/empty
        } else {
            this.currentUsername = "S001"; // Default fallback if no student data or IDs are available
        }
        System.out.println("MainPayment: Current username set to: " + currentUsername);
        // Debugging statement
        System.out.println("MainPayment: SStudentData (passed from Profile) is null: " + (SStudentData == null));
        if (SStudentData != null) {
            System.out.println("MainPayment: SStudentData Student ID: " + SStudentData.getStudentId());
            System.out.println("MainPayment: SStudentData Login Username: " + SStudentData.getLoginUsername());
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jProgressBar1 = new javax.swing.JProgressBar();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        javax.swing.JLabel needToBePaidLabel = new javax.swing.JLabel();
        javax.swing.JLabel dueDateLabel = new javax.swing.JLabel();
        javax.swing.JLabel viewHistoryLabel = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        viewPaymentTable = new javax.swing.JTable();
        returnButton = new javax.swing.JButton();
        priceLabel = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel(); // MODIFIED: No hardcoded date here
        jButton1 = new javax.swing.JButton();

        jScrollPane2.setViewportView(jEditorPane1);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String [] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        jScrollPane3.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        getContentPane().add(jPanel1);

        needToBePaidLabel.setFont(new java.awt.Font("HarmonyOS Sans SC Medium", 1, 38)); // NOI18N
        needToBePaidLabel.setForeground(new java.awt.Color(230, 0, 0));
        needToBePaidLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        needToBePaidLabel.setText("Need To Be Paid");

        dueDateLabel.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 15)); // NOI18N
        dueDateLabel.setText("Due Date: ");
        dueDateLabel.setToolTipText("");

        viewHistoryLabel.setFont(new java.awt.Font("HarmonyOS Sans SC", 0, 18)); // NOI18N
        viewHistoryLabel.setText("View history");

        viewPaymentTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {
                        "Payment Method", "Payment Date", "Total Collected", "Status"
                }
        ) {
            Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                    false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(viewPaymentTable);

        returnButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        returnButton.setText("Return");
        returnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnButtonActionPerformed(evt);
            }
        });

        priceLabel.setText("RM 0.00"); // Initialize with 0.00
        priceLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        priceLabel.setFont(new java.awt.Font("Segoe UI", 1, 55)); // NOI18N


        dateLabel.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        dateLabel.setForeground(Color.BLUE);

        jButton1.setText("Pay Now");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                // MODIFIED: Increased preferred width for returnButton
                                                .addComponent(returnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(27, 27, 27)
                                                .addComponent(needToBePaidLabel))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(17, 17, 17)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                // MODIFIED: Center the jScrollPane4 (table)
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(viewHistoryLabel)
                                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                .addComponent(dueDateLabel)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(95, 95, 95)
                                                // MODIFIED: Increased preferred width for priceLabel
                                                .addComponent(priceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(needToBePaidLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(returnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(priceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(dueDateLabel)
                                                        .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(28, 28, 28)
                                                .addComponent(viewHistoryLabel))
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>

    private void returnButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Dispose the current MainPayment frame
        this.dispose();

        // Check if the profilePageGUI reference is not null before making it visible
        if (SProfilePageGUI != null) {
            SProfilePageGUI.setVisible(true);
        } else {
            System.out.println("MainPayment: ProfilePageGUI reference is null. Cannot return to it.");
            // Handle cases where MainPayment might be run directly for testing
            // You might want to exit the application or show a message
            // if there's no parent frame to return to.
            // System.exit(0); // Example: exit if no parent
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        // Make the current MainPayment frame invisible
        this.setVisible(false);
        // Create and show the Payment frame, passing this MainPayment instance
        new SAskPayment(this, totalUnpaidAmount).setVisible(true); // Pass the totalUnpaidAmount
    }

    private void applyTableColumnFormatting() {
        // Center align the header
        ((DefaultTableCellRenderer) viewPaymentTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        // Center align all cells in the table
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        for (int i = 0; i < viewPaymentTable.getColumnCount(); i++) {
            viewPaymentTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Custom renderer for the "Status" column to color text
        viewPaymentTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) value;
                if ("PAID".equalsIgnoreCase(status)) {
                    c.setForeground(new Color(0, 153, 0)); // Green for PAID
                } else if ("UNPAID".equalsIgnoreCase(status) || "PENDING".equalsIgnoreCase(status)) {
                    c.setForeground(new Color(230, 0, 0)); // Red for UNPAID/PENDING
                } else {
                    c.setForeground(table.getForeground()); // Default color
                }
                return c;
            }
        });
    }

    public void addPaymentEntry(String paymentMethod, String paymentDate, double totalCollected, String status) {
        // MODIFIED: This saves to "payment.txt" (detailed transaction history)
        String fileName = "payment.txt";
        String entryLine = String.format("%s,%s,%s,%.2f,%s",
                currentUsername, paymentMethod, paymentDate, totalCollected, status);

        savePaymentToFile(entryLine, fileName); // Save the new payment to the detailed file

        // After adding a new entry, reload both summary and detailed table
        loadDetailedPaymentsToTable(); // Refresh the detailed table
        loadSummaryFromPaymentsFile(); // Refresh the summary (price and date)
    }

    private void savePaymentToFile(String paymentEntry, String fileName) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName, true))) { // true for append mode
            out.println(paymentEntry);
            System.out.println("MainPayment: Payment entry saved to " + fileName + ": " + paymentEntry);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving payment to file: " + e.getMessage(), "File Save Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // NEW METHOD: Loads summary (total unpaid, due date) from payments.txt
    private void loadSummaryFromPaymentsFile() {
        totalUnpaidAmount = 0.0; // Reset unpaid amount
        dueDateForDisplay = "N/A"; // Reset due date display

        File file = new File("payments.txt"); // Read from payments.txt
        if (!file.exists()) {
            System.out.println("MainPayment: payments.txt does not exist. No summary to load.");
            updatePriceLabel();
            updateDateLabel();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // REGEX FOR 4 FIELDS: username, amount, status, date
            Pattern pattern = Pattern.compile("^(.*?),(.*?),(.*?),(.*?)$");
            boolean foundUnpaidDate = false; // Flag to track if an unpaid date has been found

            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Trim whitespace from the line
                if (line.isEmpty()) { // Skip empty lines after trimming
                    continue;
                }

                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    try {
                        String fileUsername = matcher.group(1);
                        double amount = Double.parseDouble(matcher.group(2)); // Amount is the 2nd field
                        String status = matcher.group(3); // Status is the 3rd field
                        String date = matcher.group(4); // Date is the 4th field

                        // Only process payments relevant to the current user
                        if (fileUsername.equals(currentUsername)) {
                            // Update totalUnpaidAmount based on status and capture the first unpaid date
                            if (("UNPAID".equalsIgnoreCase(status) || "PENDING".equalsIgnoreCase(status)) && !foundUnpaidDate) {
                                totalUnpaidAmount += amount;
                                dueDateForDisplay = date; // Store the date of the first unpaid/pending entry
                                foundUnpaidDate = true; // Mark that we found one, so we don't overwrite
                            } else if ("UNPAID".equalsIgnoreCase(status) || "PENDING".equalsIgnoreCase(status)) {
                                totalUnpaidAmount += amount;
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("MainPayment: Skipping line in payments.txt due to invalid amount format: " + line + " - " + e.getMessage());
                    }
                } else {
                    System.err.println("MainPayment: Skipping line in payments.txt due to unexpected format (expected 4 comma-separated fields): " + line);
                }
            }
            // Debugging statement: Check values after file processing
            System.out.println("MainPayment: Calculated Total Unpaid Amount after loading: " + totalUnpaidAmount);
            System.out.println("MainPayment: Determined Due Date after loading: " + dueDateForDisplay);
        } catch (FileNotFoundException e) {
            System.err.println("MainPayment: Error: payments.txt not found. " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Summary payments file not found!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            System.err.println("MainPayment: Error reading payments.txt. " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error reading summary payments file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        updatePriceLabel(); // Update the label after loading all payments
        updateDateLabel(); // Update the date label after loading all payments
    }

    // RENAMED METHOD: Loads detailed payment history to table from payment.txt
    private void loadDetailedPaymentsToTable() {
        DefaultTableModel model = (DefaultTableModel) viewPaymentTable.getModel();
        model.setRowCount(0); // Clear existing table data before loading

        File file = new File("payment.txt"); // Read from payment.txt
        if (!file.exists()) {
            System.out.println("MainPayment: payment.txt does not exist. No detailed payments to load.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // REGEX FOR 5 FIELDS: username, paymentMethod, paymentDate, amount, status
            Pattern pattern = Pattern.compile("^(.*?),(.*?),(.*?),(.*?),(.*?)$");

            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Trim whitespace from the line
                if (line.isEmpty()) { // Skip empty lines after trimming
                    continue;
                }

                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    try {
                        String fileUsername = matcher.group(1);
                        String paymentMethodFromFile = matcher.group(2);
                        String date = matcher.group(3);
                        double amount = Double.parseDouble(matcher.group(4));
                        String status = matcher.group(5);

                        // Only add payments relevant to the current user to the table
                        if (fileUsername.equals(currentUsername)) {
                            model.addRow(new Object[]{paymentMethodFromFile, date, amount, status});
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("MainPayment: Skipping line in payment.txt due to invalid amount format: " + line + " - " + e.getMessage());
                    }
                } else {
                    // Added a clearer message for format issues in the detailed payment file
                    System.err.println("MainPayment: Skipping line in payment.txt due to unexpected format (expected 5 comma-separated fields: username,paymentMethod,paymentDate,amount,status): " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("MainPayment: Error: payment.txt not found. " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Detailed payment file not found!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            System.err.println("MainPayment: Error reading payment.txt. " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error reading detailed payment file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void updatePriceLabel() {
        priceLabel.setText(String.format("RM %.2f", totalUnpaidAmount));
    }

    // Method to update the dateLabel
    private void updateDateLabel() {
        dateLabel.setText(dueDateForDisplay);
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SMainPayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SMainPayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SMainPayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SMainPayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Old MainFrame references removed">
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // When running MainPayment directly for testing, it won't have a ProfilePageGUI context.
                // For actual application flow, ProfilePageGUI will call the parameterized constructor.
                // You can test with a dummy student here:
                SStudent testSStudent = new SStudent(
                        "S001",         // studentId
                        "Test User",    // name
                        "123456-07-8901", // icPassport (example value)
                        "test@example.com", // email
                        "012-3456789",  // contactNumber
                        "123 Test St",  // address
                        "Form 4",       // formLevel
                        "Math,Science,English", // subjects
                        "January"       // enrollmentMonth
                        // Removed "pass" and "role" as they are not in the 9-arg constructor
                );
                new SMainPayment(null, testSStudent).setVisible(true); // Pass null for ProfilePageGUI if not launching from it
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JLabel dateLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JButton returnButton;
    private javax.swing.JTable viewPaymentTable;
    // End of variables declaration
}