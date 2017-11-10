package com.unown.finalunown;

/**
 * Created by ErinA on 11/9/2017.
 */

public class Product {

    private String productCategory;
    private Double price;
    private String productName;

    public Product(String productCategory, Double price, String productName){
        this.productCategory = productCategory;
        this.price = price;
        this.productName = productName;
    }
}
