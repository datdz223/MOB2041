package com.example.mob2041;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mob2041.dao.ProductDAO;
import com.example.mob2041.database.DatabaseHelper;
import com.example.mob2041.model.Product;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AddEditProductActivity extends AppCompatActivity {
    private TextInputEditText edtProductName, edtDescription, edtPrice, edtUnit;
    private TextInputLayout tilProductName, tilDescription, tilPrice, tilUnit;
    private Spinner spinnerCategory;
    private MaterialButton btnSave;
    private ProductDAO productDAO;
    private int productId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        initViews();
        setupToolbar();
        productDAO = new ProductDAO(this);

        productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        isEditMode = productId != -1;

        if (isEditMode) {
            loadProductData();
        }

        loadCategories();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
    }

    private void initViews() {
        edtProductName = findViewById(R.id.edtProductName);
        edtDescription = findViewById(R.id.edtDescription);
        edtPrice = findViewById(R.id.edtPrice);
        edtUnit = findViewById(R.id.edtUnit);
        tilProductName = findViewById(R.id.tilProductName);
        tilDescription = findViewById(R.id.tilDescription);
        tilPrice = findViewById(R.id.tilPrice);
        tilUnit = findViewById(R.id.tilUnit);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditMode ? "Sửa sản phẩm" : "Thêm sản phẩm");
        }
    }

    private void loadCategories() {
        // Load categories from database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        android.database.sqlite.SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> categories = new ArrayList<>();
        categories.add("Chọn danh mục");

        android.database.Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCT_CATEGORIES,
                new String[]{DatabaseHelper.COL_CATEGORY_ID, DatabaseHelper.COL_CATEGORY_NAME},
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_NAME)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        dbHelper.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void loadProductData() {
        Product product = productDAO.getProductById(productId);
        if (product != null) {
            edtProductName.setText(product.getProductName());
            edtDescription.setText(product.getDescription());
            edtPrice.setText(String.valueOf(product.getPrice()));
            edtUnit.setText(product.getUnit());
        }
    }

    private void saveProduct() {
        String productName = edtProductName.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        String unit = edtUnit.getText().toString().trim();

        // Validation
        boolean isValid = true;

        if (productName.isEmpty()) {
            tilProductName.setError("Vui lòng nhập tên sản phẩm");
            isValid = false;
        } else {
            tilProductName.setError(null);
        }

        if (priceStr.isEmpty()) {
            tilPrice.setError("Vui lòng nhập giá");
            isValid = false;
        } else {
            try {
                double price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    tilPrice.setError("Giá phải lớn hơn 0");
                    isValid = false;
                } else {
                    tilPrice.setError(null);
                }
            } catch (NumberFormatException e) {
                tilPrice.setError("Giá không hợp lệ");
                isValid = false;
            }
        }

        if (!isValid) {
            return;
        }

        Product product = new Product();
        product.setProductName(productName);
        product.setDescription(description);
        product.setPrice(Double.parseDouble(priceStr));
        product.setUnit(unit.isEmpty() ? "cái" : unit);

        // Get category ID from spinner
        int selectedPosition = spinnerCategory.getSelectedItemPosition();
        if (selectedPosition > 0) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            android.database.sqlite.SQLiteDatabase db = dbHelper.getReadableDatabase();
            String categoryName = spinnerCategory.getSelectedItem().toString();
            android.database.Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCT_CATEGORIES,
                    new String[]{DatabaseHelper.COL_CATEGORY_ID},
                    DatabaseHelper.COL_CATEGORY_NAME + " = ?",
                    new String[]{categoryName}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                product.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_ID)));
                cursor.close();
            }
            db.close();
            dbHelper.close();
        }

        boolean success;
        if (isEditMode) {
            product.setProductId(productId);
            success = productDAO.updateProduct(product) > 0;
        } else {
            success = productDAO.insertProduct(product) > 0;
        }

        if (success) {
            Toast.makeText(this, isEditMode ? "Cập nhật sản phẩm thành công!" : "Thêm sản phẩm thành công!",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi lưu sản phẩm!", Toast.LENGTH_SHORT).show();
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

