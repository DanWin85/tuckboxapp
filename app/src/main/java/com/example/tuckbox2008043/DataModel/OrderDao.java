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
    void insert(Order order);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Order> orders);

    @Update
    void update(Order order);

    @Delete
    void delete(Order order);

    @Query("DELETE FROM orders")
    void deleteAll();

    @Query("SELECT * FROM orders WHERE Order_ID = :orderId")
    LiveData<Order> getOrderById(String orderId);

    @Query("SELECT * FROM orders")
    LiveData<List<Order>> getAllOrders();

    @Query("SELECT * FROM orders WHERE User_ID = :userId")
    LiveData<List<Order>> getOrdersByUser(String userId);

    @Query("SELECT * FROM orders WHERE City_ID = :cityId")
    LiveData<List<Order>> getOrdersByCity(String cityId);

    @Query("SELECT * FROM orders WHERE Food_Details_ID = :foodDetailsId")
    LiveData<List<Order>> getOrdersByFoodDetails(String foodDetailsId);

    @Query("SELECT * FROM orders WHERE Time_Slot_ID = :timeSlotId")
    LiveData<List<Order>> getOrdersByTimeSlot(String timeSlotId);

    @Query("SELECT * FROM orders WHERE Order_Date BETWEEN :startDate AND :endDate")
    LiveData<List<Order>> getOrdersByDateRange(Date startDate, Date endDate);

    @Query("SELECT * FROM orders WHERE Order_Date = :date")
    LiveData<List<Order>> getOrdersByDate(Date date);

    @Query("SELECT COUNT(*) FROM orders WHERE User_ID = :userId")
    LiveData<Integer> getOrderCountForUser(String userId);

    @Query("SELECT SUM(Quantity) FROM orders WHERE Food_Details_ID = :foodDetailsId")
    LiveData<Integer> getTotalQuantityForFood(String foodDetailsId);

    @Query("SELECT * FROM orders ORDER BY Order_Date DESC LIMIT :limit")
    LiveData<List<Order>> getRecentOrders(int limit);

    @Query("SELECT * FROM orders WHERE User_ID = :userId ORDER BY Order_Date DESC LIMIT :limit")
    LiveData<List<Order>> getRecentOrdersByUser(String userId, int limit);

    @Transaction
    @Query("SELECT EXISTS(SELECT 1 FROM orders WHERE Order_ID = :orderId)")
    LiveData<Boolean> orderExists(String orderId);

    @Query("SELECT COUNT(*) FROM orders WHERE City_ID = :cityId AND Time_Slot_ID = :timeSlotId AND Order_Date = :date")
    LiveData<Integer> getOrderCountForCityAndTimeSlot(String cityId, String timeSlotId, Date date);
}
