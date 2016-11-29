package com.codepath.project.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.codepath.project.android.R;
import com.codepath.project.android.adapter.ImagePagerAdapter;

public class ImageFullscreenActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_fullscreen);
        Intent intent = getIntent();
        String[] images = intent.getStringArrayExtra("imageSet");
        ImagePagerAdapter mCustomPagerAdapter = new ImagePagerAdapter(this, images);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
    }
}
