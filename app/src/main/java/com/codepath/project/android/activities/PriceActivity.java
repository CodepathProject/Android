package com.codepath.project.android.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.project.android.R;
import com.codepath.project.android.helpers.Constants;
import com.codepath.project.android.model.Feed;
import com.codepath.project.android.model.Product;
import com.codepath.project.android.model.Review;
import com.jjoe64.graphview.GraphView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PriceActivity extends AppCompatActivity {

    @BindView(R.id.tvProductName)
    TextView tvProductName;
    @BindView(R.id.tvProductPrice)
    TextView tvProductPrice;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String productPrice;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.report_prices);
        getSupportActionBar().setElevation(5);
        String productName = getIntent().getStringExtra(Constants.PRODUCT_NAME);
        productPrice = getIntent().getStringExtra(Constants.PRODUCT_PRICE);
        productId = getIntent().getStringExtra(Constants.PRODUCT_ID);
        tvProductName.setText(productName);
        tvProductPrice.setText("$"+productPrice);
    }


    private void updatePrice(String newPrice){
        ParseQuery<Product> query = ParseQuery.getQuery(Product.class);
        query.getInBackground(productId, (product, e) -> {
            if (e == null) {
                product.setPrice(newPrice);
                product.saveInBackground();
                Feed feed = new Feed();
                feed.setType("addPrice");
                feed.setContent(newPrice);
                feed.setFromUser(ParseUser.getCurrentUser());
                feed.setToProduct(product);
                feed.saveInBackground();
                finish();
            } else {
                Toast.makeText(PriceActivity.this, "parse error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onSubmitPrice(View view){
        String price = etPrice.getText().toString();
        if(price.length() == 0){
            Toast.makeText(PriceActivity.this, "Price cannot be null", Toast.LENGTH_SHORT).show();
            return;
        }
        if(new Double(price) < new Double(productPrice)){
            updatePrice(price);
        }
    }
}
