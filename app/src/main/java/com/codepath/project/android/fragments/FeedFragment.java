package com.codepath.project.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.project.android.R;
import com.codepath.project.android.adapter.FeedsAdapter;
import com.codepath.project.android.model.AppUser;
import com.codepath.project.android.model.Feed;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FeedFragment extends Fragment {

    @BindView(R.id.rvFeeds)
    RecyclerView rvFeeds;

    ArrayList<Feed> feeds = new ArrayList<>();
    FeedsAdapter feedsAdapter;

    private Unbinder unbinder;

    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        unbinder = ButterKnife.bind(this, view);
        setUpRecyclerView();
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setUpRecyclerView() {
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
}