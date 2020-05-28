package com.example.ecoleenligne.models;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Model class for the API response
 */

public class UserInfo implements Parcelable {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("notificationToken")
    @Expose
    private String notificaionToken;

    @SerializedName("children")
    @Expose
    private ArrayList<Child> children;

    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("uid")
    @Expose
    private String uid;
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
    private Classroom uclass;

    @SerializedName("offlineLearning")
    @Expose
    private Boolean offlineLearning;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("hasParent")
    @Expose
    private boolean hasParent;


    @SerializedName("parentUid")
    @Expose
    private String parentUid;


    @SerializedName("age")
    @Expose
    private Integer age;


    public UserInfo() {
        this.email = "";
        this.children = new ArrayList<>();
        this.password = "";
        this.uid = "";
        this.name = "";
        this.role = "";
        this.surname = "";
        this.uclass = new Classroom();
        this.gender = "";
        this.hasParent = false;
        this.parentUid = "";
        this.age = 0;
        this.notificaionToken = "";
        this.offlineLearning = false;
        //this.children = new ArrayList<Child>();
    }

    public UserInfo(String email, String password, String name, String role, String surname, Classroom uclass, String gender, Integer age) {
        this.email = email;
        this.children = new ArrayList<>();
        this.password = password;
        this.uid = "";
        this.name = name;
        this.role = role;
        this.surname = surname;
        this.uclass = uclass;
        this.gender = gender;
        this.age = age;
        this.hasParent = false;
        this.parentUid = "";
        this.offlineLearning = false;
        this.notificaionToken = "";
        //this.children = new ArrayList<Child>();
    }


    protected UserInfo(Parcel in) {
        email = in.readString();
        notificaionToken = in.readString();
        children = in.createTypedArrayList(Child.CREATOR);
        password = in.readString();

        uid = in.readString();
        name = in.readString();
        role = in.readString();
        surname = in.readString();
        uclass = in.readParcelable(Classroom.class.getClassLoader());
        byte tmpOfflineLearning = in.readByte();
        offlineLearning = tmpOfflineLearning == 0 ? null : tmpOfflineLearning == 1;
        gender = in.readString();
        hasParent = in.readByte() != 0;
        parentUid = in.readString();
        if (in.readByte() == 0) {
            age = null;
        } else {
            age = in.readInt();
        }
    }

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

    public Classroom getUclass() {
        return uclass;
    }

    public boolean getOfflineLearning() {
        return offlineLearning;
    }

    public void setOfflineLearning(boolean offlineLearning) {
        this.offlineLearning = offlineLearning;
    }

    public String getParentUid() {
        return parentUid;
    }

    public void setParentUid(String parentUid) {
        this.parentUid = parentUid;
    }

    public String getNotificaionToken() {
        return notificaionToken;
    }

    public void setNotificationToken(String notificaionToken) {
        this.notificaionToken = notificaionToken;
    }

    public boolean isHasParent() {
        return hasParent;
    }

    public void setHasParent(boolean hasParent) {
        this.hasParent = hasParent;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public Classroom getUserClass() {
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

    public void setUclass(Classroom uclass) {
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


    public ArrayList<Child> getChildren(){
        return children;
    }


    public void addChild(Child newChild){
        children.add(newChild);
    }

    public void removeChild(Child child){
        this.children.remove(child);
    }

    public String findCourseIdByName(String courseName){
        ArrayList<Course> courses = uclass.getCourses();
        for(Course course : courses)
            if(course.getName().equals(courseName))
                return course.getId();
        return null;
    }

    public int findCourseColorByName(String courseName){
        ArrayList<Course> courses = uclass.getCourses();
        for(Course course : courses)
            if(course.getName().equals(courseName))
                return Color.parseColor(course.getColor());
        return -1;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", surname='" + surname + '\'' +
                ", uclass=" + uclass +
                ", offlineLearning=" + offlineLearning +
                ", gender='" + gender + '\'' +
                ", age=" + age +
               // ", children=" + children +
                '}';
    }

    public int findCourseLightColorByName(String courseName) {
        ArrayList<Course> courses = uclass.getCourses();
        for(Course course : courses)
            if(course.getName().equals(courseName))
                return Color.parseColor(course.getLightColor());
        return -1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(notificaionToken);
        dest.writeTypedList(children);
        dest.writeString(password);
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeString(role);
        dest.writeString(surname);
        dest.writeParcelable(uclass, flags);
        dest.writeByte((byte) (offlineLearning == null ? 0 : offlineLearning ? 1 : 2));
        dest.writeString(gender);
        dest.writeByte((byte) (hasParent ? 1 : 0));
        dest.writeString(parentUid);
        if (age == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(age);
        }
    }
}
