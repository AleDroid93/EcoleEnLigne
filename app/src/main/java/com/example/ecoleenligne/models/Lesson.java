package com.example.ecoleenligne.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Lesson implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("introduction")
    @Expose
    private String introduction;
    @SerializedName("conclusion")
    @Expose
    private String conclusion;
    @SerializedName("paragraphs")
    @Expose
    private ArrayList<Paragraph> paragraphs;


    public Lesson() {
        this.id = "unknownId";
        this.title="title";
        this.introduction="";
        this.conclusion="";
        this.paragraphs = new ArrayList<>();
    }

    public Lesson(String id, String title, String introduction, String conclusion, ArrayList<Paragraph> paragraphs) {
        this.id = id;
        this.title = title;
        this.introduction = introduction;
        this.conclusion = conclusion;
        this.paragraphs = paragraphs;
    }

    protected Lesson(Parcel in) {
        id = in.readString();
        title = in.readString();
        introduction = in.readString();
        conclusion = in.readString();
        paragraphs = in.createTypedArrayList(Paragraph.CREATOR);
    }

    public static final Creator<Lesson> CREATOR = new Creator<Lesson>() {
        @Override
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }

        @Override
        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public ArrayList<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(ArrayList<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(introduction);
        dest.writeString(conclusion);
        dest.writeTypedList(paragraphs);
    }
}
