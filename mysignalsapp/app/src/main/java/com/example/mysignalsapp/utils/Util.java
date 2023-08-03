package com.example.mysignalsapp.utils;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.Bindable;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.adapter.MemberSpinnerAdapter;
import com.example.mysignalsapp.entity.Member;
import com.example.mysignalsapp.entity.Sensor;
import com.example.mysignalsapp.service.ApiClient;
import com.libelium.mysignalsconnectkit.pojo.LBSensorObject;
import com.libelium.mysignalsconnectkit.utils.StringConstants;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static String convertToHumanReadablePosition(int bodyposition) {
        String strPosition;
        switch (bodyposition) {
            case 1:
                strPosition = "Prone";
                break;
            case 2:
                strPosition = "Left";
                break;
            case 3:
                strPosition = "Right";
                break;
            case 4:
                strPosition = "Supine";
                break;
            case 5:
                strPosition = "Stand";
                break;
            default:
                strPosition = "Indef.";
        }

        return strPosition;
    }

    public static String getSensorDataByType(SensorDataType type, LBSensorObject sensorObject){

        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDBodyPositionSensor)) {
            if (SensorDataType.DATA.equals(type)){
                return convertToHumanReadablePosition(sensorObject.body_position_value);
            }
            if (SensorDataType.UNIT.equals(type)){
                return null;
            }
            if (SensorDataType.TYPE.equals(type)){
                return SensorType.POSITION.name();
            }
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDTemperatureSensor)) {

            if (SensorDataType.DATA.equals(type)){
                return String.valueOf(sensorObject.temperature_value);
            }
            if (SensorDataType.UNIT.equals(type)){
                return String.valueOf(sensorObject.temperature_unit);
            }
            if (SensorDataType.TYPE.equals(type)){
                return SensorType.TEMP.name();
            }
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDEMGSensor)) {
            if (SensorDataType.DATA.equals(type)){
                return String.valueOf(sensorObject.emg_value);
            }
            if (SensorDataType.UNIT.equals(type)){
                return String.valueOf(sensorObject.emg_unit);
            }
            if (SensorDataType.TYPE.equals(type)){
                return SensorType.EMG.name();
            }
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDECGSensor)) {
            if (SensorDataType.DATA.equals(type)){
                return String.valueOf(sensorObject.ecg_value);
            }
            if (SensorDataType.UNIT.equals(type)){
                return String.valueOf(sensorObject.ecg_unit);
            }
            if (SensorDataType.TYPE.equals(type)){
                return SensorType.ECG.name();
            }
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDAirflowSensor)) {
            if (SensorDataType.DATA.equals(type)){
                return String.valueOf(sensorObject.airflow_value);
            }
            if (SensorDataType.UNIT.equals(type)){
                return String.valueOf(sensorObject.airflow_unit);
            }
            if (SensorDataType.TYPE.equals(type)){
                return SensorType.AIRFLOW.name();
            }
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDGSRSensor)) {
            if (SensorDataType.DATA.equals(type)){
                return String.format("%.2f", sensorObject.gsr_capacitance_value);
            }
            if (SensorDataType.UNIT.equals(type)){
                return String.valueOf(sensorObject.gsr_capacitance_unit);
            }
            if (SensorDataType.TYPE.equals(type)){
                return SensorType.GSR.name();
            }
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDBloodPressureSensor)) {
            if (SensorDataType.DATA.equals(type)){
                return String.valueOf(sensorObject.diastolic_pressure_value);
            }
            if (SensorDataType.UNIT.equals(type)){
                return String.valueOf(sensorObject.diastolic_pressure_unit);
            }
            if (SensorDataType.TYPE.equals(type)){
                return SensorType.BLOOD.name();
            }
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDPulsiOximeterSensor)) {
            if (SensorDataType.DATA.equals(type)){
                return String.valueOf(sensorObject.pulsioximeter_heart_rate_value);
            }
            if (SensorDataType.UNIT.equals(type)){
                return String.valueOf(sensorObject.pulsioximeter_heart_rate_unit);
            }
            if (SensorDataType.TYPE.equals(type)){
                return SensorType.SPO2.name();
            }
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDGlucometerSensor)) {
            if (SensorDataType.DATA.equals(type)){
                return String.valueOf(sensorObject.glucometer_value);
            }
            if (SensorDataType.UNIT.equals(type)){
                return String.valueOf(sensorObject.glucometer_unit);
            }
            if (SensorDataType.TYPE.equals(type)){
                return SensorType.GLUCO.name();
            }
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDSpirometerSensor)) {
            if (SensorDataType.DATA.equals(type)){
                return String.valueOf(sensorObject.spirometer_num_measures_value);
            }
            if (SensorDataType.UNIT.equals(type)){
                return null;
            }
            if (SensorDataType.TYPE.equals(type)){
                return SensorType.SPIR.name();
            }
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDSnoreSensor)) {
            if (SensorDataType.DATA.equals(type)){
                return String.valueOf(sensorObject.snore_value);
            }
            if (SensorDataType.UNIT.equals(type)){
                return String.valueOf(sensorObject.snore_unit);
            }
            if (SensorDataType.TYPE.equals(type)){
                return SensorType.SNORE.name();
            }
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDScaleBLESensor)) {
            if (SensorDataType.DATA.equals(type)){
                return String.valueOf(sensorObject.scale_body_fat_value);
            }
            if (SensorDataType.UNIT.equals(type)){
                return String.valueOf(sensorObject.scale_body_fat_unit);
            }
            if (SensorDataType.TYPE.equals(type)){
                return SensorType.SCALE.name();
            }
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDBloodPressureBLESensor)) {
            if (SensorDataType.DATA.equals(type)){
                return String.valueOf(sensorObject.diastolic_pressure_ble_value);
            }
            if (SensorDataType.UNIT.equals(type)){
                return String.valueOf(sensorObject.diastolic_pressure_ble_unit);
            }
            if (SensorDataType.TYPE.equals(type)){
                return SensorType.BLOOD.name();
            }
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDPulsiOximeterBLESensor)) {
            if (SensorDataType.DATA.equals(type)){
                return String.valueOf(sensorObject.pulsioximeter_ble_heart_rate_value);
            }
            if (SensorDataType.UNIT.equals(type)){
                return String.valueOf(sensorObject.pulsioximeter_ble_heart_rate_unit);
            }
            if (SensorDataType.TYPE.equals(type)){
                return SensorType.SPO2.name();
            }
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDGlucometerBLESensor)) {
            if (SensorDataType.DATA.equals(type)){
                return String.valueOf(sensorObject.glucometer_ble_value);
            }
            if (SensorDataType.UNIT.equals(type)){
                return String.valueOf(sensorObject.glucometer_ble_unit);
            }
            if (SensorDataType.TYPE.equals(type)){
                return SensorType.GLUCO.name();
            }
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDEEGSensor)) {
            if (SensorDataType.DATA.equals(type)){
                return String.valueOf(sensorObject.electroencephalography_delta_signal_value);
            }
            if (SensorDataType.UNIT.equals(type)){
                return String.valueOf(sensorObject.electroencephalography_delta_signa_unit);
            }
            if (SensorDataType.TYPE.equals(type)){
                return SensorType.EEG.name();
            }
        }
        return null;
    }

    public static List<String> getSensorTypeList() {
        List<String> sensorTypes = new ArrayList<>();
        for (SensorType type : SensorType.values()) {
            sensorTypes.add(type.name());
        }
        return sensorTypes;
    }

    public static Drawable getImage(Context context, int res) {

        if(res != -1)
            return ResourcesCompat.getDrawable(context.getResources(),
                    res, context.getTheme());
        else
            return null;
    }

    public static int getSensorTypeResourceId(String type){

        switch (SensorType.valueOf(type)) {
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
