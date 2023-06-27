package com.mysignals.api.entity;

import java.sql.Date;

import com.mysignals.api.utils.SensorType;
import jakarta.persistence.*;

@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SensorType type;

    private Date date;

    private double valeur;

    // Constructors, getters, and setters

    public Sensor() {
    }

    public Sensor(SensorType type, Date date, double value) {
        this.type = type;
        this.date = date;
        this.valeur = value;
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
}
