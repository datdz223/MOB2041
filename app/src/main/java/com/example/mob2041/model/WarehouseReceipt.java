package com.example.mob2041.model;

public class WarehouseReceipt {
    private int receiptId;
    private String receiptDate;
    private String supplier;
    private int staffId;
    private String note;
    private String staffName; // Để hiển thị

    public WarehouseReceipt() {
    }

    public WarehouseReceipt(String supplier, int staffId, String note) {
        this.supplier = supplier;
        this.staffId = staffId;
        this.note = note;
    }

    // Getters and Setters
    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}


