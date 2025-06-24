package com.example.logistic_regresion.services;

import com.example.logistic_regresion.models.Incident;
import com.example.logistic_regresion.models.Route;
import com.example.logistic_regresion.responses.RouteHistoryResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Body;

public interface RouteService {
    @GET("routes/available")
    Call<List<Route>> getAvailableRoutes(@Header("Authorization") String token);

    @GET("routes/my-routes")
    Call<List<Route>> getMyRoutes(@Header("Authorization") String token);

    @POST("routes/{routeId}/assign")
    Call<Route> assignRoute(@Path("routeId") Long routeId, @Header("Authorization") String token);

    @POST("routes/{routeId}/complete")
    Call<Route> completeRoute(@Path("routeId") Long routeId, @Header("Authorization") String token);

    @POST("routes/{routeId}/cancel")
    Call<Route> cancelRoute(@Path("routeId") Long routeId, @Header("Authorization") String token);

    @GET("routes/history")
    Call<List<RouteHistoryResponse>> getRouteHistory();

    @POST("routes/report-incident")
    Call<Incident> reportIncident(@Body Incident incident, @Header("Authorization") String token);

    @Multipart
    @POST("routes/report-incident")
    Call<Incident> reportIncidentMultipart(
            @Part("type") RequestBody type,
            @Part("description") RequestBody description,
            @Part("routeId") RequestBody routeId,
            @Part MultipartBody.Part photo,
            @Header("Authorization") String token);
}
