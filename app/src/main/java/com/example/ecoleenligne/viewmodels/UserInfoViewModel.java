package com.example.ecoleenligne.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecoleenligne.data.NetworkMessage;
import com.example.ecoleenligne.models.Child;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.repositories.UserInfoRepository;

import java.util.ArrayList;

/**
 * The ViewModel class. It stores all the UI-related data in a lifecycle cosncious way. So it allows
 * data to survive configuration changes such as screen rotations.
 */
public class UserInfoViewModel extends ViewModel {
    private MutableLiveData<UserInfo> mutableLiveData;
    private MutableLiveData<NetworkMessage> mutableCreationMessage;
    private MutableLiveData<String> mutableCredentials;
    private UserInfoRepository userInfoRepository;

    public void init(String uid){
        if (mutableLiveData != null){
            return;
        }
        userInfoRepository = UserInfoRepository.getInstance();
        mutableLiveData = userInfoRepository.getUserInfo(uid);
    }

    public void createUser(String uid, UserInfo user){
        if (mutableCreationMessage != null){
            return;
        }
        userInfoRepository = UserInfoRepository.getInstance();
        mutableCreationMessage = userInfoRepository.createUser(uid, user);
    }

    public void sendChildrenCredentials(ArrayList<Child> children, String emailDest){
        if(mutableCredentials != null)
            return;
        userInfoRepository = UserInfoRepository.getInstance();
        mutableCredentials = userInfoRepository.sendChildrenCredentials(children, emailDest);
    }

    public LiveData<UserInfo> getUserInfoRepository() {
        return mutableLiveData;
    }
    public LiveData<NetworkMessage> getCreationMessage(){
        return mutableCreationMessage;
    }
    public LiveData<String> getCredentialsEmailMessage(){
        return mutableCredentials;
    }
}
