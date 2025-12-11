package com.example.mob2041;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mob2041.dao.InventoryDAO;
import com.example.mob2041.model.Inventory;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class ViewInventoryReportActivity extends AppCompatActivity {
    private TextView tvTotalProducts, tvTotalStock, tvLowStockProducts;
    private InventoryDAO inventoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory_report);

        initViews();
        setupToolbar();
        inventoryDAO = new InventoryDAO(this);
        
        loadReport();
    }

    private void initViews() {
        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        tvTotalStock = findViewById(R.id.tvTotalStock);
        tvLowStockProducts = findViewById(R.id.tvLowStockProducts);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Báo cáo tồn kho");
        }
    }

    private void loadReport() {
        List<Inventory> inventoryList = inventoryDAO.getAllInventory();
        
        int totalProducts = inventoryList.size();
        int totalStock = 0;
        int lowStockCount = 0;
        
        for (Inventory inventory : inventoryList) {
            totalStock += inventory.getStockQuantity();
            if (inventory.getStockQuantity() < 10) { // Cảnh báo khi tồn kho < 10
                lowStockCount++;
            }
        }
        
        tvTotalProducts.setText(String.valueOf(totalProducts));
        tvTotalStock.setText(String.valueOf(totalStock));
        tvLowStockProducts.setText(String.valueOf(lowStockCount));
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


