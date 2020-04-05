package com.example.ecoleenligne;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {
    com.google.firebase.auth.FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private UserInfo userInfo;
    private TextView welcomeTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        welcomeTv = findViewById(R.id.welcome_text_view);
        welcomeTv.setText(getString(R.string.welcome_text, ""));
        String welcome = getString(R.string.welcome_text);
        String userEmail = currentUser.getEmail();
        if(currentUser != null){
            Log.d("HomeActivity", "AuthID: "+ FirebaseAuth.getInstance().getUid());
            Log.d("HomeActivity","currrentUser Uid: "+ currentUser.getUid());
        }

        FirebaseClient client = ServiceGenerator.createService(FirebaseClient.class);

        Call<UserInfo> call = client.getUserInfo(currentUser.getUid());

        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                Log.d("HomeActivity","onCreate call success: ");
                userInfo = response.body();
                if (userInfo != null) {
                    Log.d("HomeActivity", "userInfo: " + userInfo.toString());
                    welcomeTv.setText(getString(R.string.welcome_text, userInfo.getName()));
                }else {
                    Log.d("HomeActivity", "user: null");
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                String callInfo = call.request().url().toString();
                Log.d("HomeActivity","onCreate: Network failure on url "+ callInfo);
                Toast.makeText(HomeActivity.this, "Network failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if(currentUser != null)
            FirebaseAuth.getInstance().signOut();
        finish();
    }
}
