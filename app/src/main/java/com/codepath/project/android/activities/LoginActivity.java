package com.codepath.project.android.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.codepath.project.android.R;

public class LoginActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_LOGIN = 100;
    public static final int RESULT_CODE_LOGIN_SUCCESS = 0;
    public static final int RESULT_CODE_LOGIN_FAILED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
