package com.example.mysignalsapp.viewmodel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.example.mysignalsapp.R;
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

    public SensorType getSensorType() {
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDBodyPositionSensor)) {
            return SensorType.POSITION;
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDTemperatureSensor)) {
            return SensorType.TEMP;
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDEMGSensor)) {
            return SensorType.EMG;
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDECGSensor)) {
            return SensorType.ECG;
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDAirflowSensor)) {
            return SensorType.AIRFLOW;
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDGSRSensor)) {
            return SensorType.GSR;
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDBloodPressureSensor)) {
            return SensorType.BLOOD;
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDPulsiOximeterSensor)) {
            return SensorType.SPO2;
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDGlucometerSensor)) {
            return SensorType.GLUCO;
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDSpirometerSensor)) {
            return SensorType.SPIR;
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDSnoreSensor)) {
            return SensorType.SNORE;
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDScaleBLESensor)) {
            return SensorType.SCALE;
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDBloodPressureBLESensor)) {
            return SensorType.BLOOD;
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDPulsiOximeterBLESensor)) {
            return SensorType.SPO2;
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDGlucometerBLESensor)) {
            return SensorType.GLUCO;
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDEEGSensor)) {
            return SensorType.EEG;
        }
        return SensorType.UNKNOWN;
    }

    @Bindable
    public String getType(){
       return getSensorType().name();
    }

    public Drawable getImage(Context context, int res) {

        if(res != -1)
            return ResourcesCompat.getDrawable(context.getResources(),
                    res, context.getTheme());
        else
            return null;
    }

    @Bindable
    public int getResourceId(){

        switch (getSensorType()) {
            case AIRFLOW:
                return R.drawable.airflow; // Replace with the actual resource ID for the airflow image
            case ECG:
                return R.drawable.ecg; // Replace with the actual resource ID for the airflow image
            case EMG:
                return R.drawable.emg; // Replace with the actual resource ID for the airflow image
            case GSR:
                return R.drawable.gsr; // Replace with the actual resource ID for the airflow image
            case POSITION:
                return R.drawable.position; // Replace with the actual resource ID for the airflow image
            case SNORE:
                return R.drawable.snore; // Replace with the actual resource ID for the airflow image
            case TEMP:
                return R.drawable.temperature;
            case SPIR:
                return R.drawable.spir; // Replace with the actual resource ID for the airflow image
            case EEG:
                return R.drawable.eeg; // Replace with the actual resource ID for the airflow image
            case SPO2:
                return R.drawable.spo; // Replace with the actual resource ID for the airflow image
            case BLOOD:
                return R.drawable.blood; // Replace with the actual resource ID for the airflow image
            case GLUCO:
                return R.drawable.gluco; // Replace with the actual resource ID for the airflow image
            case SCALE:
                return R.drawable.scale; // Replace with the actual resource ID for the airflow image
            default:
                return R.drawable.ic_sensors;
        }
    }
}
