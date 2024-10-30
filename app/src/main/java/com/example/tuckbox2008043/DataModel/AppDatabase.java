package com.example.tuckbox2008043.DataModel;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
            entities = {User.class, DeliveryAddress.class},
            version = 1,
            exportSchema = false
)
@TypeConverters(DataTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance = null;
    public abstract UserDao getUserDao();
    public abstract DeliveryAddressDao getDeliveryAddressDao();

    public static AppDatabase createDatabaseInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "TuckBox_Database"
            )
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
