package com.example.logistic_regresion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.logistic_regresion.clients.ApiClient;
import com.example.logistic_regresion.repositories.TokenRepository;
import com.example.logistic_regresion.requests.ForgotPasswordRequest;
import com.example.logistic_regresion.services.AuthService;
import com.example.logistic_regresion.requests.LoginRequest;
import com.example.logistic_regresion.responses.LoginResponse;
import com.example.logistic_regresion.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Map;
import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    @Inject
    TokenRepository tokenRepository;

    private EditText usernameEditText, passwordEditText; // Cambiado de emailEditText a usernameEditText
    private Button loginButton;
    private TextView registerButton, resetPasswordLink;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar AuthService con TokenRepository
        authService = ApiClient.getClient(this, tokenRepository).create(AuthService.class);

        if (tokenRepository.hasToken()) {
            String token = tokenRepository.getToken();
            if (isTokenValid(token)) {
                navigateToHome();
                return;
            } else {
                tokenRepository.clearToken(); // Limpia el token inválido
            }
        }

        // Inicializar vistas
        usernameEditText = findViewById(R.id.usernameEditText); // Cambiado
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        resetPasswordLink = findViewById(R.id.resetPasswordLink);
        registerButton = findViewById(R.id.signupLink);

        // Configurar listeners
        loginButton.setOnClickListener(v -> loginUser());
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
        resetPasswordLink.setOnClickListener(v -> showResetPasswordDialog());
    }

    private boolean isTokenValid(String token) {
        return token != null && !token.isEmpty();
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString().trim(); // Cambiado
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<LoginResponse> call = authService.loginUser(new LoginRequest(username, password));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    tokenRepository.saveToken(token);
                    Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    navigateToHome();
                } else {
                    Log.e(TAG, "Error de inicio de sesión: " + response.code());
                    Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "Error de conexión", t);
                Toast.makeText(LoginActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showResetPasswordDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);

        TextInputEditText emailInput = view.findViewById(R.id.emailInput);
        MaterialButton resetButton = view.findViewById(R.id.resetPasswordButton);

        AlertDialog dialog = builder.setView(view)
                .setCancelable(true)
                .create();

        resetButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese su email", Toast.LENGTH_SHORT).show();
                return;
            }
            dialog.dismiss();
            sendResetPasswordRequest(email);
        });

        dialog.show();
    }

    private void sendResetPasswordRequest(String email) {
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);
        authService.requestPasswordReset(request).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Código enviado a su email", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, VerificationCodeActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("isPasswordReset", true);
                    startActivity(intent);
                } else {
                    try {
                        Log.e(TAG, "Error al enviar el código: " + (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
                        Toast.makeText(LoginActivity.this, "Error al enviar el código", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e(TAG, "Error al leer el cuerpo de error", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e(TAG, "Error de conexión", t);
                Toast.makeText(LoginActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}