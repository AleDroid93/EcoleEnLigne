package com.example.ecoleenligne.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String BASE_URL = "https://ecoleenligne-1015c.firebaseio.com/";
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofit = builder.build();

    private static final String FUNCTIONS_BASE_URL = "https://us-central1-ecoleenligne-1015c.cloudfunctions.net";
    private static Retrofit.Builder builderFunctions = new Retrofit.Builder()
            .baseUrl(FUNCTIONS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofitFunctions = builderFunctions.build();

    public static <S> S createService(Class<S> serviceClass){
        return retrofit.create(serviceClass);
    }

    public static <S> S createFunctionService(Class<S> serviceClass){
        return retrofitFunctions.create(serviceClass);
    }
}
