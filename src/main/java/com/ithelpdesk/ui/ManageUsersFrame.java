
package com.ithelpdesk.ui;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.User;

public class ManageUsersFrame extends JPanel {
   private final MainFrame mainFrame;
   private final DatabaseInterface database;
   private JTable usersTable;
   private DefaultTableModel tableModel;
   private JButton addUserButton;
   private JButton editUserButton;
   private JButton deleteUserButton;
   private JButton refreshButton;

   public ManageUsersFrame(MainFrame mainFrame, DatabaseInterface database) {
       this.mainFrame = mainFrame;
       this.database = database;
       initComponents();
   }


   private void initComponents() {
       setLayout(new BorderLayout(10, 10));
       setBackground(new Color(240, 242, 245));

       add(createHeaderPanel(), BorderLayout.NORTH);
       add(createTablePanel(), BorderLayout.CENTER);
       add(createButtonPanel(), BorderLayout.SOUTH);

       loadUsers();
   }

   private JPanel createHeaderPanel() {
       JPanel panel = new JPanel();
       panel.setBackground(new Color(240, 242, 245));
       
       JLabel headerLabel = new JLabel("Manage Users");
       headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
       
       panel.add(headerLabel);
       return panel;
   }

   private JScrollPane createTablePanel() {
       String[] columns = {"Username", "Role", "Email"};
       tableModel = new DefaultTableModel(columns, 0);
       usersTable = new JTable(tableModel);
       usersTable.setFillsViewportHeight(true);
       return new JScrollPane(usersTable);
   }

   private JPanel createButtonPanel() {
       JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
       panel.setBackground(new Color(240, 242, 245));

       addUserButton = createStyledButton("Add User", new Color(46, 204, 113));
       editUserButton = createStyledButton("Edit User", new Color(52, 152, 219));
       deleteUserButton = createStyledButton("Delete User", new Color(231, 76, 60));
       refreshButton = createStyledButton("Refresh", new Color(52, 152, 219));

       setupButtonActions();

       panel.add(addUserButton);
       panel.add(editUserButton);
       panel.add(deleteUserButton);
       panel.add(refreshButton);

       return panel;
   }

   private JButton createStyledButton(String text, Color bgColor) {
       JButton button = new JButton(text);
       button.setFont(new Font("Arial", Font.BOLD, 14));
       button.setBackground(bgColor);
       button.setForeground(Color.WHITE);
       button.setFocusPainted(false);
       button.setBorderPainted(false);
       button.setOpaque(true);
       return button;
   }

   private void setupButtonActions() {
       addUserButton.addActionListener(e -> handleAddUser());
       editUserButton.addActionListener(e -> handleEditUser());
       deleteUserButton.addActionListener(e -> handleDeleteUser());
       refreshButton.addActionListener(e -> loadUsers());
   }

   private void handleAddUser() {
       // Replace with actual user addition logic
       String username = JOptionPane.showInputDialog(this, "Enter username:");
       if (username != null && !username.trim().isEmpty()) {
           tableModel.addRow(new Object[]{username, "User", ""});
       }
   }

   private void handleEditUser() {
       int selectedRow = usersTable.getSelectedRow();
       if (selectedRow >= 0) {
           String username = (String) usersTable.getValueAt(selectedRow, 0);
           // Replace with actual edit logic
           JOptionPane.showMessageDialog(this, "Editing user: " + username);
       }
   }

   private void handleDeleteUser() {
       int selectedRow = usersTable.getSelectedRow();
       if (selectedRow >= 0) {
           String username = (String) usersTable.getValueAt(selectedRow, 0);
           int confirm = JOptionPane.showConfirmDialog(this,
               "Are you sure you want to delete user: " + username + "?",
               "Confirm Delete",
               JOptionPane.YES_NO_OPTION);
           
           if (confirm == JOptionPane.YES_OPTION) {
               tableModel.removeRow(selectedRow);
           }
       }
   }

   
   private void loadUsers() {
	    try {
	        List<User> users = database.getAllUsers();
	        tableModel.setRowCount(0);
	        for (User user : users) {
	            tableModel.addRow(new Object[]{
	                user.getUsername(),
	                user.getRole(),
	                user.getEmail() != null ? user.getEmail() : "N/A"
	            });
	        }
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(this,
	            "Error loading users: " + e.getMessage(),
	            "Error",
	            JOptionPane.ERROR_MESSAGE);
	    }
	}
}