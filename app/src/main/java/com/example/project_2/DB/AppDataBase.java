package com.example.project_2.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.project_2.ProductLog;
import com.example.project_2.ShoppingCart;
import com.example.project_2.User;

@Database(entities = {ProductLog.class, User.class, ShoppingCart.class}, version = 3)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDataBase extends RoomDatabase {
    //database access point through abstraction layer
    public static final String DATABASE_NAME = "PRODUCT_DATABASE";
    public static final String PRODUCT_TABLE = "PRODUCT_TABLE";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String SHOPPING_CART_TABLE = "SHOPPING_TABLE";
    public abstract ProductLogDAO getProductLogDAO();

}
