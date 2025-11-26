package com.example.mob2041;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mob2041.dao.UserDAO;
import com.example.mob2041.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText edtUsername, edtPassword;
    private TextInputLayout tilUsername, tilPassword;
    private MaterialButton btnLogin;
    private UserDAO userDAO;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        userDAO = new UserDAO(this);
        sharedPreferences = getSharedPreferences("USER_INFO", MODE_PRIVATE);

        // Kiểm tra đã đăng nhập chưa
        if (sharedPreferences.getInt("USER_ID", -1) != -1) {
            navigateToMainMenu(sharedPreferences.getString("USER_ROLE", ""));
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        findViewById(R.id.tvChangePassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void handleLogin() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Validation
        if (username.isEmpty()) {
            tilUsername.setError("Vui lòng nhập tên đăng nhập");
            return;
        } else {
            tilUsername.setError(null);
        }

        if (password.isEmpty()) {
            tilPassword.setError("Vui lòng nhập mật khẩu");
            return;
        } else {
            tilPassword.setError(null);
        }

        // Đăng nhập
        User user = userDAO.login(username, password);
        if (user != null) {
            // Lưu thông tin user vào SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("USER_ID", user.getUserId());
            editor.putString("USERNAME", user.getUsername());
            editor.putString("FULL_NAME", user.getFullName());
            editor.putString("USER_ROLE", user.getRole());
            editor.apply();

            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            navigateToMainMenu(user.getRole());
        } else {
            Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToMainMenu(String role) {
        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
        intent.putExtra("ROLE", role);
        startActivity(intent);
        finish();
    }
}

