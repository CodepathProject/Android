package com.codepath.project.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.project.android.R;
import com.codepath.project.android.adapter.ReviewsAdapter;
import com.codepath.project.android.fragments.ComposeFragment;
import com.codepath.project.android.model.Product;
import com.codepath.project.android.model.Review;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductViewActivity extends AppCompatActivity {

    @BindView(R.id.ivProductImage)
    ImageView ivProductImage;
    @BindView(R.id.tvProductName)
    TextView tvProductName;
    @BindView(R.id.tvBrandName)
    TextView tvBrandName;
    @BindView(R.id.rbAverageRating)
    RatingBar rbAverageRating;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvReviews)
    RecyclerView rvReviews;

    ReviewsAdapter reviewsAdapter;
    List<Review> reviews;

    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if(savedInstanceState == null) {

            reviews = new ArrayList<>();
            reviewsAdapter = new ReviewsAdapter(this, reviews);
            rvReviews.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
            rvReviews.setAdapter(reviewsAdapter);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mLayoutManager.scrollToPosition(0);
            rvReviews.setLayoutManager(mLayoutManager);

            String productId = getIntent().getStringExtra("productId");
            ParseQuery<Product> query = ParseQuery.getQuery(Product.class);
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
            query.getInBackground(productId, (p, e) -> {
                if (e == null) {
                    product = p;
                    Picasso.with(this).load(product.getImageUrl()).into(ivProductImage);
                    getSupportActionBar().setTitle(product.getName());
                    tvProductName.setText(product.getName());
                    tvBrandName.setText(product.getBrand());
                    rbAverageRating.setRating((float) product.getAverageRating());

                    YouTubePlayerFragment youtubeFragment = (YouTubePlayerFragment)
                            getFragmentManager().findFragmentById(R.id.youtubeFragment);
                    youtubeFragment.initialize("AIzaSyCk70hKeShEmA5EDKGNDDaejcUvdb2pNW0",
                            new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                    YouTubePlayer youTubePlayer, boolean b) {
                                    youTubePlayer.cueVideo(product.getVideo());
                                }
                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                    YouTubeInitializationResult youTubeInitializationResult) {

                                }
                            });

                    ParseQuery<Review> reviewQuery = ParseQuery.getQuery(Review.class);
                    reviewQuery.whereEqualTo("product", product);
                    reviewQuery.findInBackground((reviewList, err) -> {
                        if (err == null) {
                            reviews.addAll(reviewList);
                            reviewsAdapter.notifyDataSetChanged();
//                            handler.postDelayed(runnable, speedScroll);
//                            rvReviews.setOnTouchListener((v, event) -> {
//                                handler.removeCallbacks(runnable);
//                                v.setOnTouchListener(null);
//                                return false;
//                            });
                        } else {
                            Toast.makeText(ProductViewActivity.this, "parse error", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(ProductViewActivity.this, "parse error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onAddReview(View view) {

        ParseUser user = ParseUser.getCurrentUser();

        if(user != null) {
            Bundle bundle = new Bundle();
            bundle.putString("productId", product.getObjectId());
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            ComposeFragment composeFragment = ComposeFragment.newInstance("Add review");
            composeFragment.setArguments(bundle);
            composeFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
            composeFragment.show(fm, "fragment_compose");
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

//    final int speedScroll = 1500;
//    final Handler handler = new Handler();
//    final Runnable runnable = new Runnable() {
//        int count = 0;
//        @Override
//        public void run() {
//            if (count < reviews.size()) {
//                rvReviews.scrollToPosition(++count);
//                handler.postDelayed(this, speedScroll);
//            } else {
//                count = 0;
//                handler.postDelayed(this, speedScroll);
//            }
//        }
//    };
}
