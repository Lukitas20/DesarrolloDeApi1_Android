package com.example.logistic_regresion.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.logistic_regresion.clients.ApiClient;
import com.example.logistic_regresion.services.AuthService;
import com.example.logistic_regresion.R;
import com.example.logistic_regresion.requests.SignupRequest;
import com.example.logistic_regresion.responses.SignupResponse;

import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private EditText userNameEditText, emailEditText, passwordEditText;
    private Button signupButton;
    private TextView loginLink;
    private AuthService authService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authService = ApiClient.getClient(this).create(AuthService.class);

        userNameEditText = findViewById(R.id.userNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginLink = findViewById(R.id.loginLink);

        signupButton.setOnClickListener(v -> signupUser());
        loginLink.setOnClickListener(v -> finish());
    }

    private void signupUser() {
        String userName = userNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SignupRequest request = new SignupRequest(userName, email, password);
        authService.registerUser(request).enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                Log.d(TAG, "Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    SignupResponse signupResponse = response.body();
                    Log.d(TAG, "Attempting to start VerificationCodeActivity...");
                    try {
                        Intent intent = new Intent(SignupActivity.this, VerificationCodeActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        Log.d(TAG, "VerificationCodeActivity started successfully");
                        finish();
                    } catch (Exception e) {
                        Log.e(TAG, "Error starting VerificationCodeActivity", e);
                        Toast.makeText(SignupActivity.this,
                                "Error al abrir la verificación", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "No error body";
                        Log.e(TAG, "Error response: " + errorBody);

                        if (errorBody.contains("current transaction is aborted")) {
                            Toast.makeText(SignupActivity.this,
                                    "Error temporal. Por favor intente de nuevo", Toast.LENGTH_LONG).show();
                        } else if (response.code() == 409) {
                            Toast.makeText(SignupActivity.this,
                                    "El email ya está registrado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignupActivity.this,
                                    "Error en el registro", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading error body", e);
                        Toast.makeText(SignupActivity.this,
                                "Error en el registro", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Log.e(TAG, "Network error", t);
                Toast.makeText(SignupActivity.this,
                        "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}