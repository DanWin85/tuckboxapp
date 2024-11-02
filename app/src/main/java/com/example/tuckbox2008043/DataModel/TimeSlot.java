package com.example.tuckbox2008043.DataModel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "time_slots")
public class TimeSlot {
    @PrimaryKey
    @ColumnInfo(name = "Time_Slot_ID")
    private long timeSlotId;

    @ColumnInfo(name = "Time_Slot")
    private String timeSlot;

    // Constructors
    public TimeSlot(long timeSlotId, String timeSlot) {
        this.timeSlotId = timeSlotId;
        this.timeSlot = timeSlot;
    }

    // Getters and Setters
    public long getTimeSlotId() { return timeSlotId; }
    public void setTimeSlotId(long timeSlotId) { this.timeSlotId = timeSlotId; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

}
