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
import com.example.mysignalsapp.databinding.FragmentAccountBinding;
import com.example.mysignalsapp.viewmodel.AccountViewModel;


public class AccountFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        FragmentAccountBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_account, container, false);

        AccountViewModel accountViewModel = new AccountViewModel();

        binding.setAccountViewModel(accountViewModel);

        return binding.getRoot();
    }
}