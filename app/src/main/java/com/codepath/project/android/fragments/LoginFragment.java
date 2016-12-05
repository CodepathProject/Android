package com.codepath.project.android.fragments;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.codepath.project.android.R;

import com.codepath.project.android.activities.LoadingActivity;
import com.codepath.project.android.activities.LoginActivity;
import com.codepath.project.android.activities.SignUpActivity;
import com.codepath.project.android.network.ParseHelper;
import com.codepath.project.android.utils.GeneralUtils;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoginFragment extends Fragment {

    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.btnLogIn) Button btnLogIn;
    @BindView(R.id.btnSignUp) Button btnSignUp;
    @BindView(R.id.swSkipLogin) Switch swSkipLogin;
    @BindView(R.id.btnFBLogin) Button btnFBLogin;

    @BindView(R.id.icon2) ImageView icon2;
    @BindView(R.id.icon3) ImageView icon3;
    @BindView(R.id.icon4) ImageView icon4;
    @BindView(R.id.icon5) ImageView icon5;
    @BindView(R.id.icon6) ImageView icon6;
    @BindView(R.id.icon7) ImageView icon7;
    @BindView(R.id.icon8) ImageView icon8;
    @BindView(R.id.icon9) ImageView icon9;
    @BindView(R.id.icon10) ImageView icon10;
    @BindView(R.id.icon11) ImageView icon11;
    @BindView(R.id.icon12) ImageView icon12;
    @BindView(R.id.icon13) ImageView icon13;

    ArrayList<String> imgList = new ArrayList<String>(Arrays.asList(
            "http://pisces.bbystatic.com/image2/BestBuy_US/images/products/5622/5622659_sd.jpg;maxHeight=550;maxWidth=642",
            "http://pisces.bbystatic.com/image2/BestBuy_US/images/products/4973/4973102_sd.jpg;maxHeight=550;maxWidth=642",
            "http://media.webcollage.net/rwvfp/wc/cp/20823286/module/epson/_cp/products/1400158408465/tab-1c43c03e-e20a-40f5-881f-de3791a80df5/resource-32e234b5-37da-4828-a294-1f3cfca34cc8.jpg",
            "https://cnet2.cbsistatic.com/img/z9vZAqaavBGTn0EHO47-dLjfsvM=/770x433/2014/06/05/68b92c57-ed2b-49d8-a7b1-79baf3c1e98a/jbl-clip-product-photos04.jpg",
            "http://www.techspot.com/images/products/loudspeakers/org/2012909520_2104285147_o.jpg",
            "https://media.sweetwater.com/images/items/750/SoundLinkM2C-large.jpg?5858394801",
            "http://www.brandsmartusa.com/images/product/addl/large/23756.jpg?isrc=1111",
            "http://pisces.bbystatic.com/image2/BestBuy_US/images/products/5581/5581238_sd.jpg;maxHeight=550;maxWidth=642",
            "http://pisces.bbystatic.com/image2/BestBuy_US/images/products/8618/8618136_sd.jpg;maxHeight=550;maxWidth=642",
            "http://pisces.bbystatic.com/image2/BestBuy_US/images/products/4909/4909005_sa.jpg",
            "http://www.geniusplanet.com/wp-content/uploads/2016/07/4902309_sa.jpg",
            "https://www.jbhifi.com.au/FileLibrary/ProductResources/Images/151864-L-LO.jpg",
            "http://pisces.bbystatic.com/image2/BestBuy_US/images/products/4562/4562031_sd.jpg;maxHeight=550;maxWidth=642",
            "http://blackfridaytvs.com/wp-content/uploads/2015/11/Samsung-50-Class-49.5-Diag.-LED-1080p-Smart-HDTV-UN50J6200AFXZA.jpg",
            "http://pisces.bbystatic.com/image2/BestBuy_US/images/products/5405/5405401_sd.jpg;maxHeight=550;maxWidth=642",
            "http://pisces.bbystatic.com/image2/BestBuy_US/images/products/4801/4801600_sd.jpg;maxHeight=550;maxWidth=642",
            "http://pisces.bbystatic.com/image2/BestBuy_US/images/products/5327/5327500_sd.jpg;maxHeight=550;maxWidth=642",
            "https://i.ytimg.com/vi/I9xyFnM1BFk/hqdefault.jpg",
            "https://i.ytimg.com/vi/K0ftpT4S6JU/hqdefault.jpg",
            "http://multimedia.bbycastatic.ca/multimedia/products/500x500/104/10448/10448939.jpg",
            "https://shop.usa.canon.com/wcsstore/ExtendedSitesCatalogAssetStore/32251_1_xl.jpg",
            "https://cdn.homeshopping.pk/product_images/s/nikon_d_810_digital_slr_body_1062499__78574_zoom.jpg",
            "http://blog.neocamera.com/images/nikon_d610_back_slant.jpg",
            "https://www.bhphotovideo.com/images/images500x500/sony_a77ii_dslr_camera_with_1068258.jpg",
            "http://www.adorama.com/images/Large/ssg5r5lm03us.jpg",
            "https://cnet2.cbsistatic.com/img/B19xWHLuVKHsdFVFTtwczpRWbYc=/770x433/2015/06/03/18b031a0-f24c-43b6-b77e-660de2e5cb3c/apple-macbook-pro-15-inch-2015-01.jpg",
            "http://store.hp.com/wcsstore/hpusstore/Treatment/Spyder_Gallery_zoom3.jpg",
            "http://images.esellerpro.com/2451/I/286/319/6/Samsung-Galaxy-Tab-A-7_0-front-back.jpg",
            "http://www.product-reviews.net/wp-content/uploads/galaxy-tab-3-lite-7.0.jpg",
            "http://image.priceprice.k-img.com/global/images/product/tablets/Apple_iPad_Pro/Apple_iPad_Pro_L_1.jpg",
            "http://lifeasleels.com/wp-content/uploads/2015/04/Microsoft-Surface-3-10.8-Intel-Atom-64GB.jpg"
    ));

    ArrayList<ImageView> imgHolderList;



    private Unbinder unbinder;

    public static final List<String> mPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
        add("user_friends");
    }};

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        btnLogIn.setOnClickListener(v -> ParseUser.logInInBackground(etEmail.getText().toString(),
                                    etPassword.getText().toString(),
                (user, e) -> {
                    if (user != null) {
                        GeneralUtils.showSnackBar(getView(),
                                ParseHelper.PARSE_LOGIN_SUCCESS_SNACKTOAST,
                                getActivity().getColor(R.color.colorGreen),
                                getActivity().getColor(R.color.colorGray));
                                startNextActivity();
                    } else {
                        GeneralUtils.showSnackBar(getView(),
                                ParseHelper.PARSE_LOGIN_FAILED_SNACKTOAST,
                                getActivity().getColor(R.color.colorRed),
                                getActivity().getColor(R.color.colorGray));
                    }
                }));
        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SignUpActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        btnFBLogin.setOnClickListener(v -> {
            ParseFacebookUtils.logInWithReadPermissionsInBackground(getActivity(), mPermissions, (user, err) -> {
                if (err != null) {
                    Log.d("MyApp", "Uh oh. Error occurred" + err.toString());
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                    startNextActivity();
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                    getUserDetailsFromFB(user);
                } else {
                    Toast.makeText(getActivity(), "Logged in", Toast.LENGTH_SHORT)
                            .show();
                    Log.d("MyApp", "User logged in through Facebook!");
                    startNextActivity();
                }
            });
        });

        swSkipLogin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                startNextActivity();
            }else{
                // dp nothing
            }
        });

        imgHolderList = new ArrayList<ImageView>(Arrays.asList(
                icon2,
                icon3,
                icon4,
                icon5,
                icon6,
                icon7,
                icon8,
                icon9,
                icon10,
                icon11,
                icon12,
                icon13
        ));
        loadControlBackgroundImage();

        return view;
    }

    private void startNextActivity(){
        //Intent intent = new Intent(getActivity(), HomeActivity.class);
        Intent intent = new Intent(getActivity(), LoadingActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getUserDetailsFromFB(ParseUser user) {
        // Suggested by https://disqus.com/by/dominiquecanlas/
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,name,cover,picture.type(large)");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                parameters,
                HttpMethod.GET,
                response -> {
                    try {
                        String email = response.getJSONObject().getString("email");
                        String name = response.getJSONObject().getString("name");
                        JSONObject picture = response.getJSONObject().getJSONObject("picture");
                        JSONObject data = picture.getJSONObject("data");
                        String pictureUrl = data.getString("url");
                        String id = response.getJSONObject().getString("id");

                        if(response.getJSONObject().has("cover")) {
                            JSONObject cover = response.getJSONObject().getJSONObject("cover");
                            user.put("coverUrl", cover.getString("source"));
                        }
                        user.put("fbid", id);
                        user.setEmail(email);
                        user.setUsername(email);
                        user.put("firstName", name);
                        user.put("pictureUrl", pictureUrl);
                        user.save();
                        getFriendsDetailsFromFB(user);
                        startNextActivity();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
        ).executeAsync();
    }

    private void getFriendsDetailsFromFB(ParseUser user) {
        // Suggested by https://disqus.com/by/dominiquecanlas/
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,name,picture");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                response -> {
                    try {
                        JSONArray friendsArray = response.getJSONObject().getJSONArray("data");
                        if(friendsArray != null && friendsArray.length() > 0) {
                            ArrayList<String> ids = new ArrayList<>();
                            for(int i=0; i < friendsArray.length();i++) {
                                ids.add(friendsArray.getJSONObject(i).getString("id"));
                            }
                            user.put("fbFriends", ids);
                            user.save();
                            sendPushNotificationToFriends();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
        ).executeAsync();
    }

    public void sendPushNotificationToFriends() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("a", "b");
        ParseCloud.callFunctionInBackground("facebookNotifyFriends", parameters, (mapObject, e) -> {
            if (e == null){
                System.out.println("sent");
            }
        });
    }

    public void loadControlBackgroundImage(){
        setupImage(icon2);
        setupImage(icon3);
        setupImage(icon4);
        setupImage(icon5);
        setupImage(icon6);
        setupImage(icon7);
        setupImage(icon8);
        setupImage(icon9);
        setupImage(icon10);
        setupImage(icon11);
        setupImage(icon12);
        setupImage(icon13);

    }

    public String getRandomImage()
    {
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(imgList.size());
        String item = imgList.get(index);
        return item;
    }

    public void setupImage(ImageView iv) {
        Picasso.with(getContext())
                .load(getRandomImage())
                .placeholder(R.drawable.placeholder)
                .into(iv);
    }
}
