package com.example.tuckbox2008043;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tuckbox2008043.DataModel.City;
import com.example.tuckbox2008043.DataModel.DeliveryAddress;
import com.example.tuckbox2008043.DataModel.Food;
import com.example.tuckbox2008043.DataModel.FoodExtraDetails;
import com.example.tuckbox2008043.DataModel.Order;
import com.example.tuckbox2008043.DataModel.OrderItem;
import com.example.tuckbox2008043.DataModel.TimeSlot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderConfirmationActivity extends MainMenuBarBaseActivity{
    private static final String TAG = "OrderConfirmActivity";
    private AppDataModel appDataModel;
    private TextView orderSummaryText;
    private Button confirmButton;
    private Button cancelButton;
    private long userId;
    private long cityId;
    private long addressId;
    private long[] selectedFoodIds;
    private Map<Long, Integer> foodQuantities;
    private long selectedTimeSlotId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        appDataModel = new AppDataModel(getApplication());

        getDataFromIntent();
        initializeViews();
        displayOrderSummary();
        setupButtons();
     //   isHome = false;
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        userId = intent.getLongExtra("USER_ID", -1);
        cityId = intent.getLongExtra("SELECTED_CITY_ID", -1);
        addressId = intent.getLongExtra("SELECTED_ADDRESS_ID", -1);
        selectedFoodIds = intent.getLongArrayExtra("SELECTED_FOOD_IDS");
        selectedTimeSlotId = intent.getLongExtra("SELECTED_TIME_SLOT_ID", -1);

        Log.d(TAG, "Received data - UserId: " + userId +
                ", CityId: " + cityId +
                ", AddressId: " + addressId +
                ", TimeSlotId: " + selectedTimeSlotId);

        // Get quantities from bundle
        Bundle quantityBundle = intent.getBundleExtra("FOOD_QUANTITIES");
        foodQuantities = new HashMap<>();
        if (quantityBundle != null) {
            for (String key : quantityBundle.keySet()) {
                foodQuantities.put(Long.parseLong(key), quantityBundle.getInt(key));
            }
        }

        // Validate received data
        if (userId == -1 || cityId == -1 || addressId == -1 ||
                selectedTimeSlotId == -1 || selectedFoodIds == null ||
                selectedFoodIds.length == 0) {
            Log.e(TAG, "Missing required data");
            Toast.makeText(this, "Error: Missing required data", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        orderSummaryText = findViewById(R.id.orderSummaryText);
        confirmButton = findViewById(R.id.confirmButton);
        cancelButton = findViewById(R.id.cancelButton);
    }

    private void displayOrderSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Order Summary\n\n");

        // Add city
        City city = appDataModel.getCityById(cityId);
        if (city != null) {
            summary.append("Delivery City:\n")
                    .append(city.getCityName())
                    .append("\n\n");
        }

        // Add delivery address
        DeliveryAddress address = appDataModel.getDeliveryAddressById(addressId);
        if (address != null) {
            summary.append("Delivery Address:\n")
                    .append(address.getAddress())
                    .append("\n\n");
        }

        // Add selected meals with quantities
        summary.append("Selected Items:\n");
        for (long foodId : selectedFoodIds) {
            Food food = appDataModel.getFoodById(foodId);
            int quantity = foodQuantities.getOrDefault(foodId, 1);

            if (food != null) {
                summary.append("- ")
                        .append(food.getFoodName())
                        .append(" (Quantity: ")
                        .append(quantity)
                        .append(")\n");
            }
        }
        summary.append("\n");

        // Add time slot
        TimeSlot timeSlot = appDataModel.getTimeSlotById(selectedTimeSlotId);
        if (timeSlot != null) {
            summary.append("Delivery Time: ")
                    .append(timeSlot.getTimeSlot())
                    .append("\n");
        }

        orderSummaryText.setText(summary.toString());
    }

    private List<Long> getExtrasForFood(long foodId) {
        Bundle extrasBundle = getIntent().getBundleExtra("SELECTED_EXTRAS");
        if (extrasBundle != null) {
            long[] extras = extrasBundle.getLongArray(String.valueOf(foodId));
            if (extras != null) {
                List<Long> extrasList = new ArrayList<>();
                for (long extra : extras) {
                    extrasList.add(extra);
                }
                return extrasList;
            }
        }
        return null;
    }

    private void createOrder() {
        try {
            // Create order
            Order order = new Order(cityId, addressId, selectedTimeSlotId, userId);

            // Create order items
            List<OrderItem> orderItems = new ArrayList<>();
            for (long foodId : selectedFoodIds) {
                int quantity = foodQuantities.getOrDefault(foodId, 1);
                orderItems.add(new OrderItem(0, foodId, quantity)); // orderId will be set later
            }

            // Insert order and items
            long orderId = appDataModel.insertOrder(order, orderItems);

            if (orderId != -1) {
                Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show();
                navigateToServices();
            } else {
                Toast.makeText(this, "Failed to create order", Toast.LENGTH_LONG).show();
                confirmButton.setEnabled(true);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error creating order", e);
            Toast.makeText(this, "Failed to create order", Toast.LENGTH_LONG).show();
            confirmButton.setEnabled(true);
        }
    }

    private void navigateToServices() {
        Intent intent = new Intent(this, ServicesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void setupButtons() {
        confirmButton.setOnClickListener(v -> {
            // Disable button to prevent double submission
            confirmButton.setEnabled(false);
            createOrder();
        });

        cancelButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Cancel Order")
                    .setMessage("Are you sure you want to cancel your order?")
                    .setPositiveButton("Yes", (dialog, which) -> navigateToServices())
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}