package com.example.mysignalsapp.entity;

import com.example.mysignalsapp.utils.ERole;

public class Role {

    private ERole name;

    public Role() {

    }

    public Role(ERole name) {
        this.name = name;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }
}