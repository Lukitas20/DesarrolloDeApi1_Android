package com.example.logistic_regresion.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.logistic_regresion.R;
import com.example.logistic_regresion.adapters.RouteHistoryAdapter;
import com.example.logistic_regresion.clients.ApiClient;
import com.example.logistic_regresion.repositories.TokenRepository;
import com.example.logistic_regresion.responses.RouteHistoryResponse;
import com.example.logistic_regresion.responses.UserProfileResponse;
import com.example.logistic_regresion.services.RouteService;
import com.example.logistic_regresion.services.UserService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ProfileActivity extends AppCompatActivity {
    private RecyclerView historyRecyclerView;
    private RouteService routeService;
    private UserService userService; // Servicio para obtener el perfil del usuario
    private TextView userNameText, userEmailText; // TextViews para nombre y correo

    @Inject
    TokenRepository tokenRepository; // Inyección de TokenRepository

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inicializar TextViews
        userNameText = findViewById(R.id.userNameText);
        userEmailText = findViewById(R.id.userEmailText);

        // Inicializar RecyclerView
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Asignar un adaptador vacío inicialmente
        RouteHistoryAdapter emptyAdapter = new RouteHistoryAdapter(new ArrayList<>());
        historyRecyclerView.setAdapter(emptyAdapter);

        // Inicializar los servicios con TokenRepository
        routeService = ApiClient.getClient(this, tokenRepository).create(RouteService.class);
        userService = ApiClient.getClient(this, tokenRepository).create(UserService.class);

        // Cargar el perfil del usuario
        fetchUserProfile();

        // Cargar el historial de rutas
        fetchRouteHistory();
    }

    private void fetchUserProfile() {
        userService.getUserProfile().enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String name = response.body().getName();
                    String email = response.body().getEmail();

                    // Logs para depuración
                    Log.d("ProfileActivity", "Nombre de usuario recibido: " + name);
                    Log.d("ProfileActivity", "Correo electrónico recibido: " + email);

                    // Manejar valores nulos o vacíos
                    if (name == null || name.isEmpty()) {
                        name = "Nombre no disponible";
                    }

                    // Actualizar los TextView con los datos del usuario
                    userNameText.setText(name);
                    userEmailText.setText(email);
                } else {
                    Log.e("ProfileActivity", "Error: Código de estado " + response.code());
                    Toast.makeText(ProfileActivity.this, "Error al cargar el perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.e("ProfileActivity", "Error de conexión", t);
                Toast.makeText(ProfileActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRouteHistory() {
        routeService.getRouteHistory().enqueue(new Callback<List<RouteHistoryResponse>>() {
            @Override
            public void onResponse(Call<List<RouteHistoryResponse>> call, Response<List<RouteHistoryResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Configurar el adaptador con los datos recibidos
                    RouteHistoryAdapter adapter = new RouteHistoryAdapter(response.body());
                    historyRecyclerView.setAdapter(adapter);
                } else {
                    Log.e("ProfileActivity", "Error: Código de estado " + response.code());
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                        Log.e("ProfileActivity", "Error body: " + errorBody);
                    } catch (IOException e) {
                        Log.e("ProfileActivity", "Error al leer el cuerpo de error", e);
                    }
                    Toast.makeText(ProfileActivity.this, "Error al cargar el historial", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RouteHistoryResponse>> call, Throwable t) {
                Log.e("ProfileActivity", "Error de conexión", t);
                Toast.makeText(ProfileActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}