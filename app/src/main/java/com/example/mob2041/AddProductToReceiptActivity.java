package com.example.mob2041;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mob2041.dao.ProductDAO;
import com.example.mob2041.model.Product;
import com.example.mob2041.model.WarehouseReceiptDetail;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AddProductToReceiptActivity extends AppCompatActivity {
    private Spinner spinnerProduct;
    private TextInputEditText edtQuantity, edtUnitPrice;
    private TextInputLayout tilQuantity, tilUnitPrice;
    private MaterialButton btnAdd;
    private ProductDAO productDAO;
    private List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_to_receipt);

        initViews();
        setupToolbar();
        setupSpinner();
        
        productDAO = new ProductDAO(this);
        products = productDAO.getAllProducts();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
    }

    private void initViews() {
        spinnerProduct = findViewById(R.id.spinnerProduct);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtUnitPrice = findViewById(R.id.edtUnitPrice);
        tilQuantity = findViewById(R.id.tilQuantity);
        tilUnitPrice = findViewById(R.id.tilUnitPrice);
        btnAdd = findViewById(R.id.btnAdd);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Thêm sản phẩm");
        }
    }

    private void setupSpinner() {
        List<String> productNames = new ArrayList<>();
        for (Product product : products) {
            productNames.add(product.getProductName() + " - " + product.getPrice() + " ₫");
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProduct.setAdapter(adapter);
    }

    private void addProduct() {
        if (spinnerProduct.getSelectedItemPosition() < 0 || products.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }

        String quantityStr = edtQuantity.getText().toString().trim();
        String unitPriceStr = edtUnitPrice.getText().toString().trim();

        if (quantityStr.isEmpty()) {
            tilQuantity.setError("Vui lòng nhập số lượng");
            return;
        }

        if (unitPriceStr.isEmpty()) {
            tilUnitPrice.setError("Vui lòng nhập đơn giá");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            double unitPrice = Double.parseDouble(unitPriceStr);

            if (quantity <= 0) {
                tilQuantity.setError("Số lượng phải lớn hơn 0");
                return;
            }

            if (unitPrice <= 0) {
                tilUnitPrice.setError("Đơn giá phải lớn hơn 0");
                return;
            }

            Product selectedProduct = products.get(spinnerProduct.getSelectedItemPosition());
            WarehouseReceiptDetail detail = new WarehouseReceiptDetail();
            detail.setProductId(selectedProduct.getProductId());
            detail.setQuantity(quantity);
            detail.setUnitPrice(unitPrice);
            detail.setProduct(selectedProduct);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("RECEIPT_DETAIL", detail);
            setResult(RESULT_OK, resultIntent);
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số hợp lệ!", Toast.LENGTH_SHORT).show();
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


