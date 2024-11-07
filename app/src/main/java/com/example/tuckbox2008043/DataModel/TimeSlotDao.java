package com.example.tuckbox2008043.DataModel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TimeSlotDao {

        @Query("SELECT * FROM time_slots")
        List<TimeSlot> getAllTimeSlots();

        @Query("SELECT * FROM time_slots WHERE time_slot_id = :timeSlotId")
        TimeSlot getTimeSlotById(long timeSlotId);

        @Insert
        void insert(TimeSlot timeSlot);

        @Update
        void update(TimeSlot timeSlot);

        @Delete
        void delete(TimeSlot timeSlot);

        @Insert
        void insertTimeSlot(TimeSlot slot);
}
