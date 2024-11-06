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

    @Query("SELECT * FROM orders WHERE Order_ID = :orderId")
    LiveData<Order> getOrderById(long orderId);

    @Query("SELECT * FROM orders WHERE User_ID = :userId")
    LiveData<List<Order>> getOrdersByUser(long userId);

    @Query("SELECT * FROM orders")
    LiveData<List<Order>> getAllOrders();

    @Query("SELECT * FROM orders WHERE User_ID = :userId ORDER BY Order_Date DESC LIMIT 1")
    LiveData<Order> getMostRecentOrder(long userId);

    @Query("SELECT * FROM orders ORDER BY Order_Date DESC LIMIT :limit")
    LiveData<List<Order>> getRecentOrders(int limit);

    @Query("SELECT * FROM orders WHERE User_ID = :userId ORDER BY Order_Date DESC LIMIT :limit")
    LiveData<List<Order>> getRecentOrdersByUser(long userId, int limit);

    @Query("SELECT * FROM orders WHERE User_ID = :userId")
    List<Order> getAllOrdersForUserSync(long userId);

    @Delete
    void delete(Order order);

    @Delete
    void deleteOrderSync(Order order);

    @Query("DELETE FROM orders WHERE User_ID = :userId")
    void deleteAllUserOrders(long userId);
}

