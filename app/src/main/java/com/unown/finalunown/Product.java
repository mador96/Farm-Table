package com.unown.finalunown;

/**
 * Created by ErinA on 11/9/2017.
 */

public class Product {

    private String productCategory;
    private Double price;
    private String productName;

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

    public Product(String productCategory, Double price, String productName){
        this.productCategory = productCategory;
        this.price = price;
        this.productName = productName;

    }
}
