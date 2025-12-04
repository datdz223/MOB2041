package com.example.mob2041;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob2041.adapter.UserAdapter;
import com.example.mob2041.dao.UserDAO;
import com.example.mob2041.model.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ManageEmployeesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> userList;
    private UserDAO userDAO;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_employees);

        initViews();
        setupToolbar();
        userDAO = new UserDAO(this);
        userList = new ArrayList<>();

        setupRecyclerView();
        loadEmployees();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageEmployeesActivity.this, AddEditEmployeeActivity.class);
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
            getSupportActionBar().setTitle("Quản lý nhân viên");
        }
    }

    private void setupRecyclerView() {
        adapter = new UserAdapter(userList, new UserAdapter.OnUserClickListener() {
            @Override
            public void onEditClick(User user) {
                Intent intent = new Intent(ManageEmployeesActivity.this, AddEditEmployeeActivity.class);
                intent.putExtra("USER_ID", user.getUserId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(User user) {
                showDeleteConfirmDialog(user);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadEmployees() {
        userList.clear();
        userList.addAll(userDAO.getAllStaff());
        adapter.notifyDataSetChanged();
    }

    private void showDeleteConfirmDialog(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa nhân viên \"" + user.getFullName() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    deleteEmployee(user);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteEmployee(User user) {
        int result = userDAO.deleteUser(user.getUserId());
        if (result > 0) {
            Toast.makeText(this, "Xóa nhân viên thành công!", Toast.LENGTH_SHORT).show();
            loadEmployees();
        } else {
            Toast.makeText(this, "Xóa nhân viên thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEmployees();
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

