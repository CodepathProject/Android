package com.codepath.project.android.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.project.android.R;
import com.codepath.project.android.adapter.ReviewsAdapter;
import com.codepath.project.android.fragments.ComposeFragment;
import com.codepath.project.android.helpers.ItemClickSupport;
import com.codepath.project.android.model.AppUser;
import com.codepath.project.android.model.Product;
import com.codepath.project.android.model.Review;
import com.parse.ParseException;
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
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.btnShelf)
    Button btnShelf;
    @BindView(R.id.btnWishlist)
    Button btnWishlist;

    ReviewsAdapter reviewsAdapter;
    List<Review> reviews;

    Product product;
    AppUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        ButterKnife.bind(this);

        user = (AppUser) ParseUser.getCurrentUser();

        setUpToolbar();
        if(savedInstanceState == null) {
            setUpRecyclerView();

            String productId = getIntent().getStringExtra("productId");
            ParseQuery<Product> query = ParseQuery.getQuery(Product.class);
            query.getInBackground(productId, (p, e) -> {
                if (e == null) {
                    product = p;
                    collapsingToolbar.setTitle(p.getName());
                    collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);

                    Picasso.with(this).load(product.getImageUrl()).into(ivProductImage);

                    ivProductImage.setOnClickListener(v -> {
                        Intent intent = new Intent(this, ImageFullscreenActivity.class);
                        String image = product.getImageUrl();
                        intent.putExtra("image", image);
                        startActivity(intent);
                    });

                    getSupportActionBar().setTitle(product.getName());
                    tvProductName.setText(product.getName());
                    tvBrandName.setText(product.getBrand());
                    rbAverageRating.setRating((float) product.getAverageRating());

//                    YouTubePlayerFragment youtubeFragment = (YouTubePlayerFragment)
//                            getFragmentManager().findFragmentById(R.id.youtubeFragment);
//                    youtubeFragment.initialize("AIzaSyCk70hKeShEmA5EDKGNDDaejcUvdb2pNW0",
//                            new YouTubePlayer.OnInitializedListener() {
//                                @Override
//                                public void onInitializationSuccess(YouTubePlayer.Provider provider,
//                                                                    YouTubePlayer youTubePlayer, boolean b) {
//                                    //youTubePlayer.cueVideo(product.getVideo());
//                                    youTubePlayer.loadVideo(product.getVideo());
//                                }
//                                @Override
//                                public void onInitializationFailure(YouTubePlayer.Provider provider,
//                                                                    YouTubeInitializationResult youTubeInitializationResult) {
//
//                                }
//                            });

                    ParseQuery<Review> reviewQuery = ParseQuery.getQuery(Review.class);
                    reviewQuery.include("user");
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
                    product.incrementViews();
                    product.saveInBackground();
                    setUpShelfWishClickListener();

                } else {
                    Toast.makeText(ProductViewActivity.this, "parse error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setUpShelfWishClickListener() {

        List<Product> productList;

        if(user != null) {
            productList = user.getShelfProducts();
            if(ifListContains(productList)) {
                btnShelf.setText("Remove from shelf");
            }
            productList = user.getWishListProducts();
            if(ifListContains(productList)) {
                btnWishlist.setText("Remove from wishlist");
            }
        }

        btnShelf.setOnClickListener(v -> {
            if(ParseUser.getCurrentUser() != null) {
                List<Product> productShelfList = user.getShelfProducts();
                if(ifListContains(productShelfList)) {
                    user.removeShelfProduct(product);
                    btnShelf.setText("Add to shelf");
                    Toast.makeText(this, "Removed from shelf", Toast.LENGTH_SHORT).show();
                } else {
                    user.addShelfProduct(product);
                    btnShelf.setText("Remove from shelf");
                    Toast.makeText(this, "Added to shelf", Toast.LENGTH_SHORT).show();
                }
                try {
                    user.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnWishlist.setOnClickListener(v -> {
            if(user != null) {
                List<Product> productWishList = user.getWishListProducts();
                if(ifListContains(productWishList)) {
                    user.removeWishListProduct(product);
                    btnWishlist.setText("Add to wishlist");
                    Toast.makeText(this, "Removed from wishlist", Toast.LENGTH_SHORT).show();
                } else {
                    user.addWishListProduct(product);
                    btnWishlist.setText("Remove from wishlist");
                    Toast.makeText(this, "Added to wishlist", Toast.LENGTH_SHORT).show();
                }
                try {
                    user.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean ifListContains(List<Product> productList) {
        if(productList != null) {
            for (Product prod : productList) {
                if (prod.getObjectId().equals(product.getObjectId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void setUpRecyclerView() {
        reviews = new ArrayList<>();
        reviewsAdapter = new ReviewsAdapter(this, reviews);
        rvReviews.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        rvReviews.setAdapter(reviewsAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mLayoutManager.scrollToPosition(0);
        rvReviews.setLayoutManager(mLayoutManager);
        rvReviews.setNestedScrollingEnabled(false);

        ItemClickSupport.addTo(rvReviews).setOnItemClickListener(
                (recyclerView, position, v) -> {
                    Intent intent = new Intent(ProductViewActivity.this, DetailedReviewActivity.class);
                    intent.putExtra("reviewId", reviews.get(position).getObjectId());
                    startActivity(intent);
                }
        );
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void onShowPlot(View view){
        Intent i = new Intent(this, PlotActivity.class);
        startActivity(i);
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
