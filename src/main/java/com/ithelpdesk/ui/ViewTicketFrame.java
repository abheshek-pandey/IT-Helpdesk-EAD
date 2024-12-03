package com.ithelpdesk.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.Ticket;
import java.text.SimpleDateFormat;

public class ViewTicketFrame extends JPanel {
    private final MainFrame mainFrame;
    private final DatabaseInterface database;
    private JLabel ticketIdLabel;
    private JLabel subjectLabel;
    private JTextArea descriptionArea;
    private JLabel priorityLabel;
    private JLabel severityLabel;
    private JLabel statusLabel;
    private JButton viewDetailsButton;
    private int currentTicketId;

    public ViewTicketFrame(MainFrame mainFrame, DatabaseInterface database) {
        this.mainFrame = mainFrame;
        this.database = database;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));
        initComponents();
    }

    private void initComponents() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createDetailsPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 242, 245));

        JLabel headerLabel = new JLabel("View Ticket");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
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

        ticketIdLabel = new JLabel();
        subjectLabel = new JLabel();
        descriptionArea = new JTextArea(10, 40);
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        priorityLabel = new JLabel();
        severityLabel = new JLabel();
        statusLabel = new JLabel();

        addField(panel, "Ticket ID:", ticketIdLabel, gbc, 0);
        addField(panel, "Subject:", subjectLabel, gbc, 1);
        addField(panel, "Description:", new JScrollPane(descriptionArea), gbc, 2);
        addField(panel, "Priority:", priorityLabel, gbc, 3);
        addField(panel, "Severity:", severityLabel, gbc, 4);
        addField(panel, "Status:", statusLabel, gbc, 5);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(240, 242, 245));

        viewDetailsButton = createStyledButton("Show Full Details", new Color(52, 152, 219));
        viewDetailsButton.addActionListener(e -> showFullDetails());

        panel.add(viewDetailsButton);
        return panel;
    }

    private void addField(JPanel panel, String labelText, Component component, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.fill = (component instanceof JScrollPane) ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;
        panel.add(component, gbc);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    public void loadTicket(int ticketId) {
        try {
            Ticket ticket = database.getTicketByArId(ticketId);
            if (ticket != null) {
                currentTicketId = ticketId;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                ticketIdLabel.setText(String.valueOf(ticket.getArId()));
                subjectLabel.setText(ticket.getSubject());
                descriptionArea.setText(ticket.getDescription());
                priorityLabel.setText(ticket.getPriority());
                severityLabel.setText(ticket.getSeverity());
                statusLabel.setText(ticket.getStatus());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading ticket: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showFullDetails() {
        // Show the TicketDetailsFrame panel
        mainFrame.showPanel("TicketDetailsFrame");

        // Retrieve the TicketDetailsFrame and load the ticket details
        TicketDetailsFrame ticketDetailsFrame = (TicketDetailsFrame) mainFrame.getPanel("TicketDetailsFrame");
        ticketDetailsFrame.loadTicket(currentTicketId);
    }
}
