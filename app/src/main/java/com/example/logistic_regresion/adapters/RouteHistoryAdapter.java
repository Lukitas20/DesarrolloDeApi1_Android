package com.example.logistic_regresion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.logistic_regresion.R;
import com.example.logistic_regresion.responses.RouteHistoryResponse;
import java.util.List;

public class RouteHistoryAdapter extends RecyclerView.Adapter<RouteHistoryAdapter.ViewHolder> {
    private final List<RouteHistoryResponse> routeHistoryList;

    public RouteHistoryAdapter(List<RouteHistoryResponse> routeHistoryList) {
        this.routeHistoryList = routeHistoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RouteHistoryResponse route = routeHistoryList.get(position);
        holder.originDestinationText.setText(route.getOrigin() + " - " + route.getDestination());
        holder.completionTimeText.setText("Tiempo: " + route.getCompletionTime());
        holder.paymentText.setText("Pago: $" + route.getPayment());
        holder.orderStatusText.setText("Distancia: " + route.getDistance() + " km");    }

    @Override
    public int getItemCount() {
        return routeHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView originDestinationText, completionTimeText, paymentText, orderStatusText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            originDestinationText = itemView.findViewById(R.id.originDestinationText);
            completionTimeText = itemView.findViewById(R.id.completionTimeText);
            paymentText = itemView.findViewById(R.id.paymentText);
            orderStatusText = itemView.findViewById(R.id.orderStatusText); // Nuevo campo
        }
    }
}