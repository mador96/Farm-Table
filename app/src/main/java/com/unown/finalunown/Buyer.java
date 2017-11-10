package com.unown.finalunown;

import java.util.ArrayList;

/**
 * Created by ErinA on 11/9/2017.
 */

public class Buyer  {
    private ArrayList<Product>  recentPurchases;
    private String name;
    private String description;

    public ArrayList<Product> getRecentPurchases() {
        return recentPurchases;
    }

    public void setRecentPurchases(ArrayList<Product> recentPurchases) {
        this.recentPurchases = recentPurchases;
    }

    public Buyer(String name, String description, ArrayList<Product> recentPurchases) {
        this.name = name;
        this.description = description;
        this.recentPurchases = recentPurchases;
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
