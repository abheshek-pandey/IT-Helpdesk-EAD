package com.ithelpdesk.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Ticket {
    private int arId;  // This corresponds to the "ar_id" column in your database
    private String subject;
    private String description;
    private String severity;
    private String priority;
    private String status;
    private String assignedTo;
    private String createdBy;
    private String userEmail;
    private String userContact;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String notes;
    private int categoryId;
    private List<Attachment> attachments;
    private Integer resolutionTime; // Time in hours between "In Progress" and "Resolved" states (optional)

    // Constructor with optional file path for attachment
    public Ticket(int arId, String subject, String description, String priority, String severity,
                  String status, String assignedTo, String createdBy, String userEmail,
                  String userContact, Timestamp createdAt, Timestamp updatedAt, String notes,
                  int categoryId, String attachmentFilePath, Integer resolutionTime) {
        this.arId = arId;
        this.subject = subject;
        this.description = description;
        this.priority = priority;
        this.severity = severity;
        this.status = status;
        this.assignedTo = assignedTo;
        this.createdBy = createdBy;
        this.userEmail = userEmail;
        this.userContact = userContact;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.notes = notes;
        this.categoryId = categoryId;
        this.resolutionTime = resolutionTime;

        // Initialize attachments list
        this.attachments = new ArrayList<>();
        if (attachmentFilePath != null) {
            this.attachments.add(new Attachment(attachmentFilePath));
        }
    }

    // Constructor for ResultSet mapping
    public Ticket(ResultSet rs, List<Attachment> attachments, Integer resolutionTime) throws SQLException {
        this.arId = rs.getInt("ar_id");
        this.subject = rs.getString("subject");
        this.description = rs.getString("description");
        this.priority = rs.getString("priority");
        this.severity = rs.getString("severity");
        this.status = rs.getString("status");
        this.assignedTo = rs.getString("assigned_to");
        this.createdBy = rs.getString("created_by");
        this.userEmail = rs.getString("user_email");
        this.userContact = rs.getString("user_contact");
        this.createdAt = rs.getTimestamp("created_at");
        this.updatedAt = rs.getTimestamp("updated_at");
        this.notes = rs.getString("notes");
        this.categoryId = rs.getInt("category_id");
        this.attachments = attachments != null ? new ArrayList<>(attachments) : new ArrayList<>();
        this.resolutionTime = resolutionTime;
    }

    // Getters and setters
    public int getArId() { return arId; }
    public String getSubject() { return subject; }
    public String getDescription() { return description; }
    public String getSeverity() { return severity; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public String getAssignedTo() { return assignedTo; }
    public String getCreatedBy() { return createdBy; }
    public String getUserEmail() { return userEmail; }
    public String getUserContact() { return userContact; }
    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public String getNotes() { return notes; }
    public int getCategoryId() { return categoryId; }
    public List<Attachment> getAttachments() { return attachments; }
    public Integer getResolutionTime() { return resolutionTime; }
    public void setResolutionTime(Integer resolutionTime) { this.resolutionTime = resolutionTime; }

    // Add this method
    public void setNotes(String notes) {
        this.notes = notes;
    }


    
    @Override
    public String toString() {
        return "Ticket{" +
                "arId=" + arId +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", severity='" + severity + '\'' +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                ", assignedTo='" + assignedTo + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userContact='" + userContact + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", notes='" + notes + '\'' +
                ", categoryId=" + categoryId +
                ", attachments=" + attachments +
                ", resolutionTime=" + resolutionTime +
                '}';
    }
}
