package com.ithelpdesk.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;
import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.Ticket;

public class UserTicketsFrame extends JPanel {
    private final MainFrame mainFrame;
    private final DatabaseInterface database;
    private JTable ticketsTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton viewTicketButton;
    private JComboBox<String> statusFilterComboBox;

    public UserTicketsFrame(MainFrame mainFrame, DatabaseInterface database) {
        this.mainFrame = mainFrame;
        this.database = database;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));
        initComponents();
    }

    private void initComponents() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
        loadTickets();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 242, 245));

        JLabel headerLabel = new JLabel("My Tickets");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(headerLabel);

        return panel;
    }

    private JScrollPane createTablePanel() {
        String[] columns = {"AR ID", "Subject", "Priority", "Severity", "Status", "Created At"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ticketsTable = new JTable(tableModel);
        ticketsTable.setFillsViewportHeight(true);
        ticketsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return new JScrollPane(ticketsTable);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(240, 242, 245));

        refreshButton = createStyledButton("Refresh", new Color(52, 152, 219));
        viewTicketButton = createStyledButton("View Ticket", new Color(46, 204, 113));

        refreshButton.addActionListener(e -> loadTickets());
        viewTicketButton.addActionListener(e -> viewSelectedTicket());

        statusFilterComboBox = new JComboBox<>(new String[]{"All", "Open", "Assigned", "Pending", "In Progress", "Resolved", "Closed"});
        statusFilterComboBox.addActionListener(e -> loadTickets());

        panel.add(new JLabel("Filter:"));
        panel.add(statusFilterComboBox);
        panel.add(refreshButton);
        panel.add(viewTicketButton);
        return panel;
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

    private void loadTickets() {
        tableModel.setRowCount(0);
        try {
            String statusFilter = (String) statusFilterComboBox.getSelectedItem();
            List<Ticket> userTickets = database.getTicketsByUserStatus(mainFrame.getCurrentUser(),
                    "All".equals(statusFilter) ? null : statusFilter);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            for (Ticket ticket : userTickets) {
                tableModel.addRow(new Object[]{
                        ticket.getArId(),
                        ticket.getSubject(),
                        ticket.getPriority(),
                        ticket.getSeverity(),
                        ticket.getStatus(),
                        dateFormat.format(ticket.getCreatedAt())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading tickets: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewSelectedTicket() {
        int selectedRow = ticketsTable.getSelectedRow();
        if (selectedRow >= 0) {
            int ticketId = (int) ticketsTable.getValueAt(selectedRow, 0);

            // Display a simple ticket view with basic information
            Ticket ticket = database.getTicketByArId(ticketId);
            if (ticket != null) {
                String ticketDetails = String.format(
                        "Subject: %s%nPriority: %s%nSeverity: %s%nStatus: %s%nCreated At: %s%nDescription:%n%s",
                        ticket.getSubject(),
                        ticket.getPriority(),
                        ticket.getSeverity(),
                        ticket.getStatus(),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm").format(ticket.getCreatedAt()),
                        ticket.getDescription()
                );
                JOptionPane.showMessageDialog(this, ticketDetails, "Ticket Details", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to load ticket details.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a ticket to view.",
                    "No Selection",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
