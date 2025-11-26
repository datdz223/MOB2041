package com.example.mob2041;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob2041.adapter.CartItemAdapter;
import com.example.mob2041.model.CartItem;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CartItemAdapter adapter;
    private List<CartItem> cartItems;
    private TextView tvTotal;
    private MaterialButton btnCheckout;
    private double totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        initViews();
        setupToolbar();
        cartItems = new ArrayList<>();

        setupRecyclerView();
        calculateTotal();

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItems.isEmpty()) {
                    Toast.makeText(ShoppingCartActivity.this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                } else {
                    // Chuyển sang màn hình tạo hóa đơn
                    Toast.makeText(ShoppingCartActivity.this, "Chức năng tạo hóa đơn đang phát triển", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Giỏ hàng");
        }
    }

    private void setupRecyclerView() {
        adapter = new CartItemAdapter(cartItems, new CartItemAdapter.OnCartItemClickListener() {
            @Override
            public void onQuantityChange() {
                calculateTotal();
            }

            @Override
            public void onRemoveClick(CartItem item) {
                cartItems.remove(item);
                adapter.notifyDataSetChanged();
                calculateTotal();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void calculateTotal() {
        totalAmount = 0;
        for (CartItem item : cartItems) {
            totalAmount += item.getSubtotal();
        }
        java.text.NumberFormat formatter = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.forLanguageTag("vi-VN"));
        tvTotal.setText("Tổng tiền: " + formatter.format(totalAmount));
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

