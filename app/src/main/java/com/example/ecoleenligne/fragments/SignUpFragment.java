package com.example.ecoleenligne.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.UserInfo;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener{
    private NavController navController;
    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private TextView mLoginLink;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.signup_btn).setOnClickListener(SignUpFragment.this);
        view.findViewById(R.id.back_btn).setOnClickListener(SignUpFragment.this);
        mEdtEmail = view.findViewById(R.id.edtEmail);
        mEdtPassword = view.findViewById(R.id.edtPassword);
        mLoginLink = view.findViewById(R.id.tv_login_now);
        SpannableString content = new SpannableString(getString(R.string.signin_account_link));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        mLoginLink.setText(content);
        mLoginLink.setOnClickListener(SignUpFragment.this);
    }

    @Override
    public void onClick(View v) {
        if(navController == null){
            Log.w("SignupFragment", "onClick: Attenzione, Navigation Controller null!");
            return;
        }
        if(v != null){
            switch(v.getId()){
                case R.id.signup_btn:
                    String email = mEdtEmail.getText().toString();
                    String password = mEdtPassword.getText().toString();
                    boolean pwdOk = password.length() >= 8;
                    Bundle bundle = null;
                    if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && pwdOk){
                        bundle = new Bundle();
                        UserInfo user = new UserInfo();
                        user.setEmail(email);
                        user.setPassword(password);
                        bundle.putParcelable("user", user);
                    }
                    navController.navigate(R.id.action_signUpFragment_to_chooseProfileFragment, bundle);
                    break;
                case R.id.back_btn:
                    FragmentActivity activity = getActivity();
                    if(activity != null)
                        activity.onBackPressed();
                    else
                        Log.w("SignupFragment", "onClick: Attenzione, FragmentActivity null!");
                    break;
                case R.id.tv_login_now:
                    navController.navigate(R.id.action_signUpFragment_to_signInFragment);
            }
        }
    }
}
