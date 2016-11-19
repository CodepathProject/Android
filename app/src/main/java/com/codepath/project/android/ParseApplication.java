package com.codepath.project.android;

import android.app.Application;

import com.codepath.project.android.model.Category;
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
        ParseObject.registerSubclass(Category.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myAppId")
                .clientKey(null)
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://codepath.herokuapp.com/parse/").build());

//        byte[] data = "Working at Parse is great!".getBytes();
//        ParseFile file = new ParseFile("resume.txt", data);
//        file.saveInBackground();
//
//        ParseObject jobApplication = new ParseObject("JobApplication");
//        jobApplication.put("applicantName", "Joe Smith");
//        jobApplication.put("applicantResumeFile", file);
//        jobApplication.saveInBackground();
    }
}