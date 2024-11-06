package com.example.tuckbox2008043.DataModel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OrderItemDao {
    @Insert
    long insert(OrderItem orderItem);

    @Query("SELECT * FROM order_items WHERE Order_ID = :orderId")
    LiveData<List<OrderItem>> getOrderItemsForOrder(long orderId);

    @Query("SELECT SUM(Quantity) FROM order_items WHERE Food_ID = :foodId")
    LiveData<Integer> getTotalQuantityForFood(long foodId);

    @Query("DELETE FROM order_items WHERE Order_ID = :orderId")
    void deleteOrderItems(long orderId);


    @Query("DELETE FROM order_items WHERE Order_ID IN (SELECT Order_ID FROM orders WHERE User_ID = :userId)")
    void deleteAllUserOrderItems(long userId);
}
