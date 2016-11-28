package com.codepath.project.android.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.codepath.project.android.R;
import com.codepath.project.android.helpers.Constants;

public class PriceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);
        String productName = getIntent().getStringExtra(Constants.PRODUCT_NAME);
        TextView tvProductName = (TextView)findViewById(R.id.tvProductName);
        tvProductName.setText(productName);
    }
}
