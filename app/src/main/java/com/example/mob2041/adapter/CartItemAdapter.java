package com.example.mob2041.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob2041.R;
import com.example.mob2041.model.CartItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    private List<CartItem> cartItems;
    private OnCartItemClickListener listener;

    public interface OnCartItemClickListener {
        void onQuantityChange();
        void onRemoveClick(CartItem item);
    }

    public CartItemAdapter(List<CartItem> cartItems, OnCartItemClickListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName, tvPrice, tvQuantity, tvSubtotal;
        private View btnRemove;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }

        public void bind(CartItem item) {
            tvProductName.setText(item.getProduct().getProductName());
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
            tvPrice.setText("Giá: " + formatter.format(item.getProduct().getPrice()));
            tvQuantity.setText("Số lượng: " + item.getQuantity());
            tvSubtotal.setText("Thành tiền: " + formatter.format(item.getSubtotal()));

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRemoveClick(item);
                }
            });
        }
    }
}

