//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//
//public class AdminDashboard extends JFrame implements ActionListener {
//
//    JButton RegisterTutor, DeleteTutor, AssignTutorToLevelSubject, DisplayTutor;
//    JButton RegisterReceptionist, DeleteReceptionist; // Removed AddMonthlyIncome as it was not used
//    JButton ViewMonthlyIncomeReport, UpdateMyProfile, Logout;
//
//    private loginPageTest loginPage; // Store the reference to the login page
//
//    public AdminDashboard(loginPageTest loginPage) {
//        this.loginPage = loginPage; // Initialize the loginPage reference
//
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setTitle("Java Admin Dashboard");
//        this.setSize(500, 600);
//        this.setLocationRelativeTo(null);
//        this.setLayout(null);
//        this.getContentPane().setBackground(new Color(23, 23, 23)); // Darker background
//        this.setVisible(true);
//
//        // --- Initialize Buttons ---
//        RegisterTutor = createButton("Register Tutor", 125, 35);
//        DeleteTutor = createButton("Delete Tutor", 125, 85);
//        AssignTutorToLevelSubject = createButton("Assign Tutor to Level/Subject", 125, 135);
//        DisplayTutor = createButton("Display Tutor", 125, 185);
//        RegisterReceptionist = createButton("Register Receptionist", 125, 235);
//        DeleteReceptionist = createButton("Delete Receptionist", 125, 285);
//        ViewMonthlyIncomeReport = createButton("View Monthly Income Report", 125, 385);
//        UpdateMyProfile = createButton("Update My Profile", 125, 435);
//        Logout = createButton("Logout", 125, 485);
//
//        // --- Add Buttons to Frame ---
//        this.add(RegisterTutor);
//        this.add(DeleteTutor);
//        this.add(AssignTutorToLevelSubject);
//        this.add(DisplayTutor);
//        this.add(RegisterReceptionist);
//        this.add(DeleteReceptionist);
//        this.add(ViewMonthlyIncomeReport);
//        this.add(UpdateMyProfile);
//        this.add(Logout);
//    }
//
//    // Helper method to create and style buttons
//    private JButton createButton(String text, int x, int y) {
//        JButton button = new JButton(text);
//        button.setBounds(x, y, 250, 40);
//        button.addActionListener(this);
//        button.setBackground(new Color(0, 150, 0)); // Greenish background
//        button.setForeground(Color.WHITE); // White text
//        button.setFont(new Font("Arial", Font.BOLD, 14));
//        button.setFocusPainted(false); // Remove focus border
//        return button;
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == RegisterTutor) {
//            TutorManagementGUI.showRegisterTutorDialog(this);
//        }
//        else if (e.getSource() == DeleteTutor) {
//            TutorManagementGUI.showDeleteTutorDialog(this);
//        }
//        else if (e.getSource() == Logout) {
//            // HIGHLIGHT 1: Add a null check before attempting to log out.
//            // If loginPage is null, it means the reference was not correctly established
//            // during AdminDashboard's creation.
//            if (this.loginPage != null) {
//                // Correctly call the static logout method, passing itself and the loginPage instance
//                LogoutHandler.logout(this, this.loginPage);
//            } else {
//                // HIGHLIGHT 2: Inform the user and log an error if loginPage reference is missing.
//                JOptionPane.showMessageDialog(this,
//                        "Error: The login page reference is missing. Cannot log out.",
//                        "Logout Error",
//                        JOptionPane.ERROR_MESSAGE);
//                System.err.println("AdminDashboard: loginPage reference is null during logout attempt.");
//            }
//        }
//        else if (e.getSource() == AssignTutorToLevelSubject) {
//            return;
//        }
//        else if (e.getSource() == RegisterReceptionist) {
//            ReceptionistManagementGUI.showRegisterReceptionistDialog(this);
//        }
//        else if (e.getSource() == DeleteReceptionist) {
//            ReceptionistManagementGUI.showDeleteReceptionistDialog(this);
//        }
//        else if (e.getSource() == ViewMonthlyIncomeReport) {
//            return;
//        }
//        else if (e.getSource() == UpdateMyProfile) {
//            return;
//        }
//        else if (e.getSource()==DisplayTutor) {
//            TutorManagementGUI.showAllTutorInfoDialog(this);
//        }
//        else if (e.getSource()==Displa) {
//            TutorManagementGUI.showAllTutorInfoDialog(this);
//        }
//
//
//
//    }
//}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AdminDashboard extends JFrame implements ActionListener {

    JButton RegisterTutor, DeleteTutor, AssignTutorToLevelSubject, DisplayTutor;
    JButton RegisterReceptionist, DeleteReceptionist, DisplayReceptionist; // Added DisplayReceptionist
    JButton ViewMonthlyIncomeReport, UpdateMyProfile, Logout;

    private loginPageTest loginPage; // Store the reference to the login page

    public AdminDashboard(loginPageTest loginPage) {
        this.loginPage = loginPage; // Initialize the loginPage reference

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Java Admin Dashboard");
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.getContentPane().setBackground(new Color(23, 23, 23)); // Darker background
        this.setVisible(true);

        // --- Initialize Buttons ---
        RegisterTutor = createButton("Register Tutor", 125, 35);
        DeleteTutor = createButton("Delete Tutor", 125, 85);
        AssignTutorToLevelSubject = createButton("Assign Tutor to Level/Subject", 125, 135);
        DisplayTutor = createButton("Display Tutor", 125, 185);

        RegisterReceptionist = createButton("Register Receptionist", 125, 235);
        DeleteReceptionist = createButton("Delete Receptionist", 125, 285);
        DisplayReceptionist = createButton("Display Receptionist", 125, 335); // New button for displaying receptionists

        ViewMonthlyIncomeReport = createButton("View Monthly Income Report", 125, 385);
        UpdateMyProfile = createButton("Update My Profile", 125, 435);
        Logout = createButton("Logout", 125, 485);

        // --- Add Buttons to Frame ---
        this.add(RegisterTutor);
        this.add(DeleteTutor);
        this.add(AssignTutorToLevelSubject);
        this.add(DisplayTutor);
        this.add(RegisterReceptionist);
        this.add(DeleteReceptionist);
        this.add(DisplayReceptionist); // Add the new button
        this.add(ViewMonthlyIncomeReport);
        this.add(UpdateMyProfile);
        this.add(Logout);
    }

    // Helper method to create and style buttons
    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 250, 40);
        button.addActionListener(this);
        button.setBackground(new Color(0, 150, 0)); // Greenish background
        button.setForeground(Color.WHITE); // White text
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false); // Remove focus border
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == RegisterTutor) {
            TutorManagementGUI.showRegisterTutorDialog(this);
        }
        else if (e.getSource() == DeleteTutor) {
            TutorManagementGUI.showDeleteTutorDialog(this);
        }
        else if (e.getSource() == DisplayTutor) { // Added action for Display Tutor
            TutorManagementGUI.showAllTutorInfoDialog(this);
        }
        else if (e.getSource() == RegisterReceptionist) { // New action for Register Receptionist
            ReceptionistManagementGUI.showRegisterReceptionistDialog(this);
        }
        else if (e.getSource() == DeleteReceptionist) { // New action for Delete Receptionist
            ReceptionistManagementGUI.showDeleteReceptionistDialog(this);
        }
        else if (e.getSource() == DisplayReceptionist) { // New action for Display Receptionist
            ReceptionistManagementGUI.showAllReceptionistInfoDialog(this);
        }
        else if (e.getSource() == AssignTutorToLevelSubject){
            TutorManagementGUI.showAssignTutorDialog(this);
        }
        else if (e.getSource() == UpdateMyProfile){

        }
        else if (e.getSource() == Logout) {
            // Add a null check before attempting to log out.
            if (this.loginPage != null) {
                // Correctly call the static logout method, passing itself and the loginPage instance
                LogoutHandler.logout(this, this.loginPage);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error: The login page reference is missing. Cannot log out.",
                        "Logout Error",
                        JOptionPane.ERROR_MESSAGE);
                System.err.println("AdminDashboard: loginPage reference is null during logout attempt.");
            }
        }
    }
}