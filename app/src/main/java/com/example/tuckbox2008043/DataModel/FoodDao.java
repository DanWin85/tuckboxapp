package com.example.tuckbox2008043.DataModel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM foods")
    List<Food> getAllFoods();

    @Query("SELECT * FROM foods WHERE food_id = :foodId")
    Food getFoodById(long foodId);

    @Insert
    void insert(Food food);

    @Update
    void update(Food food);

    @Delete
    void delete(Food food);

    @Insert
    void insertFood(Food food);
}
