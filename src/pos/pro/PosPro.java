package pos.pro;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class PosPro {

    public static void main(String[] args) {

        // 1. Set a modern Look and Feel (Nimbus)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed to initialize Nimbus Look and Feel. Using default.");
        }

        // 2. Launch the Main Application Screen safely
        SwingUtilities.invokeLater(() -> {
            Home homeScreen = new Home();
            homeScreen.setVisible(true);
        });
    }
}