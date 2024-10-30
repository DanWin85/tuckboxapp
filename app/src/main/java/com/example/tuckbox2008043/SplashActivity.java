package com.example.tuckbox2008043;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Apply fade-in animation to the logo
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(2000);
        findViewById(R.id.logo).startAnimation(fadeIn);

        // Delay to transition to MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish(); // Finish SplashActivity so user can't go back to it
            }
        }, SPLASH_DURATION);
    }
}