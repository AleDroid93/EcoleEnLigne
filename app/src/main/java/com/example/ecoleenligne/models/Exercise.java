package com.example.ecoleenligne.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Exercise implements Parcelable {
    @SerializedName("exercise")
    @Expose
    private ArrayList<ExerciseItem> exercises;
    @SerializedName("title")
    @Expose
    private String title;


    public Exercise() {
        this.exercises = new ArrayList<>();
        this.title="";
    }

    public Exercise(String title, ArrayList<ExerciseItem> exercises) {
        this.exercises = exercises;
        this.title = title;
    }

    protected Exercise(Parcel in) {
        exercises = in.createTypedArrayList(ExerciseItem.CREATOR);
        title = in.readString();
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public ArrayList<ExerciseItem> getExercises() {
        return exercises;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setExercises(ArrayList<ExerciseItem> exercises) {
        this.exercises = exercises;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(exercises);
        dest.writeString(title);
    }
}
