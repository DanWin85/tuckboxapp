package com.example.tuckbox2008043;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class UserInformationActivity extends MainMenuBarBaseActivity {

    private TextInputEditText etFirstName, etLastName,  etEmail, etMobile, etPassword;
    private TextInputLayout tilFirstName, tilLastName,  tilEmail, tilMobile, tilPassword;
    private TextView  tvCancel, tvAddNewDeliveryAddress;
    AppViewModel viewModel = null;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_information);
        viewModel = new AppViewModel(getApplication());
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        tilFirstName = findViewById(R.id.tilFirstName);
        tilLastName = findViewById(R.id.tilLastName);
        tilEmail = findViewById(R.id.tilEmail);
        tilMobile = findViewById(R.id.tilMobile);
        tilPassword = findViewById(R.id.tilPassword);
        tvCancel = findViewById(R.id.tvCancel);
        tvAddNewDeliveryAddress = findViewById(R.id.tvAddNewDeliveryAddress);
        MaterialButton btnUpdate = findViewById(R.id.btnUpdate);

        setupListeners();

    }
    private void setupListeners(){
        tvCancel.setOnClickListener(view -> {
            finish();
        });
        tvAddNewDeliveryAddress.setOnClickListener(view -> {
            // In the previous activity where you start AddressManagementActivity
            Intent intent = new Intent(this, AddressManagementActivity.class);
            intent.putExtra("USER_ID", userId); // Make sure userId is a long value here
            startActivity(intent);
        });
    }
}