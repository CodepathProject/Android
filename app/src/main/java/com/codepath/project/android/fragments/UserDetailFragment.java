package com.codepath.project.android.fragments;

import android.content.Intent;
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
import com.codepath.project.android.activities.FollowActivity;
import com.codepath.project.android.activities.ProductViewActivity;
import com.codepath.project.android.adapter.FeedsAdapter;
import com.codepath.project.android.helpers.ItemClickSupport;
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
    @BindView(R.id.tvUserFirstName) TextView tvUserFirstName;
    @BindView(R.id.tvFollowing) TextView tvFollowing;
    @BindView(R.id.tvFollowers) TextView tvFollowers;
    @BindView(R.id.rvUserTimeline) RecyclerView rvFeeds;
    @BindView(R.id.followUser) ImageView followUser;

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
                if (ParseUser.getCurrentUser().getObjectId().equals(object.getObjectId())) {
                    followUser.setVisibility(View.GONE);
                } else {
                    AppUser currentUser = (AppUser) ParseUser.getCurrentUser();
                    followUser.setOnClickListener(v -> {
                        if(ifListContains(currentUser.getFollowUsers(), object)) {
                            currentUser.removeFollowUser(object);
                            Toast.makeText(getActivity(), "Unfollowed", Toast.LENGTH_SHORT).show();
                        } else {
                            currentUser.setFollowUsers(object);
                            Toast.makeText(getActivity(), "Followed", Toast.LENGTH_SHORT).show();
                        }
                        currentUser.saveInBackground();
                    });
                }
                Picasso.with(getContext())
                        .load(object.getString("pictureUrl"))
                        .into(ivProfileImage);
                tvUserFirstName.setText(object.get("firstName").toString());
                AppUser currentUser = (AppUser) object;
                if(currentUser.getFollowUsers() != null && currentUser.getFollowUsers().size() > 0) {
                    tvFollowing.setText(currentUser.getFollowUsers().size() + " FOLLOWING");
                    tvFollowing.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), FollowActivity.class);
                        intent.putExtra("USER_ID", currentUser.getObjectId());
                        startActivity(intent);
                    });
                } else {
                    tvFollowing.setText("0 FOLLOWING");
                }

                setUpRecyclerView(object);
            }
        });

        ItemClickSupport.addTo(rvFeeds).setOnItemClickListener(
                (rview, position, v) -> {
                    Intent intent = new Intent(getActivity(), ProductViewActivity.class);
                    intent.putExtra("productId", feeds.get(position).getToProduct().getObjectId());
                    startActivity(intent);
                }
        );

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

    private void setUpRecyclerView(ParseUser parseUser) {
        feeds = new ArrayList<>();
        feedsAdapter = new FeedsAdapter(getContext(), feeds);
        rvFeeds.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).sizeResId(R.dimen.feed_divider).build());
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

        ParseQuery<Feed> query = ParseQuery.getQuery(Feed.class);
        query.whereEqualTo("fromUser", parseUser);
        query.include("fromUser");
        query.include("toUser");
        query.include("toProduct");
        query.setLimit(10);
        query.findInBackground((feedsList, err) -> {
            if (err == null) {
                feeds.addAll(feedsList);
                feedsAdapter.notifyDataSetChanged();
            }
        });
    }

    private boolean ifListContains(List<ParseUser> userList, ParseUser user) {
        if(userList != null) {
            for (ParseUser u1 : userList) {
                if (u1.getObjectId().equals(user.getObjectId())) {
                    return true;
                }
            }
        }
        return false;
    }

}
