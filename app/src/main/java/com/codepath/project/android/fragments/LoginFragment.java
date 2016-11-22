package com.codepath.project.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.codepath.project.android.R;
import com.codepath.project.android.activities.HomeActivity;
import com.codepath.project.android.activities.SignUpActivity;
import com.codepath.project.android.network.ParseHelper;
import com.codepath.project.android.utils.GeneralUtils;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    private Unbinder unbinder;

    public static final List<String> mPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
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
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
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
        });

        btnFBLogin.setOnClickListener(v -> {
            ParseFacebookUtils.logInWithReadPermissionsInBackground(getActivity(), mPermissions, (user, err) -> {
                if (err != null) {
                    Log.d("MyApp", "Uh oh. Error occurred" + err.toString());
                } else if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                    getUserDetailsFromFB(user);
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Logged in", Toast.LENGTH_SHORT)
                            .show();
                    Log.d("MyApp", "User logged in through Facebook!");
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                }
            });
        });

        swSkipLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // go to home
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                }else{
                    // dp nothing
                }
            }
        });

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

    private void getUserDetailsFromFB(ParseUser user) {
        // Suggested by https://disqus.com/by/dominiquecanlas/
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,name,picture");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            String email = response.getJSONObject().getString("email");
                            String name = response.getJSONObject().getString("name");
                            JSONObject picture = response.getJSONObject().getJSONObject("picture");
                            JSONObject data = picture.getJSONObject("data");
                            String pictureUrl = data.getString("url");

                            user.setEmail(email);
                            user.setUsername(email);
                            user.put("firstName", name);
                            user.put("pictureUrl", pictureUrl);

                            user.saveInBackground();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

}
