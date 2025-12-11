package com.example.mob2041.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob2041.R;
import com.example.mob2041.model.WarehouseReceiptDetail;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class WarehouseReceiptDetailAdapter extends RecyclerView.Adapter<WarehouseReceiptDetailAdapter.DetailViewHolder> {
    private List<WarehouseReceiptDetail> details;

    public WarehouseReceiptDetailAdapter(List<WarehouseReceiptDetail> details) {
        this.details = details;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_receipt_detail, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        holder.bind(details.get(position));
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName, tvQuantity, tvUnitPrice, tvSubtotal;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvUnitPrice = itemView.findViewById(R.id.tvUnitPrice);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
        }

        public void bind(WarehouseReceiptDetail detail) {
            if (detail.getProduct() != null) {
                tvProductName.setText(detail.getProduct().getProductName());
            }
            tvQuantity.setText("SL: " + detail.getQuantity());
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
            tvUnitPrice.setText("Đơn giá: " + formatter.format(detail.getUnitPrice()));
            tvSubtotal.setText("Thành tiền: " + formatter.format(detail.getSubtotal()));
        }
    }
}


