package com.example.mysignalsapp.service;


import com.example.mysignalsapp.authentication.requests.LoginRequest;
import com.example.mysignalsapp.authentication.requests.RegistrationRequest;
import com.example.mysignalsapp.authentication.responses.LoginResponse;
import com.example.mysignalsapp.authentication.responses.RegistrationResponse;
import com.example.mysignalsapp.entity.Member;
import com.example.mysignalsapp.entity.Sensor;
import com.example.mysignalsapp.utils.Constants;
import com.example.mysignalsapp.utils.SensorType;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.ArrayList;
import java.util.List;

public interface ApiService {

    @POST(Constants.REGISTRATION_URL)
    Call<RegistrationResponse> register(@Body RegistrationRequest request);

    @POST(Constants.LOGIN_URL)
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET(Constants.SENSORS_URL)
    Call<ArrayList<Sensor>> getAllSensors();

    @GET(Constants.SENSORS_URL)
    Call<Sensor> getSensorsBy(@Query("type") SensorType type);

    @GET(Constants.MEMBERS_URL)
    Call<ArrayList<Member>> getAllMembers();

    @GET(Constants.MEMBERS_URL+"/{memberId}/"+Constants.SENSORS_URL)
    Call<ArrayList<Sensor>> getMemberSensors(@Path("memberId") long memberId, @Query("type") String type);

    @POST(Constants.MEMBERS_URL+"/{memberId}/"+Constants.SENSORS_URL)
    Call<Sensor> postSensor(@Path("memberId") long memberId, @Body Sensor sensor);

    @POST(Constants.MEMBERS_URL)
    Call<Member> postMember(@Body Member member);

    @DELETE(Constants.MEMBERS_URL+"/{memberId}")
    Call<Void> deleteMember(@Path("memberId") long memberId);
}
