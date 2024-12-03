package com.ithelpdesk.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.ithelpdesk.dao.DatabaseInterface;

public class UserDashboardFrame extends JPanel {
   private final MainFrame mainFrame;
   private final DatabaseInterface database;
   private JButton viewTicketsButton;
   private JButton knowledgeBaseButton;
   private JButton createTicketButton;  // New button for creating a ticket
   private JButton visitKseaButton;     // Button to visit KSEA website
   private JLabel welcomeLabel;

   public UserDashboardFrame(MainFrame mainFrame, DatabaseInterface database) {
       this.mainFrame = mainFrame;
       this.database = database;
       setLayout(new GridBagLayout());
       setBackground(new Color(240, 242, 245));
       initComponents();
   }

   private void initComponents() {
       // Display welcome message with the username (not email)
       welcomeLabel = new JLabel("Welcome!");
       welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
       welcomeLabel.setForeground(new Color(45, 52, 54));

       // Initialize buttons
       viewTicketsButton = createStyledButton("View My Tickets");
       knowledgeBaseButton = createStyledButton("Knowledge Base");
       createTicketButton = createStyledButton("Create New Ticket");
       visitKseaButton = createStyledButton("Visit KSEA.ORG");

       // Set up actions for buttons
       setupActions();
       layoutComponents();
   }

   private JButton createStyledButton(String text) {
       JButton button = new JButton(text);
       button.setFont(new Font("Arial", Font.BOLD, 16));
       button.setBackground(new Color(52, 152, 219));
       button.setForeground(Color.WHITE);
       button.setFocusPainted(false);
       button.setBorderPainted(false);
       button.setOpaque(true);
       button.setPreferredSize(new Dimension(250, 45));

       button.addMouseListener(new MouseAdapter() {
           public void mouseEntered(MouseEvent e) {
               button.setBackground(new Color(41, 128, 185));
           }
           public void mouseExited(MouseEvent e) {
               button.setBackground(new Color(52, 152, 219));
           }
       });

       return button;
   }

   private void setupActions() {
       viewTicketsButton.addActionListener(e -> {
           mainFrame.setTitle("My Tickets");
           mainFrame.showPanel("UserTicketsFrame");  // Show UserTicketsFrame
       });

       knowledgeBaseButton.addActionListener(e -> {
           mainFrame.setTitle("Knowledge Base");
           mainFrame.showPanel("KnowledgeBaseFrame");  // Show KnowledgeBaseFrame
       });

       createTicketButton.addActionListener(e -> {
           mainFrame.setTitle("Create New Ticket");
           mainFrame.showPanel("TicketCreationFrame");  // Navigate to ticket creation frame
       });

       visitKseaButton.addActionListener(e -> {
           try {
               // Open the KSEA website in the default web browser
               Desktop.getDesktop().browse(new java.net.URI("http://www.ksea.org"));
           } catch (Exception ex) {
               JOptionPane.showMessageDialog(this, "Error opening KSEA website: " + ex.getMessage(),
                       "Error", JOptionPane.ERROR_MESSAGE);
           }
       });
   }

   private void layoutComponents() {
       GridBagConstraints gbc = new GridBagConstraints();
       gbc.insets = new Insets(20, 20, 20, 20);
       gbc.gridx = 0;
       gbc.gridy = 0;
       gbc.fill = GridBagConstraints.HORIZONTAL;
       
       // Add welcome message
       add(welcomeLabel, gbc);
       
       // Add buttons for actions
       gbc.gridy++;
       add(viewTicketsButton, gbc);
       
       gbc.gridy++;
       add(knowledgeBaseButton, gbc);

       gbc.gridy++;
       add(createTicketButton, gbc);  // Add the "Create New Ticket" button

       gbc.gridy++;
       add(visitKseaButton, gbc);  // Add "Visit KSEA.ORG" button
   }
}
