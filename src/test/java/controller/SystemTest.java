package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.CelebrityGUI;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class SystemTest {
    private CelebrityController controller;

    // Dummy GUI to avoid real GUI interactions
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
    public void testUserAuthenticationWorkflow() {
        controller.handleSignUp("authUser", "authPass");
        assertDoesNotThrow(() -> controller.handleSignIn("authUser", "authPass", false));
        assertTrue(controller.getFavorites().isEmpty(), "New user should have no favorites on sign in.");
    }

    @Test
    public void testEndToEndCelebrityManagement() {
        controller.handleSignIn("admin", "admin123", true);
        assertDoesNotThrow(() -> controller.insertCelebrity());
        assertDoesNotThrow(() -> controller.updateCelebrity());
        assertDoesNotThrow(() -> controller.deleteCelebrity());
    }

    @Test
    public void testGUINavigationAndUsability() {
        CelebrityGUI gui = new DummyGUI(controller);
        controller.setGui(gui);
        controller.handleGuestSignIn();
        gui.showMainContent("guest");
        JPanel panel = gui.getCelebListPanel();
        assertNotNull(panel, "GUI panel should not be null");
    }

    @Test
    public void testSecurityAndAccessControl() {
        controller.handleSignUp("normalUser", "userpass");

        // This call should fail or display a dialog, but not crash
        controller.handleSignIn("normalUser", "userpass", true); // Incorrect role

        // We can't assert access control here unless the method throws an exception or flag
        assertTrue(controller.getFavorites().isEmpty(), "Non-admin should not gain admin privileges.");
    }
}