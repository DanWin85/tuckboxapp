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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MealSelectionActivity extends MainMenuBarBaseActivity implements MealAdapter.MealSelectionListener {
    private static final String TAG = "MealSelectionActivity";
    private AppDataModel appDataModel;
    private RecyclerView mealsRecyclerView;
    private Button nextButton;
    private MealAdapter adapter;
    private List<Food> selectedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_selection);

        // Initialize AppDataModel
        appDataModel = new AppDataModel(getApplication());

        initializeViews();
        setupMealsList();
        setupNextButton();
        isHome = false;
    }

    private void setupMealsList() {
        List<Food> foods = appDataModel.getAllFoods();
        List<FoodExtraDetails> extras = appDataModel.getAllFoodExtras();

        Log.d(TAG, "Foods loaded: " + foods.size());
        Log.d(TAG, "Extras loaded: " + extras.size());

        adapter = new MealAdapter(foods, extras, this);
        mealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mealsRecyclerView.setAdapter(adapter);
    }

    private void initializeViews() {
        mealsRecyclerView = findViewById(R.id.mealsRecyclerView);
        nextButton = findViewById(R.id.nextButton);

        // Initially disable the next button
        nextButton.setEnabled(false);
    }

    @Override
    public void onMealSelected(Food item) {
        if (!selectedItems.contains(item)) {
            selectedItems.add(item);
        }
        nextButton.setEnabled(!selectedItems.isEmpty());
    }

    private void setupNextButton() {
        nextButton.setOnClickListener(v -> {
            if (!selectedItems.isEmpty()) {
                // Get the selected meals and extras from adapter
                List<Food> selectedFoods = adapter.getSelectedFoods();
                Map<Long, List<Long>> selectedExtras = adapter.getSelectedExtras();

                // Get the values from previous screens from intent
                Intent currentIntent = getIntent();
                long userId = currentIntent.getLongExtra("USER_ID", -1);
                long cityId = currentIntent.getLongExtra("SELECTED_CITY_ID", -1);
                long addressId = currentIntent.getLongExtra("SELECTED_ADDRESS_ID", -1);

                // Create intent for next activity
                Intent intent = new Intent(this, TimeSlotActivity.class);

                // Pass all values to next activity
                intent.putExtra("USER_ID", userId);
                intent.putExtra("SELECTED_CITY_ID", cityId);
                intent.putExtra("SELECTED_ADDRESS_ID", addressId);

                // Convert selected foods to array of IDs
                long[] selectedFoodIds = selectedFoods.stream()
                        .mapToLong(Food::getFoodId)
                        .toArray();
                intent.putExtra("SELECTED_FOOD_IDS", selectedFoodIds);

                // Convert selected extras map to a format that can be passed in intent
                Bundle extrasBundle = new Bundle();
                for (Map.Entry<Long, List<Long>> entry : selectedExtras.entrySet()) {
                    long[] extraIds = entry.getValue().stream()
                            .mapToLong(Long::longValue)
                            .toArray();
                    extrasBundle.putLongArray(String.valueOf(entry.getKey()), extraIds);
                }
                intent.putExtra("SELECTED_EXTRAS", extrasBundle);

                // Log the data being passed
                Log.d(TAG, "Passing to TimeSlotActivity - " +
                        "UserId: " + userId +
                        ", CityId: " + cityId +
                        ", AddressId: " + addressId +
                        ", Selected Foods: " + selectedFoods.size() +
                        ", Selected Extras: " + selectedExtras.size());

                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select at least one meal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}