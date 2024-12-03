package com.ithelpdesk.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.Article;

public class AddEditArticleFrame extends JPanel {
    private final MainFrame mainFrame;
    private final DatabaseInterface database;
    private JTextField titleField;
    private JTextArea contentArea;
    private JButton saveButton;
    private Article articleToEdit;

    public AddEditArticleFrame(MainFrame mainFrame, DatabaseInterface database, Article article) {
        this.mainFrame = mainFrame;
        this.database = database;
        this.articleToEdit = article;  // If null, a new article will be created, else editing an existing one
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 242, 245));
        JLabel headerLabel = new JLabel(articleToEdit == null ? "Add New Article" : "Edit Article");
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

        titleField = new JTextField(30);
        contentArea = new JTextArea(10, 30);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);

        if (articleToEdit != null) {
            titleField.setText(articleToEdit.getTitle());
            contentArea.setText(articleToEdit.getContent());
        }

        addFormField(panel, "Title:", titleField, gbc, 0);
        addFormField(panel, "Content:", new JScrollPane(contentArea), gbc, 1);

        return panel;
    }

    private void addFormField(JPanel panel, String label, Component component, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(240, 242, 245));

        saveButton = new JButton(articleToEdit == null ? "Save Article" : "Update Article");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setOpaque(true);
        saveButton.addActionListener(e -> handleSave());

        panel.add(saveButton);
        return panel;
    }

    private void handleSave() {
        if (validateForm()) {
            try {
                if (articleToEdit == null) {
                    // Add new article
                    Article newArticle = new Article(
                        0, 
                        titleField.getText(), 
                        contentArea.getText(),
                        mainFrame.getCurrentUser(), 
                        new java.sql.Timestamp(System.currentTimeMillis()),
                        new java.sql.Timestamp(System.currentTimeMillis())
                    );

                    database.addArticle(newArticle);
                } else {
                    // Update existing article
                    articleToEdit.setTitle(titleField.getText());
                    articleToEdit.setContent(contentArea.getText());
                    articleToEdit.setLastUpdated(new java.sql.Timestamp(System.currentTimeMillis()));

                    database.updateArticle(articleToEdit);
                }

                JOptionPane.showMessageDialog(this, articleToEdit == null ? "Article added!" : "Article updated!");
                mainFrame.showPanel("KnowledgeBaseFrame"); // Go back to the knowledge base
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error saving article: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setEditingArticle(Article article) {
        this.articleToEdit = article;
        if (article != null) {
            titleField.setText(article.getTitle());
            contentArea.setText(article.getContent());
        } else {
            titleField.setText("");
            contentArea.setText("");
        }
    }

    private boolean validateForm() {
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title is required");
            return false;
        }
        if (contentArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Content is required");
            return false;
        }
        return true;
    }
}
