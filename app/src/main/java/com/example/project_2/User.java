package com.example.project_2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import com.example.project_2.DB.AppDataBase;

@Entity(tableName = AppDataBase.USER_TABLE)

/*,foreignKeys = @ForeignKey(
        entity = ShoppingCart.class,
        parentColumns = "mCartIdShopping",
        childColumns = "mCartIdUser",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE))
 */
public class User {
    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private String mUserName;
    private String mPassword;
    private int mCartIdUser;
    private Boolean misAdmin;

    public User(String userName, String password,  Boolean misAdmin) {
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

    public int getCartIdUser() {
        return mCartIdUser;
    }

    public void setCartIdUser(int cartIdUser) {
        mCartIdUser = cartIdUser;
    }

    @Override
    public String toString() {
        String output;
        output = "User Id : " + getUserId() + "\n";
        output += "User name : " + getUserName() + "\n";
        output += "Password : " + getPassword() + "\n";
        output += "Admin : " + getMisAdmin() + "\n";
        output += "Cart: " + getCartIdUser() + "\n";
        return output;
    }
}
