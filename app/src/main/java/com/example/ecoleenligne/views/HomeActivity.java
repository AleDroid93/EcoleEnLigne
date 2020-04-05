package com.example.ecoleenligne.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.viewmodels.UserInfoViewModel;
import com.example.ecoleenligne.models.UserInfo;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    com.google.firebase.auth.FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private UserInfoViewModel model;
    private UserInfo userInfo;
    private TextView welcomeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        welcomeTv = findViewById(R.id.welcome_text_view);
        if(currentUser != null){
            Log.d("HomeActivity", "AuthID: "+ FirebaseAuth.getInstance().getUid());
            Log.d("HomeActivity","currrentUser Uid: "+ currentUser.getUid());
        }

        model = ViewModelProviders.of(HomeActivity.this).get(UserInfoViewModel.class);
        final Observer<UserInfo> observerUserInfo = new Observer<UserInfo>() {
            @Override
            public void onChanged(@Nullable UserInfo userInfo) {
                welcomeTv.setText(getString(R.string.welcome_text, userInfo.getName()));
            }
        };
        model.init(currentUser.getUid());
        model.getUserInfoRepository().observe(HomeActivity.this, observerUserInfo);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(currentUser != null)
            FirebaseAuth.getInstance().signOut();
        finish();
    }
}
