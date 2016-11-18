package com.codepath.project.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.codepath.project.android.R;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;

public class ImageFullscreenActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_fullscreen);
        PhotoView photoView = (PhotoView) findViewById(R.id.imageView);
        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        Picasso.with(this).load(image).into(photoView);
    }
}
