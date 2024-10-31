package com.example.tuckbox2008043.DataModel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CityDao {
    @Query("SELECT * FROM cities")
    List<City> getAllCities();

    @Query("SELECT * FROM cities WHERE City_ID = :cityId")
    City getCityById(String cityId);

    @Insert
    void insert(City city);

    @Update
    void update(City city);

    @Delete
    void delete(City city);
}
