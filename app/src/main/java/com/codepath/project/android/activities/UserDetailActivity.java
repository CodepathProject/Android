package com.codepath.project.android.activities;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.project.android.R;
import com.codepath.project.android.fragments.UserDetailFragment;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public class UserDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        String userId = getIntent().getStringExtra("USER_ID");
        Bundle bundle = new Bundle();
        bundle.putString("USER_ID", userId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new UserDetailFragment();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.fragmentUserDetail, fragment).commit();
    }
}

