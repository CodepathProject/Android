package com.codepath.project.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.project.android.R;
import com.codepath.project.android.activities.UserDetailActivity;
import com.codepath.project.android.helpers.CircleTransform;
import com.codepath.project.android.model.AppUser;
import com.codepath.project.android.utils.GeneralUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecommendedFriendsAdapter extends
        RecyclerView.Adapter<RecommendedFriendsAdapter.ViewHolder> {

    private List<AppUser> mUsers;
    private Context mContext;

    public RecommendedFriendsAdapter(Context context, List<AppUser> users) {
        mUsers = users;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivFriendProfile;
        public TextView tvFriendName;
        public Button btnFollow;

        public ViewHolder(View itemView) {
            super(itemView);
            ivFriendProfile = (ImageView) itemView.findViewById(R.id.ivFriendProfile);
            tvFriendName = (TextView) itemView.findViewById(R.id.tvFriendName);
            btnFollow = (Button) itemView.findViewById(R.id.btnFollow);
        }
    }

    @Override
    public RecommendedFriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_recommended_friends, parent, false);
        return new ViewHolder(contactView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecommendedFriendsAdapter.ViewHolder viewHolder, int position) {
        AppUser user = mUsers.get(position);
        user.fetchIfNeededInBackground((object, e) -> {
            String upperString = user.getString("firstName").substring(0,1).toUpperCase() + user.getString("firstName").substring(1);
            viewHolder.tvFriendName.setText(upperString);
            if (!TextUtils.isEmpty(user.getImage())) {
                Picasso.with(getContext()).load(user.getImage()).transform(new CircleTransform()).into(viewHolder.ivFriendProfile);
            } else {
                Picasso.with(getContext()).load(GeneralUtils.getProfileUrl(user.getObjectId())).transform(new CircleTransform()).into(viewHolder.ivFriendProfile);
            }
            viewHolder.ivFriendProfile.setOnClickListener(v ->
                    startUserDetailActivity(user.getObjectId())
            );
            viewHolder.btnFollow.setOnClickListener(v -> {
                mUsers.remove(position);
                this.notifyItemRemoved(position);
                notifyItemRangeChanged(position, mUsers.size());
            });
        });
    }

    public void startUserDetailActivity(String userId) {
        Intent intent = new Intent(getContext(), UserDetailActivity.class);
        intent.putExtra("USER_ID", userId);
        getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
