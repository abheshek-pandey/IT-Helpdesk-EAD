package com.ithelpdesk.service;

import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.User;

public class UserService {
    private final DatabaseInterface database;

    // Constructor
    public UserService(DatabaseInterface database) {
        this.database = database;
    }

    // Authenticate user (compares plain-text passwords)
    public boolean authenticate(String username, String password) {
        User user = database.getUserByUsername(username);
        return user != null && user.getPassword().equals(password);  // Direct comparison of plain-text password
    }

    // Get user by username
    public User getUserByUsername(String username) {
        return database.getUserByUsername(username);
    }

    // Other user-related services (create, update, delete) can remain as is
}
