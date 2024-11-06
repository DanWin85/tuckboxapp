package com.example.tuckbox2008043;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tuckbox2008043.DataModel.TimeSlot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeSlotActivity extends MainMenuBarBaseActivity {
    private static final String TAG = "TimeSlotActivity";
    private AppDataModel appDataModel;
    private RadioGroup timeSlotGroup;
    private Button nextButton;
    private TimeSlot selectedTimeSlot;
    private long userId;
    private long cityId;
    private long addressId;
    private long[] selectedFoodIds;
    private Map<Long, long[]> selectedExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot);

        // Initialize AppDataModel
        appDataModel = new AppDataModel(getApplication());

        // Get the intent data
        Intent intent = getIntent();
        userId = intent.getLongExtra("USER_ID", -1);
        cityId = intent.getLongExtra("SELECTED_CITY_ID", -1);
        addressId = intent.getLongExtra("SELECTED_ADDRESS_ID", -1);
        selectedFoodIds = intent.getLongArrayExtra("SELECTED_FOOD_IDS");

        // Initialize views first
        initializeViews();

        // Reconstruct the extras map
        selectedExtras = new HashMap<>();
        Bundle extrasBundle = intent.getBundleExtra("SELECTED_EXTRAS");
        if (extrasBundle != null) {
            for (String key : extrasBundle.keySet()) {
                long foodId = Long.parseLong(key);
                long[] extraIds = extrasBundle.getLongArray(key);
                if (extraIds != null) {
                    selectedExtras.put(foodId, extraIds);
                }
            }
        }

        // Log received data
        Log.d(TAG, "Received data - " +
                "UserId: " + userId +
                ", CityId: " + cityId +
                ", AddressId: " + addressId +
                ", Selected Foods: " + (selectedFoodIds != null ? selectedFoodIds.length : 0) +
                ", Selected Extras: " + selectedExtras.size());

        // Validate received data
        if (userId == -1 || cityId == -1 || addressId == -1 || selectedFoodIds == null) {
            Log.e(TAG, "Missing required data");
            Toast.makeText(this, "Error: Missing required data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Setup the views
        setupTimeSlots();
        setupNextButton();
        isHome = false;
    }

    private void initializeViews() {
        timeSlotGroup = findViewById(R.id.timeSlotGroup);
        nextButton = findViewById(R.id.nextButton);

        // Initially disable the next button
        if (nextButton != null) {
            nextButton.setEnabled(false);
        }
    }

    private void setupTimeSlots() {
        if (timeSlotGroup == null) {
            Log.e(TAG, "timeSlotGroup is null");
            return;
        }

        List<TimeSlot> timeSlots = appDataModel.getAllTimeSlots();
        Log.d(TAG, "Setting up " + timeSlots.size() + " time slots");

        for (TimeSlot slot : timeSlots) {
            RadioButton button = new RadioButton(this);
            button.setText(slot.getTimeSlot());
            button.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedTimeSlot = slot;
                    if (nextButton != null) {
                        nextButton.setEnabled(true);
                    }
                }
            });
            timeSlotGroup.addView(button);
        }
    }

    private void setupNextButton() {
        nextButton.setOnClickListener(v -> {
            if (selectedTimeSlot != null) {
                Intent currentIntent = getIntent();
                Intent intent = new Intent(this, OrderConfirmationActivity.class);

                // Pass through all the data
                intent.putExtra("USER_ID", currentIntent.getLongExtra("USER_ID", -1));
                intent.putExtra("SELECTED_CITY_ID", currentIntent.getLongExtra("SELECTED_CITY_ID", -1));
                intent.putExtra("SELECTED_ADDRESS_ID", currentIntent.getLongExtra("SELECTED_ADDRESS_ID", -1));
                intent.putExtra("SELECTED_FOOD_IDS", currentIntent.getLongArrayExtra("SELECTED_FOOD_IDS"));
                intent.putExtra("FOOD_QUANTITIES", currentIntent.getBundleExtra("FOOD_QUANTITIES"));
                intent.putExtra("SELECTED_EXTRAS", currentIntent.getBundleExtra("SELECTED_EXTRAS"));
                intent.putExtra("SELECTED_TIME_SLOT_ID", selectedTimeSlot.getTimeSlotId());

                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select a time slot", Toast.LENGTH_SHORT).show();
            }
        });
    }
}