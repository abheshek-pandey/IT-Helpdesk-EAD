package com.ithelpdesk.model;

public class PriorityAndSeverity {
    // Priority-related fields
    private String priorityLevel;       // Priority level (Very High, High, Moderate, Low)
    private String priorityDescription; // Description of the priority

    // Severity-related fields
    private String severityLevel;       // Severity level (1, 2, 3, 4)
    private int targetResolutionTime;   // Target resolution time in days
    private String severityDescription; // Description of the severity

    // Constructor for Priority
    public PriorityAndSeverity(String priorityLevel, String priorityDescription) {
        this.priorityLevel = priorityLevel;
        this.priorityDescription = priorityDescription;
    }

    // Constructor for Severity
    public PriorityAndSeverity(String severityLevel, int targetResolutionTime, String severityDescription) {
        this.severityLevel = severityLevel;
        this.targetResolutionTime = targetResolutionTime;
        this.severityDescription = severityDescription;
    }

    // Combined constructor for flexibility
    public PriorityAndSeverity(String priorityLevel, String priorityDescription,
                                String severityLevel, int targetResolutionTime, String severityDescription) {
        this.priorityLevel = priorityLevel;
        this.priorityDescription = priorityDescription;
        this.severityLevel = severityLevel;
        this.targetResolutionTime = targetResolutionTime;
        this.severityDescription = severityDescription;
    }

    // Getters and Setters
    public String getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getPriorityDescription() {
        return priorityDescription;
    }

    public void setPriorityDescription(String priorityDescription) {
        this.priorityDescription = priorityDescription;
    }

    public String getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }

    public int getTargetResolutionTime() {
        return targetResolutionTime;
    }

    public void setTargetResolutionTime(int targetResolutionTime) {
        this.targetResolutionTime = targetResolutionTime;
    }

    public String getSeverityDescription() {
        return severityDescription;
    }

    public void setSeverityDescription(String severityDescription) {
        this.severityDescription = severityDescription;
    }

    @Override
    public String toString() {
        return "PriorityAndSeverity{" +
                "priorityLevel='" + priorityLevel + '\'' +
                ", priorityDescription='" + priorityDescription + '\'' +
                ", severityLevel='" + severityLevel + '\'' +
                ", targetResolutionTime=" + targetResolutionTime +
                ", severityDescription='" + severityDescription + '\'' +
                '}';
    }
}