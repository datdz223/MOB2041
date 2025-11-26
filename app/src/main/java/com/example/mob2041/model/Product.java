package com.example.mob2041.model;

public class Product {
    private int productId;
    private String productName;
    private String description;
    private double price;
    private int categoryId;
    private String unit;
    private String image;
    private String categoryName; // Để hiển thị tên danh mục

    public Product() {
    }

    public Product(String productName, String description, double price, int categoryId, String unit) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.unit = unit;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

