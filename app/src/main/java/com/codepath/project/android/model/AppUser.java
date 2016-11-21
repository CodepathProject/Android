package com.codepath.project.android.model;

import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

@ParseClassName("_User")
public class AppUser extends ParseUser {

    public void addShelfProduct(Product product) {
        addUnique("shelfProducts", product);
    }

    public List<Product> getShelfProducts() {
        return (List<Product>) get("shelfProducts");
    }

    public void removeShelfProduct(Product product) {
        removeAll("shelfProducts", Arrays.asList(product));
    }

    public void addWishListProduct(Product product) {
        addUnique("wishListProducts", product);
    }

    public List<Product> getWishListProducts() {
        return (List<Product>) get("wishListProducts");
    }

    public void removeWishListProduct(Product product) {
        removeAll("wishListProducts", Arrays.asList(product));
    }
}