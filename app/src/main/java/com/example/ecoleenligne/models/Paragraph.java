package com.example.ecoleenligne.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Paragraph implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("video")
    @Expose
    private Video video;
    @SerializedName("resume")
    @Expose
    private String resume;
    @SerializedName("content")
    @Expose
    private String content;


    public Paragraph() {
        this.id = "unknowId";
        this.content="content";
        this.number=-1;
        this.video= new Video();
        this.resume="resume";
        this.title="title";
    }

    public Paragraph(String id, String title, Integer number, Video video, String resume, String content) {
        this.id = id;
        this.title = title;
        this.number = number;
        this.video = video;
        this.resume = resume;
        this.content = content;
    }


    protected Paragraph(Parcel in) {
        id = in.readString();
        title = in.readString();
        if (in.readByte() == 0) {
            number = null;
        } else {
            number = in.readInt();
        }
        video = in.readParcelable(Video.class.getClassLoader());
        resume = in.readString();
        content = in.readString();
    }

    public static final Creator<Paragraph> CREATOR = new Creator<Paragraph>() {
        @Override
        public Paragraph createFromParcel(Parcel in) {
            return new Paragraph(in);
        }

        @Override
        public Paragraph[] newArray(int size) {
            return new Paragraph[size];
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        if (number == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(number);
        }
        dest.writeParcelable(video, flags);
        dest.writeString(resume);
        dest.writeString(content);
    }
}
