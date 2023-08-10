package com.example.mysignalsapp.view;

import android.content.Intent;
import android.os.Bundle;

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

        apiClient = new ApiClient();
        sessionManager = new SessionManager(this);

        findViewById(R.id.login_btn).setOnClickListener(v -> {

            TextView usernameTextView = findViewById(R.id.username);
            TextView passwordTextView = findViewById(R.id.password);
            String username = usernameTextView.getText().toString();
            String password = passwordTextView.getText().toString();

            apiClient.getApiService(getApplicationContext()).login(new LoginRequest(username, password))
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {

                            if (response.isSuccessful()) {
                                LoginResponse loginResponse = response.body();
                                assert loginResponse != null;
                                sessionManager.saveAuthToken(loginResponse.getAccessToken());
                                Util.showToast(SignInActivity.this, "Login success");
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                intent.putExtra("login", loginResponse);
                                startActivity(intent);
                                //finish();
                            } else {
                                Util.showToast(SignInActivity.this, "Login error");
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                            Util.showToast(SignInActivity.this, t.getMessage());
                        }
                    });

            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            //finish();
        });

        findViewById(R.id.register_btn).setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
            //finish();
        });

    }
}
