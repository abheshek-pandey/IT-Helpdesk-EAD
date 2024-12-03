package com.ithelpdesk.ui;

import javax.swing.*;
import java.awt.*;
import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.Ticket;

public class DetailedTicketView extends JFrame {
    public DetailedTicketView(Ticket ticket, DatabaseInterface database) {
        setTitle("Ticket Details - Full");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 242, 245));

        JLabel titleLabel = new JLabel("Ticket Full Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subjectLabel = new JLabel("Subject: " + ticket.getSubject());
        JLabel descriptionLabel = new JLabel("Description: " + ticket.getDescription());
        JLabel statusLabel = new JLabel("Status: " + ticket.getStatus());
        JLabel priorityLabel = new JLabel("Priority: " + ticket.getPriority());
        JLabel severityLabel = new JLabel("Severity: " + ticket.getSeverity());
        JLabel creatorLabel = new JLabel("Created By: " + ticket.getCreatedBy());
        JLabel contactLabel = new JLabel("Contact: " + ticket.getUserContact());

        JLabel notesLabel = new JLabel("Notes:");
        JTextArea notesArea = new JTextArea(5, 30);
        notesArea.setText(ticket.getNotes());
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(notesArea);

        JButton saveButton = new JButton("Save Notes");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.addActionListener(e -> {
            try {
                ticket.setNotes(notesArea.getText());
                database.updateTicket(ticket);
                JOptionPane.showMessageDialog(this, "Notes updated successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving notes: " + ex.getMessage());
            }
        });

        JButton closeButton = new JButton("Close");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> dispose());

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subjectLabel);
        panel.add(descriptionLabel);
        panel.add(statusLabel);
        panel.add(priorityLabel);
        panel.add(severityLabel);
        panel.add(creatorLabel);
        panel.add(contactLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(notesLabel);
        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(10));
        panel.add(saveButton);
        panel.add(closeButton);

        add(panel);
    }
}
