package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Celebrity;
import controller.CelebrityController;

public class CelebrityPanel {
    private static final Color CARD_COLOR_1 = new Color(255, 245, 245); // Light pink
    private static final Color CARD_COLOR_2 = new Color(245, 245, 255); // Light blue
    private static final Color TEXT_COLOR = new Color(30, 30, 50); // Dark gray
    private static int cardCounter = 0;

    public static JPanel createCelebrityPanel(Celebrity celeb, CelebrityController controller) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setMaximumSize(new Dimension(650, 120));
        panel.setBackground(cardCounter % 2 == 0 ? CARD_COLOR_1 : CARD_COLOR_2);
        cardCounter++;

        JLabel textLabel = new JLabel("<html><b>" + celeb.getName() + "</b><br>Profession: " + celeb.getProfession() + "<br>Bio: " + celeb.getBiography() + "</html>");
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textLabel.setForeground(TEXT_COLOR);
        textLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(textLabel, BorderLayout.CENTER);

        JLabel imageLabel;
        if (celeb.getImages() != null && !celeb.getImages().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(new URL(celeb.getImages().get(0)));
                java.awt.Image scaledImage = icon.getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
                imageLabel = new JLabel(new ImageIcon(scaledImage));
            } catch (Exception e) {
                imageLabel = new JLabel("Image N/A");
                System.err.println("Failed to load image for " + celeb.getName() + ": " + e.getMessage());
            }
        } else {
            imageLabel = new JLabel("No Image");
        }
        imageLabel.setPreferredSize(new Dimension(100, 100));
        panel.add(imageLabel, BorderLayout.WEST);

        // Make panel clickable
        panel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                controller.showCelebrityDetails(celeb);
            }
        });

        return panel;
    }
}