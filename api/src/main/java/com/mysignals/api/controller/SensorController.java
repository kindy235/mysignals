package com.mysignals.api.controller;

import com.mysignals.api.entity.Sensor;
import com.mysignals.api.service.SensorService;
import com.mysignals.api.utils.SensorType;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/sensors")
// @Api(tags = "Sensors")
public class SensorController {

    private final SensorService sensorService;

    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    // @RolesAllowed("ROLE_USER")
    // @ApiOperation(value = "Get all sensors", notes = "Get all sensor from the API")
    public List<Sensor> getAllSensors(@RequestParam(value = "type", required = false) SensorType type) {
        if (type != null) {
            return sensorService.getSensorByType(type);
        } else {
            return sensorService.getAllSensors();
        }
    }

    @GetMapping("/{id}")
    // @ApiOperation(value = "Get a sensor", notes = "Add a sensors by his id")
    public Sensor getSensorById(@PathVariable Long id) {
        return sensorService.getSensorById(id);
    }

    @PostMapping
    // @ApiOperation(value = "Add a sensor", notes = "Add a sensor from Json to the repository")
    public Sensor createSensor(@RequestBody Sensor sensor) {
        return sensorService.createSensor(sensor);
    }

    @PutMapping("/{id}")
    // @ApiOperation(value = "Edit a sensor", notes = "Edit a sensor from Json to the repository")
    public Sensor updateSensor(@PathVariable Long id, @RequestBody Sensor sensor) {
        sensor.setId(id);
        return sensorService.updateSensor(sensor);
    }

    @DeleteMapping("/{id}")
    // @ApiOperation(value = "Delete a sensor", notes = "Delete a sensor by his id")
    public void deleteSensor(@PathVariable Long id) {
        sensorService.deleteSensor(id);
    }
}