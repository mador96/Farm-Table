package com.unown.finalunown;

import java.util.ArrayList;

/**
 * Created by ErinA on 11/10/2017.
 */

public class User {

    private ArrayList<Product> productsList;
    private String name;
    private String description;
    private double locationLatitude;
    private double locationLongitude;
    boolean isSeller;

    public User(ArrayList<Product> productsList, String name, String description, double locationLatitude, double locationLongitude, boolean isSeller) {
        this.productsList = productsList;
        this.name = name;
        this.description = description;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.isSeller = isSeller;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }

    public ArrayList<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(ArrayList<Product> productsList) {
        this.productsList = productsList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }


}
