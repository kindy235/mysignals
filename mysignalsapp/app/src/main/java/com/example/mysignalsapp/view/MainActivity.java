package com.example.mysignalsapp.view;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.authentication.responses.LoginResponse;
import com.example.mysignalsapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static android.content.Intent.getIntent;

public class MainActivity extends AppCompatActivity {

    private  FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private UserDataFragment userDataFragment;
    private AccountFragment accountFragment;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // Retrieve the User object from the Intent
        Intent intent = getIntent();
        if (intent != null) {
            LoginResponse user = (LoginResponse) intent.getSerializableExtra("login");
            if (user != null) {
                // Use the user object as needed
                // ...
            }
        }
        homeFragment = new HomeFragment();
        replaceFragment(homeFragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.btn_navigation);

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
}