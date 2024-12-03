package com.ithelpdesk.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SystemLogger {
    private static final String LOG_FILE_PATH = "logs/system.log";

    // Log an info message
    public static void logInfo(String message) {
        log("INFO", message);
    }

    // Log an error message
    public static void logError(String message) {
        log("ERROR", message);
    }

    // Write a log entry
    private static void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logEntry = String.format("[%s] [%s] %s%n", timestamp, level, message);

        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) {
            writer.write(logEntry);
        } catch (IOException e) {
            System.err.println("Failed to write log entry: " + e.getMessage());
        }
    }
}
