package com.codepath.project.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.project.android.R;
import com.codepath.project.android.activities.SplashScreenActivity;
import com.codepath.project.android.adapter.MyProductsAdapter;
import com.codepath.project.android.model.AppUser;
import com.codepath.project.android.model.Product;
import com.parse.ParseUser;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ProductsListFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private MyProductsAdapter productsAdapter;
    private List<Product> productsList;

    private int mPage;

    public static ProductsListFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ProductsListFragment fragment = new ProductsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        productsList = new ArrayList<>();
        productsAdapter = new MyProductsAdapter(getActivity(), productsList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvMyProducts);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).build());
        recyclerView.setAdapter(productsAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mLayoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(mLayoutManager);

        AppUser user = (AppUser) ParseUser.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
            startActivity(intent);
        } else {
            if (mPage == 1) {
                if(user.getShelfProducts() != null) {
                    productsList.addAll(user.getShelfProducts());
                }
                productsAdapter.notifyDataSetChanged();
            } else {
                if(user.getWishListProducts() != null) {
                    productsList.addAll(user.getWishListProducts());
                }
                productsAdapter.notifyDataSetChanged();
            }
        }
        return view;
    }
}
