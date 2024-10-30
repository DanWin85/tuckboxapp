package com.example.tuckbox2008043.DataModel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "food_extra_details",
        foreignKeys = @ForeignKey(
                entity = Food.class,
                parentColumns = "Food_ID",
                childColumns = "Food_ID",
                onDelete = ForeignKey.CASCADE
        )
)
public class FoodExtraDetails {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "Food_Details_ID")
    private String foodDetailsId;

    @ColumnInfo(name = "Details_Name")
    private String detailsName;

    @NonNull
    @ColumnInfo(name = "Food_ID")
    private String foodId;

    // Constructors
    public FoodExtraDetails(@NonNull String foodDetailsId, String detailsName, @NonNull String foodId) {
        this.foodDetailsId = foodDetailsId;
        this.detailsName = detailsName;
        this.foodId = foodId;
    }

    // Getters and Setters
    @NonNull
    public String getFoodDetailsId() { return foodDetailsId; }
    public void setFoodDetailsId(@NonNull String foodDetailsId) { this.foodDetailsId = foodDetailsId; }

    public String getDetailsName() { return detailsName; }
    public void setDetailsName(String detailsName) { this.detailsName = detailsName; }

    @NonNull
    public String getFoodId() { return foodId; }
    public void setFoodId(@NonNull String foodId) { this.foodId = foodId; }
}
