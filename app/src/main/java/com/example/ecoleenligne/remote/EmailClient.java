package com.example.ecoleenligne.remote;

import com.example.ecoleenligne.data.NetworkMessage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EmailClient {

    @GET("/sendMail")
    Call<String> sendChildrenCredentials(@Query("children") String children, @Query("dest") String emailDest);
}
