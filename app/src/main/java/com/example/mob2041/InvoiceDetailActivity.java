package com.example.mob2041;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob2041.adapter.OrderDetailAdapter;
import com.example.mob2041.dao.OrderDAO;
import com.example.mob2041.model.OrderDetail;
import com.example.mob2041.model.SalesOrder;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InvoiceDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderDetailAdapter adapter;
    private List<OrderDetail> orderDetails;
    private OrderDAO orderDAO;
    private MaterialTextView tvOrderCode, tvStaffName, tvCustomerName, tvOrderDate, tvTotalAmount;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);

        initViews();
        setupToolbar();
        orderDAO = new OrderDAO(this);
        orderDetails = new ArrayList<>();

        orderId = getIntent().getIntExtra("ORDER_ID", -1);
        if (orderId == -1) {
            finish();
            return;
        }

        setupRecyclerView();
        loadInvoiceDetails();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        tvOrderCode = findViewById(R.id.tvOrderCode);
        tvStaffName = findViewById(R.id.tvStaffName);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Hóa Đơn Chi Tiết");
        }
    }

    private void setupRecyclerView() {
        adapter = new OrderDetailAdapter(orderDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadInvoiceDetails() {
        SalesOrder order = orderDAO.getOrderById(orderId);
        if (order != null) {
            tvOrderCode.setText("Mã hóa đơn: " + order.getOrderCode());
            tvStaffName.setText("Tên nhân viên: " + (order.getStaffName() != null ? order.getStaffName() : "N/A"));
            tvCustomerName.setText("Tên khách hàng: " + order.getCustomerName());
            tvOrderDate.setText("Ngày lập: " + order.getOrderDate());
            
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
            tvTotalAmount.setText("Tổng tiền: " + formatter.format(order.getTotalAmount()));
        }

        orderDetails.clear();
        orderDetails.addAll(orderDAO.getOrderDetails(orderId));
        adapter.notifyDataSetChanged();
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

