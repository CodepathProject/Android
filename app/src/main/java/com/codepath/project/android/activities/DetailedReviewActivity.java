package com.codepath.project.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.project.android.R;
import com.codepath.project.android.helpers.CircleTransform;
import com.codepath.project.android.model.AppUser;
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
    @BindView(R.id.rating)
    RatingBar rating;

    ImageAdapter imageAdapter;
    List<String> imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_review);
        ButterKnife.bind(this);

        imageUrl = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, imageUrl);
        rvReviewImage.setLayoutManager(new GridLayoutManager(this, 2));
        rvReviewImage.setAdapter(imageAdapter);

        String reviewId = getIntent().getStringExtra("reviewId");
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.include("user");
        query.getInBackground(reviewId, (r, e) -> {
            if (e == null) {
                AppUser user = (AppUser) r.getUser();
                tvReview.setText(r.getText());
                String upperString = user.getString("firstName").substring(0,1).toUpperCase() + user.getString("firstName").substring(1);
                tvUserName.setText(upperString);
                rating.setRating(r.getRating());

                if(!TextUtils.isEmpty(user.getImage())) {
                    Picasso.with(this).load(user.getImage()).transform(new CircleTransform()).into(ivProfile);
                } else {
                    Picasso.with(this).load(GeneralUtils.getProfileUrl(user.getObjectId())).transform(new CircleTransform()).into(ivProfile);
                }
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
            Picasso.with(mContext).load(image).placeholder(R.drawable.placeholder).into(viewHolder.ivReviewImage);

            viewHolder.ivReviewImage.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, ImageFullscreenActivity.class);
                String[] images = mImages.toArray(new String[mImages.size()]);
                intent.putExtra("imageSet", images);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return mImages.size();
        }
    }
}
