package com.example.mysignalsapp.view;

import android.annotation.SuppressLint;
import android.bluetooth.*;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.adapter.SensorAdapter;
import com.example.mysignalsapp.databinding.DeviceInfoBinding;
import com.example.mysignalsapp.viewmodel.DeviceInfoViewModel;
import com.libelium.mysignalsconnectkit.BluetoothManagerService;
import com.libelium.mysignalsconnectkit.callbacks.BluetoothManagerCharacteristicsCallback;
import com.libelium.mysignalsconnectkit.callbacks.BluetoothManagerQueueCallback;
import com.libelium.mysignalsconnectkit.callbacks.BluetoothManagerServicesCallback;
import com.libelium.mysignalsconnectkit.pojo.LBSensorObject;

import java.util.ArrayList;
import java.util.List;

public class DeviceInfoFragment extends Fragment implements
        BluetoothManagerServicesCallback,
        BluetoothManagerCharacteristicsCallback,
        BluetoothManagerQueueCallback{

    private BluetoothManagerService mService;
    private ArrayList<LBSensorObject> sensorsList;
    private SensorAdapter sensorAdapter;
    private RecyclerView sensorsRecyclerView;
    private BluetoothDevice device;
    private Button connectBtn;
    private boolean isConnected;
    TextView connectResult;

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
        connectBtn = binding.getRoot().findViewById(R.id.btn_connect);
        isConnected = false;
        connectResult = binding.getRoot().findViewById(R.id.connect_result);

        sensorsRecyclerView = binding.getRoot().findViewById(R.id.sensors_recycler_view);
        sensorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sensorsList = new ArrayList<>();
        sensorAdapter = new SensorAdapter(sensorsList);
        sensorsRecyclerView.setAdapter(sensorAdapter);

        try {
            mService = BluetoothManagerService.getInstance();
            mService.initialize(getContext());
            mService.setServicesCallback(this);
            mService.setCharacteristicsCallback(this);
            mService.setQueueCallback(this);
        } catch (Exception ignored) {
        }

        connectBtn.setOnClickListener(v -> {
            if (!isConnected) {
                Toast.makeText(getContext(), "Connection in progress...", Toast.LENGTH_SHORT).show();
                connectResult.setVisibility(View.VISIBLE);
                performConnection();
            }else {
                Toast.makeText(getContext(), "disconnection in progress...", Toast.LENGTH_SHORT).show();
                if (mService != null) {
                    mService.disconnectDevice();
                }
            }

        });

        return binding.getRoot();
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }


    @SuppressLint("SetTextI18n")
    private void performConnection() {
        final Handler handler = new Handler();
        final Runnable postExecution = () -> {
            try {
                if (mService != null) {
                    if (mService.discoverServices()) {
                        Log.d("DEBUG", "Device discoverServices: " + device.getAddress());
                    }
                }
            } catch (Exception ignored) {
            }
        };

        if (mService.getConnectedDevices() != null && mService.getConnectedDevices().contains(device)){
            connectResult.setText("Device already connected");
        }else {
            boolean bonded = mService.startBonding(device);
            if (bonded) {
                Toast.makeText(getContext(), "Bonding starting...", Toast.LENGTH_SHORT).show();
                Log.d("DEBUG", "Bonding starting...");
            }

            if (mService.connectToDevice(device, getContext())) {
                Log.d("DEBUG", "Device connected!!");
                handler.postDelayed(postExecution, 2000);
            }else {
                connectResult.setText("Connection failed");
            }
        }
    }


    // BluetoothManagerServicesCallback

    @Override
    public void onBondAuthenticationError(BluetoothGatt bluetoothGatt) {

    }

    @Override
    public void onBonded() {
        int k = 0;
    }

    @Override
    public void onBondedFailed() {
        int i = 0;
    }

    @Override
    public void onConnectedToDevice(BluetoothDevice bluetoothDevice, int i) {

        isConnected = true;
        connectResult.setText("Connection success");
        connectBtn.setText("Disconnect");

    }

    @Override
    public void onServicesFound(List<BluetoothGattService> list) {

    }

    @Override
    public void onDisconnectFromDevice(BluetoothDevice bluetoothDevice, int i) {
        isConnected = false;
        connectResult.setText("device disconnected");
        connectBtn.setText("Connect");
    }

    @Override
    public void onReadRemoteRssi(int i, int i1) {

    }

    //BluetoothManagerCharacteristicsCallback

    @Override
    public void onCharacteristicsFound(List<BluetoothGattCharacteristic> list, BluetoothGattService bluetoothGattService) {
        Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCharacteristicChanged(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCharacteristicSubscribed(BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean b) {
        Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCharacteristicWritten(BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinishWriteAllCharacteristics() {
        Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartWriteCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
    }

    // BluetoothManagerQueueCallback
    @Override
    public void onStartWriteQueueDescriptor(BluetoothGattDescriptor bluetoothGattDescriptor) {
        Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartReadCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        byte[] b = bluetoothGattCharacteristic.getValue();
        Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinishWriteAllDescriptors() {
        Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinishReadAllCharacteristics() {
        Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCharacteristicChangedQueue(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
    }

}