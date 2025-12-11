package com.example.mob2041.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob2041.R;
import com.example.mob2041.model.Customer;

import java.util.List;

public class CustomerSelectionAdapter extends RecyclerView.Adapter<CustomerSelectionAdapter.CustomerSelectionViewHolder> {
    private List<Customer> customerList;
    private OnCustomerSelectListener listener;

    public interface OnCustomerSelectListener {
        void onCustomerSelect(Customer customer);
    }

    public CustomerSelectionAdapter(List<Customer> customerList, OnCustomerSelectListener listener) {
        this.customerList = customerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomerSelectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer_selection, parent, false);
        return new CustomerSelectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerSelectionViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.bind(customer);
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    class CustomerSelectionViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCustomerName, tvPhone, tvEmail;

        public CustomerSelectionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvEmail);
        }

        public void bind(Customer customer) {
            // Xử lý khách hàng ẩn danh (id = -1)
            if (customer.getCustomerId() == -1) {
                tvCustomerName.setText("Ẩn danh");
                tvPhone.setText("Khách hàng không để lại thông tin");
                tvEmail.setText("");
            } else {
                tvCustomerName.setText(customer.getCustomerName());
                tvPhone.setText(customer.getPhone() != null ? customer.getPhone() : "Chưa có");
                tvEmail.setText(customer.getEmail() != null ? customer.getEmail() : "Chưa có");
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onCustomerSelect(customer);
                    }
                }
            });
        }
    }
}

