package com.ithelpdesk.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {

    private static Properties properties;

    // Load the configuration file
    public static Properties loadConfig(String filePath) throws IOException {
        if (properties == null) {
            properties = new Properties();
            try (FileInputStream fis = new FileInputStream(filePath)) {
                properties.load(fis);
                System.out.println("Configuration loaded from: " + filePath);
            } catch (IOException e) {
                throw new IOException("Failed to load configuration file from: " + filePath, e);
            }
            validateConfig(properties);
        }
        return properties;
    }

    // Validate required keys and their values
    private static void validateConfig(Properties config) {
        String[] requiredKeys = {
            "db.environment", "db.type",
            "mysql.local.url", "mysql.local.username", "mysql.local.password",
            "attachments.storage"
        };

        for (String key : requiredKeys) {
            if (!config.containsKey(key)) {
                throw new IllegalArgumentException("Missing required configuration key: " + key);
            }

            // Check if critical values are non-empty (e.g., URLs, credentials)
            String value = config.getProperty(key);
            if (value == null || value.trim().isEmpty()) {
                throw new IllegalArgumentException("Configuration value for key " + key + " is empty.");
            }
        }
    }

    // Get specific configuration
    public static String getConfig(String key) {
        return getConfig(key, null);
    }

    // Get specific configuration with optional default value
    public static String getConfig(String key, String defaultValue) {
        if (properties == null) {
            throw new IllegalStateException("Configuration not loaded. Call loadConfig first.");
        }
        return properties.getProperty(key, defaultValue);
    }
}
