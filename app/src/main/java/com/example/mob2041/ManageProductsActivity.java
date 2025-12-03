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
import com.example.mob2041.dao.ProductDAO;
import com.example.mob2041.model.Product;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ManageProductsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private ProductDAO productDAO;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);

        initViews();
        setupToolbar();
        productDAO = new ProductDAO(this);
        productList = new ArrayList<>();

        setupRecyclerView();
        loadProducts();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageProductsActivity.this, AddEditProductActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý sản phẩm");
        }
    }

    private void setupRecyclerView() {
        adapter = new ProductAdapter(productList, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onEditClick(Product product) {
                Intent intent = new Intent(ManageProductsActivity.this, AddEditProductActivity.class);
                intent.putExtra("PRODUCT_ID", product.getProductId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Product product) {
                deleteProduct(product);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadProducts() {
        productList.clear();
        productList.addAll(productDAO.getAllProducts());
        adapter.notifyDataSetChanged();
    }

    private void deleteProduct(Product product) {
        int result = productDAO.deleteProduct(product.getProductId());
        if (result > 0) {
            Toast.makeText(this, "Xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            loadProducts();
        } else {
            Toast.makeText(this, "Xóa sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
        }
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



