package com.ithelpdesk.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.Article;

import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class KMSArticleFrame extends JPanel {
    private final MainFrame mainFrame;
    private JTable articlesTable;
    private JButton addArticleButton;
    private JTextField searchField;
    private JButton searchButton;
    private DefaultTableModel tableModel;
    private DatabaseInterface database;

    public KMSArticleFrame(MainFrame mainFrame, DatabaseInterface database, boolean isContributor) {
        this.mainFrame = mainFrame;
        this.database = database;
        initComponents(isContributor);
        setupTable();
        loadArticles();
    }

    private void initComponents(boolean isContributor) {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        if (isContributor) {
            add(createButtonPanel(), BorderLayout.SOUTH);
        }
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 242, 245));

        JLabel headerLabel = new JLabel("Knowledge Management System");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = createSearchPanel();

        panel.add(headerLabel, BorderLayout.NORTH);
        panel.add(searchPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(240, 242, 245));

        searchField = new JTextField(20);
        searchButton = createStyledButton("Search");
        searchButton.addActionListener(e -> handleSearch());

        panel.add(new JLabel("Search:"));
        panel.add(searchField);
        panel.add(searchButton);

        return panel;
    }

    private JScrollPane createTablePanel() {
        String[] columns = {"Title", "Created By", "Created At", "Last Updated"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        articlesTable = new JTable(tableModel);
        articlesTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return new JScrollPane(articlesTable);
    }

    private void loadArticles() {
        tableModel.setRowCount(0);  // Clear the existing rows

        // Retrieve articles from the database
        List<Article> articles = database.getAllArticles();

        // Format the date for displaying
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Article article : articles) {
            tableModel.addRow(new Object[]{
                article.getTitle(),                       // Article Title
                article.getCreatedBy(),                   // Created By
                dateFormat.format(article.getCreatedAt()), // Created At (formatted)
                dateFormat.format(article.getLastUpdated()) // Last Updated (formatted)
            });
        }
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(240, 242, 245));

        addArticleButton = createStyledButton("Add Article");
        addArticleButton.addActionListener(e -> handleAddArticle());

        panel.add(addArticleButton);
        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        return button;
    }

    private void setupTable() {
        articlesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleTableSelection();
            }
        });
    }

    private void handleSearch() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            List<Article> articles = database.getArticlesBySearchTerm(searchTerm);  // Searching articles
            tableModel.setRowCount(0); // Clear existing rows

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (Article article : articles) {
                tableModel.addRow(new Object[]{
                    article.getTitle(),
                    article.getCreatedBy(),
                    dateFormat.format(article.getCreatedAt()),
                    dateFormat.format(article.getLastUpdated())
                });
            }
        }
    }

    private void handleAddArticle() {
        int selectedRow = articlesTable.getSelectedRow();
        if (selectedRow >= 0) {
            String title = (String) articlesTable.getValueAt(selectedRow, 0);
            mainFrame.showPanel("EditArticleFrame");  // Navigate to edit article
        } else {
            mainFrame.showPanel("AddArticleFrame");  // Navigate to add new article frame
        }
    }

    private void handleTableSelection() {
        int selectedRow = articlesTable.getSelectedRow();
        if (selectedRow >= 0) {
            String title = (String) articlesTable.getValueAt(selectedRow, 0);
            showArticleDetails(title);
        }
    }

    private void showArticleDetails(String title) {
        // Fetch article by title
        Article article = database.getArticleByTitle(title);  // Fetch the article details

        if (article != null) {
            JOptionPane.showMessageDialog(this,
                "Title: " + article.getTitle() + "\n" +
                "Content: " + article.getContent() + "\n" +
                "Created By: " + article.getCreatedBy() + "\n" +
                "Created At: " + article.getCreatedAt() + "\n" +
                "Last Updated: " + article.getLastUpdated(),
                "Article Details",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Article not found.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
