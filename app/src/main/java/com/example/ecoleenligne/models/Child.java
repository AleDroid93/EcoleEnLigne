package com.example.ecoleenligne.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Child implements Parcelable {
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("surname")
    @Expose
    String surname;
    @SerializedName("classroom")
    @Expose
    String classroom;
    @SerializedName("gender")
    @Expose
    String gender;
    @SerializedName("email")
    @Expose
    String email;
    @SerializedName("password")
    @Expose
    String password;

    public Child() {
    }

    public Child(String name, String surname, String classroom, String gender, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.classroom = classroom;
        this.gender = gender;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    protected Child(Parcel in) {
        name = in.readString();
        surname = in.readString();
        classroom = in.readString();
        gender = in.readString();
        email = in.readString();
        password = in.readString();
    }

    public static final Creator<Child> CREATOR = new Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel in) {
            return new Child(in);
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(classroom);
        dest.writeString(gender);
        dest.writeString(email);
        dest.writeString(password);
    }

    @Override
    public String toString() {
        return "ChildInfo{" +
                "email:'" + email + '\'' +
                ", name:'" + name + '\'' +
                ", surname:'" + surname + '\'' +
                ", class:'" + classroom + '\'' +
                ", gender:'" + gender + '\'' +
                '}';
    }
}
