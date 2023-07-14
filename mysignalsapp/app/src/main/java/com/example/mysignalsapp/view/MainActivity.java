package com.example.mysignalsapp.view;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.mysignalsapp.R;
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

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private  FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private UserDataFragment userDataFragment;
    private AccountFragment accountFragment;
    private BottomNavigationView bottomNavigationView;
    private Window window;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        homeFragment = new HomeFragment();
        replaceFragment(homeFragment);
        BluetoothManager manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bottomNavigationView = findViewById(R.id.btn_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.action_home:
                    if (homeFragment == null){
                        homeFragment = new HomeFragment();
                    }
                    replaceFragment(homeFragment);
                    break;
                case R.id.action_sensors:
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