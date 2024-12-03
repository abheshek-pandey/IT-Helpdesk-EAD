package com.ithelpdesk.util;

public class PasswordUtil {
    public static String hashPassword(String plainPassword) {
        // Directly return the plaintext password without hashing
        return plainPassword;
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        // Compare plaintext passwords directly
        return plainPassword.equals(hashedPassword);
    }
}
