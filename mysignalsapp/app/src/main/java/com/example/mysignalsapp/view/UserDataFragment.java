package com.example.mysignalsapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.databinding.FragmentUserdataBinding;

public class UserDataFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        FragmentUserdataBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_userdata, container, false);

        return binding.getRoot();
    }
}