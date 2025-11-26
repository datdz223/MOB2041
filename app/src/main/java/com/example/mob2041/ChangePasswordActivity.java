package com.example.mob2041;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mob2041.dao.UserDAO;
import com.example.mob2041.model.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ChangePasswordActivity extends AppCompatActivity {
    private TextInputEditText edtUsername, edtOldPassword, edtNewPassword, edtConfirmPassword;
    private TextInputLayout tilUsername, tilOldPassword, tilNewPassword, tilConfirmPassword;
    private MaterialButton btnChangePassword;
    private UserDAO userDAO;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initViews();
        setupToolbar();
        userDAO = new UserDAO(this);
        sharedPreferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);

        // Nếu đã đăng nhập, điền username
        int userId = sharedPreferences.getInt("USER_ID", -1);
        if (userId != -1) {
            edtUsername.setText(sharedPreferences.getString("USERNAME", ""));
            edtUsername.setEnabled(false);
        }

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleChangePassword();
            }
        });
    }

    private void initViews() {
        edtUsername = findViewById(R.id.edtUsername);
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        tilUsername = findViewById(R.id.tilUsername);
        tilOldPassword = findViewById(R.id.tilOldPassword);
        tilNewPassword = findViewById(R.id.tilNewPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Đổi mật khẩu");
        }
    }

    private void handleChangePassword() {
        String username = edtUsername.getText().toString().trim();
        String oldPassword = edtOldPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        // Validation
        boolean isValid = true;

        if (username.isEmpty()) {
            tilUsername.setError("Vui lòng nhập tên đăng nhập");
            isValid = false;
        } else {
            tilUsername.setError(null);
        }

        if (oldPassword.isEmpty()) {
            tilOldPassword.setError("Vui lòng nhập mật khẩu cũ");
            isValid = false;
        } else {
            tilOldPassword.setError(null);
        }

        if (newPassword.isEmpty()) {
            tilNewPassword.setError("Vui lòng nhập mật khẩu mới");
            isValid = false;
        } else if (newPassword.length() < 6) {
            tilNewPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            isValid = false;
        } else {
            tilNewPassword.setError(null);
        }

        if (confirmPassword.isEmpty()) {
            tilConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            isValid = false;
        } else if (!confirmPassword.equals(newPassword)) {
            tilConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            isValid = false;
        } else {
            tilConfirmPassword.setError(null);
        }

        if (!isValid) {
            return;
        }

        // Lấy userId từ username
        User user = userDAO.login(username, oldPassword);
        if (user == null) {
            Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu cũ không đúng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đổi mật khẩu
        boolean success = userDAO.changePassword(user.getUserId(), oldPassword, newPassword);
        if (success) {
            Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

