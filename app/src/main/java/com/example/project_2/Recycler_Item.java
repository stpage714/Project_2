package com.example.project_2;

public class Recycler_Item {
    private int mImageResource;
    private String mDescription;
    private String mQuantity;
    private String mPrice;
    private int mID;
    public Recycler_Item(int imageResource, String description, String quantity, String price, int id) {
        mImageResource = imageResource;
        mDescription = description;
        mQuantity = quantity;
        mPrice = price;
        mID = id;
    }
    public void changemDescription(String text){
        mDescription = text;
    }

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public void setImageResource(int imageResource) {
        mImageResource = imageResource;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public void setQuantity(String quantity) {
        mQuantity = quantity;
    }
}
