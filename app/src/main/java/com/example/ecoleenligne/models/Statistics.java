package com.example.ecoleenligne.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Statistics implements Parcelable {
    @SerializedName("avgMark")
    @Expose
    private Double avgMark;

    @SerializedName("numExercises")
    @Expose
    private Integer numExercises;

    @SerializedName("numQuizzes")
    @Expose
    private Integer numQuizzes;

    @Exclude
    private String courseName;

    @Exclude
    private String studentName;

    @Exclude
    private String color;


    public Statistics() {
        this.avgMark = 0.0;
        this.numExercises = -1;
        this.numQuizzes = -1;
    }

    public Statistics(Double avgMark, Integer numExercises, Integer numQuizzes) {
        this.avgMark = avgMark;
        this.numExercises = numExercises;
        this.numQuizzes = numQuizzes;
        this.courseName = "";
        this.studentName = "";
    }

    protected Statistics(Parcel in) {
        if (in.readByte() == 0) {
            avgMark = null;
        } else {
            avgMark = in.readDouble();
        }
        if (in.readByte() == 0) {
            numExercises = null;
        } else {
            numExercises = in.readInt();
        }
        if (in.readByte() == 0) {
            numQuizzes = null;
        } else {
            numQuizzes = in.readInt();
        }

    }

    public static final Creator<Statistics> CREATOR = new Creator<Statistics>() {
        @Override
        public Statistics createFromParcel(Parcel in) {
            return new Statistics(in);
        }

        @Override
        public Statistics[] newArray(int size) {
            return new Statistics[size];
        }
    };

    public Double getAvgMark() {
        return avgMark;
    }

    public void setAvgMark(Double avgMark) {
        this.avgMark = avgMark;
    }

    public Integer getNumExercises() {
        return numExercises;
    }

    public void setNumExercises(Integer numExercises) {
        this.numExercises = numExercises;
    }

    public Integer getNumQuizzes() {
        return numQuizzes;
    }

    public void setNumQuizzes(Integer numQuizzes) {
        this.numQuizzes = numQuizzes;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (avgMark == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(avgMark);
        }
        if (numExercises == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(numExercises);
        }
        if (numQuizzes == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(numQuizzes);
        }

    }

    @Override
    public String toString() {
        return "Statistics{" +
                "avgMark=" + avgMark +
                ", numExercises=" + numExercises +
                ", numQuizzes=" + numQuizzes +
                ", courseName='" + courseName + '\'' +
                ", studentName='" + studentName + '\'' +
                '}';
    }
}
