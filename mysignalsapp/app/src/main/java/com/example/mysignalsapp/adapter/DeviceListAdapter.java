package com.example.mysignalsapp.adapter;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.databinding.DeviceItemBinding;
import com.example.mysignalsapp.view.HomeFragment;
import com.example.mysignalsapp.viewmodel.HomeViewModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder>{

    private ArrayList<BluetoothDevice> bluetoothDeviceList;

    public interface DeviceClickListener {
        void onDeviceClick(BluetoothDevice device);
    }

    private DeviceClickListener listener;

    public DeviceListAdapter(ArrayList<BluetoothDevice> bluetoothDeviceList) {
        assert bluetoothDeviceList != null;
        this.bluetoothDeviceList = bluetoothDeviceList;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        DeviceItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.device_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {

        BluetoothDevice device = bluetoothDeviceList.get(position);
        if (device != null) {
            holder.homeViewModel.setBluetoothDevice(device);
        }
    }

    @Override
    public int getItemCount() {
        try {
            return bluetoothDeviceList.size();
        } catch (Exception ex){return 0;}
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final HomeViewModel homeViewModel = new HomeViewModel();
        ViewHolder(DeviceItemBinding deviceItemBinding) {
            super(deviceItemBinding.getRoot());

            deviceItemBinding.setHomeViewModel(homeViewModel);
            deviceItemBinding.getRoot().setOnClickListener(view -> {

                BluetoothDevice bluetoothDevice = homeViewModel.getBluetoothDevice();
                if (listener != null && bluetoothDevice!=null)
                    listener.onDeviceClick(bluetoothDevice);
            });

        }
    }

    public void setDeviceClickListener(DeviceClickListener listener) {
        this.listener = listener;
    }

    public void updateData(ArrayList<BluetoothDevice> newBluetoothDeviceList) {
        if (newBluetoothDeviceList!=null) {
            bluetoothDeviceList = newBluetoothDeviceList;
            notifyDataSetChanged();
        }
    }


}
