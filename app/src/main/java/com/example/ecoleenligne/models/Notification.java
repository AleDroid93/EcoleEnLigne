package com.example.ecoleenligne.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification implements Parcelable{

    @SerializedName("topic")
    @Expose
    private String topic;

    @SerializedName("scope")
    @Expose
    private String scope;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("read")
    @Expose
    private Boolean read;

    @SerializedName("datetime")
    @Expose
    private String datetime;


    public Notification() {
        this.topic = "";
        this.scope = "";
        this.message = "";
        this.read = true;
        this.datetime = "";
    }

    public Notification(String topic, String scope, String message, String datetime, Boolean read) {
        this.topic = topic;
        this.scope = scope;
        this.message = message;
        this.datetime = datetime;
        this.read = read;
    }


    protected Notification(Parcel in) {
        topic = in.readString();
        scope = in.readString();
        message = in.readString();
        datetime = in.readString();
        byte tmpRead = in.readByte();
        read = tmpRead == 0 ? null : tmpRead == 1;
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(topic);
        dest.writeString(scope);
        dest.writeString(message);
        dest.writeString(datetime);
        dest.writeByte((byte) (read == null ? 0 : read ? 1 : 2));
    }
}
