package com.example.tuckbox2008043.DataModel;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "time_slots")
public class TimeSlot {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "Time_Slot_ID")
    private String timeSlotId;

    @ColumnInfo(name = "Time_Slot")
    private String timeSlot;

    // Constructors
    public TimeSlot(@NonNull String timeSlotId, String timeSlot) {
        this.timeSlotId = timeSlotId;
        this.timeSlot = timeSlot;
    }

    // Getters and Setters
    @NonNull
    public String getTimeSlotId() { return timeSlotId; }
    public void setTimeSlotId(@NonNull String timeSlotId) { this.timeSlotId = timeSlotId; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

}
