package com.ithelpdesk.model;

public class Category {
    private int id;                 // Category ID
    private String category;        // Main category name
    private String subcategory;     // Subcategory name (nullable)

    // Constructor for main category
    public Category(String category) {
        this.category = category;
    }

    // Constructor for category with subcategory
    public Category(int id, String category, String subcategory) {
        this.id = id;
        this.category = category;
        this.subcategory = subcategory;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", subcategory='" + subcategory + '\'' +
                '}';
    }
}
