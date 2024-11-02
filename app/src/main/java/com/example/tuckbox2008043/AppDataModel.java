package com.example.tuckbox2008043;

import android.app.Application;

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

    private void initializeFoodData(){
        if(foodDao.getAllFoods().size() == 0){
            List<Food> foods = new ArrayList<>();
            foods.add(new Food(
                    0,
                    "Green Salad Lunch",
                    true
            ));
            foods.add(new Food(
                    1,
                    "Green Salad Lunch",
                    false
            ));
            foods.add(new Food(
                    2,
                    "Lamb Korma",
                    true
            ));
            foods.add(new Food(
                    3,
                    "Lamb Korma",
                    false
            ));
            foods.add(new Food(
                    4,
                    "Open Chicken Sandwich",
                    true
            ));
            foods.add(new Food(
                    5,
                    "Open Chicken Sandwich",
                    false
            ));
            foods.add(new Food(
                    6,
                    "Beef Noodle Salad",
                    true
            ));
            foods.add(new Food(
                    7,
                    "Beef Noodle Salad",
                    false
            ));
        }
    }
    private void initializeFoodExtraData(){
        if(foodExtraDetailsDao.getAllFoodExtras().size() == 0){
            List<FoodExtraDetails> foodExtraDetails = new ArrayList<>();
            foodExtraDetails.add(new FoodExtraDetails(
                    1,
                    "None",
                    0
            ));
            foodExtraDetails.add(new FoodExtraDetails(
                    2,
                    "Ranch",
                    0
            ));
            foodExtraDetails.add(new FoodExtraDetails(
                    3,
                    "Vinaigrette",
                    0
            ));
            foodExtraDetails.add(new FoodExtraDetails(
                    4,
                    "Mild",
                    2
            ));
            foodExtraDetails.add(new FoodExtraDetails(
                    5,
                    "Med",
                    2
            ));
            foodExtraDetails.add(new FoodExtraDetails(
                    6,
                    "Hot",
                    2
            ));
            foodExtraDetails.add(new FoodExtraDetails(
                    7,
                    "White",
                    4
            ));
            foodExtraDetails.add(new FoodExtraDetails(
                    8,
                    "Rye",
                    4
            ));
            foodExtraDetails.add(new FoodExtraDetails(
                    9,
                    "Wholemeal",
                    4
            ));
            foodExtraDetails.add(new FoodExtraDetails(
                    10,
                    "No Chilli Flakes",
                    6
            ));
            foodExtraDetails.add(new FoodExtraDetails(
                    11,
                    "Regular Chilli Flakes",
                    6
            ));
            foodExtraDetails.add(new FoodExtraDetails(
                    12,
                    "Extra Chilli Flakes",
                    6
            ));

        }
    }
    private void initializeCityData(){
        if(cityDao.getAllCities().size() == 0){
            List<City> cities = new ArrayList<>();
            cities.add(new City(
                    0,
                    "Palmerston North"
            ));
            cities.add(new City(
                    1,
                    "Fielding"
            ));
            cities.add(new City(
                    2,
                    "Ashhurst"
            ));
            cities.add(new City(
                    3,
                    "Longburn"
            ));
        }

    }
    private void initializeTimeSlotData(){
        if(timeSlotDao.getAllTimeSlots().size() == 0){
            List<TimeSlot> timeSlots = new ArrayList<>();
            timeSlots.add(new TimeSlot(
                    1,
                    "11:45-12:15"
            ));
            timeSlots.add(new TimeSlot(
                    2,
                    "12:15-12:45"
            ));
            timeSlots.add(new TimeSlot(
                    3,
                    "12:45-13:15"
            ));
            timeSlots.add(new TimeSlot(
                    4,
                    "13:15-13:45"
            ));
        }

    }
    public List<TimeSlot> getAllTimeSlots(){return timeSlotDao.getAllTimeSlots();}
    public List<Food> getAllFoods(){return foodDao.getAllFoods();}
    public List<FoodExtraDetails> getAllFoodExtras(){return  foodExtraDetailsDao.getAllFoodExtras();}
    public List<City> getAllCities(){return cityDao.getAllCities();}
}
