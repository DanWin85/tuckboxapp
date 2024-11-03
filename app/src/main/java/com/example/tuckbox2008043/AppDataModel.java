package com.example.tuckbox2008043;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.tuckbox2008043.DataModel.AppDatabase;
import com.example.tuckbox2008043.DataModel.City;
import com.example.tuckbox2008043.DataModel.CityDao;
import com.example.tuckbox2008043.DataModel.DeliveryAddress;
import com.example.tuckbox2008043.DataModel.DeliveryAddressDao;
import com.example.tuckbox2008043.DataModel.Food;
import com.example.tuckbox2008043.DataModel.FoodDao;
import com.example.tuckbox2008043.DataModel.FoodExtraDetails;
import com.example.tuckbox2008043.DataModel.FoodExtraDetailsDao;
import com.example.tuckbox2008043.DataModel.TimeSlot;
import com.example.tuckbox2008043.DataModel.TimeSlotDao;
import com.example.tuckbox2008043.DataModel.User;
import com.example.tuckbox2008043.DataModel.UserDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppDataModel {
    private final DeliveryAddressDao deliveryAddressDao;
    private final UserDao userDao;
    private final FoodDao foodDao;
    private final FoodExtraDetailsDao foodExtraDetailsDao;
    private final CityDao cityDao;
    private final TimeSlotDao timeSlotDao;
    private final RemoteDBHandler remoteDBHandler;

    public AppDataModel(Application application){
        AppDatabase database = AppDatabase.createDatabaseInstance(application);
        userDao = database.getUserDao();
        deliveryAddressDao = database.getDeliveryAddressDao();
        foodDao = database.getFoodDao();
        foodExtraDetailsDao = database.getFoodExtraDao();
        cityDao = database.getCityDao();
        timeSlotDao = database.getTimeSlotDao();
        remoteDBHandler = new RemoteDBHandler(this);
        initializeFoodData();
        initializeFoodExtraData();
        initializeCityData();
        initializeTimeSlotData();
    }

    public long getNextUserId(){
        return userDao.getAllUsers().size() + 1;
    }
    public long insertUser(User user){
        long inserted = userDao.insertUser(user);
        if(inserted != -1){
            remoteDBHandler.insertUser(user);
        }
        return inserted;
    }

    public LiveData<List<User>> getLiveUserList(){
        return userDao.getLiveDataAllUsers();
    }

    public User getUserByEmail(String email){
        List<User> userList = userDao.getUserByEmail(email);
        if(userList.size() == 1)
            return userList.get(0);
        else
            return null;
    }
    public List<User> getUserByLastName(String lastName){
        List<User> userList = userDao.getUserByLastName(lastName);
        if(userList.size() != 0)
            return userList;
        else
            return null;
    }
    public User getUserByMobile(String mobile){
        List<User> userList = userDao.getUserByMobile(mobile);
        if(userList.size() == 1)
            return userList.get(0);
        else
            return null;
    }
    public int deleteUser (User user){
        return userDao.deleteUser(user);
    }

    public long insertDeliveryAddress(DeliveryAddress address) {
        long inserted = deliveryAddressDao.insertAddress(address);
        if(inserted != -1) {
            remoteDBHandler.insertDeliveryAddress(address);
        }
        return inserted;
    }

    public LiveData<List<DeliveryAddress>> getAddressesForUser(long userId) {
        return deliveryAddressDao.getAddressesForUser(userId);
    }
    public int deleteDeliveryAddress(DeliveryAddress address) {
        int deleted = deliveryAddressDao.deleteAddress(address);
        if (deleted > 0) {
            remoteDBHandler.deleteDeliveryAddress(address);
        }
        return deleted;
    }
    public void syncAddressesForUser(long userId) {
        remoteDBHandler.syncAddressesForUser(userId);
    }

    public int updateUser(User user) {
        int updated = userDao.updateUser(user);
        if (updated > 0) {
            remoteDBHandler.updateUser(user);
        }
        return updated;
    }

    private void initializeFoodData() {
        if (foodDao.getAllFoods().isEmpty()) {
            Log.d("AppDataModel", "Initializing food data");
            List<Food> foods = new ArrayList<>();
            foods.add(new Food(0, "Green Salad Lunch", true));
            foods.add(new Food(1, "Green Salad Lunch", false));
            foods.add(new Food(2, "Lamb Korma", true));
            foods.add(new Food(3, "Lamb Korma", false));
            foods.add(new Food(4, "Open Chicken Sandwich", true));
            foods.add(new Food(5, "Open Chicken Sandwich", false));
            foods.add(new Food(6, "Beef Noodle Salad", true));
            foods.add(new Food(7, "Beef Noodle Salad", false));

            // Insert foods into database
            for (Food food : foods) {
                foodDao.insertFood(food);
            }
            Log.d("AppDataModel", "Food data initialized");
        }
    }
    private void initializeFoodExtraData() {
        if (foodExtraDetailsDao.getAllFoodExtras().isEmpty()) {
            Log.d("AppDataModel", "Initializing food extra details data");
            List<FoodExtraDetails> foodExtraDetails = new ArrayList<>();
            foodExtraDetails.add(new FoodExtraDetails(1, "None", 0));
            foodExtraDetails.add(new FoodExtraDetails(2, "Ranch", 0));
            foodExtraDetails.add(new FoodExtraDetails(3, "Vinaigrette", 0));
            foodExtraDetails.add(new FoodExtraDetails(4, "Mild", 2));
            foodExtraDetails.add(new FoodExtraDetails(5, "Med", 2));
            foodExtraDetails.add(new FoodExtraDetails(6, "Hot", 2));
            foodExtraDetails.add(new FoodExtraDetails(7, "White", 4));
            foodExtraDetails.add(new FoodExtraDetails(8, "Rye", 4));
            foodExtraDetails.add(new FoodExtraDetails(9, "Wholemeal", 4));
            foodExtraDetails.add(new FoodExtraDetails(10, "No Chilli Flakes", 6));
            foodExtraDetails.add(new FoodExtraDetails(11, "Regular Chilli Flakes", 6));
            foodExtraDetails.add(new FoodExtraDetails(12, "Extra Chilli Flakes", 6));

            // Insert food extras into database
            for (FoodExtraDetails extra : foodExtraDetails) {
                foodExtraDetailsDao.insertFoodExtra(extra);
            }
            Log.d("AppDataModel", "Food extra details data initialized");
        }
    }
    public void initializeCityData() {
        // Check if there are already cities in the database
        List<City> existingCities = cityDao.getAllCities();

        if (existingCities.isEmpty()) {
            // List of hard-coded cities to be inserted
            List<City> Cities = Arrays.asList(
                    new City(1, "Palmerston North"),
                    new City(2, "Fielding"),
                    new City(3, "Ashhurst"),
                    new City(4, "Longburn")
            );

            // Insert each city into the local database
            for (City city : Cities) {
                cityDao.insert(city);
            }
        }
    }
    private void initializeTimeSlotData() {
        if (timeSlotDao.getAllTimeSlots().isEmpty()) {
            Log.d("AppDataModel", "Initializing time slot data");
            List<TimeSlot> timeSlots = new ArrayList<>();
            timeSlots.add(new TimeSlot(1, "11:45-12:15"));
            timeSlots.add(new TimeSlot(2, "12:15-12:45"));
            timeSlots.add(new TimeSlot(3, "12:45-13:15"));
            timeSlots.add(new TimeSlot(4, "13:15-13:45"));

            // Insert time slots into database
            for (TimeSlot slot : timeSlots) {
                timeSlotDao.insertTimeSlot(slot);
            }
            Log.d("AppDataModel", "Time slot data initialized");
        }
    }
    public List<TimeSlot> getAllTimeSlots(){return timeSlotDao.getAllTimeSlots();}
    public List<Food> getAllFoods(){return foodDao.getAllFoods();}
    public List<FoodExtraDetails> getAllFoodExtras(){return  foodExtraDetailsDao.getAllFoodExtras();}
    public List<City> getAllCities(){return cityDao.getAllCities();}

    public int deleteAddress(DeliveryAddress address) {
        return deliveryAddressDao.deleteAddress(address);
    }
}
