package com.example.tuckbox2008043;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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

    public interface MealSelectionListener {
        void onMealSelected(Food food);
    }

    public MealAdapter(List<Food> foods, List<FoodExtraDetails> extras, MealSelectionListener listener) {
        this.foods = foods;
        this.extras = extras;
        this.listener = listener;
        this.selectedFoods = new HashMap<>();
        this.selectedExtras = new HashMap<>();

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

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Food food = foods.get(position);

        // Set main food item details
        holder.mealName.setText(food.getFoodName());

        // Set checkbox state
        holder.mealCheckBox.setChecked(selectedFoods.getOrDefault(food.getFoodId(), false));

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
            if (isChecked) {
                listener.onMealSelected(food);
            } else {
                // Clear selected extras when meal is deselected
                selectedExtras.remove(food.getFoodId());
                // Uncheck all extra checkboxes
                for (int i = 0; i < holder.extrasContainer.getChildCount(); i++) {
                    View child = holder.extrasContainer.getChildAt(i);
                    if (child instanceof CheckBox) {
                        ((CheckBox) child).setChecked(false);
                    }
                }
            }
            // Enable/disable extras based on meal selection
            toggleExtrasState(holder.extrasContainer, isChecked);
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

    static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealName;
        CheckBox mealCheckBox;
        ViewGroup extrasContainer;

        MealViewHolder(View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealName);
            mealCheckBox = itemView.findViewById(R.id.mealCheckBox);
            extrasContainer = itemView.findViewById(R.id.extrasContainer);
        }
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
}