package com.example.tuckbox2008043.DataModel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "foods")
public class Food implements Serializable {

        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "Food_ID")
        private String foodId;

        @ColumnInfo(name = "Food_Name")
        private String foodName;

        @ColumnInfo(name = "Food_Extra_Choice")
        private boolean foodExtraChoice;

        // Constructors
        public Food(@NonNull String foodId, String foodName, boolean foodExtraChoice) {
            this.foodId = foodId;
            this.foodName = foodName;
            this.foodExtraChoice = foodExtraChoice;
        }

        // Getters and Setters
        @NonNull
        public String getFoodId() { return foodId; }
        public void setFoodId(@NonNull String foodId) { this.foodId = foodId; }

        public String getFoodName() { return foodName; }
        public void setFoodName(String foodName) { this.foodName = foodName; }

        public boolean isFoodExtraChoice() { return foodExtraChoice; }
        public void setFoodExtraChoice(boolean foodExtraChoice) { this.foodExtraChoice = foodExtraChoice; }

}
