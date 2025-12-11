package com.example.mob2041;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.example.mob2041.model.Product;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddToCartDialog {
    private Context context;
    private Product product;
    private int maxQuantity;
    private OnAddToCartListener listener;
    private Dialog dialog;

    public interface OnAddToCartListener {
        void onAddToCart(Product product, int quantity);
    }

    public AddToCartDialog(Context context, Product product, int maxQuantity, OnAddToCartListener listener) {
        this.context = context;
        this.product = product;
        this.maxQuantity = maxQuantity;
        this.listener = listener;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_add_to_cart, null);
        builder.setView(view);

        TextInputEditText edtQuantity = view.findViewById(R.id.edtQuantity);
        TextInputLayout tilQuantity = view.findViewById(R.id.tilQuantity);
        MaterialButton btnAdd = view.findViewById(R.id.btnAdd);
        MaterialButton btnCancel = view.findViewById(R.id.btnCancel);

        dialog = builder.create();
        dialog.setTitle("Thêm vào giỏ hàng: " + product.getProductName());

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityStr = edtQuantity.getText().toString().trim();
                if (quantityStr.isEmpty()) {
                    tilQuantity.setError("Vui lòng nhập số lượng");
                    return;
                }

                try {
                    int quantity = Integer.parseInt(quantityStr);
                    if (quantity <= 0) {
                        tilQuantity.setError("Số lượng phải lớn hơn 0");
                        return;
                    }
                    if (quantity > maxQuantity) {
                        tilQuantity.setError("Số lượng không được vượt quá " + maxQuantity);
                        return;
                    }

                    tilQuantity.setError(null);
                    if (listener != null) {
                        listener.onAddToCart(product, quantity);
                    }
                    dialog.dismiss();
                } catch (NumberFormatException e) {
                    tilQuantity.setError("Số lượng không hợp lệ");
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

