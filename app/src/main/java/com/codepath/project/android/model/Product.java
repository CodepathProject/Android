package com.codepath.project.android.model;

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
            product.setName("HP Notebook");
            product.setImageUrl("http://ssl-product-images.www8-hp.com/digmedialib/prodimg/lowres/c05218075.png");
            productList.add(product);
        }
        return productList;
    }
}
