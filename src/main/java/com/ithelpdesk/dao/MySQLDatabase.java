package com.ithelpdesk.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.ithelpdesk.dao.*;

import com.ithelpdesk.model.Article;
import com.ithelpdesk.model.Attachment;
import com.ithelpdesk.model.AuditLog;
import com.ithelpdesk.model.Category;
import com.ithelpdesk.model.KnowledgeBaseCategory;
import com.ithelpdesk.model.PriorityAndSeverity;
import com.ithelpdesk.model.Ticket;
import com.ithelpdesk.model.TicketStatusLog;
import com.ithelpdesk.model.User;
import com.ithelpdesk.ui.MetricsFrame;


public class MySQLDatabase implements DatabaseInterface {
    private final String url;
    private final String username;
    private final String password;
    private Connection connection;
    private DatabaseInterface database;
    private MetricsFrame metricsPanel;

    public MySQLDatabase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    @Override
    public void updateTicketAssignment(int ticketId, String assignedTo) {
        try {
            String query = "UPDATE tickets SET assigned_to = ?, status = 'Assigned', " +
                          "updated_at = CURRENT_TIMESTAMP WHERE ar_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, assignedTo);
            pstmt.setInt(2, ticketId);
            pstmt.executeUpdate();
            
            // Log the assignment
            logTicketAssignment(ticketId, assignedTo);
        } catch (SQLException e) {
            throw new RuntimeException("Error assigning ticket: " + e.getMessage());
        }
    }

    private void logTicketAssignment(int ticketId, String assignedTo) {
        try {
            String logQuery = "INSERT INTO ticket_status_logs (ar_id, previous_status, new_status, " +
                            "changed_at, changed_by) VALUES (?, 'Open', 'Assigned', CURRENT_TIMESTAMP, ?)";
            PreparedStatement logStmt = connection.prepareStatement(logQuery);
            logStmt.setInt(1, ticketId);
            logStmt.setString(2, assignedTo);
            logStmt.executeUpdate();
        } catch (SQLException e) {
            // Log error but don't throw - assignment already succeeded
            e.printStackTrace();
        }
    }

    @Override
    public void addTicketCategory(String category, String subcategory) {
        try {
            String query = "INSERT INTO ticket_categories (category, subcategory) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, category);
            pstmt.setString(2, subcategory);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding ticket category: " + e.getMessage());
        }
    }

    @Override
    public List<String> getTicketCategories() {
        List<String> categories = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT category FROM ticket_categories ORDER BY category";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting ticket categories: " + e.getMessage());
        }
        return categories;
    }

    @Override
    public List<String> getTicketPriorities() {
        List<String> priorities = new ArrayList<>();
        try {
            String query = "SELECT level FROM ticket_priorities ORDER BY FIELD(level, 'Very High', 'High', 'Moderate', 'Low')";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                priorities.add(rs.getString("level"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting ticket priorities: " + e.getMessage());
        }
        return priorities;
    }

    @Override 
    public List<String> getTicketSeverities() {
        List<String> severities = new ArrayList<>();
        try {
            String query = "SELECT level FROM ticket_severities ORDER BY level";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                severities.add(rs.getString("level")); 
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting ticket severities: " + e.getMessage());
        }
        return severities;
    }


    // Connect to the database
    @Override
    public void connect() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Close the database connection
    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Article Management

    @Override
    public void updateArticle(Article article) {
        try {
            String query = "UPDATE articles SET title = ?, content = ?, last_updated = NOW() WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setInt(3, article.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();
        try {
            String query = "SELECT * FROM articles";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("created_by"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("last_updated")
                );
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    @Override
    public Article getArticleByTitle(String title) {
        Article article = null;
        try {
            String query = "SELECT * FROM articles WHERE title = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                article = new Article(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("created_by"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("last_updated")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    @Override
    public void deleteArticle(String title) {
        try {
            String query = "DELETE FROM articles WHERE title = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, title);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ticket Management

    @Override
    public void updateTicket(Ticket ticket) {
        try {
            String query = "UPDATE tickets SET subject = ?, description = ?, severity = ?, priority = ?, status = ?, assigned_to = ?, updated_at = CURRENT_TIMESTAMP, notes = ? WHERE ar_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, ticket.getSubject());
            pstmt.setString(2, ticket.getDescription());
            pstmt.setString(3, ticket.getSeverity());
            pstmt.setString(4, ticket.getPriority());
            pstmt.setString(5, ticket.getStatus());
            pstmt.setString(6, ticket.getAssignedTo());
            pstmt.setString(7, ticket.getNotes());
            pstmt.setInt(8, ticket.getArId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteTicket(int arId) {
        try {
            String query = "DELETE FROM tickets WHERE ar_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, arId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addTicket(Ticket ticket) {
        String query = "INSERT INTO tickets (subject, description, severity, priority, status, assigned_to, created_by, created_at, updated_at, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, ticket.getSubject());
            stmt.setString(2, ticket.getDescription());
            stmt.setString(3, ticket.getSeverity());
            stmt.setString(4, ticket.getPriority());
            stmt.setString(5, ticket.getStatus());
            stmt.setString(6, ticket.getAssignedTo());
            stmt.setString(7, ticket.getCreatedBy());
            stmt.setTimestamp(8, ticket.getCreatedAt());
            stmt.setTimestamp(9, ticket.getUpdatedAt());
            stmt.setString(10, ticket.getNotes());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    // Ticket Status Logs
    @Override
    public void addTicketStatusLog(TicketStatusLog log) {
        try {
            String query = "INSERT INTO ticket_status_logs (ar_id, previous_status, new_status, changed_at) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, log.getArId());
            pstmt.setString(2, log.getPreviousStatus());
            pstmt.setString(3, log.getNewStatus());
            pstmt.setTimestamp(4, log.getChangedAt());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<TicketStatusLog> getTicketStatusLogsByArId(int arId) {
        List<TicketStatusLog> logs = new ArrayList<>();
        try {
            String query = "SELECT * FROM ticket_status_logs WHERE ar_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, arId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                logs.add(new TicketStatusLog(
                        rs.getInt("id"),
                        rs.getString("previous_status"),
                        rs.getString("new_status"),
                        rs.getTimestamp("changed_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    // User Management


    @Override
    public void deleteUser(String username) {
        try {
            String query = "DELETE FROM users WHERE username = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 // Helper method to map ResultSet to Ticket
    private Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {
        // Fetch attachments for the ticket
        List<Attachment> attachments = getAttachmentsByArId(rs.getInt("ar_id"));

        // Optional: Calculate resolution time (if applicable)
        Integer resolutionTime = calculateResolutionTime(rs.getInt("ar_id"));

        return new Ticket(
            rs.getInt("ar_id"),                            // AR ID
            rs.getString("subject"),                       // Subject
            rs.getString("description"),                   // Description
            rs.getString("priority"),                      // Priority
            rs.getString("severity"),                      // Severity
            rs.getString("status"),                        // Status
            rs.getString("assigned_to"),                   // Assigned To
            rs.getString("created_by"),                    // Created By
            rs.getString("user_email"),                    // User Email
            rs.getString("user_contact"),                  // User Contact
            rs.getTimestamp("created_at"),                 // Created At
            rs.getTimestamp("updated_at"),                 // Updated At
            rs.getString("notes"),                         // Notes
            rs.getInt("category_id"),                      // Category ID
            attachments.isEmpty() ? null : attachments.get(0).getFilePath(), // Single file path or null
            resolutionTime                                 // Resolution Time
        );
    }


    // Attachments Management
    public void addAttachment(int ticketId, String filePath) {
        String query = "INSERT INTO ticket_attachments (ar_id, file_path) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ticketId);
            stmt.setString(2, filePath);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Category Management
    @Override
    public List<String> getSubcategoriesByCategory(String category) {
        List<String> subcategories = new ArrayList<>();
        try {
            String query = "SELECT subcategory FROM categories WHERE category = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                subcategories.add(rs.getString("subcategory"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subcategories;
    }

    @Override
    public void addCategory(Category category) {
        try {
            String query = "INSERT INTO categories (category, subcategory) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, category.getCategory());
            pstmt.setString(2, category.getSubcategory());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCategory(int categoryId) {
        try {
            String query = "DELETE FROM categories WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, categoryId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Knowledge Base Management
    @Override
    public List<String> getAllKnowledgeBaseCategories() {
        List<String> categories = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT name FROM knowledge_base_categories";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public List<String> getKnowledgeBaseSubcategories(String category) {
        List<String> subcategories = new ArrayList<>();
        try {
            String query = "SELECT subcategory FROM knowledge_base_categories WHERE name = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                subcategories.add(rs.getString("subcategory"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subcategories;
    }

    @Override
    public void addKnowledgeBaseCategory(KnowledgeBaseCategory category) {
        try {
            String query = "INSERT INTO knowledge_base_categories (name, subcategory) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, category.getName());
            pstmt.setString(2, category.getSubcategory());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteKnowledgeBaseCategory(int categoryId) {
        try {
            String query = "DELETE FROM knowledge_base_categories WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, categoryId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Audit Logs
    @Override
    public void addAuditLog(AuditLog log) {
        try {
            String query = "INSERT INTO audit_logs (username, action, details, timestamp) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, log.getUsername());
            pstmt.setString(2, log.getAction());
            pstmt.setString(3, log.getDetails());
            pstmt.setTimestamp(4, log.getTimestamp());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AuditLog> getAllAuditLogs() {
        List<AuditLog> logs = new ArrayList<>();
        try {
            String query = "SELECT * FROM audit_logs";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                logs.add(new AuditLog(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("action"),
                        rs.getString("details"),
                        rs.getTimestamp("timestamp")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    @Override
    public List<AuditLog> getAuditLogsByUsername(String username) {
        List<AuditLog> logs = new ArrayList<>();
        try {
            String query = "SELECT * FROM audit_logs WHERE username = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                logs.add(new AuditLog(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("action"),
                        rs.getString("details"),
                        rs.getTimestamp("timestamp")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    // Priority Management
    @Override
    public List<String> getAllPriorities() {
        List<String> priorities = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT level FROM priorities";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                priorities.add(rs.getString("level"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return priorities;
    }

    @Override
    public void addPriority(PriorityAndSeverity priority) {
        try {
            String query = "INSERT INTO priorities (level, description) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, priority.getPriorityLevel());
            pstmt.setString(2, priority.getPriorityDescription());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePriority(String priorityLevel) {
        try {
            String query = "DELETE FROM priorities WHERE level = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, priorityLevel);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Severity Management
    @Override
    public List<String> getAllSeverities() {
        List<String> severities = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT level FROM severities";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                severities.add(rs.getString("level"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return severities;
    }

    @Override
    public void addSeverity(PriorityAndSeverity severity) {
        try {
            String query = "INSERT INTO severities (level, target_resolution_time, description) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, severity.getSeverityLevel());
            pstmt.setInt(2, severity.getTargetResolutionTime());
            pstmt.setString(3, severity.getSeverityDescription());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSeverity(String severityLevel) {
        try {
            String query = "DELETE FROM severities WHERE level = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, severityLevel);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper Methods for Tickets

    @Override
    public List<Ticket> getTicketsByUserStatus(String user, String status) {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM tickets WHERE created_by = ? AND status = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, user);
            ps.setString(2, status);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Fetch attachments for the ticket
                List<Attachment> attachments = getAttachmentsByArId(rs.getInt("ar_id"));

                // Calculate resolution time (if applicable)
                Integer resolutionTime = calculateResolutionTime(rs.getInt("ar_id"));

                // Create Ticket instance
                Ticket ticket = new Ticket(
                    rs.getInt("ar_id"),                   // AR ID
                    rs.getString("subject"),              // Subject
                    rs.getString("description"),          // Description
                    rs.getString("priority"),             // Priority
                    rs.getString("severity"),             // Severity
                    rs.getString("status"),               // Status
                    rs.getString("assigned_to"),          // Assigned To
                    rs.getString("created_by"),           // Created By
                    rs.getString("user_email"),           // User Email
                    rs.getString("user_contact"),         // User Contact
                    rs.getTimestamp("created_at"),        // Created At
                    rs.getTimestamp("updated_at"),        // Updated At
                    rs.getString("notes"),                // Notes
                    rs.getInt("category_id"),             // Category ID
                    attachments.isEmpty() ? null : attachments.get(0).getFilePath(), // Single file path or null
                    resolutionTime                                // Resolution Time
                );

                tickets.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle error
        }
        return tickets;
    }

    
    @Override
    public List<Ticket> getTicketsByStatus(String status) {
        String query = "SELECT * FROM tickets WHERE status = ?";
        List<Ticket> tickets = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Fetch attachments for the ticket
                List<Attachment> attachments = getAttachmentsByArId(resultSet.getInt("ar_id"));

                // Optional: Calculate resolution time if applicable
                Integer resolutionTime = calculateResolutionTime(resultSet.getInt("ar_id"));

                // Map result set to the Ticket object
                Ticket ticket = new Ticket(
                    resultSet.getInt("ar_id"),                   // AR ID
                    resultSet.getString("subject"),              // Subject
                    resultSet.getString("description"),          // Description
                    resultSet.getString("priority"),             // Priority
                    resultSet.getString("severity"),             // Severity
                    resultSet.getString("status"),               // Status
                    resultSet.getString("assigned_to"),          // Assigned To
                    resultSet.getString("created_by"),           // Created By
                    resultSet.getString("user_email"),           // User Email
                    resultSet.getString("user_contact"),         // User Contact
                    resultSet.getTimestamp("created_at"),        // Created At
                    resultSet.getTimestamp("updated_at"),        // Updated At
                    resultSet.getString("notes"),                // Notes
                    resultSet.getInt("category_id"),             // Category ID
                    attachments.isEmpty() ? null : attachments.get(0).getFilePath(), // Single file path or null
                    resolutionTime                                // Resolution Time
                );

                tickets.add(ticket); // Add the ticket to the list
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle exception
        }

        return tickets;
    }


    @Override
    public List<Ticket> getAllTickets() {
        String query = "SELECT * FROM tickets";
        List<Ticket> tickets = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Fetch attachments for the ticket
                List<Attachment> attachments = getAttachmentsByArId(resultSet.getInt("ar_id"));

                // Optional: Calculate resolution time (if applicable)
                Integer resolutionTime = calculateResolutionTime(resultSet.getInt("ar_id"));

                // Map result set to the Ticket object
                Ticket ticket = new Ticket(
                    resultSet.getInt("ar_id"),                   // AR ID
                    resultSet.getString("subject"),              // Subject
                    resultSet.getString("description"),          // Description
                    resultSet.getString("priority"),             // Priority
                    resultSet.getString("severity"),             // Severity
                    resultSet.getString("status"),               // Status
                    resultSet.getString("assigned_to"),          // Assigned To
                    resultSet.getString("created_by"),           // Created By
                    resultSet.getString("user_email"),           // User Email
                    resultSet.getString("user_contact"),         // User Contact
                    resultSet.getTimestamp("created_at"),        // Created At
                    resultSet.getTimestamp("updated_at"),        // Updated At
                    resultSet.getString("notes"),                // Notes
                    resultSet.getInt("category_id"),             // Category ID
                    attachments.isEmpty() ? null : attachments.get(0).getFilePath(), // Single file path or null
                    resolutionTime                                // Resolution Time
                );

                tickets.add(ticket); // Add the ticket to the list
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle exception
        }

        return tickets;
    }

    // Additional helper methods for getting the status of tickets by priority or severity
    
    @Override
    public void logTicketStatusChange(TicketStatusLog log) {
        try {
            String query = "INSERT INTO ticket_status_logs (ar_id, previous_status, new_status, changed_at) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, log.getArId());
            pstmt.setString(2, log.getPreviousStatus());
            pstmt.setString(3, log.getNewStatus());
            pstmt.setTimestamp(4, log.getChangedAt());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Ticket> getTicketsByUser(String username) {
        List<Ticket> tickets = new ArrayList<>();
        try {
            String query = "SELECT * FROM tickets WHERE created_by = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    @Override
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT category FROM categories";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public void deleteAttachmentsByArId(int arId) {
        try {
            String query = "DELETE FROM ticket_attachments WHERE ar_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, arId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public double getAverageResolutionTimeBySeverity(String severity) {
        String query = "SELECT AVG(resolution_time) FROM tickets WHERE severity = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, severity);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;  // Default if no data
    }

    @Override
    public double getAverageResolutionTimeByPriority(String priority) {
        String query = "SELECT AVG(resolution_time) FROM tickets WHERE priority = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, priority);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;  // Default if no data
    }
    
    @Override
    public void addUser(User user) {
        try {
            String query = "INSERT INTO users (username, password, role, email, created_at) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.setString(4, user.getEmail().isEmpty() ? null : user.getEmail()); // Insert NULL if email is empty
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            String query = "UPDATE users SET password = ?, role = ?, email = ?, updated_at = CURRENT_TIMESTAMP WHERE username = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getRole());
            pstmt.setString(3, user.getEmail().isEmpty() ? null : user.getEmail()); // Insert NULL if email is empty
            pstmt.setString(4, user.getUsername());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            String query = "SELECT * FROM users";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String email = rs.getString("email"); // Handle null email
                users.add(new User(
                        rs.getString("username"),
                        rs.getString("password"), // Fetch the password (or handle as necessary)
                        rs.getString("role"),
                        email != null ? email : ""  // Default to empty string if email is null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    
    @Override
    public User getUserByUsername(String username) {
        User user = null;
        try {
            String query = "SELECT username, password, role, email FROM users WHERE username = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public int getActiveTicketCount() {
        int count = 0;
        try {
            String query = "SELECT COUNT(*) AS count FROM tickets WHERE status IN ('Open', 'In Progress')";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<Ticket> getTicketsByAssignee(String assignee) {
        // Fetch tickets assigned to a particular user
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM tickets WHERE assigned_to = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, assignee);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs)); // Map ResultSet to Ticket
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }


	@Override
	public void addTicketPriority(String level, String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTicketSeverity(String level, int resolutionTime, String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean testConnection() {
	    try {
	        String query = "SELECT 1";
	        PreparedStatement stmt = connection.prepareStatement(query);
	        ResultSet rs = stmt.executeQuery();
	        return rs.next();
	    } catch (SQLException e) {
	        return false;
	    }
	}


	public boolean updateTicketStatus(int arId, String newStatus) {
	    String query = "UPDATE tickets SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE ar_id = ?";
	    
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, newStatus);
	        stmt.setInt(2, arId);
	        int rowsAffected = stmt.executeUpdate();
	        
	        return rowsAffected > 0; // Returns true if the status was updated successfully
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // Returns false if there was an error
	    }
	}

	@Override
	public List<Article> getArticlesBySearchTerm(String searchTerm) {
	    List<Article> articles = new ArrayList<>();
	    String query = "SELECT * FROM articles WHERE title LIKE ? OR content LIKE ?";
	    
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        // Add wildcards to the search term for SQL LIKE query
	        String searchPattern = "%" + searchTerm + "%";
	        
	        // Set the search term in the PreparedStatement
	        stmt.setString(1, searchPattern);
	        stmt.setString(2, searchPattern);
	        
	        ResultSet rs = stmt.executeQuery();
	        
	        // Process the result set and create Article objects
	        while (rs.next()) {
	            String title = rs.getString("title");
	            String content = rs.getString("content");
	            String createdBy = rs.getString("created_by");
	            Timestamp createdAt = rs.getTimestamp("created_at");
	            Timestamp lastUpdated = rs.getTimestamp("last_updated");
	            
	            // Create an Article object and add it to the list
	            articles.add(new Article(title, content, createdBy, createdAt, lastUpdated));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return articles;
	}
	
	private void createResolutionTimeChart() {
	    Map<Integer, Integer> resolutionTimes = database.getResolutionTimes();

	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    for (Map.Entry<Integer, Integer> entry : resolutionTimes.entrySet()) {
	        dataset.addValue(entry.getValue(), "Resolution Time (Hours)", "Ticket " + entry.getKey());
	    }

	    JFreeChart chart = ChartFactory.createBarChart(
	        "Resolution Time by Ticket",
	        "Ticket ID",
	        "Resolution Time (Hours)",
	        dataset,
	        PlotOrientation.VERTICAL,
	        true,
	        true,
	        false
	    );

	    ChartPanel chartPanel = new ChartPanel(chart);
	    metricsPanel.add(chartPanel);
	}

	
	@Override
	public Map<Integer, Integer> getResolutionTimes() {
	    String query = """
	        SELECT 
	            t.ar_id AS ticket_id,
	            TIMESTAMPDIFF(HOUR,
	                (SELECT changed_at FROM ticket_status_log 
	                 WHERE ticket_id = t.ar_id AND status = 'In Progress' LIMIT 1),
	                (SELECT changed_at FROM ticket_status_log 
	                 WHERE ticket_id = t.ar_id AND status = 'Resolved' LIMIT 1)
	            ) AS resolution_time
	        FROM tickets t;
	    """;
	    Map<Integer, Integer> resolutionTimes = new HashMap<>();

	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        ResultSet resultSet = statement.executeQuery();

	        while (resultSet.next()) {
	            int ticketId = resultSet.getInt("ticket_id");
	            int resolutionTime = resultSet.getInt("resolution_time");
	            resolutionTimes.put(ticketId, resolutionTime);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return resolutionTimes;
	}


	@Override
	public Ticket getTicketByArId(int arId) {
	    Ticket ticket = null;
	    try {
	        // Query to fetch ticket details
	        String query = "SELECT * FROM tickets WHERE ar_id = ?";
	        PreparedStatement stmt = connection.prepareStatement(query);
	        stmt.setInt(1, arId);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            // Fetch attachments for the ticket
	            List<Attachment> attachments = getAttachmentsByArId(arId);

	            // Optional: Calculate resolution time if applicable
	            Integer resolutionTime = calculateResolutionTime(arId); // Custom logic

	            // Map result set to Ticket
	            ticket = new Ticket(
	                rs.getInt("ar_id"),
	                rs.getString("subject"),
	                rs.getString("description"),
	                rs.getString("priority"),
	                rs.getString("severity"),
	                rs.getString("status"),
	                rs.getString("assigned_to"),
	                rs.getString("created_by"),
	                rs.getString("user_email"),
	                rs.getString("user_contact"),
	                rs.getTimestamp("created_at"),
	                rs.getTimestamp("updated_at"),
	                rs.getString("notes"),
	                rs.getInt("category_id"),
	                attachments.isEmpty() ? null : attachments.get(0).getFilePath(), // Single file path or null
	                resolutionTime
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // Log exception
	    }
	    return ticket;
	}


		@Override
	    public Connection getConnection() throws SQLException {
	        return DriverManager.getConnection(url, username, password);
	    }
	    
		public List<Attachment> getAttachmentsByArId(int arId) {
		    List<Attachment> attachments = new ArrayList<>();
		    String query = "SELECT * FROM ticket_attachments WHERE ar_id = ?";

		    try (PreparedStatement statement = connection.prepareStatement(query)) {
		        statement.setInt(1, arId);
		        ResultSet resultSet = statement.executeQuery();

		        while (resultSet.next()) {
		            attachments.add(new Attachment(resultSet.getString("file_path")));
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return attachments;
		}

	   
	    @Override
	    public void updateTicketNotes(int ticketId, String notes) {
	        String query = "UPDATE tickets SET notes = ? WHERE id = ?";
	        
	        try (Connection conn = getConnection();
	             PreparedStatement ps = conn.prepareStatement(query)) {
	             
	            ps.setString(1, notes);
	            ps.setInt(2, ticketId);
	            ps.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // Handle error
	        }
	    }

	    // Implement other methods for interacting with the database
	    
	    private Integer calculateResolutionTime(int arId) {
	        String query = """
	            SELECT TIMESTAMPDIFF(HOUR,
	                (SELECT changed_at FROM ticket_status_log WHERE ticket_id = ? AND status = 'In Progress' LIMIT 1),
	                (SELECT changed_at FROM ticket_status_log WHERE ticket_id = ? AND status = 'Resolved' LIMIT 1)
	            ) AS resolution_time
	        """;

	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setInt(1, arId);
	            stmt.setInt(2, arId);
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                return rs.getInt("resolution_time");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return null; // Return null if resolution time cannot be calculated
	    }

	    
	    @Override
	    public List<Ticket> getTicketsBySeverity(String severity) {
	        String query = "SELECT * FROM tickets WHERE severity = ?";
	        List<Ticket> tickets = new ArrayList<>();

	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setString(1, severity);
	            ResultSet resultSet = statement.executeQuery();

	            while (resultSet.next()) {
	                Ticket ticket = mapResultSetToTicket(resultSet);
	                tickets.add(ticket);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); // Log the exception
	        }

	        return tickets;
	    }

	    @Override
	    public List<Ticket> getTicketsByPriority(String priority) {
	        String query = "SELECT * FROM tickets WHERE priority = ?";
	        List<Ticket> tickets = new ArrayList<>();

	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setString(1, priority);
	            ResultSet resultSet = statement.executeQuery();

	            while (resultSet.next()) {
	                Ticket ticket = mapResultSetToTicket(resultSet);
	                tickets.add(ticket);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); // Log the exception
	        }

	        return tickets;
	    }

	    @Override
	    public Map<String, Double> getAverageResolutionTimeBySeverity() {
	        Map<String, Double> resolutionTimeMap = new HashMap<>();
	        String query = "SELECT severity, AVG(TIMESTAMPDIFF(HOUR, created_at, updated_at)) AS avg_resolution_time FROM tickets GROUP BY severity";

	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            ResultSet resultSet = statement.executeQuery();

	            while (resultSet.next()) {
	                resolutionTimeMap.put(resultSet.getString("severity"), resultSet.getDouble("avg_resolution_time"));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); // Log the exception
	        }

	        return resolutionTimeMap;
	    }
	    
	    @Override
	    public Map<String, Double> getAverageResolutionTimeByPriority() {
	        Map<String, Double> resolutionTimeMap = new HashMap<>();
	        String query = "SELECT priority, AVG(TIMESTAMPDIFF(HOUR, created_at, updated_at)) AS avg_resolution_time FROM tickets GROUP BY priority";

	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            ResultSet resultSet = statement.executeQuery();

	            while (resultSet.next()) {
	                resolutionTimeMap.put(resultSet.getString("priority"), resultSet.getDouble("avg_resolution_time"));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace(); // Log the exception
	        }

	        return resolutionTimeMap;
	    }

	    
	    @Override
	    public void addArticle(Article article) {
	        String query = "INSERT INTO articles (title, content, created_by, created_at, last_updated) VALUES (?, ?, ?, ?, ?)";
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setString(1, article.getTitle());
	            stmt.setString(2, article.getContent());
	            stmt.setString(3, article.getCreatedBy());
	            stmt.setTimestamp(4, article.getCreatedAt());
	            stmt.setTimestamp(5, article.getLastUpdated());
	            stmt.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException("Error adding article: " + e.getMessage());
	        }
	    }


	    
}




