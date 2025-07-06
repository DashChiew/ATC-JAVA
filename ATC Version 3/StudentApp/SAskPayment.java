package StudentApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.YearMonth;
import java.time.LocalDate; // Import LocalDate for current date
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import StudentApp.SMainPayment;

public class SAskPayment extends JFrame {

    private SMainPayment SMainPaymentFrame; // Change type to MainPayment
    private int amountToPay; // Store the amount to be paid

    // ClassIn Style Color Palette
    private static final Color CLASSIN_PRIMARY_BLUE = new Color(59, 130, 246);
    private static final Color CLASSIN_ACCENT_BLUE = new Color(37, 99, 235);
    private static final Color CLASSIN_BACKGROUND_WHITE = new Color(255, 255, 255);
    private static final Color CLASSIN_LIGHT_GRAY = new Color(240, 240, 240);
    private static final Color CLASSIN_DARK_TEXT = new Color(51, 51, 51);
    private static final Color CLASSIN_LIGHT_BORDER = new Color(200, 200, 200);

    // Fonts - INCREASED FONT SIZES
    private final Font labelFont = new Font("Segoe UI", Font.PLAIN, 18);
    private final Font valueFont = new Font("Segoe UI", Font.BOLD, 18);
    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 26);

    // Components
    private JLabel totalAmountLabel;
    private JLabel totalAmountValue;

    private JRadioButton creditCardRadio;
    private JRadioButton onlineBankingRadio;
    private JRadioButton touchNGoRadio;

    private JPanel cardDetailsPanel;
    private JTextField cardNumberField;
    private JTextField expiryDateField;
    private JPasswordField cvvField;

    // Components for DuitNow
    private JTextField duitnowAccountNumberField;
    private JPanel duitnowDetailsPanel;

    // Components for Touch 'n Go
    private JTextField tngPhoneNumberField;
    private JPasswordField tngPasswordField; // This is the field for the password
    private JPanel tngDetailsPanel;

    private JButton payButton;
    private JButton returnButton; // NEW: Declare the return button

    // MODIFIED CONSTRUCTOR: Accepts MainPayment as parameter
    public SAskPayment(SMainPayment SMainPaymentFrame, double amount) { // Change parameter type
        System.out.println("Payment: Parameterized constructor initialized with amount: " + amount);
        this.SMainPaymentFrame = SMainPaymentFrame; // Assign to new field
        this.amountToPay = (int) amount;
        initializeUI();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Payment: Window Closed event received.");
                if (SAskPayment.this.SMainPaymentFrame != null) {
                    System.out.println("Payment: Making MainPayment visible after Payment disposal.");
                    SAskPayment.this.SMainPaymentFrame.setVisible(true);
                } else {
                    System.out.println("Payment: MainPayment reference is null, cannot make it visible.");
                }
            }
        });
    }


    // Method to encapsulate UI initialization
    private void initializeUI() {
        setTitle("Make Payment");
        setSize(525, 690);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 20));
        getContentPane().setBackground(CLASSIN_BACKGROUND_WHITE);

        // --- Header Panel with Image and Return Button (NORTH) ---
        JPanel headerPanel = new JPanel(new BorderLayout()); // Change to BorderLayout
        headerPanel.setBackground(CLASSIN_BACKGROUND_WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20)); // Adjusted padding

        // NEW: Return Button
        returnButton = new JButton("Return");
        returnButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        returnButton.setFocusPainted(false); // Remove focus border
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnButtonActionPerformed(e);
            }
        });
        headerPanel.add(returnButton, BorderLayout.WEST); // Add to the left (WEST)

        JLabel paymentTitle = new JLabel("Choose Payment Method");
        paymentTitle.setFont(headerFont);
        paymentTitle.setForeground(CLASSIN_DARK_TEXT);
        paymentTitle.setHorizontalAlignment(SwingConstants.CENTER); // Center the title
        headerPanel.add(paymentTitle, BorderLayout.CENTER); // Add to the center


        this.add(headerPanel, BorderLayout.NORTH);


        // --- Main Payment Form Panel (CENTER) ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(CLASSIN_BACKGROUND_WHITE);
        mainPanel.setBorder(new EmptyBorder(0, 40, 0, 40));

        // Section: Payment Method Selection
        JPanel paymentMethodPanel = new JPanel(new GridLayout(0, 1, 0, 12));
        paymentMethodPanel.setBackground(CLASSIN_BACKGROUND_WHITE);
        paymentMethodPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(CLASSIN_LIGHT_BORDER, 1),
                "Select Method", 0, 0, labelFont, CLASSIN_DARK_TEXT
        ));

        ButtonGroup paymentMethodGroup = new ButtonGroup();
        creditCardRadio = createRadioButton("Credit Card", paymentMethodGroup);
        onlineBankingRadio = createRadioButton("Online Banking", paymentMethodGroup);
        touchNGoRadio = createRadioButton("Touch 'n Go eWallet", paymentMethodGroup);

        paymentMethodPanel.add(creditCardRadio);
        paymentMethodPanel.add(onlineBankingRadio);
        paymentMethodPanel.add(touchNGoRadio);

        mainPanel.add(paymentMethodPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        // Section: Card Details Input (initially hidden)
        cardDetailsPanel = new JPanel(new GridLayout(0, 1, 0, 15));
        cardDetailsPanel.setBackground(CLASSIN_BACKGROUND_WHITE);
        cardDetailsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(CLASSIN_LIGHT_BORDER, 1),
                "Card Details", 0, 0, labelFont, CLASSIN_DARK_TEXT
        ));

        cardNumberField = createStyledTextField("Card Number");
        expiryDateField = createStyledTextField("MM/YY");
        cvvField = createStyledPasswordField("CVV");

        cardDetailsPanel.add(createLabelAndFieldPanel("Card Number:", cardNumberField));
        cardDetailsPanel.add(createLabelAndFieldPanel("Expiry Date:", expiryDateField));
        cardDetailsPanel.add(createLabelAndFieldPanel("CVV:", cvvField));
        cardDetailsPanel.setVisible(false);

        mainPanel.add(cardDetailsPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        // Section: DuitNow Account Input (initially hidden)
        duitnowDetailsPanel = new JPanel(new GridLayout(0, 1, 0, 15));
        duitnowDetailsPanel.setBackground(CLASSIN_BACKGROUND_WHITE);
        duitnowDetailsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(CLASSIN_LIGHT_BORDER, 1),
                "DuitNow Details", 0, 0, labelFont, CLASSIN_DARK_TEXT
        ));

        duitnowAccountNumberField = createStyledTextField("DuitNow Account Number");
        duitnowDetailsPanel.add(createLabelAndFieldPanel("Account Number:", duitnowAccountNumberField));
        duitnowDetailsPanel.setVisible(false);

        mainPanel.add(duitnowDetailsPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        // NEW Section: Touch 'n Go eWallet Details Input (initially hidden)
        tngDetailsPanel = new JPanel(new GridLayout(0, 1, 0, 15));
        tngDetailsPanel.setBackground(CLASSIN_BACKGROUND_WHITE);
        tngDetailsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(CLASSIN_LIGHT_BORDER, 1),
                "Touch 'n Go eWallet Details", 0, 0, labelFont, CLASSIN_DARK_TEXT
        ));

        tngPhoneNumberField = createStyledTextField("Phone Number");
        tngDetailsPanel.add(createLabelAndFieldPanel("Phone Number:", tngPhoneNumberField));

        // Panel for Password and Show/Hide Checkbox
        JPanel tngPasswordPanel = new JPanel(new BorderLayout(8, 0)); // For label and inner panel
        tngPasswordPanel.setBackground(CLASSIN_BACKGROUND_WHITE);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(CLASSIN_DARK_TEXT);
        tngPasswordPanel.add(passwordLabel, BorderLayout.WEST);

        JPanel passwordInputFieldPanel = new JPanel(new BorderLayout(5, 0)); // For password field and checkbox
        passwordInputFieldPanel.setBackground(CLASSIN_BACKGROUND_WHITE);
        tngPasswordField = createStyledPasswordField("6-Digit Password"); // Call without initial text setting
        passwordInputFieldPanel.add(tngPasswordField, BorderLayout.CENTER);

        JCheckBox showPasswordCheckBox = new JCheckBox("Show");
        showPasswordCheckBox.setBackground(CLASSIN_BACKGROUND_WHITE);
        showPasswordCheckBox.setFont(labelFont);
        showPasswordCheckBox.setForeground(CLASSIN_DARK_TEXT);
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                tngPasswordField.setEchoChar((char) 0);
            } else {
                tngPasswordField.setEchoChar('*');
            }
        });
        passwordInputFieldPanel.add(showPasswordCheckBox, BorderLayout.EAST);
        tngPasswordPanel.add(passwordInputFieldPanel, BorderLayout.CENTER);

        tngDetailsPanel.add(tngPasswordPanel);
        tngDetailsPanel.setVisible(false); // Initially hidden

        mainPanel.add(tngDetailsPanel);
        mainPanel.add(Box.createVerticalStrut(30));


        // Action listeners for radio buttons to show/hide relevant panels
        creditCardRadio.addActionListener(e -> toggleDetailsPanel(cardDetailsPanel));
        onlineBankingRadio.addActionListener(e -> toggleDetailsPanel(duitnowDetailsPanel));
        touchNGoRadio.addActionListener(e -> toggleDetailsPanel(tngDetailsPanel));

        // Section: Total Amount Display
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        totalPanel.setBackground(CLASSIN_BACKGROUND_WHITE);
        totalAmountLabel = new JLabel("Total Amount to Pay:");
        totalAmountLabel.setFont(valueFont);
        totalAmountLabel.setForeground(CLASSIN_DARK_TEXT);
        totalAmountValue = new JLabel(String.format("RM %.2f", (double) amountToPay));
        totalAmountValue.setFont(headerFont); // Make total amount stand out
        totalAmountValue.setForeground(CLASSIN_PRIMARY_BLUE);

        totalPanel.add(totalAmountLabel);
        totalPanel.add(totalAmountValue);
        mainPanel.add(totalPanel);
        mainPanel.add(Box.createVerticalStrut(30));


        // Pay Button
        payButton = new JButton("Confirm Payment");
        payButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        payButton.setBackground(CLASSIN_PRIMARY_BLUE);
        payButton.setForeground(CLASSIN_DARK_TEXT);
        payButton.setFocusPainted(false);
        payButton.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        payButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmPayment();
            }
        });

        // Add a panel for the button to center it
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(CLASSIN_BACKGROUND_WHITE);
        buttonPanel.add(payButton);
        mainPanel.add(buttonPanel);


        this.add(mainPanel, BorderLayout.CENTER);
    }

    private JRadioButton createRadioButton(String text, ButtonGroup group) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setFont(labelFont);
        radioButton.setBackground(CLASSIN_BACKGROUND_WHITE);
        radioButton.setForeground(CLASSIN_DARK_TEXT);
        group.add(radioButton);
        return radioButton;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField textField = new JTextField(20);
        textField.setFont(labelFont);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CLASSIN_LIGHT_BORDER, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        textField.putClientProperty("JTextField.placeholderText", placeholder); // Placeholder text
        return textField;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(labelFont);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CLASSIN_LIGHT_BORDER, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        passwordField.putClientProperty("JPasswordField.placeholderText", placeholder); // Placeholder text
        return passwordField;
    }


    private JPanel createLabelAndFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(CLASSIN_BACKGROUND_WHITE);
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setForeground(CLASSIN_DARK_TEXT);
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void toggleDetailsPanel(JPanel panelToShow) {
        cardDetailsPanel.setVisible(false);
        duitnowDetailsPanel.setVisible(false);
        tngDetailsPanel.setVisible(false); // Hide the new TNG panel

        if (panelToShow != null) {
            panelToShow.setVisible(true);
        }
        revalidate();
        repaint();
    }

    private void confirmPayment() {
        String selectedMethod = "";

        if (creditCardRadio.isSelected()) {
            selectedMethod = "Credit Card";
            // Basic validation for Credit Card fields
            if (cardNumberField.getText().isEmpty() || expiryDateField.getText().isEmpty() || new String(cvvField.getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all credit card details.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Basic expiry date format validation (MM/YY)
            if (!isValidExpiryDate(expiryDateField.getText())) {
                JOptionPane.showMessageDialog(this, "Invalid expiry date format. Please use MM/YY (e.g., 12/25) and ensure it's not in the past.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

        } else if (onlineBankingRadio.isSelected()) {
            selectedMethod = "Online Banking";
            // Basic validation for DuitNow account
            if (duitnowAccountNumberField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the DuitNow Account Number.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else if (touchNGoRadio.isSelected()) {
            selectedMethod = "Touch 'n Go eWallet";
            // Basic validation for Touch 'n Go fields
            if (tngPhoneNumberField.getText().isEmpty() || new String(tngPasswordField.getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all Touch 'n Go details.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a payment method.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- All validations passed ---

        // Extract payment details
        // Get current date for payment date
        String paymentMethod = selectedMethod;
        String paymentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")); // Corrected pattern for full year
        double totalCollected = (double) amountToPay;
        String status = "Pending";

        // Show the payment confirmation dialog
        JOptionPane.showMessageDialog(this,
                "Payment Method: " + paymentMethod + "\n" +
                        "Total Amount: RM " + String.format("%.2f", totalCollected) + "\n" +
                        "Status: " + status,
                "Payment Confirmation", JOptionPane.INFORMATION_MESSAGE);

        System.out.println("Payment: Payment confirmation dialog closed. Attempting to return to MainPayment.");

        // Call the method in MainPayment to update the table and save to file
        if (SMainPaymentFrame != null) {
            SMainPaymentFrame.addPaymentEntry(paymentMethod, paymentDate, totalCollected, status);
        }

        // Close the current Payment frame
        this.dispose();
        System.out.println("Payment: Payment frame disposed.");
    }

    // NEW: Action method for the return button
    private void returnButtonActionPerformed(ActionEvent evt) {
        this.dispose(); // Close the current Payment frame
        if (SMainPaymentFrame != null) {
            SMainPaymentFrame.setVisible(true); // Make the MainPayment frame visible
        } else {
            System.out.println("Payment: MainPayment reference is null. Cannot return to it.");
            // Optionally, if running Payment directly for testing, you might want to exit:
            // System.exit(0);
        }
    }


    private boolean isValidExpiryDate(String expiryDate) {
        if (expiryDate == null || !expiryDate.matches("\\d{2}/\\d{2}")) {
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth yearMonth = YearMonth.parse(expiryDate, formatter);
            YearMonth currentYearMonth = YearMonth.now();
            return !yearMonth.isBefore(currentYearMonth); // Check if expiry date is not in the past
        } catch (DateTimeParseException e) {
            return false;
        }
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // For direct testing, pass null for MainPayment and an amount
        SwingUtilities.invokeLater(() -> new SAskPayment(null, 230).setVisible(true));
    }
}
