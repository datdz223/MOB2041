package com.example.mob2041;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob2041.adapter.CustomerAdapter;
import com.example.mob2041.dao.CustomerDAO;
import com.example.mob2041.model.Customer;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ManageCustomersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomerAdapter adapter;
    private List<Customer> customerList;
    private CustomerDAO customerDAO;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customers);

        initViews();
        setupToolbar();
        customerDAO = new CustomerDAO(this);
        customerList = new ArrayList<>();

        setupRecyclerView();
        loadCustomers();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageCustomersActivity.this, AddEditCustomerActivity.class);
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
            getSupportActionBar().setTitle("Quản lý khách hàng");
        }
    }

    private void setupRecyclerView() {
        adapter = new CustomerAdapter(customerList, new CustomerAdapter.OnCustomerClickListener() {
            @Override
            public void onEditClick(Customer customer) {
                Intent intent = new Intent(ManageCustomersActivity.this, AddEditCustomerActivity.class);
                intent.putExtra("CUSTOMER_ID", customer.getCustomerId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Customer customer) {
                deleteCustomer(customer);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadCustomers() {
        customerList.clear();
        customerList.addAll(customerDAO.getAllCustomers());
        adapter.notifyDataSetChanged();
    }

    private void deleteCustomer(Customer customer) {
        int result = customerDAO.deleteCustomer(customer.getCustomerId());
        if (result > 0) {
            Toast.makeText(this, "Xóa khách hàng thành công!", Toast.LENGTH_SHORT).show();
            loadCustomers();
        } else {
            Toast.makeText(this, "Xóa khách hàng thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCustomers();
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



