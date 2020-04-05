package com.example.ecoleenligne.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.repositories.UserInfoRepository;

/**
 * The ViewModel class. It stores all the UI-related data in a lifecycle cosncious way. So it allows
 * data to survive configuration changes such as screen rotations.
 */
public class UserInfoViewModel extends ViewModel {
    private MutableLiveData<UserInfo> mutableLiveData;
    private UserInfoRepository userInfoRepository;

    public void init(String uid){
        if (mutableLiveData != null){
            return;
        }
        userInfoRepository = UserInfoRepository.getInstance();
        mutableLiveData = userInfoRepository.getUserInfo(uid);
    }

    public LiveData<UserInfo> getUserInfoRepository() {
        return mutableLiveData;
    }
}
