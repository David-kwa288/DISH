package controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.Celebrity;
import view.CelebrityGUI;
import view.CelebrityPanel;

public class CelebrityController {
    private CelebrityGUI gui;
    private List<Celebrity> celebrities = new ArrayList<>();
    private Map<String, Map<String, String>> users = new HashMap<>();
    private boolean isSignedIn = false;
    private String signedInUser = null;
    private String signInTime;
    private String signOutTime;

    public CelebrityController(CelebrityGUI gui) {
        this.gui = gui;

        // this is the default admin credentials, make sure to use it when testingggg
        Map<String, String> adminDetails = new HashMap<>();
        adminDetails.put("password", "admin123");
        adminDetails.put("role", "admin");
        users.put("admin", adminDetails);
    }


    public void setGui(CelebrityGUI gui) {
        this.gui = gui;
    }

    public void showSelectionPanel() {
        gui.showSelectionPanel();
    }

    public void showSignInPanel(boolean isAdmin) {
        gui.updateFrame(gui.createSignInPanel(isAdmin));
    }

    public void showSignUpPanel() {
        gui.updateFrame(gui.createSignUpPanel());
    }

    public void handleSignIn(String username, String password, boolean isAdmin) {
        if (!username.isEmpty() && !password.isEmpty()) {
            Map<String, String> userDetails = users.get(username);
            if (userDetails != null && userDetails.get("password").equals(password)) {
                String role = userDetails.get("role");
                if (isAdmin && !"admin".equals(role)) {
                    JOptionPane.showMessageDialog(gui.getFrame(), "This is for admins only!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!isAdmin && "admin".equals(role)) {
                    JOptionPane.showMessageDialog(gui.getFrame(), "Admins use Admin Sign In!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    isSignedIn = true;
                    signedInUser = username;
                    signInTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    celebrities = getSampleCelebrities();
                    gui.showMainContent(role);
                    JOptionPane.showMessageDialog(gui.getFrame(), "Signed in at: " + signInTime);
                }
            } else {
                JOptionPane.showMessageDialog(gui.getFrame(), "Wrong username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(gui.getFrame(), "Enter both username and password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleSignUp(String username, String password) {
        if (!username.isEmpty() && !password.isEmpty()) {
            if (users.containsKey(username)) {
                JOptionPane.showMessageDialog(gui.getFrame(), "Username already taken!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Map<String, String> userDetails = new HashMap<>();
                userDetails.put("password", password);
                userDetails.put("role", "user");
                users.put(username, userDetails);
                JOptionPane.showMessageDialog(gui.getFrame(), "Sign-up done! Please sign in.");
                showSelectionPanel();
            }
        } else {
            JOptionPane.showMessageDialog(gui.getFrame(), "Enter both username and password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void signOut() {
        int confirm = JOptionPane.showConfirmDialog(gui.getFrame(), "Sure you want to sign out?", "Confirm Sign Out", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            signOutTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            JOptionPane.showMessageDialog(gui.getFrame(), "Signed in at: " + signInTime + "\nSigned out at: " + signOutTime);
            isSignedIn = false;
            signedInUser = null;
            celebrities.clear();
            showSelectionPanel();
        }
    }

    public void updateCelebrityList(JPanel panel) {
        panel.removeAll();
        for (Celebrity celeb : celebrities) {
            JPanel celebPanel = CelebrityPanel.createCelebrityPanel(celeb);
            panel.add(celebPanel);
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        panel.revalidate();
        panel.repaint();
    }

    public void insertCelebrity() {
        JTextField nameField = new JTextField(20);
        JTextField professionField = new JTextField(20);
        JTextField bioField = new JTextField(20);
        JTextField imageUrlField = new JTextField(20);
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Profession:"));
        inputPanel.add(professionField);
        inputPanel.add(new JLabel("Biography:"));
        inputPanel.add(bioField);
        inputPanel.add(new JLabel("Image URL or Path:"));
        inputPanel.add(imageUrlField);
        int result = JOptionPane.showConfirmDialog(gui.getFrame(), inputPanel, "Insert Celebrity", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION && !nameField.getText().trim().isEmpty()) {
            String name = nameField.getText().trim();
            String profession = professionField.getText().trim();
            String bio = bioField.getText().trim();
            String imageUrl = imageUrlField.getText().trim().isEmpty() ? "https://via.placeholder.com/100" : imageUrlField.getText().trim();

            boolean nameExists = celebrities.stream().anyMatch(celeb -> celeb.getName().equalsIgnoreCase(name));
            if (nameExists) {
                JOptionPane.showMessageDialog(gui.getFrame(), "A celebrity with the name '" + name + "' already exists!", "Duplicate Name Error", JOptionPane.ERROR_MESSAGE);
            } else {
                celebrities.add(new Celebrity(name, profession, bio, "", List.of(imageUrl), null));
                updateCelebrityList(gui.getCelebListPanel());
            }
        } else if (result == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(gui.getFrame(), "Name can’t be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateCelebrity() {
        String[] names = celebrities.stream().map(Celebrity::getName).toArray(String[]::new);
        if (names.length == 0) {
            JOptionPane.showMessageDialog(gui.getFrame(), "No celebrities to update!");
        } else {
            String selectedName = (String) JOptionPane.showInputDialog(gui.getFrame(), "Select Celebrity to Update:", "Update Celebrity", JOptionPane.QUESTION_MESSAGE, null, names, names[0]);
            if (selectedName != null) {
                Celebrity celebToUpdate = celebrities.stream().filter(c -> c.getName().equals(selectedName)).findFirst().orElse(null);
                if (celebToUpdate != null) {
                    JTextField nameField = new JTextField(celebToUpdate.getName(), 20);
                    JTextField professionField = new JTextField(celebToUpdate.getProfession(), 20);
                    JTextField bioField = new JTextField(celebToUpdate.getBiography(), 20);
                    JTextField imageUrlField = new JTextField(celebToUpdate.getImages() != null && !celebToUpdate.getImages().isEmpty() ? celebToUpdate.getImages().get(0) : "", 20);
                    JPanel inputPanel = new JPanel(new GridLayout(4, 2));
                    inputPanel.add(new JLabel("Name:"));
                    inputPanel.add(nameField);
                    inputPanel.add(new JLabel("Profession:"));
                    inputPanel.add(professionField);
                    inputPanel.add(new JLabel("Biography:"));
                    inputPanel.add(bioField);
                    inputPanel.add(new JLabel("Image URL or Path:"));
                    inputPanel.add(imageUrlField);
                    int result = JOptionPane.showConfirmDialog(gui.getFrame(), inputPanel, "Update Celebrity", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION && !nameField.getText().trim().isEmpty()) {
                        String newName = nameField.getText().trim();
                        String newProfession = professionField.getText().trim();
                        String newBio = bioField.getText().trim();
                        String newImageUrl = imageUrlField.getText().trim().isEmpty() ? "https://via.placeholder.com/100" : imageUrlField.getText().trim();
                        Celebrity updatedCelebrity = new Celebrity(newName, newProfession, newBio, "", List.of(newImageUrl), null);
                        int index = celebrities.indexOf(celebToUpdate);
                        if (index != -1) {
                            celebrities.set(index, updatedCelebrity);
                            updateCelebrityList(gui.getCelebListPanel());
                        }
                    } else if (result == JOptionPane.OK_OPTION) {
                        JOptionPane.showMessageDialog(gui.getFrame(), "Name can’t be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    public void deleteCelebrity() {
        String[] names = celebrities.stream().map(Celebrity::getName).toArray(String[]::new);
        if (names.length == 0) {
            JOptionPane.showMessageDialog(gui.getFrame(), "No celebrities to delete!");
        } else {
            String selectedName = (String) JOptionPane.showInputDialog(gui.getFrame(), "Select Celebrity to Delete:", "Delete Celebrity", JOptionPane.QUESTION_MESSAGE, null, names, names[0]);
            if (selectedName != null) {
                Celebrity celebToDelete = celebrities.stream().filter(c -> c.getName().equals(selectedName)).findFirst().orElse(null);
                if (celebToDelete != null) {
                    int confirm = JOptionPane.showConfirmDialog(gui.getFrame(), "Sure you want to delete " + selectedName + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        celebrities.remove(celebToDelete);
                        updateCelebrityList(gui.getCelebListPanel());
                    }
                }
            }
        }
    }

    private List<Celebrity> getSampleCelebrities() {
        List<Celebrity> list = new ArrayList<>();
        String placeholderImage = "https://via.placeholder.com/100";
        list.add(new Celebrity("Leonardo DiCaprio", "Actor", "Star of Titanic and The Revenant", "Oscar Winner", List.of(placeholderImage), null));
        list.add(new Celebrity("Beyoncé", "Singer", "Global icon with multiple Grammys", "Music Legend", List.of(placeholderImage), null));
        list.add(new Celebrity("Chris Hemsworth", "Actor", "Thor in the Marvel Universe", "Action Star", List.of(placeholderImage), null));
        list.add(new Celebrity("Taylor Swift", "Singer-Songwriter", "Pop star with record-breaking albums", "Billboard Queen", List.of(placeholderImage), null));
        list.add(new Celebrity("Dwayne Johnson", "Actor/Wrestler", "The Rock, action movie icon", "Box Office King", List.of(placeholderImage), null));
        return list;
    }
}