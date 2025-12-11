package com.example.mob2041.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mob2041.database.DatabaseHelper;
import com.example.mob2041.model.Inventory;
import com.example.mob2041.model.Product;

import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public InventoryDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Thêm hoặc cập nhật tồn kho
    public long insertOrUpdateInventory(Inventory inventory) {
        open();
        // Kiểm tra xem đã có tồn kho cho sản phẩm này chưa
        String[] columns = {DatabaseHelper.COL_INVENTORY_ID, DatabaseHelper.COL_STOCK_QUANTITY};
        String selection = DatabaseHelper.COL_PRODUCT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(inventory.getProductId())};
        Cursor cursor = db.query(DatabaseHelper.TABLE_INVENTORY, columns, selection, selectionArgs, null, null, null);

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_PRODUCT_ID, inventory.getProductId());
        values.put(DatabaseHelper.COL_STOCK_QUANTITY, inventory.getStockQuantity());
        // Nếu minimumStock = 0, set mặc định là 10
        int minimumStock = inventory.getMinimumStock() > 0 ? inventory.getMinimumStock() : 10;
        values.put(DatabaseHelper.COL_MINIMUM_STOCK, minimumStock);

        long result;
        if (cursor != null && cursor.moveToFirst()) {
            // Cập nhật
            result = db.update(DatabaseHelper.TABLE_INVENTORY, values, selection, selectionArgs);
            cursor.close();
        } else {
            // Thêm mới
            result = db.insert(DatabaseHelper.TABLE_INVENTORY, null, values);
        }
        close();
        return result;
    }

    // Cập nhật số lượng tồn kho
    public int updateStockQuantity(int productId, int quantity) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_STOCK_QUANTITY, quantity);

        int result = db.update(DatabaseHelper.TABLE_INVENTORY, values,
                DatabaseHelper.COL_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(productId)});
        close();
        return result;
    }

    // Cập nhật số lượng tối thiểu
    public int updateMinimumStock(int productId, int minimumStock) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_MINIMUM_STOCK, minimumStock);

        int result = db.update(DatabaseHelper.TABLE_INVENTORY, values,
                DatabaseHelper.COL_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(productId)});
        close();
        return result;
    }

    // Cập nhật cả số lượng và số lượng tối thiểu
    public int updateInventory(int productId, int quantity, int minimumStock) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_STOCK_QUANTITY, quantity);
        values.put(DatabaseHelper.COL_MINIMUM_STOCK, minimumStock);

        int result = db.update(DatabaseHelper.TABLE_INVENTORY, values,
                DatabaseHelper.COL_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(productId)});
        close();
        return result;
    }

    // Tăng số lượng tồn kho (khi nhập kho)
    public int increaseStock(int productId, int quantity) {
        open();
        Inventory inventory = getInventoryByProductId(productId);
        if (inventory != null) {
            int newQuantity = inventory.getStockQuantity() + quantity;
            return updateStockQuantity(productId, newQuantity);
        } else {
            // Tạo mới nếu chưa có
            Inventory newInventory = new Inventory(productId, quantity);
            return (int) insertOrUpdateInventory(newInventory);
        }
    }

    // Giảm số lượng tồn kho (khi bán hàng)
    public int decreaseStock(int productId, int quantity) {
        open();
        try {
            Inventory inventory = getInventoryByProductId(productId);
            if (inventory != null && inventory.getStockQuantity() >= quantity) {
                int newQuantity = inventory.getStockQuantity() - quantity;
                int result = updateStockQuantity(productId, newQuantity);
                close();
                return result;
            } else {
                close();
                return -1; // Không đủ hàng
            }
        } catch (Exception e) {
            close();
            return -1; // Lỗi
        }
    }

    // Lấy tồn kho theo productId
    public Inventory getInventoryByProductId(int productId) {
        open();
        Inventory inventory = null;
        String[] columns = {
                DatabaseHelper.COL_INVENTORY_ID,
                DatabaseHelper.COL_PRODUCT_ID,
                DatabaseHelper.COL_STOCK_QUANTITY,
                DatabaseHelper.COL_MINIMUM_STOCK,
                DatabaseHelper.COL_LAST_UPDATED
        };
        String selection = DatabaseHelper.COL_PRODUCT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(productId)};

        Cursor cursor = db.query(DatabaseHelper.TABLE_INVENTORY, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            inventory = new Inventory();
            inventory.setInventoryId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_INVENTORY_ID)));
            inventory.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_ID)));
            inventory.setStockQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STOCK_QUANTITY)));
            int minimumStockIndex = cursor.getColumnIndex(DatabaseHelper.COL_MINIMUM_STOCK);
            if (minimumStockIndex >= 0) {
                inventory.setMinimumStock(cursor.getInt(minimumStockIndex));
            } else {
                inventory.setMinimumStock(10); // Mặc định 10
            }
            inventory.setLastUpdated(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LAST_UPDATED)));
            cursor.close();
        }
        close();
        return inventory;
    }

    // Lấy tất cả tồn kho với thông tin sản phẩm
    public List<Inventory> getAllInventory() {
        open();
        List<Inventory> inventoryList = new ArrayList<>();
        String query = "SELECT i.*, p." + DatabaseHelper.COL_PRODUCT_NAME + ", p." + DatabaseHelper.COL_PRICE +
                " FROM " + DatabaseHelper.TABLE_INVENTORY + " i " +
                "LEFT JOIN " + DatabaseHelper.TABLE_PRODUCTS + " p " +
                "ON i." + DatabaseHelper.COL_PRODUCT_ID + " = p." + DatabaseHelper.COL_PRODUCT_ID +
                " ORDER BY p." + DatabaseHelper.COL_PRODUCT_NAME;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Inventory inventory = new Inventory();
                inventory.setInventoryId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_INVENTORY_ID)));
                inventory.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_ID)));
                inventory.setStockQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STOCK_QUANTITY)));
                int minimumStockIndex = cursor.getColumnIndex(DatabaseHelper.COL_MINIMUM_STOCK);
                if (minimumStockIndex >= 0) {
                    inventory.setMinimumStock(cursor.getInt(minimumStockIndex));
                } else {
                    inventory.setMinimumStock(10); // Mặc định 10
                }
                inventory.setLastUpdated(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LAST_UPDATED)));

                Product product = new Product();
                product.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_ID)));
                product.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_NAME)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRICE)));
                inventory.setProduct(product);

                inventoryList.add(inventory);
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return inventoryList;
    }
}


