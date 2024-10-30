package com.example.tuckbox2008043;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tuckbox2008043.DataModel.DeliveryAddress;
import com.example.tuckbox2008043.DataModel.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.button.MaterialButton;

import java.util.UUID;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etFirstName, etLastName, etAddress, etEmail, etMobile, etPassword;
    private TextInputLayout tilFirstName, tilLastName, tilAddress, tilEmail, tilMobile, tilPassword;
    AppViewModel viewModel = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAddress = findViewById(R.id.etAddress);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);

        tilFirstName = findViewById(R.id.tilFirstName);
        tilLastName = findViewById(R.id.tilLastName);
        tilAddress = findViewById(R.id.tilAddress);
        tilEmail = findViewById(R.id.tilEmail);
        tilMobile = findViewById(R.id.tilMobile);
        tilPassword = findViewById(R.id.tilPassword);

        MaterialButton btnRegister = findViewById(R.id.btnRegister);
        @SuppressLint("MissingInflatedId") TextView tvCancel = findViewById(R.id.tvCancelRegister);
        viewModel = new AppViewModel(getApplication());

        // Set register button click listener
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    registerUser();
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean validateInput() {
        // Reset errors
        tilFirstName.setError(null);
        tilLastName.setError(null);
        tilAddress.setError(null);
        tilEmail.setError(null);
        tilMobile.setError(null);
        tilPassword.setError(null);

        // Validate first name
        if (TextUtils.isEmpty(etFirstName.getText())) {
            tilFirstName.setError(getString(R.string.error_first_name_required));
            return false;
        }

        // Validate last name
        if (TextUtils.isEmpty(etLastName.getText())) {
            tilLastName.setError(getString(R.string.error_last_name_required));
            return false;
        }
        if(TextUtils.isEmpty(etAddress.getText())){
            tilAddress.setError("Address is required");
            return false;
        }

        // Validate email
        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(getString(R.string.error_email_required));
            return false;
        } else if (!isValidEmail(email)) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            return false;
        }

        // Validate mobile number
        String mobile = etMobile.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            tilMobile.setError(getString(R.string.error_mobile_required));
            return false;
        } else if (!isValidMobile(mobile)) {
            tilMobile.setError(getString(R.string.error_invalid_mobile));
            return false;
        }

        // Validate password
        if (TextUtils.isEmpty(etPassword.getText())) {
            tilPassword.setError(getString(R.string.error_password_required));
            return false;
        }


        return true;
    }

    private void registerUser() {
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String address = etAddress.getText().toString();
        String email = etEmail.getText().toString();
        String mobile = etMobile.getText().toString();
        String password = etPassword.getText().toString();

        long userId = viewModel.getNextUserID();

        // Create and insert user
        User user = new User(userId, email, password, firstName, lastName, mobile);
        if(viewModel.insertUser(user) != -1) {
            // Generate a unique address ID
            String addressId = UUID.randomUUID().toString();

            // Create and insert delivery address
            DeliveryAddress deliveryAddress = new DeliveryAddress(addressId, address, userId);
            if(viewModel.insertDeliveryAddress(deliveryAddress) != -1) {
                Toast.makeText(this, "Registration successful for " + firstName + "" + lastName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save address", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
    private boolean isValidEmail(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean isValidMobile(String mobile){
        return mobile.matches("^(0|(\\+64(\\s|-)?)){1}(21|22|27){1}(\\s|-)?\\d{3}(\\s|-)?\\d{4}(\\d{1})?");
    }
}