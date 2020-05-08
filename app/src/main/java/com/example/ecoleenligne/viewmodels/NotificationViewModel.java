package com.example.ecoleenligne.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecoleenligne.models.Notification;
import com.example.ecoleenligne.repositories.NotificationRepository;

import java.util.ArrayList;

public class NotificationViewModel extends ViewModel {
    private MutableLiveData<String> mutableNotificationMessage;
    private MutableLiveData<ArrayList<Notification>> mutableNotifications;
    ArrayList<Notification> notifications;
    private NotificationRepository repository;

    public LiveData<String> getMutableNotificationMessage() {
        if(mutableNotificationMessage == null)
            mutableNotificationMessage = new MutableLiveData<String>();
        return mutableNotificationMessage;
    }


    public LiveData<ArrayList<Notification>> getMutableNotifications() {
        if(mutableNotifications == null)
            mutableNotifications = new MutableLiveData<ArrayList<Notification>>();
        return mutableNotifications;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public void putNotification(String uid, Notification notification){
        /*
        if (mutableNotificationMessage != null){
            repository = NotificationRepository.getInstance();
            mutableNotificationMessage = repository.putNotification(uid, notification);
        }else{
            Log.e("NotificationViewModel", "MutableNotification null");
        }*/
        repository = NotificationRepository.getInstance();
        mutableNotificationMessage = repository.putNotification(uid, notification);
    }

    public void getNotifications(String uid){
        /*
        if (mutableNotifications != null)
            return;
        repository = NotificationRepository.getInstance();
        mutableNotifications = repository.getNotifications(uid);
        */
    }

}
