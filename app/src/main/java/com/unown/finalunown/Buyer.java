package com.unown.finalunown;

import java.util.ArrayList;

/**
 * Created by ErinA on 11/9/2017.
 */

public class Buyer  extends User {
    private ArrayList<Product>  recentPurchases;


    public Buyer(ArrayList<Product> productsList, String name, String description, double locationLatitude, double locationLongitude, ArrayList<Product> recentPurchases, boolean isSeller) {
        super(productsList, name, description, locationLatitude, locationLongitude, isSeller);
        this.recentPurchases = recentPurchases;
    }


    public ArrayList<Product> getRecentPurchases() {
        return recentPurchases;
    }

    public void setRecentPurchases(ArrayList<Product> recentPurchases) {
        this.recentPurchases = recentPurchases;
    }


}
