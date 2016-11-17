package com.codepath.project.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.project.android.R;
import com.codepath.project.android.model.Review;
import com.codepath.project.android.utils.GeneralUtils;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends
        RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<Review> mReviews;
    private Context mContext;

    public ReviewsAdapter(Context context, List<Review> reviews) {
        mReviews = reviews;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivProfile) ImageView ivProfile;
        @BindView(R.id.tvReview) TextView tvReview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_review, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ViewHolder viewHolder, int position) {
        Review review = mReviews.get(position);
        TextView tvReview = viewHolder.tvReview;
        ParseUser user = review.getUser();
        String formattedText = "";
        if(user != null) {
            formattedText += "<b>" + user.getString("firstName") + ": </b> ";
            Picasso.with(getContext()).load(GeneralUtils.getProfileUrl(user.getObjectId())).into(viewHolder.ivProfile);
        }
        formattedText += review.getText();
        tvReview.setText(Html.fromHtml(formattedText));
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

}
