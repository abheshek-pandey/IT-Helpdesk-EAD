package com.ithelpdesk.model;

public class KnowledgeBaseCategory {
    private int id;                 // Category ID (primary key)
    private String name;            // Main category name
    private String subcategory;     // Subcategory name (optional)

    // Constructor for creating a main category
    public KnowledgeBaseCategory(String name) {
        this.name = name;
    }

    // Constructor for creating a subcategory
    public KnowledgeBaseCategory(int id, String name, String subcategory) {
        this.id = id;
        this.name = name;
        this.subcategory = subcategory;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    @Override
    public String toString() {
        return "KnowledgeBaseCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subcategory='" + subcategory + '\'' +
                '}';
    }
}
