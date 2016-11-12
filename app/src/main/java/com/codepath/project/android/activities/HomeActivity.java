package com.codepath.project.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.project.android.R;
import com.codepath.project.android.adapter.ProductsAdapter;
import com.codepath.project.android.helpers.ItemClickSupport;
import com.codepath.project.android.model.Product;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    RecyclerView rvProducts;
    ProductsAdapter productsAdapter;
    List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        setNavigationDrawer();
        setRecycleView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    private void setRecycleView(){
        rvProducts = (RecyclerView) findViewById(R.id.rv_products);
        LinearLayoutManager layoutManagerProducts
                = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvProducts.setLayoutManager(layoutManagerProducts);
        products = new ArrayList<>();
        productsAdapter = new ProductsAdapter(HomeActivity.this, products);
        rvProducts.setAdapter(productsAdapter);
        productsAdapter.notifyDataSetChanged();

        ParseQuery<Product> query = ParseQuery.getQuery("Product");
        query.findInBackground(new FindCallback<Product>() {
            public void done(List<Product> productList, ParseException e) {
                products.addAll(productList);
                productsAdapter.notifyDataSetChanged();
                ItemClickSupport.addTo(rvProducts).setOnItemClickListener(
                        (recyclerView, position, v) -> {
                            Intent intent = new Intent(HomeActivity.this, ProductViewActivity.class);
                            intent.putExtra("productId", productList.get(position).getObjectId());
                            startActivity(intent);
                        }
                );
            }
        });


        RecyclerView rvReviews = (RecyclerView) findViewById(R.id.rv_reviews);
        LinearLayoutManager layoutManagerReviews
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ArrayList<Product> reviews = Product.createReviewList(20);
        ProductsAdapter reviewsAdapter = new ProductsAdapter(this, reviews);
        rvReviews.setAdapter(reviewsAdapter);
        rvReviews.setLayoutManager(layoutManagerReviews);
    }

    private void setSearchView(MenuItem searchItem){
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

    private void setNavigationDrawer(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
