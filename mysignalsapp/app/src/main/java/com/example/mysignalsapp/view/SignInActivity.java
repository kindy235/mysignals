package com.example.mysignalsapp.view;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mysignalsapp.R;
import com.example.mysignalsapp.authentication.requests.LoginRequest;
import com.example.mysignalsapp.authentication.responses.LoginResponse;
import com.example.mysignalsapp.utils.SessionManager;
import com.example.mysignalsapp.service.ApiClient;
import com.example.mysignalsapp.utils.Util;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private ApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Util.requestPermissions(this, this);
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        Intent signUpActivityintent = new Intent(this, SignUpActivity.class);
        apiClient = new ApiClient();
        sessionManager = new SessionManager(this);
        TextView usernameTextView = findViewById(R.id.username);
        TextView passwordTextView = findViewById(R.id.password);
        ProgressBar progressBar = findViewById(R.id.loading);

        findViewById(R.id.login_btn).setOnClickListener(v -> {

            String username = usernameTextView.getText().toString();
            String password = passwordTextView.getText().toString();

            if (isLoginParamsValid(username, password)) {
                progressBar.getProgress();
                progressBar.setVisibility(View.VISIBLE);

                apiClient.getApiService(getApplicationContext()).login(new LoginRequest(username, password))
                        .enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {

                                if (response.isSuccessful()) {
                                    LoginResponse loginResponse = response.body();
                                    assert loginResponse != null;
                                    sessionManager.saveAuthToken(loginResponse.getAccessToken());
                                    Util.showToast(SignInActivity.this, "Login success");
                                    mainActivityIntent.putExtra("login", loginResponse);
                                    startActivity(mainActivityIntent);
                                    finish();
                                } else {
                                    Util.showToast(SignInActivity.this, "Login error");
                                }
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                                Util.showToast(SignInActivity.this, t.getMessage());
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            } else {
                Util.showToast(this, "Invalid fields");
            }
            startActivity(mainActivityIntent);
        });

        findViewById(R.id.register_btn).setOnClickListener(v -> {
            startActivity(signUpActivityintent);
            finish();
        });

    }

    private boolean isLoginParamsValid(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }
}
