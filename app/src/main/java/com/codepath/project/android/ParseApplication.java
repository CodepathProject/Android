package com.codepath.project.android;

import android.app.Application;

import com.parse.Parse;
import com.parse.interceptors.ParseLogInterceptor;

/**
 * Created by skasabar on 11/7/16.
 */

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myAppId") // should correspond to APP_ID env variable
                .clientKey(null)  // set explicitly unless clientKey is explicitly configured on Parse server
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://productreviewsudhish.herokuapp.com/parse/").build());
    }
}