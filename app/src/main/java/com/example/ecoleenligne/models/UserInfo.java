package com.example.ecoleenligne.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model class for the API response
 */

public class UserInfo implements Parcelable {

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };


    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("uclass")
    @Expose
    private String uclass;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("age")
    @Expose
    private Integer age;

    public UserInfo() {
        this.email = "";
        this.password = "";
        this.name = "";
        this.role = "";
        this.surname = "";
        this.uclass = "";
        this.gender = "";
        this.age = 0;
    }

    public UserInfo(String email, String password, String name, String role, String surname, String uclass, String gender, Integer age) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.surname = surname;
        this.uclass = uclass;
        this.gender = gender;
        this.age = age;
    }

    protected UserInfo(Parcel in) {
        email = in.readString();
        password = in.readString();
        name = in.readString();
        role = in.readString();
        surname = in.readString();
        uclass = in.readString();
        gender = in.readString();
        age = in.readInt();
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getSurname() {
        return surname;
    }

    public String getUserClass() {
        return uclass;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUclass(String uclass) {
        this.uclass = uclass;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "email:'" + email + '\'' +
                ", name:'" + name + '\'' +
                ", role:'" + role + '\'' +
                ", surname:'" + surname + '\'' +
                ", class:'" + uclass + '\'' +
                ", gender:'" + gender + '\'' +
                ", age:'" + age + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(name);
        dest.writeString(role);
        dest.writeString(surname);
        dest.writeString(uclass);
        dest.writeString(gender);
        dest.writeInt(age);
    }
}
