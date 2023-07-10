package com.example.mysignalsapp.viewmodel;

import androidx.databinding.BaseObservable;
import com.libelium.mysignalsconnectkit.pojo.LBSensorObject;

public class SensorViewModel extends BaseObservable {
    private LBSensorObject sensorObject;

    public void setSensorObject(LBSensorObject sensorObject) {
        this.sensorObject = sensorObject;
        notifyChange();
    }

    public LBSensorObject getSensorObject() {
        return sensorObject;
    }
}
