package com.example.logistic_regresion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.logistic_regresion.R;
import com.example.logistic_regresion.repositories.TokenRepository;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    @Inject
    TokenRepository tokenRepository;

    private TextView welcomeText;
    private ImageButton profileButton;
    private ImageButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeText = findViewById(R.id.welcomeText);
        profileButton = findViewById(R.id.profileButton);
        settingsButton = findViewById(R.id.settingsButton);

        // Obtener el token del intent si está disponible
        Intent intent = getIntent();
        String token = intent.getStringExtra("TOKEN");

        // Si el token está disponible, guardarlo y mostrar el pantalla de home
        if (token != null) {
            tokenRepository.saveToken(token);
            welcomeText.setText("Bienvenido al sistema");
        } else {
            // Verificar si el token está almacenado
            if (tokenRepository.hasToken()) {
                welcomeText.setText("Bienvenido de nuevo");
            } else {
                Log.e(TAG, "El token es NULL");
            }
        }
      
        profileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        });

        settingsButton.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        });
      
    }

    private void logout() {
        tokenRepository.clearToken();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}