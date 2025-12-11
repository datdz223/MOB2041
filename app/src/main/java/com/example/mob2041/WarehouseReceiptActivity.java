package com.example.mob2041;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob2041.adapter.WarehouseReceiptDetailAdapter;
import com.example.mob2041.dao.InventoryDAO;
import com.example.mob2041.dao.WarehouseReceiptDAO;
import com.example.mob2041.model.WarehouseReceipt;
import com.example.mob2041.model.WarehouseReceiptDetail;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class WarehouseReceiptActivity extends AppCompatActivity {
    private TextInputEditText edtSupplier, edtNote;
    private TextInputLayout tilSupplier;
    private RecyclerView recyclerView;
    private WarehouseReceiptDetailAdapter adapter;
    private List<WarehouseReceiptDetail> receiptDetails;
    private MaterialButton btnAddProduct, btnSaveReceipt;
    private WarehouseReceiptDAO receiptDAO;
    private InventoryDAO inventoryDAO;
    private SharedPreferences sharedPreferences;
    private int receiptId = -1; // -1 nếu tạo mới

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_receipt);

        initViews();
        setupToolbar();
        
        receiptDAO = new WarehouseReceiptDAO(this);
        inventoryDAO = new InventoryDAO(this);
        sharedPreferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        receiptDetails = new ArrayList<>();

        setupRecyclerView();

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WarehouseReceiptActivity.this, AddProductToReceiptActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        btnSaveReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReceipt();
            }
        });
    }

    private void initViews() {
        edtSupplier = findViewById(R.id.edtSupplier);
        edtNote = findViewById(R.id.edtNote);
        tilSupplier = findViewById(R.id.tilSupplier);
        recyclerView = findViewById(R.id.recyclerView);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnSaveReceipt = findViewById(R.id.btnSaveReceipt);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Ghi nhận nhập kho");
        }
    }

    private void setupRecyclerView() {
        adapter = new WarehouseReceiptDetailAdapter(receiptDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data != null) {
                WarehouseReceiptDetail detail = (WarehouseReceiptDetail) data.getSerializableExtra("RECEIPT_DETAIL");
                if (detail != null) {
                    receiptDetails.add(detail);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void saveReceipt() {
        String supplier = edtSupplier.getText().toString().trim();
        
        if (supplier.isEmpty()) {
            tilSupplier.setError("Vui lòng nhập tên nhà cung cấp");
            return;
        }
        
        if (receiptDetails.isEmpty()) {
            Toast.makeText(this, "Vui lòng thêm ít nhất một sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }

        String note = edtNote.getText().toString().trim();
        int staffId = sharedPreferences.getInt("USER_ID", -1);
        
        if (staffId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin nhân viên!", Toast.LENGTH_SHORT).show();
            return;
        }

        WarehouseReceipt receipt = new WarehouseReceipt(supplier, staffId, note);
        long receiptIdResult = receiptDAO.insertReceipt(receipt);
        
        if (receiptIdResult > 0) {
            // Thêm chi tiết và cập nhật tồn kho
            for (WarehouseReceiptDetail detail : receiptDetails) {
                detail.setReceiptId((int) receiptIdResult);
                receiptDAO.insertReceiptDetail(detail);
                // Tăng tồn kho
                inventoryDAO.increaseStock(detail.getProductId(), detail.getQuantity());
            }
            
            Toast.makeText(this, "Ghi nhận nhập kho thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi lưu phiếu nhập kho!", Toast.LENGTH_SHORT).show();
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


