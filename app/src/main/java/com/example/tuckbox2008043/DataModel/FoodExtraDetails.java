package com.example.tuckbox2008043.DataModel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "food_extra_details",
        indices = {
                @Index("Food_ID")
        },
        foreignKeys = @ForeignKey(
                entity = Food.class,
                parentColumns = "Food_ID",
                childColumns = "Food_ID",
                onDelete = ForeignKey.CASCADE
        )
)
public class FoodExtraDetails {
    @PrimaryKey
    @ColumnInfo(name = "Food_Details_ID")
    private long foodDetailsId;

    @ColumnInfo(name = "Details_Name")
    private String detailsName;

    @ColumnInfo(name = "Food_ID")
    private long foodId;

    // Constructors
    public FoodExtraDetails(@NonNull long foodDetailsId, String detailsName, long foodId) {
        this.foodDetailsId = foodDetailsId;
        this.detailsName = detailsName;
        this.foodId = foodId;
    }

    // Getters and Setters
    public long getFoodDetailsId() { return foodDetailsId; }
    public void setFoodDetailsId(long foodDetailsId) { this.foodDetailsId = foodDetailsId; }

    public String getDetailsName() { return detailsName; }
    public void setDetailsName(String detailsName) { this.detailsName = detailsName; }

    public long getFoodId() { return foodId; }
    public void setFoodId(long foodId) { this.foodId = foodId; }
}
