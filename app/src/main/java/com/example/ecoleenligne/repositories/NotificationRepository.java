package com.example.ecoleenligne.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.ecoleenligne.models.Notification;
import com.example.ecoleenligne.remote.FirebaseClient;
import com.example.ecoleenligne.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationRepository {

    private static NotificationRepository notificationRepository;
    private FirebaseClient firebaseAPI;

    public NotificationRepository() {
        firebaseAPI = ServiceGenerator.createService(FirebaseClient.class);
    }

    public static NotificationRepository getInstance(){
        if(notificationRepository == null){
            notificationRepository = new NotificationRepository();
        }
        return notificationRepository;
    }

    public MutableLiveData<Notification> getNotifications(String uid){
        MutableLiveData<Notification> notification = new MutableLiveData<>();
        firebaseAPI.getUserNotifications(uid).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {

                if (response.isSuccessful()) {Log.d("NotificationRepository","onResponse call success: "+response.message());
                    notification.setValue(response.body());
                }else{
                    Log.d("NotificationRepository","onCreate call: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                String callInfo = call.request().url().toString();

                Log.d("NotificationRepository","onCreate: Network failure on url "+ callInfo);
                notification.setValue(null);
            }
        });
        return notification;
    }


    public void putNotification(String uid, Notification notification, MutableLiveData<String> message){
        firebaseAPI.putNotification(uid, notification).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("NotificationRepository","onResponse call success: ");
                if (response.isSuccessful())
                    message.setValue("success");
                else
                    message.setValue("not success");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                String callInfo = call.request().url().toString();
                Log.d("NotificationRepository","onFailure: Network failure on url "+ callInfo);
                message.setValue("fail");
            }
        });
    }

}
