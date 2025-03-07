package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.PrintStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import model.Celebrity;

public class CelebrityGUI {
    private static List<Celebrity> celebrities = new ArrayList();
    private static JFrame frame;
    private static JPanel celebListPanel;
    private static JPanel mainPanel;
    private static boolean isSignedIn = false;
    private static String signInTime;
    private static String signOutTime;

    public CelebrityGUI() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Celebrities Catalog - Week 1 Progress");
            frame.setDefaultCloseOperation(3);
            frame.setSize(700, 600);

            // Show sign-in panel initially
            showSignInPanel();

            frame.setVisible(true);
        });
    }

    private static void showSignInPanel() {
        JPanel signInPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        signInPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JButton signInButton = new JButton("Sign In");

        signInPanel.add(new JLabel("Username:"));
        signInPanel.add(usernameField);
        signInPanel.add(new JLabel("Password:"));
        signInPanel.add(passwordField);
        signInPanel.add(new JLabel(""));
        signInPanel.add(signInButton);

        frame.getContentPane().removeAll();
        frame.add(signInPanel);
        frame.revalidate();
        frame.repaint();

        signInButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            // Simple validation (you can enhance this)
            if (!username.isEmpty() && !password.isEmpty()) {
                isSignedIn = true;
                signInTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                showMainContent();
                JOptionPane.showMessageDialog(frame, "Signed in at: " + signInTime);
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static void showMainContent() {
        celebrities = getSampleCelebrities();
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.LIGHT_GRAY);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        JButton insertButton = new JButton("Insert Celebrity");
        JButton updateButton = new JButton("Update Celebrity");
        JButton deleteButton = new JButton("Delete Celebrity");
        JButton signOutButton = new JButton("Sign Out");

        controlPanel.add(insertButton);
        controlPanel.add(updateButton);
        controlPanel.add(deleteButton);
        controlPanel.add(signOutButton);

        celebListPanel = new JPanel();
        celebListPanel.setLayout(new BoxLayout(celebListPanel, 1));
        updateCelebrityList(celebListPanel);

        JScrollPane scrollPane = new JScrollPane(celebListPanel);
        scrollPane.setVerticalScrollBarPolicy(22);

        mainPanel.add(controlPanel, "North");
        mainPanel.add(scrollPane, "Center");

        insertButton.addActionListener((e) -> insertCelebrity());
        updateButton.addActionListener((e) -> updateCelebrity());
        deleteButton.addActionListener((e) -> deleteCelebrity());
        signOutButton.addActionListener((e) -> signOut());

        frame.getContentPane().removeAll();
        frame.add(mainPanel);
        frame.revalidate();
        frame.repaint();
    }

    private static void signOut() {
        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to sign out?", "Confirm Sign Out", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            signOutTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            JOptionPane.showMessageDialog(frame, "Signed in at: " + signInTime + "\nSigned out at: " + signOutTime);
            isSignedIn = false;
            celebrities.clear();
            showSignInPanel();
        }
    }

    private static void updateCelebrityList(JPanel panel) {
        panel.removeAll();
        Iterator var1 = celebrities.iterator();

        while(var1.hasNext()) {
            Celebrity celeb = (Celebrity)var1.next();
            JPanel celebPanel = createCelebrityPanel(celeb);
            panel.add(celebPanel);
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        panel.revalidate();
        panel.repaint();
    }

    private static JPanel createCelebrityPanel(Celebrity celeb) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setMaximumSize(new Dimension(650, 120));
        String var10002 = celeb.getName();
        JLabel textLabel = new JLabel("<html><b>" + var10002 + "</b><br>Profession: " + celeb.getProfession() + "<br>Bio: " + celeb.getBiography() + "</html>");
        textLabel.setFont(new Font("Arial", 0, 14));
        textLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(textLabel, "Center");
        JLabel imageLabel;
        if (celeb.getImages() != null && !celeb.getImages().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(new URL((String)celeb.getImages().get(0)));
                Image scaledImage = icon.getImage().getScaledInstance(100, 100, 4);
                imageLabel = new JLabel(new ImageIcon(scaledImage));
            } catch (Exception var6) {
                Exception e = var6;
                imageLabel = new JLabel("Image N/A");
                PrintStream var10000 = System.err;
                String var10001 = celeb.getName();
                var10000.println("Failed to load image for " + var10001 + ": " + e.getMessage());
            }
        } else {
            imageLabel = new JLabel("No Image");
        }

        imageLabel.setPreferredSize(new Dimension(100, 100));
        panel.add(imageLabel, "East");
        return panel;
    }

    private static void insertCelebrity() {
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
        int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Insert Celebrity", 2);
        if (result == 0 && !nameField.getText().trim().isEmpty()) {
            String name = nameField.getText().trim();
            String profession = professionField.getText().trim();
            String bio = bioField.getText().trim();
            String imageUrl = imageUrlField.getText().trim().isEmpty() ? "https://via.placeholder.com/100" : imageUrlField.getText().trim();

            // Check for duplicate name
            boolean nameExists = celebrities.stream()
                    .anyMatch(celeb -> celeb.getName().equalsIgnoreCase(name)); // Case-insensitive check
            if (nameExists) {
                JOptionPane.showMessageDialog(frame, "A celebrity with the name '" + name + "' already exists!", "Duplicate Name Error", JOptionPane.ERROR_MESSAGE);
            } else {
                celebrities.add(new Celebrity(name, profession, bio, "", List.of(imageUrl), (List)null));
                updateCelebrityList(celebListPanel);
            }
        } else if (result == 0) {
            JOptionPane.showMessageDialog(frame, "Name cannot be empty!", "Error", 0);
        }
    }
    private static void updateCelebrity() {
        String[] names = (String[])celebrities.stream().map(Celebrity::getName).toArray((x$0) -> {
            return new String[x$0];
        });
        if (names.length == 0) {
            JOptionPane.showMessageDialog(frame, "No celebrities to update!");
        } else {
            String selectedName = (String)JOptionPane.showInputDialog(frame, "Select Celebrity to Update:", "Update Celebrity", 3, (Icon)null, names, names[0]);
            if (selectedName != null) {
                Celebrity celebToUpdate = (Celebrity)celebrities.stream().filter((c) -> c.getName().equals(selectedName)).findFirst().orElse(null);
                if (celebToUpdate != null) {
                    JTextField nameField = new JTextField(celebToUpdate.getName(), 20);
                    JTextField professionField = new JTextField(celebToUpdate.getProfession(), 20);
                    JTextField bioField = new JTextField(celebToUpdate.getBiography(), 20);
                    JTextField imageUrlField = new JTextField(celebToUpdate.getImages() != null && !celebToUpdate.getImages().isEmpty() ? (String)celebToUpdate.getImages().get(0) : "", 20);
                    JPanel inputPanel = new JPanel(new GridLayout(4, 2));
                    inputPanel.add(new JLabel("Name:"));
                    inputPanel.add(nameField);
                    inputPanel.add(new JLabel("Profession:"));
                    inputPanel.add(professionField);
                    inputPanel.add(new JLabel("Biography:"));
                    inputPanel.add(bioField);
                    inputPanel.add(new JLabel("Image URL or Path:"));
                    inputPanel.add(imageUrlField);
                    int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Update Celebrity", 2);
                    if (result == 0 && !nameField.getText().trim().isEmpty()) {
                        String newName = nameField.getText().trim();
                        String newProfession = professionField.getText().trim();
                        String newBio = bioField.getText().trim();
                        String newImageUrl = imageUrlField.getText().trim().isEmpty() ? "https://via.placeholder.com/100" : imageUrlField.getText().trim();
                        // Create a new Celebrity object
                        Celebrity updatedCelebrity = new Celebrity(newName, newProfession, newBio, "", List.of(newImageUrl), (List)null);
                        // Find the index of the celebrity to update
                        int index = celebrities.indexOf(celebToUpdate);
                        if (index != -1) {
                            celebrities.set(index, updatedCelebrity);
                            updateCelebrityList(celebListPanel);
                        }
                    } else if (result == 0) {
                        JOptionPane.showMessageDialog(frame, "Name cannot be empty!", "Error", 0);
                    }
                }
            }
        }
    }

    private static void deleteCelebrity() {
        String[] names = (String[])celebrities.stream().map(Celebrity::getName).toArray((x$0) -> {
            return new String[x$0];
        });
        if (names.length == 0) {
            JOptionPane.showMessageDialog(frame, "No celebrities to delete!");
        } else {
            String selectedName = (String)JOptionPane.showInputDialog(frame, "Select Celebrity to Delete:", "Delete Celebrity", 3, (Icon)null, names, names[0]);
            if (selectedName != null) {
                Celebrity celebToDelete = celebrities.stream()
                        .filter((c) -> c.getName().equals(selectedName))
                        .findFirst()
                        .orElse(null); // Use null directly, which will be inferred as Celebrity
                if (celebToDelete != null) {
                    int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete " + selectedName + "?", "Confirm Delete", 0);
                    if (confirm == 0) {
                        celebrities.remove(celebToDelete);
                        updateCelebrityList(celebListPanel);
                    }
                }
            }
        }
    }

    private static List<Celebrity> getSampleCelebrities() {
        List<Celebrity> list = new ArrayList();
        list.add(new Celebrity("Leonardo DiCaprio", "Actor", "Star of Titanic and The Revenant", "Oscar Winner", List.of("https://upload.wikimedia.org/wikipedia/commoons/thumb/2/25/Leonardo_DiCaprio_2014.jpg/22320px-Leonardo_DiCaprio_20014.jpg"), (List)null));
        list.add(new Celebrity("Beyoncé", "Singer", "Global icon with multiple Grammys", "Music Legend", List.of("https://upload.wikimedia.org/wikipedia/commons/thumb/3/39/Beyonc%C3%A9_-_Golden_Globe_Awards_2020.png/220px-Beyonc%C3%A9_-_Golden_Globe_Awards_2020.png"), (List)null));
        list.add(new Celebrity("Chris Hemsworth", "Actor", "Thor in the Marvel Universe", "Action Star", List.of("https://upload.wikimedia.org/wikipedia/commons/thumb/e/e4/Chris_Hemsworth_by_Gage_Skidmore_2.jpg/220px-Chris_Hemsworth_by_Gage_Skidmore_2.jpg"), (List)null));
        list.add(new Celebrity("Taylor Swift", "Singer-Songwriter", "Pop star with record-breaking albums", "Billboard Queen", List.of("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ed/Taylor_Swift_-_Toronto_2015.jpg/220px-Taylor_Swift_-_Toronto_2015.jpg"), (List)null));
        list.add(new Celebrity("Dwayne Johnson", "Actor/Wrestler", "The Rock, action movie icon", "Box Office King", List.of("https://upload.wikimedia.org/wikipedia/commons/thumb/4/45/Dwayne_Johnson_2%2C_2013.jpg/220px-Dwayne_Johnson_2%2C_2013.jpg"), (List)null));
        return list;
    }
}