package com.example.tuckbox2008043;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.tuckbox2008043.DataModel.AppDatabase;
import com.example.tuckbox2008043.DataModel.DeliveryAddress;
import com.example.tuckbox2008043.DataModel.DeliveryAddressDao;
import com.example.tuckbox2008043.DataModel.User;
import com.example.tuckbox2008043.DataModel.UserDao;

import java.util.List;

public class AppDataModel {
    private DeliveryAddressDao deliveryAddressDao;
    private UserDao userDao;
    private RemoteDBHandler remoteDBHandler;

    public AppDataModel(Application application){
        AppDatabase database = AppDatabase.createDatabaseInstance(application);
        userDao = database.getUserDao();
        deliveryAddressDao = database.getDeliveryAddressDao();
        remoteDBHandler = new RemoteDBHandler(this);
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
    public int updateUser (User user){
        return userDao.updateUser(user);
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

    public List<DeliveryAddress> getAddressesForUser(String userId) {
        return deliveryAddressDao.getAddressesForUser(userId);
    }
    public int deleteDeliveryAddress(DeliveryAddress address) {
        int deleted = deliveryAddressDao.deleteAddress(address);
        if (deleted > 0) {
            remoteDBHandler.deleteDeliveryAddress(address);
        }
        return deleted;
    }
}
