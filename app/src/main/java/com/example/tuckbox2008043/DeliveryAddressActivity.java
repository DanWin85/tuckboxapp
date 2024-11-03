package com.example.tuckbox2008043;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuckbox2008043.DataModel.DeliveryAddress;

import java.util.List;

public class DeliveryAddressActivity extends MainMenuBarBaseActivity implements AddressAdapter.AddressClickListener {
    private AppDataModel appDataModel;
    private AppViewModel appViewModel;
    private RecyclerView addressRecyclerView;
    private AddressAdapter addressAdapter;
    private Button nextButton;
    private long selectedAddressId = -1;
    private long userId;
    private LiveData<List<DeliveryAddress>> addressesLiveData;
    private static final String TAG = "DeliveryAddressActivity";
    private long selectedCityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
        userId = getIntent().getLongExtra("USER_ID", -1);
        if (userId == -1) {
            Log.e(TAG, "User not logged in");
            Toast.makeText(this, "Please login to continue", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "User ID: " + userId);

        // Get the selected city ID
        selectedCityId = getIntent().getLongExtra("SELECTED_CITY_ID", -1);
        Log.d(TAG, "Received city ID: " + selectedCityId);

        if (selectedCityId == -1) {
            Log.e(TAG, "No city ID received");
            Toast.makeText(this, "Error: No city selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        // Initialize ViewModels and DataModels
        initializeModels();

        // Initialize views
        initializeViews();

        // Setup RecyclerView
        setupRecyclerView();

        // Start observing addresses
        observeAddresses();

        // Setup button listeners
        setupButtons();

        Log.d(TAG, "onCreate completed");
        isHome = false;
    }

    private void initializeModels() {
        Log.d(TAG, "Initializing models");
        appViewModel = new AppViewModel(getApplication());
        appDataModel = new AppDataModel(getApplication());
        appViewModel.syncAddressesForUser(userId);
    }

    private void initializeViews() {
        Log.d(TAG, "Initializing views");
        addressRecyclerView = findViewById(R.id.rvAddresses);

        nextButton = findViewById(R.id.nextButton);

        if (addressRecyclerView == null) Log.e(TAG, "addressRecyclerView is null");
        if (nextButton == null) Log.e(TAG, "nextButton is null");
    }

    private void observeAddresses() {
        Log.d(TAG, "Starting address observation for userId: " + userId);
        appViewModel.getAddressesForUser(userId).observe(this, addresses -> {
            Log.d(TAG, "Address list updated. Count: " + (addresses != null ? addresses.size() : 0));
            if (addresses != null && addressAdapter != null) {
                addressAdapter.setAddresses(addresses);
            }
        });
    }

    private void setupRecyclerView() {
        Log.d(TAG, "Setting up RecyclerView");
        if (addressRecyclerView != null) {
            addressRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            addressAdapter = new AddressAdapter(this, this);
            addressRecyclerView.setAdapter(addressAdapter);
            Log.d(TAG, "RecyclerView setup completed");
        } else {
            Log.e(TAG, "Cannot setup RecyclerView - view is null");
        }
    }

    @Override
    public void onDeleteClick(DeliveryAddress address) {
        if (address != null && appDataModel != null) {
            appDataModel.deleteAddress(address);
        }
    }

    @Override
    public void onEditClick(DeliveryAddress address) {
        if (address != null) {
            Intent editIntent = new Intent(this, AddressManagementActivity.class);
            editIntent.putExtra("ADDRESS_ID", address.getAddressId());
            startActivity(editIntent);
        }
    }

    @Override
    public void onAddressSelected(DeliveryAddress address) {
        if (address != null) {
            selectedAddressId = address.getAddressId();
            if (nextButton != null) {
                nextButton.setEnabled(true);
            }
        }
    }

    private void setupButtons() {
        Log.d(TAG, "Setting up buttons");

        if (nextButton != null) {
            nextButton.setEnabled(false);
            nextButton.setOnClickListener(v -> {
                if (selectedAddressId != -1) {
                    Intent intent = new Intent(this, MealSelectionActivity.class);
                    intent.putExtra("SELECTED_ADDRESS_ID", selectedAddressId);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Please select an address", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}