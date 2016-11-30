package com.codepath.project.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.project.android.R;
import com.codepath.project.android.activities.DetailedReviewActivity;
import com.codepath.project.android.activities.HomeActivity;
import com.codepath.project.android.activities.ProductViewActivity;
import com.codepath.project.android.activities.SignUpActivity;
import com.codepath.project.android.adapter.ReviewsAdapter;
import com.codepath.project.android.adapter.UserTimelineAdapter;
import com.codepath.project.android.helpers.ItemClickSupport;
import com.codepath.project.android.model.AppUser;
import com.codepath.project.android.model.Review;
import com.codepath.project.android.network.ParseHelper;
import com.codepath.project.android.utils.GeneralUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserDetailFragment extends Fragment {

    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvUserEmail) TextView tvUserEmail;
    @BindView(R.id.tvUserFirstName) TextView tvUserFirstName;
    @BindView(R.id.rvUserTimeline) RecyclerView rvReviews;

    List<Review> reviews;
    UserTimelineAdapter reviewsAdapter;

    String userId;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        userId = getArguments().getString("USER_ID");
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        Picasso.with(getContext())
                .load(GeneralUtils
                .getProfileUrl(ParseUser.getCurrentUser().getObjectId()))
                .into(ivProfileImage);
        tvUserFirstName.setText(ParseUser.getCurrentUser().get("firstName").toString());
        //tvUserLastName.setText(ParseUser.getCurrentUser().get("lastName").toString());
        // TODO : update backend to have lastName for User Collection
        tvUserEmail.setText(ParseUser.getCurrentUser().getEmail().toString());
        setUpRecyclerView();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setUpRecyclerView() {
        reviews = new ArrayList<>();
        reviewsAdapter = new UserTimelineAdapter(getContext(), reviews);
        rvReviews.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
        rvReviews.setAdapter(reviewsAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mLayoutManager.scrollToPosition(0);
        rvReviews.setLayoutManager(mLayoutManager);
        rvReviews.setNestedScrollingEnabled(false);

        ItemClickSupport.addTo(rvReviews).setOnItemClickListener(
                (recyclerView, position, v) -> {
                    /*Intent intent = new Intent(ProductViewActivity.this, DetailedReviewActivity.class);
                    intent.putExtra("reviewId", reviews.get(position).getObjectId());
                    startActivity(intent);*/
                }
        );
        ParseUser currentUser = (AppUser) ParseUser.getCurrentUser();
        ParseQuery<Review> reviewQuery = ParseQuery.getQuery(Review.class);
        reviewQuery.include("user");
        reviewQuery.setLimit(1000);
        //reviewQuery.whereMatchesQuery("_p_user", innerQuery);
        //reviewQuery.whereEqualTo("_p_user", ParseUser.getCurrentUser().getObjectId().toString());
        reviewQuery.findInBackground((reviewList, err) -> {
            if (err == null) {
                /*ArrayList<Review> filteredReviewList = new ArrayList();
                for (int i = 0; i < reviewList.size(); i++) {
                    String str = reviewList.get(i).getUser().getUsername().toString();
                    Log.d("testsss", str);
                    *//*if (reviewObject.getUser().getUsername().toString().equals(currentUser.getUsername())) {
                        filteredReviewList.add(reviewObject);
                    }*//*
                }
*/
                reviews.addAll(reviewList);
                reviewsAdapter.notifyDataSetChanged();
           } else {
                //Toast.makeText(ProductViewActivity.this, "parse error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
