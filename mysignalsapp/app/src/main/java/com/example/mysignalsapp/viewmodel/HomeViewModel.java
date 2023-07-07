package com.example.mysignalsapp.viewmodel;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class HomeViewModel extends BaseObservable {

    private BluetoothDevice bluetoothDevice;
    public void setBluetoothDevice(BluetoothDevice device) {
        this.bluetoothDevice = device;
        notifyChange();
    }
    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    @SuppressLint("MissingPermission")
    @Bindable
    public String getName() {
        return bluetoothDevice.getName();
    }

    @Bindable
    public String getAddress() {
        return bluetoothDevice.getAddress();
    }

}
