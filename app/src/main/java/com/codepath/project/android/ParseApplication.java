package com.codepath.project.android;

import android.app.Application;

import com.codepath.project.android.model.Product;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.interceptors.ParseLogInterceptor;

import java.util.List;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Product.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myAppId")
                .clientKey(null)
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://codepath.herokuapp.com/parse/").build());

        ParseQuery<Product> query = ParseQuery.getQuery("Product");

        query.findInBackground(new FindCallback<Product>() {
            public void done(List<Product> productList, ParseException e) {
                System.out.println(productList);
            }
        });

    }
}