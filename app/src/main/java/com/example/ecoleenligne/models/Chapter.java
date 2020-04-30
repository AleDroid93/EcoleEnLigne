package com.example.ecoleenligne.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Chapter implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("lessons")
    @Expose
    private ArrayList<Lesson> lessons;

    public Chapter() {
        this.id="unknownID";
        this.number = 0;
        this.title="unknown";
        this.lessons = new ArrayList<>();
    }

    public Chapter(String id, Integer number, String title, ArrayList<Lesson> lessons) {
        this.id = id;
        this.number = number;
        this.title = title;
        this.lessons = lessons;
    }

    protected Chapter(Parcel in) {
        id = in.readString();
        if (in.readByte() == 0) {
            number = null;
        } else {
            number = in.readInt();
        }
        title = in.readString();
        lessons = in.createTypedArrayList(Lesson.CREATOR);
    }

    public static final Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel in) {
            return new Chapter(in);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        if (number == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(number);
        }
        dest.writeString(title);
        dest.writeTypedList(lessons);
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "id='" + id + '\'' +
                ", number=" + number +
                ", title='" + title + '\'' +
                ", lessons=" + lessons +
                '}';
    }
}
