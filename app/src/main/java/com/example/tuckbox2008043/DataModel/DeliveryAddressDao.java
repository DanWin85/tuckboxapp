package com.example.tuckbox2008043.DataModel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface DeliveryAddressDao {
    @Insert
    long insertAddress(DeliveryAddress address);

    @Query("SELECT * FROM delivery_addresses WHERE User_ID = :userId")
    List<DeliveryAddress> getAddressesForUser(String userId);

    @Update
    int updateAddress(DeliveryAddress address);

    @Delete
    int deleteAddress(DeliveryAddress address);
}
