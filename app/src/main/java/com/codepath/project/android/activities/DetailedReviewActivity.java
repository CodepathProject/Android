package com.codepath.project.android.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.project.android.R;
import com.codepath.project.android.model.Review;
import com.codepath.project.android.utils.GeneralUtils;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailedReviewActivity extends AppCompatActivity {

    @BindView(R.id.ivProfile)
    ImageView ivProfile;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvReview)
    TextView tvReview;
    @BindView(R.id.rvReviewImage)
    RecyclerView rvReviewImage;

    ImageAdapter imageAdapter;
    List<String> imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_review);
        ButterKnife.bind(this);

        imageUrl = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, imageUrl);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvReviewImage.setLayoutManager(new GridLayoutManager(this, 2));
        rvReviewImage.setAdapter(imageAdapter);



        String reviewId = getIntent().getStringExtra("reviewId");
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.include("user");
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.getInBackground(reviewId, (r, e) -> {
            if (e == null) {
                tvReview.setText(r.getText());
                tvUserName.setText(r.getUser().getString("firstName"));
                Picasso.with(this).load(GeneralUtils.getProfileUrl(r.getUser().getObjectId())).into(ivProfile);

                if(r.getImages() != null && r.getImages().size() > 0) {
                    for(ParseFile pf: r.getImages()) {
                        imageUrl.add(pf.getUrl());
                    }
                }
                imageAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(DetailedReviewActivity.this, "parse error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ImageAdapter  extends
            RecyclerView.Adapter<ImageAdapter.ViewHolder> {

        private List<String> mImages;
        private Context mContext;

        public ImageAdapter(Context context, List<String> images) {
            mContext = context;
            mImages = images;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.ivReviewImage)
            ImageView ivReviewImage;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        @Override
        public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.item_review_image, parent, false);
            return new ViewHolder(contactView);
        }

        @Override
        public void onBindViewHolder(ImageAdapter.ViewHolder viewHolder, int position) {
            String image = mImages.get(position);
            Picasso.with(mContext).load(image).into(viewHolder.ivReviewImage);
        }

        @Override
        public int getItemCount() {
            return mImages.size();
        }
    }
}
