package com.example.logistic_regresion.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.logistic_regresion.R;
import com.example.logistic_regresion.adapters.AvatarAdapter;
import com.example.logistic_regresion.adapters.RouteHistoryAdapter;
import com.example.logistic_regresion.clients.ApiClient;
import com.example.logistic_regresion.repositories.TokenRepository;
import com.example.logistic_regresion.responses.RouteHistoryResponse;
import com.example.logistic_regresion.responses.UserProfileResponse;
import com.example.logistic_regresion.services.RouteService;
import com.example.logistic_regresion.services.UserService;
import com.google.android.material.button.MaterialButton;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private RecyclerView historyRecyclerView;
    private RouteService routeService;
    private UserService userService;
    private TextView userNameText, userEmailText;
    private ImageView profileImage;
    private MaterialButton editProfileButton;

    @Inject
    TokenRepository tokenRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Perfil");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        userNameText = findViewById(R.id.userNameText);
        userEmailText = findViewById(R.id.userEmailText);
        profileImage = findViewById(R.id.profileImage);
        editProfileButton = findViewById(R.id.editProfileButton);

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RouteHistoryAdapter emptyAdapter = new RouteHistoryAdapter(new ArrayList<>());
        historyRecyclerView.setAdapter(emptyAdapter);

        routeService = ApiClient.getClient(this, tokenRepository).create(RouteService.class);
        userService = ApiClient.getClient(this, tokenRepository).create(UserService.class);

        fetchUserProfile();
        fetchRouteHistory();

        editProfileButton.setOnClickListener(v -> showAvatarSelectionDialog());
    }

    private void showAvatarSelectionDialog() {
        // Lista de IDs de las imágenes locales
        List<Integer> avatarIds = new ArrayList<>();
        avatarIds.add(R.drawable.avatar1);
        avatarIds.add(R.drawable.avatar2);
        avatarIds.add(R.drawable.avatar3);
        avatarIds.add(R.drawable.avatar4);
        avatarIds.add(R.drawable.avatar5);
        avatarIds.add(R.drawable.avatar6);
        avatarIds.add(R.drawable.avatar7);
        avatarIds.add(R.drawable.avatar8);
        avatarIds.add(R.drawable.avatar9);
        avatarIds.add(R.drawable.avatar10);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Selecciona un avatar");

        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        AvatarAdapter adapter = new AvatarAdapter(avatarIds, id -> {
            Glide.with(ProfileActivity.this)
                    .load(id)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(profileImage);
        });
        recyclerView.setAdapter(adapter);

        builder.setView(recyclerView);
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri sourceUri = data.getData();
            if (sourceUri != null) {
                File destinationFile = new File(getCacheDir(), "croppedImage.jpg");
                Uri destinationUri = Uri.fromFile(destinationFile);

                UCrop.of(sourceUri, destinationUri)
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(500, 500)
                        .start(this);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                profileImage.setImageURI(resultUri);
            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
            Throwable cropError = UCrop.getError(data);
            if (cropError != null) {
                Toast.makeText(this, "Error al recortar la imagen: " + cropError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Volver a la actividad anterior
        return true;
    }
    private void fetchUserProfile() {
        userService.getUserProfile().enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String name = response.body().getUsername();
                    String email = response.body().getEmail();

                    if (name == null || name.isEmpty()) {
                        name = "Nombre no disponible";
                    }

                    userNameText.setText(name);
                    userEmailText.setText(email);

                    Glide.with(ProfileActivity.this)
                            .load(R.drawable.placeholder_image)
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.error_image)
                            .into(profileImage);
                } else {
                    Toast.makeText(ProfileActivity.this, "Error al cargar el perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRouteHistory() {
        routeService.getRouteHistory().enqueue(new Callback<List<RouteHistoryResponse>>() {
            @Override
            public void onResponse(Call<List<RouteHistoryResponse>> call, Response<List<RouteHistoryResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RouteHistoryAdapter adapter = new RouteHistoryAdapter(response.body());
                    historyRecyclerView.setAdapter(adapter);
                } else {
                    Log.e("API Error", "Código de error: " + response.code());
                    Log.e("API Error", "Error body: " + response.errorBody());
                    Toast.makeText(ProfileActivity.this, "Error al cargar el historial", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RouteHistoryResponse>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}