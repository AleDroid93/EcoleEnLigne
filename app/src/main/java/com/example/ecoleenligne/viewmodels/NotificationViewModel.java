package com.example.ecoleenligne.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecoleenligne.models.Notification;
import com.example.ecoleenligne.repositories.NotificationRepository;

import java.util.ArrayList;

public class NotificationViewModel extends ViewModel {
    private MutableLiveData<String> mutableNotificationMessage;
    private MutableLiveData<Notification> mutableNotification;
    ArrayList<Notification> notifications;
    private NotificationRepository repository;

    public LiveData<String> getMutableNotificationMessage() {
        return mutableNotificationMessage;
    }


    public LiveData<Notification> getMutableNotification() {
        return mutableNotification;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public void putNotification(String uid, Notification notification){
        if (mutableNotificationMessage != null){
            return;
        }
        repository = NotificationRepository.getInstance();
        mutableNotificationMessage = repository.putNotification(uid, notification);
    }

    public void getNotifications(String uid){
        if (mutableNotification != null)
            return;
        repository = NotificationRepository.getInstance();
        mutableNotification = repository.getNotifications(uid);
    }
}
