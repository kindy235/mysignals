package com.example.mysignalsapp.utils;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.example.mysignalsapp.entity.Member;
import com.example.mysignalsapp.entity.Sensor;
import com.example.mysignalsapp.service.ApiClient;
import com.libelium.mysignalsconnectkit.pojo.LBSensorObject;
import com.libelium.mysignalsconnectkit.utils.StringConstants;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class ApiRequests {

    public static void postSensor(Sensor sensor, Member member, Context context) {
        ApiClient apiClient = new ApiClient();
        apiClient.getApiService(context).postSensor(member.getId(), sensor)
                .enqueue(new Callback<Sensor>() {
                    @Override
                    public void onResponse(@NotNull Call<Sensor> call, @NotNull Response<Sensor> response) {
                        if (response.isSuccessful()) {
                            //Sensor sensors = response.body();
                            //Toast.makeText(context, "Data Upload success", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Sensor> call, @NotNull Throwable t) {
                        Log.d("FETCH", t.getMessage());
                    }
                });
    }

    public static void postMember(Member member, Context context) {
        ApiClient apiClient = new ApiClient();
        apiClient.getApiService(context).postMember(member).enqueue(new Callback<Member>() {
            @Override
            public void onResponse(@NotNull Call<Member> call, @NotNull Response<Member> response) {
                if (response.isSuccessful()){
                    Member member = response.body();
                    Toast.makeText(context, "Member added to the cloud API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Member> call, @NotNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void deleteMember(Long memberId, Context context) {
        ApiClient apiClient = new ApiClient();
        apiClient.getApiService(context).deleteMember(memberId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "Member deleted from the cloud API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
