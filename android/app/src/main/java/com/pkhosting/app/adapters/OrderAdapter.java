package com.pkhosting.app.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pkhosting.app.databinding.ItemOrderBinding;
import com.pkhosting.app.models.Order;
import com.pkhosting.app.utils.CurrencyUtil;
import com.pkhosting.app.utils.DateUtils;

import java.util.List;
import java.util.Locale;

/**
 * Orders list (full history and the recent-orders section of the dashboard).
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    private final List<Order> orders;
    private final OnOrderClickListener listener;

    public OrderAdapter(List<Order> orders, OnOrderClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    public void replace(List<Order> newOrders) {
        orders.clear();
        orders.addAll(newOrders);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = ItemOrderBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderHolder extends RecyclerView.ViewHolder {
        private final ItemOrderBinding binding;

        OrderHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Order order) {
            binding.textOrderNumber.setText(order.getOrderNumber());
            binding.textOrderItem.setText(order.getItemName());
            binding.textOrderDate.setText(DateUtils.formatDate(order.getCreatedAt()));
            binding.textOrderAmount.setText(CurrencyUtil.format(order.getAmount()));
            binding.chipOrderType.setText(order.getType().substring(0, 1).toUpperCase(Locale.US)
                    + order.getType().substring(1));
            binding.chipOrderStatus.setText(order.getStatus());
            binding.getRoot().setOnClickListener(v -> listener.onOrderClick(order));
        }
    }
}
