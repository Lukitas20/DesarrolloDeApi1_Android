package com.example.logistic_regresion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logistic_regresion.R;
import com.example.logistic_regresion.models.Route;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private List<Route> routes;
    private OnRouteActionListener actionListener;

    public RouteAdapter(List<Route> routes, OnRouteActionListener actionListener) {
        this.routes = routes;
        this.actionListener = actionListener;
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

        // Configurar los datos de la ruta
        holder.origin.setText("Origen: " + route.getOrigin());
        holder.destination.setText("Destino: " + route.getDestination());
        holder.distance.setText("Distancia: " + route.getDistance() + " km");

        // Controlar la visibilidad de los botones según el estado de la ruta
        if ("AVAILABLE".equals(route.getStatus())) {
            holder.assignButton.setVisibility(View.VISIBLE);
            holder.completeButton.setVisibility(View.GONE);
            holder.cancelButton.setVisibility(View.GONE);

            holder.assignButton.setOnClickListener(v -> actionListener.onAssign(route.getId()));
        } else if ("ASSIGNED".equals(route.getStatus())) {
            holder.assignButton.setVisibility(View.GONE);
            holder.completeButton.setVisibility(View.VISIBLE);
            holder.cancelButton.setVisibility(View.VISIBLE);

            holder.completeButton.setOnClickListener(v -> actionListener.onComplete(route.getId()));
            holder.cancelButton.setOnClickListener(v -> actionListener.onCancel(route.getId()));
        } else {
            // Ocultar todos los botones para otros estados
            holder.assignButton.setVisibility(View.GONE);
            holder.completeButton.setVisibility(View.GONE);
            holder.cancelButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView origin, destination, distance;
        Button assignButton, completeButton, cancelButton;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            origin = itemView.findViewById(R.id.textOrigin);
            destination = itemView.findViewById(R.id.textDestination);
            distance = itemView.findViewById(R.id.textDistance);
            assignButton = itemView.findViewById(R.id.assignButton);
            completeButton = itemView.findViewById(R.id.completeButton);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }
    }

    public interface OnRouteActionListener {
        void onAssign(Long routeId);
        void onComplete(Long routeId);
        void onCancel(Long routeId);
    }
}