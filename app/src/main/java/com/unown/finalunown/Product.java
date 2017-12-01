package com.unown.finalunown;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ErinA on 11/9/2017.
 */

public class Product implements Serializable{

    private String productCategory;
    private Double price;
    private String productName;
    private int quantity;
    private String owner;

    /*@Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel out, int flags){
        out.writeString(productCategory);
        out.writeDouble(price);
        out.writeString(productName);
        out.writeString(owner);
        out.writeInt(quantity);
    }
    private Product(Parcel in){
        this.productCategory = in.readString();
        this.price = in.readDouble();
        this.productName = in.readString();
        this.quantity = in.readInt();
        this.owner = in.readString();
    }
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }
        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    */

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }


    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product(String productCategory, Double price, String productName, int quantity, String owner){
        this.productCategory = productCategory;
        this.price = price;
        this.productName = productName;
        this.quantity = quantity;
        this.owner = owner;

    }

    public ArrayList<Product> populateArray(ArrayList<Product> beginPantry) {
        Product apple = new Product("fruit", 0.30, "apple", 1, "");
        Product carrot = new Product("vegetable", 0.45, "carrot", 1, "");

        return beginPantry;

    }
}