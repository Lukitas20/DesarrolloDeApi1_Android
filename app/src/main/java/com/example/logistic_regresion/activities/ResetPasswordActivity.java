package com.example.logistic_regresion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.logistic_regresion.clients.ApiClient;
import com.example.logistic_regresion.services.AuthService;
import com.example.logistic_regresion.R;
import com.example.logistic_regresion.requests.ResetPasswordRequest;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ResetPasswordActivity";
    private TextInputEditText newPasswordEditText;
    private TextInputEditText confirmPasswordEditText;
    private Button changePasswordButton;
    private String email;
    private String code;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        authService = ApiClient.getClient(this).create(AuthService.class);
        email = getIntent().getStringExtra("email");
        code = getIntent().getStringExtra("code");

        if (email == null || code == null) {
            Toast.makeText(this, "Error: Datos inválidos", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        changePasswordButton = findViewById(R.id.changePasswordButton);

        changePasswordButton.setOnClickListener(v -> validateAndChangePassword());
    }

    private void validateAndChangePassword() {
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        changePasswordButton.setEnabled(false);

        ResetPasswordRequest request = new ResetPasswordRequest(email, code, newPassword, confirmPassword);
        authService.resetPassword(request).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                changePasswordButton.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this,
                            "Contraseña actualizada exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Unknown error";
                        Log.e(TAG, "Error response: " + response.code() + " - " + errorBody);
                        Toast.makeText(ResetPasswordActivity.this,
                                "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e(TAG, "Network error", t);
                changePasswordButton.setEnabled(true);
                Toast.makeText(ResetPasswordActivity.this,
                        "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}