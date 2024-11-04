package com.example.tuckbox2008043.DataModel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface DeliveryAddressDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertAddress(DeliveryAddress address);

    @Query("SELECT * FROM delivery_addresses WHERE User_ID = :userId")
    LiveData<List<DeliveryAddress>> getAddressesForUser(long userId);

    @Query("SELECT * FROM delivery_addresses WHERE User_ID = :userId")
    List<DeliveryAddress> getAddressesForUserDirect(long userId);

    @Query("SELECT * FROM delivery_addresses WHERE address_id = :addressId")
    DeliveryAddress getAddressById(long addressId);

    @Update
    int updateAddress(DeliveryAddress address);

    @Delete
    int deleteAddress(DeliveryAddress address);
}
