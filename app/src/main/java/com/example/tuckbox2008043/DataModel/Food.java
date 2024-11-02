package com.example.tuckbox2008043.DataModel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "foods")
public class Food implements Serializable {

        @PrimaryKey
        @ColumnInfo(name = "Food_ID")
        private long foodId;

        @ColumnInfo(name = "Food_Name")
        private String foodName;

        @ColumnInfo(name = "Food_Extra_Choice")
        private boolean foodExtraChoice;

        // Constructors
        public Food(long foodId, String foodName, boolean foodExtraChoice) {
            this.foodId = foodId;
            this.foodName = foodName;
            this.foodExtraChoice = foodExtraChoice;
        }

        // Getters and Setters
        public long getFoodId() { return foodId; }
        public void setFoodId(long foodId) { this.foodId = foodId; }

        public String getFoodName() { return foodName; }
        public void setFoodName(String foodName) { this.foodName = foodName; }

        public boolean isFoodExtraChoice() { return foodExtraChoice; }
        public void setFoodExtraChoice(boolean foodExtraChoice) { this.foodExtraChoice = foodExtraChoice; }

}
