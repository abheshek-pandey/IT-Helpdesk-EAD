package com.ithelpdesk.model;

import java.util.Map;

public class DashboardData {
    private int totalTickets;                           // Total tickets in the system
    private int openTickets;                            // Tickets with "Open" status
    private int resolvedTickets;                        // Tickets with "Resolved" status
    private int pendingTickets;                         // Tickets with "Pending" status
    private Map<String, Integer> ticketsByPriority;     // Map of ticket counts by priority
    private Map<String, Integer> ticketsBySeverity;     // Map of ticket counts by severity
    private Metrics overallMetrics;                     // Overall metrics (e.g., resolution time)

    // Constructor
    public DashboardData(int totalTickets, int openTickets, int resolvedTickets, int pendingTickets,
                         Map<String, Integer> ticketsByPriority, Map<String, Integer> ticketsBySeverity, Metrics overallMetrics) {
        this.totalTickets = totalTickets;
        this.openTickets = openTickets;
        this.resolvedTickets = resolvedTickets;
        this.pendingTickets = pendingTickets;
        this.ticketsByPriority = ticketsByPriority;
        this.ticketsBySeverity = ticketsBySeverity;
        this.overallMetrics = overallMetrics;
    }

    // Getters and Setters
    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getOpenTickets() {
        return openTickets;
    }

    public void setOpenTickets(int openTickets) {
        this.openTickets = openTickets;
    }

    public int getResolvedTickets() {
        return resolvedTickets;
    }

    public void setResolvedTickets(int resolvedTickets) {
        this.resolvedTickets = resolvedTickets;
    }

    public int getPendingTickets() {
        return pendingTickets;
    }

    public void setPendingTickets(int pendingTickets) {
        this.pendingTickets = pendingTickets;
    }

    public Map<String, Integer> getTicketsByPriority() {
        return ticketsByPriority;
    }

    public void setTicketsByPriority(Map<String, Integer> ticketsByPriority) {
        this.ticketsByPriority = ticketsByPriority;
    }

    public Map<String, Integer> getTicketsBySeverity() {
        return ticketsBySeverity;
    }

    public void setTicketsBySeverity(Map<String, Integer> ticketsBySeverity) {
        this.ticketsBySeverity = ticketsBySeverity;
    }

    public Metrics getOverallMetrics() {
        return overallMetrics;
    }

    public void setOverallMetrics(Metrics overallMetrics) {
        this.overallMetrics = overallMetrics;
    }

    @Override
    public String toString() {
        return "DashboardData{" +
                "totalTickets=" + totalTickets +
                ", openTickets=" + openTickets +
                ", resolvedTickets=" + resolvedTickets +
                ", pendingTickets=" + pendingTickets +
                ", ticketsByPriority=" + ticketsByPriority +
                ", ticketsBySeverity=" + ticketsBySeverity +
                ", overallMetrics=" + overallMetrics +
                '}';
    }
}
