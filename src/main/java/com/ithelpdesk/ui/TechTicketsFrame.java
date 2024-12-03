package com.ithelpdesk.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;
import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.Ticket;

public class TechTicketsFrame extends JPanel {
    private final MainFrame mainFrame;
    private final DatabaseInterface database;
    private JTable ticketsTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton viewDetailsButton;
    private JButton changeStatusButton;
    private JComboBox<String> statusFilterCombo;
    private JComboBox<String> priorityFilterCombo;

    public TechTicketsFrame(MainFrame mainFrame, DatabaseInterface database) {
        this.mainFrame = mainFrame;
        this.database = database;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));

        // Header panel with the title
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Filter panel for status and priority filters
        add(createFilterPanel(), BorderLayout.NORTH);

        // Table panel to display tickets
        add(createTablePanel(), BorderLayout.CENTER);

        // Button panel for actions
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadTickets();  // Initially load all tickets
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 242, 245));

        JLabel headerLabel = new JLabel("Assigned Tickets");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(headerLabel);

        return panel;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(240, 242, 245));

        JLabel statusLabel = new JLabel("Filter by Status: ");
        statusFilterCombo = new JComboBox<>(new String[]{"All", "Open", "Assigned", "In Progress", "Resolved", "Closed"});
        statusFilterCombo.addActionListener(e -> loadTickets());

        JLabel priorityLabel = new JLabel("Filter by Priority: ");
        priorityFilterCombo = new JComboBox<>(new String[]{"All", "Very High", "High", "Moderate", "Low"});
        priorityFilterCombo.addActionListener(e -> loadTickets());

        panel.add(statusLabel);
        panel.add(statusFilterCombo);
        panel.add(priorityLabel);
        panel.add(priorityFilterCombo);

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

        refreshButton = createStyledButton("Refresh");
        viewDetailsButton = createStyledButton("View Ticket Details");
        changeStatusButton = createStyledButton("Change Status");

        // Action for Refresh Button
        refreshButton.addActionListener(e -> loadTickets());

        // Action for View Details Button
        viewDetailsButton.addActionListener(e -> handleViewDetails());

        // Action for Change Status Button
        changeStatusButton.addActionListener(e -> handleChangeStatus());

        panel.add(refreshButton);
        panel.add(viewDetailsButton);
        panel.add(changeStatusButton);

        return panel;
    }

    private void loadTickets() {
        tableModel.setRowCount(0);  // Clear the existing rows

        String statusFilter = (String) statusFilterCombo.getSelectedItem();
        String priorityFilter = (String) priorityFilterCombo.getSelectedItem();

        try {
            List<Ticket> assignedTickets = database.getTicketsByAssignee(mainFrame.getCurrentUser());
            for (Ticket ticket : assignedTickets) {
                if ((statusFilter.equals("All") || ticket.getStatus().equals(statusFilter)) &&
                    (priorityFilter.equals("All") || ticket.getPriority().equals(priorityFilter))) {

                    tableModel.addRow(new Object[]{
                            ticket.getArId(),
                            ticket.getSubject(),
                            ticket.getPriority(),
                            ticket.getSeverity(),
                            ticket.getStatus(),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm").format(ticket.getCreatedAt())
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading tickets: " + e.getMessage(),
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
        Ticket ticket = database.getTicketByArId(ticketId);

        if (ticket == null) {
            JOptionPane.showMessageDialog(this, "Error fetching ticket details.");
            return;
        }

        new BasicTicketView(ticket, mainFrame, database).setVisible(true);
    }

    private void handleChangeStatus() {
        int selectedRow = ticketsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a ticket to change its status.");
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
                // Update the status in the database
                database.updateTicketStatus(ticketId, newStatus);

                // Show notification if the status is resolved
                if ("Resolved".equals(newStatus)) {
                    JOptionPane.showMessageDialog(
                        this,
                        "The ticket has been resolved. An email notification was sent to the user.",
                        "Notification Sent",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "The ticket status has been updated to: " + newStatus,
                        "Status Updated",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }

                loadTickets(); // Refresh the ticket list
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error updating ticket status: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }


    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

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
