package com.example.mysignalsapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.authentication.requests.RegistrationRequest;
import com.example.mysignalsapp.authentication.responses.RegistrationResponse;
import com.example.mysignalsapp.service.ApiClient;
import com.example.mysignalsapp.utils.Util;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashSet;
import java.util.Set;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Intent intent = new Intent(this, SignInActivity.class);
        ApiClient apiClient = new ApiClient();

        TextView usernameTextView = findViewById(R.id.username);
        TextView emailTextView = findViewById(R.id.email);
        TextView passwordTextView = findViewById(R.id.password);
        TextView confirmPasswordTextView = findViewById(R.id.re_password);
        ProgressBar progressBar = findViewById(R.id.loading);

        findViewById(R.id.register_btn).setOnClickListener(v -> {

            String username = usernameTextView.getText().toString();
            String email = emailTextView.getText().toString();
            String password = passwordTextView.getText().toString();
            String confirmPassword = confirmPasswordTextView.getText().toString();
            Set<String> role = new HashSet<>();
            role.add("mod");

            if (isRegistrationParamsValid(username, email, password, confirmPassword)) {
                progressBar.setVisibility(View.VISIBLE);
                apiClient.getApiService(getApplicationContext()).register(new RegistrationRequest(username, email, password, role))
                        .enqueue(new Callback<RegistrationResponse>() {

                            @Override
                            public void onResponse(@NotNull Call<RegistrationResponse> call, @NotNull Response<RegistrationResponse> response) {

                                if (response.isSuccessful()) {
                                    RegistrationResponse registrationResponse = response.body();
                                    assert registrationResponse != null;
                                    Util.showToast(SignUpActivity.this, registrationResponse.getMessage());
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Util.showToast(SignUpActivity.this, "Registration error");
                                }
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(@NotNull Call<RegistrationResponse> call, @NotNull Throwable t) {
                                Util.showToast(SignUpActivity.this, t.getMessage());
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }else {
                Util.showToast(this, "invalid fields");
            }
        });

        findViewById(R.id.login_btn).setOnClickListener(v -> {
            startActivity(intent);
            //finish();
        });
    }

    private boolean isRegistrationParamsValid(String username, String email, String password, String confirmPassword) {
        return !username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && password.equals(confirmPassword);
    }
}