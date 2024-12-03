package com.ithelpdesk.model;

public class EscalationPath {
    private String currentRole;     // The current role (e.g., Tech, Admin)
    private String escalateTo;     // The next role in the escalation path
    private String description;    // Description of the escalation rule

    // Constructor
    public EscalationPath(String currentRole, String escalateTo, String description) {
        this.currentRole = currentRole;
        this.escalateTo = escalateTo;
        this.description = description;
    }

    // Getters and Setters
    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    public String getEscalateTo() {
        return escalateTo;
    }

    public void setEscalateTo(String escalateTo) {
        this.escalateTo = escalateTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "EscalationPath{" +
                "currentRole='" + currentRole + '\'' +
                ", escalateTo='" + escalateTo + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
