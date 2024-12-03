package com.ithelpdesk.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.Ticket;
import java.sql.Timestamp;

public class TicketCreationFrame extends JPanel {
   private final MainFrame mainFrame;
   private final DatabaseInterface database;
   private JTextField subjectField, emailField, contactField;
   private JTextArea descriptionArea;
   private JComboBox<String> categoryDropdown;
   private JComboBox<String> priorityDropdown;
   private JComboBox<String> severityDropdown;
   private JButton createTicketButton;
   private JButton attachFileButton;
   private File attachedFile; // File to store the attached file
   private Ticket ticket;

   public TicketCreationFrame(MainFrame mainFrame, DatabaseInterface database) {
       this.mainFrame = mainFrame;
       this.database = database;
       setLayout(new BorderLayout(10, 10));
       setBackground(new Color(240, 242, 245));
       initComponents();
   }

   private void initComponents() {
       add(createHeaderPanel(), BorderLayout.NORTH);
       add(createFormPanel(), BorderLayout.CENTER);
       add(createButtonPanel(), BorderLayout.SOUTH);
   }

   private void handleTicketCreation() {
	    if (validateForm()) {
	        try {
	            // Create a new ticket and include the attachment (if available)
	            Ticket ticket = new Ticket(
	                0, // ar_id will be auto-generated
	                subjectField.getText(),                // Subject
	                descriptionArea.getText(),             // Description
	                priorityDropdown.getSelectedItem().toString(), // Priority
	                severityDropdown.getSelectedItem().toString(), // Severity
	                "Open",                                // Ticket status initially set to "Open"
	                null,                                  // assigned_to (left as null)
	                mainFrame.getCurrentUser(),            // Created by
	                emailField.getText(),                  // user_email
	                contactField.getText(),                // user_contact
	                new Timestamp(System.currentTimeMillis()), // Created At
	                new Timestamp(System.currentTimeMillis()), // Updated At
	                "",                                    // Notes (left as empty string)
	                ((String) categoryDropdown.getSelectedItem()).equals("Other") ? 10 : categoryDropdown.getSelectedIndex() + 1, // Category ID
	                attachedFile != null ? attachedFile.getAbsolutePath() : null, // Attach file path if selected
	                null                                   // Resolution time (not applicable for new tickets)
	            );

	            // Add the ticket to the database
	            database.addTicket(ticket);

	            // Notify the user
	            JOptionPane.showMessageDialog(this, "Ticket created successfully");

	            // Clear the form for new input
	            clearForm();
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(
	                this,
	                "Error creating ticket: " + e.getMessage(),
	                "Error",
	                JOptionPane.ERROR_MESSAGE
	            );
	        }
	    }
	}


   private JPanel createHeaderPanel() {
       JPanel panel = new JPanel();
       panel.setBackground(new Color(240, 242, 245));
       
       JLabel headerLabel = new JLabel("Create New Ticket");
       headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
       panel.add(headerLabel);
       
       return panel;
   }

   private JPanel createFormPanel() {
       JPanel panel = new JPanel(new GridBagLayout());
       panel.setBackground(new Color(240, 242, 245));
       GridBagConstraints gbc = new GridBagConstraints();
       gbc.insets = new Insets(5, 5, 5, 5);
       gbc.anchor = GridBagConstraints.WEST;
       
       subjectField = new JTextField(30);
       descriptionArea = new JTextArea(5, 30);
       descriptionArea.setLineWrap(true);
       emailField = new JTextField(30); // email field
       contactField = new JTextField(30); // contact field
       
       categoryDropdown = new JComboBox<>(new String[]{"About", "News", "Organization", "Activity", "Award", "Membership", "Other"});
       priorityDropdown = new JComboBox<>(new String[]{"Very High", "High", "Moderate", "Low"});
       severityDropdown = new JComboBox<>(new String[]{"1", "2", "3", "4"});

       addFormField(panel, "Subject:", subjectField, gbc, 0);
       addFormField(panel, "Description:", new JScrollPane(descriptionArea), gbc, 1);
       addFormField(panel, "Category:", categoryDropdown, gbc, 2);
       addFormField(panel, "Priority:", priorityDropdown, gbc, 3);
       addFormField(panel, "Severity:", severityDropdown, gbc, 4);
       addFormField(panel, "Email:", emailField, gbc, 5);  // Added Email field
       addFormField(panel, "Contact:", contactField, gbc, 6); // Added Contact field

       return panel;
   }

   private void addFormField(JPanel panel, String label, Component component, GridBagConstraints gbc, int row) {
       gbc.gridx = 0;
       gbc.gridy = row;
       panel.add(new JLabel(label), gbc);
       
       gbc.gridx = 1;
       gbc.fill = (component instanceof JScrollPane) ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;
       panel.add(component, gbc);
   }

   private JPanel createButtonPanel() {
       JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
       panel.setBackground(new Color(240, 242, 245));

       createTicketButton = new JButton("Create Ticket");
       createTicketButton.setFont(new Font("Arial", Font.BOLD, 14));
       createTicketButton.setBackground(new Color(46, 204, 113));
       createTicketButton.setForeground(Color.WHITE);
       createTicketButton.setFocusPainted(false);
       createTicketButton.setBorderPainted(false);
       createTicketButton.setOpaque(true);
       
       createTicketButton.addActionListener(e -> handleTicketCreation());
       
       createTicketButton.addMouseListener(new MouseAdapter() {
           public void mouseEntered(MouseEvent e) {
               createTicketButton.setBackground(new Color(39, 174, 96));
           }
           public void mouseExited(MouseEvent e) {
               createTicketButton.setBackground(new Color(46, 204, 113));
           }
       });

       attachFileButton = new JButton("Attach File");
       attachFileButton.setFont(new Font("Arial", Font.BOLD, 14));
       attachFileButton.setBackground(new Color(52, 152, 219));
       attachFileButton.setForeground(Color.WHITE);
       attachFileButton.setFocusPainted(false);
       attachFileButton.setBorderPainted(false);
       attachFileButton.setOpaque(true);
       
       attachFileButton.addActionListener(e -> handleAttachFile());
       
       panel.add(attachFileButton);
       panel.add(createTicketButton);
       return panel;
   }

   private void handleAttachFile() {
       // Open file chooser to select an attachment
       JFileChooser fileChooser = new JFileChooser();
       fileChooser.setDialogTitle("Select File to Attach");
       int result = fileChooser.showOpenDialog(this);
       if (result == JFileChooser.APPROVE_OPTION) {
           attachedFile = fileChooser.getSelectedFile(); // Save the selected file
           JOptionPane.showMessageDialog(this, "File attached: " + attachedFile.getName());
       }
   }

   private boolean validateForm() {
       if (subjectField.getText().trim().isEmpty()) {
           JOptionPane.showMessageDialog(this, "Subject is required");
           return false;
       }
       if (descriptionArea.getText().trim().isEmpty()) {
           JOptionPane.showMessageDialog(this, "Description is required");
           return false;
       }
       if (emailField.getText().trim().isEmpty()) {
           JOptionPane.showMessageDialog(this, "Email is required");
           return false;
       }
       if (contactField.getText().trim().isEmpty()) {
           JOptionPane.showMessageDialog(this, "Contact is required");
           return false;
       }
       return true;
   }

   private void clearForm() {
       subjectField.setText("");
       descriptionArea.setText("");
       categoryDropdown.setSelectedIndex(0);
       priorityDropdown.setSelectedIndex(0);
       severityDropdown.setSelectedIndex(0);
       emailField.setText("");  // Clear email field
       contactField.setText("");  // Clear contact field
   }
}
