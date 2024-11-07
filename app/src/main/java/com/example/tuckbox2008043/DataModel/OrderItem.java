package com.example.tuckbox2008043.DataModel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_items",
        indices = {
                @Index("Order_ID"),
                @Index("Food_ID")
        },
        foreignKeys = {
                @ForeignKey(
                        entity = Order.class,
                        parentColumns = "Order_ID",
                        childColumns = "Order_ID",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Food.class,
                        parentColumns = "Food_ID",
                        childColumns = "Food_ID",
                        onDelete = ForeignKey.RESTRICT
                )
        })
public class OrderItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Order_Item_ID")
    private long orderItemId;

    @ColumnInfo(name = "Order_ID")
    private long orderId;

    @ColumnInfo(name = "Food_ID")
    private long foodId;

    @ColumnInfo(name = "Quantity")
    private int quantity;

    public OrderItem(long orderId, long foodId, int quantity) {
        this.orderId = orderId;
        this.foodId = foodId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public long getOrderItemId() { return orderItemId; }
    public void setOrderItemId(long orderItemId) { this.orderItemId = orderItemId; }

    public long getOrderId() { return orderId; }
    public void setOrderId(long orderId) { this.orderId = orderId; }

    public long getFoodId() { return foodId; }
    public void setFoodId(long foodId) { this.foodId = foodId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
