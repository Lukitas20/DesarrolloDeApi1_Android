package com.example.logistic_regresion.services;

import com.example.logistic_regresion.models.Route;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RouteService {
    @GET("routes/available")
    Call<List<Route>> getAvailableRoutes();

    @POST("routes/{routeId}/assign")
    Call<Route> assignRoute(@Path("routeId") Long routeId);
}