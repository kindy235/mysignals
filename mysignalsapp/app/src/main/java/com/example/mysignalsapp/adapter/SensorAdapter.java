package com.example.mysignalsapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.databinding.SensorItemBinding;
import com.example.mysignalsapp.view.DeviceInfoFragment;
import com.example.mysignalsapp.viewmodel.SensorViewModel;
import com.libelium.mysignalsconnectkit.pojo.LBSensorObject;
import com.libelium.mysignalsconnectkit.utils.LBValueConverter;
import com.libelium.mysignalsconnectkit.utils.StringConstants;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.ViewHolder> {

    private ArrayList<LBSensorObject> sensorList;

    public SensorAdapter(ArrayList<LBSensorObject> sensorList) {
        assert sensorList != null;
        this.sensorList = sensorList;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        SensorItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.sensor_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NotNull SensorAdapter.ViewHolder holder, int position) {

        LBSensorObject sensorObject = sensorList.get(position);
        if (sensorObject != null) {
            holder.sensorViewModel.setSensorObject(sensorObject);
        }
    }

    @Override
    public int getItemCount() {
        try {
            return sensorList.size();
        } catch (Exception ex){return 0;}
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final SensorViewModel sensorViewModel = new SensorViewModel();
        ViewHolder(SensorItemBinding sensorItemBinding) {
            super(sensorItemBinding.getRoot());

            sensorItemBinding.setSensorViewModel(sensorViewModel);
            sensorItemBinding.getRoot().setOnClickListener(view -> {
                LBSensorObject sensorObject = sensorViewModel.getSensorObject();
            });
        }
    }


    public void updateData(ArrayList<LBSensorObject> newSensorList) {
        if (newSensorList!=null) {
            sensorList = newSensorList;
            notifyDataSetChanged();
        }
    }

    public void updateValue(LBSensorObject sensorObject, Float newValue) {

        int position = sensorList.indexOf(sensorObject);

        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDBodyPositionSensor)) {
            sensorList.get(position).body_position_value = Math.round(newValue);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDTemperatureSensor)) {
            sensorList.get(position).temperature_value = newValue;
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDEMGSensor)) {
            sensorList.get(position).emg_value = Math.round(newValue);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDECGSensor)) {
            sensorList.get(position).ecg_value = Math.round(newValue);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDAirflowSensor)) {
            sensorList.get(position).airflow_value = Math.round(newValue);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDGSRSensor)) {
            sensorList.get(position).gsr_capacitance_value = newValue;
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDBloodPressureSensor)) {
            sensorList.get(position).diastolic_pressure_value = Math.round(newValue);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDPulsiOximeterSensor)) {
            sensorList.get(position).pulsioximeter_heart_rate_value = Math.round(newValue);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDGlucometerSensor)) {
            sensorList.get(position).glucometer_value = Math.round(newValue);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDSpirometerSensor)) {
            sensorList.get(position).spirometer_num_measures_value = Math.round(newValue);
        }
        if (sensorObject.uuidString.equalsIgnoreCase(StringConstants.kUUIDSnoreSensor)) {
            sensorList.get(position).snore_value = Math.round(newValue);
        }

        notifyItemChanged(position);
    }
}
