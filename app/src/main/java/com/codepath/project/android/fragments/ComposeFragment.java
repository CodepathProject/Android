package com.codepath.project.android.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codepath.project.android.R;
import com.codepath.project.android.helpers.BitmapScaler;
import com.codepath.project.android.model.Product;
import com.codepath.project.android.model.Review;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ComposeFragment extends DialogFragment {

    @BindView(R.id.etReviewText) EditText etReviewText;
    @BindView(R.id.btnPost) Button btnPost;
    @BindView(R.id.ivComposeCancel) ImageView ivComposeCancel;
    @BindView(R.id.ivCamera) ImageView ivCamera;
    @BindView(R.id.llImages) LinearLayout llImages;

    List<ParseFile> images;

    Product product;

    private Unbinder unbinder;

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
        unbinder = ButterKnife.bind(this, view);

        images = new ArrayList<>();

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
            if(images.size() > 0) {
                review.setImages(images);
            }
            review.saveInBackground();
            closeKeyboardAndDismiss(view);
        });

        ivCamera.setOnClickListener(v -> onCameraClick());
    }

    public void closeKeyboardAndDismiss(View view) {
        InputMethodManager imm =(InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        dismiss();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onCameraClick() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri("photo-codepath.jpg")); // set the image file name
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, 1034);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1034) {
            if (resultCode == getActivity().RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri("photo-codepath.jpg");
                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, 500);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytearray= stream.toByteArray();

                if (bytearray != null){
                    ParseFile file = new ParseFile("abcd.jpg", bytearray);
                    images.add(file);
                }

                ImageView ivImage = new ImageView(getActivity());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 500);
                layoutParams.setMarginEnd(30);
                ivImage.setLayoutParams(layoutParams);
                ivImage.setImageBitmap(resizedBitmap);
                llImages.addView(ivImage);
            }
        }
    }

    public Uri getPhotoFileUri(String fileName) {
        if (isExternalStorageAvailable()) {
            File mediaStorageDir = new File(
                    getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyCustomApp");
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){}
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ComposeFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}