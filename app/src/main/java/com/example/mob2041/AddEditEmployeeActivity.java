package com.example.mob2041;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mob2041.dao.UserDAO;
import com.example.mob2041.model.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditEmployeeActivity extends AppCompatActivity {
    private TextInputEditText edtUsername, edtPassword, edtFullName, edtEmail, edtPhone;
    private TextInputLayout tilUsername, tilPassword, tilFullName, tilEmail, tilPhone;
    private Spinner spinnerRole;
    private MaterialButton btnSave;
    private UserDAO userDAO;
    private int userId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_employee);

        initViews();
        setupToolbar();
        setupSpinner();
        userDAO = new UserDAO(this);

        userId = getIntent().getIntExtra("USER_ID", -1);
        isEditMode = userId != -1;

        if (isEditMode) {
            loadUserData();
            // Ẩn password field khi edit (có thể thêm option đổi mật khẩu riêng)
            tilPassword.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEmployee();
            }
        });
    }

    private void initViews() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPassword);
        tilFullName = findViewById(R.id.tilFullName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPhone = findViewById(R.id.tilPhone);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditMode ? "Sửa nhân viên" : "Thêm nhân viên");
        }
    }

    private void setupSpinner() {
        String[] roles = {"staff", "admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
    }

    private void loadUserData() {
        User user = userDAO.getUserById(userId);
        if (user != null) {
            edtUsername.setText(user.getUsername());
            edtFullName.setText(user.getFullName());
            edtEmail.setText(user.getEmail());
            edtPhone.setText(user.getPhone());
            
            // Set spinner selection
            String[] roles = {"staff", "admin"};
            for (int i = 0; i < roles.length; i++) {
                if (roles[i].equals(user.getRole())) {
                    spinnerRole.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveEmployee() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String fullName = edtFullName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();

        // Validation
        boolean isValid = true;

        if (username.isEmpty()) {
            tilUsername.setError("Vui lòng nhập tên đăng nhập");
            isValid = false;
        } else {
            // Kiểm tra username trùng
            if (userDAO.isUsernameExists(username, userId)) {
                tilUsername.setError("Tên đăng nhập đã tồn tại");
                isValid = false;
            } else {
                tilUsername.setError(null);
            }
        }

        if (!isEditMode && password.isEmpty()) {
            tilPassword.setError("Vui lòng nhập mật khẩu");
            isValid = false;
        } else {
            tilPassword.setError(null);
        }

        if (fullName.isEmpty()) {
            tilFullName.setError("Vui lòng nhập họ tên");
            isValid = false;
        } else {
            tilFullName.setError(null);
        }

        if (!isValid) {
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);
        
        if (isEditMode) {
            user.setUserId(userId);
            // Chỉ cập nhật password nếu có nhập
            if (!password.isEmpty()) {
                user.setPassword(password);
            }
            boolean success = userDAO.updateUser(user) > 0;
            if (success) {
                Toast.makeText(this, "Cập nhật nhân viên thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật nhân viên!", Toast.LENGTH_SHORT).show();
            }
        } else {
            user.setPassword(password);
            boolean success = userDAO.insertUser(user) > 0;
            if (success) {
                Toast.makeText(this, "Thêm nhân viên thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi thêm nhân viên!", Toast.LENGTH_SHORT).show();
            }
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

