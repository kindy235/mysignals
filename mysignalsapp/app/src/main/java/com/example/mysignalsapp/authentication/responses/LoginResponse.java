package com.example.mysignalsapp.authentication.responses;

import com.example.mysignalsapp.entity.User;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("roles")
    private String[] roles;

    @SerializedName("tokenType")
    private String tokenType;

    @SerializedName("accessToken")
    private String accessToken;

    // Create getters and setters as needed

    // Constructor (optional, you can create it if needed)
    public LoginResponse(int id, String username, String email, String[] roles, String tokenType, String accessToken) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}