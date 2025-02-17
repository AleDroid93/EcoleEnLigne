package com.example.ecoleenligne.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.viewmodels.UserInfoViewModel;
import com.example.ecoleenligne.views.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyAccountFragment extends Fragment implements View.OnClickListener{
    private NavController navController;
    private FirebaseAuth mAuth;
    private UserInfoViewModel model;
    private Observer<UserInfo> observerUserInfo;

    public VerifyAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        //incomingUser = getArguments().getParcelable("user");
        view.findViewById(R.id.confirm_activate_btn).setOnClickListener(VerifyAccountFragment.this);
        mAuth = FirebaseAuth.getInstance();
        observerUserInfo = getUserInfoObserver();

    }

    private Observer<UserInfo> getUserInfoObserver() {
        model = ViewModelProviders.of(VerifyAccountFragment.this).get(UserInfoViewModel.class);
        final Observer<UserInfo> observerUserInfo = new Observer<UserInfo>() {
            @Override
            public void onChanged(@Nullable UserInfo userInfo) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("user", userInfo);
                startActivity(intent);
            }
        };
        return observerUserInfo;
    }


    @Override
    public void onClick(View v) {
        if(navController == null){
            Log.w("ParentInfoFragment", "onClick: Attenzione, Navigation Controller null!");
            return;
        }

        switch (v.getId()) {
            case R.id.confirm_activate_btn:
                Log.d("VerifyAccountFragment", "onClick: activation done pressed!");
                mAuth = FirebaseAuth.getInstance();
                mAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(mAuth.getCurrentUser().isEmailVerified()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            model.init(user.getUid());
                            model.getUserInfoRepository().observe(VerifyAccountFragment.this, observerUserInfo);
                        }else {
                            Toast.makeText(getActivity(), "Account not activated yet", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;

        }
    }

}
