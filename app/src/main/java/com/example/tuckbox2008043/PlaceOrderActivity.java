package com.example.tuckbox2008043;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tuckbox2008043.DataModel.City;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderActivity extends MainMenuBarBaseActivity {
    private static final String TAG = "PlaceOrderActivity";
    private AppDataModel appDataModel;
    private Spinner regionSpinner;
    private Button nextButton;
    private City selectedCity;
    private long userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        Log.d(TAG, "onCreate started");

        // Get userId from intent
        userId = getIntent().getLongExtra("USER_ID", -1);
        Log.d(TAG, "Received USER_ID: " + userId);

        if (userId == -1) {
            Log.e(TAG, "No user ID available");
            Toast.makeText(this, "Please login to continue", Toast.LENGTH_SHORT).show();
            // Navigate back to services activity
            Intent serviceIntent = new Intent(this, ServicesActivity.class);
            serviceIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(serviceIntent);
            finish();
            return;

        }

        Log.d(TAG, "onCreate started");

        // Initialize views
        initializeViews();

        // Initialize data model
        appDataModel = new AppDataModel(getApplication());

        // Setup spinner
        setupRegionSpinner();

        // Setup button click with more direct implementation
        setupNextButton();

        Log.d(TAG, "onCreate completed");
        //isHome = false;
    }

    private void initializeViews() {
        Log.d(TAG, "Initializing views");

        regionSpinner = findViewById(R.id.regionSpinner);
        nextButton = findViewById(R.id.nextButton);

        if (regionSpinner == null) {
            Log.e(TAG, "regionSpinner is null after findViewById");
        }
        if (nextButton == null) {
            Log.e(TAG, "nextButton is null after findViewById");
        }

        // Set initial button state
        if (nextButton != null) {
            nextButton.setEnabled(false);
            Log.d(TAG, "Next button initialized and disabled");
        }
    }

    private void setupNextButton() {
        Log.d(TAG, "Setting up next button");

        // First remove any existing listeners
        if (nextButton != null) {
            nextButton.setOnClickListener(null);

            // Add new click listener
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Next button clicked");
                    handleNextButtonClick();
                }
            });

            Log.d(TAG, "Next button click listener set");
        } else {
            Log.e(TAG, "Cannot setup next button - button is null");
        }
    }

    private void handleNextButtonClick() {
        Log.d(TAG, "Handling next button click");

        if (selectedCity == null) {
            Log.e(TAG, "Selected city is null");
            Toast.makeText(this, "Please select a city", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Log.d(TAG, "Creating intent for DeliveryAddressActivity with userId: " + userId);
            Intent intent = new Intent(PlaceOrderActivity.this, DeliveryAddressActivity.class);
            intent.putExtra("SELECTED_CITY_ID", selectedCity.getCityId());
            intent.putExtra("USER_ID", userId);
            Log.d(TAG, "Starting DeliveryAddressActivity with city ID: " + selectedCity.getCityId() +
                    " and userId: " + userId);
            startActivity(intent);

            // Add animation transition
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            Log.d(TAG, "DeliveryAddressActivity started successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error launching DeliveryAddressActivity", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupRegionSpinner() {
        Log.d(TAG, "Setting up region spinner");

        // Get cities and verify data
        List<City> cities = appDataModel.getAllCities();
        Log.d(TAG, "Loaded cities: " + cities.size());

        if (cities.isEmpty()) {
            Log.e(TAG, "No cities available");
            Toast.makeText(this, "No cities available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert cities to string list
        List<String> cityNames = new ArrayList<>();
        for (City city : cities) {
            cityNames.add(city.getCityName());
            Log.d(TAG, "Added city: " + city.getCityName() + " (ID: " + city.getCityId() + ")");
        }

        // Create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                cityNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapter
        if (regionSpinner != null) {
            regionSpinner.setAdapter(adapter);
            Log.d(TAG, "Adapter set on spinner");

            // Set selection listener
            regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedCityName = (String) parent.getItemAtPosition(position);
                    Log.d(TAG, "City selected: " + selectedCityName);

                    // Find matching city
                    for (City city : cities) {
                        if (city.getCityName().equals(selectedCityName)) {
                            selectedCity = city;
                            if (nextButton != null) {
                                nextButton.setEnabled(true);
                                Log.d(TAG, "Next button enabled for city: " + selectedCityName);
                            }
                            return;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.d(TAG, "No city selected");
                    selectedCity = null;
                    if (nextButton != null) {
                        nextButton.setEnabled(false);
                    }
                }
            });
        } else {
            Log.e(TAG, "Cannot setup spinner - spinner is null");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        // Refresh the button state
        if (nextButton != null) {
            nextButton.setEnabled(selectedCity != null);
        }
    }
}