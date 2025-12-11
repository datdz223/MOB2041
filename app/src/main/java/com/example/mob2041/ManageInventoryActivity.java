package com.example.mob2041;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob2041.adapter.InventoryAdapter;
import com.example.mob2041.dao.InventoryDAO;
import com.example.mob2041.model.Inventory;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class ManageInventoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private InventoryAdapter adapter;
    private List<Inventory> inventoryList;
    private InventoryDAO inventoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_inventory);

        initViews();
        setupToolbar();
        inventoryDAO = new InventoryDAO(this);
        inventoryList = new ArrayList<>();

        setupRecyclerView();
        loadInventory();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý tồn kho");
        }
    }

    private void setupRecyclerView() {
        adapter = new InventoryAdapter(inventoryList, new InventoryAdapter.OnInventoryClickListener() {
            @Override
            public void onUpdateClick(Inventory inventory) {
                Intent intent = new Intent(ManageInventoryActivity.this, UpdateInventoryActivity.class);
                intent.putExtra("INVENTORY_ID", inventory.getInventoryId());
                intent.putExtra("PRODUCT_ID", inventory.getProductId());
                intent.putExtra("STOCK_QUANTITY", inventory.getStockQuantity());
                intent.putExtra("MINIMUM_STOCK", inventory.getMinimumStock());
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadInventory() {
        inventoryList.clear();
        inventoryList.addAll(inventoryDAO.getAllInventory());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInventory();
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


