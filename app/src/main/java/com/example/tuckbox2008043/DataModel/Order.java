package com.example.tuckbox2008043.DataModel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;


@Entity(tableName = "orders",
        indices = {
                @Index("Food_Details_ID"),
                @Index("City_ID"),
                @Index("Time_Slot_ID"),
                @Index("User_ID")
        },
        foreignKeys = {
                @ForeignKey(entity = FoodExtraDetails.class,
                        parentColumns = "Food_Details_ID",
                        childColumns = "Food_Details_ID",
                        onDelete = ForeignKey.RESTRICT),
                @ForeignKey(entity = City.class,
                        parentColumns = "City_ID",
                        childColumns = "City_ID",
                        onDelete = ForeignKey.RESTRICT),
                @ForeignKey(entity = TimeSlot.class,
                        parentColumns = "Time_Slot_ID",
                        childColumns = "Time_Slot_ID",
                        onDelete = ForeignKey.RESTRICT),
                @ForeignKey(entity = User.class,
                        parentColumns = "User_ID",
                        childColumns = "User_ID",
                        onDelete = ForeignKey.RESTRICT)
        })
public class Order implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Order_ID")
    private long orderId;

    @ColumnInfo(name = "Order_Date")
    private Date orderDate;

    @ColumnInfo(name = "Quantity")
    private int quantity;

    @ColumnInfo(name = "Food_Details_ID")
    private long foodDetailsId;

    @ColumnInfo(name = "City_ID")
    private long cityId;

    @ColumnInfo(name = "Time_Slot_ID")
    private long timeSlotId;

    @ColumnInfo(name = "User_ID")
    private long userId;

    public Order(int quantity, long foodDetailsId, long cityId, long timeSlotId, long userId) {
        this.orderDate = new Date(); // Current date/time
        this.quantity = quantity;
        this.foodDetailsId = foodDetailsId;
        this.cityId = cityId;
        this.timeSlotId = timeSlotId;
        this.userId = userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setTimeSlotId(long timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public void setFoodDetailsId(long foodDetailsId) {
        this.foodDetailsId = foodDetailsId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    // Add getters and setters

    public long getOrderId() {
        return orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getFoodDetailsId() {
        return foodDetailsId;
    }

    public long getCityId() {
        return cityId;
    }

    public long getTimeSlotId() {
        return timeSlotId;
    }

    public long getUserId() {
        return userId;
    }
}

