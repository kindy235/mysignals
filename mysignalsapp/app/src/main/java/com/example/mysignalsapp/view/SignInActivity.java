package com.example.mysignalsapp.view;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

public class SignInActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private ApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                                Toast.makeText(SignInActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                //finish();
                            } else {
                                Toast.makeText(SignInActivity.this, "Login error", Toast.LENGTH_SHORT).show();
                                // Error logging in
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                            Toast.makeText(SignInActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
