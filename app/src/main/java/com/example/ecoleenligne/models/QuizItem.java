package com.example.ecoleenligne.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizItem implements Parcelable {

    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("choice1")
    @Expose
    private String choice1;
    @SerializedName("choice2")
    @Expose
    private String choice2;
    @SerializedName("choice3")
    @Expose
    private String choice3;
    @SerializedName("choice4")
    @Expose
    private String choice4;
    @SerializedName("answer")
    @Expose
    private int answer;

    public QuizItem() {
        this.question = "";
        this.choice1 = "";
        this.choice2 = "";
        this.choice3 = "";
        this.choice4 = "";
        this.answer = 0;

    }

    public QuizItem(String question, String choice1, String choice2, String choice3, String choice4, int answer) {
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.answer = answer;
    }

    protected QuizItem(Parcel in) {
        question = in.readString();
        choice1 = in.readString();
        choice2 = in.readString();
        choice3 = in.readString();
        choice4 = in.readString();
        answer = in.readInt();
    }

    public static final Creator<QuizItem> CREATOR = new Creator<QuizItem>() {
        @Override
        public QuizItem createFromParcel(Parcel in) {
            return new QuizItem(in);
        }

        @Override
        public QuizItem[] newArray(int size) {
            return new QuizItem[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoice1() {
        return choice1;
    }

    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public void setChoice3(String choice3) {
        this.choice3 = choice3;
    }

    public String getChoice4() {
        return choice4;
    }

    public void setChoice4(String choice4) {
        this.choice4 = choice4;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getAnswerTextByIndex(){
        switch(answer){
            case 1:
                return choice1;
            case 2:
                return choice2;
            case 3:
                return choice3;
            case 4:
                return choice4;
        }
        return "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(choice1);
        dest.writeString(choice2);
        dest.writeString(choice3);
        dest.writeString(choice4);
        dest.writeInt(answer);
    }
}
