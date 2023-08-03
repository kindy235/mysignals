package com.example.mysignalsapp.service;


import android.content.Context;
import java.io.IOException;
import com.example.mysignalsapp.utils.SessionManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

public class AuthInterceptor implements Interceptor {
    private final SessionManager sessionManager;

    public AuthInterceptor(Context context) {
        sessionManager = new SessionManager(context);
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();

        // If token has been saved, add it to the request
        String authToken = sessionManager.fetchAuthToken();
        if (authToken != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + authToken);
        }

        return chain.proceed(requestBuilder.build());
    }
}
