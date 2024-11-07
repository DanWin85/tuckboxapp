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
import com.example.tuckbox2008043.DataModel.DeliveryAddress;
import com.example.tuckbox2008043.DataModel.Food;
import com.example.tuckbox2008043.DataModel.Order;
import com.example.tuckbox2008043.DataModel.OrderItem;
import com.example.tuckbox2008043.DataModel.TimeSlot;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class OrderDetailsActivity extends MainMenuBarBaseActivity {
    private static final String TAG = "OrderDetailsActivity";
    private AppDataModel appDataModel;
    private TextView orderDetailsText;
    private long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        appDataModel = new AppDataModel(getApplication());

        orderId = getIntent().getLongExtra("ORDER_ID", -1);
        if (orderId == -1) {
            Toast.makeText(this, "Error: Order not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
      //  isHome = false;
        initializeViews();
        displayOrderDetails();
    }

    private void initializeViews() {
        orderDetailsText = findViewById(R.id.orderDetailsText);
    }

    private void displayOrderDetails() {
        appDataModel.getOrderById(orderId).observe(this, order -> {
            if (order != null) {
                displayOrder(order);
            } else {
                orderDetailsText.setText("Order not found");
            }
        });
    }

    private void displayOrder(Order order) {
        StringBuilder details = new StringBuilder();
        details.append("Order Details\n\n");

        // Add order number and date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        details.append("Order #").append(order.getOrderId())
                .append("\nDate: ").append(dateFormat.format(order.getOrderDate()))
                .append("\n\n");

        // Add city
        City city = appDataModel.getCityById(order.getCityId());
        if (city != null) {
            details.append("Delivery City: ")
                    .append(city.getCityName())
                    .append("\n\n");
        }

        // Add delivery address
        DeliveryAddress address = appDataModel.getDeliveryAddressById(order.getAddressId());
        if (address != null) {
            details.append("Delivery Address:\n")
                    .append(address.getAddress())
                    .append("\n\n");
        }

        // Add items
        details.append("Items:\n");
        appDataModel.getOrderItems(order.getOrderId()).observe(this, orderItems -> {
            if (orderItems != null && !orderItems.isEmpty()) {
                for (OrderItem item : orderItems) {
                    Food food = appDataModel.getFoodById(item.getFoodId());
                    if (food != null) {
                        details.append("- ")
                                .append(food.getFoodName())
                                .append(" (Quantity: ")
                                .append(item.getQuantity())
                                .append(")\n");
                    }
                }
            }

            // Add time slot
            TimeSlot timeSlot = appDataModel.getTimeSlotById(order.getTimeSlotId());
            if (timeSlot != null) {
                details.append("\nDelivery Time: ")
                        .append(timeSlot.getTimeSlot())
                        .append("\n");
            }

            orderDetailsText.setText(details.toString());
        });
    }
}