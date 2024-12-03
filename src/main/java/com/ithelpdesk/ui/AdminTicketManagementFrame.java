package com.ithelpdesk.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.*;

public class AdminTicketManagementFrame extends JPanel {
    private final MainFrame mainFrame;
    private final DatabaseInterface database;
    private JTable ticketsTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusFilter;
    private JComboBox<String> priorityFilter;
    private JComboBox<String> severityFilter;
    private JButton refreshButton;
    private JButton assignButton;
    private JButton viewDetailsButton;
    private JButton updateStatusButton;

    public AdminTicketManagementFrame(MainFrame mainFrame, DatabaseInterface database) {
        this.mainFrame = mainFrame;
        this.database = database;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));

        // Header with filters
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(new Color(240, 242, 245));

        JLabel titleLabel = new JLabel("Ticket Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(new Color(240, 242, 245));

        statusFilter = new JComboBox<>(new String[]{"All", "Open", "Assigned", "Pending", "In Progress", "Resolved", "Closed"});
        priorityFilter = new JComboBox<>(new String[]{"All", "Very High", "High", "Moderate", "Low"});
        severityFilter = new JComboBox<>(new String[]{"All", "1", "2", "3", "4"});

        refreshButton = createStyledButton("Refresh", new Color(52, 152, 219));
        refreshButton.addActionListener(e -> loadTickets());

        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusFilter);
        filterPanel.add(new JLabel("Priority:"));
        filterPanel.add(priorityFilter);
        filterPanel.add(new JLabel("Severity:"));
        filterPanel.add(severityFilter);
        filterPanel.add(refreshButton);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(filterPanel, BorderLayout.CENTER);

        // Table
        String[] columns = {"AR ID", "Subject", "Priority", "Severity", "Status", "Assigned To", "Created By", "Created At"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ticketsTable = new JTable(tableModel);
        ticketsTable.setFillsViewportHeight(true);
        ticketsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(ticketsTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(new Color(240, 242, 245));

        assignButton = createStyledButton("Assign Ticket", new Color(46, 204, 113));
        viewDetailsButton = createStyledButton("View Details", new Color(52, 152, 219));
        updateStatusButton = createStyledButton("Update Status", new Color(255, 152, 0));

        assignButton.addActionListener(e -> handleAssignTicket());
        viewDetailsButton.addActionListener(e -> handleViewDetails());
        updateStatusButton.addActionListener(e -> handleUpdateStatus());

        buttonPanel.add(assignButton);
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(updateStatusButton);

        // Add all components
        add(headerPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add filters action listeners
        statusFilter.addActionListener(e -> applyFilters());
        priorityFilter.addActionListener(e -> applyFilters());
        severityFilter.addActionListener(e -> applyFilters());

        // Initial load
        loadTickets();
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
            List<Ticket> tickets = database.getAllTickets();
            for (Ticket ticket : tickets) {
                tableModel.addRow(new Object[]{
                        ticket.getArId(),
                        ticket.getSubject(),
                        ticket.getPriority(),
                        ticket.getSeverity(),
                        ticket.getStatus(),
                        ticket.getAssignedTo() != null ? ticket.getAssignedTo() : "Unassigned",
                        ticket.getCreatedBy(),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm").format(ticket.getCreatedAt())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading tickets: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyFilters() {
        String statusFilter = this.statusFilter.getSelectedItem().toString();
        String priorityFilter = this.priorityFilter.getSelectedItem().toString();
        String severityFilter = this.severityFilter.getSelectedItem().toString();

        List<Ticket> tickets = database.getAllTickets();
        tickets = tickets.stream()
                .filter(ticket -> statusFilter.equals("All") || ticket.getStatus().equals(statusFilter))
                .filter(ticket -> priorityFilter.equals("All") || ticket.getPriority().equals(priorityFilter))
                .filter(ticket -> severityFilter.equals("All") || ticket.getSeverity().equals(severityFilter))
                .collect(Collectors.toList());

        updateTable(tickets);
    }

    private void updateTable(List<Ticket> tickets) {
        tableModel.setRowCount(0);
        for (Ticket ticket : tickets) {
            tableModel.addRow(new Object[]{
                    ticket.getArId(),
                    ticket.getSubject(),
                    ticket.getPriority(),
                    ticket.getSeverity(),
                    ticket.getStatus(),
                    ticket.getAssignedTo() != null ? ticket.getAssignedTo() : "Unassigned",
                    ticket.getCreatedBy(),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm").format(ticket.getCreatedAt())
            });
        }
    }

    private void handleAssignTicket() {
        int selectedRow = ticketsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a ticket to assign.");
            return;
        }

        int ticketId = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            List<User> techs = database.getAllUsers().stream()
                    .filter(user -> "Tech".equals(user.getRole()))
                    .collect(Collectors.toList());

            if (techs.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No tech users available for assignment.");
                return;
            }

            JComboBox<String> techSelect = new JComboBox<>(
                    techs.stream().map(User::getUsername).toArray(String[]::new)
            );

            int result = JOptionPane.showConfirmDialog(
                    this,
                    techSelect,
                    "Select Tech to Assign",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (result == JOptionPane.OK_OPTION) {
                String selectedTech = (String) techSelect.getSelectedItem();
                database.updateTicketAssignment(ticketId, selectedTech);
                loadTickets();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error assigning ticket: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleViewDetails() {
        int selectedRow = ticketsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a ticket to view details.");
            return;
        }

        int ticketId = (int) tableModel.getValueAt(selectedRow, 0);
        mainFrame.showPanel("TicketDetailsFrame");

        TicketDetailsFrame ticketDetailsFrame = (TicketDetailsFrame) mainFrame.getPanel("TicketDetailsFrame");
        ticketDetailsFrame.loadTicket(ticketId);
    }

    private void handleUpdateStatus() {
        int selectedRow = ticketsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a ticket to update.");
            return;
        }

        int ticketId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 4);

        String[] statuses = {"Open", "Assigned", "Pending", "In Progress", "Resolved", "Closed"};
        JComboBox<String> statusSelect = new JComboBox<>(statuses);
        statusSelect.setSelectedItem(currentStatus);

        int result = JOptionPane.showConfirmDialog(
                this,
                statusSelect,
                "Select New Status",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            String newStatus = (String) statusSelect.getSelectedItem();
            try {
                database.updateTicketStatus(ticketId, newStatus);
                loadTickets();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error updating status: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
