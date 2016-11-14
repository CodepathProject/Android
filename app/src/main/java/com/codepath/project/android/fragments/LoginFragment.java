package com.codepath.project.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.project.android.R;
import com.codepath.project.android.network.ParseHelper;
import com.codepath.project.android.utils.GeneralUtils;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by skasabar on 11/12/16.
 */

public class LoginFragment extends Fragment {

    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.btnLogIn) Button btnLogIn;
    @BindView(R.id.btnSignUp) Button btnSignUp;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(etEmail.getText().toString(),
                                            etPassword.getText().toString(),
                                            new LogInCallback() {
                                    @Override
                                    public void done(ParseUser user, ParseException e) {
                                        if (user != null) {
                                            GeneralUtils.showSnackBar(getView(),
                                                    ParseHelper.PARSE_LOGIN_SUCCESS_SNACKTOAST,
                                                    getActivity().getColor(R.color.colorGreen),
                                                    getActivity().getColor(R.color.colorGray));
                                        } else {
                                            GeneralUtils.showSnackBar(getView(),
                                                    ParseHelper.PARSE_LOGIN_FAILED_SNACKTOAST,
                                                    getActivity().getColor(R.color.colorRed),
                                                    getActivity().getColor(R.color.colorGray));
                                        }
                                    }
                                });
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

}
