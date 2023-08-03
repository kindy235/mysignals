package com.example.mysignalsapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.entity.Member;
import com.example.mysignalsapp.utils.Util;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SensorTypeSpinnerAdapter extends ArrayAdapter<String> {

    private final List<String> sensorTypes;
    private final Context context;

    public SensorTypeSpinnerAdapter(Context context, List<String> sensorTypes) {
        super(context, 0, sensorTypes);
        this.sensorTypes = sensorTypes;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_type_item, parent, false);
        }

        // Set the member's name as the text for the dropdown item
        String type  = sensorTypes.get(position);
        if (type != null) {
            ImageView imageView = convertView.findViewById(R.id.image);
            TextView textView = convertView.findViewById(R.id.name);
            imageView.setImageResource(Util.getSensorTypeResourceId(type)); // Adjust to your Member model getter
            textView.setText(type); // Adjust to your Member model getter
        }
        return convertView;
    }
}
