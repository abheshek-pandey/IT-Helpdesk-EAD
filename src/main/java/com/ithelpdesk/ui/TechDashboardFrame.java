package com.ithelpdesk.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.ithelpdesk.dao.DatabaseInterface;

public class TechDashboardFrame extends JPanel {
    private final MainFrame mainFrame;
    private final DatabaseInterface database;
    private JButton viewAssignedTicketsButton;
    private JButton metricsButton;          // Metrics button reintroduced
    private JButton knowledgeBaseButton;   // New button for Knowledge Base

    public TechDashboardFrame(MainFrame mainFrame, DatabaseInterface database) {
        this.mainFrame = mainFrame;
        this.database = database;
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 242, 245));
        initComponents();
    }

    private void initComponents() {
        viewAssignedTicketsButton = createStyledButton("View Assigned Tickets");
        metricsButton = createStyledButton("View Metrics");         // Metrics button
        knowledgeBaseButton = createStyledButton("Knowledge Base"); // New button

        // Action for "View Assigned Tickets"
        viewAssignedTicketsButton.addActionListener(e -> mainFrame.showPanel("TechTicketsFrame"));

        // Action for "View Metrics"
        metricsButton.addActionListener(e -> mainFrame.showPanel("MetricsFrame"));

        // Action for "Knowledge Base"
        knowledgeBaseButton.addActionListener(e -> mainFrame.showPanel("KnowledgeBaseFrame"));

        layoutComponents();
    }

    private void layoutComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        add(viewAssignedTicketsButton, gbc);
        gbc.gridy++;
        add(metricsButton, gbc); // Add Metrics button
        gbc.gridy++;
        add(knowledgeBaseButton, gbc); // Add Knowledge Base button
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(250, 45));

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
