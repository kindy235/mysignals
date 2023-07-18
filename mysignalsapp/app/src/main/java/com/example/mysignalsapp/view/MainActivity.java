package com.example.mysignalsapp.view;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.adapter.FragmentListAdapter;
import com.example.mysignalsapp.authentication.responses.PostsResponse;
import com.example.mysignalsapp.utils.SessionManager;
import com.example.mysignalsapp.databinding.ActivityMainBinding;
import com.example.mysignalsapp.service.ApiClient;
import com.example.mysignalsapp.view.AccountFragment;
import com.example.mysignalsapp.view.HomeFragment;
import com.example.mysignalsapp.view.UserDataFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private  FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private UserDataFragment userDataFragment;
    private AccountFragment accountFragment;
    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager2;
    ArrayList<Fragment> fragmentArrayList;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        /*
        viewPager2 = findViewById(R.id.fragment_list);

        bottomNavigationView = findViewById(R.id.btn_navigation);

        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new HomeFragment());
        fragmentArrayList.add(new UserDataFragment());
        fragmentArrayList.add(new AccountFragment());
        fragmentArrayList.add(new Fragment());

        FragmentListAdapter fragmentListAdapter = new FragmentListAdapter(this, fragmentArrayList);
        viewPager2.setAdapter(fragmentListAdapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.action_home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.action_data);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.action_account);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.action_settings);
                        break;
                    default:
                        break;
                }

            }
        });


        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.action_home:
                    viewPager2.setCurrentItem(0);
                    break;
                case R.id.action_data:
                    viewPager2.setCurrentItem(1);
                    break;
                case R.id.action_account:
                    viewPager2.setCurrentItem(2);
                    break;
                case R.id.action_settings:
                    viewPager2.setCurrentItem(3);
                    break;
                default:
                    break;
            }
            return true;
        });

         */
        homeFragment = new HomeFragment();
        replaceFragment(homeFragment);
        bottomNavigationView = findViewById(R.id.btn_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.action_home:
                    if (homeFragment == null){
                        homeFragment = new HomeFragment();
                    }
                    replaceFragment(homeFragment);
                    break;
                case R.id.action_data:
                    if (userDataFragment == null){
                        userDataFragment = new UserDataFragment();
                    }
                    replaceFragment(userDataFragment);
                    break;
                case R.id.action_account:
                    if (accountFragment == null){
                        accountFragment = new AccountFragment();
                    }
                    replaceFragment(accountFragment);
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){

        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        // Sert à remplacer un fragement dans fragment_container de l'activity principale
        if (fragment != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    //.addToBackStack(null)
                    .commit();
        }
    }

    private void fetchPosts() {
        ApiClient apiClient = new ApiClient();
        apiClient.getApiService(this).fetchPosts()
                .enqueue(new Callback<PostsResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<PostsResponse> call, @NotNull Response<PostsResponse> response) {
                        // Handle function to display posts
                    }

                    @Override
                    public void onFailure(@NotNull Call<PostsResponse> call, @NotNull Throwable t) {
                        // Error fetching posts
                    }
                });
    }

}