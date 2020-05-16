package com.example.ecoleenligne.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExerciseItem implements Parcelable {

    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("answer")
    @Expose
    private String answer;

    public ExerciseItem() {
        this.question="";
        this.answer="";
    }

    public ExerciseItem(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    protected ExerciseItem(Parcel in) {
        question = in.readString();
        answer = in.readString();
    }

    public static final Creator<ExerciseItem> CREATOR = new Creator<ExerciseItem>() {
        @Override
        public ExerciseItem createFromParcel(Parcel in) {
            return new ExerciseItem(in);
        }

        @Override
        public ExerciseItem[] newArray(int size) {
            return new ExerciseItem[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(answer);
    }
}
