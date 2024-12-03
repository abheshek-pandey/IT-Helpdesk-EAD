package com.ithelpdesk.service;

import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.Article;

import java.util.List;

public class ArticleService {
    private final DatabaseInterface database;

    // Constructor
    public ArticleService(DatabaseInterface database) {
        this.database = database;
    }

    // Create a new article
    public void createArticle(Article article) {
        database.addArticle(article);
        System.out.println("Article created successfully: " + article.getTitle());
    }

    // Update an existing article
    public void updateArticle(Article article) {
        database.updateArticle(article);
        System.out.println("Article updated successfully: " + article.getTitle());
    }

    // Delete an article by title
    public void deleteArticle(String title) {
        database.deleteArticle(title);
        System.out.println("Article deleted successfully: " + title);
    }

    // Fetch all articles
    public List<Article> getAllArticles() {
        return database.getAllArticles();
    }

    // Fetch an article by title
    public Article getArticleByTitle(String title) {
        return database.getArticleByTitle(title);
    }
}
