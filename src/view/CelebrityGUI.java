package view;

import model.Celebrity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CelebrityGUI {
    private static List<Celebrity> celebrities = new ArrayList<>();
    private static JFrame frame; // Store the frame reference globally
    private static JPanel celebListPanel; // Store the celebrity list panel reference

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize with sample data
            celebrities = getSampleCelebrities();

            // Create the main frame
            frame = new JFrame("Celebrities Catalog - Week 1 Progress");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 600);

            // Main panel with scrolling and controls
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(Color.LIGHT_GRAY);

            // Top panel for controls (Insert, Update, Delete)
            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new FlowLayout());
            JButton insertButton = new JButton("Insert Celebrity");
            JButton updateButton = new JButton("Update Celebrity");
            JButton deleteButton = new JButton("Delete Celebrity");
            controlPanel.add(insertButton);
            controlPanel.add(updateButton);
            controlPanel.add(deleteButton);

            // Celebrity list panel (scrollable)
            celebListPanel = new JPanel();
            celebListPanel.setLayout(new BoxLayout(celebListPanel, BoxLayout.Y_AXIS));
            updateCelebrityList(celebListPanel);

            JScrollPane scrollPane = new JScrollPane(celebListPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            mainPanel.add(controlPanel, BorderLayout.NORTH);
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            // Action listeners for buttons
            insertButton.addActionListener(e -> insertCelebrity());
            updateButton.addActionListener(e -> updateCelebrity());
            deleteButton.addActionListener(e -> deleteCelebrity());

            frame.add(mainPanel);
            frame.setVisible(true);
        });
    }

    private static void updateCelebrityList(JPanel panel) {
        panel.removeAll(); // Clear existing content
        for (Celebrity celeb : celebrities) {
            JPanel celebPanel = createCelebrityPanel(celeb);
            panel.add(celebPanel);
            panel.add(Box.createRigidArea(new Dimension(0, 15))); // Spacing
        }
        panel.revalidate();
        panel.repaint();
    }

    private static JPanel createCelebrityPanel(Celebrity celeb) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setMaximumSize(new Dimension(650, 120));

        // Text details on the left
        JLabel textLabel = new JLabel(
                "<html><b>" + celeb.getName() + "</b><br>" +
                        "Profession: " + celeb.getProfession() + "<br>" +
                        "Bio: " + celeb.getBiography() + "</html>"
        );
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        textLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(textLabel, BorderLayout.CENTER);

        // Image on the right
        JLabel imageLabel;
        if (celeb.getImages() != null && !celeb.getImages().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(new java.net.URL(celeb.getImages().get(0)));
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
        // Simple dialog for inserting a new celebrity
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
            celebrities.add(new Celebrity(name, profession, bio, "", List.of(imageUrl), null));
            updateCelebrityList(celebListPanel); // Use the stored celebListPanel reference
        } else if (result == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(frame, "Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void updateCelebrity() {
        // Simple dialog for selecting and updating a celebrity
        String[] names = celebrities.stream().map(Celebrity::getName).toArray(String[]::new);
        if (names.length == 0) {
            JOptionPane.showMessageDialog(frame, "No celebrities to update!");
            return;
        }

        String selectedName = (String) JOptionPane.showInputDialog(frame, "Select Celebrity to Update:", "Update Celebrity",
                JOptionPane.QUESTION_MESSAGE, null, names, names[0]);

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
                    celebToUpdate = new Celebrity(
                            nameField.getText().trim(), professionField.getText().trim(), bioField.getText().trim(), "",
                            List.of(imageUrlField.getText().trim().isEmpty() ? "https://via.placeholder.com/100" : imageUrlField.getText().trim()), null
                    );
                    int index = celebrities.indexOf(celebrities.stream().filter(c -> c.getName().equals(selectedName)).findFirst().get());
                    celebrities.set(index, celebToUpdate);
                    updateCelebrityList(celebListPanel); // Use the stored celebListPanel reference
                } else if (result == JOptionPane.OK_OPTION) {
                    JOptionPane.showMessageDialog(frame, "Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private static void deleteCelebrity() {
        // Simple dialog for deleting a celebrity
        String[] names = celebrities.stream().map(Celebrity::getName).toArray(String[]::new);
        if (names.length == 0) {
            JOptionPane.showMessageDialog(frame, "No celebrities to delete!");
            return;
        }

        String selectedName = (String) JOptionPane.showInputDialog(frame, "Select Celebrity to Delete:", "Delete Celebrity",
                JOptionPane.QUESTION_MESSAGE, null, names, names[0]);

        if (selectedName != null) {
            Celebrity celebToDelete = celebrities.stream().filter(c -> c.getName().equals(selectedName)).findFirst().orElse(null);
            if (celebToDelete != null) {
                int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete " + selectedName + "?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    celebrities.remove(celebToDelete);
                    updateCelebrityList(celebListPanel); // Use the stored celebListPanel reference
                }
            }
        }
    }

    private static List<Celebrity> getSampleCelebrities() {
        List<Celebrity> list = new ArrayList<>();
        // Replace these URLs with real celebrity image URLs or local paths
        list.add(new Celebrity("Leonardo DiCaprio", "Actor", "Star of Titanic and The Revenant", "Oscar Winner",
                List.of("https://upload.wikimedia.org/wikipedia/commoons/thumb/2/25/Leonardo_DiCaprio_2014.jpg/22320px-Leonardo_DiCaprio_20014.jpg"), null));
        list.add(new Celebrity("Beyoncé", "Singer", "Global icon with multiple Grammys", "Music Legend",
                List.of("https://upload.wikimedia.org/wikipedia/commons/thumb/3/39/Beyonc%C3%A9_-_Golden_Globe_Awards_2020.png/220px-Beyonc%C3%A9_-_Golden_Globe_Awards_2020.png"), null));
        list.add(new Celebrity("Chris Hemsworth", "Actor", "Thor in the Marvel Universe", "Action Star",
                List.of("https://upload.wikimedia.org/wikipedia/commons/thumb/e/e4/Chris_Hemsworth_by_Gage_Skidmore_2.jpg/220px-Chris_Hemsworth_by_Gage_Skidmore_2.jpg"), null));
        list.add(new Celebrity("Taylor Swift", "Singer-Songwriter", "Pop star with record-breaking albums", "Billboard Queen",
                List.of("https://upload.wikimedia.org/wikipedia/commons/thumb/e/ed/Taylor_Swift_-_Toronto_2015.jpg/220px-Taylor_Swift_-_Toronto_2015.jpg"), null));
        list.add(new Celebrity("Dwayne Johnson", "Actor/Wrestler", "The Rock, action movie icon", "Box Office King",
                List.of("https://upload.wikimedia.org/wikipedia/commons/thumb/4/45/Dwayne_Johnson_2%2C_2013.jpg/220px-Dwayne_Johnson_2%2C_2013.jpg"), null));
        return list; // Start with a small list for Week 1
    }
}