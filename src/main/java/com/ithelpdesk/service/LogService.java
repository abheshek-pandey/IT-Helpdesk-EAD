package com.ithelpdesk.service;

import com.ithelpdesk.dao.DatabaseInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class LogService {
    private final DatabaseInterface database;

    public LogService(DatabaseInterface database) {
        this.database = database;
    }


    private void logToSQLDatabase(Connection connection, String action, String details, String performedBy) {
        try {
            String query = "INSERT INTO logs (action, details, performed_by, timestamp) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, action);
            stmt.setString(2, details);
            stmt.setString(3, performedBy);
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
