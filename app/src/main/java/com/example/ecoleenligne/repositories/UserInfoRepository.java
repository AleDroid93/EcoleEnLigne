package com.example.ecoleenligne.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.remote.FirebaseClient;
import com.example.ecoleenligne.remote.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository class that request user data from the network. I use also LiveData for observing API
 * response and notify view model.
 */
public class UserInfoRepository {

    private static UserInfoRepository userInfoRepository;
    private FirebaseClient firebaseAPI;

    public UserInfoRepository() {
        firebaseAPI = ServiceGenerator.createService(FirebaseClient.class);
    }

    public static UserInfoRepository getInstance(){
        if(userInfoRepository == null){
            userInfoRepository = new UserInfoRepository();
        }
        return userInfoRepository;
    }

    public MutableLiveData<UserInfo> getUserInfo(String uid){
        MutableLiveData<UserInfo> userInfo = new MutableLiveData<>();
        firebaseAPI.getUserInfo(uid).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                Log.d("HomeActivity","onCreate call success: ");
                if (response.isSuccessful()) {
                    userInfo.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                String callInfo = call.request().url().toString();
                Log.d("HomeActivity","onCreate: Network failure on url "+ callInfo);
                userInfo.setValue(null);
            }
        });
        return userInfo;
    }
}
