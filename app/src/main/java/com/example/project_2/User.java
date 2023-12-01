package com.example.project_2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project_2.DB.AppDataBase;

@Entity(tableName = AppDataBase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private String mUserName;
    private String mPassword;
    private Boolean misAdmin;

    public User(String userName, String password, Boolean misAdmin) {
        mUserName = userName;
        mPassword = password;
        this.misAdmin = misAdmin;
    }

    public Boolean getMisAdmin() {
        return misAdmin;
    }

    public void setMisAdmin(Boolean misAdmin) {
        this.misAdmin = misAdmin;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    @Override
    public String toString() {
        String output;
        output = "User Id : " + getUserId() + "\n";
        output += "User name : " + getUserName() + "\n";
        output += "Password : " + getPassword() + "\n";
        output += "Admin : " + getMisAdmin() + "\n";
        return output;
    }
}
