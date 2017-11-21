package com.unown.finalunown;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ErinA on 11/10/2017.
 */

public class User {

    private List<Product> productsList;
    private String username;
    private String name;
    private String description;
    //private double locationLatitude;
    //private double locationLongitude;
    private String location;
    boolean isSeller;

    //public User(List<Product> productsList, String username, String name, String description, double locationLatitude, double locationLongitude, boolean isSeller) {
    public User(List<Product> productsList, String username, String name, String description, String location, boolean isSeller) {

        this.productsList = productsList;
        this.username = username;
        this.name = name;
        this.description = description;
        this.location = location;
        //this.locationLatitude = locationLatitude;
        //this.locationLongitude = locationLongitude;
        this.isSeller = isSeller;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }

    public List<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<Product> productsList) {
        this.productsList = productsList;
    }

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

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

    public String getLocation() {return location;}

    public void setLocation(String location) {this.location = location;}

    /*

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
    */


}
