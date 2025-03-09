package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Celebrity;

public class CelebrityPanel {
    public static JPanel createCelebrityPanel(Celebrity celeb) {
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
        panel.add(imageLabel, BorderLayout.EAST);

        return panel;
    }
}