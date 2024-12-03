package com.ithelpdesk;

import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.dao.MySQLDatabase;
import com.ithelpdesk.service.*;
import com.ithelpdesk.ui.*;
import com.ithelpdesk.util.ConfigLoader;
import com.ithelpdesk.util.SystemLogger;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    private static DatabaseInterface database;
    private static UserService userService;
    private static TicketService ticketService;

    public static void main(String[] args) {
        try {
            initializeApplication();
        } catch (Exception e) {
            SystemLogger.logError("Critical error during application startup: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Error starting application: " + e.getMessage(), 
                "Startup Error", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private static void initializeApplication() throws Exception {
        Properties config = ConfigLoader.loadConfig("src/main/resources/config.properties");
        database = initializeDatabase(config);
        initializeServices();
        startUI();
    }

    private static DatabaseInterface initializeDatabase(Properties config) throws SQLException {
        String dbUrl = config.getProperty("mysql.local.url");
        String dbUsername = config.getProperty("mysql.local.username");
        String dbPassword = config.getProperty("mysql.local.password");

        DatabaseInterface db = new MySQLDatabase(dbUrl, dbUsername, dbPassword);
        db.connect();

        // Verify connection
        if (!testDatabaseConnection(db)) {
            throw new SQLException("Failed to establish database connection");
        }

        return db;
    }

    private static boolean testDatabaseConnection(DatabaseInterface db) {
        try {
            // Test query
            db.getAllUsers();
            return true;
        } catch (Exception e) {
            SystemLogger.logError("Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    private static void initializeServices() {
        userService = new UserService(database);
        ticketService = new TicketService(database);
    }

    private static void startUI() {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(database);
            
            // Initialize frames with database connection
            LoginFrame loginFrame = new LoginFrame(mainFrame, userService);
            AdminDashboardFrame adminDashboard = new AdminDashboardFrame(mainFrame);
            UserDashboardFrame userDashboard = new UserDashboardFrame(mainFrame, database);
            TechDashboardFrame techDashboard = new TechDashboardFrame(mainFrame, database);
            ManageUsersFrame manageUsers = new ManageUsersFrame(mainFrame, database);
            AdminTicketManagementFrame ticketManagement = new AdminTicketManagementFrame(mainFrame, database);
            MetricsFrame metrics = new MetricsFrame(mainFrame, database);
            TicketCreationFrame ticketCreation = new TicketCreationFrame(mainFrame, database);
            TicketDetailsFrame ticketDetails = new TicketDetailsFrame(mainFrame, database);
            UserTicketsFrame userTickets = new UserTicketsFrame(mainFrame, database);
            KnowledgeBaseFrame knowledgeBase = new KnowledgeBaseFrame(mainFrame, database, true);
            TechTicketsFrame techTickets = new TechTicketsFrame(mainFrame, database);

            // Add panels to main frame
            mainFrame.addPanel("LoginFrame", loginFrame);
            mainFrame.addPanel("AdminDashboardFrame", adminDashboard);
            mainFrame.addPanel("UserDashboardFrame", userDashboard);
            mainFrame.addPanel("TechDashboardFrame", techDashboard);
            mainFrame.addPanel("ManageUsersFrame", manageUsers);
            mainFrame.addPanel("AdminTicketManagementFrame", ticketManagement);
            mainFrame.addPanel("MetricsFrame", metrics);
            mainFrame.addPanel("TicketCreationFrame", ticketCreation);
            mainFrame.addPanel("TicketDetailsFrame", ticketDetails);
            mainFrame.addPanel("UserTicketsFrame", userTickets);
            mainFrame.addPanel("KnowledgeBaseFrame", knowledgeBase);
            mainFrame.addPanel("TechTicketsFrame", techTickets);

            // Add back buttons to all frames
            addBackButtons(mainFrame);

            // Show login frame
            mainFrame.showPanel("LoginFrame");
            mainFrame.setVisible(true);
        });
    }

    private static void addBackButtons(MainFrame mainFrame) {
        // Add back buttons to all panels except LoginFrame
        Component[] components = mainFrame.getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JPanel && !(component instanceof LoginFrame)) {
                JPanel panel = (JPanel) component;
                addBackButton(panel, mainFrame);
            }
        }
    }

    private static void addBackButton(JPanel panel, MainFrame mainFrame) {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> handleBack(mainFrame));
        panel.add(backButton, BorderLayout.SOUTH);
    }

    private static void handleBack(MainFrame mainFrame) {
        String userRole = mainFrame.getUserRole();
        if (userRole != null) {
            switch (userRole) {
                case "Admin":
                    mainFrame.showPanel("AdminDashboardFrame");
                    break;
                case "Tech":
                    mainFrame.showPanel("TechDashboardFrame");
                    break;
                case "User":
                    mainFrame.showPanel("UserDashboardFrame");
                    break;
            }
        }
    }

    public static DatabaseInterface getDatabase() {
        return database;
    }

    public static UserService getUserService() {
        return userService;
    }
    
    private static boolean verifyDatabaseConnection(DatabaseInterface db) {
        try {
            db.connect();
            return db.testConnection(); // Add this method to DatabaseInterface
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return false;
        }
    }
}