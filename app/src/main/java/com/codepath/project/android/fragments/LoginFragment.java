package com.codepath.project.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.codepath.project.android.R;
import com.codepath.project.android.activities.HomeActivity;
import com.codepath.project.android.activities.SignUpActivity;
import com.codepath.project.android.network.ParseHelper;
import com.codepath.project.android.utils.GeneralUtils;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoginFragment extends Fragment {

    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.btnLogIn) Button btnLogIn;
    @BindView(R.id.btnSignUp) Button btnSignUp;
    @BindView(R.id.swSkipLogin) Switch swSkipLogin;

    private Unbinder unbinder;

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

}
