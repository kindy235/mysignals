package com.mysignals.api.entity;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mysignals.api.utils.SensorType;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Member member;

    @Enumerated(EnumType.STRING)
    private SensorType type;

    private Date date;

    private double valeur;
    
    private String unit;

// Constructors, getters, and setters

    public Sensor() {
    }

    public Sensor(SensorType type, Date date, double value, String unit, Member member) {
        this.type = type;
        this.date = date;
        this.valeur = value;
        this.unit = unit;
        this.member = member;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SensorType getType() {
        return type;
    }

    public void setType(SensorType type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getValue() {
        return valeur;
    }

    public void setValue(double value) {
        this.valeur = value;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }


    public void setMember(Member member) {
        this.member = member;
    }
    public Member getMember() {
        return member;
    }
}
