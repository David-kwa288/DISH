package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import controller.CelebrityController;

public class CelebrityGUI {
    private JFrame frame;
    private JPanel celebListPanel;
    private JPanel mainPanel;
    private JPanel sidebarPanel;
    private CelebrityController controller;
    private JTextField searchField;
    private JComboBox<String> professionFilter;
    private JComboBox<String> awardsFilter;

    private static final Color PRIMARY_COLOR = new Color(70, 130, 255);
    private static final Color SECONDARY_COLOR = new Color(255, 90, 120);
    private static final Color BACKGROUND_COLOR = new Color(240, 245, 255);
    private static final Color ACCENT_COLOR = new Color(255, 255, 255);
    private static final Color TEXT_COLOR = new Color(30, 30, 50);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 22);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 16);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final int PADDING = 15;

    public CelebrityGUI(CelebrityController controller) {
        this.controller = controller;
        frame = new JFrame("Celebrity Catalog");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        initializeSidebar();
    }

    public void show() {
        frame.setVisible(true);
        showLoginPanel();
    }

    private void initializeSidebar() {
        sidebarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_COLOR, 0, getHeight(), SECONDARY_COLOR);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setPreferredSize(new Dimension(200, frame.getHeight()));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        JLabel appTitle = new JLabel("Celeb Catalog");
        appTitle.setFont(TITLE_FONT);
        appTitle.setForeground(ACCENT_COLOR);
        appTitle.setHorizontalAlignment(JLabel.CENTER);
        sidebarPanel.add(appTitle, BorderLayout.NORTH);

        frame.add(sidebarPanel, BorderLayout.WEST);
    }

    public void updateSidebarButtons(String role) {
        sidebarPanel.removeAll();

        JLabel appTitle = new JLabel("Celeb Catalog");
        appTitle.setFont(TITLE_FONT);
        appTitle.setForeground(ACCENT_COLOR);
        appTitle.setHorizontalAlignment(JLabel.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        if (role != null) {
            buttonPanel.add(createSidebarButton("Search Celebrity", e -> {
                if (searchField != null) searchField.requestFocus();
            }));
            buttonPanel.add(Box.createVerticalStrut(10));
            if (!"guest".equals(role)) {
                buttonPanel.add(createSidebarButton("Favorites", e -> controller.showFavorites()));
                buttonPanel.add(Box.createVerticalStrut(10));
            }
            if ("admin".equals(role)) {
                buttonPanel.add(createSidebarButton("Add Celebrity", e -> controller.insertCelebrity()));
                buttonPanel.add(Box.createVerticalStrut(10));
                buttonPanel.add(createSidebarButton("Edit Celebrity", e -> controller.updateCelebrity()));
                buttonPanel.add(Box.createVerticalStrut(10));
                buttonPanel.add(createSidebarButton("Remove Celebrity", e -> controller.deleteCelebrity()));
                buttonPanel.add(Box.createVerticalStrut(20));
            }
            buttonPanel.add(createSidebarButton("Sign Out", e -> controller.signOut()));
        } else {
            buttonPanel.add(createSidebarButton("Sign In", e -> controller.showSignInPanel(false)));
            buttonPanel.add(Box.createVerticalStrut(10));
            buttonPanel.add(createSidebarButton("Sign Up", e -> controller.showSignUpPanel()));
            buttonPanel.add(Box.createVerticalStrut(10));
            buttonPanel.add(createSidebarButton("Admin Sign In", e -> controller.showSignInPanel(true)));
            buttonPanel.add(Box.createVerticalStrut(10));
            buttonPanel.add(createSidebarButton("Guest Sign In", e -> controller.handleGuestSignIn()));
        }

        sidebarPanel.add(appTitle, BorderLayout.NORTH);
        sidebarPanel.add(buttonPanel, BorderLayout.CENTER);
        sidebarPanel.revalidate();
        sidebarPanel.repaint();
    }

    private JButton createSidebarButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_COLOR);
        button.setBackground(ACCENT_COLOR);
        button.setMaximumSize(new Dimension(180, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.addActionListener(action);
        return button;
    }

    public void showLoginPanel() {
        updateSidebarButtons(null);
        JPanel loginPanel = createAuthPanel(false, false);
        updateMainContent(loginPanel);
    }

    public void showSignInPanel(boolean isAdmin) {
        updateSidebarButtons(null);
        JPanel signInPanel = createAuthPanel(isAdmin, false);
        updateMainContent(signInPanel);
    }

    public void showSignUpPanel() {
        updateSidebarButtons(null);
        JPanel signUpPanel = createAuthPanel(false, true);
        updateMainContent(signUpPanel);
    }

    private JPanel createAuthPanel(boolean isAdmin, boolean isSignUp) {
        JPanel authPanel = new JPanel(new GridBagLayout());
        authPanel.setBackground(BACKGROUND_COLOR);
        authPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel(isSignUp ? "Create Account" : (isAdmin ? "Admin Login" : "Sign In"));
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        authPanel.add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel(isSignUp ? "Username:" : "Username:");
        usernameLabel.setFont(LABEL_FONT);
        usernameLabel.setForeground(TEXT_COLOR);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        authPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        usernameField.setToolTipText(isSignUp ? "Choose a username" : "Enter your username");
        usernameField.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR, 1));
        gbc.gridx = 1;
        authPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel(isSignUp ? "Password:" : "Password:");
        passwordLabel.setFont(LABEL_FONT);
        passwordLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 2;
        authPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setToolTipText(isSignUp ? "Choose a password" : "Enter your password");
        passwordField.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR, 1));
        gbc.gridx = 1;
        authPanel.add(passwordField, gbc);

        JButton actionButton = new JButton(isSignUp ? "Sign Up" : "Sign In") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        actionButton.setFont(BUTTON_FONT);
        actionButton.setBackground(SECONDARY_COLOR);
        actionButton.setForeground(ACCENT_COLOR);
        actionButton.setOpaque(false);
        actionButton.setContentAreaFilled(false);
        actionButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        actionButton.addActionListener(e -> {
            if (isSignUp) {
                controller.handleSignUp(usernameField.getText(), new String(passwordField.getPassword()));
            } else {
                controller.handleSignIn(usernameField.getText(), new String(passwordField.getPassword()), isAdmin);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        authPanel.add(actionButton, gbc);

        return authPanel;
    }

    public void showMainContent(String role) {
        updateSidebarButtons(role);
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        JLabel headerLabel = new JLabel("Celebrity Catalog");
        headerLabel.setFont(TITLE_FONT);
        headerLabel.setForeground(TEXT_COLOR);
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, PADDING, 0));

        JPanel searchAndFilterPanel = new JPanel(new GridBagLayout());
        searchAndFilterPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        searchField = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GRAY);
                g.drawString("🔍", 5, getHeight() / 2 + 5); // Magnifying glass emoji as fallback
            }
        };
        searchField.setToolTipText("Search by name");
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
                BorderFactory.createEmptyBorder(0, 25, 0, 0)
        ));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                applyFilters();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        searchAndFilterPanel.add(searchField, gbc);

        String[] professions = {"All", "Actor", "Actress", "Singer", "Rapper", "Singer-Songwriter", "Actor/Wrestler", "Athlete"};
        professionFilter = new JComboBox<>(professions);
        professionFilter.setToolTipText("Filter by profession");
        professionFilter.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR, 1));
        professionFilter.addActionListener(e -> applyFilters());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        searchAndFilterPanel.add(professionFilter, gbc);

        String[] awards = {"All", "Oscar Winner", "Grammy Winner", "Billboard Queen", "Box Office King", "None"};
        awardsFilter = new JComboBox<>(awards);
        awardsFilter.setToolTipText("Filter by awards");
        awardsFilter.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR, 1));
        awardsFilter.addActionListener(e -> applyFilters());
        gbc.gridx = 2;
        gbc.gridy = 0;
        searchAndFilterPanel.add(awardsFilter, gbc);

        JButton homeButton = new JButton("\uD83C\uDFE0") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        homeButton.setFont(BUTTON_FONT);
        homeButton.setBackground(SECONDARY_COLOR);
        homeButton.setForeground(ACCENT_COLOR);
        homeButton.setOpaque(false);
        homeButton.setContentAreaFilled(false);
        homeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        homeButton.addActionListener(e -> {
            searchField.setText("");
            professionFilter.setSelectedItem("All");
            awardsFilter.setSelectedItem("All");
            controller.filterCelebrities("", "All", "All");
        });
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        searchAndFilterPanel.add(homeButton, gbc);

        searchPanel.add(headerLabel, BorderLayout.NORTH);
        searchPanel.add(searchAndFilterPanel, BorderLayout.CENTER);
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        celebListPanel = new JPanel();
        celebListPanel.setLayout(new BoxLayout(celebListPanel, BoxLayout.Y_AXIS));
        celebListPanel.setBackground(BACKGROUND_COLOR);
        controller.updateCelebrityList(celebListPanel);

        JScrollPane scrollPane = new JScrollPane(celebListPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        updateMainContent(mainPanel);
    }

    private void applyFilters() {
        String nameSearch = searchField.getText().trim();
        String profession = (String) professionFilter.getSelectedItem();
        String award = (String) awardsFilter.getSelectedItem();
        controller.filterCelebrities(nameSearch, profession, award);
    }

    public void updateMainContent(JPanel panel) {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(sidebarPanel, BorderLayout.WEST);
        mainPanel = panel;
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getCelebListPanel() {
        return celebListPanel;
    }
}