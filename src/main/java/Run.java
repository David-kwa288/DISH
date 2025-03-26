package main.java;

import javax.swing.SwingUtilities;
import main.java.view.CelebrityGUI;
import main.java.controller.CelebrityController;

public class Run {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CelebrityController controller = new CelebrityController(null);
            CelebrityGUI gui = new CelebrityGUI(controller);
            controller.setGui(gui);
            gui.show();
        });
    }
}