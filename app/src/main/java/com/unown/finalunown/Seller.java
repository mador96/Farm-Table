package com.unown.finalunown;

import java.util.ArrayList;

/**
 * Created by ErinA on 11/9/2017.
 */

public class Seller  {

    private String name;
    private String description;
    private double locationLatitude;
    private double locationLongitude;
    private ArrayList<Product> sellerProducts;

    public Seller(String name, String description, double locationLatitude, double locationLongitude, ArrayList<Product> sellerProducts){
        this.name = name;
        this.description = description;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.sellerProducts = sellerProducts;
    }

    public ArrayList<Product> getSellerProducts() {
        return sellerProducts;
    }

    public void setSellerProducts(ArrayList<Product> sellerProducts) {
        this.sellerProducts = sellerProducts;
    }

    public double getLocationLongitude() {

        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public double getLocationLatitude() {

        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
