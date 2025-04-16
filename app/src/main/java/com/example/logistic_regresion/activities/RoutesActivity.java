package com.example.logistic_regresion.activities;

import android.os.Bundle;
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
public class RoutesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RouteService routeService;

    @Inject
    TokenRepository tokenRepository; // Inyección del repositorio de tokens

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        // Habilitar el botón de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerViewRoutes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        routeService = ApiClient.getClient(this).create(RouteService.class);

        fetchRoutes();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Finaliza la actividad y regresa al menú principal
        return true;
    }

    private void fetchRoutes() {
        String token = "Bearer " + tokenRepository.getToken(); // Obtén el token del repositorio

        routeService.getAvailableRoutes(token).enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(Call<List<Route>> call, Response<List<Route>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Route> routes = response.body();
                    recyclerView.setAdapter(new RouteAdapter(routes, new RouteAdapter.OnRouteActionListener() {
                        @Override
                        public void onAssign(Long routeId) {
                            assignRoute(routeId);
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
                } else {
                    Toast.makeText(RoutesActivity.this, "Error al cargar rutas: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Route>> call, Throwable t) {
                Toast.makeText(RoutesActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void assignRoute(Long routeId) {
        String token = "Bearer " + tokenRepository.getToken();

        routeService.assignRoute(routeId, token).enqueue(new Callback<Route>() {
            @Override
            public void onResponse(Call<Route> call, Response<Route> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RoutesActivity.this, "Ruta asignada exitosamente", Toast.LENGTH_SHORT).show();
                    fetchRoutes(); // Actualiza la lista de rutas
                } else {
                    Toast.makeText(RoutesActivity.this, "Error al asignar la ruta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Route> call, Throwable t) {
                Toast.makeText(RoutesActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void completeRoute(Long routeId) {
        String token = "Bearer " + tokenRepository.getToken();

        routeService.completeRoute(routeId, token).enqueue(new Callback<Route>() {
            @Override
            public void onResponse(Call<Route> call, Response<Route> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RoutesActivity.this, "Ruta completada exitosamente", Toast.LENGTH_SHORT).show();
                    fetchRoutes(); // Actualiza la lista de rutas
                } else {
                    Toast.makeText(RoutesActivity.this, "Error al completar la ruta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Route> call, Throwable t) {
                Toast.makeText(RoutesActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelRoute(Long routeId) {
        String token = "Bearer " + tokenRepository.getToken();

        routeService.cancelRoute(routeId, token).enqueue(new Callback<Route>() {
            @Override
            public void onResponse(Call<Route> call, Response<Route> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RoutesActivity.this, "Ruta cancelada exitosamente", Toast.LENGTH_SHORT).show();
                    fetchRoutes(); // Actualiza la lista de rutas
                } else {
                    Toast.makeText(RoutesActivity.this, "Error al cancelar la ruta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Route> call, Throwable t) {
                Toast.makeText(RoutesActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}