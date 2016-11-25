package com.codepath.project.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.project.android.R;
import com.codepath.project.android.model.Feed;

import java.util.List;

public class FeedsAdapter extends
        RecyclerView.Adapter<FeedsAdapter.ViewHolder> {

    private List<Feed> mFeeds;
    private Context mContext;

    public FeedsAdapter(Context context, List<Feed> feeds) {
        mFeeds = feeds;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
        }
    }

    @Override
    public FeedsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_feed, parent, false);
        return new ViewHolder(view);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(FeedsAdapter.ViewHolder viewHolder, int position) {
        Feed feed = mFeeds.get(position);
        TextView tvContent = viewHolder.tvContent;
        if(feed.getType().equals("followUser")) {
            tvContent.setText(feed.getFromUser().getUsername() + " followed " + feed.getToUser().getUsername());
            Log.e("quest", "onBindViewHolder: null content" );
        } else if(feed.getType().equals("addPrice")) {
            Log.e("quest", "onBindViewHolder: addPrice" + feed.getContent() );
            tvContent.setText(feed.getFromUser().getUsername() + " reported " + feed.getToProduct().getName() + " price: " + feed.getContent());
        } else if(feed.getType().equals("addReview")) {
            Log.e("quest", "onBindViewHolder: addReview" + feed.getContent() );
            tvContent.setText(feed.getFromUser().getUsername() + " added a review on " + feed.getToProduct().getName() + ": " + feed.getContent());
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mFeeds.size();
    }
}