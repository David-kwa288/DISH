package controller;

import controller.CelebrityController;
import model.Celebrity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.CelebrityGUI;

import javax.swing.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {
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
    public void testLoginAndInsertCelebrityIntegration() {
        controller.handleSignUp("user1", "pass1");
        controller.handleSignIn("user1", "pass1", false);
        assertDoesNotThrow(() -> controller.insertCelebrity());
    }

    @Test
    public void testAddAndRemoveFavoritesIntegration() {
        controller.handleGuestSignIn();
        Celebrity celeb = new Celebrity("TestFav", "Profession", "Bio", "", List.of(), null);
        controller.addToFavorites(celeb);
        assertTrue(controller.getFavorites().contains(celeb), "Celebrity should be added to favorites");
        controller.removeFromFavorites(celeb);
        assertFalse(controller.getFavorites().contains(celeb), "Celebrity should be removed from favorites");
    }

    @Test
    public void testSignInAndViewDetailsIntegration() {
        controller.handleSignUp("viewer", "123");
        controller.handleSignIn("viewer", "123", false);
        Celebrity celeb = new Celebrity("DetailView", "Profession", "Bio", "Award", List.of("https://via.placeholder.com/100"), null);
        assertDoesNotThrow(() -> controller.showCelebrityDetails(celeb));
    }

    @Test
    public void testGuestSignInAndFilterIntegration() {
        controller.handleGuestSignIn();
        Celebrity celeb = new Celebrity("FilterTest", "Actor", "Filtered Bio", "Oscar Winner", List.of(), null);
        controller.addToFavorites(celeb);
        controller.filterCelebrities("FilterTest", "Actor", "Oscar Winner");
        assertTrue(controller.getFavorites().contains(celeb), "Filtered celebrity should still exist in favorites");
    }

    @Test
    public void testSignInTriggersSampleCelebrities() {
        controller.handleSignUp("testviewer", "testpass");
        controller.handleSignIn("testviewer", "testpass", false);
        List<Celebrity> sampleList = controller.getFavorites();
        assertNotNull(sampleList, "Favorites list should be initialized, even if empty");
    }

    @Test
    public void testDuplicateFavoritesNotAllowed() {
        controller.handleGuestSignIn();
        Celebrity celeb = new Celebrity("UniqueFav", "Singer", "Bio", "", List.of(), null);
        controller.addToFavorites(celeb);
        controller.addToFavorites(celeb);
        long count = controller.getFavorites().stream().filter(c -> c.getName().equals("UniqueFav")).count();
        assertEquals(1, count, "Celebrity should not be added more than once to favorites");
    }

    @Test
    public void testRemoveNonExistentFavorite() {
        controller.handleGuestSignIn();
        Celebrity celeb = new Celebrity("Ghost", "Magician", "Bio", "", List.of(), null);
        assertDoesNotThrow(() -> controller.removeFromFavorites(celeb));
    }

    @Test
    public void testFilterWithEmptyFields() {
        controller.handleGuestSignIn();
        controller.filterCelebrities("", "All", "All");
        assertNotNull(controller.getFavorites(), "Favorites list should remain valid after filtering");
    }

    @Test
    public void testFavoritesNotPersistedAfterSignOut() {
        controller.handleSignUp("tester", "testpass");
        controller.handleSignIn("tester", "testpass", false);

        Celebrity celeb = new Celebrity("Van Gogh", "Painter", "Famous artist", "Legend", List.of(), null);
        controller.addToFavorites(celeb);
        assertTrue(controller.getFavorites().contains(celeb));

        controller.signOut();  // Simulate session end
        controller.handleSignIn("tester", "testpass", false);  // Simulate session restart

        assertFalse(controller.getFavorites().contains(celeb), "Favorites should not persist without a database.");
    }

}
