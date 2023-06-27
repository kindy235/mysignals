package com.mysignals.api.service;

import com.mysignals.api.entity.Sensor;
import com.mysignals.api.utils.SensorType;

import java.util.List;

public interface SensorService {
    List<Sensor> getAllSensors();
    Sensor getSensorById(Long id);
    Sensor createSensor(Sensor sensor);
    Sensor updateSensor(Sensor sensor);
    void deleteSensor(Long id);
    List<Sensor> getSensorByType(SensorType type);
}
