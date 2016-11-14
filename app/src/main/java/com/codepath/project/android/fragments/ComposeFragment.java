package com.codepath.project.android.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.project.android.R;
import com.codepath.project.android.model.Product;
import com.codepath.project.android.model.Review;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ComposeFragment extends DialogFragment {

    private EditText etReviewText;
    Product product;

    public ComposeFragment() {}

    public static ComposeFragment newInstance(String title) {
        ComposeFragment frag = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString("Compose review", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etReviewText = (EditText) view.findViewById(R.id.etReviewText);
        Button btnPost = (Button) view.findViewById(R.id.btnPost);
        ImageView ivComposeCancel = (ImageView) view.findViewById(R.id.ivComposeCancel);

        ivComposeCancel.setOnClickListener(v -> closeKeyboardAndDismiss(view));

        String title = getArguments().getString("title", "Compose review");
        getDialog().setTitle(title);
        etReviewText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        Bundle bundle = this.getArguments();
        String productId = bundle.getString("productId");
        ParseQuery<Product> query = ParseQuery.getQuery(Product.class);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        assert productId != null;
        query.getInBackground(productId.trim(), (p, e) -> {
            if(e == null) {
                product = p;
            } else {
                Toast.makeText(getActivity(), "parse error", Toast.LENGTH_SHORT).show();
            }
        });

        btnPost.setOnClickListener(arg0 -> {
            Review review = new Review();
            review.setText(etReviewText.getText().toString());
            product.incrementReviewCount();
            review.setProduct(product);
            review.setUser(ParseUser.getCurrentUser());
            review.saveInBackground();
            closeKeyboardAndDismiss(view);
        });
    }

    public void closeKeyboardAndDismiss(View view) {
        InputMethodManager imm =(InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        dismiss();
    }
}