package com.example.mob2041.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mob2041.database.DatabaseHelper;
import com.example.mob2041.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public ProductDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Thêm sản phẩm
    public long insertProduct(Product product) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_PRODUCT_NAME, product.getProductName());
        values.put(DatabaseHelper.COL_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(DatabaseHelper.COL_PRICE, product.getPrice());
        values.put(DatabaseHelper.COL_CATEGORY_ID, product.getCategoryId());
        values.put(DatabaseHelper.COL_UNIT, product.getUnit());
        values.put(DatabaseHelper.COL_IMAGE, product.getImage());

        long result = db.insert(DatabaseHelper.TABLE_PRODUCTS, null, values);
        close();
        return result;
    }

    // Cập nhật sản phẩm
    public int updateProduct(Product product) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_PRODUCT_NAME, product.getProductName());
        values.put(DatabaseHelper.COL_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(DatabaseHelper.COL_PRICE, product.getPrice());
        values.put(DatabaseHelper.COL_CATEGORY_ID, product.getCategoryId());
        values.put(DatabaseHelper.COL_UNIT, product.getUnit());
        values.put(DatabaseHelper.COL_IMAGE, product.getImage());

        int result = db.update(DatabaseHelper.TABLE_PRODUCTS, values,
                DatabaseHelper.COL_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(product.getProductId())});
        close();
        return result;
    }

    // Xóa sản phẩm
    public int deleteProduct(int productId) {
        open();
        int result = db.delete(DatabaseHelper.TABLE_PRODUCTS,
                DatabaseHelper.COL_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(productId)});
        close();
        return result;
    }

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() {
        open();
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.*, c." + DatabaseHelper.COL_CATEGORY_NAME +
                " FROM " + DatabaseHelper.TABLE_PRODUCTS + " p " +
                "LEFT JOIN " + DatabaseHelper.TABLE_PRODUCT_CATEGORIES + " c " +
                "ON p." + DatabaseHelper.COL_CATEGORY_ID + " = c." + DatabaseHelper.COL_CATEGORY_ID +
                " ORDER BY p." + DatabaseHelper.COL_PRODUCT_NAME;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_ID)));
                product.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_DESCRIPTION)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRICE)));
                product.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_ID)));
                product.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UNIT)));
                product.setImage(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IMAGE)));
                product.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_NAME)));
                products.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return products;
    }

    // Lấy sản phẩm theo ID
    public Product getProductById(int productId) {
        open();
        Product product = null;
        String[] columns = {
                DatabaseHelper.COL_PRODUCT_ID,
                DatabaseHelper.COL_PRODUCT_NAME,
                DatabaseHelper.COL_PRODUCT_DESCRIPTION,
                DatabaseHelper.COL_PRICE,
                DatabaseHelper.COL_CATEGORY_ID,
                DatabaseHelper.COL_UNIT,
                DatabaseHelper.COL_IMAGE
        };
        String selection = DatabaseHelper.COL_PRODUCT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(productId)};

        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTS, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            product = new Product();
            product.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_ID)));
            product.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_NAME)));
            product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_DESCRIPTION)));
            product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRICE)));
            product.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_ID)));
            product.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UNIT)));
            product.setImage(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IMAGE)));
            cursor.close();
        }
        close();
        return product;
    }
}

