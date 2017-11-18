package com.unown.finalunown;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ErinA on 11/9/2017.
 */

public class Buyer  extends User {
    private List<Product>  recentPurchases;


    public Buyer(List<Product> productsList, String username, String name, String description, double locationLatitude, double locationLongitude, List<Product> recentPurchases, boolean isSeller) {
        super(productsList, username, name, description, locationLatitude, locationLongitude, isSeller);
        this.recentPurchases = recentPurchases;
    }


    public List<Product> getRecentPurchases() {
        return recentPurchases;
    }

    public void setRecentPurchases(ArrayList<Product> recentPurchases) {
        this.recentPurchases = recentPurchases;
    }


}
