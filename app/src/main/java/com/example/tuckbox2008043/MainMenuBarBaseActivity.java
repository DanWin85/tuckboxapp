package com.example.tuckbox2008043;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tuckbox2008043.DataModel.User;

public class MainMenuBarBaseActivity extends AppCompatActivity {

    static User user = null;
    static boolean isHome;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu_bar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == R.id.app_bar_sign_out){
            signOutUser();
        } else if (item.getItemId() == R.id.app_bar_home) {
            goHome();
        } else if (item.getItemId() == R.id.app_bar_user_info) {
            userInformationEditing();
        } else if (item.getItemId() == R.id.app_bar_place_order) {
            placeNewOrder();
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOutUser(){
        SharedPreferences preferences = getSharedPreferences(AppViewModel.USER_PREF_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(AppViewModel.USER_PREF_USER_EMAIL);
        editor.remove(AppViewModel.USER_PREF_USER_PASSWORD);
        editor.apply();
        Intent intent = new Intent(this, LoginActivity.class);
        finishAffinity();
        startActivity(intent);
    }

    private void goHome(){
        if(!isHome)
            finish();
        isHome = true;
    }
    void userInformationEditing(){
        if(!isHome)
            finish();
        isHome = false;
        Intent intent = new Intent(this, UserInformationActivity.class);
        startActivity(intent);
    }
    void placeNewOrder(){
        if(!isHome)
            finish();
        isHome = false;
        Intent intent = new Intent(this, PlaceOrderActivity.class);
        startActivity(intent);
    }
}
