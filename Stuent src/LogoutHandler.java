import javax.swing.*;

class LogoutHandler {
    /**
     * Performs the logout operation.
     * This method is static, allowing it to be called directly on the class.
     *
     * @param currentFrame The JFrame that needs to be disposed (e.g., the dashboard).
     * @param loginPage The instance of loginPageTest to show after logout.
     */
    public static void logout(JFrame currentFrame, loginPageTest loginPage) {
        currentFrame.dispose(); // Close the current frame (e.g., AdminDashboard)
        loginPage.setVisible(true); // <--- HIGHLIGHT 1: Make the login page visible again
        loginPage.clearFields(); // <--- HIGHLIGHT 2: Clear login fields and reset attempts for next login
    }
}