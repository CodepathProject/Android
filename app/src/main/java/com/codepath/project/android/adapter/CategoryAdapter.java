package com.codepath.project.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.project.android.R;
import com.codepath.project.android.activities.CategoryActivity;
import com.codepath.project.android.model.CategoryViewType;
import com.codepath.project.android.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends
        RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Product> mProducts;
    private Context mContext;
    private CategoryViewType mItemLayoutType;

    public CategoryAdapter(Context context, List<Product> products, CategoryViewType itemLayoutType) {
        mProducts = products;
        mContext = context;
        mItemLayoutType = itemLayoutType;
    }

    private Context getContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_category_name) TextView tvCategoryName;
        @BindView(R.id.iv_category) ImageView ivCategoryImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = null;
        if(mItemLayoutType.equals(CategoryViewType.GRID)){
            contactView = inflater.inflate(R.layout.item_category, parent, false);
        }else if(mItemLayoutType.equals(CategoryViewType.VERTICAL)){
            contactView = inflater.inflate(R.layout.item_category_vertical, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder viewHolder, int position) {
        Product product = mProducts.get(position);
        TextView tvProductName = viewHolder.tvCategoryName;
        tvProductName.setText(product.getName());
        ImageView ivProductImage = viewHolder.ivCategoryImage;
        Picasso.with(getContext()).load(product.getImageUrl()).into(ivProductImage);

        if(mItemLayoutType.equals(CategoryViewType.GRID)){
            if(position == (getItemCount() - 1)) {
                ivProductImage.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), CategoryActivity.class);
                    getContext().startActivity(intent);
                });
                tvProductName.setVisibility(View.INVISIBLE);
            } else{
                tvProductName.setVisibility(View.VISIBLE);
            }
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mProducts.size();
    }
}