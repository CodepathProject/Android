package com.codepath.project.android.model;

import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

public class AppUser {
    private ParseUser user;

    public AppUser(ParseUser user) {
        this.user = user;
    }

    public ParseUser getParseUser() {
        return this.user;
    }

    public void addShelfProduct(Product product) {
        user.addUnique("shelfProducts", product);
    }

    public List<Product> getShelfProducts() {
        return (List<Product>) user.get("shelfProducts");
    }

    public void removeShelfProduct(Product product) {
        user.removeAll("shelfProducts", Arrays.asList(product));
    }

    public void addWishListProduct(Product product) {
        user.addUnique("wishListProducts", product);
    }

    public List<Product> getWishListProducts() {
        return (List<Product>) user.get("wishListProducts");
    }

    public void removeWishListProduct(Product product) {
        user.removeAll("wishListProducts", Arrays.asList(product));
    }

    public void save() {
        user.saveInBackground();
    }
}
