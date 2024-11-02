package com.example.tuckbox2008043;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tuckbox2008043.DataModel.DeliveryAddress;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.List;
import java.util.UUID;

public class AddressManagementActivity extends MainMenuBarBaseActivity {
    private AppViewModel viewModel;
    private RecyclerView recyclerView;
    private AddressAdapter addressAdapter;
    private TextInputEditText etNewAddress;
    private TextInputLayout tilNewAddress;
    private long userId;
    private LiveData<List<DeliveryAddress>> addressesLiveData;
    private static final String TAG = "AddressManagement";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_management);
        // Get userId from intent
        userId = getIntent().getLongExtra("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "Error: User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        viewModel = new AppViewModel(getApplication());
        viewModel.syncAddressesForUser(userId);
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);
       // viewModel.debugAddresses(userId);
        initializeViews();
        setupRecyclerView();
        observeAddresses();
        isHome = false;
    }
    private void initializeViews() {
        recyclerView = findViewById(R.id.rvAddresses);
        etNewAddress = findViewById(R.id.etNewAddress);
        tilNewAddress = findViewById(R.id.tilNewAddress);
        FloatingActionButton fabAddAddress = findViewById(R.id.fabAddAddress);

        fabAddAddress.setOnClickListener(v -> addNewAddress());
    }


    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.rvAddresses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addressAdapter = new AddressAdapter(this, new AddressAdapter.AddressClickListener() {
            @Override
            public void onDeleteClick(DeliveryAddress address) {
                deleteAddress(address);
            }

            @Override
            public void onEditClick(DeliveryAddress address) {
                // TODO: Implement edit
            }
        });

        recyclerView.setAdapter(addressAdapter);
        Log.d(TAG, "RecyclerView setup completed");
    }
    private void observeAddresses() {
        Log.d(TAG, "Starting address observation for userId: " + userId);

        viewModel.getAddressesForUser(userId).observe(this, addresses -> {
            Log.d(TAG, "Address list updated. Count: " + (addresses != null ? addresses.size() : 0));
            if (addresses != null) {
                for (DeliveryAddress address : addresses) {
                    Log.d(TAG, "Address: " + address.getAddress() + " for user: " + address.getUserId());
                }
                addressAdapter.setAddresses(addresses);
            } else {
                Log.d(TAG, "Received null address list");
            }
        });
        Log.d(TAG, "Starting observation for user: " + userId);
        viewModel.getAddressesForUser(userId).observe(this, addresses -> {
            Log.d(TAG, "Received " + (addresses != null ? addresses.size() : 0) + " addresses");
        });
    }

    private void addNewAddress() {
        String address = etNewAddress.getText().toString().trim();
        if (address.isEmpty()) {
            tilNewAddress.setError("Address cannot be empty");
            return;
        }

        DeliveryAddress newAddress = new DeliveryAddress(address, userId);

        try {
            long result = viewModel.insertDeliveryAddress(newAddress);
            if (result != -1) {
                etNewAddress.setText("");
                tilNewAddress.setError(null);
                Toast.makeText(this, "Address added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add address", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error adding address", e);
            Toast.makeText(this, "Error adding address: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private void deleteAddress(DeliveryAddress address) {
        if (viewModel.deleteDeliveryAddress(address) > 0) {
            // No need to manually refresh - LiveData will handle it
            Toast.makeText(this, "Address deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete address", Toast.LENGTH_SHORT).show();
        }
    }


}