package com.example.logistic_regresion.services;

import com.example.logistic_regresion.requests.LoginRequest;
import com.example.logistic_regresion.requests.ResetPasswordRequest;
import com.example.logistic_regresion.requests.SignupRequest;
import com.example.logistic_regresion.requests.VerifyRequest;
import com.example.logistic_regresion.responses.LoginResponse;
import com.example.logistic_regresion.responses.SignupResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {
    @POST("auth/signup")
    Call<SignupResponse> registerUser(@Body SignupRequest request);

    @POST("auth/verify")
    Call<Void> verifyCode(@Body VerifyRequest request);

    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);
    @POST("auth/resend")
    Call<Void> resendCode(@Query("email") String email);

    @POST("auth/forgot-password")
    Call<Void> requestPasswordReset(@Body Map<String, String> emailMap);

    @POST("/auth/reset-password")
    Call<Void> resetPassword(@Body ResetPasswordRequest request);
}
