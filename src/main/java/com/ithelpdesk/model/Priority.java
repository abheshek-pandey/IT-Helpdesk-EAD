package com.ithelpdesk.model;

public enum Priority {
    VERY_HIGH("Very High"),
    HIGH("High"),
    MODERATE("Moderate"),
    LOW("Low");

    private final String value;

    Priority(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Priority fromValue(String value) {
        for (Priority priority : Priority.values()) {
            if (priority.getValue().equals(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Unknown priority value: " + value);
    }
}
