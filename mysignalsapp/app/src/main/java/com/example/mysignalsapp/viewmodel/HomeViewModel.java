package com.example.mysignalsapp.viewmodel;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class HomeViewModel extends BaseObservable {

    private BluetoothDevice device;
    public void setBluetoothDevice(BluetoothDevice device) {
        this.device = device;
        notifyChange();
    }
    public BluetoothDevice getBluetoothDevice() {
        return device;
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
