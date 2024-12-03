package com.ithelpdesk.dao;

import com.ithelpdesk.model.*;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.List;
import java.util.Map;

public interface DatabaseInterface {

    Connection getConnection() throws SQLException;

    // General Database Management
    void connect();
    void close();
    
    

    // User Management
    List<User> getAllUsers();
    User getUserByUsername(String username);
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(String username);

    // Ticket Management
    Ticket getTicketByArId(int arId);  // Fetch ticket by AR ID
    List<Ticket> getTicketsByUser(String username);  // Get tickets by user
    List<Ticket> getTicketsByAssignee(String assignee);  // Get tickets assigned to a tech
    void addTicket(Ticket ticket);  // Add a new ticket
    void updateTicket(Ticket ticket);  // Update ticket details
    boolean updateTicketStatus(int arId, String newStatus);  // Update ticket status (e.g., close)
    void deleteTicket(int arId);  // Delete ticket by AR ID

    // Attachment Management
    void addAttachment(int arId, String filePath);  // Add an attachment to a ticket
    List<Attachment> getAttachmentsByArId(int arId);  // Get attachments related to a ticket
    void deleteAttachmentsByArId(int arId);  // Delete all attachments related to a ticket

    // Category Management
    List<String> getAllCategories();  // Get all ticket categories
    void addCategory(Category category);  // Add a new category
    void deleteCategory(int categoryId);  // Delete category by ID

    // Knowledge Base Category Management
    List<String> getAllKnowledgeBaseCategories();  // Get all knowledge base categories
    List<String> getKnowledgeBaseSubcategories(String category);  // Get subcategories by category
    void addKnowledgeBaseCategory(KnowledgeBaseCategory category);  // Add a new knowledge base category
    void deleteKnowledgeBaseCategory(int categoryId);  // Delete knowledge base category by ID

    // Article Management
    List<Article> getAllArticles();  // Get all articles
    Article getArticleByTitle(String title);  // Get article by title
    void addArticle(Article article);  // Add a new article
    void updateArticle(Article article);  // Update an article
    void deleteArticle(String title);  // Delete article by title

    // Priority Management
    List<String> getAllPriorities();  // Get all priority levels
    void addPriority(PriorityAndSeverity priority);  // Add a new priority
    void deletePriority(String priorityLevel);  // Delete priority level by name

    // Severity Management
    List<String> getAllSeverities();  // Get all severity levels
    void addSeverity(PriorityAndSeverity severity);  // Add a new severity
    void deleteSeverity(String severityLevel);  // Delete severity level by name

    // Metrics
    double getAverageResolutionTimeByPriority(String priority);  // Get average resolution time by priority
    double getAverageResolutionTimeBySeverity(String severity);  // Get average resolution time by severity

    // Audit Logs
    void addAuditLog(AuditLog log);  // Add a new audit log entry
    List<AuditLog> getAllAuditLogs();  // Get all audit logs
    List<AuditLog> getAuditLogsByUsername(String username);  // Get audit logs by username

    // Ticket Status Logs
    void addTicketStatusLog(TicketStatusLog log);  // Add a ticket status log entry
    List<TicketStatusLog> getTicketStatusLogsByArId(int arId);  // Get status logs for a ticket

    // Active Ticket Count
    int getActiveTicketCount();  // Get the count of active tickets

    // Log Ticket Status Changes
    void logTicketStatusChange(TicketStatusLog log);  // Log a status change for a ticket

    // Method to get tickets by status
    List<Ticket> getTicketsByStatus(String status);  // Get tickets by status (e.g., Open, Closed)

    // Method to get tickets by severity
    List<Ticket> getTicketsBySeverity(String severity);  // Get tickets by severity (e.g., Low, High)

     // Get all tickets in the system
    
    // Ticket Category Methods
    void addTicketCategory(String category, String subcategory);  // Add a new ticket category and subcategory
    List<String> getTicketCategories();  // Get all ticket categories
    List<String> getSubcategoriesByCategory(String category);  // Get subcategories for a specific category

    // Ticket Priority Methods
    void addTicketPriority(String level, String description);  // Add a new ticket priority level
    List<String> getTicketPriorities();  // Get all ticket priorities

    // Ticket Severity Methods
    void addTicketSeverity(String level, int resolutionTime, String description);  // Add a new severity level with resolution time
    List<String> getTicketSeverities();  // Get all ticket severities

    // Test Database Connection
    boolean testConnection();  // Test the database connection

    // Update Ticket Assignment
    void updateTicketAssignment(int ticketId, String assignedTo);  // Update the assignment of a ticket
	List<Article> getArticlesBySearchTerm(String searchTerm);
	
    // Abstract method for fetching tickets by user and status
    List<Ticket> getTicketsByUserStatus(String user, String status);

    // Abstract method for updating ticket notes
    void updateTicketNotes(int ticketId, String notes);
    
    // Ticket Management
    List<Ticket> getAllTickets();
    List<Ticket> getTicketsByPriority(String priority);

    // Resolution Time Analytics
    Map<String, Double> getAverageResolutionTimeBySeverity();
    Map<String, Double> getAverageResolutionTimeByPriority();

	Map<Integer, Integer> getResolutionTimes();

    // Attachments

}
