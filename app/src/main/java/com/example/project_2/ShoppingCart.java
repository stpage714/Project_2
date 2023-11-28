package com.example.project_2;

import androidx.room.Entity;

import androidx.room.PrimaryKey;

import com.example.project_2.DB.AppDataBase;

    @Entity(tableName = AppDataBase.SHOPPING_CART_TABLE)
    public class ShoppingCart {
        @PrimaryKey(autoGenerate = true)
        private int mCartId;

        private int mProductId;

        public ShoppingCart(int cartId, int productId) {
            mCartId = cartId;
            mProductId = productId;
        }

        public int getCartId() {
            return mCartId;
        }

        public void setCartId(int cartId) {
            mCartId = cartId;
        }

        public int getProductId() {
            return mProductId;
        }

        public void setProductId(int productId) {
            mProductId = productId;
        }

        @Override
        public String toString() {
            return "ShoppingCart{" +
                    "mCartId=" + mCartId +
                    ", mProductId=" + mProductId +
                    '}';
        }
    }
