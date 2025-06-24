import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder; // Added for EmptyBorder
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// Removed unused imports from setting.java that are not relevant to this form
// import java.awt.geom.Area;
// import java.awt.geom.Ellipse2D;
// import java.awt.geom.RoundRectangle2D;
// import java.awt.image.BufferedImage;
// import java.io.File;
// import java.io.IOException;
// import javax.imageio.ImageIO;

// Custom Rounded Border for TextFields and Buttons (Copied from setting.java)
class RoundedBorder extends AbstractBorder {
    private int radius;
    private Color borderColor;
    private int borderWidth;

    RoundedBorder(int radius, Color borderColor, int borderWidth) {
        this.radius = radius;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(borderWidth));
        g2d.draw(new java.awt.geom.RoundRectangle2D.Double(x + borderWidth / 2.0, y + borderWidth / 2.0,
                width - borderWidth, height - borderWidth, radius, radius));
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius + borderWidth, radius + borderWidth, radius + borderWidth, radius + borderWidth);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = radius + borderWidth;
        return insets;
    }
}

// RoundImageLabel and its related methods are not needed for this Subject Update Form,
// as it doesn't have a profile picture. Removing them to keep the code focused.

public class SubjectUpdateForm extends JFrame {

    // --- ClassIn Style Color Palette (Copied from setting.java) ---
    private static final Color CLASSIN_PRIMARY_BLUE = new Color(59, 130, 246); // A common vibrant blue
    private static final Color CLASSIN_ACCENT_BLUE = new Color(37, 99, 235); // A darker blue for pressed states
    private static final Color CLASSIN_BACKGROUND_WHITE = new Color(255, 255, 255);
    private static final Color CLASSIN_LIGHT_GRAY = new Color(240, 240, 240); // For text field backgrounds
    private static final Color CLASSIN_DARK_TEXT = new Color(51, 51, 51); // Dark gray for most text
    private static final Color CLASSIN_LIGHT_BORDER = new Color(200, 200, 200); // Light gray for borders

    // Declare components
    private JButton returnButton;
    private JLabel titleLabel;
    private JTextField subject1Field;
    private JTextField subject2Field;
    private JTextField subject3Field;
    private JButton updateButton;

    public SubjectUpdateForm() {
        // Set up the JFrame (main window)
        setTitle("Update Your Profile");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(525, 525); // Set the frame to 525 width and 525 height
        setLocationRelativeTo(null); // Center the window on the screen
        setResizable(false); // Make the window not resizable for consistent layout

        // Set the background color for the main frame's content pane
        getContentPane().setBackground(CLASSIN_BACKGROUND_WHITE); // Use ClassIn background color

        // Create the main content panel using BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(CLASSIN_BACKGROUND_WHITE); // Use ClassIn background color

        // --- Top Panel for Return Button ONLY ---
        JPanel topContainerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Left-aligned flow, with padding
        topContainerPanel.setOpaque(false);

        returnButton = new JButton("Return");
        // Apply ClassIn styling to the Return button
        returnButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Changed font
        returnButton.setBackground(CLASSIN_PRIMARY_BLUE); // ClassIn primary blue
        returnButton.setForeground(CLASSIN_DARK_TEXT); // White text
        returnButton.setBorder(new RoundedBorder(15, CLASSIN_PRIMARY_BLUE, 0)); // Rounded button, no visible border
        returnButton.setPreferredSize(new Dimension(100, 40));
        returnButton.setFocusPainted(false);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, "Return button clicked!");
            }
        });
        // Add hover effect for the return button
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

        // Add Title Label (now directly in fieldsContainerPanel, above subjects)
        titleLabel = new JLabel("Update Your Subjects");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Changed font
        titleLabel.setForeground(CLASSIN_DARK_TEXT); // Set text color
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        fieldsContainerPanel.add(titleLabel);
        fieldsContainerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Helper method to create and add a subject row (label + text field) to a panel.
        // This panel uses a BoxLayout for vertical stacking.
        // This helper method is slightly modified to return the JTextField reference
        // so it can be assigned to the class's member variables.
        this.subject1Field = addSubjectRow(fieldsContainerPanel, "Subject 1:", "BAHASA MELAYU");
        this.subject2Field = addSubjectRow(fieldsContainerPanel, "Subject 2:", "ENGLISH");
        this.subject3Field = addSubjectRow(fieldsContainerPanel, "Subject 3:", "CHINESE");

        mainPanel.add(fieldsContainerPanel, BorderLayout.CENTER);

        // --- Bottom Panel for Update Button ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        buttonPanel.setOpaque(false);
        updateButton = new JButton("Update Subjects");
        // Apply ClassIn styling to the Update button
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Changed font
        updateButton.setBackground(CLASSIN_PRIMARY_BLUE); // ClassIn primary blue
        updateButton.setForeground(CLASSIN_DARK_TEXT); // White text
        updateButton.setBorder(new RoundedBorder(15, CLASSIN_PRIMARY_BLUE, 0)); // Rounded button, no visible border
        updateButton.setPreferredSize(new Dimension(180, 45)); // Adjusted height
        updateButton.setFocusPainted(false);
        // Add hover effect for the update button
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

                System.out.println("Subject 1 updated to: " + updatedSubject1);
                System.out.println("Subject 2 updated to: " + updatedSubject2);
                System.out.println("Subject 3 updated to: " + updatedSubject3);

                JOptionPane.showMessageDialog(mainPanel, "Subjects updated successfully!", "Update Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonPanel.add(updateButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
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
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // ClassIn label font
        label.setForeground(CLASSIN_DARK_TEXT); // ClassIn text color
        rowPanel.add(label);

        JTextField textField = new JTextField(initialText, 20);
        // Apply ClassIn styling to the text field
        textField.setBackground(CLASSIN_LIGHT_GRAY); // Light gray background
        textField.setForeground(CLASSIN_DARK_TEXT); // Dark text color
        textField.setFont(new Font("Segoe UI", Font.BOLD, 14)); // ClassIn value font
        textField.setBorder(new RoundedBorder(8, CLASSIN_LIGHT_BORDER, 1)); // Rounded border
        textField.setMargin(new Insets(5, 10, 5, 10)); // Inner padding
        textField.setCaretColor(CLASSIN_PRIMARY_BLUE); // ClassIn-like caret color
        textField.setPreferredSize(new Dimension(200, 35)); // Slightly increased height for visual consistency
        rowPanel.add(textField);

        parentPanel.add(rowPanel);
        return textField; // Return the JTextField instance
    }

    // This helper is no longer strictly necessary because JTextField references are assigned directly.
    // Keeping it as a placeholder or if direct assignment is not always feasible.
    private String findTextFieldValue(JPanel container, String initialValue) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel row = (JPanel) comp;
                for (Component rowComp : row.getComponents()) {
                    if (rowComp instanceof JTextField) {
                        JTextField field = (JTextField) rowComp;
                        // In a real app, you'd give JTextFields unique names/variables or IDs
                        if (field.getText().equals(initialValue) || field.getPreferredSize().width == 200) {
                            return field.getText();
                        }
                    }
                }
            }
        }
        return "";
    }

    public static void main(String[] args) {
        // Use UIManager to set a consistent look and feel if desired,
        // though we are manually styling most components here.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new SubjectUpdateForm().setVisible(true);
        });
    }
}
