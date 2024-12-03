package com.ithelpdesk.service;

import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.Ticket;
import java.util.List;

public class TicketService {

    private DatabaseInterface database;

    public TicketService(DatabaseInterface database) {
        this.database = database;
    }

    public List<Ticket> getTicketsByStatus(String status) {
        return database.getTicketsByStatus(status);
    }

    public List<Ticket> getTicketsBySeverity(String severity) {
        return database.getTicketsBySeverity(severity);
    }

    public List<Ticket> getAllTickets() {
        return database.getAllTickets();
    }

    public double getAverageResolutionTimeBySeverity(String severity) {
        return database.getAverageResolutionTimeBySeverity(severity);
    }

    public double getAverageResolutionTimeByPriority(String priority) {
        return database.getAverageResolutionTimeByPriority(priority);
    }
}
