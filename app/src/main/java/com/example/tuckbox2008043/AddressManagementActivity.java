package com.example.tuckbox2008043;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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
            //isHome = false;
        }



        viewModel = new AppViewModel(getApplication());

        // Initialize views
        recyclerView = findViewById(R.id.rvAddresses);
        etNewAddress = findViewById(R.id.etNewAddress);
        tilNewAddress = findViewById(R.id.tilNewAddress);
        FloatingActionButton fabAddAddress = findViewById(R.id.fabAddAddress);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addressAdapter = new AddressAdapter(this, new AddressAdapter.AddressClickListener() {
            @Override
            public void onDeleteClick(DeliveryAddress address) {
                deleteAddress(address);
            }

            @Override
            public void onEditClick(DeliveryAddress address) {
                // Implement edit functionality if needed
            }
        });
        recyclerView.setAdapter(addressAdapter);

        // Load addresses
        loadAddresses();

        // Setup add button
        fabAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAddress();
            }
        });
    }

    private void loadAddresses() {
        // Convert long userId to String
        List<DeliveryAddress> addresses = viewModel.getAddressesForUser(String.valueOf(userId));
        addressAdapter.setAddresses(addresses);
    }

    private void addNewAddress() {
        String address = etNewAddress.getText().toString().trim();
        if (address.isEmpty()) {
            tilNewAddress.setError("Address cannot be empty");
            return;
        }

        DeliveryAddress newAddress = new DeliveryAddress(
                UUID.randomUUID().toString(),  // Assuming you still want a UUID string for the address ID
                address,
                (userId)  // Convert long to String here as well
        );

        if (viewModel.insertDeliveryAddress(newAddress) != -1) {
            etNewAddress.setText("");
            tilNewAddress.setError(null);
            loadAddresses(); // Refresh the list
            Toast.makeText(this, "Address added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add address", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteAddress(DeliveryAddress address) {
        if (viewModel.deleteDeliveryAddress(address) > 0) {
            loadAddresses(); // Refresh the list
            Toast.makeText(this, "Address deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete address", Toast.LENGTH_SHORT).show();
        }
    }
}