package com.ithelpdesk.service;

import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.PriorityAndSeverity;

import java.util.List;

public class PriorityAndSeverityService {
    private final DatabaseInterface database;

    // Constructor
    public PriorityAndSeverityService(DatabaseInterface database) {
        this.database = database;
    }

    // Get all priorities
    public List<String> getAllPriorities() {
        return database.getAllPriorities();
    }

    // Get all severities
    public List<String> getAllSeverities() {
        return database.getAllSeverities();
    }

    // Add a new priority
    public void addPriority(String level, String description) {
        PriorityAndSeverity priority = new PriorityAndSeverity(level, description);
        database.addPriority(priority);
        System.out.println("Priority added successfully: " + level);
    }

    // Add a new severity
    public void addSeverity(String level, int resolutionTime, String description) {
        PriorityAndSeverity severity = new PriorityAndSeverity(level, resolutionTime, description);
        database.addSeverity(severity);
        System.out.println("Severity added successfully: " + level);
    }

    // Delete a priority
    public void deletePriority(String level) {
        database.deletePriority(level);
        System.out.println("Priority deleted successfully: " + level);
    }

    // Delete a severity
    public void deleteSeverity(String level) {
        database.deleteSeverity(level);
        System.out.println("Severity deleted successfully: " + level);
    }
}
