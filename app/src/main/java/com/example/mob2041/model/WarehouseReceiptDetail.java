package com.example.mob2041.model;

import java.io.Serializable;

public class WarehouseReceiptDetail implements Serializable {
    private int detailId;
    private int receiptId;
    private int productId;
    private int quantity;
    private double unitPrice;
    private Product product; // Để hiển thị

    public WarehouseReceiptDetail() {
    }

    public WarehouseReceiptDetail(int receiptId, int productId, int quantity, double unitPrice) {
        this.receiptId = receiptId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters and Setters
    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getSubtotal() {
        return quantity * unitPrice;
    }
}

