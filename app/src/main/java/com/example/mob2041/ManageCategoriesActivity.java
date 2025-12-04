package com.example.mob2041;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob2041.adapter.CategoryAdapter;
import com.example.mob2041.dao.CategoryDAO;
import com.example.mob2041.model.Category;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ManageCategoriesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<Category> categoryList;
    private CategoryDAO categoryDAO;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);

        initViews();
        setupToolbar();
        categoryDAO = new CategoryDAO(this);
        categoryList = new ArrayList<>();

        setupRecyclerView();
        loadCategories();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageCategoriesActivity.this, AddEditCategoryActivity.class);
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
            getSupportActionBar().setTitle("Quản lý danh mục");
        }
    }

    private void setupRecyclerView() {
        adapter = new CategoryAdapter(categoryList, new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onEditClick(Category category) {
                Intent intent = new Intent(ManageCategoriesActivity.this, AddEditCategoryActivity.class);
                intent.putExtra("CATEGORY_ID", category.getCategoryId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Category category) {
                deleteCategory(category);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadCategories() {
        categoryList.clear();
        categoryList.addAll(categoryDAO.getAllCategories());
        adapter.notifyDataSetChanged();
    }

    private void deleteCategory(Category category) {
        int result = categoryDAO.deleteCategory(category.getCategoryId());
        if (result > 0) {
            Toast.makeText(this, "Xóa danh mục thành công!", Toast.LENGTH_SHORT).show();
            loadCategories();
        } else {
            Toast.makeText(this, "Xóa danh mục thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategories();
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

