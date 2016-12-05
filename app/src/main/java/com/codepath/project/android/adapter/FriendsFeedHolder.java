package com.codepath.project.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codepath.project.android.R;

public class FriendsFeedHolder extends RecyclerView.ViewHolder {

    public RecyclerView rvFriends;

    public FriendsFeedHolder(View v) {
        super(v);
        rvFriends = (RecyclerView) v.findViewById(R.id.rvFriends);
    }
}