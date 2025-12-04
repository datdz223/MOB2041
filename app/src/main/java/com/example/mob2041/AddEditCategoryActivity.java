package com.example.mob2041;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mob2041.dao.CategoryDAO;
import com.example.mob2041.model.Category;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditCategoryActivity extends AppCompatActivity {
    private TextInputEditText edtCategoryName, edtDescription;
    private TextInputLayout tilCategoryName, tilDescription;
    private MaterialButton btnSave;
    private CategoryDAO categoryDAO;
    private int categoryId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_category);

        initViews();
        setupToolbar();
        categoryDAO = new CategoryDAO(this);

        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
        isEditMode = categoryId != -1;

        if (isEditMode) {
            loadCategoryData();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCategory();
            }
        });
    }

    private void initViews() {
        edtCategoryName = findViewById(R.id.edtCategoryName);
        edtDescription = findViewById(R.id.edtDescription);
        tilCategoryName = findViewById(R.id.tilCategoryName);
        tilDescription = findViewById(R.id.tilDescription);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditMode ? "Sửa danh mục" : "Thêm danh mục");
        }
    }

    private void loadCategoryData() {
        Category category = categoryDAO.getCategoryById(categoryId);
        if (category != null) {
            edtCategoryName.setText(category.getCategoryName());
            edtDescription.setText(category.getDescription());
        }
    }

    private void saveCategory() {
        String categoryName = edtCategoryName.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        // Validation
        boolean isValid = true;

        if (categoryName.isEmpty()) {
            tilCategoryName.setError("Vui lòng nhập tên danh mục");
            isValid = false;
        } else {
            tilCategoryName.setError(null);
        }

        if (!isValid) {
            return;
        }

        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setDescription(description);

        boolean success;
        if (isEditMode) {
            category.setCategoryId(categoryId);
            success = categoryDAO.updateCategory(category) > 0;
        } else {
            success = categoryDAO.insertCategory(category) > 0;
        }

        if (success) {
            Toast.makeText(this, isEditMode ? "Cập nhật danh mục thành công!" : "Thêm danh mục thành công!",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi lưu danh mục!", Toast.LENGTH_SHORT).show();
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

