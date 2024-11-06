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
                @Index("City_ID"),
                @Index("Time_Slot_ID"),
                @Index("User_ID")
        },
        foreignKeys = {
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
                        onDelete = ForeignKey.CASCADE)
        })
public class Order {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Order_ID")
    private long orderId;

    @ColumnInfo(name = "Order_Date")
    private Date orderDate;

    @ColumnInfo(name = "City_ID")
    private long cityId;

    @ColumnInfo(name = "Time_Slot_ID")
    private long timeSlotId;

    @ColumnInfo(name = "User_ID")
    private long userId;

    public Order(long cityId, long timeSlotId, long userId) {
        this.orderDate = new Date();
        this.cityId = cityId;
        this.timeSlotId = timeSlotId;
        this.userId = userId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public void setTimeSlotId(long timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getOrderId() {
        return orderId;
    }

    public Date getOrderDate() {
        return orderDate;
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

