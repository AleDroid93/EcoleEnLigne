package com.example.ecoleenligne;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FirebaseClient {

    @GET("users/.json")
    Call<Map<String, FirebaseUser>> getUsers();

}
