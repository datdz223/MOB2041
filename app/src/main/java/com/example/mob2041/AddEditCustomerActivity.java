package com.example.mob2041;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mob2041.dao.CustomerDAO;
import com.example.mob2041.model.Customer;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditCustomerActivity extends AppCompatActivity {
    private TextInputEditText edtCustomerName, edtPhone, edtEmail, edtAddress;
    private TextInputLayout tilCustomerName, tilPhone, tilEmail, tilAddress;
    private MaterialButton btnSave;
    private CustomerDAO customerDAO;
    private int customerId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_customer);

        initViews();
        setupToolbar();
        customerDAO = new CustomerDAO(this);

        customerId = getIntent().getIntExtra("CUSTOMER_ID", -1);
        isEditMode = customerId != -1;

        if (isEditMode) {
            loadCustomerData();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCustomer();
            }
        });
    }

    private void initViews() {
        edtCustomerName = findViewById(R.id.edtCustomerName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
        tilCustomerName = findViewById(R.id.tilCustomerName);
        tilPhone = findViewById(R.id.tilPhone);
        tilEmail = findViewById(R.id.tilEmail);
        tilAddress = findViewById(R.id.tilAddress);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditMode ? "Sửa khách hàng" : "Thêm khách hàng");
        }
    }

    private void loadCustomerData() {
        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer != null) {
            edtCustomerName.setText(customer.getCustomerName());
            edtPhone.setText(customer.getPhone());
            edtEmail.setText(customer.getEmail());
            edtAddress.setText(customer.getAddress());
        }
    }

    private void saveCustomer() {
        String customerName = edtCustomerName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        // Validation
        boolean isValid = true;

        if (customerName.isEmpty()) {
            tilCustomerName.setError("Vui lòng nhập tên khách hàng");
            isValid = false;
        } else {
            tilCustomerName.setError(null);
        }

        if (!isValid) {
            return;
        }

        Customer customer = new Customer();
        customer.setCustomerName(customerName);
        customer.setPhone(phone.isEmpty() ? null : phone);
        customer.setEmail(email.isEmpty() ? null : email);
        customer.setAddress(address.isEmpty() ? null : address);

        boolean success;
        if (isEditMode) {
            customer.setCustomerId(customerId);
            success = customerDAO.updateCustomer(customer) > 0;
        } else {
            success = customerDAO.insertCustomer(customer) > 0;
        }

        if (success) {
            Toast.makeText(this, isEditMode ? "Cập nhật khách hàng thành công!" : "Thêm khách hàng thành công!",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi lưu khách hàng!", Toast.LENGTH_SHORT).show();
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

