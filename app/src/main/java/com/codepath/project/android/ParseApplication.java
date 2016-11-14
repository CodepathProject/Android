package com.codepath.project.android;

import android.app.Application;

import com.codepath.project.android.model.Product;
import com.codepath.project.android.model.Review;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.interceptors.ParseLogInterceptor;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Product.class);
        ParseObject.registerSubclass(Review.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myAppId")
                .clientKey(null)
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://codepath.herokuapp.com/parse/").build());
    }
}