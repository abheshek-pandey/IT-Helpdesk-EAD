package com.ithelpdesk.model;

public class Metrics {
    private double avgResolutionTime; // Average time to resolve tickets (in hours)
    private int ticketCount;          // Total number of tickets (filtered by criteria)
    private int activeTickets;        // Number of active (non-closed) tickets

    // Constructor
    public Metrics(double avgResolutionTime, int ticketCount, int activeTickets) {
        this.avgResolutionTime = avgResolutionTime;
        this.ticketCount = ticketCount;
        this.activeTickets = activeTickets;
    }

    // Getters and Setters
    public double getAvgResolutionTime() {
        return avgResolutionTime;
    }

    public void setAvgResolutionTime(double avgResolutionTime) {
        this.avgResolutionTime = avgResolutionTime;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public int getActiveTickets() {
        return activeTickets;
    }

    public void setActiveTickets(int activeTickets) {
        this.activeTickets = activeTickets;
    }

    @Override
    public String toString() {
        return "Metrics{" +
                "avgResolutionTime=" + avgResolutionTime +
                ", ticketCount=" + ticketCount +
                ", activeTickets=" + activeTickets +
                '}';
    }
}
