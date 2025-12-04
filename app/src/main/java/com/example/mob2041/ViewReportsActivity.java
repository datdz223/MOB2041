package com.example.mob2041;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mob2041.dao.CategoryDAO;
import com.example.mob2041.dao.CustomerDAO;
import com.example.mob2041.dao.ProductDAO;
import com.example.mob2041.dao.UserDAO;
import com.example.mob2041.database.DatabaseHelper;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.NumberFormat;
import java.util.Locale;

public class ViewReportsActivity extends AppCompatActivity {
    private TextView tvTotalProducts, tvTotalCategories, tvTotalCustomers, tvTotalStaff;
    private TextView tvTotalRevenue, tvTotalOrders, tvCompletedOrders, tvPendingOrders;
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private CustomerDAO customerDAO;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);

        initViews();
        setupToolbar();
        
        productDAO = new ProductDAO(this);
        categoryDAO = new CategoryDAO(this);
        customerDAO = new CustomerDAO(this);
        userDAO = new UserDAO(this);

        loadStatistics();
    }

    private void initViews() {
        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        tvTotalCategories = findViewById(R.id.tvTotalCategories);
        tvTotalCustomers = findViewById(R.id.tvTotalCustomers);
        tvTotalStaff = findViewById(R.id.tvTotalStaff);
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        tvCompletedOrders = findViewById(R.id.tvCompletedOrders);
        tvPendingOrders = findViewById(R.id.tvPendingOrders);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Báo cáo thống kê");
        }
    }

    private void loadStatistics() {
        // Thống kê sản phẩm
        int totalProducts = productDAO.getAllProducts().size();
        tvTotalProducts.setText(String.valueOf(totalProducts));

        // Thống kê danh mục
        int totalCategories = categoryDAO.getAllCategories().size();
        tvTotalCategories.setText(String.valueOf(totalCategories));

        // Thống kê khách hàng
        int totalCustomers = customerDAO.getAllCustomers().size();
        tvTotalCustomers.setText(String.valueOf(totalCustomers));

        // Thống kê nhân viên
        int totalStaff = userDAO.getAllStaff().size();
        tvTotalStaff.setText(String.valueOf(totalStaff));

        // Thống kê đơn hàng và doanh thu
        loadOrderStatistics();
    }

    private void loadOrderStatistics() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        android.database.sqlite.SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Tổng số đơn hàng
        String countQuery = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_SALES_ORDERS;
        android.database.Cursor cursor = db.rawQuery(countQuery, null);
        int totalOrders = 0;
        if (cursor != null && cursor.moveToFirst()) {
            totalOrders = cursor.getInt(0);
            cursor.close();
        }
        tvTotalOrders.setText(String.valueOf(totalOrders));

        // Đơn hàng đã hoàn thành
        String completedQuery = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_SALES_ORDERS +
                " WHERE " + DatabaseHelper.COL_STATUS + " = 'completed'";
        cursor = db.rawQuery(completedQuery, null);
        int completedOrders = 0;
        if (cursor != null && cursor.moveToFirst()) {
            completedOrders = cursor.getInt(0);
            cursor.close();
        }
        tvCompletedOrders.setText(String.valueOf(completedOrders));

        // Đơn hàng đang chờ
        String pendingQuery = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_SALES_ORDERS +
                " WHERE " + DatabaseHelper.COL_STATUS + " = 'pending'";
        cursor = db.rawQuery(pendingQuery, null);
        int pendingOrders = 0;
        if (cursor != null && cursor.moveToFirst()) {
            pendingOrders = cursor.getInt(0);
            cursor.close();
        }
        tvPendingOrders.setText(String.valueOf(pendingOrders));

        // Tổng doanh thu (từ các đơn hàng đã hoàn thành)
        String revenueQuery = "SELECT SUM(" + DatabaseHelper.COL_TOTAL_AMOUNT + ") FROM " +
                DatabaseHelper.TABLE_SALES_ORDERS +
                " WHERE " + DatabaseHelper.COL_STATUS + " = 'completed'";
        cursor = db.rawQuery(revenueQuery, null);
        double totalRevenue = 0;
        if (cursor != null && cursor.moveToFirst()) {
            if (!cursor.isNull(0)) {
                totalRevenue = cursor.getDouble(0);
            }
            cursor.close();
        }
        
        // Format số tiền
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        formatter.setCurrency(java.util.Currency.getInstance("VND"));
        tvTotalRevenue.setText(formatter.format(totalRevenue));

        db.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

