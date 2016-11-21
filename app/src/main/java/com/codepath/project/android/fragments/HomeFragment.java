package com.codepath.project.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.project.android.R;
import com.codepath.project.android.activities.ProductViewActivity;
import com.codepath.project.android.adapter.CategoryAdapter;
import com.codepath.project.android.adapter.ProductsAdapter;
import com.codepath.project.android.helpers.ItemClickSupport;
import com.codepath.project.android.model.Category;
import com.codepath.project.android.model.Product;
import com.codepath.project.android.model.ViewType;
import com.codepath.project.android.network.ParseHelper;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
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
        query.addDescendingOrder("views");
        query.setLimit(20);
        query.findInBackground((productList, e) -> {
            products.addAll(productList);
            productsAdapter.notifyDataSetChanged();
        });

        LinearLayoutManager layoutManagerReviews
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        ArrayList<Product> productsBestRated = new ArrayList<>();
        ProductsAdapter reviewsAdapter = new ProductsAdapter(getActivity(), productsBestRated, ViewType.HORIZONTAL);
        rvReviews.setAdapter(reviewsAdapter);
        rvReviews.setLayoutManager(layoutManagerReviews);
        ItemClickSupport.addTo(rvReviews).setOnItemClickListener(
                (recyclerView, position, v) -> {
                    Intent intent = new Intent(getActivity(), ProductViewActivity.class);
                    intent.putExtra("productId", products.get(position).getObjectId());
                    startActivity(intent);
                }
        );

        ParseQuery<Product> queryByBestRating = ParseQuery.getQuery(Product.class);
        queryByBestRating.addDescendingOrder("averageRating");
        queryByBestRating.setLimit(20);
        queryByBestRating.findInBackground((productList, e) -> {
            productsBestRated.addAll(productList);
            reviewsAdapter.notifyDataSetChanged();
        });

        GridLayoutManager layoutManagerCategory = new GridLayoutManager(getActivity(), GRID_ROW_COUNT, GridLayoutManager.HORIZONTAL, false);
        ArrayList<Category> categoryList = new ArrayList<>();
        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), categoryList, ViewType.GRID);
        ParseHelper.createCategoryListFromProducts(categoryList, categoryAdapter);
        rvCategory.setAdapter(categoryAdapter);
        rvCategory.setLayoutManager(layoutManagerCategory);

    }
}
