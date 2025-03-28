package main.java.controller;


import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;

import main.java.model.Celebrity;
import main.java.view.CelebrityGUI;
import main.java.view.CelebrityPanel;

public class CelebrityController {
    private CelebrityGUI gui;
    private List<Celebrity> celebrities = new ArrayList<>();
    private List<Celebrity> favorites = new ArrayList<>(); // New field for favorites
    private Map<String, Map<String, String>> users = new HashMap<>();
    private boolean isSignedIn = false;
    private String signedInUser = null;
    private String signInTime;
    private String signOutTime;

    public CelebrityController(CelebrityGUI gui) {
        this.gui = gui;

        // Default admin credentials
        Map<String, String> adminDetails = new HashMap<>();
        adminDetails.put("password", "admin123");
        adminDetails.put("role", "admin");
        users.put("admin", adminDetails);
    }

    public void setGui(CelebrityGUI gui) {
        this.gui = gui;
    }

    public void showSignInPanel(boolean isAdmin) {
        gui.showSignInPanel(isAdmin);
    }

    public void showSignUpPanel() {
        gui.showSignUpPanel();
    }

    public void handleSignIn(String username, String password, boolean isAdmin) {
        System.out.println("Handling sign-in for username: " + username + ", isAdmin: " + isAdmin);
        if (!username.isEmpty() && !password.isEmpty()) {
            Map<String, String> userDetails = users.get(username);
            if (userDetails != null && userDetails.get("password").equals(password)) {
                String role = userDetails.get("role");
                System.out.println("User role: " + role);
                if (isAdmin && !"admin".equals(role)) {
                    JOptionPane.showMessageDialog(gui.getFrame(), "This is for admins only!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!isAdmin && "admin".equals(role)) {
                    JOptionPane.showMessageDialog(gui.getFrame(), "Admins use Admin Sign In!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    isSignedIn = true;
                    signedInUser = username;
                    signInTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    celebrities = getSampleCelebrities();
                    favorites.clear(); // Reset favorites on new sign-in
                    System.out.println("Celebrities loaded: " + celebrities.size());
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
        System.out.println("Handling sign-up for username: " + username);
        if (!username.isEmpty() && !password.isEmpty()) {
            if (users.containsKey(username)) {
                JOptionPane.showMessageDialog(gui.getFrame(), "Username already taken!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Map<String, String> userDetails = new HashMap<>();
                userDetails.put("password", password);
                userDetails.put("role", "user");
                users.put(username, userDetails);
                JOptionPane.showMessageDialog(gui.getFrame(), "Sign-up done! Please sign in.");
                gui.showLoginPanel();
            }
        } else {
            JOptionPane.showMessageDialog(gui.getFrame(), "Enter both username and password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleGuestSignIn() {
        System.out.println("Handling guest sign-in");
        isSignedIn = true;
        signedInUser = "Guest";
        signInTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        celebrities = getSampleCelebrities();
        favorites.clear(); // Reset favorites for guest
        gui.showMainContent("guest");
        JOptionPane.showMessageDialog(gui.getFrame(), "Signed in as Guest at: " + signInTime);
    }

    public void signOut() {
        System.out.println("Signing out user: " + signedInUser);
        int confirm = JOptionPane.showConfirmDialog(gui.getFrame(), "Sure you want to sign out?", "Confirm Sign Out", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            signOutTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            JOptionPane.showMessageDialog(gui.getFrame(), "Signed in at: " + signInTime + "\nSigned out at: " + signOutTime);
            isSignedIn = false;
            signedInUser = null;
            celebrities.clear();
            favorites.clear(); // Clear favorites on sign-out
            gui.showLoginPanel();
        }
    }

    public void updateCelebrityList(JPanel panel) {
        System.out.println("Updating celebrity list, count: " + celebrities.size());
        panel.removeAll();
        if (celebrities.isEmpty()) {
            JLabel emptyLabel = new JLabel("No celebrities available.");
            emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            emptyLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(emptyLabel);
        } else {
            for (Celebrity celeb : celebrities) {
                JPanel celebPanel = CelebrityPanel.createCelebrityPanel(celeb, this);
                if (favorites.contains(celeb)) {
                    celebPanel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(255, 215, 0), 2), // Gold border for favorites
                            BorderFactory.createEmptyBorder(10, 10, 10, 10)
                    ));
                }
                panel.add(celebPanel);
                panel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    public void insertCelebrity() {
        JTextField nameField = new JTextField(20);
        JTextField professionField = new JTextField(20);
        JTextField bioField = new JTextField(20);
        JTextField imageUrlField = new JTextField(20);
        JTextField awardsField = new JTextField(20); // Added awards field
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Profession:"));
        inputPanel.add(professionField);
        inputPanel.add(new JLabel("Biography:"));
        inputPanel.add(bioField);
        inputPanel.add(new JLabel("Awards:"));
        inputPanel.add(awardsField);
        inputPanel.add(new JLabel("Image URL or Path:"));
        inputPanel.add(imageUrlField);
        int result = JOptionPane.showConfirmDialog(gui.getFrame(), inputPanel, "Insert Celebrity", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION && !nameField.getText().trim().isEmpty()) {
            String name = nameField.getText().trim();
            String profession = professionField.getText().trim();
            String bio = bioField.getText().trim();
            String awards = awardsField.getText().trim();
            String imageUrl = imageUrlField.getText().trim().isEmpty() ? "https://via.placeholder.com/100" : imageUrlField.getText().trim();

            boolean nameExists = celebrities.stream().anyMatch(celeb -> celeb.getName().equalsIgnoreCase(name));
            if (nameExists) {
                JOptionPane.showMessageDialog(gui.getFrame(), "A celebrity with the name '" + name + "' already exists!", "Duplicate Name Error", JOptionPane.ERROR_MESSAGE);
            } else {
                celebrities.add(new Celebrity(name, profession, bio, awards, List.of(imageUrl), null));
                updateCelebrityList(gui.getCelebListPanel());
                JOptionPane.showMessageDialog(gui.getFrame(), "Celebrity '" + name + "' added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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
                    JTextField awardsField = new JTextField(celebToUpdate.getAchievements(), 20); // Added awards
                    JTextField imageUrlField = new JTextField(celebToUpdate.getImages() != null && !celebToUpdate.getImages().isEmpty() ? celebToUpdate.getImages().get(0) : "", 20);
                    JPanel inputPanel = new JPanel(new GridLayout(5, 2));
                    inputPanel.add(new JLabel("Name:"));
                    inputPanel.add(nameField);
                    inputPanel.add(new JLabel("Profession:"));
                    inputPanel.add(professionField);
                    inputPanel.add(new JLabel("Biography:"));
                    inputPanel.add(bioField);
                    inputPanel.add(new JLabel("Awards:"));
                    inputPanel.add(awardsField);
                    inputPanel.add(new JLabel("Image URL or Path:"));
                    inputPanel.add(imageUrlField);
                    int result = JOptionPane.showConfirmDialog(gui.getFrame(), inputPanel, "Update Celebrity", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION && !nameField.getText().trim().isEmpty()) {
                        String newName = nameField.getText().trim();
                        String newProfession = professionField.getText().trim();
                        String newBio = bioField.getText().trim();
                        String newAwards = awardsField.getText().trim();
                        String newImageUrl = imageUrlField.getText().trim().isEmpty() ? "https://via.placeholder.com/100" : imageUrlField.getText().trim();
                        Celebrity updatedCelebrity = new Celebrity(newName, newProfession, newBio, newAwards, List.of(newImageUrl), null);
                        int index = celebrities.indexOf(celebToUpdate);
                        if (index != -1) {
                            celebrities.set(index, updatedCelebrity);
                            updateCelebrityList(gui.getCelebListPanel());
                            JOptionPane.showMessageDialog(gui.getFrame(), "Celebrity '" + newName + "' updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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
                        favorites.remove(celebToDelete); // Remove from favorites if present
                        updateCelebrityList(gui.getCelebListPanel());
                        JOptionPane.showMessageDialog(gui.getFrame(), "Celebrity '" + selectedName + "' deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }
    }

    public void showCelebrityDetails(Celebrity celeb) {
        JDialog detailDialog = new JDialog(gui.getFrame(), celeb.getName() + " - Details", true);
        detailDialog.setLayout(new BorderLayout(10, 10));
        detailDialog.setSize(400, 300);
        detailDialog.setLocationRelativeTo(gui.getFrame());

        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setBackground(new Color(240, 245, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name: " + celeb.getName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        detailPanel.add(nameLabel, gbc);

        gbc.gridy = 1;
        JLabel professionLabel = new JLabel("Profession: " + celeb.getProfession());
        detailPanel.add(professionLabel, gbc);

        gbc.gridy = 2;
        JLabel bioLabel = new JLabel("<html>Bio: " + celeb.getBiography() + "</html>");
        bioLabel.setVerticalAlignment(JLabel.TOP);
        detailPanel.add(bioLabel, gbc);

        gbc.gridy = 3;
        JLabel achievementsLabel = new JLabel("Awards: " + (celeb.getAchievements().isEmpty() ? "N/A" : celeb.getAchievements()));
        detailPanel.add(achievementsLabel, gbc);

        if (celeb.getImages() != null && !celeb.getImages().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(new URL(celeb.getImages().get(0)));
                java.awt.Image scaledImage = icon.getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                detailDialog.add(imageLabel, BorderLayout.WEST);
            } catch (Exception e) {
                System.err.println("Failed to load image for " + celeb.getName() + ": " + e.getMessage());
            }
        }

        detailDialog.add(detailPanel, BorderLayout.CENTER);
        detailDialog.setVisible(true);
    }

    public void filterCelebrities(String nameSearch, String profession, String award) {
        System.out.println("Filtering celebrities - Name: " + nameSearch + ", Profession: " + profession + ", Award: " + award);
        JPanel panel = gui.getCelebListPanel();
        panel.removeAll();

        List<Celebrity> filteredCelebrities = new ArrayList<>();
        for (Celebrity celeb : celebrities) {
            boolean matchesName = nameSearch.isEmpty() || celeb.getName().toLowerCase().startsWith(nameSearch.toLowerCase());
            boolean matchesProfession = profession.equals("All") || celeb.getProfession().equalsIgnoreCase(profession);
            boolean matchesAward = award.equals("All") ||
                    (award.equals("None") && celeb.getAchievements().isEmpty()) ||
                    (!celeb.getAchievements().isEmpty() && celeb.getAchievements().equalsIgnoreCase(award));

            if (matchesName && matchesProfession && matchesAward) {
                filteredCelebrities.add(celeb);
            }
        }

        if (filteredCelebrities.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No celebrities found matching the criteria.");
            noResultsLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            noResultsLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(noResultsLabel);
        } else {
            for (Celebrity celeb : filteredCelebrities) {
                JPanel celebPanel = CelebrityPanel.createCelebrityPanel(celeb, this);
                if (favorites.contains(celeb)) {
                    celebPanel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(255, 215, 0), 2), // Gold border for favorites
                            BorderFactory.createEmptyBorder(10, 10, 10, 10)
                    ));
                }
                panel.add(celebPanel);
                panel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    // Add to favorites
    public void addToFavorites(Celebrity celeb) {
        if (!favorites.contains(celeb)) {
            favorites.add(celeb);
            JOptionPane.showMessageDialog(gui.getFrame(), celeb.getName() + " added to favorites!");
        } else {
            JOptionPane.showMessageDialog(gui.getFrame(), celeb.getName() + " is already in favorites!");
        }
        updateCelebrityList(gui.getCelebListPanel()); // Refresh display
    }

    // Remove from favorites
    public void removeFromFavorites(Celebrity celeb) {
        if (favorites.remove(celeb)) {
            JOptionPane.showMessageDialog(gui.getFrame(), celeb.getName() + " removed from favorites!");
        } else {
            JOptionPane.showMessageDialog(gui.getFrame(), celeb.getName() + " was not in favorites!");
        }
        updateCelebrityList(gui.getCelebListPanel()); // Refresh display
    }

    // Show favorites list
    public void showFavorites() {
        System.out.println("Showing favorites list, count: " + favorites.size());
        JPanel panel = gui.getCelebListPanel();
        panel.removeAll();
        if (favorites.isEmpty()) {
            JLabel emptyLabel = new JLabel("No favorites yet.");
            emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            emptyLabel.setHorizontalAlignment(JLabel.CENTER);
            panel.add(emptyLabel);
        } else {
            for (Celebrity celeb : favorites) {
                JPanel celebPanel = CelebrityPanel.createCelebrityPanel(celeb, this);
                celebPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(255, 215, 0), 2), // Gold border for favorites
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                panel.add(celebPanel);
                panel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    // Getter for favorites list (used by CelebrityPanel)
    public List<Celebrity> getFavorites() {
        return favorites;
    }

    public static JPanel addImage(Celebrity celeb) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setMaximumSize(new Dimension(650, 120));

        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(100, 100));

        if (celeb.getImages() != null && !celeb.getImages().isEmpty()) {
            try {
                URL imageURL = new URL(celeb.getImages().get(0));
                Image image = ImageIO.read(imageURL);
                Image scaled = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaled));
            } catch (IOException e) {
                imageLabel.setText("Image N/A");
                System.out.println("Failed to load image " + celeb.getName() + ": " + e.getMessage());
            }
        } else {
            imageLabel.setText("No Image");
        }

        panel.add(imageLabel, BorderLayout.EAST);
        return panel;
    }

    List<Celebrity> getSampleCelebrities() {
        List<Celebrity> list = new ArrayList<>();
        String placeholderImage = "https://via.placeholder.com/100";

        // 30 random celebrities with varied professions and awards
        list.add(new Celebrity("Leonardo DiCaprio", "Actor", "Star of Titanic and The Revenant", "Oscar Winner", List.of(placeholderImage), null));
        list.add(new Celebrity("Beyoncé", "Singer", "Global icon with multiple Grammys", "Grammy Winner", List.of(placeholderImage), null));
        list.add(new Celebrity("Chris Hemsworth", "Actor", "Thor in the Marvel Universe", "Action Star", List.of(placeholderImage), null));
        list.add(new Celebrity("Taylor Swift", "Singer-Songwriter", "Pop star with record-breaking albums", "Billboard Queen", List.of(placeholderImage), null));
        list.add(new Celebrity("Dwayne Johnson", "Actor/Wrestler", "The Rock, action movie icon", "Box Office King", List.of(placeholderImage), null));
        list.add(new Celebrity("Angelina Jolie", "Actress", "Known for Tomb Raider and Maleficent", "Humanitarian", List.of(placeholderImage), null));
        list.add(new Celebrity("Brad Pitt", "Actor", "Star of Fight Club and Once Upon a Time in Hollywood", "Oscar Winner", List.of(placeholderImage), null));
        list.add(new Celebrity("Rihanna", "Singer", "Hitmaker and Fenty Beauty founder", "Fashion Icon", List.of(placeholderImage), null));
        list.add(new Celebrity("Tom Hanks", "Actor", "Forrest Gump and Cast Away legend", "Oscar Winner", List.of(placeholderImage), null));
        list.add(new Celebrity("Lady Gaga", "Singer", "Known for her bold style and vocals", "Oscar Winner", List.of(placeholderImage), null));
        list.add(new Celebrity("Will Smith", "Actor", "Fresh Prince and Men in Black star", "", List.of(placeholderImage), null));
        list.add(new Celebrity("Adele", "Singer", "Soulful voice behind Hello", "Grammy Winner", List.of(placeholderImage), null));
        list.add(new Celebrity("Johnny Depp", "Actor", "Pirates of the Caribbean icon", "", List.of(placeholderImage), null));
        list.add(new Celebrity("Emma Watson", "Actress", "Hermione in Harry Potter", "", List.of(placeholderImage), null));
        list.add(new Celebrity("Kanye West", "Rapper", "Influential artist and producer", "", List.of(placeholderImage), null));
        list.add(new Celebrity("Scarlett Johansson", "Actress", "Black Widow in the MCU", "", List.of(placeholderImage), null));
        list.add(new Celebrity("Justin Bieber", "Singer", "Pop sensation from Baby to Justice", "", List.of(placeholderImage), null));
        list.add(new Celebrity("Natalie Portman", "Actress", "Star of Black Swan", "Oscar Winner", List.of(placeholderImage), null));
        list.add(new Celebrity("Drake", "Rapper", "Hitmaker with Take Care and Scorpion", "", List.of(placeholderImage), null));
        list.add(new Celebrity("Sandra Bullock", "Actress", "Known for Speed and The Blind Side", "Oscar Winner", List.of(placeholderImage), null));
        list.add(new Celebrity("Robert Downey Jr.", "Actor", "Iron Man in the MCU", "", List.of(placeholderImage), null));
        list.add(new Celebrity("Ariana Grande", "Singer", "Pop star with a powerful voice", "Grammy Winner", List.of(placeholderImage), null));
        list.add(new Celebrity("Hugh Jackman", "Actor", "Wolverine in X-Men", "", List.of(placeholderImage), null));
        list.add(new Celebrity("Selena Gomez", "Singer", "Disney star turned pop artist", "", List.of(placeholderImage), null));
        list.add(new Celebrity("Matt Damon", "Actor", "Star of Bourne series", "Oscar Winner", List.of(placeholderImage), null));
        list.add(new Celebrity("Katy Perry", "Singer", "Known for Teenage Dream", "", List.of(placeholderImage), null));
        list.add(new Celebrity("Chris Evans", "Actor", "Captain America in the MCU", "", List.of(placeholderImage), null));
        list.add(new Celebrity("Meryl Streep", "Actress", "Legendary performer with many awards", "Oscar Winner", List.of(placeholderImage), null));
        list.add(new Celebrity("Ed Sheeran", "Singer", "Acoustic star with Divide", "Grammy Winner", List.of(placeholderImage), null));
        list.add(new Celebrity("Zendaya", "Actress", "Star of Euphoria and Spider-Man", "", List.of(placeholderImage), null));

        return list;
    }
}