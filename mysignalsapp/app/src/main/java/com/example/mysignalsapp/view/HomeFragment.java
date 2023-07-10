package com.example.mysignalsapp.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.adapter.DeviceListAdapter;
import com.example.mysignalsapp.databinding.FragmentHomeBinding;
import com.libelium.mysignalsconnectkit.BluetoothManagerHelper;
import com.libelium.mysignalsconnectkit.callbacks.BluetoothManagerHelperCallback;

import java.util.ArrayList;


@SuppressLint("MissingPermission")
public class HomeFragment extends Fragment implements
        BluetoothManagerHelperCallback,
        DeviceListAdapter.DeviceClickListener {


    private static final int REQUEST_ENABLE_BLUETOOTH= 1000;
    private static final int REQUEST_BLUETOOTH_ADMIN_PERMISSION = 1;
    private static final int REQUEST_BLUETOOTH_PERMISSION = 2;
    private static final int REQUEST_BLUETOOTH_SCAN_PERMISSION = 3;
    private static final int REQUEST_BLUETOOTH_ADVERTISE_PERMISSION = 4;
    private static final int LOCATION_PERMISSION = 5;

    private BluetoothManagerHelper bluetoothManager;
    private ArrayList<BluetoothDevice> bluetoothDeviceList;

    private DeviceListAdapter adapter;
    private CardView cardView;
    private Button startScanBtn;
    private Button stopScanBtn;
    private RecyclerView devicesListRecyclerView;
    private TextView scanResult;

    @SuppressLint("SetTextI18n")
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
        //devicesListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        bluetoothDeviceList = (ArrayList<BluetoothDevice>) bluetoothManager.getBondedDevices();
        if (bluetoothDeviceList == null){
            bluetoothDeviceList = new ArrayList<>();
        }
        adapter = new DeviceListAdapter(bluetoothDeviceList);
        adapter.setDeviceClickListener(this);
        devicesListRecyclerView.setAdapter(adapter);


        isRequestBluetoothPermissionsOK();
        requestLocationPermission();

        startScanBtn.setOnClickListener(v -> {
            if (isRequestBluetoothPermissionsOK() && bluetoothManager.isBluetoothEnabled()) {
                scanResult.setText("scanning in progress...");
                scanBluetoothDevices();
            }
        });

        stopScanBtn.setOnClickListener(v -> {
            if (isRequestBluetoothPermissionsOK()) {
                scanResult.setText("scanning stopped");
                bluetoothManager.stopLeScan();
            }
        });

        return binding.getRoot();

    }



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

            if (!bluetoothManager.isBluetoothEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
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
            adapter.updateData(bluetoothDeviceList);
            //cardView.setVisibility(View.VISIBLE);
        } // cardView.setVisibility(View.GONE);

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
                //cardView.setVisibility(View.VISIBLE);

                scanResult.setText(name +"::" + bluetoothDeviceList.size());
            }
        }
        //bluetoothManager.stopLeScan();
    }

    @Override
    public void onManagerDidNotFoundDevices() {
        //bluetoothManager.startLEScan(true);
    }


    @Override
    public void onDeviceClick(BluetoothDevice device) {

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