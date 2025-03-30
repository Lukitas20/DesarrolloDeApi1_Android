package com.example.logistic_regresion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logistic_regresion.R;

public class HomeActivity extends AppCompatActivity {
    private TextView welcomeText;
    private Button logoutButton;
    private ImageButton profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeText = findViewById(R.id.welcomeText);
        logoutButton = findViewById(R.id.logoutButton);
        profileButton = findViewById(R.id.profileButton);

        // Obtener el token desde el Intent
        Intent intent = getIntent();
        String token = intent.getStringExtra("TOKEN");
        Log.d("HomeActivity", "Token recibido: " + token);

        if (token != null) {
            welcomeText.setText("Bienvenido al sistema");
        } else {
            Log.e("HomeActivity", "El token es NULL");
        }

        logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        });

        profileButton.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        });
    }

    private void logout() {
        // Clear preferences or stored token
        // Navigate back to login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}