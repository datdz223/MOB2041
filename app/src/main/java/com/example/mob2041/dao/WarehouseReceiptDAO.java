package com.example.mob2041.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mob2041.database.DatabaseHelper;
import com.example.mob2041.model.Product;
import com.example.mob2041.model.WarehouseReceipt;
import com.example.mob2041.model.WarehouseReceiptDetail;

import java.util.ArrayList;
import java.util.List;

public class WarehouseReceiptDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public WarehouseReceiptDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Thêm phiếu nhập kho
    public long insertReceipt(WarehouseReceipt receipt) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_SUPPLIER, receipt.getSupplier());
        values.put(DatabaseHelper.COL_STAFF_ID, receipt.getStaffId());
        values.put(DatabaseHelper.COL_RECEIPT_NOTE, receipt.getNote());

        long result = db.insert(DatabaseHelper.TABLE_WAREHOUSE_RECEIPTS, null, values);
        close();
        return result;
    }

    // Thêm chi tiết phiếu nhập kho
    public long insertReceiptDetail(WarehouseReceiptDetail detail) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_RECEIPT_ID, detail.getReceiptId());
        values.put(DatabaseHelper.COL_PRODUCT_ID, detail.getProductId());
        values.put(DatabaseHelper.COL_QUANTITY, detail.getQuantity());
        values.put(DatabaseHelper.COL_UNIT_PRICE, detail.getUnitPrice());

        long result = db.insert(DatabaseHelper.TABLE_WAREHOUSE_RECEIPT_DETAILS, null, values);
        close();
        return result;
    }

    // Lấy tất cả phiếu nhập kho
    public List<WarehouseReceipt> getAllReceipts() {
        open();
        List<WarehouseReceipt> receipts = new ArrayList<>();
        String query = "SELECT r.*, u." + DatabaseHelper.COL_FULL_NAME +
                " FROM " + DatabaseHelper.TABLE_WAREHOUSE_RECEIPTS + " r " +
                "LEFT JOIN " + DatabaseHelper.TABLE_USERS + " u " +
                "ON r." + DatabaseHelper.COL_STAFF_ID + " = u." + DatabaseHelper.COL_USER_ID +
                " ORDER BY r." + DatabaseHelper.COL_RECEIPT_DATE + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                WarehouseReceipt receipt = new WarehouseReceipt();
                receipt.setReceiptId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECEIPT_ID)));
                receipt.setReceiptDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECEIPT_DATE)));
                receipt.setSupplier(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SUPPLIER)));
                receipt.setStaffId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STAFF_ID)));
                receipt.setNote(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECEIPT_NOTE)));
                receipt.setStaffName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME)));
                receipts.add(receipt);
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return receipts;
    }

    // Lấy chi tiết phiếu nhập kho
    public List<WarehouseReceiptDetail> getReceiptDetails(int receiptId) {
        open();
        List<WarehouseReceiptDetail> details = new ArrayList<>();
        String query = "SELECT d.*, p." + DatabaseHelper.COL_PRODUCT_NAME + ", p." + DatabaseHelper.COL_UNIT +
                " FROM " + DatabaseHelper.TABLE_WAREHOUSE_RECEIPT_DETAILS + " d " +
                "LEFT JOIN " + DatabaseHelper.TABLE_PRODUCTS + " p " +
                "ON d." + DatabaseHelper.COL_PRODUCT_ID + " = p." + DatabaseHelper.COL_PRODUCT_ID +
                " WHERE d." + DatabaseHelper.COL_RECEIPT_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(receiptId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                WarehouseReceiptDetail detail = new WarehouseReceiptDetail();
                detail.setDetailId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECEIPT_DETAIL_ID)));
                detail.setReceiptId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECEIPT_ID)));
                detail.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_ID)));
                detail.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUANTITY)));
                detail.setUnitPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UNIT_PRICE)));

                Product product = new Product();
                product.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_ID)));
                product.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_NAME)));
                product.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UNIT)));
                detail.setProduct(product);

                details.add(detail);
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return details;
    }
}


