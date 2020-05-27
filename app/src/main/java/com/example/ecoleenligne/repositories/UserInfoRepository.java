package com.example.ecoleenligne.repositories;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.ecoleenligne.data.NetworkMessage;
import com.example.ecoleenligne.models.Child;
import com.example.ecoleenligne.models.Course;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.remote.EmailClient;
import com.example.ecoleenligne.remote.FirebaseClient;
import com.example.ecoleenligne.remote.ServiceGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository class that request user data from the network. I use also LiveData for observing API
 * response and notify view model.
 */
public class UserInfoRepository {

    private static final String TAG = "UserInfoRepository";
    private static UserInfoRepository userInfoRepository;
    private FirebaseClient firebaseAPI;
    private EmailClient emailClient;
    public UserInfoRepository() {
        firebaseAPI = ServiceGenerator.createService(FirebaseClient.class);
        emailClient = ServiceGenerator.createFunctionService(EmailClient.class);

    }

    public static UserInfoRepository getInstance(){
        if(userInfoRepository == null){
            userInfoRepository = new UserInfoRepository();
        }
        return userInfoRepository;
    }

    public MutableLiveData<UserInfo> getUserInfo(String uid){
        MutableLiveData<UserInfo> userInfo = new MutableLiveData<>();
        firebaseAPI.getUserInfo(uid).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {

                if (response.isSuccessful()) {Log.d("UserInfoRepository","onCreate call success: "+response.message());
                    userInfo.setValue(response.body());
                }else{
                    Log.d("UserInfoRepository","onCreate call: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                String callInfo = call.request().url().toString();

                Log.d("UserInfoRepository","onCreate: Network failure on url "+ callInfo);
                userInfo.setValue(null);
            }
        });
        return userInfo;
    }


    public MutableLiveData<NetworkMessage> createUser(String uid, UserInfo user){
        MutableLiveData<NetworkMessage> message = new MutableLiveData<>();
        firebaseAPI.createUser(uid, user).enqueue(new Callback<NetworkMessage>() {
            @Override
            public void onResponse(Call<NetworkMessage> call, Response<NetworkMessage> response) {
                Log.d("UserInfoRepository","onCreate call success: ");
                if (response.isSuccessful())
                    message.setValue(new NetworkMessage("success"));
                else
                    message.setValue(new NetworkMessage("not success"));
            }

            @Override
            public void onFailure(Call<NetworkMessage> call, Throwable t) {
                String callInfo = call.request().url().toString();
                Log.d("UserInfoRepository","onCreate: Network failure on url "+ callInfo);
                message.setValue(new NetworkMessage("fail"));
            }
        });
        return message;
    }


    public void updateCourses(String uid, ArrayList<Course> newCourses, MutableLiveData<String> message){
        firebaseAPI.postNewCourses(uid, newCourses).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d(TAG,"onResponse call success: ");
                if (response.isSuccessful())
                    message.setValue("success");
                else
                    message.setValue("not success");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                String callInfo = call.request().url().toString();
                Log.d(TAG,"onFailure: Network failure on url "+ callInfo);
                message.setValue("fail");
            }
        });
    }


    public MutableLiveData<String> sendChildrenCredentials(ArrayList<Child> children, String emailDest) {
        MutableLiveData<String> message = new MutableLiveData<>();
        JSONObject JSONChildren = new JSONObject();
        Gson gson = new Gson();
        String json = gson.toJson(children);
        try {
            for(Child c : children){
                JSONChildren.put(String.valueOf(children.indexOf(c)), gson.toJson(c));
            }

            String encodedChildren = Base64.encodeToString(JSONChildren.toString().getBytes("UTF-8"), Base64.NO_WRAP);

            emailClient.sendChildrenCredentials(json, emailDest).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d("UserInfoRepository", "onCreate call success: ");
                    if (response.isSuccessful())
                        message.setValue("success");
                    else
                        message.setValue("not success");
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    String callInfo = call.request().url().toString();
                    Log.d("UserInfoRepository", "onCreate: Network failure on url " + callInfo);
                    message.setValue("fail");
                }
            });
        }catch(Exception ex){
            Log.e("UserInfoRepository", ex.getMessage());
        }
        return message;
    }

}
