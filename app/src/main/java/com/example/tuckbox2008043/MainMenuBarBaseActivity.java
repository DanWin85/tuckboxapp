package com.example.tuckbox2008043;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tuckbox2008043.DataModel.User;

public class MainMenuBarBaseActivity extends AppCompatActivity {
    private static final String TAG = "MainMenuBarBaseActivity";
    private AppViewModel viewModel;
    protected static boolean isHome;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_bar, menu);
        viewModel = new AppViewModel(getApplication());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.app_bar_sign_out) {
            signOutUser();
            return true;
        } else if (itemId == R.id.app_bar_home) {
            goToHome();
            return true;
        } else if (itemId == R.id.app_bar_user_info) {
            goToUserInformation();
            return true;
        } else if (itemId == R.id.app_bar_place_order) {
            goToPlaceOrder();
            return true;
        } else if (itemId == R.id.app_bar_current_order) {
            goToCurrentOrder();
            return true;
        } else if (itemId == R.id.app_bar_order_history) {
            goToOrderHistory();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOutUser() {
        SharedPreferences preferences = getSharedPreferences(AppViewModel.USER_PREF_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void goToHome() {
        if (this instanceof ServicesActivity) {
            return; // Already on home screen
        }
        Intent intent = new Intent(this, ServicesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void goToUserInformation() {
        SharedPreferences preferences = getSharedPreferences(AppViewModel.USER_PREF_DATA, MODE_PRIVATE);
        String userEmail = preferences.getString(AppViewModel.USER_PREF_USER_EMAIL, null);

        if (userEmail != null) {
            Intent intent = new Intent(this, UserInformationActivity.class);
            intent.putExtra("USER_EMAIL", userEmail);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please login to continue", Toast.LENGTH_SHORT).show();
            signOutUser();
        }
    }

    private void goToPlaceOrder() {
        SharedPreferences preferences = getSharedPreferences(AppViewModel.USER_PREF_DATA, MODE_PRIVATE);
        String userEmail = preferences.getString(AppViewModel.USER_PREF_USER_EMAIL, null);

        if (userEmail != null) {
            User user = viewModel.getUserByEmail(userEmail);
            if (user != null) {
                Intent intent = new Intent(this, PlaceOrderActivity.class);
                intent.putExtra("USER_ID", user.getUserId());
                startActivity(intent);
            } else {
                Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                signOutUser();
            }
        } else {
            Toast.makeText(this, "Please login to continue", Toast.LENGTH_SHORT).show();
            signOutUser();
        }
    }

    private void goToCurrentOrder() {
        SharedPreferences preferences = getSharedPreferences(AppViewModel.USER_PREF_DATA, MODE_PRIVATE);
        String userEmail = preferences.getString(AppViewModel.USER_PREF_USER_EMAIL, null);

        if (userEmail != null) {
            User user = viewModel.getUserByEmail(userEmail);
            if (user != null) {
                Intent intent = new Intent(this, CurrentOrderActivity.class);
                intent.putExtra("USER_ID", user.getUserId());
                startActivity(intent);
            } else {
                Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                signOutUser();
            }
        } else {
            Toast.makeText(this, "Please login to continue", Toast.LENGTH_SHORT).show();
            signOutUser();
        }
    }

    private void goToOrderHistory() {
        SharedPreferences preferences = getSharedPreferences(AppViewModel.USER_PREF_DATA, MODE_PRIVATE);
        String userEmail = preferences.getString(AppViewModel.USER_PREF_USER_EMAIL, null);

        if (userEmail != null) {
            User user = viewModel.getUserByEmail(userEmail);
            if (user != null) {
                Intent intent = new Intent(this, OrderHistoryActivity.class);
                intent.putExtra("USER_ID", user.getUserId());
                startActivity(intent);
            } else {
                Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                signOutUser();
            }
        } else {
            Toast.makeText(this, "Please login to continue", Toast.LENGTH_SHORT).show();
            signOutUser();
        }
    }
}
