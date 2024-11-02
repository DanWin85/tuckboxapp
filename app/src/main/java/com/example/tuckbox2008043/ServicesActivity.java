package com.example.tuckbox2008043;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.tuckbox2008043.DataModel.User;
import com.google.android.material.button.MaterialButton;

public class ServicesActivity extends MainMenuBarBaseActivity {
    AppViewModel viewModel = null;
    private MaterialButton btnPlaceOrder, btnUpdateUserDetails, btnCurrentOrder, btnOrderHistory;
    private String userEmail; // Add class-level variable to store userEmail

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_services);
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);
        TextView welcomeTextView = findViewById(R.id.userMainWelcomeTextView);

        SharedPreferences preferences = getSharedPreferences(AppViewModel.USER_PREF_DATA, MODE_PRIVATE);
        userEmail = preferences.getString(AppViewModel.USER_PREF_USER_EMAIL, null); // Store userEmail in class variable

        if (userEmail != null) {
            User user = viewModel.getUserByEmail(userEmail);
            if (user != null) {
                welcomeTextView.setText("Welcome Back, " + user.getFirstName());
            }
        } else {
            Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show();
            finish();
        }
        isHome = true;
        initViews();
        setupListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isHome = true;
    }

    private void initViews() {
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnUpdateUserDetails = findViewById(R.id.btnUpdateUserDetails);
        btnCurrentOrder = findViewById(R.id.btnCurrentOrder);
        btnOrderHistory = findViewById(R.id.btnOrderHistory);
    }

    private void setupListeners() {
        btnPlaceOrder.setOnClickListener(view -> {
            startActivity(new Intent(ServicesActivity.this, PlaceOrderActivity.class));
        });

        btnUpdateUserDetails.setOnClickListener(view -> {
            if (userEmail != null) { // Add null check for safety
                Intent intent = new Intent(this, UserInformationActivity.class);
                intent.putExtra("USER_EMAIL", userEmail);
                startActivity(intent);
            } else {
                Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}