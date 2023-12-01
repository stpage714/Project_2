package com.example.project_2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project_2.DB.AppDataBase;

import java.util.Date;


@Entity(tableName = AppDataBase.PRODUCT_TABLE)
public class ProductLog {

    @PrimaryKey(autoGenerate = true)
    //declare primary key for table
    private int mProductId;//mProductId is primary key


    private String mDescription;
    private int mQuantity;
    private double mPrice;
    private Date mDate;

    public ProductLog(String description, int quantity, double price) {
        //constructor
        mDescription = description;
        mQuantity = quantity;
        mPrice = price;
        mDate = new Date();
    }

    public int getProductId() {
        return mProductId;
    }

    public void setProductId(int productId) {
        mProductId = productId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }


    @Override
    public String toString() {
        String output;
        output = "Product Id: " + mProductId;
        output += "\n";
        output += "Description: " + mDescription;
        output += "\n";
        output += "Quantity: " + mQuantity;
        output += "\n";
        output += "Price: " + mPrice;
        output += "\n";
        output += getDate();
        output += "\n";
        return output;
    }

}//end productlog
