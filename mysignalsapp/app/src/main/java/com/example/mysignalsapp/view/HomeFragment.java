package com.example.mysignalsapp.view;

import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.viewmodel.HomeViewModel;
import com.libelium.mysignalsconnectkit.BluetoothManagerHelper;
import com.libelium.mysignalsconnectkit.BluetoothManagerService;
import com.libelium.mysignalsconnectkit.callbacks.BluetoothManagerCharacteristicsCallback;
import com.libelium.mysignalsconnectkit.callbacks.BluetoothManagerHelperCallback;
import com.libelium.mysignalsconnectkit.callbacks.BluetoothManagerQueueCallback;
import com.libelium.mysignalsconnectkit.callbacks.BluetoothManagerServicesCallback;
import com.libelium.mysignalsconnectkit.pojo.LBSensorObject;
import com.example.mysignalsapp.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements BluetoothManagerHelperCallback {



    private BluetoothManagerService mService;
    private BluetoothManagerHelper bluetoothManager;
    private BluetoothDevice selectedDevice;
    private final String kMySignalsId = "IT100 Plus".toLowerCase();
    private ArrayList<LBSensorObject> sensors;


    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;

// Inside your HomeFragment


    private void requestBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_BLUETOOTH_PERMISSION);
            } else {
                // Bluetooth permission already granted
                // Proceed with Bluetooth operations
            }
        } else {
            // Device is running an older Android version (pre-Marshmallow)
            // Bluetooth permission is granted by default
            // Proceed with Bluetooth operations
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Bluetooth permission granted
                // Proceed with Bluetooth operations
            } else {
                // Bluetooth permission denied
                // Handle the case accordingly
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        FragmentHomeBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_home, container, false);

        HomeViewModel homeViewModel = new HomeViewModel();
        binding.setHomeViewModel(homeViewModel);

        requestLocationPermission();
        requestBluetoothPermission();

        /*
        try {
            mService = BluetoothManagerService.getInstance();
            mService.initialize(getContext());
            mService.setServicesCallback((BluetoothManagerServicesCallback) this);
            mService.setCharacteristicsCallback((BluetoothManagerCharacteristicsCallback) this);
            mService.setQueueCallback((BluetoothManagerQueueCallback) this);
        } catch (Exception ignored) {
        }

        //checkBluetoothPermissions();

        scanBluetoothDevices();

         */

        return binding.getRoot();

    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }


    private void scanBluetoothDevices() {
        bluetoothManager = BluetoothManagerHelper.getInstance();
        bluetoothManager.setInitParameters(this, this.getContext());
        List<BluetoothDevice> devicesBonded = bluetoothManager.getBondedDevices();
        if (devicesBonded.size() > 0) {
            selectedDevice = null;
            for (BluetoothDevice deviceItem : devicesBonded) {

                String name = deviceItem.getName();
                Log.d("DEBUG", "Address: " + name);

                if (name != null) {
                    if (name.toLowerCase().contains(kMySignalsId)) {
                        Log.d("DEBUG", "Address: " + name);
                        this.selectedDevice = deviceItem;
                        break;
                    }
                }
            }
            if (selectedDevice != null) {
                performConnection();
            }else {
                bluetoothManager.startLEScan(true);
            }
        }else {
            bluetoothManager.startLEScan(true);
        }
    }

    @Override
    public void onListDevicesFound(ArrayList<BluetoothDevice> devices) {
        for (BluetoothDevice deviceItem : devices) {
            String name = deviceItem.getName();
            if (name != null) {
                if (name.toLowerCase().contains(kMySignalsId)) {
                    Log.d("DEBUG", "Address: " + name);
                    this.selectedDevice = deviceItem;
                    break;
                }
            }
        }
        if (selectedDevice != null) {
            bluetoothManager.stopLeScan();
            boolean bonded = mService.startBonding(selectedDevice);
            if (bonded) {
                Log.d("DEBUG", "Bonding starting...");
                performConnection();
            }
        }
    }

    @Override
    public void onManagerDidNotFoundDevices() {
        //bluetoothManager.startLEScan(true);
    }

    private void performConnection() {
        final Handler handler = new Handler();
        final Runnable postExecution = new Runnable() {
            @Override
            public void run() {
                try {
                    if (mService != null) {
                        if (mService.discoverServices()) {
                            Log.d("DEBUG", "Device discoverServices: " + selectedDevice.getAddress());
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        };
        if (mService.connectToDevice(selectedDevice, getContext())) {
            Log.d("DEBUG", "Device connected!!");
            handler.postDelayed(postExecution, 2000);
        }
    }
}