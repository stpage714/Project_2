package com.example.project_2.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project_2.ProductLog;
import com.example.project_2.ShoppingCart;
import com.example.project_2.User;

import java.util.List;

@Dao //must annotate with Dao here
public interface ProductLogDAO {
    //these are were queries are made; entry point for database
    //created whenever database objects are manipulated
    @Insert //built in crud query
    void insert(ProductLog... productLogs);

    @Update //built in crud query
    void update(ProductLog... productLogs);

    @Delete // built in crud query
    void delete(ProductLog productLogs);
    //only delete single log

    //custom queries below you have to write
    @Query("SELECT * FROM " + AppDataBase.PRODUCT_TABLE + " ORDER BY mDate desc" )
        //custom = @Query
    List<ProductLog> getAllProductLogs();// query returns this

    @Query("SELECT * FROM " + AppDataBase.PRODUCT_TABLE + " WHERE mProductId = :productId")
    List<ProductLog> getProductLogsById(int productId);//only return records where id matches productId

    @Query("UPDATE " + AppDataBase.PRODUCT_TABLE + " SET mDescription = :description, mQuantity = :quantity,mPrice = :price WHERE mProductId = :productId")
    void updateProductLogsById(String description,int quantity, double price, int productId);//only return records where id matches productId

    @Query("UPDATE " + AppDataBase.PRODUCT_TABLE + " SET mQuantity = :quantity WHERE mProductId = :productId")
    void updateProductLogsByQuantityAndId(int productId, int quantity);//only return records where id matches productId


    //create crud for users
    @Insert
    void insert(User...users);

    @Update
    void update(User... users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserName = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserId = :userId")
    User getUserByUserId(int userId);



    //create crud for Shopping Cart
    @Insert
    void insert(ShoppingCart...shoppingCarts);

    @Update
    void update(ShoppingCart...shoppingCarts);

    @Delete
    void delete(ShoppingCart...shoppingCarts);

    @Query("SELECT * FROM " + AppDataBase.SHOPPING_CART_TABLE )
    ShoppingCart allShoppingCarts();

    @Query("SELECT * FROM " + AppDataBase.SHOPPING_CART_TABLE + " WHERE mCartIdShopping = :cartId")
    ShoppingCart getCartByCartId(int cartId);

}
