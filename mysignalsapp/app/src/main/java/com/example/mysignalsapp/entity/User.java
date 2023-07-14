package com.example.mysignalsapp.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class User {
    @SerializedName("id")
    private String id;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("roles")
    private Set<Role> roles;

    public User(String id, String userName, String email) {
        this.id = id;
        this.userName = userName;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
