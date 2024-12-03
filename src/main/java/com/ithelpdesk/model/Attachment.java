package com.ithelpdesk.model;

import java.sql.Timestamp;

public class Attachment {
    private int id;                 // Attachment ID (if needed)
    private int arId;               // Associated ticket AR ID
    private String filePath;        // Path to the file
    private Timestamp uploadedAt;   // Timestamp when the attachment was uploaded

    // Constructor for creating a new attachment
    public Attachment(int arId, String filePath) {
        this.arId = arId;
        this.filePath = filePath;
    }

    // Constructor for database retrieval
    public Attachment(int id, int arId, String filePath, Timestamp uploadedAt) {
        this.id = id;
        this.arId = arId;
        this.filePath = filePath;
        this.uploadedAt = uploadedAt;
    }

    public Attachment(String attachmentFilePath) {
		// TODO Auto-generated constructor stub
	}

	// Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArId() {
        return arId;
    }

    public void setArId(int arId) {
        this.arId = arId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Timestamp getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Timestamp uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "arId=" + arId +
                ", filePath='" + filePath + '\'' +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}
