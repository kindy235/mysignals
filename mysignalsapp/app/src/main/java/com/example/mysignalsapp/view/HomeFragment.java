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
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import static android.app.Activity.RESULT_OK;
import static com.example.mysignalsapp.utils.Util.*;

@SuppressLint({"MissingPermission", "SetTextI18n"})
public class HomeFragment extends Fragment implements
        BluetoothManagerHelperCallback,
        DeviceListAdapter.DeviceClickListener {

    // Bluetooth components
    private BluetoothManagerHelper bluetoothManager;
    private ActivityResultLauncher<Intent> enableBluetoothLauncher;
    // UI components
    private DeviceListAdapter adapter;
    private TextView scanResult;

    private DeviceInfoFragment deviceInfoFragment;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // DataBinding initialization
        FragmentHomeBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_home, container, false);

        // Initialize UI components
        CardView cardView = binding.getRoot().findViewById(R.id.card_view_list);
        Button startScanBtn = binding.getRoot().findViewById(R.id.btn_start_scan);
        Button stopScanBtn = binding.getRoot().findViewById(R.id.btn_stop_scan);
        scanResult = binding.getRoot().findViewById(R.id.scan_result);

        // Initialize BluetoothManagerHelper
        bluetoothManager = BluetoothManagerHelper.getInstance();
        bluetoothManager.setInitParameters(this, this.getContext());

        // Initialize RecyclerView for devices list
        RecyclerView devicesListRecyclerView = binding.getRoot().findViewById(R.id.device_list);
        devicesListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<BluetoothDevice> bluetoothDeviceList = (ArrayList<BluetoothDevice>) bluetoothManager.getBondedDevices();
        if (bluetoothDeviceList == null) {
            bluetoothDeviceList = new ArrayList<>();
        }
        adapter = new DeviceListAdapter(bluetoothDeviceList);
        adapter.setDeviceClickListener(this);
        devicesListRecyclerView.setAdapter(adapter);

        // Request necessary permissions
        Util.requestPermissions(requireContext(), requireActivity());

        // Initialize the enableBluetoothLauncher
        enableBluetoothLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Util.showToast(getContext(), "Bluetooth activation success");
                    }
                });

        // Start scan button click listener
        startScanBtn.setOnClickListener(v -> {
            if (!bluetoothManager.isBluetoothEnabled()) {
                enableBluetooth();
            }else {
                scanResult.setText("scanning in progress...");
                scanBluetoothDevices();
            }
        });

        // Stop scan button click listener
        stopScanBtn.setOnClickListener(v -> {
            scanResult.setText("scanning stopped");
            bluetoothManager.stopLeScan();
        });

        return binding.getRoot();
    }

    // Enable Bluetooth if not enabled
    private void enableBluetooth() {
        if (!bluetoothManager.isBluetoothEnabled() && Util.requestPermissions(requireContext(), requireActivity())) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBluetoothLauncher.launch(enableBtIntent);
        }
    }

    // Start Bluetooth device scan
    private void scanBluetoothDevices() {
        for (BluetoothDevice deviceItem : bluetoothManager.getBondedDevices()) {
            String name = deviceItem.getName();
            if (name != null) {
                adapter.updateDeviceList(deviceItem);
                scanResult.setText(name + "::" + adapter.getBluetoothDeviceList().size());
            }
        }
        bluetoothManager.startLEScan(true);
    }

    @Override
    public void onListDevicesFound(ArrayList<BluetoothDevice> devices) {
        for (BluetoothDevice deviceItem : devices) {
            String name = deviceItem.getName();
            if (name != null) {
                adapter.updateDeviceList(deviceItem);
                scanResult.setText(name + "::" + adapter.getBluetoothDeviceList().size());
            }
        }
    }

    @Override
    public void onManagerDidNotFoundDevices() {
        // Handle scenario when no devices are found
    }

    @Override
    public void onDeviceClick(BluetoothDevice device) {
        // Handle device click event, open DeviceInfoFragment
        if (deviceInfoFragment==null){
            deviceInfoFragment = new DeviceInfoFragment();

        }
        deviceInfoFragment.setDevice(device);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, deviceInfoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
