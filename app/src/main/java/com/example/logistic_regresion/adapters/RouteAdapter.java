package com.example.logistic_regresion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logistic_regresion.R;
import com.example.logistic_regresion.models.Route;

import java.util.List;

import com.example.logistic_regresion.models.Route;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private List<Route> routes;

    public RouteAdapter(List<Route> routes) {
        this.routes = routes;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Route route = routes.get(position);
        holder.origin.setText("Origen: " + route.getOrigin());
        holder.destination.setText("Destino: " + route.getDestination());
        holder.distance.setText("Distancia: " + route.getDistance() + " km");
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView origin, destination, distance;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            origin = itemView.findViewById(R.id.textOrigin);
            destination = itemView.findViewById(R.id.textDestination);
            distance = itemView.findViewById(R.id.textDistance);
        }
    }
}