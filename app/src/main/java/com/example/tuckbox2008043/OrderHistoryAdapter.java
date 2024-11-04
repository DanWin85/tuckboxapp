package com.example.tuckbox2008043;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuckbox2008043.DataModel.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {
    private List<Order> orders = new ArrayList<>();
    private final Context context;
    private final OrderClickListener listener;
    private final SimpleDateFormat dateFormat;

    public interface OrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderHistoryAdapter(Context context, OrderClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        // Set order date
        holder.orderDate.setText(dateFormat.format(order.getOrderDate()));

        // Set order details
        StringBuilder details = new StringBuilder();
        details.append("Order #").append(order.getOrderId());

        holder.orderDetails.setText(details.toString());

        holder.itemView.setOnClickListener(v -> listener.onOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setOrders(List<Order> orders) {
        this.orders = new ArrayList<>(orders);
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderDate;
        TextView orderDetails;

        OrderViewHolder(View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderDetails = itemView.findViewById(R.id.orderDetails);
        }
    }
}