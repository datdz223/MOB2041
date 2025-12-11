package com.example.mob2041.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob2041.R;
import com.example.mob2041.model.SalesOrder;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> {
    private List<SalesOrder> invoiceList;
    private OnInvoiceClickListener listener;

    public interface OnInvoiceClickListener {
        void onItemClick(SalesOrder order);
        void onDeleteClick(SalesOrder order);
    }

    public InvoiceAdapter(List<SalesOrder> invoiceList, OnInvoiceClickListener listener) {
        this.invoiceList = invoiceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invoice, parent, false);
        return new InvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position) {
        SalesOrder order = invoiceList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    class InvoiceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderCode, tvStaffName, tvCustomerName, tvOrderDate, tvTotalAmount;
        private View btnDelete;

        public InvoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderCode = itemView.findViewById(R.id.tvOrderCode);
            tvStaffName = itemView.findViewById(R.id.tvStaffName);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(SalesOrder order) {
            tvOrderCode.setText("Mã hóa đơn: " + order.getOrderCode());
            tvStaffName.setText("Tên nhân viên: " + (order.getStaffName() != null ? order.getStaffName() : "N/A"));
            tvCustomerName.setText("Tên khách hàng: " + order.getCustomerName());
            tvOrderDate.setText("Ngày lập: " + order.getOrderDate());
            
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
            tvTotalAmount.setText("Tổng tiền: " + formatter.format(order.getTotalAmount()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(order);
                    }
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDeleteClick(order);
                    }
                }
            });
        }
    }
}

