package com.example.logistic_regresion.services;

import com.example.logistic_regresion.requests.LoginRequest;
import com.example.logistic_regresion.requests.ResendVerificationRequest;
import com.example.logistic_regresion.requests.ResetPasswordRequest;
import com.example.logistic_regresion.requests.SignupRequest;
import com.example.logistic_regresion.requests.VerifyRequest;
import com.example.logistic_regresion.responses.LoginResponse;
import com.example.logistic_regresion.responses.SignupResponse;
import com.example.logistic_regresion.requests.ForgotPasswordRequest;
import com.example.logistic_regresion.requests.VerifyResetCodeRequest;

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
    Call<Map<String, String>> resendVerificationCode(@Body ResendVerificationRequest request);
    @POST("auth/forgot-password")
    Call<Map<String, String>> requestPasswordReset(@Body ForgotPasswordRequest request);
    @POST("auth/verify-reset-code")
    Call<Map<String, Boolean>> verifyResetCode(@Body VerifyResetCodeRequest request);
    @POST("auth/reset-password")
    Call<Map<String, String>> resetPassword(@Body ResetPasswordRequest request);
}
