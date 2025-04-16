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
import com.example.logistic_regresion.services.RouteService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RouteService routeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        recyclerView = findViewById(R.id.recyclerViewRoutes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        routeService = ApiClient.getClient(this).create(RouteService.class);

        fetchRoutes();
    }

    private void fetchRoutes() {
        routeService.getAvailableRoutes().enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(Call<List<Route>> call, Response<List<Route>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Route> routes = response.body();
                    recyclerView.setAdapter(new RouteAdapter(routes));
                } else {
                    Toast.makeText(RoutesActivity.this, "Error al cargar rutas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Route>> call, Throwable t) {
                Toast.makeText(RoutesActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}