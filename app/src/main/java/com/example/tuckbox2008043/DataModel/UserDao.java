package com.example.tuckbox2008043.DataModel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Query("SELECT * FROM users")
    LiveData<List<User>> getLiveDataAllUsers();

    @Query("SELECT * FROM users WHERE User_Email LIKE :email")
    List<User> getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE User_Last_Name LIKE :lastName")
    List<User> getUserByLastName(String lastName);

    @Query("SELECT * FROM users WHERE User_Mobile LIKE :mobile")
    List<User> getUserByMobile(String mobile);

    @Insert
    long insertUser(User user);

    @Update
    int updateUser(User user);

    @Delete
    int deleteUser(User user);


}
