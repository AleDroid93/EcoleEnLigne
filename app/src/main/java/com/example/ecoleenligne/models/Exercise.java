package com.example.ecoleenligne.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Exercise implements Parcelable {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("questions")
    @Expose
    private ArrayList<ExerciseItem> exercises;

    public Exercise() {
        this.exercises = new ArrayList<>();
        this.title="";
    }

    public Exercise(String title, ArrayList<ExerciseItem> exercises) {
        this.title = title;
        this.exercises = exercises;
    }

    protected Exercise(Parcel in) {
        title = in.readString();
        exercises = in.createTypedArrayList(ExerciseItem.CREATOR);
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
        dest.writeString(title);
        dest.writeTypedList(exercises);
    }
}
