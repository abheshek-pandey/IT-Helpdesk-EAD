package com.ithelpdesk.ui;

import java.awt.*;
import javax.swing.*;
import com.ithelpdesk.model.User;
import com.ithelpdesk.service.UserService;

public class LoginFrame extends JPanel {
    private final MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorMessageLabel;
    private UserService userService;

    // Constructor with UserService only
    public LoginFrame(MainFrame mainFrame, UserService userService) {
        this.mainFrame = mainFrame;
        this.userService = userService;  // Use UserService
        initComponents();  // Initialize components
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 242, 245));

        JLabel titleLabel = new JLabel("IT Helpdesk Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(45, 52, 54));

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = createLoginButton();
        errorMessageLabel = new JLabel("");
        errorMessageLabel.setForeground(Color.RED);

        layoutComponents(titleLabel);
    }

    private JButton createLoginButton() {
        JButton button = new JButton("Login");
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(200, 40));
        button.addActionListener(e -> handleLogin());
        return button;
    }

    private void layoutComponents(JLabel titleLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = 2;

        // Title
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // Username
        gbc.gridy++;
        gbc.gridwidth = 1;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);

        // Password
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Login button
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        // Error message
        gbc.gridy++;
        add(errorMessageLabel, gbc);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Use UserService to authenticate the user
        if (userService.authenticate(username, password)) {
            User user = userService.getUserByUsername(username);  // Retrieve user info
            mainFrame.setCurrentUser(username, user.getRole());  // Set current user
            navigateToUserDashboard(user.getRole());  // Navigate to the user's dashboard
            errorMessageLabel.setText("");  // Clear error message
        } else {
            errorMessageLabel.setText("Invalid credentials. Please try again.");
        }
    }

    private void navigateToUserDashboard(String role) {
        switch (role) {
            case "Admin":
                mainFrame.showPanel("AdminDashboardFrame");
                break;
            case "Tech":
                mainFrame.showPanel("TechDashboardFrame");
                break;
            case "User":
                mainFrame.showPanel("UserDashboardFrame");
                break;
            default:
                errorMessageLabel.setText("Invalid role assigned.");
        }
    }
}
