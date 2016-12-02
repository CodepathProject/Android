package com.codepath.project.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.codepath.project.android.R;
import com.codepath.project.android.adapter.FollowAdapter;
import com.codepath.project.android.model.AppUser;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowActivity extends AppCompatActivity {

    @BindView(R.id.rvFollowList)
    RecyclerView rvFollows;

    private FollowAdapter followAdapter;
    private ArrayList<ParseUser> followList;
    private AppUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        ButterKnife.bind(this);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        try {
//            user = (AppUser) query.get(getIntent().getStringExtra("USER_ID"));
//            populate(user.getFollowUsers());
////            if(getIntent().getBooleanExtra("isFollower", false)) {
////                populate(user.getFollowUsers());
////            } else {
////                populate(user.getFollowUsers());
////            }
//        } catch (ParseException e) {
//            e.printStackTrace();
      //  }

        String userId = getIntent().getStringExtra("USER_ID");
        query.getInBackground(userId, (object, e) -> {
            user = (AppUser) object;
            if(getIntent().getBooleanExtra("isFollower", false)) {
                populate(user.getFollowUsers());
            } else {
                populate(user.getFollowUsers());
            }
        });
        followList = new ArrayList<>();
        followAdapter = new FollowAdapter(this, followList);
        rvFollows.setAdapter(followAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFollows.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        rvFollows.setLayoutManager(layoutManager);
    }

    private void populate(List<ParseUser> users) {
        if(users != null && users.size() > 0) {
            followList.addAll(users);
            followAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}