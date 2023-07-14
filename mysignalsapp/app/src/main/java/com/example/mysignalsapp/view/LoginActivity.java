package com.example.mysignalsapp.view;

import android.os.Bundle;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mysignalsapp.R;
import com.example.mysignalsapp.authentication.requests.LoginRequest;
import com.example.mysignalsapp.authentication.responses.LoginResponse;
import com.example.mysignalsapp.utils.SessionManager;
import com.example.mysignalsapp.service.ApiClient;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private ApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiClient = new ApiClient();
        sessionManager = new SessionManager(this);

        apiClient.getApiService(getApplicationContext()).login(new LoginRequest("s@sample.com", "mypassword"))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {
                        LoginResponse loginResponse = response.body();
                        if (loginResponse != null && loginResponse.getStatusCode() == 200 && loginResponse.getUser() != null) {
                            sessionManager.saveAuthToken(loginResponse.getAuthToken());
                        } else {
                            Toast.makeText(LoginActivity.this, "Login error", Toast.LENGTH_SHORT).show();
                            // Error logging in
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                        // Error logging in
                    }
                });
    }
}
