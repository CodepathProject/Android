package com.codepath.project.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.project.android.R;
import com.codepath.project.android.adapter.FeedsAdapter;
import com.codepath.project.android.model.AppUser;
import com.codepath.project.android.model.Feed;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserDetailFragment extends Fragment {

    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvUserEmail) TextView tvUserEmail;
    @BindView(R.id.tvUserFirstName) TextView tvUserFirstName;
    @BindView(R.id.rvUserTimeline) RecyclerView rvFeeds;
    @BindView(R.id.followUser) ImageView followUser;

    /*List<Review> reviews;
    UserTimelineAdapter reviewsAdapter;*/
    ArrayList<Feed> feeds = new ArrayList<>();
    FeedsAdapter feedsAdapter;

    String userId;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        userId = getArguments().getString("USER_ID");
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(userId, (object, e) -> {
            if (e == null) {
                if (ParseUser.getCurrentUser() == object) {
                    followUser.setVisibility(View.GONE);
                } else {
                    AppUser currentUser = (AppUser) ParseUser.getCurrentUser();
                    followUser.setOnClickListener(v -> {
                        if(ifListContains(currentUser.getFollowUsers(), object)) {
                            currentUser.removeFollowUser(object);
                            Toast.makeText(getActivity(), "Unfollowed", Toast.LENGTH_SHORT).show();;
                        } else {
                            currentUser.setFollowUsers(object);
                            Toast.makeText(getActivity(), "Followed", Toast.LENGTH_SHORT).show();;
                        }
                        currentUser.saveInBackground();
                    });
                }
                // The query was successful.
                Picasso.with(getContext())
                        .load(object.getString("pictureUrl"))
                        .into(ivProfileImage);
                tvUserFirstName.setText(object.get("firstName").toString());
                //tvUserLastName.setText(ParseUser.getCurrentUser().get("lastName").toString());
                // TODO : update backend to have lastName for User Collection
                tvUserEmail.setText(object.getEmail().toString());
                setUpRecyclerView(object);
            }
        });

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

    /*private void setUpRecyclerView(ParseUser parseUser) {
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
                    *//*Intent intent = new Intent(ProductViewActivity.this, DetailedReviewActivity.class);
                    intent.putExtra("reviewId", reviews.get(position).getObjectId());
                    startActivity(intent);*//*
                }
        );
        //ParseUser currentUser = (AppUser) ParseUser.getCurrentUser();
        ParseQuery<Review> reviewQuery = ParseQuery.getQuery(Review.class);
        reviewQuery.include("user");
        reviewQuery.setLimit(1000);
        //reviewQuery.whereMatchesQuery("_p_user", innerQuery);
        //reviewQuery.whereEqualTo("_p_user", ParseUser.getCurrentUser().getObjectId().toString());
        reviewQuery.findInBackground((reviewList, err) -> {
            if (err == null) {
                *//*ArrayList<Review> filteredReviewList = new ArrayList();
                for (int i = 0; i < reviewList.size(); i++) {
                    String str = reviewList.get(i).getUser().getUsername().toString();
                    Log.d("testsss", str);
                    *//**//*if (reviewObject.getUser().getUsername().toString().equals(currentUser.getUsername())) {
                        filteredReviewList.add(reviewObject);
                    }*//**//*
                }
*//*
                reviews.addAll(reviewList);
                reviewsAdapter.notifyDataSetChanged();
           } else {
                //Toast.makeText(ProductViewActivity.this, "parse error", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
    private void setUpRecyclerView(ParseUser parseUser) {
        feeds = new ArrayList<>();
        feedsAdapter = new FeedsAdapter(getContext(), feeds);
        rvFeeds.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
        rvFeeds.setAdapter(feedsAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mLayoutManager.scrollToPosition(0);
        rvFeeds.setLayoutManager(mLayoutManager);
        rvFeeds.setNestedScrollingEnabled(false);

        ParseUser currentUser = ParseUser.getCurrentUser();
        try {
            currentUser.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        ParseQuery query = ParseQuery.getQuery(Product.class);
//        query.whereEqualTo("name", "Canon EOS Rebel T5 DSLR Digital Camera");
//        query.getFirstInBackground(new GetCallback<ParseObject>() {
//            public void done(ParseObject object, ParseException e) {
//                if (object == null) {
//                    Log.d("score", "The getFirst request failed.");
//                } else {
//                    Log.d("score", "Retrieved the object.");
//                    AppUser user = (AppUser) ParseUser.getCurrentUser();
//                    user.setFollowProducts((Product) object);
//                    user.saveInBackground();
//                }
//            }
//        });

        AppUser user = (AppUser) ParseUser.getCurrentUser();

        List<ParseQuery<Feed>> queries = new ArrayList<>();

        if(user.getFollowUsers() != null) {
            ParseQuery<Feed> followsUserQuery = ParseQuery.getQuery(Feed.class);
            followsUserQuery.whereContainedIn("fromUser", user.getFollowUsers());
            queries.add(followsUserQuery);
        }

        if(user.getFollowProducts() != null) {
            ParseQuery<Feed> followsProductQuery = ParseQuery.getQuery(Feed.class);
            followsProductQuery.whereContainedIn("toProduct", user.getFollowProducts());
            queries.add(followsProductQuery);
        }

        if(queries.size() > 0) {
            ParseQuery<Feed> mainQuery = ParseQuery.or(queries);
            mainQuery.include("fromUser");
            mainQuery.include("toUser");
            mainQuery.include("toProduct");

            mainQuery.findInBackground((feedsList, err) -> {
                if (err == null) {
                    feeds.addAll(feedsList);
                    feedsAdapter.notifyDataSetChanged();
                } else {
                    //Toast.makeText(ProductViewActivity.this, "parse error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean ifListContains(List<ParseUser> userList, ParseUser user) {
        if(userList != null) {
            for (ParseUser u1 : userList) {
//                try {
//                    u1.fetchIfNeeded();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                if (u1.getObjectId().equals(user.getObjectId())) {
                    return true;
                }
            }
        }
        return false;
    }

}
