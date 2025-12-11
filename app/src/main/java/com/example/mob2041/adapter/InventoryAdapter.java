package com.example.mob2041.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob2041.R;
import com.example.mob2041.model.Inventory;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {
    private List<Inventory> inventoryList;
    private OnInventoryClickListener listener;

    public interface OnInventoryClickListener {
        void onUpdateClick(Inventory inventory);
    }

    public InventoryAdapter(List<Inventory> inventoryList, OnInventoryClickListener listener) {
        this.inventoryList = inventoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventory, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        holder.bind(inventoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    class InventoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName, tvStockQuantity, tvLastUpdated, tvWarning;
        private View btnUpdate;

        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvStockQuantity = itemView.findViewById(R.id.tvStockQuantity);
            tvLastUpdated = itemView.findViewById(R.id.tvLastUpdated);
            tvWarning = itemView.findViewById(R.id.tvWarning);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }

        public void bind(Inventory inventory) {
            if (inventory.getProduct() != null) {
                tvProductName.setText(inventory.getProduct().getProductName());
            }
            tvStockQuantity.setText("Tồn kho: " + inventory.getStockQuantity());
            tvLastUpdated.setText("Cập nhật: " + (inventory.getLastUpdated() != null ? inventory.getLastUpdated() : "N/A"));

            // Kiểm tra và hiển thị cảnh báo sắp hết
            int minimumStock = inventory.getMinimumStock() > 0 ? inventory.getMinimumStock() : 10;
            if (inventory.getStockQuantity() < minimumStock) {
                tvWarning.setVisibility(View.VISIBLE);
                tvWarning.setText("⚠️ Cảnh báo: Sắp hết hàng! (Tối thiểu: " + minimumStock + ")");
                tvWarning.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
            } else {
                tvWarning.setVisibility(View.GONE);
            }

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUpdateClick(inventory);
                }
            });
        }
    }
}


