package com.example.logistic_regresion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logistic_regresion.R;
import com.example.logistic_regresion.repositories.TokenRepository;
import com.google.android.material.button.MaterialButton;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    @Inject
    TokenRepository tokenRepository;

    private TextView welcomeText;
    private MaterialButton profileButton;
    private MaterialButton settingsButton;
    private MaterialButton routesButton;
    private MaterialButton myRoutesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeText = findViewById(R.id.welcomeText);
        profileButton = findViewById(R.id.profileButton);
        settingsButton = findViewById(R.id.settingsButton);
        routesButton = findViewById(R.id.routesButton);
        myRoutesButton = findViewById(R.id.myRoutesButton);

        // Configura el evento onClick para el botón de perfil
        profileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        });

        // Configura el evento onClick para el botón de configuración
        settingsButton.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        });

        // Configura el evento onClick para el botón de rutas disponibles
        routesButton.setOnClickListener(v -> {
            Intent routesIntent = new Intent(HomeActivity.this, RoutesActivity.class);
            startActivity(routesIntent);
        });

        // Configura el evento onClick para el botón de "Mis Rutas"
        myRoutesButton.setOnClickListener(v -> {
            Intent myRoutesIntent = new Intent(HomeActivity.this, MyRoutesActivity.class);
            startActivity(myRoutesIntent);
        });

        // Código existente para manejar el token
        Intent intent = getIntent();
        String token = intent.getStringExtra("TOKEN");

        if (token != null) {
            tokenRepository.saveToken(token);
            welcomeText.setText("Bienvenido al sistema");
        } else {
            if (tokenRepository.hasToken()) {
                welcomeText.setText("Bienvenido de nuevo");
            } else {
                Log.e(TAG, "El token es NULL");
            }
        }
    }

    private void logout() {
        tokenRepository.clearToken();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}