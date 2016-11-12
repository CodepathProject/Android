package com.codepath.project.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.project.android.R;
import com.codepath.project.android.model.Product;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductViewActivity extends AppCompatActivity {

    @BindView(R.id.ivProductImage)
    ImageView ivProductImage;
    @BindView(R.id.tvProductName)
    TextView tvProductName;
    @BindView(R.id.tvBrandName) TextView tvBrandName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        ButterKnife.bind(this);

        String productId = getIntent().getStringExtra("productId");

        ParseQuery<Product> query = ParseQuery.getQuery("Product");
        query.getInBackground(productId, (product, e) -> {
            if (product == null) {
                Log.d("score", "The getFirst request failed.");
            } else {
                Picasso.with(this).load(product.getImageUrl()).into(ivProductImage);
                tvProductName.setText(product.getName());
                tvBrandName.setText(product.getBrand());
            }
        });
    }
}
