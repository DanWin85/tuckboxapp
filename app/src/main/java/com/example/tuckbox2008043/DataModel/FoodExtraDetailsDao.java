package com.example.tuckbox2008043.DataModel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FoodExtraDetailsDao {
    @Query("SELECT * FROM food_extra_details WHERE Food_ID = :foodId")
    List<FoodExtraDetails> getExtraDetailsByFoodId(String foodId);

    @Query("SELECT * FROM food_extra_details WHERE Food_Details_ID = :detailsId")
    FoodExtraDetails getFoodExtraDetailsById(String detailsId);

    @Insert
    void insert(FoodExtraDetails foodExtraDetails);

    @Update
    void update(FoodExtraDetails foodExtraDetails);

    @Delete
    void delete(FoodExtraDetails foodExtraDetails);
}
