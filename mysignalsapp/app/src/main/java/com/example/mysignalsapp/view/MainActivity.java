package com.example.mysignalsapp.view;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.entity.Sensor;
import com.example.mysignalsapp.databinding.ActivityMainBinding;
import com.example.mysignalsapp.service.ApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

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
        //fetchSensors();
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

    private void fetchSensors() {
        ApiClient apiClient = new ApiClient();
        apiClient.getApiService(this).getAllSensors()
                .enqueue(new Callback<List<Sensor>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Sensor>> call, @NotNull Response<List<Sensor>> response) {
                        if (response.isSuccessful()){
                            List<Sensor> sensors = response.body();
                            Toast.makeText(MainActivity.this, "Fetch sensors data success", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Sensor>> call, @NotNull Throwable t) {
                        Log.d("FETCH", t.getMessage());
                    }
                });
    }

}