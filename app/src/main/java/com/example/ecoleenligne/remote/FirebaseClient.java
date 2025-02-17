package com.example.ecoleenligne.remote;


import com.example.ecoleenligne.data.NetworkMessage;
import com.example.ecoleenligne.models.Course;
import com.example.ecoleenligne.models.Exercise;
import com.example.ecoleenligne.models.ExerciseSubmission;
import com.example.ecoleenligne.models.Notification;
import com.example.ecoleenligne.models.UserInfo;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FirebaseClient {

    @GET("users/{uid}/.json")
    Call<UserInfo> getUserInfo(@Path("uid") String userId);

    @GET("notifications/{uid}/.json")
    Call<Notification> getUserNotifications(@Path("uid") String userId);

    @PUT("/users/{uid}.json")
    Call<NetworkMessage> createUser(@Path("uid") String uid, @Body UserInfo user);

    @POST("/notifications/{uid}/.json")
    Call<Object> putNotification(@Path("uid") String uid, @Body Notification notification);

    @POST("/exercise-submissions/{uid}/.json")
    Call<Object> postExercise(@Path("uid") String uid, @Body ExerciseSubmission submission);


    @POST("/users/{uid}/uclass/.json")
    Call<Object> postNewCourses(@Path("uid") String uid, @Body ArrayList<Course> courses);

}
