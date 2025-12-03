package com.example.mob2041.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mob2041.database.DatabaseHelper;
import com.example.mob2041.model.User;

public class UserDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Đăng nhập
    public User login(String username, String password) {
        open();
        User user = null;
        String[] columns = {
                DatabaseHelper.COL_USER_ID,
                DatabaseHelper.COL_USERNAME,
                DatabaseHelper.COL_PASSWORD,
                DatabaseHelper.COL_FULL_NAME,
                DatabaseHelper.COL_ROLE,
                DatabaseHelper.COL_EMAIL,
                DatabaseHelper.COL_PHONE
        };
        String selection = DatabaseHelper.COL_USERNAME + " = ? AND " + DatabaseHelper.COL_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USERNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PASSWORD)));
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ROLE)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL)));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE)));
            cursor.close();
        }
        close();
        return user;
    }

    // Đổi mật khẩu
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        open();
        // Kiểm tra mật khẩu cũ
        String[] columns = {DatabaseHelper.COL_PASSWORD};
        String selection = DatabaseHelper.COL_USER_ID + " = ? AND " + DatabaseHelper.COL_PASSWORD + " = ?";
        String[] selectionArgs = {String.valueOf(userId), oldPassword};
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            // Cập nhật mật khẩu mới
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COL_PASSWORD, newPassword);
            int rowsAffected = db.update(DatabaseHelper.TABLE_USERS, values, DatabaseHelper.COL_USER_ID + " = ?", new String[]{String.valueOf(userId)});
            close();
            return rowsAffected > 0;
        }
        close();
        return false;
    }

    // Lấy thông tin user theo ID
    public User getUserById(int userId) {
        open();
        User user = null;
        String[] columns = {
                DatabaseHelper.COL_USER_ID,
                DatabaseHelper.COL_USERNAME,
                DatabaseHelper.COL_FULL_NAME,
                DatabaseHelper.COL_ROLE,
                DatabaseHelper.COL_EMAIL,
                DatabaseHelper.COL_PHONE,
                DatabaseHelper.COL_CREATED_DATE
        };
        String selection = DatabaseHelper.COL_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USERNAME)));
            user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME)));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ROLE)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL)));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE)));
            user.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CREATED_DATE)));
            cursor.close();
        }
        close();
        return user;
    }

    // Thêm nhân viên
    public long insertUser(User user) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_USERNAME, user.getUsername());
        values.put(DatabaseHelper.COL_PASSWORD, user.getPassword());
        values.put(DatabaseHelper.COL_FULL_NAME, user.getFullName());
        values.put(DatabaseHelper.COL_ROLE, user.getRole());
        values.put(DatabaseHelper.COL_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COL_PHONE, user.getPhone());

        long result = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        close();
        return result;
    }

    // Cập nhật nhân viên
    public int updateUser(User user) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_USERNAME, user.getUsername());
        values.put(DatabaseHelper.COL_FULL_NAME, user.getFullName());
        values.put(DatabaseHelper.COL_ROLE, user.getRole());
        values.put(DatabaseHelper.COL_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COL_PHONE, user.getPhone());
        
        // Chỉ cập nhật mật khẩu nếu có
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            values.put(DatabaseHelper.COL_PASSWORD, user.getPassword());
        }

        int result = db.update(DatabaseHelper.TABLE_USERS, values,
                DatabaseHelper.COL_USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});
        close();
        return result;
    }

    // Xóa nhân viên
    public int deleteUser(int userId) {
        open();
        int result = db.delete(DatabaseHelper.TABLE_USERS,
                DatabaseHelper.COL_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});
        close();
        return result;
    }

    // Lấy tất cả nhân viên
    public java.util.List<User> getAllUsers() {
        open();
        java.util.List<User> users = new java.util.ArrayList<>();
        String[] columns = {
                DatabaseHelper.COL_USER_ID,
                DatabaseHelper.COL_USERNAME,
                DatabaseHelper.COL_FULL_NAME,
                DatabaseHelper.COL_ROLE,
                DatabaseHelper.COL_EMAIL,
                DatabaseHelper.COL_PHONE,
                DatabaseHelper.COL_CREATED_DATE
        };

        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, columns, null, null, null, null,
                DatabaseHelper.COL_FULL_NAME);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USERNAME)));
                user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME)));
                user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ROLE)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL)));
                user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE)));
                user.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CREATED_DATE)));
                users.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return users;
    }

    // Kiểm tra username đã tồn tại chưa
    public boolean isUsernameExists(String username, int excludeUserId) {
        open();
        String[] columns = {DatabaseHelper.COL_USER_ID};
        String selection = DatabaseHelper.COL_USERNAME + " = ?";
        String[] selectionArgs = {username};
        
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        
        boolean exists = false;
        if (cursor != null && cursor.moveToFirst()) {
            int foundUserId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID));
            // Nếu tìm thấy user khác (không phải user đang chỉnh sửa)
            if (foundUserId != excludeUserId) {
                exists = true;
            }
            cursor.close();
        }
        close();
        return exists;
    }
}



