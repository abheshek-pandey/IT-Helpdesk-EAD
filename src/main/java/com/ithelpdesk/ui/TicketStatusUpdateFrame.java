package com.ithelpdesk.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.ithelpdesk.dao.DatabaseInterface;

public class TicketStatusUpdateFrame extends JPanel {
   private final MainFrame mainFrame;
   private final DatabaseInterface database;
   private JLabel ticketIdLabel;
   private JComboBox<String> statusDropdown;
   private JTextArea notesArea;
   private JButton updateStatusButton;

   public TicketStatusUpdateFrame(MainFrame mainFrame, DatabaseInterface database) {
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

   private JPanel createHeaderPanel() {
       JPanel panel = new JPanel();
       panel.setBackground(new Color(240, 242, 245));
       
       JLabel headerLabel = new JLabel("Update Ticket Status");
       headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
       headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
       
       panel.add(headerLabel);
       return panel;
   }

   private JPanel createFormPanel() {
       JPanel panel = new JPanel(new GridBagLayout());
       panel.setBackground(new Color(240, 242, 245));
       panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

       GridBagConstraints gbc = new GridBagConstraints();
       gbc.insets = new Insets(5, 5, 5, 5);
       gbc.anchor = GridBagConstraints.WEST;
       gbc.fill = GridBagConstraints.HORIZONTAL;

       ticketIdLabel = new JLabel();
       statusDropdown = new JComboBox<>(new String[]{"Open", "Assigned", "Pending", "In Progress", "Resolved", "Closed"});
       notesArea = new JTextArea(5, 30);
       notesArea.setLineWrap(true);
       notesArea.setWrapStyleWord(true);

       addFormField(panel, "Ticket ID:", ticketIdLabel, gbc, 0);
       addFormField(panel, "Status:", statusDropdown, gbc, 1);
       addFormField(panel, "Notes:", new JScrollPane(notesArea), gbc, 2);

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

       updateStatusButton = new JButton("Update Status");
       updateStatusButton.setFont(new Font("Arial", Font.BOLD, 14));
       updateStatusButton.setBackground(new Color(46, 204, 113));
       updateStatusButton.setForeground(Color.WHITE);
       updateStatusButton.setFocusPainted(false);
       updateStatusButton.setBorderPainted(false);
       updateStatusButton.setOpaque(true);
       
       updateStatusButton.addActionListener(e -> handleStatusUpdate());
       
       updateStatusButton.addMouseListener(new MouseAdapter() {
           public void mouseEntered(MouseEvent e) {
               updateStatusButton.setBackground(new Color(39, 174, 96));
           }
           public void mouseExited(MouseEvent e) {
               updateStatusButton.setBackground(new Color(46, 204, 113));
           }
       });
       
       panel.add(updateStatusButton);
       return panel;
   }

   private void handleStatusUpdate() {
       try {
           int ticketId = Integer.parseInt(ticketIdLabel.getText());
           String newStatus = (String) statusDropdown.getSelectedItem();
           
           database.updateTicketStatus(ticketId, newStatus);
           
           JOptionPane.showMessageDialog(this,
               "Status updated successfully",
               "Success",
               JOptionPane.INFORMATION_MESSAGE);
               
           mainFrame.showPanel("AdminTicketManagementFrame");
       } catch (Exception e) {
           JOptionPane.showMessageDialog(this,
               "Error updating status: " + e.getMessage(),
               "Error",
               JOptionPane.ERROR_MESSAGE);
       }
   }

   public void setTicketId(String ticketId) {
       ticketIdLabel.setText(ticketId);
   }
}