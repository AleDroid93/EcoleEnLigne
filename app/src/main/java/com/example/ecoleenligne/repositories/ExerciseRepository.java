package com.example.ecoleenligne.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.ecoleenligne.models.Exercise;
import com.example.ecoleenligne.models.ExerciseSubmission;
import com.example.ecoleenligne.remote.FirebaseClient;
import com.example.ecoleenligne.remote.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseRepository {
    public static final String TAG = "ExerciseRepository";
    private static ExerciseRepository exerciseRepository;
    private FirebaseClient firebaseAPI;

    public ExerciseRepository() {
        firebaseAPI = ServiceGenerator.createService(FirebaseClient.class);
    }

    public static ExerciseRepository getInstance(){
        if(exerciseRepository == null){
            exerciseRepository = new ExerciseRepository();
        }
        return exerciseRepository;
    }

    public void postExercise(String uid, ExerciseSubmission exerciseSubmission, MutableLiveData<String> message){
        firebaseAPI.postExercise(uid, exerciseSubmission).enqueue(new Callback<Object>() {
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

}
