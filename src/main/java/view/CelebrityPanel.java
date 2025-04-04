package view;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import model.Celebrity;
import controller.CelebrityController;

public class CelebrityPanel {
    private static final Color CARD_COLOR_1 = new Color(255, 245, 245);
    private static final Color CARD_COLOR_2 = new Color(245, 245, 255);
    private static final Color TEXT_COLOR = new Color(30, 30, 50);
    private static int cardCounter = 0;

    public static JPanel createCelebrityPanel(Celebrity celeb, CelebrityController controller) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setMaximumSize(new Dimension(650, 150));
        panel.setBackground(cardCounter % 2 == 0 ? CARD_COLOR_1 : CARD_COLOR_2);
        cardCounter++;

        JLabel imageLabel;
        if (celeb.getImages() != null && !celeb.getImages().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(new URL(celeb.getImages().get(0)));
                java.awt.Image scaledImage = icon.getImage().getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
                imageLabel = new JLabel(new ImageIcon(scaledImage));
            } catch (Exception e) {
                imageLabel = new JLabel("Image N/A");
                System.err.println("Failed to load image for " + celeb.getName() + ": " + e.getMessage());
            }
        } else {
            imageLabel = new JLabel("No Image");
        }
        imageLabel.setPreferredSize(new Dimension(80, 80));
        panel.add(imageLabel, BorderLayout.WEST);

        JLabel textLabel = new JLabel("<html><b>" + celeb.getName() + "</b><br>Profession: " + celeb.getProfession() + "<br>Bio: " + celeb.getBiography() + "</html>");
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textLabel.setForeground(TEXT_COLOR);
        textLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(textLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton favButton = null;
        if (!"Guest".equals(controller.getSignedInUser())) {
            favButton = new JButton("★");
            favButton.setFont(new Font("SansSerif", Font.BOLD, 16));
            favButton.setForeground(controller.getFavorites().contains(celeb) ? Color.YELLOW : TEXT_COLOR);
            favButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            favButton.setContentAreaFilled(false);
            favButton.addActionListener(e -> {
                if (controller.getFavorites().contains(celeb)) {
                    controller.removeFromFavorites(celeb);
                    ((JButton)e.getSource()).setForeground(TEXT_COLOR);
                } else {
                    controller.addToFavorites(celeb);
                    ((JButton)e.getSource()).setForeground(Color.YELLOW);
                }
            });
            buttonPanel.add(favButton);
        }

        JButton editButton = null;
        JButton deleteButton = null;
        if ("admin".equals(controller.getRole())) {
            editButton = new JButton("✎");
            editButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
            editButton.addActionListener(e -> controller.updateCelebrity(celeb));
            buttonPanel.add(editButton);

            deleteButton = new JButton("🗑");
            deleteButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
            deleteButton.addActionListener(e -> controller.deleteCelebrity(celeb));
            buttonPanel.add(deleteButton);
        }

        panel.add(buttonPanel, BorderLayout.EAST);

        panel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        JButton finalFavButton = favButton;
        JButton finalEditButton = editButton;
        JButton finalDeleteButton = deleteButton;
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getComponent() != finalFavButton && e.getComponent() != finalEditButton && e.getComponent() != finalDeleteButton) {
                    controller.showCelebrityDetails(celeb);
                }
            }
        });

        return panel;
    }
}