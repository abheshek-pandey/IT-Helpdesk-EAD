package com.ithelpdesk.service;

import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.KnowledgeBaseCategory;

import java.util.List;

public class KnowledgeBaseCategoryService {
    private final DatabaseInterface database;

    // Constructor
    public KnowledgeBaseCategoryService(DatabaseInterface database) {
        this.database = database;
    }

    // Get all main categories
    public List<String> getAllCategories() {
        return database.getAllKnowledgeBaseCategories();
    }

    // Get subcategories for a specific category
    public List<String> getSubcategories(String category) {
        return database.getKnowledgeBaseSubcategories(category);
    }

    // Add a new category
    public void addCategory(KnowledgeBaseCategory category) {
        database.addKnowledgeBaseCategory(category);
        System.out.println("Knowledge base category added successfully: " + category.getName());
    }

    // Delete a category
    public void deleteCategory(int categoryId) {
        database.deleteKnowledgeBaseCategory(categoryId);
        System.out.println("Knowledge base category deleted successfully: ID " + categoryId);
    }
}
