package com.example.ecoleenligne.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Course implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("lightColor")
    @Expose
    private String lightColor;

    @SerializedName("chapters")
    @Expose
    private ArrayList<Chapter> chapters;

    public Course() {
        this.id = "unkID";
        this.name="unknown";
        this.color="#ffffff";
    }
    public Course(String id, String name, String color, String lightColor) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.lightColor = lightColor;
        this.chapters = new ArrayList<>();
    }

    public Course(String id, String name, String color, String lightColor,ArrayList<Chapter> chapters) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.lightColor = lightColor;
        this.chapters = chapters;
    }


    protected Course(Parcel in) {
        id = in.readString();
        name = in.readString();
        color = in.readString();
        lightColor = in.readString();
        chapters = in.createTypedArrayList(Chapter.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(color);
        dest.writeString(lightColor);
        dest.writeTypedList(chapters);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLightColor() {
        return lightColor;
    }

    public void setLightColor(String lightColor) {
        this.lightColor = lightColor;
    }

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }


}
