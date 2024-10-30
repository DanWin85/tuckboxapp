package com.example.tuckbox2008043.DataModel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(
        tableName = "delivery_addresses",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "User_ID",
                childColumns = "User_ID",
                onDelete = ForeignKey.CASCADE
        )
)
public class DeliveryAddress implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "Address_ID")
    private String addressId;

    @ColumnInfo(name = "Address")
    private String address;

    @ColumnInfo(name = "User_ID")
    private long userId;

    // Constructors
    public DeliveryAddress(@NonNull String addressId, String address, long userId) {
        this.addressId = addressId;
        this.address = address;
        this.userId = userId;
    }

    // Getters and Setters
    @NonNull
    public String getAddressId() { return addressId; }
    public void setAddressId(@NonNull String addressId) { this.addressId = addressId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
}
