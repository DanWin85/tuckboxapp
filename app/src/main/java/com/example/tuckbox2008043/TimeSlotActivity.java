package com.example.tuckbox2008043;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tuckbox2008043.DataModel.TimeSlot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeSlotActivity extends AppCompatActivity {
    private AppDataModel appDataModel;
    private RadioGroup timeSlotGroup;
    private Button nextButton;
    private TimeSlot selectedTimeSlot;
    private static final String TAG = "TimeSlotActivity";
    private long userId;
    private long cityId;
    private long addressId;
    private long[] selectedFoodIds;
    private Map<Long, long[]> selectedExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot);

        appDataModel = new AppDataModel(getApplication());
        Intent intent = getIntent();
        userId = intent.getLongExtra("USER_ID", -1);
        cityId = intent.getLongExtra("SELECTED_CITY_ID", -1);
        addressId = intent.getLongExtra("SELECTED_ADDRESS_ID", -1);
        selectedFoodIds = intent.getLongArrayExtra("SELECTED_FOOD_IDS");

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

            //initializeViews();
            setupTimeSlots();
            setupNextButton();
        }
    }

    private void setupTimeSlots() {
        List<TimeSlot> timeSlots = appDataModel.getAllTimeSlots();
        for (TimeSlot slot : timeSlots) {
            RadioButton button = new RadioButton(this);
            button.setText(slot.getTimeSlot());
            button.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedTimeSlot = slot;
                }
            });
            timeSlotGroup.addView(button);
        }
    }

    private void setupNextButton() {
        nextButton.setOnClickListener(v -> {
            if (selectedTimeSlot != null) {
                Intent intent = new Intent(this, OrderConfirmationActivity.class);
                startActivity(intent);
            }
        });
    }
}