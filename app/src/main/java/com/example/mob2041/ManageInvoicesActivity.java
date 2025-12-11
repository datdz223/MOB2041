package com.example.mob2041;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob2041.adapter.InvoiceAdapter;
import com.example.mob2041.dao.OrderDAO;
import com.example.mob2041.model.SalesOrder;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ManageInvoicesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private InvoiceAdapter adapter;
    private List<SalesOrder> invoiceList;
    private OrderDAO orderDAO;
    private TextInputEditText edtFromDate, edtToDate;
    private MaterialButton btnFilter, btnToday, btnReset;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_invoices);

        initViews();
        setupToolbar();
        orderDAO = new OrderDAO(this);
        invoiceList = new ArrayList<>();
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        setupRecyclerView();
        setupDatePickers();
        setupButtons();
        loadInvoices();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        edtFromDate = findViewById(R.id.edtFromDate);
        edtToDate = findViewById(R.id.edtToDate);
        btnFilter = findViewById(R.id.btnFilter);
        btnToday = findViewById(R.id.btnToday);
        btnReset = findViewById(R.id.btnReset);
    }

    private void setupDatePickers() {
        edtFromDate.setFocusable(false);
        edtFromDate.setClickable(true);
        edtFromDate.setOnClickListener(v -> showDatePicker(edtFromDate));

        edtToDate.setFocusable(false);
        edtToDate.setClickable(true);
        edtToDate.setOnClickListener(v -> showDatePicker(edtToDate));
    }

    private void showDatePicker(TextInputEditText editText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    editText.setText(dateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void setupButtons() {
        btnToday.setOnClickListener(v -> {
            String today = dateFormat.format(Calendar.getInstance().getTime());
            edtFromDate.setText(today);
            edtToDate.setText(today);
            filterInvoices();
        });

        btnFilter.setOnClickListener(v -> filterInvoices());

        btnReset.setOnClickListener(v -> {
            edtFromDate.setText("");
            edtToDate.setText("");
            loadInvoices();
        });
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý hóa đơn");
        }
    }

    private void setupRecyclerView() {
        adapter = new InvoiceAdapter(invoiceList, new InvoiceAdapter.OnInvoiceClickListener() {
            @Override
            public void onItemClick(SalesOrder order) {
                // Chuyển sang màn hình chi tiết hóa đơn
                Intent intent = new Intent(ManageInvoicesActivity.this, InvoiceDetailActivity.class);
                intent.putExtra("ORDER_ID", order.getOrderId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(SalesOrder order) {
                showDeleteConfirmDialog(order);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadInvoices() {
        invoiceList.clear();
        invoiceList.addAll(orderDAO.getAllOrders());
        adapter.notifyDataSetChanged();
    }

    private void filterInvoices() {
        String fromDate = edtFromDate.getText().toString().trim();
        String toDate = edtToDate.getText().toString().trim();

        if (fromDate.isEmpty() || toDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn đầy đủ từ ngày và đến ngày!", Toast.LENGTH_SHORT).show();
            return;
        }

        invoiceList.clear();
        invoiceList.addAll(orderDAO.getOrdersByDateRange(fromDate, toDate));
        adapter.notifyDataSetChanged();

        if (invoiceList.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy hóa đơn trong khoảng thời gian này!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Tìm thấy " + invoiceList.size() + " hóa đơn!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmDialog(SalesOrder order) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa hóa đơn " + order.getOrderCode() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    deleteInvoice(order);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteInvoice(SalesOrder order) {
        int result = orderDAO.deleteOrder(order.getOrderId());
        if (result > 0) {
            Toast.makeText(this, "Xóa hóa đơn thành công!", Toast.LENGTH_SHORT).show();
            loadInvoices();
        } else {
            Toast.makeText(this, "Xóa hóa đơn thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInvoices();
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

