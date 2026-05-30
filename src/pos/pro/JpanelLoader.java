package pos.pro;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class JpanelLoader {

    public void jPanelLoader(JPanel Main, JPanel setPanel) {
        // 1. Remove whatever panel is currently visible
        Main.removeAll();

        // 2. Use a clean BorderLayout for the main container
        Main.setLayout(new BorderLayout());

        // 3. Add the new panel to the center so it fills all available space
        Main.add(setPanel, BorderLayout.CENTER);

        // 4. Tell Java Swing to visually refresh the screen
        Main.revalidate();
        Main.repaint();
    }
}