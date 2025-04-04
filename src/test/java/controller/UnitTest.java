package controller;

import controller.CelebrityController;
import model.Celebrity;
import view.CelebrityGUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTest {
    private CelebrityController controller;

    private static class DummyGUI extends CelebrityGUI {
        public DummyGUI(CelebrityController controller) {
            super(controller);
        }

        @Override
        public void showMainContent(String role) {
            System.out.println("Showing main content for role: " + role);
        }

        @Override
        public JPanel getCelebListPanel() {
            return new JPanel();
        }

        @Override
        public JFrame getFrame() {
            return new JFrame();
        }
    }

    @BeforeEach
    public void setUp() {
        controller = new CelebrityController(new DummyGUI(null));
    }

    @Test
    public void testValidateLoginSuccess() {
        controller.handleSignUp("admin", "admin123");
        assertDoesNotThrow(() -> controller.handleSignIn("admin", "admin123", false));
    }

    @Test
    public void testValidateLoginFailure() {
        Exception exception = assertThrows(Exception.class, () -> {
            controller.handleSignIn("invalidUser", "wrongPass", false);
        });
        assertNotNull(exception);
    }

    @Test
    public void testHandleSignUp() {
        assertDoesNotThrow(() -> controller.handleSignUp("newUser", "password1"));
    }

    @Test
    public void testInsertCelebrity() {
        controller.handleGuestSignIn();
        controller.insertCelebrity();
    }

    @Test
    public void testFavoritesAddRemove() {
        controller.handleGuestSignIn();
        Celebrity testCeleb = new Celebrity("Test Name", "Test Job", "Test Bio", "", List.of(), null);
        controller.addToFavorites(testCeleb);
        assertTrue(controller.getFavorites().contains(testCeleb));

        controller.removeFromFavorites(testCeleb);
        assertFalse(controller.getFavorites().contains(testCeleb));
    }

    @Test
    public void testFilterCelebrities() {
        Celebrity celeb = new Celebrity("Actor", "Oscar Winner", "Bio", "", List.of(), null);
        controller.handleGuestSignIn();
        controller.addToFavorites(celeb);
        controller.filterCelebrities("Actor", "Oscar Winner", "Bio");
        assertTrue(controller.getFavorites().contains(celeb));
    }

    @Test
    public void testUpdateCelebrity() {
        controller.handleSignIn("admin", "admin123", true);
        controller.insertCelebrity(); // insert a celeb first to update
        controller.updateCelebrity();
    }

    @Test
    public void testDeleteCelebrity() {
        controller.handleSignIn("admin", "admin123", true);
        controller.insertCelebrity(); // insert a celeb first to delete
        controller.deleteCelebrity();
    }
    @Test
    public void testImageIntegration() {
        Celebrity celeb = new Celebrity("Actor", "Profession", "Bio", "", List.of("http://example.com/image.jpg"), null);
        assertFalse(celeb.getImages().isEmpty());
        assertEquals("http://example.com/image.jpg", celeb.getImages().get(0));
    }
    @Test
    public void testImageDisplayFromURL() {
        String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/7a/LeBron_James_(51959977144)_(cropped2).jpg";
        BufferedImage image = null;
        try {
            image = ImageIO.read(new URL(imageUrl));
            // Show image in a dialog
            ImageIcon icon = new ImageIcon(image);
            JLabel label = new JLabel(icon);
            JOptionPane.showMessageDialog(null, label, "Loaded Image", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e) {
            fail("Image could not be loaded from URL: " + e.getMessage());
        }
        assertNotNull(image, "Image should be loaded successfully from the given URL");
    }

    @Test
    public void testImageResizingFromURL() {
        String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/7a/LeBron_James_(51959977144)_(cropped2).jpg"; // Intentional non-square
        try {
            ImageIcon icon = new ImageIcon(new URL(imageUrl));
            Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

            assertEquals(100, scaled.getWidth(null));
            assertEquals(100, scaled.getHeight(null));
        } catch (IOException e) {
            fail("Image could not be loaded or resized correctly: " + e.getMessage());
        }
    }
}