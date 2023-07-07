package com.example.mysignalsapp.view;

import android.annotation.SuppressLint;
import android.bluetooth.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.mysignalsapp.view.HomeFragment.*;
import static com.libelium.mysignalsconnectkit.BluetoothManagerHelper.REQUEST_ENABLE_BT;


public class HomeFragment extends Fragment implements
        BluetoothManagerHelperCallback,
        BluetoothManagerServicesCallback,
        DeviceListAdapter.DeviceClickListener {


    private static final int REQUEST_BLUETOOTH_ADMIN_PERMISSION = 1;
    private static final int REQUEST_BLUETOOTH_PERMISSION = 2;
    private static final int REQUEST_BLUETOOTH_SCAN_PERMISSION = 3;
    private static final int REQUEST_BLUETOOTH_ADVERTISE_PERMISSION = 4;
    private static final int LOCATION_PERMISSION = 5;

    private BluetoothManagerService mService;
    private BluetoothManagerHelper bluetoothManager;
    private BluetoothDevice selectedDevice;
    private ArrayList<BluetoothDevice> bluetoothDeviceList;
    private final String kMySignalsId = "MySignals 000150".toLowerCase();

    private ArrayList<LBSensorObject> sensors;
    private DeviceListAdapter adapter;
    private CardView cardView;
    private Button startScanBtn;
    private Button stopScanBtn;
    private RecyclerView devicesListRecyclerView;
    private TextView scanResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {



        FragmentHomeBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_home, container, false);

        cardView = binding.getRoot().findViewById(R.id.card_view_list);
        startScanBtn = binding.getRoot().findViewById(R.id.btn_start_scan);
        stopScanBtn = binding.getRoot().findViewById(R.id.btn_stop_scan);
        scanResult = binding.getRoot().findViewById(R.id.scan_result);
        bluetoothManager = BluetoothManagerHelper.getInstance();
        bluetoothManager.setInitParameters(this, this.getContext());

        devicesListRecyclerView = binding.getRoot().findViewById(R.id.device_list);
        devicesListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bluetoothDeviceList = new ArrayList<>();
        adapter = new DeviceListAdapter(bluetoothDeviceList);
        adapter.setDeviceClickListener(this);
        devicesListRecyclerView.setAdapter(adapter);

        try {
            mService = BluetoothManagerService.getInstance();
            mService.initialize(getContext());
            mService.setServicesCallback(this);
            mService.setCharacteristicsCallback((BluetoothManagerCharacteristicsCallback) this);
            mService.setQueueCallback((BluetoothManagerQueueCallback) this);

        } catch (Exception ignored) {
        }

        isRequestBluetoothPermissionsOK();
        requestLocationPermission();

        startScanBtn.setOnClickListener(v -> {
            if (isRequestBluetoothPermissionsOK() && bluetoothManager.isBluetoothEnabled()) {
                bluetoothManager.stopLeScan();
                scanResult.setText("Bluetooth Scan Started...");
                scanBluetoothDevices();


                //bluetoothManager.startLEScan(true);
            }
        });

        stopScanBtn.setOnClickListener(v -> {
            if (isRequestBluetoothPermissionsOK()) {
                bluetoothManager.stopLeScan();
            }
        });

        return binding.getRoot();

    }

    // Check Bluetooth permissions and enable Bluetooth

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
            case REQUEST_BLUETOOTH_PERMISSION:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getContext(), "bluetooth ON", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "bluetooth OFF", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

     */

    private boolean isRequestBluetoothPermissionsOK() {
        boolean result = false;
        if (BluetoothManagerHelper.hasBluetooth(requireContext())){

            // Request Bluetooth permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(requireActivity(), new String[]{
                            Manifest.permission.BLUETOOTH_CONNECT,
                    }, REQUEST_BLUETOOTH_PERMISSION);
                }
            } else {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{
                            Manifest.permission.BLUETOOTH,
                    }, REQUEST_BLUETOOTH_PERMISSION);
                }
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{
                            Manifest.permission.BLUETOOTH_ADMIN,
                    }, REQUEST_BLUETOOTH_ADMIN_PERMISSION);
                }
            }

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(requireActivity(), new String[]{
                        Manifest.permission.BLUETOOTH_SCAN,
                }, REQUEST_BLUETOOTH_SCAN_PERMISSION);
            }

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(requireActivity(), new String[]{
                        Manifest.permission.BLUETOOTH_ADVERTISE,
                }, REQUEST_BLUETOOTH_ADVERTISE_PERMISSION);
            }

            if (!bluetoothManager.isBluetoothEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }

            result = true;
        }else {
            showToast("Device has not bluetooth feature");
        }
        return result;
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION);
        }
    }





    private void scanBluetoothDevices() {
        bluetoothDeviceList = (ArrayList<BluetoothDevice>) bluetoothManager.getBondedDevices();
        if (bluetoothDeviceList.size() > 0) {

            selectedDevice = null;

            for (BluetoothDevice deviceItem : bluetoothDeviceList) {

                String name = deviceItem.getName();

                Log.d("DEBUG", "Address: " + name);

                if (name != null) {
                    //scanResult.setText(name +"::" +Integer.toString(bluetoothDeviceList.size()));
                    if (name.toLowerCase().contains(kMySignalsId)) {
                        //showToast(name);

                        Log.d("DEBUG", "Address: " + name);
                        this.selectedDevice = deviceItem;
                        break;
                    }
                }

            }

            adapter.updateData(bluetoothDeviceList);
            cardView.setVisibility(View.VISIBLE);

            if (selectedDevice != null) {
                performConnection();
            }else {
                bluetoothManager.startLEScan(true);
            }

        }else {
            cardView.setVisibility(View.GONE);
            bluetoothManager.startLEScan(true);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onListDevicesFound(ArrayList<BluetoothDevice> devices) {
        //bluetoothDeviceList.clear();
        for (BluetoothDevice deviceItem : devices) {

            String name = deviceItem.getName();
            if (name != null) {
                //showToast("Scan Result : "+name);
                if (!bluetoothDeviceList.contains(deviceItem)) {
                    bluetoothDeviceList.add(deviceItem);
                }

                adapter.updateData(bluetoothDeviceList);
                cardView.setVisibility(View.VISIBLE);
                //adapter.updateData(bluetoothDeviceList);

                scanResult.setText(name +"::" + bluetoothDeviceList.size());
                //bluetoothManager.stopLeScan();
                if (name.toLowerCase().contains(kMySignalsId)) {
                    Log.d("FOUND!!!!!!!!!!!!!!!!", name);

                    Log.d("DEBUG", "Address: " + name);
                    this.selectedDevice = deviceItem;
                    break;
                }
                bluetoothManager.stopLeScan();
            }
        }


        if (selectedDevice != null) {
            bluetoothManager.stopLeScan();
            boolean bonded = mService.startBonding(selectedDevice);
            if (bonded) {
                showToast("Bonding starting...");
                Log.d("DEBUG", "Bonding starting...");
                performConnection();
            }
        }
        //bluetoothManager.stopLeScan();
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
            scanResult.setText("Bluetooth Scan Stopped...");
            Log.d("DEBUG", "Device connected!!");
            handler.postDelayed(postExecution, 2000);
        }
    }


    @Override
    public void onBondAuthenticationError(BluetoothGatt bluetoothGatt) {

    }

    @Override
    public void onBonded() {
        showToast("Bonded success");
    }

    @Override
    public void onBondedFailed() {
        showToast("Bonded failed");
    }

    @Override
    public void onConnectedToDevice(BluetoothDevice bluetoothDevice, int i) {
        showToast("Device connected!!");
        scanResult.setText("Device connected!!");
    }

    @Override
    public void onServicesFound(List<BluetoothGattService> list) {

    }

    @Override
    public void onDisconnectFromDevice(BluetoothDevice bluetoothDevice, int i) {
        showToast("Device disconnected!!");
    }

    @Override
    public void onReadRemoteRssi(int i, int i1) {

    }

    @Override
    public void onDeviceClick(BluetoothDevice device) {
        String name = device.getName();
        showToast(name);
        // Ouvrez le fragment DeviceFragmentInfo et transmettez les informations du dispositif
        DeviceInfoFragment deviceInfoFragment = new DeviceInfoFragment();
        deviceInfoFragment.setDevice(device);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, deviceInfoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void showToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}