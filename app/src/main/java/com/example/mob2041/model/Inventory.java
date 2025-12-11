package com.example.mob2041.model;

public class Inventory {
    private int inventoryId;
    private int productId;
    private int stockQuantity;
    private int minimumStock;
    private String lastUpdated;
    private Product product; // Để hiển thị

    public Inventory() {
    }

    public Inventory(int productId, int stockQuantity) {
        this.productId = productId;
        this.stockQuantity = stockQuantity;
        this.minimumStock = 10; // Mặc định 10
    }

    // Getters and Setters
    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
    }
}


