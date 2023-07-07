package com.example.mysignalsapp.view;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.databinding.DeviceInfoBinding;
import com.example.mysignalsapp.viewmodel.DeviceInfoViewModel;

public class DeviceInfoFragment extends Fragment {

    private BluetoothDevice device;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        DeviceInfoBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.device_info, container, false);


        DeviceInfoViewModel deviceInfoViewModel = new DeviceInfoViewModel();
        deviceInfoViewModel.setDevice(device);
        binding.setDeviceInfoViewModel(deviceInfoViewModel);

        return binding.getRoot();
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }
}