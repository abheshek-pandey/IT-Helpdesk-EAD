package com.ithelpdesk.service;

import com.ithelpdesk.dao.DatabaseInterface;
import com.ithelpdesk.model.Category;

import java.util.List;

public class CategoryService {
    private final DatabaseInterface database;

    // Constructor
    public CategoryService(DatabaseInterface database) {
        this.database = database;
    }

    // Get all main categories
    public List<String> getAllCategories() {
        return database.getAllCategories();
    }

    // Get subcategories for a specific category
    public List<String> getSubcategories(String category) {
        return database.getSubcategoriesByCategory(category);
    }

    // Add a new category
    public void addCategory(Category category) {
        database.addCategory(category);
        System.out.println("Category added successfully: " + category.getCategory());
    }

    // Delete a category
    public void deleteCategory(int categoryId) {
        database.deleteCategory(categoryId);
        System.out.println("Category deleted successfully: ID " + categoryId);
    }
}
