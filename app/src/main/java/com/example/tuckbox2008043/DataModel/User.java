package com.example.tuckbox2008043.DataModel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "users",
        indices = {
                    @Index(
                    value = "User_Email",
                    unique = true
                    ),
                    @Index(name = "User_Last_Name_Index",
                    value = "User_Last_Name"
                    ),
                    @Index(name = "User_Mobile_Index",
                    value = "User_Mobile", unique = true
                    )
        }
)
public class User implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "User_ID")
    private long userId;

    @ColumnInfo(name = "User_Email")
    private String userEmail;

    @ColumnInfo(name = "User_Password")
    private String password;

    @ColumnInfo(name = "User_First_Name")
    private String firstName;

    @ColumnInfo(name = "User_Last_Name")
    private String lastName;

    @ColumnInfo(name = "User_Mobile")
    private String mobile;

    // Constructors
    public User(long userId, String userEmail, String password,
                String firstName, String lastName, String mobile) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
    }

    // Getters and Setters
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
}