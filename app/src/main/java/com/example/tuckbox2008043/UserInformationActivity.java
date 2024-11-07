package com.example.tuckbox2008043;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;

import com.example.tuckbox2008043.DataModel.DeliveryAddress;
import com.example.tuckbox2008043.DataModel.Order;
import com.example.tuckbox2008043.DataModel.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.regex.Pattern;

public class UserInformationActivity extends MainMenuBarBaseActivity {

    private TextInputEditText etFirstName, etLastName, etEmail, etMobile, etPassword;
    private TextInputLayout tilFirstName, tilLastName, tilEmail, tilMobile, tilPassword;
    private TextView tvCancel, tvAddNewDeliveryAddress, tvDeleteAccount;
    AppViewModel viewModel = null;
    private long userId;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_information);
        viewModel = new AppViewModel(getApplication());
//        isHome = false;
        initializeViews();
        setupListeners();
        loadUserData();
    }

    private void initializeViews() {
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
        tvDeleteAccount = findViewById(R.id.tvDeleteAccount);
        MaterialButton btnUpdate = findViewById(R.id.btnUpdate);


    }

    private void setupListeners() {
        tvCancel.setOnClickListener(view -> finish());

        tvAddNewDeliveryAddress.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddressManagementActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        findViewById(R.id.btnUpdate).setOnClickListener(view -> {
            if (validateInput()) {
                updateUserInformation();
            }
        });
        tvDeleteAccount.setOnClickListener(view -> {
            deleteUserAccount();
        });
    }

    private void deleteUserAccount() {
        if (currentUser != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        ProgressDialog progressDialog = new ProgressDialog(this);
                        progressDialog.setMessage("Deleting account data...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        viewModel.deleteUser(currentUser, success -> {
                            progressDialog.dismiss();
                            if (success) {
                                SharedPreferences preferences = getSharedPreferences(
                                        AppViewModel.USER_PREF_DATA, MODE_PRIVATE);
                                preferences.edit().clear().apply();

                                Toast.makeText(this, "Account deleted successfully",
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(this, "Failed to delete account. Check logs for details.",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    private void loadUserData() {
        // Get current user email from shared preferences or intent
        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        if (userEmail != null) {
            currentUser = viewModel.getUserByEmail(userEmail);
            if (currentUser != null) {
                userId = currentUser.getUserId();
                populateFields(currentUser);
            }
        }
    }

    private void populateFields(User user) {
        etFirstName.setText(user.getFirstName());
        etLastName.setText(user.getLastName());
        etEmail.setText(user.getUserEmail());
        etMobile.setText(user.getMobile());
        etPassword.setText(user.getPassword());
    }

    private boolean validateInput() {
        // Reset errors
        tilFirstName.setError(null);
        tilLastName.setError(null);
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

    private void updateUserInformation() {
        if (currentUser != null) {
            User updatedUser = new User(
                    currentUser.getUserId(),
                    etEmail.getText().toString(),
                    etPassword.getText().toString(),
                    etFirstName.getText().toString(),
                    etLastName.getText().toString(),
                    etMobile.getText().toString()
            );

            int result = viewModel.updateUser(updatedUser);
            if (result > 0) {
                Toast.makeText(this, "User information updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update user information", Toast.LENGTH_SHORT).show();
            }
        }
    }





    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private boolean isValidMobile(String mobile) {
        return mobile.matches("^(0|(\\+64(\\s|-)?)){1}(21|22|27){1}(\\s|-)?\\d{3}(\\s|-)?\\d{4}(\\d{1})?");
    }


}