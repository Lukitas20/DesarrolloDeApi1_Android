package com.example.logistic_regresion.clients;

import android.content.Context;

import com.example.logistic_regresion.repositories.TokenRepository;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "http://10.0.2.2:8080/";

    public static Retrofit getClient(Context context, TokenRepository tokenRepository) {
        if (retrofit == null) {
            // Crear caché de 5MB
            File cacheDir = new File(context.getCacheDir(), "http-cache");
            Cache cache = new Cache(cacheDir, 5 * 1024 * 1024); // 5MB

            // Interceptor para agregar el token dinámico
            Interceptor authInterceptor = chain -> {
                String token = tokenRepository.getToken();

                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();

                if (token != null) {
                    requestBuilder.addHeader("Authorization", "Bearer " + token);
                }

                return chain.proceed(requestBuilder.build());
            };

            // Interceptor de caché
            Interceptor cacheInterceptor = chain -> {
                Request request = chain.request();
                Response response = chain.proceed(request);

                int maxAge = 60 * 60 * 24; // 1 día
                return response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            };

            // Interceptor de logging
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(authInterceptor)
                    .addInterceptor(cacheInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}