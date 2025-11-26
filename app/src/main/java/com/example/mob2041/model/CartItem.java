package com.example.mob2041.model;

public class CartItem {
    private Product product;
    private int quantity;
    private double subtotal;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        calculateSubtotal();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        calculateSubtotal();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    private void calculateSubtotal() {
        if (product != null) {
            this.subtotal = product.getPrice() * quantity;
        }
    }
}

