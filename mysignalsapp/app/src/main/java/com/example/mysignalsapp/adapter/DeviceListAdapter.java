package com.example.mysignalsapp.adapter;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.databinding.DeviceItemBinding;
import com.example.mysignalsapp.view.HomeFragment;
import com.example.mysignalsapp.viewmodel.HomeViewModel;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder>{

    private HomeFragment.OnClickOnNoteListener listener;
    private List<BluetoothDevice> bluetoothDeviceList;

    public DeviceListAdapter(List<BluetoothDevice> bluetoothDeviceList, HomeFragment.OnClickOnNoteListener listener) {
        //assert bluetoothDeviceList != null;
        this.bluetoothDeviceList = bluetoothDeviceList;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        DeviceItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.device_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DeviceListAdapter.ViewHolder holder, int position) {

        if (bluetoothDeviceList.get(position) != null) {
            BluetoothDevice bluetoothDevice = bluetoothDeviceList.get(position);
            holder.homeViewModel.setBluetoothDevice(bluetoothDevice);
        }
    }

    @Override
    public int getItemCount() {
        return bluetoothDeviceList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private DeviceItemBinding binding;
        TextView deviceName;
        private HomeViewModel homeViewModel = new HomeViewModel();
        ViewHolder(DeviceItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
            this.binding.setHomeViewModel(homeViewModel);
            this.binding.getRoot().setOnClickListener(view -> {
                BluetoothDevice bluetoothDevice = homeViewModel.getBluetoothDevice();
                if (listener != null)
                    listener.onClickOnNote(bluetoothDevice);
            });

        }
    }
}
