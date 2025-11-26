package com.example.mob2041;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

public class MainMenuActivity extends AppCompatActivity {
    private String userRole;
    private SharedPreferences sharedPreferences;
    private TextView tvWelcome, tvAdminSection, tvStaffSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        sharedPreferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        userRole = getIntent().getStringExtra("ROLE");
        if (userRole == null) {
            userRole = sharedPreferences.getString("USER_ROLE", "");
        }

        initViews();
        setupToolbar();
        setupMenuVisibility();
        setupClickListeners();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvAdminSection = findViewById(R.id.tvAdminSection);
        tvStaffSection = findViewById(R.id.tvStaffSection);

        String fullName = sharedPreferences.getString("FULL_NAME", "");
        tvWelcome.setText("Chào mừng, " + fullName + "!");
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void setupMenuVisibility() {
        // Hiển thị/ẩn các chức năng theo role
        if ("admin".equals(userRole)) {
            tvAdminSection.setVisibility(View.VISIBLE);
            findViewById(R.id.cardManageCategories).setVisibility(View.VISIBLE);
            findViewById(R.id.cardManageEmployees).setVisibility(View.VISIBLE);
            findViewById(R.id.cardViewReports).setVisibility(View.VISIBLE);
        } else {
            tvAdminSection.setVisibility(View.GONE);
            findViewById(R.id.cardManageCategories).setVisibility(View.GONE);
            findViewById(R.id.cardManageEmployees).setVisibility(View.GONE);
            findViewById(R.id.cardViewReports).setVisibility(View.GONE);
        }
    }

    private void setupClickListeners() {
        // Admin functions
        findViewById(R.id.cardManageProducts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ManageProductsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.cardManageCategories).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainMenuActivity.this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.cardManageEmployees).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainMenuActivity.this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.cardViewReports).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainMenuActivity.this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
            }
        });

        // Staff functions
        findViewById(R.id.cardManageCustomers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ManageCustomersActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.cardShoppingCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.cardCreateOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, CreateOrderActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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

