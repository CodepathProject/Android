package com.codepath.project.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.project.android.R;
import com.codepath.project.android.activities.HomeActivity;
import com.codepath.project.android.activities.SignUpActivity;
import com.codepath.project.android.network.ParseHelper;
import com.codepath.project.android.utils.GeneralUtils;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserDetailFragment extends Fragment {

    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvUserEmail) TextView tvUserEmail;
    @BindView(R.id.tvUserFirstName) TextView tvUserFirstName;
    @BindView(R.id.tvUserLastName) TextView tvUserLastName;
    @BindView(R.id.rvUserTimeline) RecyclerView rvUserTimeline;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        Picasso.with(getContext()).load(GeneralUtils.getProfileUrl(ParseUser.getCurrentUser().getObjectId())).into(ivProfileImage);
        tvUserFirstName.setText(ParseUser.getCurrentUser().get("firstName").toString());
        //tvUserLastName.setText(ParseUser.getCurrentUser().get("lastName").toString());
        // TODO : update backend to have lastName for User Collection
        tvUserEmail.setText(ParseUser.getCurrentUser().getEmail().toString());
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

}
