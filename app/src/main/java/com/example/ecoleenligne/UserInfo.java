package com.example.ecoleenligne;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class UserInfo {
    @SerializedName("email")
    @Expose
    private String email;
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

    public UserInfo(String email, String name, String role, String surname, String uclass) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.surname = surname;
        this.uclass = uclass;
    }

    public String getEmail() {
        return email;
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

    @Override
    public String toString() {
        return "UserInfo{" +
                "email:'" + email + '\'' +
                ", name:'" + name + '\'' +
                ", role:'" + role + '\'' +
                ", surname:'" + surname + '\'' +
                ", class:'" + uclass + '\'' +
                '}';
    }
}
