package com.example.ecoleenligne.remote;


import com.example.ecoleenligne.models.UserInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FirebaseClient {

    @GET("users/{uid}/.json")
    Call<UserInfo> getUserInfo(@Path("uid") String userId);

    @PUT("/users/{uid}.json")
    Call<ResponseBody> createUser(@Path("uid") String uid, @Body UserInfo user);
}
