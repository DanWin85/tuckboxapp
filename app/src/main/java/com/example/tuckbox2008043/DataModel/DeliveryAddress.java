package com.example.tuckbox2008043.DataModel;

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
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Address_ID")
    private long addressId;

    @ColumnInfo(name = "Address")
    private String address;

    @ColumnInfo(name = "User_ID")
    private long userId;

    // Constructors
    public DeliveryAddress(String address, long userId) {
        this.address = address;
        this.userId = userId;
    }

    // Getters and Setters
    public long getAddressId() { return addressId; }
    public void setAddressId(long addressId) { this.addressId = addressId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
}
