package com.example.mysignalsapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mysignalsapp.R;
import com.example.mysignalsapp.authentication.requests.RegistrationRequest;
import com.example.mysignalsapp.authentication.responses.RegistrationResponse;
import com.example.mysignalsapp.service.ApiClient;
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
        ApiClient apiClient = new ApiClient();

        findViewById(R.id.register_btn).setOnClickListener(v -> {

            TextView usernameTextView = findViewById(R.id.username);
            TextView emailTextView = findViewById(R.id.email);
            TextView passwordTextView = findViewById(R.id.password);


            String username = usernameTextView.getText().toString();
            String email = emailTextView.getText().toString();
            String password = passwordTextView.getText().toString();
            Set<String> role = new HashSet<>();
            role.add("mod");

            apiClient.getApiService(getApplicationContext()).register(new RegistrationRequest(username, email,password, role))
                    .enqueue(new Callback<RegistrationResponse>() {

                        @Override
                        public void onResponse(@NotNull Call<RegistrationResponse> call, @NotNull Response<RegistrationResponse> response) {

                            if (response.isSuccessful()) {
                                RegistrationResponse registrationResponse = response.body();
                                assert registrationResponse != null;
                                Toast.makeText(SignUpActivity.this, registrationResponse.getMessage() , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignUpActivity.this, "Registration error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<RegistrationResponse> call, @NotNull Throwable t) {
                            Toast.makeText(SignUpActivity.this, "Registration error", Toast.LENGTH_SHORT).show();
                            Log.w("MyTag", "requestFailed", t);
                        }
                    });
        });

        findViewById(R.id.login_btn).setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }
}