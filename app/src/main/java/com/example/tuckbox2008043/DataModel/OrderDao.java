package com.example.tuckbox2008043.DataModel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Transaction;

import java.util.Date;
import java.util.List;

@Dao
public interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Order order);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Order> orders);

    @Update
    void update(Order order);

    @Delete
    void delete(Order order);

    @Query("DELETE FROM orders")
    void deleteAll();

    @Query("SELECT * FROM orders WHERE Order_ID = :orderId")
    LiveData<Order> getOrderById(long orderId);

    @Query("SELECT * FROM orders WHERE User_ID = :userId")
    LiveData<List<Order>> getOrdersByUser(long userId);

    @Query("SELECT * FROM orders WHERE City_ID = :cityId")
    LiveData<List<Order>> getOrdersByCity(long cityId);

    @Query("SELECT * FROM orders WHERE Food_Details_ID = :foodDetailsId")
    LiveData<List<Order>> getOrdersByFoodDetails(long foodDetailsId);

    @Query("SELECT * FROM orders WHERE Time_Slot_ID = :timeSlotId")
    LiveData<List<Order>> getOrdersByTimeSlot(long timeSlotId);

    @Query("SELECT COUNT(*) FROM orders WHERE User_ID = :userId")
    LiveData<Integer> getOrderCountForUser(long userId);

    @Query("SELECT SUM(Quantity) FROM orders WHERE Food_Details_ID = :foodDetailsId")
    LiveData<Integer> getTotalQuantityForFood(long foodDetailsId);

    @Query("SELECT * FROM orders ORDER BY Order_Date DESC LIMIT :limit")
    LiveData<List<Order>> getRecentOrders(int limit);

    @Query("SELECT * FROM orders WHERE User_ID = :userId ORDER BY Order_Date DESC LIMIT :limit")
    LiveData<List<Order>> getRecentOrdersByUser(long userId, int limit);

    @Query("SELECT EXISTS(SELECT 1 FROM orders WHERE Order_ID = :orderId)")
    LiveData<Boolean> orderExists(long orderId);

    @Query("SELECT COUNT(*) FROM orders WHERE City_ID = :cityId AND Time_Slot_ID = :timeSlotId AND Order_Date = :date")
    LiveData<Integer> getOrderCountForCityAndTimeSlot(long cityId, long timeSlotId, Date date);

    @Query("SELECT * FROM orders WHERE User_ID = :userId ORDER BY Order_Date DESC LIMIT 1")
    LiveData<Order> getMostRecentOrder(long userId);

    @Transaction
    @Query("SELECT EXISTS(SELECT 1 FROM orders WHERE Order_ID = :orderId)")
    LiveData<Boolean> orderExists(String orderId);

    @Query("SELECT COUNT(*) FROM orders WHERE City_ID = :cityId AND Time_Slot_ID = :timeSlotId AND Order_Date = :date")
    LiveData<Integer> getOrderCountForCityAndTimeSlot(String cityId, String timeSlotId, Date date);
}
