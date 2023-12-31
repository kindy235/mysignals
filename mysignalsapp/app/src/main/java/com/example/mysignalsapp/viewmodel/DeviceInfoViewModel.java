package com.example.mysignalsapp.viewmodel;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class DeviceInfoViewModel extends BaseObservable {
    private BluetoothDevice device;

    public void setDevice(BluetoothDevice device) {
        this.device = device;
        notifyChange();
    }

    @SuppressLint("MissingPermission")
    @Bindable
    public String getName() {
        return device != null ? device.getName() : "";
    }

    @Bindable
    public String getAddress() {
        return device != null ? device.getAddress() : "";
    }

}
