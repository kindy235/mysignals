package com.example.mysignalsapp.authentication.responses;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

public class LoginResponse implements Parcelable {
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

    protected LoginResponse(Parcel in) {
        id = in.readInt();
        username = in.readString();
        email = in.readString();
        roles = in.createStringArray();
        tokenType = in.readString();
        accessToken = in.readString();
    }

    public static final Creator<LoginResponse> CREATOR = new Creator<LoginResponse>() {
        @Override
        public LoginResponse createFromParcel(Parcel in) {
            return new LoginResponse(in);
        }

        @Override
        public LoginResponse[] newArray(int size) {
            return new LoginResponse[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeStringArray(roles);
        dest.writeString(tokenType);
        dest.writeString(accessToken);
    }
}