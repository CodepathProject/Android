package com.codepath.project.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codepath.project.android.R;
import com.codepath.project.android.helpers.CircleTransform;
import com.codepath.project.android.model.AppUser;
import com.codepath.project.android.utils.GeneralUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactFriendsAdapter extends
        RecyclerView.Adapter<ContactFriendsAdapter.ViewHolder> {

    private List<AppUser> mUsers;
    private Context mContext;

    public ContactFriendsAdapter(Context context, List<AppUser> users) {
        mUsers = users;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivFriendProfile;

        public ViewHolder(View itemView) {
            super(itemView);
            ivFriendProfile = (ImageView) itemView.findViewById(R.id.ivFriendProfile);
        }
    }

    @Override
    public ContactFriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_contact_friends, parent, false);
        return new ViewHolder(contactView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ContactFriendsAdapter.ViewHolder viewHolder, int position) {
        AppUser user = mUsers.get(position);
        if (!TextUtils.isEmpty(user.getImage())) {
            Picasso.with(getContext()).load(user.getImage()).transform(new CircleTransform()).into(viewHolder.ivFriendProfile);
        } else {
            Picasso.with(getContext()).load(GeneralUtils.getProfileUrl(user.getObjectId())).transform(new CircleTransform()).into(viewHolder.ivFriendProfile);
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
