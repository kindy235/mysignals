package com.example.mysignalsapp.view;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.adapter.DeviceListAdapter;
import com.example.mysignalsapp.databinding.FragmentHomeBinding;
import com.example.mysignalsapp.utils.Util;
import com.libelium.mysignalsconnectkit.BluetoothManagerHelper;
import com.libelium.mysignalsconnectkit.callbacks.BluetoothManagerHelperCallback;

import java.util.ArrayList;

import static com.example.mysignalsapp.utils.Util.*;


@SuppressLint("MissingPermission")
public class HomeFragment extends Fragment implements
        BluetoothManagerHelperCallback,
        DeviceListAdapter.DeviceClickListener {
    private static final int REQUEST_ENABLE_BLUETOOTH = 1000;
    private BluetoothManagerHelper bluetoothManager;
    private ArrayList<BluetoothDevice> bluetoothDeviceList;

    private DeviceListAdapter adapter;
    private TextView scanResult;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentHomeBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_home, container, false);

        CardView cardView = binding.getRoot().findViewById(R.id.card_view_list);
        Button startScanBtn = binding.getRoot().findViewById(R.id.btn_start_scan);
        Button stopScanBtn = binding.getRoot().findViewById(R.id.btn_stop_scan);
        scanResult = binding.getRoot().findViewById(R.id.scan_result);

        bluetoothManager = BluetoothManagerHelper.getInstance();
        bluetoothManager.setInitParameters(this, this.getContext());

        RecyclerView devicesListRecyclerView = binding.getRoot().findViewById(R.id.device_list);
        devicesListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //devicesListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        bluetoothDeviceList = (ArrayList<BluetoothDevice>) bluetoothManager.getBondedDevices();
        if (bluetoothDeviceList == null){
            bluetoothDeviceList = new ArrayList<>();
        }
        adapter = new DeviceListAdapter(bluetoothDeviceList);
        adapter.setDeviceClickListener(this);
        devicesListRecyclerView.setAdapter(adapter);

        Util.requestPermissions(requireContext(), requireActivity());

        startScanBtn.setOnClickListener(v -> {
            if (bluetoothManager.isBluetoothEnabled()) {
                scanResult.setText("scanning in progress...");
                enableBluetooth();
                scanBluetoothDevices();
            }else {
                showToast(getContext(), "Bluetooth adapter is disable");
            }
        });

        stopScanBtn.setOnClickListener(v -> {
            if (bluetoothManager.isBluetoothEnabled()) {
                scanResult.setText("scanning stopped");
                bluetoothManager.stopLeScan();
            }
        });

        return binding.getRoot();
    }


    private void enableBluetooth() {
        if (!bluetoothManager.isBluetoothEnabled() && Util.requestPermissions(requireContext(), requireActivity())) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
            //registerForActivityResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
        }
    }

    private void scanBluetoothDevices() {
        bluetoothDeviceList = (ArrayList<BluetoothDevice>) bluetoothManager.getBondedDevices();
        if (bluetoothDeviceList.size() > 0) {
            adapter.updateData(bluetoothDeviceList);
        }
        bluetoothManager.startLEScan(true);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onListDevicesFound(ArrayList<BluetoothDevice> devices) {
        for (BluetoothDevice deviceItem : devices) {
            String name = deviceItem.getName();
            if (name != null) {
                if (!bluetoothDeviceList.contains(deviceItem)) {
                    bluetoothDeviceList.add(deviceItem);
                }
                adapter.updateData(bluetoothDeviceList);
                scanResult.setText(name +"::" + bluetoothDeviceList.size());
            }
        }
    }

    @Override
    public void onManagerDidNotFoundDevices() {}

    @Override
    public void onDeviceClick(BluetoothDevice device) {

        DeviceInfoFragment deviceInfoFragment = new DeviceInfoFragment();
        deviceInfoFragment.setDevice(device);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, deviceInfoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}