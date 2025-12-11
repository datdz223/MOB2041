package com.example.mob2041.model;

import java.io.Serializable;

public class RevenueByDate implements Serializable {
    private String date;
    private double revenue;
    private int orderCount;

    public RevenueByDate() {
    }

    public RevenueByDate(String date, double revenue, int orderCount) {
        this.date = date;
        this.revenue = revenue;
        this.orderCount = orderCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}

