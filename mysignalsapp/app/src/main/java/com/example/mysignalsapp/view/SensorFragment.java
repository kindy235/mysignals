package com.example.mysignalsapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.databinding.FragmentSensorBinding;
import com.example.mysignalsapp.viewmodel.SensorViewModel;

public class SensorFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        FragmentSensorBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_sensor, container, false);

        SensorViewModel sensorViewModel = new SensorViewModel();

        binding.setSensorViewModel(sensorViewModel);

        return binding.getRoot();
    }
}