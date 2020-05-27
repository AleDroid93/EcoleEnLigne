package com.example.ecoleenligne.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Classroom implements Parcelable {
    @SerializedName("courses")
    @Expose
    private ArrayList<Course> courses;

    @SerializedName("id")
    @Expose
    private String id;



    public Classroom() {
        this.id = "";
        this.courses = new ArrayList<>();
    }

    public Classroom(String id, ArrayList<Course> courses) {
        this.id = id;
        this.courses = courses;
    }

    protected Classroom(Parcel in) {
        courses = in.createTypedArrayList(Course.CREATOR);
        id = in.readString();
    }

    public static final Creator<Classroom> CREATOR = new Creator<Classroom>() {
        @Override
        public Classroom createFromParcel(Parcel in) {
            return new Classroom(in);
        }

        @Override
        public Classroom[] newArray(int size) {
            return new Classroom[size];
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

    public boolean isPresent(Course course){
        for(Course c : courses){
            if(c.getName().equalsIgnoreCase(course.getName()))
                return true;
        }
        return false;
    }

    public void addCourse(Course newCourse){
        courses.add(newCourse);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("courses", courses);
        return result;
    }

    @Override
    public String toString() {
        return "Classroom{" +
                "id='" + id + '\'' +
                ", courses=" + courses +
                '}';
    }
}
