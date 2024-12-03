package com.ithelpdesk.service;

import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.TicketStatusLog;
import java.sql.Timestamp;
import java.util.List;
import java.sql.SQLException;

public class TicketStatusService {
    private final DatabaseInterface database;

    // Constructor
    public TicketStatusService(DatabaseInterface database) {
        this.database = database;
    }
    
    // Method to add a ticket status log
    public void addTicketStatusLog(TicketStatusLog log) {
        database.addTicketStatusLog(log);
    }

    // Method to get status logs for a specific ticket
    public List<TicketStatusLog> getTicketStatusLogs(int ticketId) {
        return database.getTicketStatusLogsByArId(ticketId);
    }

    // Change ticket status
    public void changeTicketStatus(int arId, String newStatus, String previousStatus, String updatedBy) {
        // Validate the status change (ensure the new status is different from the previous one)
        if (newStatus == null || newStatus.equals(previousStatus)) {
            System.err.println("Invalid status change: the status must be different from the previous one.");
            return;
        }
        
        

        // Create a new ticket status log
        TicketStatusLog statusLog = new TicketStatusLog(
                arId,
                previousStatus,
                newStatus,
                new Timestamp(System.currentTimeMillis())
        );

        // Log the status change and update ticket status
		database.addTicketStatusLog(statusLog);
		database.updateTicketStatus(arId, newStatus);
		System.out.println("Ticket status updated and logged for AR ID: " + arId);
    }
}
