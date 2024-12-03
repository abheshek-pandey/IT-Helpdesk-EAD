package com.ithelpdesk.model;

public class User {
    private String username;
    private String password; // Password should be hashed
    private String role;     // Admin, Tech, or User
    private String email;    // Optional, can be null

    // Constructor with email being optional
    public User(String username, String password, String role, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email != null ? email : "";  // If email is null, set it to empty string
    }


    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password; // Hash the password before saving
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email : "";  // If email is set to null, reset it to empty string
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", email='" + (email.isEmpty() ? "N/A" : email) + '\'' + // Display N/A if email is empty
                '}';
    }
}
