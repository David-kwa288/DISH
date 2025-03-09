package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import controller.CelebrityController;

public class CelebrityGUI {
    private JFrame frame;
    private JPanel celebListPanel; // Already private
    private JPanel mainPanel;
    private CelebrityController controller;

    public CelebrityGUI(CelebrityController controller) {
        this.controller = controller;
        frame = new JFrame("Celebrities Catalog - Week 1 Progress");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
    }

    public void show() {
        frame.setVisible(true);
        showSelectionPanel();
    }

    public void showSelectionPanel() {
        JPanel selectionPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton signInButton = new JButton("Sign In (Users)");
        JButton signUpButton = new JButton("Sign Up (Users)");
        JButton adminSignInButton = new JButton("Admin Sign In");

        selectionPanel.add(signInButton);
        selectionPanel.add(signUpButton);
        selectionPanel.add(adminSignInButton);

        signInButton.addActionListener(e -> controller.showSignInPanel(false));
        signUpButton.addActionListener(e -> controller.showSignUpPanel());
        adminSignInButton.addActionListener(e -> controller.showSignInPanel(true));

        updateFrame(selectionPanel);
    }

    public JPanel createSignInPanel(boolean isAdmin) {
        JPanel signInPanel = new JPanel(new BorderLayout(10, 10));
        signInPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        JButton signInButton = new JButton("Sign In");

        centerPanel.add(new JLabel("Username:"));
        centerPanel.add(usernameField);
        centerPanel.add(new JLabel("Password:"));
        centerPanel.add(passwordField);
        centerPanel.add(new JLabel(""));
        centerPanel.add(signInButton);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 30));
        southPanel.add(backButton);

        JLabel titleLabel = new JLabel(isAdmin ? "Admin Login" : "User Login");
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        signInPanel.add(titleLabel, BorderLayout.NORTH);

        signInPanel.add(centerPanel, BorderLayout.CENTER);
        signInPanel.add(southPanel, BorderLayout.SOUTH);

        signInButton.addActionListener(e -> controller.handleSignIn(usernameField.getText(), new String(passwordField.getPassword()), isAdmin));
        backButton.addActionListener(e -> controller.showSelectionPanel());

        return signInPanel;
    }

    public JPanel createSignUpPanel() {
        JPanel signUpPanel = new JPanel(new BorderLayout(10, 10));
        signUpPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField newUsernameField = new JTextField(10);
        JPasswordField newPasswordField = new JPasswordField(10);
        JButton registerButton = new JButton("Register");

        centerPanel.add(new JLabel("New Username:"));
        centerPanel.add(newUsernameField);
        centerPanel.add(new JLabel("New Password:"));
        centerPanel.add(newPasswordField);
        centerPanel.add(new JLabel(""));
        centerPanel.add(registerButton);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 30));
        southPanel.add(backButton);

        signUpPanel.add(centerPanel, BorderLayout.CENTER);
        signUpPanel.add(southPanel, BorderLayout.SOUTH);

        registerButton.addActionListener(e -> controller.handleSignUp(newUsernameField.getText(), new String(newPasswordField.getPassword())));
        backButton.addActionListener(e -> controller.showSelectionPanel());

        return signUpPanel;
    }

    public void showMainContent(String role) {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.LIGHT_GRAY);

        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton signOutButton = new JButton("Sign Out");

        if ("admin".equals(role)) {
            JButton insertButton = new JButton("Insert Celebrity");
            JButton updateButton = new JButton("Update Celebrity");
            JButton deleteButton = new JButton("Delete Celebrity");
            controlPanel.add(insertButton);
            controlPanel.add(updateButton);
            controlPanel.add(deleteButton);
            insertButton.addActionListener(e -> controller.insertCelebrity());
            updateButton.addActionListener(e -> controller.updateCelebrity());
            deleteButton.addActionListener(e -> controller.deleteCelebrity());
        }

        controlPanel.add(signOutButton);
        signOutButton.addActionListener(e -> controller.signOut());

        celebListPanel = new JPanel();
        celebListPanel.setLayout(new BoxLayout(celebListPanel, BoxLayout.Y_AXIS));
        controller.updateCelebrityList(celebListPanel);

        JScrollPane scrollPane = new JScrollPane(celebListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        updateFrame(mainPanel);
    }

    public void updateFrame(JPanel panel) {
        frame.getContentPane().removeAll();
        frame.add(panel);
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