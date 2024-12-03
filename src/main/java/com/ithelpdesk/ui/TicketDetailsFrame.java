package com.ithelpdesk.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.Ticket;

public class TicketDetailsFrame extends JPanel {
    private final MainFrame mainFrame;
    private final DatabaseInterface database;
    private int currentTicketId;
    private JLabel ticketIdLabel;
    private JTextArea ticketDetailsArea;
    private JLabel statusLabel;
    private JLabel emailLabel;
    private JLabel contactLabel;
    private JButton closeTicketButton;
    private Ticket ticket;

    public TicketDetailsFrame(MainFrame mainFrame, DatabaseInterface database) {
        this.mainFrame = mainFrame;
        this.database = database;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));

        // Add header panel
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Add ticket details panel
        add(createDetailsPanel(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 242, 245));

        JLabel headerLabel = new JLabel("Ticket Details");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(headerLabel);
        return panel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialize fields
        ticketIdLabel = new JLabel();
        ticketDetailsArea = createTextArea();
        statusLabel = new JLabel();
        emailLabel = new JLabel();
        contactLabel = new JLabel();
        closeTicketButton = createCloseButton();

        // Add components to the panel
        addField(panel, "Ticket ID:", ticketIdLabel, gbc, 0);
        addField(panel, "Details:", new JScrollPane(ticketDetailsArea), gbc, 1);
        addField(panel, "Status:", statusLabel, gbc, 2);
        addField(panel, "Email:", emailLabel, gbc, 3);
        addField(panel, "Contact:", contactLabel, gbc, 4);

        gbc.gridy = 5;
        gbc.gridx = 1;
        panel.add(closeTicketButton, gbc);

        return panel;
    }

    private JTextArea createTextArea() {
        JTextArea area = new JTextArea(10, 40);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setText("No ticket selected");
        return area;
    }

    private JButton createCloseButton() {
        JButton button = new JButton("Close Ticket");
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(231, 76, 60));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.addActionListener(e -> handleCloseTicket());

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(192, 57, 43));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(231, 76, 60));
            }
        });

        return button;
    }

    private void addField(JPanel panel, String labelText, Component component, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.fill = (component instanceof JScrollPane) ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;
        panel.add(component, gbc);
    }

    public void loadTicket(int arId) {
        try {
            Ticket ticket = database.getTicketByArId(arId); // Actual DB call to get ticket
            if(ticket != null) {
                currentTicketId = arId;
                ticketIdLabel.setText(String.valueOf(arId));
                ticketDetailsArea.setText(ticket.getDescription());
                statusLabel.setText(ticket.getStatus());
                emailLabel.setText(ticket.getUserEmail());
                contactLabel.setText(ticket.getUserContact());
                closeTicketButton.setEnabled(ticket.getStatus().equals("Resolved"));
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading ticket: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCloseTicket() {
        try {
            if ("Resolved".equalsIgnoreCase(ticket.getStatus())) {
                database.updateTicketStatus(currentTicketId, "Closed"); // Actual DB call to close ticket
                JOptionPane.showMessageDialog(this, "Ticket closed successfully");
                loadTicket(currentTicketId);  // Reload ticket details after closing
            } else {
                JOptionPane.showMessageDialog(this,
                    "Only resolved tickets can be closed",
                    "Invalid Action",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error closing ticket: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
