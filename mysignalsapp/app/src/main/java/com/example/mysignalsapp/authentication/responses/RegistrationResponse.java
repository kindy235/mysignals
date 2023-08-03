package com.example.mysignalsapp.authentication.responses;

import com.example.mysignalsapp.entity.User;
import com.google.gson.annotations.SerializedName;

public class RegistrationResponse {

    @SerializedName("message")
    private String message;

    public RegistrationResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
