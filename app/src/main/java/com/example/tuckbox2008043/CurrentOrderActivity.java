package com.example.tuckbox2008043;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tuckbox2008043.DataModel.City;
import com.example.tuckbox2008043.DataModel.Food;
import com.example.tuckbox2008043.DataModel.Order;
import com.example.tuckbox2008043.DataModel.TimeSlot;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CurrentOrderActivity extends MainMenuBarBaseActivity {

    private static final String TAG = "CurrentOrderActivity";
    private AppDataModel appDataModel;
    private TextView orderSummaryText;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        appDataModel = new AppDataModel(getApplication());

        // Get userId from intent
        userId = getIntent().getLongExtra("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "Error: User not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        displayCurrentOrder();
        isHome = false;
    }

    private void initializeViews() {
        orderSummaryText = findViewById(R.id.orderSummaryText);
    }

    private void displayCurrentOrder() {
        // Get most recent order for user
        appDataModel.getMostRecentOrder(userId).observe(this, order -> {
            if (order != null) {
                displayOrderDetails(order);
            } else {
                orderSummaryText.setText("No current order found.");
            }
        });
    }

    private void displayOrderDetails(Order order) {
        StringBuilder summary = new StringBuilder();
        summary.append("Order Details\n\n");

        // Add order date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        summary.append("Order Date: ")
                .append(dateFormat.format(order.getOrderDate()))
                .append("\n\n");

        // Add delivery address
        City city = appDataModel.getCityById(order.getCityId());
        if (city != null) {
            summary.append("Delivery City: ")
                    .append(city.getCityName())
                    .append("\n\n");
        }

        // Add food item and quantity
        Food food = appDataModel.getFoodById(order.getFoodDetailsId());
        if (food != null) {
            summary.append("Item: ")
                    .append(food.getFoodName())
                    .append("\nQuantity: ")
                    .append(order.getQuantity())
                    .append("\n\n");
        }

        // Add time slot
        TimeSlot timeSlot = appDataModel.getTimeSlotById(order.getTimeSlotId());
        if (timeSlot != null) {
            summary.append("Delivery Time: ")
                    .append(timeSlot.getTimeSlot())
                    .append("\n");
        }

        orderSummaryText.setText(summary.toString());
    }
}