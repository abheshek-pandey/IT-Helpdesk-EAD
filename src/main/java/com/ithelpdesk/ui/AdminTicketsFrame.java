package com.ithelpdesk.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminTicketsFrame extends JPanel {
   private final MainFrame mainFrame;
   private JTable ticketsTable;
   private JComboBox<String> statusFilter;
   private JComboBox<String> priorityFilter;
   private JComboBox<String> severityFilter;
   private JButton refreshButton;

   public AdminTicketsFrame(MainFrame mainFrame) {
       this.mainFrame = mainFrame;
       initComponents();
   }

   private void initComponents() {
       setLayout(new BorderLayout(10, 10));
       setBackground(new Color(240, 242, 245));

       add(createHeaderPanel(), BorderLayout.NORTH);
       add(createTablePanel(), BorderLayout.CENTER);
   }

   private JPanel createHeaderPanel() {
       JPanel panel = new JPanel(new BorderLayout());
       panel.setBackground(new Color(240, 242, 245));

       JLabel headerLabel = new JLabel("Manage Tickets");
       headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
       headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

       JPanel filterPanel = createFilterPanel();

       panel.add(headerLabel, BorderLayout.NORTH);
       panel.add(filterPanel, BorderLayout.CENTER);

       return panel;
   }

   private JPanel createFilterPanel() {
       JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
       panel.setBackground(new Color(240, 242, 245));

       statusFilter = new JComboBox<>(new String[]{"All", "Open", "Assigned", "Pending", "In Progress", "Resolved", "Closed"});
       priorityFilter = new JComboBox<>(new String[]{"All", "Very High", "High", "Moderate", "Low"});
       severityFilter = new JComboBox<>(new String[]{"All", "1", "2", "3", "4"});
       refreshButton = createStyledButton("Refresh");

       panel.add(new JLabel("Status:"));
       panel.add(statusFilter);
       panel.add(new JLabel("Priority:"));
       panel.add(priorityFilter);
       panel.add(new JLabel("Severity:"));
       panel.add(severityFilter);
       panel.add(refreshButton);

       return panel;
   }

   private JScrollPane createTablePanel() {
       ticketsTable = new JTable();
       ticketsTable.setFillsViewportHeight(true);
       return new JScrollPane(ticketsTable);
   }

   private JButton createStyledButton(String text) {
       JButton button = new JButton(text);
       button.setFont(new Font("Arial", Font.BOLD, 14));
       button.setBackground(new Color(52, 152, 219));
       button.setForeground(Color.WHITE);
       button.setFocusPainted(false);
       button.setBorderPainted(false);
       button.setOpaque(true);

       button.addActionListener(e -> handleRefresh());
       
       return button;
   }

   private void handleRefresh() {
       // Implement refresh logic
   }
}