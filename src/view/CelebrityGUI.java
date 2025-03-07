package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    private static List<Celebrity> celebrities = new ArrayList<>();
    private static Map<String, String> users = new HashMap<>(); // In-memory username:password storage
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
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 600);

            // Show initial selection panel
            showSelectionPanel();

            frame.setVisible(true);
        });
    }

    private static void showSelectionPanel() {
        JPanel selectionPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton signInButton = new JButton("Sign In");
        JButton signUpButton = new JButton("Sign Up");

        selectionPanel.add(signInButton);
        selectionPanel.add(signUpButton);

        frame.getContentPane().removeAll();
        frame.add(selectionPanel);
        frame.revalidate();
        frame.repaint();

        // Sign-in action
        signInButton.addActionListener(e -> showSignInPanel());

        // Sign-up action
        signUpButton.addActionListener(e -> showSignUpPanel());
    }

    private static void showSignInPanel() {
        JPanel signInPanel = new JPanel(new BorderLayout(10, 10));
        signInPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Center panel for username and password fields and sign-in button
        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField usernameField = new JTextField(10); // Smaller size
        JPasswordField passwordField = new JPasswordField(10); // Smaller size
        JButton signInButton = new JButton("Sign In");

        centerPanel.add(new JLabel("Username:"));
        centerPanel.add(usernameField);
        centerPanel.add(new JLabel("Password:"));
        centerPanel.add(passwordField);
        centerPanel.add(new JLabel("")); // Empty space for alignment
        centerPanel.add(signInButton);

        // South panel for the back button (bottom left)
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 30)); // Smaller size
        southPanel.add(backButton);

        signInPanel.add(centerPanel, BorderLayout.CENTER);
        signInPanel.add(southPanel, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.add(signInPanel);
        frame.revalidate();
        frame.repaint();

        signInButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                if (users.containsKey(username) && users.get(username).equals(password)) {
                    isSignedIn = true;
                    signInTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    showMainContent();
                    JOptionPane.showMessageDialog(frame, "Signed in at: " + signInTime);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> showSelectionPanel());
    }

    private static void showSignUpPanel() {
        JPanel signUpPanel = new JPanel(new BorderLayout(10, 10));
        signUpPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Center panel for username and password fields and register button
        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField newUsernameField = new JTextField(10); // Smaller size
        JPasswordField newPasswordField = new JPasswordField(10); // Smaller size
        JButton registerButton = new JButton("Register");

        centerPanel.add(new JLabel("New Username:"));
        centerPanel.add(newUsernameField);
        centerPanel.add(new JLabel("New Password:"));
        centerPanel.add(newPasswordField);
        centerPanel.add(new JLabel("")); // Empty space for alignment
        centerPanel.add(registerButton);

        // South panel for the back button (bottom left)
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 30)); // Smaller size
        southPanel.add(backButton);

        signUpPanel.add(centerPanel, BorderLayout.CENTER);
        signUpPanel.add(southPanel, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.add(signUpPanel);
        frame.revalidate();
        frame.repaint();

        registerButton.addActionListener(e -> {
            String username = newUsernameField.getText().trim();
            String password = new String(newPasswordField.getPassword()).trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                if (users.containsKey(username)) {
                    JOptionPane.showMessageDialog(frame, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    users.put(username, password);
                    JOptionPane.showMessageDialog(frame, "Sign-up successful! Please sign in.");
                    showSelectionPanel(); // Return to selection panel after sign-up
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> showSelectionPanel());
    }

    private static void showMainContent() {
        celebrities = getSampleCelebrities();
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.LIGHT_GRAY);

        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton insertButton = new JButton("Insert Celebrity");
        JButton updateButton = new JButton("Update Celebrity");
        JButton deleteButton = new JButton("Delete Celebrity");
        JButton signOutButton = new JButton("Sign Out");

        controlPanel.add(insertButton);
        controlPanel.add(updateButton);
        controlPanel.add(deleteButton);
        controlPanel.add(signOutButton);

        celebListPanel = new JPanel();
        celebListPanel.setLayout(new BoxLayout(celebListPanel, BoxLayout.Y_AXIS));
        updateCelebrityList(celebListPanel);

        JScrollPane scrollPane = new JScrollPane(celebListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        insertButton.addActionListener(e -> insertCelebrity());
        updateButton.addActionListener(e -> updateCelebrity());
        deleteButton.addActionListener(e -> deleteCelebrity());
        signOutButton.addActionListener(e -> signOut());

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
            showSelectionPanel(); // Return to selection panel after sign-out
        }
    }

    private static void updateCelebrityList(JPanel panel) {
        panel.removeAll();
        for (Celebrity celeb : celebrities) {
            JPanel celebPanel = createCelebrityPanel(celeb);
            panel.add(celebPanel);
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        panel.revalidate();
        panel.repaint();
    }

    private static JPanel createCelebrityPanel(Celebrity celeb) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setMaximumSize(new Dimension(650, 120));
        JLabel textLabel = new JLabel("<html><b>" + celeb.getName() + "</b><br>Profession: " + celeb.getProfession() + "<br>Bio: " + celeb.getBiography() + "</html>");
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        textLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(textLabel, BorderLayout.CENTER);

        JLabel imageLabel;
        if (celeb.getImages() != null && !celeb.getImages().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(new URL(celeb.getImages().get(0)));
                Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imageLabel = new JLabel(new ImageIcon(scaledImage));
            } catch (Exception e) {
                imageLabel = new JLabel("Image N/A");
                System.err.println("Failed to load image for " + celeb.getName() + ": " + e.getMessage());
            }
        } else {
            imageLabel = new JLabel("No Image");
        }
        imageLabel.setPreferredSize(new Dimension(100, 100));
        panel.add(imageLabel, BorderLayout.EAST);
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
        int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Insert Celebrity", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION && !nameField.getText().trim().isEmpty()) {
            String name = nameField.getText().trim();
            String profession = professionField.getText().trim();
            String bio = bioField.getText().trim();
            String imageUrl = imageUrlField.getText().trim().isEmpty() ? "https://via.placeholder.com/100" : imageUrlField.getText().trim();

            boolean nameExists = celebrities.stream().anyMatch(celeb -> celeb.getName().equalsIgnoreCase(name));
            if (nameExists) {
                JOptionPane.showMessageDialog(frame, "A celebrity with the name '" + name + "' already exists!", "Duplicate Name Error", JOptionPane.ERROR_MESSAGE);
            } else {
                celebrities.add(new Celebrity(name, profession, bio, "", List.of(imageUrl), null));
                updateCelebrityList(celebListPanel);
            }
        } else if (result == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(frame, "Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void updateCelebrity() {
        String[] names = celebrities.stream().map(Celebrity::getName).toArray(String[]::new);
        if (names.length == 0) {
            JOptionPane.showMessageDialog(frame, "No celebrities to update!");
        } else {
            String selectedName = (String) JOptionPane.showInputDialog(frame, "Select Celebrity to Update:", "Update Celebrity", JOptionPane.QUESTION_MESSAGE, null, names, names[0]);
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
                    int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Update Celebrity", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION && !nameField.getText().trim().isEmpty()) {
                        String newName = nameField.getText().trim();
                        String newProfession = professionField.getText().trim();
                        String newBio = bioField.getText().trim();
                        String newImageUrl = imageUrlField.getText().trim().isEmpty() ? "https://via.placeholder.com/100" : imageUrlField.getText().trim();
                        Celebrity updatedCelebrity = new Celebrity(newName, newProfession, newBio, "", List.of(newImageUrl), null);
                        int index = celebrities.indexOf(celebToUpdate);
                        if (index != -1) {
                            celebrities.set(index, updatedCelebrity);
                            updateCelebrityList(celebListPanel);
                        }
                    } else if (result == JOptionPane.OK_OPTION) {
                        JOptionPane.showMessageDialog(frame, "Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private static void deleteCelebrity() {
        String[] names = celebrities.stream().map(Celebrity::getName).toArray(String[]::new);
        if (names.length == 0) {
            JOptionPane.showMessageDialog(frame, "No celebrities to delete!");
        } else {
            String selectedName = (String) JOptionPane.showInputDialog(frame, "Select Celebrity to Delete:", "Delete Celebrity", JOptionPane.QUESTION_MESSAGE, null, names, names[0]);
            if (selectedName != null) {
                Celebrity celebToDelete = celebrities.stream().filter(c -> c.getName().equals(selectedName)).findFirst().orElse(null);
                if (celebToDelete != null) {
                    int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete " + selectedName + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        celebrities.remove(celebToDelete);
                        updateCelebrityList(celebListPanel);
                    }
                }
            }
        }
    }

    private static List<Celebrity> getSampleCelebrities() {
        List<Celebrity> list = new ArrayList<>();
        String placeholderImage = "https://via.placeholder.com/100";
        list.add(new Celebrity("Leonardo DiCaprio", "Actor", "Star of Titanic and The Revenant", "Oscar Winner", List.of(placeholderImage), null));
        list.add(new Celebrity("Beyoncé", "Singer", "Global icon with multiple Grammys", "Music Legend", List.of(placeholderImage), null));
        list.add(new Celebrity("Chris Hemsworth", "Actor", "Thor in the Marvel Universe", "Action Star", List.of(placeholderImage), null));
        list.add(new Celebrity("Taylor Swift", "Singer-Songwriter", "Pop star with record-breaking albums", "Billboard Queen", List.of(placeholderImage), null));
        list.add(new Celebrity("Dwayne Johnson", "Actor/Wrestler", "The Rock, action movie icon", "Box Office King", List.of(placeholderImage), null));
        list.add(new Celebrity("Tom Hanks", "Actor", "Known for Forrest Gump and Cast Away", "Academy Award Winner", List.of(placeholderImage), null));
        list.add(new Celebrity("Lady Gaga", "Singer/Actress", "Famous for A Star is Born", "Pop Icon", List.of(placeholderImage), null));
        list.add(new Celebrity("Robert Downey Jr.", "Actor", "Iconic as Iron Man", "Marvel Legend", List.of(placeholderImage), null));
        list.add(new Celebrity("Adele", "Singer", "Known for soulful ballads", "Grammy Winner", List.of(placeholderImage), null));
        list.add(new Celebrity("Scarlett Johansson", "Actress", "Black Widow in MCU", "Hollywood Star", List.of(placeholderImage), null));
        list.add(new Celebrity("Ed Sheeran", "Singer-Songwriter", "Hitmaker of Shape of You", "Music Sensation", List.of(placeholderImage), null));
        list.add(new Celebrity("Brad Pitt", "Actor", "Star of Fight Club", "Oscar Nominee", List.of(placeholderImage), null));
        list.add(new Celebrity("Rihanna", "Singer/Businesswoman", "Fenty Beauty founder", "Pop Royalty", List.of(placeholderImage), null));
        list.add(new Celebrity("Chris Pratt", "Actor", "Star of Guardians of the Galaxy", "Action Hero", List.of(placeholderImage), null));
        list.add(new Celebrity("Ariana Grande", "Singer", "Known for Thank U, Next", "Pop Princess", List.of(placeholderImage), null));
        list.add(new Celebrity("Johnny Depp", "Actor", "Iconic as Captain Jack Sparrow", "Versatile Performer", List.of(placeholderImage), null));
        list.add(new Celebrity("Katy Perry", "Singer", "Known for Firework", "Pop Star", List.of(placeholderImage), null));
        list.add(new Celebrity("Will Smith", "Actor/Rapper", "Fresh Prince and Men in Black", "Hollywood Icon", List.of(placeholderImage), null));
        return list;
    }
}