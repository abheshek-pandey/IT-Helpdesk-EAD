package com.ithelpdesk.model;

import java.sql.Timestamp;

public class TicketStatusLog {

    private int arId;
    private String previousStatus;
    private String newStatus;
    private Timestamp changedAt;


    // Constructor for database retrieval
    public TicketStatusLog(int arId, String previousStatus, String newStatus, Timestamp changedAt) {
        this.arId = arId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.changedAt = changedAt;
    }

    // Getters and Setters
    public int getArId() {
        return arId;
    }

    public void setArId(int arId) {
        this.arId = arId;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public Timestamp getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Timestamp changedAt) {
        this.changedAt = changedAt;
    }
}