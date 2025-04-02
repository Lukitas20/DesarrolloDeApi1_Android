package com.example.logistic_regresion.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.logistic_regresion.R;
import com.example.logistic_regresion.adapters.DeliveryAdapter;
import com.example.logistic_regresion.models.DeliveryItem;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Habilitar la flecha de navegación
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.deliveryHistoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<DeliveryItem> deliveryItemList = new ArrayList<>();
        deliveryItemList.add(new DeliveryItem("Producto A", "30 minutos", 3));
        deliveryItemList.add(new DeliveryItem("Producto B", "45 minutos", 4));
        deliveryItemList.add(new DeliveryItem("Producto C", "25 minutos", 5));

        DeliveryAdapter adapter = new DeliveryAdapter(deliveryItemList);
        recyclerView.setAdapter(adapter);

        // Configurar el botón de cerrar sesión
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoCerrarSesion();
            }
        });
    }

    private void mostrarDialogoCerrarSesion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cerrarSesion();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void cerrarSesion() {
        // Implementa la lógica para cerrar sesión
        // Por ejemplo, limpiar las preferencias compartidas y redirigir al login
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Volver a la actividad principal (HomeActivity)
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}