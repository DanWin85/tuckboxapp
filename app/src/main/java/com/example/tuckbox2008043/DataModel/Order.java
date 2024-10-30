package com.example.tuckbox2008043.DataModel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;


@Entity(
        tableName = "orders",
        foreignKeys = {
                @ForeignKey(
                        entity = FoodExtraDetails.class,
                        parentColumns = "Food_Details_ID",
                        childColumns = "Food_Details_ID",
                        onDelete = ForeignKey.RESTRICT
                ),
                @ForeignKey(
                        entity = City.class,
                        parentColumns = "City_ID",
                        childColumns = "City_ID",
                        onDelete = ForeignKey.RESTRICT
                ),
                @ForeignKey(
                        entity = TimeSlot.class,
                        parentColumns = "Time_Slot_ID",
                        childColumns = "Time_Slot_ID",
                        onDelete = ForeignKey.RESTRICT
                ),
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "User_ID",
                        childColumns = "User_ID",
                        onDelete = ForeignKey.RESTRICT
                )
        }
)
public class Order implements Serializable {

        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "Order_ID")
        private String orderId;

        @ColumnInfo(name = "Order_Date")
        private Date orderDate;

        @ColumnInfo(name = "Quantity")
        private int quantity;

        @NonNull
        @ColumnInfo(name = "Food_Details_ID")
        private String foodDetailsId;

        @NonNull
        @ColumnInfo(name = "City_ID")
        private String cityId;

        @NonNull
        @ColumnInfo(name = "Time_Slot_ID")
        private String timeSlotId;

        @NonNull
        @ColumnInfo(name = "User_ID")
        private String userId;

        // Constructors
        public Order(@NonNull String orderId, Date orderDate, int quantity,
                     @NonNull String foodDetailsId, @NonNull String cityId,
                     @NonNull String timeSlotId, @NonNull String userId) {
            this.orderId = orderId;
            this.orderDate = orderDate;
            this.quantity = quantity;
            this.foodDetailsId = foodDetailsId;
            this.cityId = cityId;
            this.timeSlotId = timeSlotId;
            this.userId = userId;
        }

        // Getters and Setters
        @NonNull
        public String getOrderId() { return orderId; }
        public void setOrderId(@NonNull String orderId) { this.orderId = orderId; }

        public Date getOrderDate() { return orderDate; }
        public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        @NonNull
        public String getFoodDetailsId() { return foodDetailsId; }
        public void setFoodDetailsId(@NonNull String foodDetailsId) { this.foodDetailsId = foodDetailsId; }

        @NonNull
        public String getCityId() { return cityId; }
        public void setCityId(@NonNull String cityId) { this.cityId = cityId; }

        @NonNull
        public String getTimeSlotId() { return timeSlotId; }
        public void setTimeSlotId(@NonNull String timeSlotId) { this.timeSlotId = timeSlotId; }

        @NonNull
        public String getUserId() { return userId; }
        public void setUserId(@NonNull String userId) { this.userId = userId; }
}

