package com.unown.finalunown;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ErinA on 11/9/2017.
 */

public class Seller extends User {

    double totalSales;
    int numberSales;



    public Seller(List<Product> ProductsList, String name, String description, double locationLatitude, double locationLongitude, double totalSales, int numberSales, boolean isSeller){
        super(ProductsList, description, name, locationLatitude, locationLongitude, isSeller);
        this.totalSales = totalSales;
        this.numberSales = numberSales;

    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public int getNumberSales() {
        return numberSales;
    }

    public void setNumberSales(int numberSales) {
        this.numberSales = numberSales;
    }
}
