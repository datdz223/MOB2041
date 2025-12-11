package com.example.mob2041;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob2041.adapter.ProductAdapter;
import com.example.mob2041.dao.InventoryDAO;
import com.example.mob2041.dao.ProductDAO;
import com.example.mob2041.model.CartItem;
import com.example.mob2041.model.Product;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class SelectProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private ProductDAO productDAO;
    private InventoryDAO inventoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product);

        initViews();
        setupToolbar();
        productDAO = new ProductDAO(this);
        inventoryDAO = new InventoryDAO(this);
        productList = new ArrayList<>();

        setupRecyclerView();
        loadProducts();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chọn sản phẩm");
        }
    }

    private void setupRecyclerView() {
        adapter = new ProductAdapter(productList, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onEditClick(Product product) {
                // Không cho phép edit ở đây, chỉ cho phép chọn
            }

            @Override
            public void onDeleteClick(Product product) {
                // Không cho phép delete ở đây
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Thêm click listener để chọn sản phẩm
        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                selectProduct(product);
            }
        });
    }

    private void loadProducts() {
        productList.clear();
        productList.addAll(productDAO.getAllProducts());
        adapter.notifyDataSetChanged();
    }

    private void selectProduct(Product product) {
        // Kiểm tra tồn kho
        com.example.mob2041.model.Inventory inventory = inventoryDAO.getInventoryByProductId(product.getProductId());
        int availableStock = (inventory != null) ? inventory.getStockQuantity() : 9999; // Nếu chưa có inventory, cho phép thêm với số lượng lớn

        if (inventory != null && availableStock <= 0) {
            Toast.makeText(this, "Sản phẩm " + product.getProductName() + " đã hết hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mở dialog để nhập số lượng
        int maxQuantity = (inventory != null) ? availableStock : 9999;
        AddToCartDialog dialog = new AddToCartDialog(this, product, maxQuantity, new AddToCartDialog.OnAddToCartListener() {
            @Override
            public void onAddToCart(Product product, int quantity) {
                CartItem cartItem = new CartItem(product, quantity);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("CART_ITEM", cartItem);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
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

