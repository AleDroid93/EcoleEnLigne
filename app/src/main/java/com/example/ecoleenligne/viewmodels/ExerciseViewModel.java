package com.example.ecoleenligne.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecoleenligne.models.Exercise;
import com.example.ecoleenligne.models.ExerciseSubmission;
import com.example.ecoleenligne.repositories.ExerciseRepository;

import java.util.ArrayList;

public class ExerciseViewModel extends ViewModel {
    private MutableLiveData<String> mutableExSubmissionMessage;
    ArrayList<ExerciseSubmission> exercises;
    private ExerciseRepository repository;


    public ExerciseViewModel() {
        this.exercises = new ArrayList<ExerciseSubmission>();
    }

    public LiveData<String> getMutableExSubmissionMessage() {
        if(mutableExSubmissionMessage == null)
            mutableExSubmissionMessage = new MutableLiveData<String>();
        return mutableExSubmissionMessage;
    }



    public ArrayList<ExerciseSubmission> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<ExerciseSubmission> exercises) {
        this.exercises = exercises;
    }

    public void postExercise(String uid, ExerciseSubmission exerciseSubmission){
        repository = ExerciseRepository.getInstance();
        exercises.add(exerciseSubmission);
        repository.postExercise(uid, exerciseSubmission, mutableExSubmissionMessage);
    }


}
