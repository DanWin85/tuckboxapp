package com.example.tuckbox2008043;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tuckbox2008043.DataModel.DeliveryAddress;
import com.example.tuckbox2008043.DataModel.User;

import java.util.List;

public class AppViewModel extends AndroidViewModel {
    AppDataModel dataModel = null;
    static final String DATE_FORMAT = "dd/MM/yyyy";

    public static final String USER_PREF_DATA = "USER_PREF_DATA";
    public static final String USER_PREF_USER_EMAIL = "USER_PREF_USER_EMAIL";
    public static final String USER_PREF_USER_PASSWORD = "USER_PREF_USER_PASSWORD";

    public AppViewModel(Application application){
        super(application);
        dataModel = new AppDataModel(application);
    }

    public long getNextUserID(){
        return dataModel.getNextUserId();
    }

    public long insertUser(User user){
        return dataModel.insertUser(user);
    }

    public LiveData<List<User>> getLiveUserList(){
        return dataModel.getLiveUserList();
    }

    public User getUserByEmail(String email){
        return dataModel.getUserByEmail(email);
    }

    public long insertDeliveryAddress(DeliveryAddress address) {
        return dataModel.insertDeliveryAddress(address);
    }

    public List<DeliveryAddress> getAddressesForUser(String userId) {
        return dataModel.getAddressesForUser(userId);
    }
    public int deleteDeliveryAddress(DeliveryAddress address) {
        return dataModel.deleteDeliveryAddress(address);
    }
}
