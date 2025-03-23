package controller;

import model.Celebrity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.CelebrityGUI;

import javax.swing.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CelebrityControllerTest {

    private static CelebrityController controller;

    // Dummy GUI class to avoid null pointers in testing
    private static class DummyGUI extends CelebrityGUI {
        public DummyGUI() {
            super(controller);
        }

        @Override
        public void showMainContent(String role) {
            // Simulate showing main content
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
        CelebrityGUI dummyGUI = new DummyGUI();
        controller = new CelebrityController(dummyGUI);
    }

    @Test
    public void testHandleGuestSignInLoadsCelebrities() {
        controller.handleGuestSignIn();
        List<Celebrity> loadedCelebs = controller.getFavorites();
        assertNotNull(loadedCelebs);
        assertTrue(controller.getFavorites().isEmpty(), "Favorites should be empty after guest sign in");
    }


    @Test
    public void testSignUpAndSignInUser() {
        controller.handleSignUp("newuser", "password");
        controller.handleSignIn("newuser", "password", false);
        // If no exceptions are thrown, the test passes
    }

    @Test
    public void testAddToFavorites() {
        controller.handleGuestSignIn();
        Celebrity testCeleb = new Celebrity("Test Name", "Test Job", "Test Bio", "", List.of(), null);
        controller.addToFavorites(testCeleb);
        assertTrue(controller.getFavorites().contains(testCeleb), "Celebrity should be added to favorites");
    }

    @Test
    public void testRemoveFromFavorites() {
        Celebrity testCeleb = new Celebrity("Remove Me", "Job", "Bio", "", List.of(), null);
        controller.handleGuestSignIn();
        controller.addToFavorites(testCeleb);
        controller.removeFromFavorites(testCeleb);
        assertFalse(controller.getFavorites().contains(testCeleb), "Celebrity should be removed from favorites");
    }

    @Test
    public void testFilterCelebritiesByNameAndProfession() {
        // Arrange
        Celebrity lebron = new Celebrity("Lebron", "NBA", "GOAT", "", List.of(), null);
        controller.handleGuestSignIn(); // Load sample data
        controller.addToFavorites(lebron); // Add custom celebrity for filter test

        // Simulate user filtering by name and profession
        controller.filterCelebrities("Lebron", "NBA", "GOAT");

        // Assert
        List<Celebrity> favorites = controller.getFavorites();
        assertTrue(favorites.contains(lebron), "Filter should include Lebron with correct criteria.");
    }

}