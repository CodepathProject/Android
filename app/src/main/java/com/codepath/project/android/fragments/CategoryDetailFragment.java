package com.codepath.project.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.codepath.project.android.R;
import com.codepath.project.android.adapter.CategoryAdapter;
import com.codepath.project.android.adapter.ProductsAdapter;
import com.codepath.project.android.model.CategoryViewType;
import com.codepath.project.android.model.Product;

import java.util.ArrayList;

/**
 * Created by anmallya on 11/18/2016.
 */

public class CategoryDetailFragment extends Fragment {

        protected static final String ARG_PAGE = "ARG_PAGE";
        protected ArrayList<Product> productList;
        protected ProductsAdapter productsAdapter;
        protected int mPage;

        public CategoryDetailFragment() {
        }

        public static CategoryDetailFragment newInstance(int page) {
            CategoryDetailFragment fragment = new CategoryDetailFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_PAGE, page);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mPage = getArguments().getInt(ARG_PAGE);
            }
            productList = Product.createReviewList(24);
            productsAdapter = new ProductsAdapter(getActivity(), productList, CategoryViewType.VERTICAL_GRID);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_category_detail, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            setRecyclerView();
        }

        private static final int GRID_ROW_COUNT = 2;

        private void setRecyclerView(){
            RecyclerView rvCategoryDetail = (RecyclerView)getView().findViewById(R.id.rv_category_detail);
            GridLayoutManager layoutManagerCategoryDetail = new GridLayoutManager(getActivity(), GRID_ROW_COUNT, GridLayoutManager.VERTICAL, false);
            rvCategoryDetail.setAdapter(productsAdapter);
            rvCategoryDetail.setLayoutManager(layoutManagerCategoryDetail);
        }
    }
