package com.codepath.project.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.codepath.project.android.R;
import com.codepath.project.android.activities.HomeActivity;
import com.codepath.project.android.activities.ProductViewActivity;
import com.codepath.project.android.activities.SplashScreenActivity;
import com.codepath.project.android.adapter.CategoryAdapter;
import com.codepath.project.android.adapter.MyProductsAdapter;
import com.codepath.project.android.adapter.ProductsAdapter;
import com.codepath.project.android.helpers.ItemClickSupport;
import com.codepath.project.android.model.AppUser;
import com.codepath.project.android.model.Category;
import com.codepath.project.android.model.Product;
import com.codepath.project.android.model.ViewType;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by anmallya on 11/19/2016.
 */

public class HomeFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    @BindView(R.id.rv_products)
    RecyclerView rvProducts;
    @BindView(R.id.rv_categories)
    RecyclerView rvCategory;
    @BindView(R.id.rv_reviews)
    RecyclerView rvReviews;

    public static final int GRID_ROW_COUNT = 2;

    ProductsAdapter productsAdapter;
    List<Product> products;


    public static HomeFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        setRecycleView();
    }

    private void setRecycleView() {
        LinearLayoutManager layoutManagerProducts
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvProducts.setLayoutManager(layoutManagerProducts);
        products = new ArrayList<>();
        productsAdapter = new ProductsAdapter(getActivity(), products, ViewType.HORIZONTAL);
        rvProducts.setAdapter(productsAdapter);

        ItemClickSupport.addTo(rvProducts).setOnItemClickListener(
                (recyclerView, position, v) -> {
                    Intent intent = new Intent(getActivity(), ProductViewActivity.class);
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
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        ArrayList<Product> reviews = Product.createReviewList(20);
        ProductsAdapter reviewsAdapter = new ProductsAdapter(getActivity(), reviews, ViewType.HORIZONTAL);
        rvReviews.setAdapter(reviewsAdapter);
        rvReviews.setLayoutManager(layoutManagerReviews);

        GridLayoutManager layoutManagerCategory = new GridLayoutManager(getActivity(), GRID_ROW_COUNT, GridLayoutManager.HORIZONTAL, false);
        ArrayList<Category> categoryList = new ArrayList<Category>();
        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), categoryList, ViewType.GRID);
        Category.createCategoryList(12, ViewType.GRID, categoryList, categoryAdapter);
        rvCategory.setAdapter(categoryAdapter);
        rvCategory.setLayoutManager(layoutManagerCategory);
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
