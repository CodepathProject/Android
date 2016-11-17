package com.codepath.project.android.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Review")
public class Review extends ParseObject {

    public Review() {
        super();
    }

    public String getText() {
        return getString("text");
    }

    public void setText(String text) {
        put("text", text);
    }

    public Product getProduct() {
        return (Product) get("product");
    }

    public void setProduct(Product product) {
        put("product", product);
    }

    public ParseUser getUser() {
        return (ParseUser) get("user");
    }

    public void setUser(ParseUser user) {
        put("user", user);
    }

    public List<ParseFile> getImages() {
        return (List<ParseFile>) get("images");
    }

    public void setImages(List<ParseFile> images) {
        for(ParseFile image: images) {
            image.saveInBackground();
        }
        put("images", images);
    }
}