package com.example.tuckbox2008043.DataModel;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(
            entities = {User.class,
                    DeliveryAddress.class,
                    Food.class,
                    FoodExtraDetails.class,
                    City.class,
                    TimeSlot.class,
                    Order.class,
                    OrderItem.class},
            version = 1,
            exportSchema = false
)
@TypeConverters(DataTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance = null;
    public abstract UserDao getUserDao();
    public abstract DeliveryAddressDao getDeliveryAddressDao();
    public abstract FoodDao getFoodDao();
    public abstract FoodExtraDetailsDao getFoodExtraDao();
    public abstract CityDao getCityDao();
    public abstract TimeSlotDao getTimeSlotDao();
    public abstract OrderDao getOrderDao();
    public abstract OrderItemDao getOrderItemDao();


    public static AppDatabase createDatabaseInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "TuckBox_Database")
                    .addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Drop existing tables
            database.execSQL("DROP TABLE IF EXISTS order_items");
            database.execSQL("DROP TABLE IF EXISTS orders");

            // Recreate orders table with CASCADE
            database.execSQL("CREATE TABLE IF NOT EXISTS orders (" +
                    "Order_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "Order_Date INTEGER, " +
                    "City_ID INTEGER NOT NULL, " +
                    "Time_Slot_ID INTEGER NOT NULL, " +
                    "User_ID INTEGER NOT NULL, " +
                    "FOREIGN KEY(User_ID) REFERENCES users(User_ID) ON DELETE CASCADE, " +
                    "FOREIGN KEY(City_ID) REFERENCES cities(City_ID) ON DELETE RESTRICT, " +
                    "FOREIGN KEY(Time_Slot_ID) REFERENCES time_slots(Time_Slot_ID) ON DELETE RESTRICT)");

            // Create indices for orders
            database.execSQL("CREATE INDEX IF NOT EXISTS index_orders_User_ID ON orders(User_ID)");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_orders_City_ID ON orders(City_ID)");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_orders_Time_Slot_ID ON orders(Time_Slot_ID)");

            // Recreate order_items table with CASCADE
            database.execSQL("CREATE TABLE IF NOT EXISTS order_items (" +
                    "Order_Item_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "Order_ID INTEGER NOT NULL, " +
                    "Food_ID INTEGER NOT NULL, " +
                    "Quantity INTEGER NOT NULL, " +
                    "FOREIGN KEY(Order_ID) REFERENCES orders(Order_ID) ON DELETE CASCADE, " +
                    "FOREIGN KEY(Food_ID) REFERENCES foods(Food_ID) ON DELETE RESTRICT)");

            // Create indices for order_items
            database.execSQL("CREATE INDEX IF NOT EXISTS index_order_items_Order_ID ON order_items(Order_ID)");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_order_items_Food_ID ON order_items(Food_ID)");
        }
    };




}
