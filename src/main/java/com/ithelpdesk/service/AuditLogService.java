package com.ithelpdesk.service;

import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.AuditLog;

import java.sql.Timestamp;
import java.util.List;

public class AuditLogService {
    private final DatabaseInterface database;

    // Constructor
    public AuditLogService(DatabaseInterface database) {
        this.database = database;
    }

    // Log an action
    public void logAction(String username, String action, String details) {
        AuditLog log = new AuditLog(username, action, details, new Timestamp(System.currentTimeMillis()));
        database.addAuditLog(log);
        System.out.println("Audit log created: " + log);
    }

    // Get all logs
    public List<AuditLog> getAllLogs() {
        return database.getAllAuditLogs();
    }

    // Get logs by username
    public List<AuditLog> getLogsByUsername(String username) {
        return database.getAuditLogsByUsername(username);
    }
}
