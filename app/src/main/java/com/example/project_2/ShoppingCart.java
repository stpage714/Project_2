package com.example.project_2;

import androidx.room.Entity;

import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.project_2.DB.AppDataBase;

    @Entity(tableName = AppDataBase.SHOPPING_CART_TABLE)
/*,
            foreignKeys = {
            @ForeignKey(entity = User.class,
            parentColumns = "mUserId",
            childColumns = "mUserId",
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE),
            @ForeignKey(
                    entity = ProductLog.class,
                    parentColumns = "mProductId",
                    childColumns = "mProductId",
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE)})
*/
    public class ShoppingCart {
        @PrimaryKey(autoGenerate = true)
        private int mCartId;
        private int mUserId;
        private int mProductId;

        public ShoppingCart() {
            mCartId = 0;
            mUserId = 0;
            mProductId = 0;
        }

        public ShoppingCart(int cartIdShopping, int userShopping, int productIdShopping) {
            mCartId = cartIdShopping;
            mUserId = userShopping;
            mProductId = productIdShopping;
        }

        public int getCartId() {
            return mCartId;
        }

        public void setCartId(int cartId) {
            mCartId = cartId;
        }

        public int getUserId() {
            return mUserId;
        }

        public void setUserId(int userId) {
            mUserId = userId;
        }

        public int getProductId() {
            return mProductId;
        }

        public void setProductId(int productId) {
            mProductId = productId;
        }

        @Override
        public String toString() {
            String output;
            output = "Cart Id: " + mCartId;
           return output;
        }
    }
