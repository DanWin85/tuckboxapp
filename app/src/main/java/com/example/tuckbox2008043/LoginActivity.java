package com.example.tuckbox2008043;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tuckbox2008043.DataModel.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    public static final String USER_OBJECT_EXTRA = "USER_OBJECT_EXTRA";
    AppViewModel viewModel = null;

    private EditText etEmail, etPassword;
    private TextInputLayout tilEmail, tilPassword;
    private MaterialButton btnLogin;
    private TextView  tvRegister;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = new AppViewModel(getApplication());
        initViews();
        setupListeners();
        checkUserLoginPreference();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in...");
        progressDialog.setCancelable(false);
    }
   private void checkUserLoginPreference() {
    SharedPreferences preferences = getSharedPreferences(AppViewModel.USER_PREF_DATA, MODE_PRIVATE);
    String email = preferences.getString(AppViewModel.USER_PREF_USER_EMAIL, null);
    String password = preferences.getString(AppViewModel.USER_PREF_USER_PASSWORD, null);
    if (email != null && password != null) {
        performLogin(email, password);
    }
}
    private void setupListeners() {
        btnLogin.setOnClickListener(v -> validateAndLogin());

        tvRegister.setOnClickListener(v -> {
            // Navigate to registration screen
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void validateAndLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        tilEmail.setError(null);
        tilPassword.setError(null);

        if (email.isEmpty()) {
            tilEmail.setError("Email is required");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Please enter a valid email");
            return;
        }

        if (password.isEmpty()) {
            tilPassword.setError("Password is required");
            return;
        }
        if (password.length() < 8) {
            tilPassword.setError("Password must be at least 8 characters");
            return;
        }

        performLogin(email, password);
    }

    private void performLogin(String email, String password) {
        progressDialog.show();
        User user = viewModel.getUserByEmail(email);

        // Simulate a delay for the "network call"
        new Handler().postDelayed(() -> {
            progressDialog.dismiss();
            if (user != null) {
                if (password.equals(user.getPassword())) {
                    SharedPreferences preferences = getSharedPreferences(AppViewModel.USER_PREF_DATA, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(AppViewModel.USER_PREF_USER_EMAIL, email);
                    editor.putString(AppViewModel.USER_PREF_USER_PASSWORD, password);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, ServicesActivity.class);
                    intent.putExtra(USER_OBJECT_EXTRA, user);
                    startActivity(intent);
                    finish();
                } else {
                    showError("Invalid credentials");
                }
            } else {
                showError("User not found");
            }
        }, 2000);
    }

    private void showError(String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}