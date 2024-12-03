package com.ithelpdesk.model;

import java.sql.Timestamp;

public class AuditLog {
    private int id;                 // Log ID (primary key)
    private String username;        // User who performed the action
    private String action;          // Description of the action
    private String details;         // Additional details about the action
    private Timestamp timestamp;    // Timestamp of when the action was performed

    // Constructor
    public AuditLog(String username, String action, String details, Timestamp timestamp) {
        this.username = username;
        this.action = action;
        this.details = details;
        this.timestamp = timestamp;
    }

    // Constructor for database retrieval
    public AuditLog(int id, String username, String action, String details, Timestamp timestamp) {
        this.id = id;
        this.username = username;
        this.action = action;
        this.details = details;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", action='" + action + '\'' +
                ", details='" + details + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
