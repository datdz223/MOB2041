package com.example.mob2041;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mob2041.dao.InventoryDAO;
import com.example.mob2041.model.Inventory;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class UpdateInventoryActivity extends AppCompatActivity {
    private TextInputEditText edtStockQuantity, edtMinimumStock;
    private TextInputLayout tilStockQuantity, tilMinimumStock;
    private MaterialButton btnSave;
    private InventoryDAO inventoryDAO;
    private int inventoryId;
    private int productId;
    private int currentQuantity;
    private int currentMinimumStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_inventory);

        initViews();
        setupToolbar();
        inventoryDAO = new InventoryDAO(this);

        inventoryId = getIntent().getIntExtra("INVENTORY_ID", -1);
        productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        currentQuantity = getIntent().getIntExtra("STOCK_QUANTITY", 0);
        currentMinimumStock = getIntent().getIntExtra("MINIMUM_STOCK", 10);

        edtStockQuantity.setText(String.valueOf(currentQuantity));
        edtMinimumStock.setText(String.valueOf(currentMinimumStock));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInventory();
            }
        });
    }

    private void initViews() {
        edtStockQuantity = findViewById(R.id.edtStockQuantity);
        tilStockQuantity = findViewById(R.id.tilStockQuantity);
        edtMinimumStock = findViewById(R.id.edtMinimumStock);
        tilMinimumStock = findViewById(R.id.tilMinimumStock);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Cập nhật tồn kho");
        }
    }

    private void updateInventory() {
        String quantityStr = edtStockQuantity.getText().toString().trim();
        String minimumStockStr = edtMinimumStock.getText().toString().trim();

        if (quantityStr.isEmpty()) {
            tilStockQuantity.setError("Vui lòng nhập số lượng");
            return;
        }

        if (minimumStockStr.isEmpty()) {
            tilMinimumStock.setError("Vui lòng nhập số lượng tối thiểu");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            int minimumStock = Integer.parseInt(minimumStockStr);
            
            if (quantity < 0) {
                tilStockQuantity.setError("Số lượng không được âm");
                return;
            }

            if (minimumStock < 0) {
                tilMinimumStock.setError("Số lượng tối thiểu không được âm");
                return;
            }

            int result = inventoryDAO.updateInventory(productId, quantity, minimumStock);
            if (result > 0) {
                Toast.makeText(this, "Cập nhật tồn kho thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật tồn kho!", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            tilStockQuantity.setError("Vui lòng nhập số hợp lệ");
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


