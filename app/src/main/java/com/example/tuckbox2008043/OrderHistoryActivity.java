package com.example.tuckbox2008043;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHistoryActivity extends MainMenuBarBaseActivity {
    private static final String TAG = "OrderHistoryActivity";
    private AppDataModel appDataModel;
    private RecyclerView orderRecyclerView;
    private OrderHistoryAdapter adapter;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        appDataModel = new AppDataModel(getApplication());

        // Get userId from intent
        userId = getIntent().getLongExtra("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "Error: User not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupRecyclerView();
        loadOrders();
    }

    private void initializeViews() {
        orderRecyclerView = findViewById(R.id.orderRecyclerView);
    }

    private void setupRecyclerView() {
        adapter = new OrderHistoryAdapter(this, order -> {
            // Handle order click
            Intent intent = new Intent(this, OrderDetailsActivity.class);
            intent.putExtra("ORDER_ID", order.getOrderId());
            startActivity(intent);
        });
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderRecyclerView.setAdapter(adapter);
    }

    private void loadOrders() {
        appDataModel.getOrdersForUser(userId).observe(this, orders -> {
            if (orders != null && !orders.isEmpty()) {
                adapter.setOrders(orders);
            } else {
                // Show empty state
                Toast.makeText(this, "No order history found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}