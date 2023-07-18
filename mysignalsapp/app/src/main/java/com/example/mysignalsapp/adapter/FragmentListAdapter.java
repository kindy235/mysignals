package com.example.mysignalsapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FragmentListAdapter extends FragmentStateAdapter {

    ArrayList<Fragment> fragmentArrayList;

    public FragmentListAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> fragmentArrayList) {
        super(fragmentActivity);
        this.fragmentArrayList = fragmentArrayList;
    }


    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }
}
