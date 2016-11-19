package com.codepath.project.android.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.codepath.project.android.R;
import com.codepath.project.android.adapter.CategoryAdapter;
import com.codepath.project.android.model.ViewType;
import com.codepath.project.android.model.Product;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends AppCompatActivity {

    @BindView(R.id.rv_categories)
    RecyclerView rvCategory;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Categories");
        setupRecyclerView();
    }

    private void setupRecyclerView(){
        ArrayList<Product> category = Product.createCategoryList(12, ViewType.VERTICAL);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, category, ViewType.VERTICAL);
        rvCategory.setAdapter(categoryAdapter);
        rvCategory.setLayoutManager(new LinearLayoutManager(this));
    }
}
