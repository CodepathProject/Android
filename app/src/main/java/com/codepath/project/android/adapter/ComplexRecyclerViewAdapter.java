package com.codepath.project.android.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.project.android.R;
import com.codepath.project.android.helpers.CircleTransform;
import com.codepath.project.android.model.AppUser;
import com.codepath.project.android.model.Feed;
import com.codepath.project.android.model.Product;
import com.codepath.project.android.utils.GeneralUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Feed> items;
    private List<AppUser> mUsers;
    private Context mContext;

    private final int USER = 0, FEED = 1;

    public ComplexRecyclerViewAdapter(Context context, List<Feed> items, List<AppUser> users) {
        this.items = items;
        mContext = context;
        mUsers = users;
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return USER;
        } else {
            return FEED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case USER:
                View v1 = inflater.inflate(R.layout.horizonal_recommended_friends, viewGroup, false);
                viewHolder = new FriendsFeedHolder(v1);
                break;
            case FEED:
                View v2 = inflater.inflate(R.layout.item_feed, viewGroup, false);
                viewHolder = new FeedHolder(v2);
                break;
            default:
                View v3 = inflater.inflate(R.layout.item_feed, viewGroup, false);
                viewHolder = new FeedHolder(v3);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case USER:
                FriendsFeedHolder vh1 = (FriendsFeedHolder) viewHolder;
                configureFriendsFeedHolder(vh1);
                break;
            case FEED:
                FeedHolder vh2 = (FeedHolder) viewHolder;
                configureFeedHolder(vh2, position);
                break;
            default:
                FeedHolder vh3 = (FeedHolder) viewHolder;
                configureFeedHolder(vh3, position);
                break;
        }
    }

    private void configureFriendsFeedHolder(FriendsFeedHolder vh1) {
        if (mUsers != null && mUsers.size() > 0) {
            RecommendedFriendsAdapter friendsAdapter = new RecommendedFriendsAdapter(mContext, mUsers);
            vh1.rvFriends.setAdapter(friendsAdapter);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            mLayoutManager.scrollToPosition(0);
            vh1.rvFriends.setNestedScrollingEnabled(false);
            vh1.rvFriends.setLayoutManager(mLayoutManager);
            friendsAdapter.notifyDataSetChanged();
        }
    }

    private void configureFeedHolder(FeedHolder viewHolder, int position) {
        Feed feed = items.get(position-1);
        TextView tvContent = viewHolder.tvContent;
        AppUser fromUser = (AppUser) feed.getFromUser();
        Product toProduct = feed.getToProduct();
        AppUser toUser = (AppUser) feed.getToUser();
        viewHolder.rating.setVisibility(View.GONE);
        tvContent.setVisibility(View.GONE);
        String fromUserName = fromUser.getString("firstName").substring(0,1).toUpperCase() + fromUser.getString("firstName").substring(1);
        if(feed.getType().equals("followUser")) {
            viewHolder.tvUserAction.setText(" followed a user");
            String upperString = toUser.getString("firstName").substring(0,1).toUpperCase() + toUser.getString("firstName").substring(1);
            viewHolder.tvDestName.setText(upperString);
            Picasso.with(mContext).load(toUser.getImage()).into(viewHolder.ivProductImage);
        } else if(feed.getType().equals("followProduct")) {
            viewHolder.tvUserAction.setText(" followed a product");
            viewHolder.tvDestName.setText(toProduct.getName());
            Picasso.with(mContext).load(toProduct.getImageUrl()).into(viewHolder.ivProductImage);
        } else if(feed.getType().equals("likeReview")) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(feed.getContent());
            viewHolder.tvUserAction.setText(" liked a review");
            viewHolder.rating.setRating(feed.getRating());
            viewHolder.tvDestName.setText(toProduct.getName());
            Picasso.with(mContext).load(toProduct.getImageUrl()).into(viewHolder.ivProductImage);
        } else if(feed.getType().equals("addPrice")) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText("Product price: $" + feed.getContent());
            viewHolder.tvUserAction.setText(" reported a price");
            viewHolder.tvDestName.setText(toProduct.getName());
            Picasso.with(mContext).load(toProduct.getImageUrl()).into(viewHolder.ivProductImage);
        } else if(feed.getType().equals("addReview")) {
            //fromUserName += " reviewed " + feed.getToProduct().getName();
            tvContent.setText(feed.getContent());
            tvContent.setVisibility(View.VISIBLE);
            viewHolder.tvUserAction.setText(" reviewed a product");
            viewHolder.rating.setVisibility(View.VISIBLE);
            viewHolder.rating.setRating(feed.getRating());
            viewHolder.tvDestName.setText(toProduct.getName());
            Picasso.with(mContext).load(toProduct.getImageUrl()).into(viewHolder.ivProductImage);
        }
        viewHolder.tvUserName.setText(fromUserName);
        viewHolder.tvTime.setText(GeneralUtils.getRelativeTimeAgo(feed.getCreatedAt().toString()));
        setImage(fromUser, viewHolder);
    }

    private void setImage(AppUser fromUser, FeedHolder viewHolder){
        if(!TextUtils.isEmpty(fromUser.getImage())) {
            Picasso.with(mContext).load(fromUser.getImage()).transform(new CircleTransform()).into(viewHolder.ivProfile);
        } else {
            Picasso.with(mContext).load(GeneralUtils.getProfileUrl(fromUser.getObjectId())).transform(new CircleTransform()).into(viewHolder.ivProfile);
        }
    }
}
