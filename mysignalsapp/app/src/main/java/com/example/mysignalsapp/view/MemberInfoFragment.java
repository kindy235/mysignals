package com.example.mysignalsapp.view;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.adapter.SensorTypeSpinnerAdapter;
import com.example.mysignalsapp.databinding.FragmentMemberInfoBinding;
import com.example.mysignalsapp.entity.Member;
import com.example.mysignalsapp.entity.Sensor;
import com.example.mysignalsapp.service.ApiClient;
import com.example.mysignalsapp.utils.Util;
import com.example.mysignalsapp.viewmodel.UserDataViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MemberInfoFragment extends Fragment {

    private Member member;
    private LineChart lineChart;
    private ArrayList<Sensor> sensors;

    private List<Entry> entries;
    private LineDataSet lineDataSet;
    private LineData lineData;

    private ChartUpdateRunnable chartUpdateRunnable;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentMemberInfoBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_member_info, container, false);


        UserDataViewModel userDataViewModel = new UserDataViewModel();
        userDataViewModel.setMember(member);
        binding.setUserDataViewModel(userDataViewModel);

        Spinner spinner = binding.getRoot().findViewById(R.id.filter_spinner);
        lineChart = binding.getRoot().findViewById(R.id.lineChart);
        List<String> sensorTypes = Util.getSensorTypeList();
        SensorTypeSpinnerAdapter adapter = new SensorTypeSpinnerAdapter(getContext(), sensorTypes);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Select sensor type");
        spinner.setAdapter(adapter);

        chartUpdateRunnable = new ChartUpdateRunnable();

        sensors = new ArrayList<>();
        entries = new ArrayList<>();
        setupChart();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type= (String) parent.getSelectedItem();
                if (type != null && member != null){
                    //Update chart value
                    chartUpdateRunnable.setMemberId(member.getId());
                    chartUpdateRunnable.setType(type);
                    //Restart chart updating with new type
                    chartUpdateRunnable.startUpdating();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return binding.getRoot();
    }

    private void setupChart() {
        // Create an empty LineDataSet and LineData
        lineDataSet = new LineDataSet(entries, "Sensor Data");
        lineDataSet.setColor(Color.MAGENTA);
        //lineDataSet.setCircleColor(Color.RED);
        lineData = new LineData(lineDataSet);

        // Customize the appearance of the LineChart
        lineChart.setData(lineData);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDrawGridBackground(false);

        // Customize the appearance of the X-axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Customize the appearance of the Y-axis
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        // Customize the appearance of the chart description
        Description description = new Description();
        description.setText("Sensor Data Chart");
        lineChart.setDescription(description);

        // Customize the appearance of the chart legend
        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);
        legend.setTextSize(12f);

        // Invalidate the chart to refresh the view
        lineChart.invalidate();
    }

    private void drawChart() {
        entries.clear();
        int startIndex = Math.max(sensors.size() - 21, 0);

        // Populate the entries list with data from the last ten sensors
        for (int i = startIndex; i < sensors.size(); i++) {
            Sensor sensor = sensors.get(i);
            try {
                float value = Float.parseFloat(sensor.getValue());
                entries.add(new Entry(sensors.indexOf(sensor) - startIndex, value));
            }catch (Exception ignored){}
        }

        // Notify the LineData that the data has changed and update the chart
        lineDataSet.notifyDataSetChanged();
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void getMemberSensors(Long memberId, String type) {
        ApiClient apiClient = new ApiClient();
        apiClient.getApiService(getContext()).getMemberSensors(memberId, type).enqueue(new Callback<ArrayList<Sensor>>() {
            @Override
            public void onResponse(@NotNull Call<ArrayList<Sensor>> call, @NotNull Response<ArrayList<Sensor>> response) {
                if (response.isSuccessful()){
                    sensors = response.body();
                    drawChart();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ArrayList<Sensor>> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chartUpdateRunnable != null){
            chartUpdateRunnable.stopUpdating();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (chartUpdateRunnable != null){
            chartUpdateRunnable.stopUpdating();
        }
    }

    public class ChartUpdateRunnable implements Runnable {
        private Long memberId;
        private String type;
        private final ScheduledExecutorService executorService;

        public ChartUpdateRunnable() {
            this.executorService = Executors.newSingleThreadScheduledExecutor();
        }

        public Long getMemberId() {
            return memberId;
        }

        public void setMemberId(Long memberId) {
            this.memberId = memberId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void startUpdating() {
            executorService.scheduleWithFixedDelay(this, 0, 2, TimeUnit.SECONDS);
        }

        public void stopUpdating() {
            executorService.shutdown();
        }

        @Override
        public void run() {
            if (memberId != null && type != null) {
                // Call your function to update the chart here
                getMemberSensors(memberId, type);
            }
        }
    }
}