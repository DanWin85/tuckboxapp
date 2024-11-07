package com.example.tuckbox2008043;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuckbox2008043.AppDataModel;
import com.example.tuckbox2008043.DataModel.Food;
import com.example.tuckbox2008043.DataModel.FoodExtraDetails;
import com.example.tuckbox2008043.MealAdapter;
import com.example.tuckbox2008043.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MealSelectionActivity extends MainMenuBarBaseActivity implements MealAdapter.MealSelectionListener {
    private static final String TAG = "MealSelectionActivity";
    private AppDataModel appDataModel;
    private RecyclerView mealsRecyclerView;
    private MaterialButton nextButton;
    private MealAdapter adapter;
    private long userId;
    private long cityId;
    private long addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_selection);

        // Get data from intent
        Intent intent = getIntent();
        userId = intent.getLongExtra("USER_ID", -1);
        cityId = intent.getLongExtra("SELECTED_CITY_ID", -1);
        addressId = intent.getLongExtra("SELECTED_ADDRESS_ID", -1);

        Log.d(TAG, "Received values - UserId: " + userId +
                ", CityId: " + cityId +
                ", AddressId: " + addressId);

        // Validate received data
        if (userId == -1 || cityId == -1 || addressId == -1) {
            Log.e(TAG, "Missing required data");
            Toast.makeText(this, "Error: Missing required data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        appDataModel = new AppDataModel(getApplication());
        initializeViews();
        setupMealsList();
        setupNextButton();
       // isHome = false;
    }
    @Override
    public void onQuantityChanged(Food food, int quantity) {
        // This method is called when the quantity of a selected food changes
        Log.d(TAG, "Quantity changed for " + food.getFoodName() + " to " + quantity);
    }

    private void setupMealsList() {
        List<Food> foods = appDataModel.getAllFoods();
        List<FoodExtraDetails> extras = appDataModel.getAllFoodExtras();

        adapter = new MealAdapter(foods, extras, this);
        mealsRecyclerView.setAdapter(adapter);
        mealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializeViews() {
        mealsRecyclerView = findViewById(R.id.mealsRecyclerView);
        nextButton = findViewById(R.id.nextButton);
        nextButton.setEnabled(false); // Initially disable the next button
    }

    @Override
    public void onMealSelected(Food item) {
        // Enable next button when at least one meal is selected
        nextButton.setEnabled(true);
    }

    private void setupNextButton() {
        nextButton.setOnClickListener(v -> {
            List<Food> selectedFoods = adapter.getSelectedFoods();
            Map<Long, List<Long>> selectedExtras = adapter.getSelectedExtras();
            Map<Long, Integer> selectedQuantities = adapter.getSelectedFoodQuantities();  // Get quantities

            if (!selectedFoods.isEmpty()) {
                Intent intent = new Intent(this, TimeSlotActivity.class);

                // Pass all necessary data
                intent.putExtra("USER_ID", userId);
                intent.putExtra("SELECTED_CITY_ID", cityId);
                intent.putExtra("SELECTED_ADDRESS_ID", addressId);

                // Convert selected foods to array of IDs
                long[] selectedFoodIds = selectedFoods.stream()
                        .mapToLong(Food::getFoodId)
                        .toArray();
                intent.putExtra("SELECTED_FOOD_IDS", selectedFoodIds);

                // Create bundle for quantities
                Bundle quantityBundle = new Bundle();
                for (Map.Entry<Long, Integer> entry : selectedQuantities.entrySet()) {
                    quantityBundle.putInt(String.valueOf(entry.getKey()), entry.getValue());
                }
                intent.putExtra("FOOD_QUANTITIES", quantityBundle);

                // Pass the extras
                Bundle extrasBundle = new Bundle();
                for (Map.Entry<Long, List<Long>> entry : selectedExtras.entrySet()) {
                    long[] extraIds = entry.getValue().stream()
                            .mapToLong(Long::longValue)
                            .toArray();
                    extrasBundle.putLongArray(String.valueOf(entry.getKey()), extraIds);
                }
                intent.putExtra("SELECTED_EXTRAS", extrasBundle);

                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select at least one meal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}