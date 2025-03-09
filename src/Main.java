import javax.swing.SwingUtilities;
import view.CelebrityGUI;
import controller.CelebrityController;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CelebrityController controller = new CelebrityController(null);
            CelebrityGUI gui = new CelebrityGUI(controller);
            controller.setGui(gui);
            gui.show();
        });
    }
}