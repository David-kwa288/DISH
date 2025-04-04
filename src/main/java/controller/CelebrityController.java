package controller;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Celebrity;
import view.CelebrityGUI;
import view.CelebrityPanel;

public class CelebrityController {
    private CelebrityGUI gui;
    private List<Celebrity> celebrities = new ArrayList<>();
    private List<Celebrity> favorites = new ArrayList<>();
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
                    favorites.clear();
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
                gui.showLoginPanel();
            }
        } else {
            JOptionPane.showMessageDialog(gui.getFrame(), "Enter both username and password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleGuestSignIn() {
        isSignedIn = true;
        signedInUser = "Guest";
        signInTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        celebrities = getSampleCelebrities();
        favorites.clear();
        gui.showMainContent("guest");
        JOptionPane.showMessageDialog(gui.getFrame(), "Signed in as Guest at: " + signInTime);
    }

    public void signOut() {
        int confirm = JOptionPane.showConfirmDialog(gui.getFrame(), "Sure you want to sign out?", "Confirm Sign Out", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            signOutTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            JOptionPane.showMessageDialog(gui.getFrame(), "Signed in at: " + signInTime + "\nSigned out at: " + signOutTime);
            isSignedIn = false;
            signedInUser = null;
            celebrities.clear();
            favorites.clear();
            gui.showLoginPanel();
        }
    }

    public void updateCelebrityList(JPanel panel) {
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
                            BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
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
        JTextField awardsField = new JTextField(20);
        JTextField imageUrlField = new JTextField(20);
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
        JTextField searchField = new JTextField(20);
        JPanel searchPanel = new JPanel(new GridLayout(1, 2));
        searchPanel.add(new JLabel("Search Celebrity Name:"));
        searchPanel.add(searchField);
        int result = JOptionPane.showConfirmDialog(gui.getFrame(), searchPanel, "Search Celebrity to Update", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION && !searchField.getText().trim().isEmpty()) {
            String searchName = searchField.getText().trim().toLowerCase();
            List<Celebrity> matches = celebrities.stream()
                    .filter(c -> c.getName().toLowerCase().contains(searchName))
                    .collect(Collectors.toList());
            if (matches.isEmpty()) {
                JOptionPane.showMessageDialog(gui.getFrame(), "No celebrities found matching '" + searchName + "'!", "Not Found", JOptionPane.WARNING_MESSAGE);
            } else if (matches.size() > 1) {
                JOptionPane.showMessageDialog(gui.getFrame(), "Multiple matches found. Please be more specific!", "Multiple Matches", JOptionPane.WARNING_MESSAGE);
            } else {
                updateCelebrity(matches.get(0));
            }
        } else if (result == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(gui.getFrame(), "Search field can’t be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateCelebrity(Celebrity celebToUpdate) {
        if (celebToUpdate != null) {
            JTextField nameField = new JTextField(celebToUpdate.getName(), 20);
            JTextField professionField = new JTextField(celebToUpdate.getProfession(), 20);
            JTextField bioField = new JTextField(celebToUpdate.getBiography(), 20);
            JTextField awardsField = new JTextField(celebToUpdate.getAchievements(), 20);
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

    public void deleteCelebrity() {
        JTextField searchField = new JTextField(20);
        JPanel searchPanel = new JPanel(new GridLayout(1, 2));
        searchPanel.add(new JLabel("Search Celebrity Name:"));
        searchPanel.add(searchField);
        int result = JOptionPane.showConfirmDialog(gui.getFrame(), searchPanel, "Search Celebrity to Delete", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION && !searchField.getText().trim().isEmpty()) {
            String searchName = searchField.getText().trim().toLowerCase();
            List<Celebrity> matches = celebrities.stream()
                    .filter(c -> c.getName().toLowerCase().contains(searchName))
                    .collect(Collectors.toList());
            if (matches.isEmpty()) {
                JOptionPane.showMessageDialog(gui.getFrame(), "No celebrities found matching '" + searchName + "'!", "Not Found", JOptionPane.WARNING_MESSAGE);
            } else if (matches.size() > 1) {
                JOptionPane.showMessageDialog(gui.getFrame(), "Multiple matches found. Please be more specific!", "Multiple Matches", JOptionPane.WARNING_MESSAGE);
            } else {
                deleteCelebrity(matches.get(0));
            }
        } else if (result == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(gui.getFrame(), "Search field can’t be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteCelebrity(Celebrity celebToDelete) {
        if (celebToDelete != null) {
            int confirm = JOptionPane.showConfirmDialog(gui.getFrame(), "Sure you want to delete " + celebToDelete.getName() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                celebrities.remove(celebToDelete);
                favorites.remove(celebToDelete);
                updateCelebrityList(gui.getCelebListPanel());
                JOptionPane.showMessageDialog(gui.getFrame(), "Celebrity '" + celebToDelete.getName() + "' deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void showCelebrityDetails(Celebrity celeb) {
        // Create the dialog
        JDialog detailDialog = new JDialog(gui.getFrame(), celeb.getName(), true);
        detailDialog.setLayout(new BorderLayout());
        detailDialog.setSize(500, 350);
        detailDialog.setLocationRelativeTo(gui.getFrame());

        // Main content panel with a gradient background
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(245, 245, 255), 0, getHeight(), new Color(220, 230, 255));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Image panel (left side)
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        JLabel imageLabel = new JLabel();
        if (celeb.getImages() != null && !celeb.getImages().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(new URL(celeb.getImages().get(0)));
                java.awt.Image scaledImage = icon.getImage().getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
                imageLabel = new JLabel(new ImageIcon(scaledImage));
                imageLabel.setHorizontalAlignment(JLabel.CENTER);
                // Add a circular border and shadow effect
                imageLabel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(70, 130, 255), 2, true),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            } catch (Exception e) {
                imageLabel.setText("Image N/A");
                imageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                imageLabel.setForeground(new Color(150, 150, 150));
                System.err.println("Failed to load image for " + celeb.getName() + ": " + e.getMessage());
            }
        } else {
            imageLabel.setText("No Image");
            imageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            imageLabel.setForeground(new Color(150, 150, 150));
        }
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        contentPanel.add(imagePanel, BorderLayout.WEST);

        // Details panel (right side)
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);

        // Name
        JLabel nameLabel = new JLabel(celeb.getName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        nameLabel.setForeground(new Color(30, 30, 50));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(nameLabel);

        detailsPanel.add(Box.createVerticalStrut(10));

        // Profession
        JLabel professionLabel = new JLabel("Profession: " + celeb.getProfession());
        professionLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
        professionLabel.setForeground(new Color(70, 130, 255));
        professionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(professionLabel);

        detailsPanel.add(Box.createVerticalStrut(10));

        // Bio
        JLabel bioLabel = new JLabel("<html><b>Bio:</b> " + celeb.getBiography() + "</html>");
        bioLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bioLabel.setForeground(new Color(50, 50, 50));
        bioLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(bioLabel);

        detailsPanel.add(Box.createVerticalStrut(10));

        // Awards
        JLabel achievementsLabel = new JLabel("<html><b>Awards:</b> " + (celeb.getAchievements().isEmpty() ? "N/A" : celeb.getAchievements()) + "</html>");
        achievementsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        achievementsLabel.setForeground(new Color(50, 50, 50));
        achievementsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(achievementsLabel);

        contentPanel.add(detailsPanel, BorderLayout.CENTER);

        // Add a subtle shadow effect to the entire dialog
        detailDialog.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        detailDialog.add(contentPanel, BorderLayout.CENTER);
        detailDialog.setVisible(true);
    }

    public void filterCelebrities(String nameSearch, String profession, String award) {
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
                            BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
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

    public void addToFavorites(Celebrity celeb) {
        if ("Guest".equals(signedInUser)) {
            return; // No action or message for guests
        }
        if (!favorites.contains(celeb)) {
            favorites.add(celeb);
            JOptionPane.showMessageDialog(gui.getFrame(), celeb.getName() + " added to favorites!");
            updateCelebrityList(gui.getCelebListPanel());
        } else {
            JOptionPane.showMessageDialog(gui.getFrame(), celeb.getName() + " is already in favorites!");
        }
    }

    public void removeFromFavorites(Celebrity celeb) {
        if ("Guest".equals(signedInUser)) {
            return; // No action or message for guests
        }
        if (favorites.remove(celeb)) {
            JOptionPane.showMessageDialog(gui.getFrame(), celeb.getName() + " removed from favorites!");
            updateCelebrityList(gui.getCelebListPanel());
        } else {
            JOptionPane.showMessageDialog(gui.getFrame(), celeb.getName() + " was not in favorites!");
        }
    }

    public void showFavorites() {
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
                        BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                panel.add(celebPanel);
                panel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    public List<Celebrity> getFavorites() {
        return favorites;
    }

    public String getSignedInUser() {
        return signedInUser;
    }

    public String getRole() {
        return users.get(signedInUser) != null ? users.get(signedInUser).get("role") : null;
    }

    public static JPanel addImage(Celebrity celeb) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setMaximumSize(new Dimension(650, 120));

        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(80, 80));

        if (celeb.getImages() != null && !celeb.getImages().isEmpty()) {
            try {
                URL imageURL = new URL(celeb.getImages().get(0));
                Image image = ImageIO.read(imageURL);
                Image scaled = image.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
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

    private List<Celebrity> getSampleCelebrities() {
        List<Celebrity> list = new ArrayList<>();
        String placeholderImage = "https://via.placeholder.com/100";
        list.add(new Celebrity("Leonardo DiCaprio", "Actor", "Star of Titanic and The Revenant", "Oscar Winner", List.of("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS9R7hEKTEHZPf9cCG314IjrPjftcwCUrAHdA&s"), null));
        list.add(new Celebrity("Beyoncé", "Singer", "Global icon with multiple Grammys", "Grammy Winner", List.of("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQvmoCfN884frsjshSRnaXTEVgK3tEq9jhE9Q&s"), null));
        list.add(new Celebrity("Chris Hemsworth", "Actor", "Thor in the Marvel Universe", "Action Star", List.of("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9ALJAyGJC7tuswE_-04bz0Ej0aemfK1qAKw&s"), null));
        list.add(new Celebrity("Taylor Swift", "Singer-Songwriter", "Pop star with record-breaking albums", "Billboard Queen", List.of("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTw5tE7TlJnLliRelqV_nNc_cQQb7uAJAG2KA&s"), null));
        list.add(new Celebrity("Dwayne Johnson", "Actor/Wrestler", "The Rock, action movie icon", "Box Office King", List.of("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcROksKDZqYYqYz-IrYLm9BTrgRYf8VaqkjZjw&s"), null));
        list.add(new Celebrity("Angelina Jolie", "Actress", "Known for Tomb Raider and Maleficent", "Humanitarian", List.of("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS_DCG9zZmNmegdUxekU-zp3Xi29ditcGDFrg&s"), null));
        list.add(new Celebrity("Brad Pitt", "Actor", "Star of Fight Club and Once Upon a Time in Hollywood", "Oscar Winner", List.of("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcST3SEKmrY7tJkYf56NUrhl-ccX5FfbBS6tbw&s"), null));
        list.add(new Celebrity("Rihanna", "Singer", "Hitmaker and Fenty Beauty founder", "Fashion Icon", List.of("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTVU2hF7RaELxZSEVaBwXIuMEg41pHLCAXARQ&s"), null));
        list.add(new Celebrity("Tom Hanks", "Actor", "Forrest Gump and Cast Away legend", "Oscar Winner", List.of("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhQVbTdh9qRXs3AIynt1tCl-rKWceK83HYaQ&s"), null));
        list.add(new Celebrity("Lady Gaga", "Singer", "Known for her bold style and vocals", "Oscar Winner", List.of("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTgOKYouIRz5drnvAmTJYgqvFr36XiCUxMhtg&s"), null));
        return list;
    }
}