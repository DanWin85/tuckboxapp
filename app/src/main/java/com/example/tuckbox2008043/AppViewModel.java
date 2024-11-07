package com.example.tuckbox2008043;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tuckbox2008043.DataModel.City;
import com.example.tuckbox2008043.DataModel.DeliveryAddress;
import com.example.tuckbox2008043.DataModel.Food;
import com.example.tuckbox2008043.DataModel.FoodExtraDetails;
import com.example.tuckbox2008043.DataModel.TimeSlot;
import com.example.tuckbox2008043.DataModel.User;
import com.example.tuckbox2008043.DataModel.Order;
import com.example.tuckbox2008043.DataModel.OrderItem;

import java.util.List;

public class AppViewModel extends AndroidViewModel {
    AppDataModel dataModel = null;
    static final String DATE_FORMAT = "dd/MM/yyyy";

    public static final String USER_PREF_DATA = "USER_PREF_DATA";
    public static final String USER_PREF_USER_EMAIL = "USER_PREF_USER_EMAIL";
    public static final String USER_PREF_USER_PASSWORD = "USER_PREF_USER_PASSWORD";

    public AppViewModel(Application application) {
        super(application);
        dataModel = new AppDataModel(application);
    }

    public long getNextUserID() {
        return dataModel.getNextUserId();
    }

    public long insertUser(User user) {
        return dataModel.insertUser(user);
    }

    public LiveData<List<User>> getLiveUserList() {
        return dataModel.getLiveUserList();
    }

    public User getUserByEmail(String email) {
        return dataModel.getUserByEmail(email);
    }

    public int updateUser(User user) {
        return dataModel.updateUser(user);
    }

    public void deleteUser(User user, OnDeleteCallback callback) {
        new Thread(() -> {
            try {
                dataModel.beginTransaction();

                long userId = user.getUserId();
                Log.d("AppViewModel", "Starting user deletion process for userId: " + userId);

                // 1. Delete all orders and their items
                boolean ordersDeleted = dataModel.deleteAllUserOrdersAndItems(userId);
                Log.d("AppViewModel", "Orders deletion result: " + ordersDeleted);

                // 2. Delete addresses
                List<DeliveryAddress> addresses = dataModel.getAllAddressesForUserSync(userId);
                Log.d("AppViewModel", "Found addresses count: " + (addresses != null ? addresses.size() : 0));

                if (addresses != null) {
                    for (DeliveryAddress address : addresses) {
                        dataModel.deleteDeliveryAddress(address);
                        Log.d("AppViewModel", "Deleted address: " + address.getAddressId());
                    }
                }

                // 3. Delete the user
                int result = dataModel.deleteUser(user);
                Log.d("AppViewModel", "User deletion result: " + result);

                dataModel.setTransactionSuccessful();

                new Handler(Looper.getMainLooper()).post(() -> {
                    callback.onComplete(result > 0);
                });
            } catch (Exception e) {
                Log.e("AppViewModel", "Error deleting user", e);
                new Handler(Looper.getMainLooper()).post(() -> {
                    callback.onComplete(false);
                });
            } finally {
                dataModel.endTransaction();
            }
        }).start();
    }

    public interface OnDeleteCallback {
        void onComplete(boolean success);
    }




    // Address methods
    public long insertDeliveryAddress(DeliveryAddress address) {
        return dataModel.insertDeliveryAddress(address);
    }

    public LiveData<List<DeliveryAddress>> getAddressesForUser(long userId) {
        return dataModel.getAddressesForUser(userId);
    }

    public int deleteDeliveryAddress(DeliveryAddress address) {
        return dataModel.deleteDeliveryAddress(address);
    }

    public void syncAddressesForUser(long userId) {
        dataModel.syncAddressesForUser(userId);
    }

    // Order methods
    public LiveData<List<Order>> getOrdersForUser(long userId) {
        return dataModel.getOrdersForUser(userId);
    }

    public void deleteOrder(Order order) {
        dataModel.deleteOrder(order.getOrderId());
    }

    public long insertOrder(Order order, List<OrderItem> orderItems) {
        return dataModel.insertOrder(order, orderItems);
    }

    public LiveData<Order> getMostRecentOrder(long userId) {
        return dataModel.getMostRecentOrder(userId);
    }

    public LiveData<List<OrderItem>> getOrderItems(long orderId) {
        return dataModel.getOrderItems(orderId);
    }

    // Get methods for other entities
    public List<Food> getAllFoods() {
        return dataModel.getAllFoods();
    }

    public List<FoodExtraDetails> getAllFoodExtras() {
        return dataModel.getAllFoodExtras();
    }

    public List<City> getAllCities() {
        return dataModel.getAllCities();
    }

    public List<TimeSlot> getAllTimeSlots() {
        return dataModel.getAllTimeSlots();
    }
}