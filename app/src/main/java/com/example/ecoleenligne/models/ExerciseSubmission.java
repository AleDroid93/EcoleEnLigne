package com.example.ecoleenligne.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ExerciseSubmission implements Parcelable {
    @SerializedName("submission-date")
    @Expose
    private String subdate;
    @SerializedName("class")
    @Expose
    private String classroom;

    @SerializedName("course")
    @Expose
    private String course;
    @SerializedName("chapter")
    @Expose
    private String chapter;
    @SerializedName("lesson")
    @Expose
    private String lesson;
    @SerializedName("answers")
    @Expose
    private ArrayList<String> answers;

    public ExerciseSubmission() {
    }


    public ExerciseSubmission(String subdate, String classroom,
                              String course, String chapter, String lesson, ArrayList<String> answers) {
        this.subdate = subdate;
        this.classroom = classroom;
        this.course = course;
        this.chapter = chapter;
        this.lesson = lesson;
        this.answers = answers;
    }

    protected ExerciseSubmission(Parcel in) {
        subdate = in.readString();
        classroom = in.readString();
        course = in.readString();
        chapter = in.readString();
        lesson = in.readString();
        answers = in.createStringArrayList();
    }

    public static final Creator<ExerciseSubmission> CREATOR = new Creator<ExerciseSubmission>() {
        @Override
        public ExerciseSubmission createFromParcel(Parcel in) {
            return new ExerciseSubmission(in);
        }

        @Override
        public ExerciseSubmission[] newArray(int size) {
            return new ExerciseSubmission[size];
        }
    };


    public String getSubdate() {
        return subdate;
    }

    public void setSubdate(String subdate) {
        this.subdate = subdate;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subdate);
        dest.writeString(classroom);
        dest.writeString(course);
        dest.writeString(chapter);
        dest.writeString(lesson);
        dest.writeStringList(answers);
    }
}
