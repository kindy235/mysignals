package com.example.mysignalsapp.entity;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class Member implements Parcelable {
    private Long id;
    private String name;
    private String surname;
    private String picture;
    private String description;
    private int height;
    private int weight;
    private String birthday;

    public Member(String name, String surname, String picture, String description, int height, int weight, String birthday) {
        this.name = name;
        this.surname = surname;
        this.picture = picture;
        this.description = description;
        this.height = height;
        this.weight = weight;
        this.birthday = birthday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(picture);
        dest.writeString(description);
        dest.writeInt(height);
        dest.writeInt(weight);
        dest.writeString(birthday);
    }

    public static final Parcelable.Creator<Member> CREATOR = new Parcelable.Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel source) {
            return new Member(source);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    protected Member(Parcel in) {
        id = (Long) in.readValue(Long.class.getClassLoader());
        name = in.readString();
        surname = in.readString();
        picture = in.readString();
        description = in.readString();
        height = in.readInt();
        weight = in.readInt();
        birthday = in.readString();
    }
}
