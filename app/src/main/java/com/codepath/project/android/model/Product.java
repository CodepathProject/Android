package com.codepath.project.android.model;

import com.codepath.project.android.data.TestData;

import junit.framework.Test;

import java.util.ArrayList;

/**
 * Created by anmallya on 11/11/2016.
 */

public class Product {

    private String name;
    private String imageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static ArrayList<Product> createProductList(int length){
        ArrayList<Product> productList = new ArrayList<Product>();
        for(int i = 0; i < length; i++){
            Product product = new Product();
            String name = TestData.itemName[i%3];
            String imageUrl = TestData.itemUrl[i%3];
            product.setName(name);
            product.setImageUrl(imageUrl);
            productList.add(product);
        }
        return productList;
    }

    public static ArrayList<Product> createReviewList(int length){
        ArrayList<Product> productList = new ArrayList<Product>();
        for(int i = 0; i < length; i++){
            Product product = new Product();
            String name = TestData.reviewItemName[i%3];
            String imageUrl = TestData.reviewItemUrl[i%3];
            product.setName(name);
            product.setImageUrl(imageUrl);
            productList.add(product);
        }
        return productList;
    }
}
