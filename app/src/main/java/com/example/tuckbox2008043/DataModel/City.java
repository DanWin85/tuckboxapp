package com.example.tuckbox2008043.DataModel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cities")
public class City {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "City_ID")
    private String cityId;

    @ColumnInfo(name = "CityName")
    private String cityName;

    // Constructors
    public City(@NonNull String cityId, String cityName) {
        this.cityId = cityId;
        this.cityName = cityName;
    }

    // Getters and Setters
    @NonNull
    public String getCityId() { return cityId; }
    public void setCityId(@NonNull String cityId) { this.cityId = cityId; }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
}
