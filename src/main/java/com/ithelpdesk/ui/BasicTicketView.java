package com.ithelpdesk.ui;

import javax.swing.*;
import java.awt.*;
import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.Ticket;

public class BasicTicketView extends JFrame {
    public BasicTicketView(Ticket ticket, MainFrame mainFrame, DatabaseInterface database) {
        setTitle("Ticket Details");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 242, 245));

        JLabel titleLabel = new JLabel("Ticket Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subjectLabel = new JLabel("Subject: " + ticket.getSubject());
        JLabel descriptionLabel = new JLabel("Description: " + ticket.getDescription());
        JLabel statusLabel = new JLabel("Status: " + ticket.getStatus());

        subjectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton moreDetailsButton = new JButton("More Details");
        moreDetailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        moreDetailsButton.addActionListener(e -> {
            new DetailedTicketView(ticket, database).setVisible(true);
            dispose();
        });

        JButton closeButton = new JButton("Close");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> dispose());

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subjectLabel);
        panel.add(descriptionLabel);
        panel.add(statusLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(moreDetailsButton);
        panel.add(closeButton);

        add(panel);
    }
}
