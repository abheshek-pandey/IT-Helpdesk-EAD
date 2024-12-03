package com.ithelpdesk.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UserFormFrame extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JComboBox<String> roleDropdown;
    private JButton saveButton;

    public UserFormFrame() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));

        // Header
        JLabel headerLabel = new JLabel("User Form");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(new Color(240, 242, 245));

        // Form Fields
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        emailField = new JTextField();
        roleDropdown = new JComboBox<>(new String[]{"Admin", "Tech", "User"});

        // Tooltips for better UX
        usernameField.setToolTipText("Enter username");
        passwordField.setToolTipText("Enter password");
        emailField.setToolTipText("Enter email");
        roleDropdown.setToolTipText("Select user role");

        // Add form fields to the form panel
        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleDropdown);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 242, 245));
        saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(new Color(46, 204, 113));  // Green for Save
        saveButton.setForeground(Color.WHITE);

        buttonPanel.add(saveButton);

        // Add components to the main panel
        add(headerLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getSelectedRole() {
        return (String) roleDropdown.getSelectedItem();
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public void setButtonActionListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }
}
