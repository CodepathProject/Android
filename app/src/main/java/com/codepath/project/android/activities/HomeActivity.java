package com.codepath.project.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.codepath.project.android.R;
import com.codepath.project.android.adapter.CategoryAdapter;
import com.codepath.project.android.adapter.ProductsAdapter;
import com.codepath.project.android.helpers.ItemClickSupport;
import com.codepath.project.android.model.CategoryViewType;
import com.codepath.project.android.model.Product;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_products)
    RecyclerView rvProducts;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.rv_categories)
    RecyclerView rvCategory;
    @BindView(R.id.rv_reviews)
    RecyclerView rvReviews;

    public static final int GRID_ROW_COUNT = 2;

    ProductsAdapter productsAdapter;
    List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        setNavigationDrawer();
        setRecycleView();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        setSearchView(searchItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setRecycleView() {
        LinearLayoutManager layoutManagerProducts
                = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvProducts.setLayoutManager(layoutManagerProducts);
        products = new ArrayList<>();
        productsAdapter = new ProductsAdapter(HomeActivity.this, products, CategoryViewType.HORIZONTAL);
        rvProducts.setAdapter(productsAdapter);

        ItemClickSupport.addTo(rvProducts).setOnItemClickListener(
                (recyclerView, position, v) -> {
                    Intent intent = new Intent(HomeActivity.this, ProductViewActivity.class);
                    intent.putExtra("productId", products.get(position).getObjectId());
                    startActivity(intent);
                }
        );
        productsAdapter.notifyDataSetChanged();

        ParseQuery<Product> query = ParseQuery.getQuery(Product.class);
        query.findInBackground((productList, e) -> {
            products.addAll(productList);
            productsAdapter.notifyDataSetChanged();
            handler.postDelayed(runnable, speedScroll);
            rvProducts.setOnTouchListener((v, event) -> {
                handler.removeCallbacks(runnable);
                v.setOnTouchListener(null);
                return false;
            });
        });

        LinearLayoutManager layoutManagerReviews
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ArrayList<Product> reviews = Product.createReviewList(20);
        ProductsAdapter reviewsAdapter = new ProductsAdapter(this, reviews, CategoryViewType.HORIZONTAL);
        rvReviews.setAdapter(reviewsAdapter);
        rvReviews.setLayoutManager(layoutManagerReviews);

        GridLayoutManager layoutManagerCategory = new GridLayoutManager(this, GRID_ROW_COUNT, GridLayoutManager.HORIZONTAL, false);
        ArrayList<Product> category = Product.createCategoryList(12, CategoryViewType.GRID);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, category, CategoryViewType.GRID);
        rvCategory.setAdapter(categoryAdapter);
        rvCategory.setLayoutManager(layoutManagerCategory);
    }

    private void setSearchView(MenuItem searchItem) {
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("Query submitted");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView tvUserName = (TextView) header.findViewById(R.id.tvUserName);
        tvUserName.setText("Hello, " + ParseUser.getCurrentUser().get("firstName"));
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                ParseUser.logOut();
                Intent intent = new Intent(this, SplashScreenActivity.class);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    final int speedScroll = 1500;
    final Handler handler = new Handler();
    final Runnable runnable = new Runnable() {
        int count = 0;
        @Override
        public void run() {
            if (count < products.size()) {
                rvProducts.scrollToPosition(++count);
                handler.postDelayed(this, speedScroll);
            }
        }
    };
}
