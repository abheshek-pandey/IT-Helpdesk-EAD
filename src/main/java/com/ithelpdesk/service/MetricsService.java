package com.ithelpdesk.service;

import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.Ticket;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MetricsService {
    private final DatabaseInterface database;

    public MetricsService(DatabaseInterface database) {
        this.database = database;
    }

    /**
     * Fetch all metrics to be displayed.
     * @return A map of metric names and their values.
     */
    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        try {
            List<Ticket> allTickets = database.getAllTickets();

            // Count tickets by status
            long openTickets = allTickets.stream().filter(ticket -> "Open".equalsIgnoreCase(ticket.getStatus())).count();
            long inProgressTickets = allTickets.stream().filter(ticket -> "In Progress".equalsIgnoreCase(ticket.getStatus())).count();
            long resolvedTickets = allTickets.stream().filter(ticket -> "Resolved".equalsIgnoreCase(ticket.getStatus())).count();
            long closedTickets = allTickets.stream().filter(ticket -> "Closed".equalsIgnoreCase(ticket.getStatus())).count();

            // Calculate the average resolution time
            double averageResolutionTime = calculateAverageResolutionTime(allTickets);

            // Populate metrics map
            metrics.put("Total Tickets", allTickets.size());
            metrics.put("Open Tickets", openTickets);
            metrics.put("In Progress Tickets", inProgressTickets);
            metrics.put("Resolved Tickets", resolvedTickets);
            metrics.put("Closed Tickets", closedTickets);
            metrics.put("Average Resolution Time (hours)", averageResolutionTime);

        } catch (Exception e) {
            throw new RuntimeException("Error fetching metrics: " + e.getMessage());
        }
        return metrics;
    }

    /**
     * Export metrics to a text file at a specified location.
     * @param filePath The path where the metrics report should be saved. If null, defaults to the working directory.
     * @throws IOException if an error occurs while writing the file.
     */
    public void exportMetricsReport(String filePath) throws IOException {
        Map<String, Object> metrics = getMetrics();
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("System Metrics Report\n");
            writer.write("======================\n");
            metrics.forEach((key, value) -> {
                try {
                    writer.write(key + ": " + value + "\n");
                } catch (IOException e) {
                    throw new RuntimeException("Error writing report: " + e.getMessage());
                }
            });
            writer.write("\nGenerated on: " + new Date() + "\n");
        }
    }



    /**
     * Calculate the average resolution time in hours.
     * @param tickets List of tickets to calculate the resolution time.
     * @return Average resolution time in hours.
     */
    private double calculateAverageResolutionTime(List<Ticket> tickets) {
        long totalResolutionTime = 0;
        int resolvedTicketCount = 0;

        for (Ticket ticket : tickets) {
            if ("Resolved".equalsIgnoreCase(ticket.getStatus()) || "Closed".equalsIgnoreCase(ticket.getStatus())) {
                Date createdAt = ticket.getCreatedAt();
                Date updatedAt = ticket.getUpdatedAt();
                if (createdAt != null && updatedAt != null) {
                    long duration = updatedAt.getTime() - createdAt.getTime();
                    totalResolutionTime += duration;
                    resolvedTicketCount++;
                }
            }
        }

        if (resolvedTicketCount == 0) return 0.0;

        // Convert milliseconds to hours
        return TimeUnit.MILLISECONDS.toHours(totalResolutionTime) / (double) resolvedTicketCount;
    }
}
