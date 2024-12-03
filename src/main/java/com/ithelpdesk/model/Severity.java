package com.ithelpdesk.model;

public enum Severity {
    LEVEL_1("1"),
    LEVEL_2("2"),
    LEVEL_3("3"),
    LEVEL_4("4");

    private final String value;

    Severity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Severity fromValue(String value) {
        for (Severity severity : Severity.values()) {
            if (severity.getValue().equals(value)) {
                return severity;
            }
        }
        throw new IllegalArgumentException("Unknown severity value: " + value);
    }
}
