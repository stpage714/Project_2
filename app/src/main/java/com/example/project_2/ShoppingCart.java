package com.example.project_2;

import androidx.room.Entity;

import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.project_2.DB.AppDataBase;

    @Entity(tableName = AppDataBase.SHOPPING_CART_TABLE)
/*,foreignKeys = @ForeignKey(
            entity = ProductLog.class,
            parentColumns = "mProductId",
            childColumns = "mProductIdShopping",
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE))
 */
    public class ShoppingCart {
        @PrimaryKey(autoGenerate = true)
        private int mCartIdShopping;
        private int mProductIdShopping;

        public ShoppingCart(int cartIdShopping) {
            mCartIdShopping = cartIdShopping;
        }

        public int getCartIdShopping() {
            return mCartIdShopping;
        }

        public void setCartIdShopping(int cartIdShopping) {
            mCartIdShopping = cartIdShopping;
        }

        public int getProductIdShopping() {
            return mProductIdShopping;
        }

        public void setProductIdShopping(int productIdShopping) {
            mProductIdShopping = productIdShopping;
        }

        @Override
        public String toString() {
            return "ShoppingCart{" +
                    "mCartId=" + mCartIdShopping +
                    ", mProductId=" + mProductIdShopping +
                    '}';
        }
    }
