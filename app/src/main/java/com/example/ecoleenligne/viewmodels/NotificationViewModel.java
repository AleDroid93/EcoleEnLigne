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
    private int notificationsToRead;
    private NotificationRepository repository;


    public NotificationViewModel() {
        this.notificationsToRead = 0;
        this.notifications = new ArrayList<Notification>();
    }


    public int getNotificationsToRead() {
        return notificationsToRead;
    }

    public void setNotificationsToRead(int notificationsToRead) {
        this.notificationsToRead = notificationsToRead;
    }

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
        repository = NotificationRepository.getInstance();
        repository.putNotification(uid, notification, mutableNotificationMessage);
    }

    public void getNotifications(String uid){
        /*
        if (mutableNotifications != null)
            return;
        repository = NotificationRepository.getInstance();
        mutableNotifications = repository.getNotifications(uid);
        */
    }

    public void updateNotifications(Notification notificationItem) {
        if(notificationItem.getRead())
            this.notificationsToRead++;
        notifications.add(notificationItem);
        mutableNotifications.setValue(notifications);
    }
}
