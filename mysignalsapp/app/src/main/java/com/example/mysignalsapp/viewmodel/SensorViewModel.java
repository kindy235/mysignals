package com.example.mysignalsapp.viewmodel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.example.mysignalsapp.utils.SensorType;
import com.libelium.mysignalsconnectkit.pojo.LBSensorObject;
import com.libelium.mysignalsconnectkit.utils.StringConstants;

public class SensorViewModel extends BaseObservable {
    private LBSensorObject sensorObject;

    public void setSensorObject(LBSensorObject sensorObject) {
        this.sensorObject = sensorObject;
        notifyChange();
    }

    public LBSensorObject getSensorObject() {
        return sensorObject;
    }

    @Bindable
    public String getSensorValue() {
        String sensorValue = null;

        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDBodyPositionSensor)) {
            sensorValue =  String.valueOf(sensorObject.body_position_value);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDTemperatureSensor)) {
            sensorValue =  String.valueOf(sensorObject.temperature_value);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDEMGSensor)) {
            sensorValue =  String.valueOf(sensorObject.emg_value);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDECGSensor)) {
            sensorValue =  String.valueOf(sensorObject.ecg_value);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDAirflowSensor)) {
            sensorValue =  String.valueOf(sensorObject.airflow_value);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDGSRSensor)) {
            sensorValue =  String.valueOf(sensorObject.gsr_capacitance_value);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDBloodPressureSensor)) {
            sensorValue =  String.valueOf(sensorObject.diastolic_pressure_value);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDPulsiOximeterSensor)) {
            sensorValue =  String.valueOf(sensorObject.pulsioximeter_heart_rate_value);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDGlucometerSensor)) {
            sensorValue =  String.valueOf(sensorObject.glucometer_value);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDSpirometerSensor)) {
            sensorValue =  String.valueOf(sensorObject.spirometer_num_measures_value);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDSnoreSensor)) {
            sensorValue =  String.valueOf(sensorObject.snore_value);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDScaleBLESensor)) {
            sensorValue =  String.valueOf(sensorObject.scale_body_fat_value);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDBloodPressureBLESensor)) {
            sensorValue =  String.valueOf(sensorObject.diastolic_pressure_ble_value);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDPulsiOximeterBLESensor)) {
            sensorValue =  String.valueOf(sensorObject.pulsioximeter_ble_heart_rate_value);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDGlucometerBLESensor)) {
            sensorValue =  String.valueOf(sensorObject.glucometer_ble_value);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDEEGSensor)) {
            sensorValue =  String.valueOf(sensorObject.electroencephalography_delta_signal_value);
        }

        return sensorValue;
    }

    @Bindable
    public String getSensorType() {
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDBodyPositionSensor)) {
            return SensorType.POSITION.name();
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDTemperatureSensor)) {
            return SensorType.TEMP.name();
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDEMGSensor)) {
            return SensorType.EMG.name();
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDECGSensor)) {
            return SensorType.ECG.name();
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDAirflowSensor)) {
            return SensorType.AIRFLOW.name();
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDGSRSensor)) {
            return SensorType.GSR.name();
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDBloodPressureSensor)) {
            return SensorType.BLOOD.name();
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDPulsiOximeterSensor)) {
            return SensorType.SPO2.name();
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDGlucometerSensor)) {
            return SensorType.GLUCO.name();
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDSpirometerSensor)) {
            return SensorType.SPIR.name();
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDSnoreSensor)) {
            return SensorType.SNORE.name();
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDScaleBLESensor)) {
            return SensorType.SCALE.name();
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDBloodPressureBLESensor)) {
            return SensorType.BLOOD.name();
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDPulsiOximeterBLESensor)) {
            return SensorType.SPO2.name();
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDGlucometerBLESensor)) {
            return SensorType.GLUCO.name();
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDEEGSensor)) {
            return SensorType.EEG.name();
        }
        return SensorType.UNKNOWN.name();
    }
}
