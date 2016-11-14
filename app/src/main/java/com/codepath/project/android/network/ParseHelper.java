package com.codepath.project.android.network;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class ParseHelper {

    public static final String PARSE_MASTER_KEY = "myMasterKey";
    public static final String PARSE_APPLICATION_ID = "myAppId";
    public static final String PARSE_SERVER_URL = "https://productreviewsudhish.herokuapp.com/parse/";
    public static final String PARSE_LOGIN_SUCCESS_SNACKTOAST = "Login Successful !";
    public static final String PARSE_LOGIN_FAILED_SNACKTOAST = "Login Failed !";
    public static final String PARSE_SIGNUP_SUCCESS_SNACKTOAST = "Sign Up Successful !";

    public static void newUserSignUp(String name,
                                     String password,
                                     String email) {
        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setUsername(email);
        //user.put("phone", phoneNumber);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }
}
