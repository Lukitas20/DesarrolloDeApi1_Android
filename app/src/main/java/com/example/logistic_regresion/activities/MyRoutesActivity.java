package com.example.logistic_regresion.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logistic_regresion.R;
import com.example.logistic_regresion.adapters.RouteAdapter;
import com.example.logistic_regresion.clients.ApiClient;
import com.example.logistic_regresion.models.Route;
import com.example.logistic_regresion.repositories.TokenRepository;
import com.example.logistic_regresion.services.RouteService;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class MyRoutesActivity extends AppCompatActivity {

    private static final String TAG = "MyRoutesActivity";

    private RecyclerView recyclerView;
    private RouteService routeService;

    @Inject
    TokenRepository tokenRepository; // Inyección del repositorio de tokens

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_routes);

        recyclerView = findViewById(R.id.recyclerViewMyRoutes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        routeService = ApiClient.getClient(this).create(RouteService.class);

        fetchMyRoutes();
    }

    private void fetchMyRoutes() {
        String token = tokenRepository.getToken();

        // Verifica si el token es válido
        if (token == null || token.isEmpty()) {
            Log.e(TAG, "El token es nulo o está vacío");
            Toast.makeText(this, "Error: Sesión expirada. Por favor, inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        token = "Bearer " + token; // Agrega el prefijo "Bearer" al token

        routeService.getMyRoutes(token).enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(Call<List<Route>> call, Response<List<Route>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Route> routes = response.body();
                    recyclerView.setAdapter(new RouteAdapter(routes, new RouteAdapter.OnRouteActionListener() {
                        @Override
                        public void onAssign(Long routeId) {
                            // No se usa en "Mis Rutas"
                        }

                        @Override
                        public void onComplete(Long routeId) {
                            completeRoute(routeId);
                        }

                        @Override
                        public void onCancel(Long routeId) {
                            cancelRoute(routeId);
                        }
                    }));
                } else if (response.code() == 401) {
                    Log.e(TAG, "Error 401: Sesión expirada o token inválido");
                    Toast.makeText(MyRoutesActivity.this, "Sesión expirada. Por favor, inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
                    tokenRepository.clearToken();
                    finish(); // Cierra la actividad y regresa al inicio de sesión
                } else {
                    Log.e(TAG, "Error al cargar rutas: " + response.code());
                    Toast.makeText(MyRoutesActivity.this, "Error al cargar tus rutas: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Route>> call, Throwable t) {
                Log.e(TAG, "Error de conexión", t);
                Toast.makeText(MyRoutesActivity.this, "Error de conexión. Por favor, intenta nuevamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void completeRoute(Long routeId) {
        String token = tokenRepository.getToken();

        // Verifica si el token es válido
        if (token == null || token.isEmpty()) {
            Log.e(TAG, "El token es nulo o está vacío");
            Toast.makeText(this, "Error: Sesión expirada. Por favor, inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        token = "Bearer " + token;

        routeService.completeRoute(routeId, token).enqueue(new Callback<Route>() {
            @Override
            public void onResponse(Call<Route> call, Response<Route> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MyRoutesActivity.this, "Ruta completada exitosamente", Toast.LENGTH_SHORT).show();
                    fetchMyRoutes(); // Actualiza la lista de rutas
                } else {
                    Log.e(TAG, "Error al completar la ruta: " + response.code());
                    Toast.makeText(MyRoutesActivity.this, "Error al completar la ruta: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Route> call, Throwable t) {
                Log.e(TAG, "Error de conexión al completar la ruta", t);
                Toast.makeText(MyRoutesActivity.this, "Error de conexión. Por favor, intenta nuevamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelRoute(Long routeId) {
        String token = tokenRepository.getToken();

        // Verifica si el token es válido
        if (token == null || token.isEmpty()) {
            Log.e(TAG, "El token es nulo o está vacío");
            Toast.makeText(this, "Error: Sesión expirada. Por favor, inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        token = "Bearer " + token;

        routeService.cancelRoute(routeId, token).enqueue(new Callback<Route>() {
            @Override
            public void onResponse(Call<Route> call, Response<Route> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MyRoutesActivity.this, "Ruta cancelada exitosamente", Toast.LENGTH_SHORT).show();
                    fetchMyRoutes(); // Actualiza la lista de rutas
                } else {
                    Log.e(TAG, "Error al cancelar la ruta: " + response.code());
                    Toast.makeText(MyRoutesActivity.this, "Error al cancelar la ruta: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Route> call, Throwable t) {
                Log.e(TAG, "Error de conexión al cancelar la ruta", t);
                Toast.makeText(MyRoutesActivity.this, "Error de conexión. Por favor, intenta nuevamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}