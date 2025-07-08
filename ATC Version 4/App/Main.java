package App; // Assuming Main.java is in the 'app' package

import common.auth.MainLoginPageTest;

public class Main {
    public static void main(String[] args) {
        // Ensure that Swing operations are performed on the Event Dispatch Thread (EDT)
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainLoginPageTest loginPage = new MainLoginPageTest();
        });
    }
}