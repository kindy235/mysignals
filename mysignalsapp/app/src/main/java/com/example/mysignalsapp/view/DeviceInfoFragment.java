package com.example.mysignalsapp.view;

import android.annotation.SuppressLint;
import android.bluetooth.*;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.adapter.MemberSpinnerAdapter;
import com.example.mysignalsapp.adapter.SensorListAdapter;
import com.example.mysignalsapp.databinding.DeviceInfoBinding;
import com.example.mysignalsapp.entity.Member;
import com.example.mysignalsapp.entity.Sensor;
import com.example.mysignalsapp.service.ApiClient;
import com.example.mysignalsapp.utils.ApiRequests;
import com.example.mysignalsapp.utils.SensorDataType;
import com.example.mysignalsapp.utils.Util;
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
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SuppressLint("SetTextI18n")
public class DeviceInfoFragment extends Fragment implements
        BluetoothManagerServicesCallback,
        BluetoothManagerCharacteristicsCallback,
        BluetoothManagerQueueCallback {

    @SuppressLint("StaticFieldLeak")
    private static BluetoothManagerService mService = null;

    private BluetoothGattService selectedService;
    private ArrayList<LBSensorObject> selectedSensors;
    private ArrayList<BluetoothGattCharacteristic> notifyCharacteristics;
    private boolean writtenService;
    private BluetoothGattCharacteristic characteristicSensorList;

    private Timer timerRssi;
    private SensorListAdapter sensorListAdapter;
    private BluetoothDevice device;
    private Button connectBtn;
    private boolean isConnected;
    private TextView connectResult;
    private Spinner spinner;
    private MemberSpinnerAdapter memberSpinnerAdapter;
    private ArrayList<Member> members;
    private Member selectedMember;

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

        RecyclerView sensorsRecyclerView = binding.getRoot().findViewById(R.id.sensors_recycler_view);
        spinner = binding.getRoot().findViewById(R.id.spinner);
        spinner.setPrompt("Select a member");

        connectBtn = binding.getRoot().findViewById(R.id.btn_connect);
        connectResult = binding.getRoot().findViewById(R.id.connect_result);
        notifyCharacteristics = new ArrayList<>();
        isConnected = false;

        //sensorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sensorsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        selectedSensors = createSensorsDisplay();

        sensorListAdapter = new SensorListAdapter(selectedSensors);
        sensorsRecyclerView.setAdapter(sensorListAdapter);

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
            } else {
                Toast.makeText(getContext(), "disconnection in progress...", Toast.LENGTH_SHORT).show();
                if (mService != null) {
                    mService.disconnectDevice();
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Member member = (Member) parent.getSelectedItem();
                if (member != null){
                    selectedMember = member;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getAllMembers();

        return binding.getRoot();
    }

    private ArrayList<LBSensorObject> createSensorsDisplay1() {
        int maxNotifications = Utils.getMaxNotificationNumber();
        ArrayList<LBSensorObject> sensors = new ArrayList<>();
        int count = 1;

        try {
            for (Field field : StringConstants.class.getDeclaredFields()) {
                String uuid = Objects.requireNonNull(field.get(StringConstants.class)).toString();
                if (!StringConstants.kServiceMainUUID.equalsIgnoreCase(uuid) &&
                        !StringConstants.kSensorList.equalsIgnoreCase(uuid) &&
                        !uuid.equals(StringConstants.kUUIDScaleBLESensor) &&
                        !uuid.equals(StringConstants.kUUIDBloodPressureBLESensor) &&
                        !uuid.equals(StringConstants.kUUIDPulsiOximeterBLESensor) &&
                        !uuid.equals(StringConstants.kUUIDGlucometerBLESensor) &&
                        !uuid.equals(StringConstants.kUUIDEEGSensor)) {

                    LBSensorObject object = LBSensorObject.newInstance();
                    object.tag = count;
                    object.tickStatus = count <= maxNotifications;
                    object.uuidString = uuid;
                    LBSensorObject.preloadValues(object);
                    sensors.add(object);
                    count++;
                }
            }
            return sensors;

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    private ArrayList<LBSensorObject> createSensorsDisplay() {

        int maxNotifications = Utils.getMaxNotificationNumber();

        ArrayList<LBSensorObject> sensors = new ArrayList<>();

        LBSensorObject object = LBSensorObject.newInstance();

        object.tag = 1;
        object.tickStatus = true;
        object.uuidString = StringConstants.kUUIDBodyPositionSensor;

        LBSensorObject.preloadValues(object);
        sensors.add(object);

        object = LBSensorObject.newInstance();

        object.tag = 2;
        object.tickStatus = true;
        object.uuidString = StringConstants.kUUIDTemperatureSensor;

        LBSensorObject.preloadValues(object);
        sensors.add(object);

        object = LBSensorObject.newInstance();

        object.tag = 3;
        object.tickStatus = true;
        object.uuidString = StringConstants.kUUIDEMGSensor;

        LBSensorObject.preloadValues(object);
        sensors.add(object);

        object = LBSensorObject.newInstance();

        object.tag = 4;
        object.tickStatus = true;
        object.uuidString = StringConstants.kUUIDECGSensor;

        LBSensorObject.preloadValues(object);
        sensors.add(object);

        object = LBSensorObject.newInstance();

        object.tag = 5;
        object.tickStatus = maxNotifications > 4;
        object.uuidString = StringConstants.kUUIDAirflowSensor;

        LBSensorObject.preloadValues(object);
        sensors.add(object);

        object = LBSensorObject.newInstance();

        object.tag = 6;
        object.tickStatus = maxNotifications > 4;
        object.uuidString = StringConstants.kUUIDGSRSensor;

        LBSensorObject.preloadValues(object);
        sensors.add(object);

        object = LBSensorObject.newInstance();

        object.tag = 7;
        object.tickStatus = maxNotifications > 4;
        object.uuidString = StringConstants.kUUIDBloodPressureSensor;

        LBSensorObject.preloadValues(object);
        sensors.add(object);

        object = LBSensorObject.newInstance();

        object.tag = 8;
        object.tickStatus = maxNotifications > 7;
        object.uuidString = StringConstants.kUUIDPulsiOximeterSensor;

        LBSensorObject.preloadValues(object);
        sensors.add(object);

        object = LBSensorObject.newInstance();

        object.tag = 9;
        object.tickStatus = maxNotifications > 7;
        object.uuidString = StringConstants.kUUIDGlucometerSensor;

        LBSensorObject.preloadValues(object);
        sensors.add(object);

        object = LBSensorObject.newInstance();

        object.tag = 10;
        object.tickStatus = maxNotifications > 7;
        object.uuidString = StringConstants.kUUIDSpirometerSensor;

        LBSensorObject.preloadValues(object);
        sensors.add(object);

        object = LBSensorObject.newInstance();

        object.tag = 11;
        object.tickStatus = maxNotifications > 7;
        object.uuidString = StringConstants.kUUIDSnoreSensor;

        LBSensorObject.preloadValues(object);
        sensors.add(object);

        return sensors;
    }


    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    private void performConnection() {
        Executor executor = Executors.newSingleThreadExecutor();

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
            executor.execute(() -> {
                try {
                    Thread.sleep(2000);
                    postExecution.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } else {
            connectResult.setText("Connection failed");
        }
    }

    /**
     * Scheduler method to update and query RSSI value
     */
    private void scheduleRSSIReader() {

        if (timerRssi != null) {

            timerRssi.cancel();
            timerRssi.purge();
            timerRssi = null;
        }

        timerRssi = new Timer();
        timerRssi.schedule(new TimerTask() {

            @Override
            public void run() {

                if (mService != null) {

                    mService.readRemoteRSSI();
                }
            }
        }, 1000, 1000);
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
        //notifyCharacteristics.clear();
    }

    @Override
    public void onServicesFound(List<BluetoothGattService> services) {

        if (services != null) {

            selectedService = null;

            for (BluetoothGattService service : services) {

                String uuidService = service.getUuid().toString().toUpperCase();

                if (uuidService.equals(StringConstants.kServiceMainUUID)) {
                    selectedService = service;
                    break;
                }
            }

            if (selectedService != null) {
                writtenService = false;
                mService.readCharacteristicsForService(selectedService);
            }
        }
    }

    @Override
    public void onDisconnectFromDevice(BluetoothDevice bluetoothDevice, int i) {
        isConnected = false;
        connectResult.setText("device disconnected");
        connectBtn.setText("Connect");
        //mService.close();
    }

    @Override
    public void onReadRemoteRssi(int i, int i1) {
        Log.d("DEBUG", "RSSI: " + i + " dBm - Status: " + i1);
    }

    //BluetoothManagerCharacteristicsCallback

    @Override
    public void onCharacteristicsFound(List<BluetoothGattCharacteristic> characteristics, BluetoothGattService service) {
        //connectResult.setText("onCharacteristicsFound");
        if (service.getUuid().toString().toUpperCase().equals(StringConstants.kServiceMainUUID)) {

            if (!writtenService) {

                characteristicSensorList = null;
                writtenService = true;

                for (BluetoothGattCharacteristic characteristic : characteristics) {

                    String uuid = characteristic.getUuid().toString().toUpperCase();

                    if (characteristic.getUuid().toString().toUpperCase().equals(StringConstants.kSensorList)) {

                        Log.d("DEBUG", "characteristic: " + uuid);
                        Log.d("DEBUG", "characteristic uuid: " + characteristic.getUuid().toString().toUpperCase());
                        Log.d("DEBUG", "characteristic getWriteType: " + characteristic.getWriteType());

                        characteristicSensorList = characteristic;

                        break;
                    }
                }
                writeCharacteristicData();
            }
        }
    }

    private void writeCharacteristicData() {
        if (characteristicSensorList != null) {

            BitManager bitManager = BitManager.newObject();
            bitManager.objectByte = BitManager.createByteObjectFromSensors(selectedSensors, BitManager.BLUETOOTH_DISPLAY_MODE.BLUETOOTH_DISPLAY_MODE_GENERAL, getContext());

            byte[] data = BitManager.convertToData(bitManager.objectByte);

            String dataString = Arrays.toString(data);
            String hexByte = Utils.toHexString(data);

            Log.d("DEBUG", "hex dataString value: " + hexByte);
            Log.d("DEBUG", "dataString: " + dataString);

            mService.writeCharacteristicQueue(characteristicSensorList, data);

            Log.d("DEBUG", "Writing characteristic: " + characteristicSensorList.getUuid().toString().toUpperCase());
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
        //connectResult.setText("onCharacteristicChanged");
        readCharacteristic(characteristic);

    }

    @Override
    public void onCharacteristicSubscribed(BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean b) {
        //connectResult.setText("onCharacteristicSubscribed");
        if (b) {

            Log.d("DEBUG", "unsubscribed from characteristic!!");
        } else {

            Log.d("DEBUG", "subscribed to characteristic!!");
        }
    }

    @Override
    public void onCharacteristicWritten(BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        //connectResult.setText("onCharacteristicWritten");
    }

    @Override
    public void onFinishWriteAllCharacteristics() {
        //connectResult.setText("onFinishWriteAllCharacteristics");
    }

    @Override
    public void onStartWriteCharacteristic(BluetoothGattCharacteristic characteristic, int status) {
        //connectResult.setText("onStartWriteCharacteristic");
        if (status != BluetoothGatt.GATT_SUCCESS) {

            Log.d("DEBUG", "writing characteristic error: " + status + " - " + characteristic.getUuid().toString().toUpperCase());
        } else {

            String uuid = characteristic.getService().getUuid().toString().toUpperCase();

            if (uuid.equals(StringConstants.kServiceMainUUID)) {

                Log.d("DEBUG", "pasa aquiasdadds");

                for (BluetoothGattCharacteristic charac : notifyCharacteristics) {

                    mService.writeCharacteristicSubscription(charac, false);
                }

                notifyCharacteristics.clear();

                for (BluetoothGattCharacteristic charac : selectedService.getCharacteristics()) {

                    for (LBSensorObject sensor : selectedSensors) {

                        if (sensor.uuidString.equalsIgnoreCase(charac.getUuid().toString()) && sensor.tickStatus) {

                            notifyCharacteristics.add(charac);

                            mService.writeCharacteristicSubscription(charac, true);
                        }
                    }
                }
            }
        }
    }

    // BluetoothManagerQueueCallback
    @Override
    public void onStartWriteQueueDescriptor(BluetoothGattDescriptor bluetoothGattDescriptor) {
    }

    @Override
    public void onStartReadCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }


    @Override
    public void onFinishWriteAllDescriptors() {
        writeCharacteristicData();
    }

    @Override
    public void onFinishReadAllCharacteristics() {
    }

    @Override
    public void onCharacteristicChangedQueue(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
    }

    /**
     * Manages and parse values from notified characteristic.
     *
     * @param characteristic Notified characteristic
     */
    private void readCharacteristic(BluetoothGattCharacteristic characteristic) {

        try {

            String uuid = characteristic.getUuid().toString().toUpperCase();

            byte[] value = characteristic.getValue();

            if (value == null) {

                return;
            }

            for (LBSensorObject sensorObject : selectedSensors) {
                if (uuid.equalsIgnoreCase(sensorObject.uuidString)) {
                    if (uuid.equals(StringConstants.kUUIDBodyPositionSensor)) {

                        HashMap<String, String> dataDict = LBValueConverter.manageValuePosition(value);

                        Log.d("DEBUG", "kUUIDBodyPositionSensor dict: " + dataDict);
                        requireActivity().runOnUiThread(new MyRunnable(sensorObject, dataDict.get("0")));
                    }

                    if (uuid.equals(StringConstants.kUUIDTemperatureSensor)) {

                        HashMap<String, String> dataDict = LBValueConverter.manageValueTemperature(value);

                        Log.d("DEBUG", "kUUIDTemperatureSensor dict: " + dataDict);
                        requireActivity().runOnUiThread(new MyRunnable(sensorObject, dataDict.get("0")));
                    }

                    if (uuid.equals(StringConstants.kUUIDEMGSensor)) {

                        HashMap<String, String> dataDict = LBValueConverter.manageValueElectromyography(value);

                        Log.d("DEBUG", "kUUIDEMGSensor dict: " + dataDict);
                        requireActivity().runOnUiThread(new MyRunnable(sensorObject, dataDict.get("0")));
                    }

                    if (uuid.equals(StringConstants.kUUIDECGSensor)) {

                        HashMap<String, String> dataDict = LBValueConverter.manageValueElectrocardiography(value);

                        Log.d("DEBUG", "kUUIDECGSensor dict: " + dataDict);
                        requireActivity().runOnUiThread(new MyRunnable(sensorObject, dataDict.get("0")));
                    }

                    if (uuid.equals(StringConstants.kUUIDAirflowSensor)) {

                        HashMap<String, String> dataDict = LBValueConverter.manageValueAirflow(value);

                        Log.d("DEBUG", "kUUIDAirflowSensor dict: " + dataDict);
                        requireActivity().runOnUiThread(new MyRunnable(sensorObject, dataDict.get("0")));
                    }

                    if (uuid.equals(StringConstants.kUUIDGSRSensor)) {

                        HashMap<String, String> dataDict = LBValueConverter.manageValueGSR(value);

                        Log.d("DEBUG", "kUUIDGSRSensor dict: " + dataDict);
                        requireActivity().runOnUiThread(new MyRunnable(sensorObject, dataDict.get("0")));
                    }

                    if (uuid.equals(StringConstants.kUUIDBloodPressureSensor) || uuid.equals(StringConstants.kUUIDBloodPressureBLESensor)) {

                        HashMap<String, String> dataDict = LBValueConverter.manageValueBloodPressure(value);

                        if (uuid.equals(StringConstants.kUUIDBloodPressureSensor)) {

                            Log.d("DEBUG", "kUUIDBloodPressureSensor dict: " + dataDict);
                        }

                        if (uuid.equals(StringConstants.kUUIDBloodPressureBLESensor)) {

                            Log.d("DEBUG", "kUUIDBloodPressureBLESensor dict: " + dataDict);
                        }
                        requireActivity().runOnUiThread(new MyRunnable(sensorObject, dataDict.get("0")));
                    }

                    if (uuid.equals(StringConstants.kUUIDPulsiOximeterSensor) || uuid.equals(StringConstants.kUUIDPulsiOximeterBLESensor)) {

                        HashMap<String, String> dataDict = LBValueConverter.manageValuePulsiOximeter(value);

                        if (uuid.equals(StringConstants.kUUIDPulsiOximeterSensor)) {

                            Log.d("DEBUG", "kUUIDPulsiOximeterSensor dict: " + dataDict);
                        }

                        if (uuid.equals(StringConstants.kUUIDPulsiOximeterBLESensor)) {

                            Log.d("DEBUG", "kUUIDPulsiOximeterBLESensor dict: " + dataDict);
                        }
                        requireActivity().runOnUiThread(new MyRunnable(sensorObject, dataDict.get("0")));
                    }

                    if (uuid.equals(StringConstants.kUUIDGlucometerSensor) || uuid.equals(StringConstants.kUUIDGlucometerBLESensor)) {

                        HashMap<String, String> dataDict = LBValueConverter.manageValueGlucometer(value);

                        if (uuid.equals(StringConstants.kUUIDGlucometerSensor)) {

                            Log.d("DEBUG", "kUUIDGlucometerSensor dict: " + dataDict);
                        } else {

                            Log.d("DEBUG", "kUUIDGlucometerBLESensor dict: " + dataDict);
                        }
                        requireActivity().runOnUiThread(new MyRunnable(sensorObject, dataDict.get("0")));
                    }

                    if (uuid.equals(StringConstants.kUUIDSpirometerSensor)) {

                        HashMap<String, String> dataDict = LBValueConverter.manageValueSpirometer(value);

                        Log.d("DEBUG", "kUUIDSpirometerSensor dict: " + dataDict);
                        requireActivity().runOnUiThread(new MyRunnable(sensorObject, dataDict.get("0")));
                    }

                    if (uuid.equals(StringConstants.kUUIDSnoreSensor)) {

                        HashMap<String, String> dataDict = LBValueConverter.manageValueSnore(value);

                        Log.d("DEBUG", "kUUIDSnoreSensor dict: " + dataDict);
                        requireActivity().runOnUiThread(new MyRunnable(sensorObject, dataDict.get("0")));
                    }

                    if (uuid.equals(StringConstants.kUUIDScaleBLESensor)) {

                        HashMap<String, String> dataDict = LBValueConverter.manageValueScale(value);

                        Log.d("DEBUG", "kUUIDScaleBLESensor dict: " + dataDict);
                        requireActivity().runOnUiThread(new MyRunnable(sensorObject, dataDict.get("0")));
                    }

                    if (uuid.equals(StringConstants.kUUIDEEGSensor)) {

                        HashMap<String, String> dataDict = LBValueConverter.manageValueElectroencephalography(value);

                        Log.d("DEBUG", "kUUIDEEGSensor dict: " + dataDict);
                        requireActivity().runOnUiThread(new MyRunnable(sensorObject, dataDict.get("0")));
                    }
                }
            }

        } catch (Exception ignored) {

        }
    }

    @Override
    public void onDestroy() {

        if (timerRssi != null) {

            timerRssi.cancel();
            timerRssi.purge();
            timerRssi = null;
        }

        try {

            mService.unregisterBondNotification();
        } catch (Exception ignored) {

        }

        try {

            if (mService != null) {

                mService.disconnectDevice();
                mService.serviceDestroy();
                mService.close();
                mService = null;
            }
        } catch (Exception ignored) {

        }
        super.onDestroy();
    }


    private void getAllMembers() {
        ApiClient apiClient = new ApiClient();
        apiClient.getApiService(getContext()).getAllMembers().enqueue(new Callback<ArrayList<Member>>() {
            @Override
            public void onResponse(@NotNull Call<ArrayList<Member>> call, @NotNull Response<ArrayList<Member>> response) {
                if (response.isSuccessful()){
                    members = response.body();
                    memberSpinnerAdapter = new MemberSpinnerAdapter(getContext(), members);
                    spinner.setAdapter(memberSpinnerAdapter);
                    //memberSpinnerAdapter.setMembers(members);
                }
            }
            @Override
            public void onFailure(@NotNull Call<ArrayList<Member>> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyRunnable implements Runnable {
        private final LBSensorObject lbSensorObject;
        private final String value;

        public MyRunnable(LBSensorObject lbSensorObject, String value) {
            this.lbSensorObject = lbSensorObject;
            this.value = value;
        }

        @Override
        public void run() {
            Log.d("NEW VALUE", "Value: " + value);
            sensorListAdapter.updateValue(lbSensorObject, Float.parseFloat(value));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentDate = sdf.format(new Date());
            currentDate = currentDate.replace(" ", "T");
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            if (selectedMember !=null) {
                Sensor sensor = new Sensor(Util.getSensorDataByType(SensorDataType.TYPE, lbSensorObject), currentDate, Util.getSensorDataByType(SensorDataType.UNIT, lbSensorObject), value);
                ApiRequests.postSensor(sensor, selectedMember, getContext());
            }
        }
    }
}