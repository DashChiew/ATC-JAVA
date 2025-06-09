//import javax.swing.*;
//import java.awt.*;
//// Make sure you have the RoundImageLabel class defined (either here or in a separate file)
//
//public class setting {
//
//    // --- JLabels for field names (these remain the same) ---
//    JLabel nameLabel = new JLabel("Name:");
//    JLabel usernameLabel = new JLabel("Username:");
//    JLabel passwordLabel = new JLabel("Password:");
//    JLabel icNumberLabel = new JLabel("IC Number:");
//    JLabel emailLabel = new JLabel("Email:");
//    JLabel contactNumberLabel = new JLabel("Contact Number:");
//    JLabel addressLabel = new JLabel("Address:");
//    JLabel levelLabel = new JLabel("Level:");
//    JLabel subjectLabel = new JLabel("Subject:");
//    JLabel branchLabel = new JLabel("Branch:");
//    JLabel intakeCodeLabel = new JLabel("Intake Code:");
//
//    // --- JTextFields for editable fields ---
//    JTextField nameField = new JTextField(20);
//    JTextField usernameField = new JTextField(20);
//    JTextField passwordField = new JTextField(20); // Consider JPasswordField for actual passwords
//    JTextField icNumberField = new JTextField(20);
//    JTextField emailField = new JTextField(20);
//    JTextField contactNumberField = new JTextField(20);
//    JTextField addressField = new JTextField(20);
//
//    // --- JLabels for display-only fields (Replaced JTextFields here!) ---
//    JLabel levelValueLabel = new JLabel("Level 10"); // Example static value
//    JLabel subjectValueLabel = new JLabel("Computer Science"); // Example static value
//    JLabel branchValueLabel = new JLabel("Main Campus"); // Example static value
//    JLabel intakeCodeValueLabel = new JLabel("2025-JAN-A"); // Example static value
//
//    roundImageLabel profilePictureLabel;
//
//    public setting() {
//        JFrame frame = new JFrame();
//        frame.setTitle("Setting");
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setSize(500, 700);
//        frame.setLocationRelativeTo(null);
//        frame.setLayout(new BorderLayout());
//
//        // --- Profile Picture Section (NORTH) ---
//        // IMPORTANT: Replace "path/to/your/profile.jpg" with your actual image path
//        profilePictureLabel = new roundImageLabel("fei.png", 160); // Assuming fei.png is in the project root
//
//        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
//        headerPanel.add(profilePictureLabel);
//
//        frame.add(headerPanel, BorderLayout.NORTH);
//
//        // --- Form Section (CENTER) ---
//        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
//        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        // Editable Fields
//        panel.add(nameLabel);
//        panel.add(nameField);
//
//        panel.add(usernameLabel);
//        panel.add(usernameField);
//
//        panel.add(passwordLabel);
//        panel.add(passwordField);
//
//        panel.add(icNumberLabel);
//        panel.add(icNumberField);
//
//        panel.add(emailLabel);
//        panel.add(emailField);
//
//        panel.add(contactNumberLabel);
//        panel.add(contactNumberField);
//
//        panel.add(addressLabel);
//        panel.add(addressField);
//
//        // Display-Only Fields (Now using JLabels for values)
//        panel.add(levelLabel);
//        panel.add(levelValueLabel); // Added JLabel for value
//
//        panel.add(subjectLabel);
//        panel.add(subjectValueLabel); // Added JLabel for value
//
//        panel.add(branchLabel);
//        panel.add(branchValueLabel); // Added JLabel for value
//
//        panel.add(intakeCodeLabel);
//        panel.add(intakeCodeValueLabel); // Added JLabel for value
//
//        JPanel outerAlignmentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        outerAlignmentPanel.add(panel);
//
//        frame.add(outerAlignmentPanel, BorderLayout.CENTER);
//
//        // --- Button Section (SOUTH) ---
//        JButton saveButton = new JButton("Save Profile");
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        buttonPanel.add(saveButton);
//
//        frame.add(buttonPanel, BorderLayout.SOUTH);
//
//        frame.setVisible(true);
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new setting();
//        });
//    }
//}
//
//// Remember to include your RoundImageLabel class definition here or import it.
//// (Provided in previous responses)

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// Re-including RoundImageLabel for completeness.
// You can keep this in a separate file if preferred.
class RoundImageLabel extends JLabel {
    private BufferedImage image;

    public RoundImageLabel(String imagePath, int size) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            if (originalImage != null) {
                this.image = createRoundImage(originalImage, size);
            }
        } catch (IOException e) {
            e.printStackTrace();
            setText("Image Error");
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        setPreferredSize(new Dimension(size, size));
    }

    private BufferedImage createRoundImage(BufferedImage originalImage, int size) {
        BufferedImage scaledImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int imgWidth = originalImage.getWidth();
        int imgHeight = originalImage.getHeight();
        int drawX = 0;
        int drawY = 0;
        int drawSize = size;

        if (imgWidth > imgHeight) {
            drawSize = imgHeight;
            drawX = (imgWidth - imgHeight) / 2;
        } else if (imgHeight > imgWidth) {
            drawSize = imgWidth;
            drawY = (imgHeight - imgWidth) / 2;
        }

        g2d.drawImage(originalImage, 0, 0, size, size, drawX, drawY, drawX + drawSize, drawY + drawSize, null);

        g2d.setComposite(AlphaComposite.Clear);
        g2d.fill(new Rectangle(0, 0, size, size));
        g2d.setComposite(AlphaComposite.SrcOver);

        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, size, size);
        g2d.setClip(circle);

        g2d.drawImage(originalImage, 0, 0, size, size, drawX, drawY, drawX + drawSize, drawY + drawSize, null);

        g2d.dispose();
        return scaledImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } else {
            super.paintComponent(g);
        }
    }
}


public class Setting {

    // --- JLabels for field names ---
    JLabel nameLabel = new JLabel("Name:");
    JLabel usernameLabel = new JLabel("Username:");
    JLabel passwordLabel = new JLabel("Password:"); // Use JPasswordField for actual passwords
    JLabel icNumberLabel = new JLabel("IC Number:");
    JLabel emailLabel = new JLabel("Email:");
    JLabel contactNumberLabel = new JLabel("Contact Number:");
    JLabel addressLabel = new JLabel("Address:");
    JLabel levelLabel = new JLabel("Level:");
    JLabel subjectLabel = new JLabel("Subject:");
    JLabel branchLabel = new JLabel("Branch:");
    JLabel intakeCodeLabel = new JLabel("Intake Code:");

    // --- JTextFields for editable fields (pre-filled with data) ---
    JLabel nameValueField = new JLabel("Jony");
    JTextField usernameField = new JTextField("jony.doe", 20); // Pre-fill username
    JTextField passwordField = new JTextField("mypassword123", 20); // Pre-fill password (consider JPasswordField)
    JTextField icNumberField = new JTextField("901231-14-5678", 20); // Pre-fill IC
    JLabel emailValueField = new JLabel("jony.doe@example.com");
    JTextField contactNumberField = new JTextField("012-3456789", 20); // Pre-fill contact no.
    JTextField addressField = new JTextField("123, Jalan ABC, Taman DEF, 50000 Kuala Lumpur", 20); // Pre-fill address

    // --- JLabels for display-only fields (static data) ---
    JLabel levelValueLabel = new JLabel("Level 10"); // Example static value
    JLabel subjectValueLabel = new JLabel("Computer Science"); // Example static value
    JLabel branchValueLabel = new JLabel("Main Campus"); // Example static value
    JLabel intakeCodeValueLabel = new JLabel("2025-JAN-A"); // Example static value

    RoundImageLabel profilePictureLabel;

    public Setting() {
        JFrame frame = new JFrame();
        frame.setTitle("User Profile Settings"); // Changed title for clarity
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 700);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // --- Profile Picture Section (NORTH) ---
        // Replace "fei.png" with the actual path to your profile image
        profilePictureLabel = new RoundImageLabel("fei.png", 160);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerPanel.add(profilePictureLabel);

        frame.add(headerPanel, BorderLayout.NORTH);

        // --- Form Section (CENTER) ---
        JPanel innerFormPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        innerFormPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Editable Fields - Labels and their pre-filled JTextFields
        innerFormPanel.add(nameLabel);
        innerFormPanel.add(nameValueField);

        innerFormPanel.add(usernameLabel);
        innerFormPanel.add(usernameField);

        innerFormPanel.add(passwordLabel);
        innerFormPanel.add(passwordField);

        innerFormPanel.add(icNumberLabel);
        innerFormPanel.add(icNumberField);

        innerFormPanel.add(emailLabel);
        innerFormPanel.add(emailValueField);

        innerFormPanel.add(contactNumberLabel);
        innerFormPanel.add(contactNumberField);

        innerFormPanel.add(addressLabel);
        innerFormPanel.add(addressField);

        // Display-Only Fields - Labels and their static JLabels
        innerFormPanel.add(levelLabel);
        innerFormPanel.add(levelValueLabel);

        innerFormPanel.add(subjectLabel);
        innerFormPanel.add(subjectValueLabel);

        innerFormPanel.add(branchLabel);
        innerFormPanel.add(branchValueLabel);

        innerFormPanel.add(intakeCodeLabel);
        innerFormPanel.add(intakeCodeValueLabel);

        JPanel outerAlignmentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        outerAlignmentPanel.add(innerFormPanel);

        frame.add(outerAlignmentPanel, BorderLayout.CENTER);

        // --- Button Section (SOUTH) ---
        JButton saveButton = new JButton("Save Profile");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // --- Add ActionListener to the Save Button ---
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This code runs when the Save Profile button is clicked
                System.out.println("--- Profile Data Saved (for demonstration) ---");
                System.out.println("Name: " + nameValueField.getText());
                System.out.println("Username: " + usernameField.getText());
                System.out.println("Password: " + passwordField.getText()); // Be careful with passwords in real apps!
                System.out.println("IC Number: " + icNumberField.getText());
                System.out.println("Email: " + emailValueField.getText());
                System.out.println("Contact No: " + contactNumberField.getText());
                System.out.println("Address: " + addressField.getText());
                // Static fields don't change, but you could still retrieve their initial values
                System.out.println("Level: " + levelValueLabel.getText());
                System.out.println("Subject: " + subjectValueLabel.getText());
                System.out.println("Branch: " + branchValueLabel.getText());
                System.out.println("Intake Code: " + intakeCodeValueLabel.getText());
                System.out.println("------------------------------------------");

                // In a real application, you would now:
                // 1. Update a user object with these new values.
                // 2. Persist the changes (e.g., save to a database, file, or send to a server).
                // 3. Provide feedback to the user (e.g., JOptionPane.showMessageDialog("Profile saved successfully!"))
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Setting();
        });
    }
}