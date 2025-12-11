package com.example.mob2041.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mob2041.database.DatabaseHelper;
import com.example.mob2041.model.CartItem;
import com.example.mob2041.model.Customer;
import com.example.mob2041.model.OrderDetail;
import com.example.mob2041.model.Product;
import com.example.mob2041.model.RevenueByDate;
import com.example.mob2041.model.SalesOrder;

import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public OrderDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Tạo đơn hàng mới (customerId có thể null cho khách hàng ẩn danh)
    public long createOrder(Integer customerId, double totalAmount, int staffId) {
        open();
        ContentValues values = new ContentValues();
        if (customerId != null) {
            values.put(DatabaseHelper.COL_CUSTOMER_ID, customerId);
        } else {
            values.putNull(DatabaseHelper.COL_CUSTOMER_ID);
        }
        values.put(DatabaseHelper.COL_TOTAL_AMOUNT, totalAmount);
        values.put(DatabaseHelper.COL_STAFF_ID, staffId);
        values.put(DatabaseHelper.COL_STATUS, "pending");

        long orderId = db.insert(DatabaseHelper.TABLE_SALES_ORDERS, null, values);
        close();
        return orderId;
    }

    // Thêm chi tiết đơn hàng
    public long insertOrderDetail(int orderId, CartItem cartItem) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_ORDER_ID, orderId);
        values.put(DatabaseHelper.COL_PRODUCT_ID, cartItem.getProduct().getProductId());
        values.put(DatabaseHelper.COL_QUANTITY, cartItem.getQuantity());
        values.put(DatabaseHelper.COL_UNIT_PRICE, cartItem.getProduct().getPrice());
        values.put(DatabaseHelper.COL_SUBTOTAL, cartItem.getSubtotal());

        long result = db.insert(DatabaseHelper.TABLE_ORDER_DETAILS, null, values);
        close();
        return result;
    }

    // Cập nhật trạng thái đơn hàng
    public int updateOrderStatus(int orderId, String status) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_STATUS, status);

        int result = db.update(DatabaseHelper.TABLE_SALES_ORDERS, values,
                DatabaseHelper.COL_ORDER_ID + " = ?",
                new String[]{String.valueOf(orderId)});
        close();
        return result;
    }

    // Thêm thanh toán
    public long insertPayment(int orderId, String paymentMethod, double amount) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_ORDER_ID, orderId);
        values.put(DatabaseHelper.COL_PAYMENT_METHOD, paymentMethod);
        values.put(DatabaseHelper.COL_PAYMENT_AMOUNT, amount);

        long result = db.insert(DatabaseHelper.TABLE_PAYMENTS, null, values);
        
        // Cập nhật trạng thái đơn hàng thành completed
        if (result > 0) {
            updateOrderStatus(orderId, "completed");
        }
        close();
        return result;
    }

    // Lấy tất cả hóa đơn
    public List<SalesOrder> getAllOrders() {
        open();
        List<SalesOrder> orders = new ArrayList<>();
        String query = "SELECT o.*, " +
                "c." + DatabaseHelper.COL_CUSTOMER_NAME + ", " +
                "u." + DatabaseHelper.COL_FULL_NAME + " as staff_name " +
                "FROM " + DatabaseHelper.TABLE_SALES_ORDERS + " o " +
                "LEFT JOIN " + DatabaseHelper.TABLE_CUSTOMERS + " c " +
                "ON o." + DatabaseHelper.COL_CUSTOMER_ID + " = c." + DatabaseHelper.COL_CUSTOMER_ID + " " +
                "LEFT JOIN " + DatabaseHelper.TABLE_USERS + " u " +
                "ON o." + DatabaseHelper.COL_STAFF_ID + " = u." + DatabaseHelper.COL_USER_ID + " " +
                "ORDER BY o." + DatabaseHelper.COL_ORDER_DATE + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                SalesOrder order = new SalesOrder();
                order.setOrderId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ID)));
                
                // Kiểm tra customer_id có NULL không (khách hàng ẩn danh)
                int customerIdIndex = cursor.getColumnIndex(DatabaseHelper.COL_CUSTOMER_ID);
                if (customerIdIndex >= 0 && cursor.isNull(customerIdIndex)) {
                    // Khách hàng ẩn danh (customer_id = NULL)
                    order.setCustomerId(0);
                    order.setCustomerName("Ẩn danh");
                } else {
                    order.setCustomerId(cursor.getInt(customerIdIndex));
                    int customerNameIndex = cursor.getColumnIndex(DatabaseHelper.COL_CUSTOMER_NAME);
                    if (customerNameIndex >= 0) {
                        String customerName = cursor.getString(customerNameIndex);
                        order.setCustomerName(customerName != null ? customerName : "Ẩn danh");
                    } else {
                        order.setCustomerName("Ẩn danh");
                    }
                }
                
                order.setOrderDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_DATE)));
                order.setTotalAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TOTAL_AMOUNT)));
                order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STATUS)));
                order.setStaffId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STAFF_ID)));
                
                int staffNameIndex = cursor.getColumnIndex("staff_name");
                if (staffNameIndex >= 0) {
                    order.setStaffName(cursor.getString(staffNameIndex));
                }
                
                orders.add(order);
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return orders;
    }

    // Lấy hóa đơn theo khoảng ngày
    public List<SalesOrder> getOrdersByDateRange(String fromDate, String toDate) {
        open();
        List<SalesOrder> orders = new ArrayList<>();
        String query = "SELECT o.*, " +
                "c." + DatabaseHelper.COL_CUSTOMER_NAME + ", " +
                "u." + DatabaseHelper.COL_FULL_NAME + " as staff_name " +
                "FROM " + DatabaseHelper.TABLE_SALES_ORDERS + " o " +
                "LEFT JOIN " + DatabaseHelper.TABLE_CUSTOMERS + " c " +
                "ON o." + DatabaseHelper.COL_CUSTOMER_ID + " = c." + DatabaseHelper.COL_CUSTOMER_ID + " " +
                "LEFT JOIN " + DatabaseHelper.TABLE_USERS + " u " +
                "ON o." + DatabaseHelper.COL_STAFF_ID + " = u." + DatabaseHelper.COL_USER_ID + " " +
                "WHERE DATE(o." + DatabaseHelper.COL_ORDER_DATE + ") >= DATE(?) " +
                "AND DATE(o." + DatabaseHelper.COL_ORDER_DATE + ") <= DATE(?) " +
                "ORDER BY o." + DatabaseHelper.COL_ORDER_DATE + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{fromDate, toDate});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                SalesOrder order = new SalesOrder();
                order.setOrderId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ID)));
                
                // Kiểm tra customer_id có NULL không (khách hàng ẩn danh)
                int customerIdIndex = cursor.getColumnIndex(DatabaseHelper.COL_CUSTOMER_ID);
                if (customerIdIndex >= 0 && cursor.isNull(customerIdIndex)) {
                    // Khách hàng ẩn danh (customer_id = NULL)
                    order.setCustomerId(0);
                    order.setCustomerName("Ẩn danh");
                } else {
                    order.setCustomerId(cursor.getInt(customerIdIndex));
                    int customerNameIndex = cursor.getColumnIndex(DatabaseHelper.COL_CUSTOMER_NAME);
                    if (customerNameIndex >= 0) {
                        String customerName = cursor.getString(customerNameIndex);
                        order.setCustomerName(customerName != null ? customerName : "Ẩn danh");
                    } else {
                        order.setCustomerName("Ẩn danh");
                    }
                }
                
                order.setOrderDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_DATE)));
                order.setTotalAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TOTAL_AMOUNT)));
                order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STATUS)));
                order.setStaffId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STAFF_ID)));
                
                int staffNameIndex = cursor.getColumnIndex("staff_name");
                if (staffNameIndex >= 0) {
                    order.setStaffName(cursor.getString(staffNameIndex));
                }
                
                orders.add(order);
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return orders;
    }

    // Lấy hóa đơn theo ID
    public SalesOrder getOrderById(int orderId) {
        open();
        SalesOrder order = null;
        String query = "SELECT o.*, " +
                "c." + DatabaseHelper.COL_CUSTOMER_NAME + ", " +
                "u." + DatabaseHelper.COL_FULL_NAME + " as staff_name " +
                "FROM " + DatabaseHelper.TABLE_SALES_ORDERS + " o " +
                "LEFT JOIN " + DatabaseHelper.TABLE_CUSTOMERS + " c " +
                "ON o." + DatabaseHelper.COL_CUSTOMER_ID + " = c." + DatabaseHelper.COL_CUSTOMER_ID + " " +
                "LEFT JOIN " + DatabaseHelper.TABLE_USERS + " u " +
                "ON o." + DatabaseHelper.COL_STAFF_ID + " = u." + DatabaseHelper.COL_USER_ID + " " +
                "WHERE o." + DatabaseHelper.COL_ORDER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});

        if (cursor != null && cursor.moveToFirst()) {
            order = new SalesOrder();
            order.setOrderId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ID)));
            
            // Kiểm tra customer_id có NULL không (khách hàng ẩn danh)
            int customerIdIndex = cursor.getColumnIndex(DatabaseHelper.COL_CUSTOMER_ID);
            if (customerIdIndex >= 0 && cursor.isNull(customerIdIndex)) {
                // Khách hàng ẩn danh (customer_id = NULL)
                order.setCustomerId(0);
                order.setCustomerName("Ẩn danh");
            } else {
                order.setCustomerId(cursor.getInt(customerIdIndex));
                int customerNameIndex = cursor.getColumnIndex(DatabaseHelper.COL_CUSTOMER_NAME);
                if (customerNameIndex >= 0) {
                    String customerName = cursor.getString(customerNameIndex);
                    order.setCustomerName(customerName != null ? customerName : "Ẩn danh");
                } else {
                    order.setCustomerName("Ẩn danh");
                }
            }
            
            order.setOrderDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_DATE)));
            order.setTotalAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TOTAL_AMOUNT)));
            order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STATUS)));
            order.setStaffId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STAFF_ID)));
            
            int staffNameIndex = cursor.getColumnIndex("staff_name");
            if (staffNameIndex >= 0) {
                order.setStaffName(cursor.getString(staffNameIndex));
            }
            
            cursor.close();
        }
        close();
        return order;
    }

    // Lấy chi tiết hóa đơn
    public List<OrderDetail> getOrderDetails(int orderId) {
        open();
        List<OrderDetail> details = new ArrayList<>();
        String query = "SELECT od.*, p." + DatabaseHelper.COL_PRODUCT_NAME + " " +
                "FROM " + DatabaseHelper.TABLE_ORDER_DETAILS + " od " +
                "LEFT JOIN " + DatabaseHelper.TABLE_PRODUCTS + " p " +
                "ON od." + DatabaseHelper.COL_PRODUCT_ID + " = p." + DatabaseHelper.COL_PRODUCT_ID + " " +
                "WHERE od." + DatabaseHelper.COL_ORDER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                OrderDetail detail = new OrderDetail();
                detail.setOrderDetailId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_DETAIL_ID)));
                detail.setOrderId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ID)));
                detail.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_ID)));
                detail.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUANTITY)));
                detail.setUnitPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UNIT_PRICE)));
                detail.setSubtotal(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SUBTOTAL)));
                detail.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_NAME)));
                details.add(detail);
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return details;
    }

    // Xóa hóa đơn
    public int deleteOrder(int orderId) {
        open();
        // Xóa chi tiết hóa đơn trước
        db.delete(DatabaseHelper.TABLE_ORDER_DETAILS,
                DatabaseHelper.COL_ORDER_ID + " = ?",
                new String[]{String.valueOf(orderId)});
        
        // Xóa thanh toán
        db.delete(DatabaseHelper.TABLE_PAYMENTS,
                DatabaseHelper.COL_ORDER_ID + " = ?",
                new String[]{String.valueOf(orderId)});
        
        // Xóa hóa đơn
        int result = db.delete(DatabaseHelper.TABLE_SALES_ORDERS,
                DatabaseHelper.COL_ORDER_ID + " = ?",
                new String[]{String.valueOf(orderId)});
        close();
        return result;
    }

    // Lấy doanh thu theo ngày
    public List<RevenueByDate> getRevenueByDate(String fromDate, String toDate) {
        open();
        List<RevenueByDate> revenueList = new ArrayList<>();
        String query = "SELECT DATE(" + DatabaseHelper.COL_ORDER_DATE + ") as order_date, " +
                "SUM(" + DatabaseHelper.COL_TOTAL_AMOUNT + ") as total_revenue, " +
                "COUNT(*) as order_count " +
                "FROM " + DatabaseHelper.TABLE_SALES_ORDERS + " " +
                "WHERE " + DatabaseHelper.COL_STATUS + " = 'completed' " +
                "AND DATE(" + DatabaseHelper.COL_ORDER_DATE + ") >= DATE(?) " +
                "AND DATE(" + DatabaseHelper.COL_ORDER_DATE + ") <= DATE(?) " +
                "GROUP BY DATE(" + DatabaseHelper.COL_ORDER_DATE + ") " +
                "ORDER BY DATE(" + DatabaseHelper.COL_ORDER_DATE + ") DESC";

        Cursor cursor = db.rawQuery(query, new String[]{fromDate, toDate});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                RevenueByDate revenue = new RevenueByDate();
                revenue.setDate(cursor.getString(cursor.getColumnIndexOrThrow("order_date")));
                revenue.setRevenue(cursor.getDouble(cursor.getColumnIndexOrThrow("total_revenue")));
                revenue.setOrderCount(cursor.getInt(cursor.getColumnIndexOrThrow("order_count")));
                revenueList.add(revenue);
            } while (cursor.moveToNext());
            cursor.close();
        }
        close();
        return revenueList;
    }

    // Lấy tổng doanh thu trong khoảng thời gian
    public double getTotalRevenue(String fromDate, String toDate) {
        open();
        String query = "SELECT SUM(" + DatabaseHelper.COL_TOTAL_AMOUNT + ") as total " +
                "FROM " + DatabaseHelper.TABLE_SALES_ORDERS + " " +
                "WHERE " + DatabaseHelper.COL_STATUS + " = 'completed' " +
                "AND DATE(" + DatabaseHelper.COL_ORDER_DATE + ") >= DATE(?) " +
                "AND DATE(" + DatabaseHelper.COL_ORDER_DATE + ") <= DATE(?)";

        Cursor cursor = db.rawQuery(query, new String[]{fromDate, toDate});
        double totalRevenue = 0;
        if (cursor != null && cursor.moveToFirst()) {
            if (!cursor.isNull(0)) {
                totalRevenue = cursor.getDouble(0);
            }
            cursor.close();
        }
        close();
        return totalRevenue;
    }
}


