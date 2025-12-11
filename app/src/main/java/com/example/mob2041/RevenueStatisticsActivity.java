package com.example.mob2041;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob2041.adapter.RevenueAdapter;
import com.example.mob2041.dao.OrderDAO;
import com.example.mob2041.model.RevenueByDate;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RevenueStatisticsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RevenueAdapter adapter;
    private List<RevenueByDate> revenueList;
    private OrderDAO orderDAO;
    private TextInputEditText edtFromDate, edtToDate;
    private MaterialButton btnFilter, btnToday, btnThisMonth, btnReset;
    private MaterialTextView tvTotalRevenue;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue_statistics);

        initViews();
        setupToolbar();
        orderDAO = new OrderDAO(this);
        revenueList = new ArrayList<>();
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        setupRecyclerView();
        setupDatePickers();
        setupButtons();
        loadTodayRevenue();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        edtFromDate = findViewById(R.id.edtFromDate);
        edtToDate = findViewById(R.id.edtToDate);
        btnFilter = findViewById(R.id.btnFilter);
        btnToday = findViewById(R.id.btnToday);
        btnThisMonth = findViewById(R.id.btnThisMonth);
        btnReset = findViewById(R.id.btnReset);
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Thống kê doanh thu");
        }
    }

    private void setupRecyclerView() {
        adapter = new RevenueAdapter(revenueList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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
            filterRevenue();
        });

        btnThisMonth.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            String firstDay = dateFormat.format(cal.getTime());
            String lastDay = dateFormat.format(Calendar.getInstance().getTime());
            edtFromDate.setText(firstDay);
            edtToDate.setText(lastDay);
            filterRevenue();
        });

        btnFilter.setOnClickListener(v -> filterRevenue());

        btnReset.setOnClickListener(v -> {
            edtFromDate.setText("");
            edtToDate.setText("");
            loadTodayRevenue();
        });
    }

    private void loadTodayRevenue() {
        String today = dateFormat.format(Calendar.getInstance().getTime());
        edtFromDate.setText(today);
        edtToDate.setText(today);
        filterRevenue();
    }

    private void filterRevenue() {
        String fromDate = edtFromDate.getText().toString().trim();
        String toDate = edtToDate.getText().toString().trim();

        if (fromDate.isEmpty() || toDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn đầy đủ từ ngày và đến ngày!", Toast.LENGTH_SHORT).show();
            return;
        }

        revenueList.clear();
        revenueList.addAll(orderDAO.getRevenueByDate(fromDate, toDate));
        adapter.notifyDataSetChanged();

        // Tính tổng doanh thu
        double totalRevenue = orderDAO.getTotalRevenue(fromDate, toDate);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
        tvTotalRevenue.setText("Tổng doanh thu: " + formatter.format(totalRevenue));

        if (revenueList.isEmpty()) {
            Toast.makeText(this, "Không có doanh thu trong khoảng thời gian này!", Toast.LENGTH_SHORT).show();
        }
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

