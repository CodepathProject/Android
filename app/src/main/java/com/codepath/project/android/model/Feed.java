package com.codepath.project.android.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Activity")
public class Feed extends ParseObject {

    public Feed() {
        super();
    }

    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        put("content", content);
    }

    public String getType() {
        return getString("type");
    }

    public void setType(String type) {
        put("type", type);
    }

    public ParseUser getFromUser() {
        return getParseUser("fromUser");
    }

    public void setFromUser(ParseUser user) {
        put("fromUser", user);
    }

    public ParseUser getToUser() {
        return getParseUser("toUser");
    }

    public Product getToProduct() {
        return (Product) get("toProduct");
    }

    public void setToProduct(Product product) {
        put("toProduct", product);
    }
}
