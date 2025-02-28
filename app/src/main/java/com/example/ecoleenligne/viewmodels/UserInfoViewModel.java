package com.example.ecoleenligne.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecoleenligne.data.NetworkMessage;
import com.example.ecoleenligne.models.Child;
import com.example.ecoleenligne.models.Course;
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
    private MutableLiveData<String> updateCoursesMessage;
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
        if(user.getRole().equalsIgnoreCase("parent")){
            ArrayList<Child> children = user.getChildren();
            for(Child child : children){

            }
        }
    }

    public void sendChildrenCredentials(ArrayList<Child> children, String emailDest){
        if(mutableCredentials != null)
            return;
        userInfoRepository = UserInfoRepository.getInstance();
        mutableCredentials = userInfoRepository.sendChildrenCredentials(children, emailDest);
    }

    public void updateCourses(String uid, ArrayList<Course> courses){
        userInfoRepository = UserInfoRepository.getInstance();
        userInfoRepository.updateCourses(uid, courses, updateCoursesMessage);
    }


    public LiveData<UserInfo> getUserInfoRepository() {
        return mutableLiveData;
    }
    public LiveData<NetworkMessage> getCreationMessage(){
        return mutableCreationMessage;
    }

    public LiveData<String> getMutableUpdateCoursesMessage() {
        if(updateCoursesMessage == null)
            updateCoursesMessage = new MutableLiveData<String>();
        return updateCoursesMessage;
    }

    public LiveData<String> getCredentialsEmailMessage(){
        return mutableCredentials;
    }
}
