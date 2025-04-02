package com.example.logistic_regresion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logistic_regresion.clients.ApiClient;
import com.example.logistic_regresion.requests.ResendVerificationRequest;
import com.example.logistic_regresion.requests.VerifyResetCodeRequest;
import com.example.logistic_regresion.services.AuthService;
import com.example.logistic_regresion.R;
import com.example.logistic_regresion.requests.VerifyRequest;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationCodeActivity extends AppCompatActivity {
    private static final String TAG = "VerificationCode";
    private EditText[] digits;
    private MaterialButton verifyButton;
    private TextView resendCode;
    private String email;
    private AuthService authService;
    private boolean isPasswordReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);

        email = getIntent().getStringExtra("email");
        isPasswordReset = getIntent().getBooleanExtra("isPasswordReset", false);

        if (email == null || email.isEmpty()) {
            Log.e(TAG, "No email provided");
            Toast.makeText(this, "Error: Email no proporcionado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        authService = ApiClient.getClient(this).create(AuthService.class);

        setupViews();
        setupDigitInputs();
        setupButtons();
    }

    private void setupViews() {
        digits = new EditText[]{
                findViewById(R.id.digit1),
                findViewById(R.id.digit2),
                findViewById(R.id.digit3),
                findViewById(R.id.digit4),
                findViewById(R.id.digit5),
                findViewById(R.id.digit6)
        };
        verifyButton = findViewById(R.id.verifyButton);
        resendCode = findViewById(R.id.resendCode);
    }

    private void setupDigitInputs() {
        for (int i = 0; i < digits.length; i++) {
            final int index = i;
            digits[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && index < digits.length - 1) {
                        digits[index + 1].requestFocus();
                    } else if (s.length() == 0 && index > 0) {
                        digits[index - 1].requestFocus();
                    }
                }
            });
        }
    }

    private void setupButtons() {
        verifyButton.setOnClickListener(v -> verifyCode());
        resendCode.setOnClickListener(v -> resendVerificationCode());
    }

    private void verifyCode() {
        StringBuilder codeBuilder = new StringBuilder();
        for (EditText digit : digits) {
            codeBuilder.append(digit.getText());
        }

        if (codeBuilder.length() != 6) {
            Toast.makeText(this, "Por favor ingrese el código completo", Toast.LENGTH_SHORT).show();
            return;
        }

        verifyButton.setEnabled(false);
        String code = codeBuilder.toString().trim();

        if (isPasswordReset) {
            verifyResetCode(code);
        } else {
            verifyRegistrationCode(code);
        }
    }

    private void verifyResetCode(String code) {
        VerifyResetCodeRequest request = new VerifyResetCodeRequest(email, code);

        authService.verifyResetCode(request).enqueue(new Callback<Map<String, Boolean>>() {
            @Override
            public void onResponse(Call<Map<String, Boolean>> call, Response<Map<String, Boolean>> response) {
                verifyButton.setEnabled(true);
                if (response.isSuccessful() && response.body() != null &&
                        Boolean.TRUE.equals(response.body().get("verified"))) {

                    Intent intent = new Intent(VerificationCodeActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("code", code);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        Log.e(TAG, "Error: " + (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading error response", e);
                    }
                    Toast.makeText(VerificationCodeActivity.this, "Código inválido", Toast.LENGTH_SHORT).show();
                    clearDigits();
                    digits[0].requestFocus();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Boolean>> call, Throwable t) {
                Log.e(TAG, "Network error", t);
                verifyButton.setEnabled(true);
                Toast.makeText(VerificationCodeActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyRegistrationCode(String code) {
        VerifyRequest request = new VerifyRequest(email, code);

        authService.verifyCode(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                verifyButton.setEnabled(true);
                if (response.isSuccessful()) {
                    handleSuccessfulVerification(code);
                } else {
                    try {
                        Log.e(TAG, "Error response: " + response.code() + " - " +
                                (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    Toast.makeText(VerificationCodeActivity.this,
                            "Código inválido", Toast.LENGTH_SHORT).show();
                    clearDigits();
                    digits[0].requestFocus();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Network error", t);
                verifyButton.setEnabled(true);
                Toast.makeText(VerificationCodeActivity.this,
                        "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void handleSuccessfulVerification(String code) {
        if (isPasswordReset) {
            Intent intent = new Intent(this, ResetPasswordActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("code", code);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Verificación exitosa", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        finish();
    }

    private void resendVerificationCode() {
        resendCode.setEnabled(false);

        String codeType = isPasswordReset ? "passwordReset" : "verification";

        ResendVerificationRequest request = new ResendVerificationRequest(email, codeType);

        authService.resendVerificationCode(request).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                resendCode.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(VerificationCodeActivity.this,
                            "Código reenviado exitosamente", Toast.LENGTH_SHORT).show();
                    clearDigits();
                    digits[0].requestFocus();
                } else {
                    try {
                        Log.e(TAG, "Error response: " + response.code() + " - " +
                                (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    Toast.makeText(VerificationCodeActivity.this,
                            "Error al reenviar el código", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e(TAG, "Network error", t);
                resendCode.setEnabled(true);
                Toast.makeText(VerificationCodeActivity.this,
                        "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearDigits() {
        for (EditText digit : digits) {
            digit.setText("");
        }
    }
}