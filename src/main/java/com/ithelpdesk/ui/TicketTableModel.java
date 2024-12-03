package com.ithelpdesk.ui;

import com.ithelpdesk.model.Ticket;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TicketTableModel extends AbstractTableModel {
    private final String[] columnNames = {"AR ID", "Subject", "Priority", "Severity", "Status", "Assigned To", "Created By", "Created At"};
    private List<Ticket> tickets;

    public TicketTableModel(List<Ticket> tickets) {
        this.tickets = tickets != null ? tickets : List.of(); // Ensure we don’t get a null list
    }

    @Override
    public int getRowCount() {
        return tickets.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ticket ticket = tickets.get(rowIndex);
        switch (columnIndex) {
            case 0: return ticket.getArId();
            case 1: return ticket.getSubject();
            case 2: return ticket.getPriority();
            case 3: return ticket.getSeverity();
            case 4: return ticket.getStatus();
            case 5: return ticket.getAssignedTo();
            case 6: return ticket.getCreatedBy();
            case 7: return ticket.getCreatedAt();
            default: return null;
        }
    }

    // Set the tickets and notify the table to update
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets != null ? tickets : List.of(); // Ensure we don’t get a null list
        fireTableDataChanged();
    }
}
