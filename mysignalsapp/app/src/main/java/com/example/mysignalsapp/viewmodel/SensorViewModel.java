package com.example.mysignalsapp.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.utils.SensorDataType;
import com.example.mysignalsapp.utils.SensorType;
import com.example.mysignalsapp.utils.Util;
import com.libelium.mysignalsconnectkit.pojo.LBSensorObject;
import com.libelium.mysignalsconnectkit.utils.StringConstants;

import static com.libelium.mysignalsconnectkit.pojo.LBSensorObject.convertToHumanReadablePosition;


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
        if (Util.getSensorDataByType(SensorDataType.UNIT, getSensorObject()) != null) {
            return Util.getSensorDataByType(SensorDataType.DATA, sensorObject) + " "+ Util.getSensorDataByType(SensorDataType.UNIT, sensorObject);
        }
        return Util.getSensorDataByType(SensorDataType.DATA, sensorObject);
    }

    @Bindable
    public String getSensorType(){
       return Util.getSensorDataByType(SensorDataType.TYPE, sensorObject);
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

        switch (SensorType.valueOf(Util.getSensorDataByType(SensorDataType.TYPE, sensorObject))) {
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
