package com.example.mysignalsapp.service;

import com.example.mysignalsapp.utils.Constants;
import android.content.Context;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit instance class
 */
public class ApiClient {
    private ApiService apiService;

    public ApiService getApiService(Context context) {

        // Initialize ApiService if not initialized yet
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpClient(context))
                    .build();

            apiService = retrofit.create(ApiService.class);
        }

        return apiService;
    }

    /**
     * Initialize OkhttpClient with our interceptor
     */
    private OkHttpClient okhttpClient(Context context) {
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(context))
                .build();
    }
}

