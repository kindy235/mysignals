package com.example.mysignalsapp.entity;

import com.google.gson.annotations.SerializedName;

public class Sensor {
    @SerializedName("id")
    private int id;

    @SerializedName("type")
    private String type;

    @SerializedName("date")
    private String date;

    @SerializedName("unit")
    private String unit;

    @SerializedName("value")
    private String value;

    public Sensor(String type, String date, String unit, String value) {
        this.type = type;
        this.date = date;
        this.unit = unit;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getUnit() {
        return unit;
    }

    public String getValue() {
        return value;
    }
}