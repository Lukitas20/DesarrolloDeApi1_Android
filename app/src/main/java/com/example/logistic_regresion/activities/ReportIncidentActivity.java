package com.example.logistic_regresion.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.logistic_regresion.R;
import com.example.logistic_regresion.clients.ApiClient;
import com.example.logistic_regresion.models.Incident;
import com.example.logistic_regresion.repositories.TokenRepository;
import com.example.logistic_regresion.services.RouteService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ReportIncidentActivity extends AppCompatActivity {

    private static final String TAG = "ReportIncidentActivity";
    private static final int STORAGE_PERMISSION_REQUEST = 100;
    private static final int PICK_IMAGE_REQUEST = 101;

    private Spinner spinnerIncidentType;
    private EditText editTextDescription;
    private Button buttonTakePhoto; // We're keeping the same ID but using it for gallery selection
    private ImageView imageViewPhoto;
    private Button buttonSubmit;

    private Long routeId;
    private String photoBase64;
    private RouteService routeService;

    @Inject
    TokenRepository tokenRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);
        setTitle("Reportar Incidente");

        // Get routeId from intent
        routeId = getIntent().getLongExtra("routeId", -1);
        if (routeId == -1) {
            Toast.makeText(this, "Error: No se encontró información de la ruta", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        spinnerIncidentType = findViewById(R.id.spinnerIncidentType);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonTakePhoto = findViewById(R.id.buttonTakePhoto);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Create API client
        routeService = ApiClient.getClient(this, tokenRepository).create(RouteService.class);

        // Enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup incident type spinner
        setupIncidentTypeSpinner();

        // Setup gallery button
        buttonTakePhoto.setOnClickListener(v -> requestGalleryPermission());

        // Setup submit button
        buttonSubmit.setOnClickListener(v -> submitIncidentReport());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setupIncidentTypeSpinner() {
        // Define incident types
        String[] incidentTypes = new String[]{
                "Dirección incorrecta",
                "Cliente ausente",
                "Paquete dañado",
                "Zona peligrosa",
                "Entrega rechazada",
                "Otro"
        };

        // Create adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, incidentTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply adapter to spinner
        spinnerIncidentType.setAdapter(adapter);
    }

    private void requestGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13+ (API 33+)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        STORAGE_PERMISSION_REQUEST);
            } else {
                openGallery();
            }
        } else {
            // For Android 12 and below
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_REQUEST);
            } else {
                openGallery();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Se requiere permiso para acceder a la galería",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                    // Show photo in ImageView
                    imageViewPhoto.setImageBitmap(imageBitmap);
                    imageViewPhoto.setVisibility(View.VISIBLE);

                    // Convert bitmap to Base64 string for upload
                    photoBase64 = bitmapToBase64(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void submitIncidentReport() {
        String type = spinnerIncidentType.getSelectedItem().toString();
        String description = editTextDescription.getText().toString().trim();

        if (description.isEmpty()) {
            editTextDescription.setError("La descripción es obligatoria");
            return;
        }

        // Create incident object
        Incident incident = new Incident(type, description, photoBase64, routeId);

        // Get token
        String token = tokenRepository.getToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Error: Sesión expirada", Toast.LENGTH_SHORT).show();
            return;
        }
        token = "Bearer " + token;

        // Show loading state
        buttonSubmit.setEnabled(false);
        buttonSubmit.setText("Enviando...");

        // Call API
        routeService.reportIncident(incident, token).enqueue(new Callback<Incident>() {
            @Override
            public void onResponse(Call<Incident> call, Response<Incident> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReportIncidentActivity.this,
                            "Incidente reportado exitosamente", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Log.e(TAG, "Error al reportar incidente: " + response.code());
                    Toast.makeText(ReportIncidentActivity.this,
                            "Error al reportar incidente: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                    buttonSubmit.setEnabled(true);
                    buttonSubmit.setText("Enviar Reporte");
                }
            }

            @Override
            public void onFailure(Call<Incident> call, Throwable t) {
                Log.e(TAG, "Error de conexión al reportar incidente", t);
                Toast.makeText(ReportIncidentActivity.this,
                        "Error de conexión. Intente nuevamente",
                        Toast.LENGTH_SHORT).show();
                buttonSubmit.setEnabled(true);
                buttonSubmit.setText("Enviar Reporte");
            }
        });
    }
}
