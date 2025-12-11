package com.example.mob2041.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob2041.R;
import com.example.mob2041.model.RevenueByDate;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RevenueAdapter extends RecyclerView.Adapter<RevenueAdapter.RevenueViewHolder> {
    private List<RevenueByDate> revenueList;

    public RevenueAdapter(List<RevenueByDate> revenueList) {
        this.revenueList = revenueList;
    }

    @NonNull
    @Override
    public RevenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_revenue, parent, false);
        return new RevenueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RevenueViewHolder holder, int position) {
        RevenueByDate revenue = revenueList.get(position);
        holder.bind(revenue);
    }

    @Override
    public int getItemCount() {
        return revenueList.size();
    }

    class RevenueViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate, tvRevenue, tvOrderCount;

        public RevenueViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvRevenue = itemView.findViewById(R.id.tvRevenue);
            tvOrderCount = itemView.findViewById(R.id.tvOrderCount);
        }

        public void bind(RevenueByDate revenue) {
            tvDate.setText(revenue.getDate());
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
            tvRevenue.setText(formatter.format(revenue.getRevenue()));
            tvOrderCount.setText("Số đơn: " + revenue.getOrderCount());
        }
    }
}

