package com.codepath.project.android.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.codepath.project.android.R;
import com.codepath.project.android.helpers.Constants;
import com.jjoe64.graphview.GraphView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PriceActivity extends AppCompatActivity {

    @BindView(R.id.tvProductName)
    TextView tvProductName;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.report_prices);
        String productName = getIntent().getStringExtra(Constants.PRODUCT_NAME);
        tvProductName.setText(productName);
    }
}
