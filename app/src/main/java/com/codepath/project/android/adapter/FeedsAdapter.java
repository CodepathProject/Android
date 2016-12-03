package com.codepath.project.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.project.android.R;
import com.codepath.project.android.helpers.CircleTransform;
import com.codepath.project.android.model.AppUser;
import com.codepath.project.android.model.Feed;
import com.codepath.project.android.model.Product;
import com.codepath.project.android.utils.GeneralUtils;
import com.squareup.picasso.Picasso;

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
        public ImageView ivProfile;
        public TextView tvProductName;
        public TextView tvUserName;
        public RatingBar rating;
        public ImageView ivProductImage;
        public TextView tvTime;
        public TextView tvUserAction;

        public ViewHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            rating = (RatingBar) itemView.findViewById(R.id.rating);
            ivProductImage = (ImageView) itemView.findViewById(R.id.ivProductImage);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            tvUserAction  = (TextView) itemView.findViewById(R.id.tvUserAction);
        }
    }

    @Override
    public FeedsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_feed2, parent, false);
        return new ViewHolder(view);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(FeedsAdapter.ViewHolder viewHolder, int position) {
        Feed feed = mFeeds.get(position);
        TextView tvContent = viewHolder.tvContent;
        AppUser fromUser = (AppUser) feed.getFromUser();
        Product toProduct = feed.getToProduct();
        viewHolder.rating.setVisibility(View.GONE);
        String fromUserName = fromUser.getString("firstName").substring(0,1).toUpperCase() + fromUser.getString("firstName").substring(1);
        if(feed.getType().equals("followUser")) {
            tvContent.setText(((AppUser) feed.getFromUser()).getFirstName() + " followed " + feed.getToUser().getUsername());
        } else if(feed.getType().equals("addPrice")) {
            //fromUserName += " reported price on " + feed.getToProduct().getName();
            tvContent.setText("Product price: $" + feed.getContent());
            viewHolder.tvUserAction.setText(" reported a price");
        } else if(feed.getType().equals("addReview")) {
            //fromUserName += " reviewed " + feed.getToProduct().getName();
            tvContent.setText(feed.getContent());
            viewHolder.tvUserAction.setText(" reviewed a product");
            viewHolder.rating.setVisibility(View.VISIBLE);
            viewHolder.rating.setRating(feed.getRating());
        }
        viewHolder.tvProductName.setText(toProduct.getName());
        viewHolder.tvUserName.setText(fromUserName);
        viewHolder.tvTime.setText(GeneralUtils.getRelativeTimeAgo(feed.getCreatedAt().toString()));
        setImage(fromUser, toProduct, viewHolder);
    }

    private void setImage(AppUser fromUser, Product toProduct, FeedsAdapter.ViewHolder viewHolder){
        if(!TextUtils.isEmpty(fromUser.getImage())) {
            Picasso.with(getContext()).load(fromUser.getImage()).transform(new CircleTransform()).into(viewHolder.ivProfile);
        } else {
            Picasso.with(getContext()).load(GeneralUtils.getProfileUrl(fromUser.getObjectId())).transform(new CircleTransform()).into(viewHolder.ivProfile);
        }
        if(toProduct != null) {
            Picasso.with(getContext()).load(toProduct.getImageUrl()).into(viewHolder.ivProductImage);
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mFeeds.size();
    }
}