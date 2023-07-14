package com.mysignals.api.controller;

import com.mysignals.api.entity.Sensor;
import com.mysignals.api.service.MemberService;
import com.mysignals.api.service.SensorService;
import com.mysignals.api.utils.ResourceNotFoundException;
import com.mysignals.api.utils.SensorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
// @Api(tags = "Sensors")
public class SensorController {

    private final SensorService sensorService;
    private final MemberService memberService;

    @Autowired
    public SensorController(SensorService sensorService, MemberService memberService) {
        this.sensorService = sensorService;
        this.memberService = memberService;
    }

    @GetMapping("/sensors")
    // @RolesAllowed("ROLE_USER")
    // @ApiOperation(value = "Get all sensors", notes = "Get all sensor from the API")
    public ResponseEntity<List<Sensor>> getAllSensors() {
        return new ResponseEntity<>(sensorService.getAllSensors(), HttpStatus.OK);
    }


    @GetMapping("/members/{memberId}/sensors")
    public ResponseEntity<List<Sensor>> getAllSensorsByMemberId(@PathVariable Long memberId, @RequestParam(value = "type", required = false) SensorType type) {

        if (!memberService.existsById(memberId)) {
            throw new ResourceNotFoundException("Not found Member with id = " + memberId);
        }

        if (type != null) {
            return new ResponseEntity<>(sensorService.getSensorsByTypeAndByMember(type, memberId), HttpStatus.OK);
        }

        return new ResponseEntity<>(sensorService.getSensorsByMemberId(memberId), HttpStatus.OK);
    }

    @PostMapping("/members/{memberId}/sensors")
    public ResponseEntity<Sensor> createSensor(@PathVariable Long memberId, @RequestBody Sensor sensorRequest) {
        Sensor sensor = memberService.getMemberById(memberId).map(member -> {
            sensorRequest.setMember(member);
            return sensorService.createSensor(sensorRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Member with id = " + memberId));

        return new ResponseEntity<>(sensor, HttpStatus.CREATED);
    }


    @GetMapping("/sensors/{id}")
    // @ApiOperation(value = "Get a sensor", notes = "Add a sensors by his id")
    public Sensor getSensorById(@PathVariable Long id) {
        return sensorService.getSensorById(id);
    }

    @PutMapping("/sensors/{id}")
    // @ApiOperation(value = "Edit a sensor", notes = "Edit a sensor from Json to the repository")
    public Sensor updateSensor(@PathVariable Long id, @RequestBody Sensor sensor) {
        sensor.setId(id);
        return sensorService.updateSensor(sensor);
    }

    @DeleteMapping("/sensors/{id}")
    // @ApiOperation(value = "Delete a sensor", notes = "Delete a sensor by his id")
    public void deleteSensor(@PathVariable Long id) {
        sensorService.deleteSensor(id);
    }

    @DeleteMapping("/members/{memberId}/sensors")
    public ResponseEntity<List<Sensor>> deleteAllSensorsOfMember(@PathVariable(value = "memberId") Long memberId) {
        if (!memberService.existsById(memberId)) {
            throw new ResourceNotFoundException("Not found Member with id = " + memberId);
        }

        sensorService.deleteAllSenorsOfMember(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}