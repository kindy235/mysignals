package com.mysignals.api.service.impl;


import com.mysignals.api.entity.Sensor;
import com.mysignals.api.repository.SensorRepository;
import com.mysignals.api.service.SensorService;
import com.mysignals.api.utils.SensorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;

    @Autowired
    public SensorServiceImpl(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Override
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    @Override
    public Sensor getSensorById(Long id) {
        return sensorRepository.findById(id).orElse(null);
    }

    @Override
    public Sensor createSensor(Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    @Override
    public Sensor updateSensor(Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    @Override
    public void deleteSensor(Long id) {
        sensorRepository.deleteById(id);
    }

    @Override
    public List<Sensor> getSensorsByTypeAndByMember(SensorType type, Long memberId) {
        return sensorRepository.findByTypeAndByMemberId(type, memberId);
    }

    @Override
    public List<Sensor> getSensorsByMemberId(Long memberId) {
        return sensorRepository.findByMemberId(memberId);
    }

    @Override
    public void deleteAllSenorsOfMember(Long memberId) {
        sensorRepository.deleteByMemberId(memberId);
    }
}
