package com.example.mob2041.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mob2041.database.DatabaseHelper;
import com.example.mob2041.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public CategoryDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Thêm danh mục
    public long insertCategory(Category category) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_CATEGORY_NAME, category.getCategoryName());
        values.put(DatabaseHelper.COL_CATEGORY_DESCRIPTION, category.getDescription());

        long result = db.insert(DatabaseHelper.TABLE_PRODUCT_CATEGORIES, null, values);
        close();
        return result;
    }

    // Cập nhật danh mục
    public int updateCategory(Category category) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_CATEGORY_NAME, category.getCategoryName());
        values.put(DatabaseHelper.COL_CATEGORY_DESCRIPTION, category.getDescription());

        int result = db.update(DatabaseHelper.TABLE_PRODUCT_CATEGORIES, values,
                DatabaseHelper.COL_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(category.getCategoryId())});
        close();
        return result;
    }

    // Xóa danh mục
    public int deleteCategory(int categoryId) {
        open();
        int result = db.delete(DatabaseHelper.TABLE_PRODUCT_CATEGORIES,
                DatabaseHelper.COL_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(categoryId)});
        close();
        return result;
    }

    // Lấy tất cả danh mục
    public List<Category> getAllCategories() {
        open();
        List<Category> categories = new ArrayList<>();
        String[] columns = {
                DatabaseHelper.COL_CATEGORY_ID,
                DatabaseHelper.COL_CATEGORY_NAME,
                DatabaseHelper.COL_CATEGORY_DESCRIPTION
        };

        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCT_CATEGORIES, columns, null, null, null, null,
                DatabaseHelper.COL_CATEGORY_NAME);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_ID)));
                category.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_NAME)));
                category.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_DESCRIPTION)));
                categories.add(category);
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return categories;
    }

    // Lấy danh mục theo ID
    public Category getCategoryById(int categoryId) {
        open();
        Category category = null;
        String[] columns = {
                DatabaseHelper.COL_CATEGORY_ID,
                DatabaseHelper.COL_CATEGORY_NAME,
                DatabaseHelper.COL_CATEGORY_DESCRIPTION
        };
        String selection = DatabaseHelper.COL_CATEGORY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(categoryId)};

        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCT_CATEGORIES, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            category = new Category();
            category.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_ID)));
            category.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_NAME)));
            category.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_DESCRIPTION)));
            cursor.close();
        }
        close();
        return category;
    }
}

