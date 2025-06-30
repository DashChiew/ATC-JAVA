
public class Main {
    public static void main(String[] args) {
        // Ensure that Swing operations are performed on the Event Dispatch Thread (EDT)
        javax.swing.SwingUtilities.invokeLater(() -> {
            new loginPageTest();
        });
    }
}
