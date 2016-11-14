package com.codepath.project.android.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

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
}
