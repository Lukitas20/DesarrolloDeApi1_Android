package com.example.logistic_regresion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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