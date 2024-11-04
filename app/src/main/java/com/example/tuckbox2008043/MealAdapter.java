package com.example.tuckbox2008043;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuckbox2008043.DataModel.Food;
import com.example.tuckbox2008043.DataModel.FoodExtraDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private List<Food> foods;
    private List<FoodExtraDetails> extras;
    private final MealSelectionListener listener;
    private Map<Long, List<FoodExtraDetails>> foodExtrasMap;
    private Map<Long, Boolean> selectedFoods;
    private Map<Long, List<Long>> selectedExtras;
    private Map<Long, Integer> foodQuantities;

    public interface MealSelectionListener {
        void onMealSelected(Food food);
        void onQuantityChanged(Food food, int quantity);
    }

    public MealAdapter(List<Food> foods, List<FoodExtraDetails> extras, MealSelectionListener listener) {
        this.foods = foods;
        this.extras = extras;
        this.listener = listener;
        this.selectedFoods = new HashMap<>();
        this.selectedExtras = new HashMap<>();
        this.foodQuantities = new HashMap<>(); // Initialize the new map

        // Group extras by food ID for easier access
        this.foodExtrasMap = new HashMap<>();
        for (FoodExtraDetails extra : extras) {
            foodExtrasMap.computeIfAbsent(extra.getFoodId(), k -> new ArrayList<>()).add(extra);
        }
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_layout, parent, false);
        return new MealViewHolder(view);
    }
    public Map<Long, Integer> getSelectedFoodQuantities() {
        Map<Long, Integer> result = new HashMap<>();
        for (Map.Entry<Long, Boolean> entry : selectedFoods.entrySet()) {
            if (entry.getValue()) {
                result.put(entry.getKey(), foodQuantities.getOrDefault(entry.getKey(), 1));
            }
        }
        return result;
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Food food = foods.get(position);

        // Set main food item details
        holder.mealName.setText(food.getFoodName());

        // Set checkbox state
        boolean isSelected = selectedFoods.getOrDefault(food.getFoodId(), false);
        holder.mealCheckBox.setChecked(isSelected);

        // Set quantity visibility and value
        holder.quantityLayout.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        holder.quantity = foodQuantities.getOrDefault(food.getFoodId(), 1);
        holder.quantityText.setText(String.valueOf(holder.quantity));

        // Handle extras container visibility based on food's extraChoice flag
        if (food.isFoodExtraChoice()) {
            holder.extrasContainer.setVisibility(View.VISIBLE);
            setupExtrasForFood(holder, food);
        } else {
            holder.extrasContainer.setVisibility(View.GONE);
        }

        // Handle meal selection
        holder.mealCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedFoods.put(food.getFoodId(), isChecked);
            holder.quantityLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);

            if (isChecked) {
                foodQuantities.put(food.getFoodId(), holder.quantity);
                listener.onMealSelected(food);
            } else {
                foodQuantities.remove(food.getFoodId());
                selectedExtras.remove(food.getFoodId());
                holder.quantity = 1;
                holder.quantityText.setText("1");
                // Uncheck all extra checkboxes
                for (int i = 0; i < holder.extrasContainer.getChildCount(); i++) {
                    View child = holder.extrasContainer.getChildAt(i);
                    if (child instanceof CheckBox) {
                        ((CheckBox) child).setChecked(false);
                    }
                }
            }
            toggleExtrasState(holder.extrasContainer, isChecked);
        });

        // Setup quantity controls
        holder.decreaseButton.setOnClickListener(v -> {
            if (holder.quantity > 1) {
                holder.quantity--;
                holder.quantityText.setText(String.valueOf(holder.quantity));
                foodQuantities.put(food.getFoodId(), holder.quantity);
                listener.onQuantityChanged(food, holder.quantity);
            }
        });

        holder.increaseButton.setOnClickListener(v -> {
            holder.quantity++;
            holder.quantityText.setText(String.valueOf(holder.quantity));
            foodQuantities.put(food.getFoodId(), holder.quantity);
            listener.onQuantityChanged(food, holder.quantity);
        });
    }

    private void setupExtrasForFood(MealViewHolder holder, Food food) {
        holder.extrasContainer.removeAllViews();
        List<FoodExtraDetails> foodExtras = foodExtrasMap.get(food.getFoodId());
        if (foodExtras != null && !foodExtras.isEmpty()) {
            for (FoodExtraDetails extra : foodExtras) {
                CheckBox extraCheckBox = new CheckBox(holder.itemView.getContext());
                extraCheckBox.setText(extra.getDetailsName());
                extraCheckBox.setEnabled(holder.mealCheckBox.isChecked());

                // Restore selected state if any
                List<Long> selectedExtraIds = selectedExtras.get(food.getFoodId());
                if (selectedExtraIds != null) {
                    extraCheckBox.setChecked(selectedExtraIds.contains(extra.getFoodDetailsId()));
                }

                extraCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    updateSelectedExtras(food.getFoodId(), extra.getFoodDetailsId(), isChecked);
                });

                holder.extrasContainer.addView(extraCheckBox);
            }
        }
    }

    private void updateSelectedExtras(long foodId, long extraId, boolean isSelected) {
        selectedExtras.computeIfAbsent(foodId, k -> new ArrayList<>());
        List<Long> extras = selectedExtras.get(foodId);
        if (isSelected && !extras.contains(extraId)) {
            extras.add(extraId);
        } else if (!isSelected) {
            extras.remove(extraId);
        }
    }

    private void toggleExtrasState(ViewGroup extrasContainer, boolean enabled) {
        for (int i = 0; i < extrasContainer.getChildCount(); i++) {
            View child = extrasContainer.getChildAt(i);
            child.setEnabled(enabled);
        }
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }


    public List<Food> getSelectedFoods() {
        List<Food> selected = new ArrayList<>();
        for (Food food : foods) {
            if (selectedFoods.getOrDefault(food.getFoodId(), false)) {
                selected.add(food);
            }
        }
        return selected;
    }

    public Map<Long, List<Long>> getSelectedExtras() {
        return new HashMap<>(selectedExtras);
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealName;
        CheckBox mealCheckBox;
        ViewGroup extrasContainer;
        // Add new quantity control fields
        LinearLayout quantityLayout;
        ImageButton decreaseButton;
        ImageButton increaseButton;
        TextView quantityText;
        int quantity = 1;

        MealViewHolder(View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealName);
            mealCheckBox = itemView.findViewById(R.id.mealCheckBox);
            extrasContainer = itemView.findViewById(R.id.extrasContainer);
            // Initialize new quantity control views
            quantityLayout = itemView.findViewById(R.id.quantityLayout);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            quantityText = itemView.findViewById(R.id.quantityText);
        }
    }
}