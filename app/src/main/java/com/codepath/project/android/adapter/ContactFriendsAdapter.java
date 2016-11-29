package com.codepath.project.android.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
        public ImageView ivMessenger;
        public ImageView ivGmail;

        public ViewHolder(View itemView) {
            super(itemView);
            ivFriendProfile = (ImageView) itemView.findViewById(R.id.ivFriendProfile);
            ivMessenger = (ImageView) itemView.findViewById(R.id.ivMessenger);
            ivGmail = (ImageView) itemView.findViewById(R.id.ivGmail);
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
        viewHolder.ivMessenger.setOnClickListener(v -> {
            Uri uri = Uri.parse("fb-messenger://user/");
            uri = ContentUris.withAppendedId(uri, 100014431891986L);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            getContext().startActivity(intent);
        });

        viewHolder.ivGmail.setOnClickListener(v -> {
            Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + user.getUsername()));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Quest");
            intent.putExtra(Intent.EXTRA_TEXT, "Question on product");
            getContext().startActivity(intent);
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
