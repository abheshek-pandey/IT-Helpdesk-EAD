package com.ithelpdesk.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.Article;
import com.ithelpdesk.service.UserService;

public class MainFrame extends JFrame {
    private String currentUser;
    private String userRole;
    private JPanel contentPanel;
    private JLabel headerLabel;
    private JButton logoutButton;
    private CardLayout cardLayout;
    private DatabaseInterface database;
    private UserService userService;

    public MainFrame(DatabaseInterface database) {
        this.database = database;
        setTitle("IT Helpdesk System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeaderPanel();
        contentPanel = createContentPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        initializePanels();

        setVisible(true);

        // Show Login Panel initially
        showPanel("LoginFrame");
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(45, 52, 54));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        headerLabel = new JLabel("IT Helpdesk System");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);

        logoutButton = createLogoutButton();

        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        return headerPanel;
    }

    private JButton createLogoutButton() {
        JButton button = new JButton("Logout");
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(100, 35));
        button.setBackground(new Color(231, 76, 60));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(192, 57, 43));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(231, 76, 60));
            }
        });

        button.addActionListener(e -> handleLogout());
        return button;
    }

    private JPanel createContentPanel() {
        cardLayout = new CardLayout();
        JPanel panel = new JPanel(cardLayout);
        panel.setBackground(new Color(240, 242, 245));
        return panel;
    }

    public void addPanel(String name, JPanel panel) {
        contentPanel.add(panel, name);
        panel.setName(name);
    }

    public JPanel getPanel(String panelName) {
        for (Component comp : contentPanel.getComponents()) {
            if (comp instanceof JPanel && panelName.equals(comp.getName())) {
                return (JPanel) comp;
            }
        }
        return null; // Return null if panel not found
    }

    public void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            setCurrentUser(null, null);
            showPanel("LoginFrame");
        }
    }

    public void setCurrentUser(String username, String role) {
        this.currentUser = username;
        this.userRole = role;
        updateHeaderLabel();
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public String getUserRole() {
        return userRole;
    }

    private void updateHeaderLabel() {
        if (currentUser != null) {
            headerLabel.setText("IT Helpdesk System - " + currentUser + " (" + userRole + ")");
        } else {
            headerLabel.setText("IT Helpdesk System");
        }
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    private void initializePanels() {
        // Add panels here
        addPanel("LoginFrame", new LoginFrame(this, userService));
        addPanel("AdminDashboardFrame", new AdminDashboardFrame(this));
        addPanel("TechDashboardFrame", new TechDashboardFrame(this, database));
        addPanel("UserTicketsFrame", new UserTicketsFrame(this, database));
        addPanel("KnowledgeBaseFrame", new KnowledgeBaseFrame(this, database, isAdminOrTech()));

        // Register AddEditArticleFrame for adding/editing articles
        addPanel("AddEditArticleFrame", new AddEditArticleFrame(this, database, null));

        // Add additional panels as needed
    }

    private boolean isAdminOrTech() {
        return "Admin".equals(userRole) || "Tech".equals(userRole);
    }

    public void showAddEditArticleFrame(Article article) {
        AddEditArticleFrame addEditArticleFrame = (AddEditArticleFrame) getPanel("AddEditArticleFrame");
        if (addEditArticleFrame != null) {
            addEditArticleFrame.setEditingArticle(article); // Pass null for new articles
            showPanel("AddEditArticleFrame");
        }
    }
}
