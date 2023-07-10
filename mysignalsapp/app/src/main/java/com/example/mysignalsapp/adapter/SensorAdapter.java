package com.example.mysignalsapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.databinding.SensorItemBinding;
import com.example.mysignalsapp.viewmodel.SensorViewModel;
import com.libelium.mysignalsconnectkit.pojo.LBSensorObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
}
