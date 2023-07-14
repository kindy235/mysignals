package com.example.mysignalsapp.service;


import com.example.mysignalsapp.authentication.requests.LoginRequest;
import com.example.mysignalsapp.authentication.responses.LoginResponse;
import com.example.mysignalsapp.authentication.responses.PostsResponse;
import com.example.mysignalsapp.utils.Constants;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @POST(Constants.LOGIN_URL)
    @FormUrlEncoded
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET(Constants.POSTS_URL)
    Call<PostsResponse> fetchPosts();
}
