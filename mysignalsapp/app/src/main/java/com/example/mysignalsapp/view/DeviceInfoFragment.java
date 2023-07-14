package com.example.mysignalsapp.view;

import android.annotation.SuppressLint;
import android.app.Application;
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
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.libelium.mysignalsconnectkit.utils.BitManager;
import com.libelium.mysignalsconnectkit.utils.LBValueConverter;
import com.libelium.mysignalsconnectkit.utils.StringConstants;
import com.libelium.mysignalsconnectkit.utils.Utils;

import java.lang.reflect.Field;
import java.util.*;

@SuppressLint("SetTextI18n")
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
    private TextView connectResult;

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
        //sensorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sensorsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        sensorsList = new ArrayList<>();
        LBSensorObject object = LBSensorObject.newInstance();
        object.tag = 1;
        object.tickStatus = true;
        object.uuidString = StringConstants.kUUIDBodyPositionSensor;
        LBSensorObject.preloadValues(object);
        sensorsList.add(object);
        //initializeSensorsArrayList();
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

    private void initializeSensorsArrayList() {
        sensorsList = new ArrayList<>();
        for (Field field: StringConstants.class.getDeclaredFields()){
            try {

                String uuid = Objects.requireNonNull(field.get(StringConstants.class)).toString();
                if (!StringConstants.kServiceMainUUID.equalsIgnoreCase(uuid ) &&
                        !StringConstants.kSensorList.equalsIgnoreCase(uuid) &&
                        !uuid.equals(StringConstants.kUUIDScaleBLESensor) &&
                        !uuid.equals(StringConstants.kUUIDBloodPressureBLESensor) &&
                        !uuid.equals(StringConstants.kUUIDPulsiOximeterBLESensor)&&
                        !uuid.equals(StringConstants.kUUIDGlucometerBLESensor) &&
                        !uuid.equals(StringConstants.kUUIDEEGSensor)) {
                    LBSensorObject object = LBSensorObject.newInstance();
                    LBSensorObject.preloadValues(object);
                    object.uuidString = uuid;
                    object.tickStatus = true;
                    object.bluetooth_mode = LBSensorObject.BluetoothMode.BLUETOOTH_MODE_REAL_TIME;
                    sensorsList.add(object);
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }


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

        if (mService.startBonding(device)) {
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

    // BluetoothManagerServicesCallback

    @Override
    public void onBondAuthenticationError(BluetoothGatt bluetoothGatt) {
        connectResult.setText("onBondAuthenticationError");
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
    public void onServicesFound(List<BluetoothGattService> services) {

        for (BluetoothGattService service: services) {
            if (service.getUuid().toString().equalsIgnoreCase(StringConstants.kServiceMainUUID)) {
                BitManager bitManager = BitManager.newObject();
                bitManager.objectByte = BitManager.createByteObjectFromSensors(
                        sensorsList, BitManager.BLUETOOTH_DISPLAY_MODE.BLUETOOTH_DISPLAY_MODE_GENERAL, getContext());
                byte[] data = BitManager.convertToData(bitManager.objectByte);
                String dataString = Arrays.toString(data);
                String hexByte = Utils.toHexString(data);
                Log.d("DEBUG onCharaFound", "hex dataString value: " + hexByte);
                Log.d("DEBUG onCharaFound", "dataString: " + dataString);
                for (LBSensorObject sensor: sensorsList) {
                    for (BluetoothGattCharacteristic characteristic: service.getCharacteristics()) {
                        if (sensor.uuidString.equalsIgnoreCase(characteristic.getUuid().toString()) && sensor.tickStatus) {
                            mService.writeCharacteristicQueue(characteristic, data);
                            mService.writeCharacteristicSubscription(characteristic,true);
                        }
                    }
                }

                mService.readCharacteristicsForService(service);
            }
        }
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
    public void onCharacteristicsFound(List<BluetoothGattCharacteristic> characteristics, BluetoothGattService bluetoothGattService) {
        connectResult.setText("onCharacteristicsFound");
        /*
        if (bluetoothGattService.getUuid().toString().equalsIgnoreCase(StringConstants.kServiceMainUUID)) {
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                mService.writeCharacteristicSubscription(characteristic,true);
            }
        }

         */
    }

    @Override
    public void onCharacteristicChanged(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        connectResult.setText("onCharacteristicChanged");
        try {
            String uuid = bluetoothGattCharacteristic.getUuid().toString().toUpperCase();
            byte[] value = bluetoothGattCharacteristic.getValue();
            if (value == null) {
                return;
            }
            if (uuid.equals(StringConstants.kUUIDBodyPositionSensor)) {
                HashMap<String, String> dataDict = LBValueConverter.
                        manageValuePosition(value);
                Log.d("DEBUG", "kUUIDBodyPositionSensor dict: " + dataDict);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        byte[] b = bluetoothGattCharacteristic.getValue();
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