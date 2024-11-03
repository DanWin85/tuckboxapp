package com.example.tuckbox2008043;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OrderConfirmationActivity extends AppCompatActivity {
    private AppDataModel appDataModel;
    private TextView orderSummaryText;
    private Button confirmButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        appDataModel = new AppDataModel(getApplication());

        //initializeViews();
        displayOrderSummary();
        setupButtons();
    }

    private void displayOrderSummary() {
        // Get all order details from previous activities via Intent
        StringBuilder summary = new StringBuilder();
        // Add delivery address
        // Add selected meals and extras
        // Add time slot
        orderSummaryText.setText(summary.toString());
    }

    private void setupButtons() {
        confirmButton.setOnClickListener(v -> {
            // Save order to database
            // Show success message
            finish(); // Return to main screen
        });

        cancelButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Cancel Order")
                    .setMessage("Are you sure you want to cancel your order?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}