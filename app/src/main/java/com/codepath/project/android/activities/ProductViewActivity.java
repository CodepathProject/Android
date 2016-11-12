package com.codepath.project.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.project.android.R;
import com.codepath.project.android.fragments.ComposeFragment;
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
    @BindView(R.id.tvBrandName)
    TextView tvBrandName;

    Product product;
    String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        ButterKnife.bind(this);

        if(savedInstanceState == null) {
            productId = getIntent().getStringExtra("productId");

            ParseQuery<Product> query = ParseQuery.getQuery(Product.class);
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
            query.getInBackground(productId, (p, e) -> {
                if (e == null) {
                    product = p;
                    Picasso.with(this).load(product.getImageUrl()).into(ivProductImage);
                    tvProductName.setText(product.getName());
                    tvBrandName.setText(product.getBrand());
                } else {
                    Toast.makeText(ProductViewActivity.this, "parse error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void onAddReview(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("productId", product.getObjectId());

        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        ComposeFragment composeFragment = ComposeFragment.newInstance("Add review");
        composeFragment.setArguments(bundle);
        composeFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        composeFragment.show(fm, "fragment_compose");
    }
}
