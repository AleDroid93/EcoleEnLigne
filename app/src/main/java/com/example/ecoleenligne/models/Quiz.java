package com.example.ecoleenligne.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Quiz implements Parcelable {
    @SerializedName("questions")
    @Expose
    private ArrayList<QuizItem> questions;

    protected Quiz(Parcel in) {
        questions = in.createTypedArrayList(QuizItem.CREATOR);
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    public ArrayList<QuizItem> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuizItem> questions) {
        this.questions = questions;
    }

    public Quiz() {
    }

    public Quiz(ArrayList<QuizItem> questions) {
        this.questions = questions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(questions);
    }
}
