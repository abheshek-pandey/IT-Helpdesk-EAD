package com.ithelpdesk.ui;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import com.ithelpdesk.model.User;

public class UserTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Username", "Role", "Email"};
    private List<User> users;

    public UserTableModel(List<User> users) {
        this.users = users;
    }

    @Override
    public int getRowCount() {
        return users != null ? users.size() : 0;  // Added a null check
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (users == null || rowIndex >= users.size()) {
            return null;  // Handle out-of-bounds error gracefully
        }
        
        User user = users.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return user.getUsername();
            case 1:
                return user.getRole();
            case 2:
                return (user.getEmail() != null && !user.getEmail().isEmpty()) ? user.getEmail() : "N/A";  // Display N/A if email is empty
            default:
                return null;
        }
    }

    public User getUserAt(int rowIndex) {
        return (users != null && rowIndex < users.size()) ? users.get(rowIndex) : null;  // Handle out-of-bounds errors
    }

    public void setUsers(List<User> users) {
        this.users = users;
        fireTableDataChanged();  // Notify the table to update with the new data
    }
}
