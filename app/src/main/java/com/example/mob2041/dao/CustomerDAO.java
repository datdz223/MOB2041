package com.example.mob2041.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mob2041.database.DatabaseHelper;
import com.example.mob2041.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public CustomerDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Thêm khách hàng
    public long insertCustomer(Customer customer) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_CUSTOMER_NAME, customer.getCustomerName());
        values.put(DatabaseHelper.COL_CUSTOMER_PHONE, customer.getPhone());
        values.put(DatabaseHelper.COL_CUSTOMER_EMAIL, customer.getEmail());
        values.put(DatabaseHelper.COL_CUSTOMER_ADDRESS, customer.getAddress());

        long result = db.insert(DatabaseHelper.TABLE_CUSTOMERS, null, values);
        close();
        return result;
    }

    // Cập nhật khách hàng
    public int updateCustomer(Customer customer) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_CUSTOMER_NAME, customer.getCustomerName());
        values.put(DatabaseHelper.COL_CUSTOMER_PHONE, customer.getPhone());
        values.put(DatabaseHelper.COL_CUSTOMER_EMAIL, customer.getEmail());
        values.put(DatabaseHelper.COL_CUSTOMER_ADDRESS, customer.getAddress());

        int result = db.update(DatabaseHelper.TABLE_CUSTOMERS, values,
                DatabaseHelper.COL_CUSTOMER_ID + " = ?",
                new String[]{String.valueOf(customer.getCustomerId())});
        close();
        return result;
    }

    // Xóa khách hàng
    public int deleteCustomer(int customerId) {
        open();
        int result = db.delete(DatabaseHelper.TABLE_CUSTOMERS,
                DatabaseHelper.COL_CUSTOMER_ID + " = ?",
                new String[]{String.valueOf(customerId)});
        close();
        return result;
    }

    // Lấy tất cả khách hàng
    public List<Customer> getAllCustomers() {
        open();
        List<Customer> customers = new ArrayList<>();
        String[] columns = {
                DatabaseHelper.COL_CUSTOMER_ID,
                DatabaseHelper.COL_CUSTOMER_NAME,
                DatabaseHelper.COL_CUSTOMER_PHONE,
                DatabaseHelper.COL_CUSTOMER_EMAIL,
                DatabaseHelper.COL_CUSTOMER_ADDRESS
        };

        Cursor cursor = db.query(DatabaseHelper.TABLE_CUSTOMERS, columns, null, null, null, null,
                DatabaseHelper.COL_CUSTOMER_NAME);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();
                customer.setCustomerId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_ID)));
                customer.setCustomerName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_NAME)));
                customer.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_PHONE)));
                customer.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_EMAIL)));
                customer.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_ADDRESS)));
                customers.add(customer);
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return customers;
    }

    // Lấy khách hàng theo ID
    public Customer getCustomerById(int customerId) {
        open();
        Customer customer = null;
        String[] columns = {
                DatabaseHelper.COL_CUSTOMER_ID,
                DatabaseHelper.COL_CUSTOMER_NAME,
                DatabaseHelper.COL_CUSTOMER_PHONE,
                DatabaseHelper.COL_CUSTOMER_EMAIL,
                DatabaseHelper.COL_CUSTOMER_ADDRESS
        };
        String selection = DatabaseHelper.COL_CUSTOMER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(customerId)};

        Cursor cursor = db.query(DatabaseHelper.TABLE_CUSTOMERS, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            customer = new Customer();
            customer.setCustomerId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_ID)));
            customer.setCustomerName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_NAME)));
            customer.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_PHONE)));
            customer.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_EMAIL)));
            customer.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CUSTOMER_ADDRESS)));
            cursor.close();
        }
        close();
        return customer;
    }
}

