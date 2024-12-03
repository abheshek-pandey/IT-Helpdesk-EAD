package com.ithelpdesk.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboardFrame extends JPanel {
    private final MainFrame mainFrame;
    private JButton manageUsersButton;
    private JButton viewTicketsButton;
    private JButton metricsButton;
    private JButton knowledgeBaseButton; // New button for Knowledge Base

    public AdminDashboardFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 242, 245));

        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(45, 52, 54));

        manageUsersButton = createStyledButton("Manage Users");
        viewTicketsButton = createStyledButton("View Tickets");
        metricsButton = createStyledButton("View Metrics");
        knowledgeBaseButton = createStyledButton("Knowledge Base"); // New button

        setupActionListeners();
        layoutComponents(titleLabel);
    }

    private void setupActionListeners() {
        manageUsersButton.addActionListener(e -> mainFrame.showPanel("ManageUsersFrame"));
        viewTicketsButton.addActionListener(e -> mainFrame.showPanel("AdminTicketManagementFrame"));
        metricsButton.addActionListener(e -> mainFrame.showPanel("MetricsFrame"));
        knowledgeBaseButton.addActionListener(e -> mainFrame.showPanel("KnowledgeBaseFrame")); // Link Knowledge Base
    }

    private void layoutComponents(JLabel titleLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;

        add(titleLabel, gbc);
        gbc.gridy++;
        add(manageUsersButton, gbc);
        gbc.gridy++;
        add(viewTicketsButton, gbc);
        gbc.gridy++;
        add(metricsButton, gbc);
        gbc.gridy++;
        add(knowledgeBaseButton, gbc); // Add Knowledge Base button
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 45));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(52, 152, 219));
            }
        });

        return button;
    }
}
