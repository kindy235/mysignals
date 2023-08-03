package com.example.mysignalsapp.utils;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
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

    public static void postSensor(Sensor sensor, Member member, Context context) {
        ApiClient apiClient = new ApiClient();
        apiClient.getApiService(context).postSensor(member.getId(), sensor)
                .enqueue(new Callback<Sensor>() {
                    @Override
                    public void onResponse(@NotNull Call<Sensor> call, @NotNull Response<Sensor> response) {
                        Sensor sensors = response.body();
                        if (response.isSuccessful()) {
                            //Sensor sensors = response.body();
                            Toast.makeText(context, "Data Upload success", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Sensor> call, @NotNull Throwable t) {
                        Log.d("FETCH", t.getMessage());
                    }
                });
    }

    public static void postMember(Member member, Context context) {
        ApiClient apiClient = new ApiClient();
        apiClient.getApiService(context).postMember(member).enqueue(new Callback<Member>() {
            @Override
            public void onResponse(@NotNull Call<Member> call, @NotNull Response<Member> response) {
                if (response.isSuccessful()){
                    Member member = response.body();
                    Toast.makeText(context, "Member added to the cloud API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Member> call, @NotNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void deleteMember(Long memberId, Context context) {
        ApiClient apiClient = new ApiClient();
        apiClient.getApiService(context).deleteMember(memberId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "Member deleted from the cloud API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
