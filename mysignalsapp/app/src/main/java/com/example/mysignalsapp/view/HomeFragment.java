package com.example.mysignalsapp.view;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.adapter.DeviceListAdapter;
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
import java.util.Objects;

import static android.app.appsearch.AppSearchResult.RESULT_OK;


public class HomeFragment extends Fragment implements BluetoothManagerHelperCallback {


    private BluetoothManagerService mService;
    private BluetoothManagerHelper bluetoothManager;
    private BluetoothDevice selectedDevice;
    private List<BluetoothDevice> bluetoothDeviceList;
    private final String kMySignalsId = "IT100 Plus".toLowerCase();
    private ArrayList<LBSensorObject> sensors;
    private DeviceListAdapter adapter;
    private CardView cardView;

    private static final int REQUEST_BLUETOOTH_PERMISSION = 1001;


    private BluetoothAdapter bluetoothAdapter;

    public interface OnClickOnNoteListener{
        public void onClickOnNote(BluetoothDevice bluetoothDevice);
    }
    private OnClickOnNoteListener listener;

    // Check Bluetooth permissions and enable Bluetooth

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (resultCode == RESULT_OK) {
                // Bluetooth has been enabled by the user
                // Proceed with your Bluetooth operations
                // ...
            } else {
                // User denied enabling Bluetooth
                // Handle accordingly
            }
        }
    }

    private boolean hasBluetoothPermissions() {
        // Check if the required Bluetooth permissions are granted

        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;

    }


    private void requestBluetoothPermissions() {
        if (!hasBluetoothPermissions()){
            // Request Bluetooth permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{
                        Manifest.permission.BLUETOOTH_CONNECT,
                }, REQUEST_BLUETOOTH_PERMISSION);
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_ADMIN,
                }, REQUEST_BLUETOOTH_PERMISSION);
            }
        }
    }

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Bluetooth permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Bluetooth permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

     */


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        FragmentHomeBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_home, container, false);
        binding.deviceList.setLayoutManager(new LinearLayoutManager(
                binding.getRoot().getContext()));

        cardView = binding.getRoot().findViewById(R.id.card_view_list);
        requestBluetoothPermissions();
        requestLocationPermission();

        try {
            mService = BluetoothManagerService.getInstance();
            mService.initialize(getContext());
            mService.setServicesCallback((BluetoothManagerServicesCallback) this);
            mService.setCharacteristicsCallback((BluetoothManagerCharacteristicsCallback) this);
            mService.setQueueCallback((BluetoothManagerQueueCallback) this);
        } catch (Exception ignored) {
        }

        scanBluetoothDevices();

        //adapter = new DeviceListAdapter(bluetoothDeviceList, listener);
        binding.deviceList.setAdapter(adapter);

        return binding.getRoot();

    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }



    @SuppressLint("NotifyDataSetChanged")
    private void scanBluetoothDevices() {
        bluetoothManager = BluetoothManagerHelper.getInstance();
        bluetoothManager.setInitParameters(this, this.getContext());
        bluetoothDeviceList = bluetoothManager.getBondedDevices();
        if (bluetoothDeviceList.size() > 0) {
            /*
            selectedDevice = null;

            for (BluetoothDevice deviceItem : bluetoothDeviceList) {

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

             */
            //adapter.notifyDataSetChanged();
            adapter = new DeviceListAdapter(bluetoothDeviceList, listener);
            cardView.setVisibility(View.VISIBLE);

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

    public void setOnClickOnNoteListener(OnClickOnNoteListener listener)
    {
        this.listener = listener;
    }
}