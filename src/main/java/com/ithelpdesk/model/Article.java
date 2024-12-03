package com.ithelpdesk.model;

import java.sql.Timestamp;

public class Article {
    private int id;                  // Article ID
    private String title;            // Article title
    private String content;          // Article content
    private String createdBy;        // Creator username
    private Timestamp createdAt;     // Creation timestamp
    private Timestamp lastUpdated;   // Last updated timestamp

    // Constructor for creating a new article
    public Article(String title, String content, String createdBy) {
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
    }

    // Constructor for database retrieval
    public Article(int id, String title, String content, String createdBy, Timestamp createdAt, Timestamp lastUpdated) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
    }
    
    public Article(String title, String content, String createdBy, Timestamp createdAt, Timestamp lastUpdated) {
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
    }


    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
