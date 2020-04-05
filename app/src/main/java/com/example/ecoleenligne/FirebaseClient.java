package com.example.ecoleenligne;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FirebaseClient {

    @GET("users/{uid}/.json")
    Call<UserInfo> getUserInfo(@Path("uid") String userId);


}
