package com.example.logistic_regresion.services;

import com.example.logistic_regresion.responses.UserProfileResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    @GET("users/me")
    Call<UserProfileResponse> getUserProfile();
}