package com.example.mysignalsapp.authentication.responses;

import com.example.mysignalsapp.entity.User;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("status_code")
    private int statusCode;

    @SerializedName("auth_token")
    private String authToken;

    @SerializedName("user")
    private User user;

    public LoginResponse(int statusCode, String authToken, User user) {
        this.statusCode = statusCode;
        this.authToken = authToken;
        this.user = user;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
