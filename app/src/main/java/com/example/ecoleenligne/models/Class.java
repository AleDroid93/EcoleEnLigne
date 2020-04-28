package com.example.ecoleenligne.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Class implements Parcelable {
    @SerializedName("courses")
    @Expose
    private ArrayList<Course> courses;

    @SerializedName("id")
    @Expose
    private String id;



    public Class() {
        this.id = "";
        this.courses = new ArrayList<>();
    }

    public Class(String id, ArrayList<Course> courses) {
        this.id = id;
        this.courses = courses;
    }

    protected Class(Parcel in) {
        courses = in.createTypedArrayList(Course.CREATOR);
        id = in.readString();
    }

    public static final Creator<Class> CREATOR = new Creator<Class>() {
        @Override
        public Class createFromParcel(Parcel in) {
            return new Class(in);
        }

        @Override
        public Class[] newArray(int size) {
            return new Class[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(courses);
        dest.writeString(id);
    }

    @Override
    public String toString() {
        return "Class{" +
                "id='" + id + '\'' +
                ", courses=" + courses +
                '}';
    }
}
