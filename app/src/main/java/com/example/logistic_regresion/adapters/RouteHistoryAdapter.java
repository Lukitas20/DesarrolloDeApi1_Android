package com.example.logistic_regresion.adapters;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
        holder.orderStatusText.setText("Distancia: " + route.getDistance() + " km");

        if (route.getReview() != null) {
            holder.reviewCommentText.setText(route.getReview().getComentario());
            holder.reviewRatingBar.setRating(route.getReview().getPuntuacion());

            String imageUrl = route.getReview().getImagenUrl();
            Log.d("IMAGEN_URL", "URL de imagen recibida: " + imageUrl);
            if (!TextUtils.isEmpty(imageUrl)) {
                holder.reviewImage.setVisibility(View.VISIBLE);
                Glide.with(holder.reviewImage.getContext())
                        .load(imageUrl)
                        .into(holder.reviewImage);
            } else {
                holder.reviewImage.setVisibility(View.GONE);
            }
        } else {
            holder.reviewCommentText.setVisibility(View.GONE);
            holder.reviewRatingBar.setRating(0);
            holder.reviewRatingBar.setVisibility(View.GONE);
            holder.reviewImage.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return routeHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView originDestinationText, completionTimeText, paymentText, orderStatusText, reviewCommentText;
        RatingBar reviewRatingBar;
        ImageView reviewImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            originDestinationText = itemView.findViewById(R.id.originDestinationText);
            completionTimeText = itemView.findViewById(R.id.completionTimeText);
            paymentText = itemView.findViewById(R.id.paymentText);
            orderStatusText = itemView.findViewById(R.id.orderStatusText);
            reviewCommentText = itemView.findViewById(R.id.reviewCommentText);
            reviewRatingBar = itemView.findViewById(R.id.reviewRatingBar);
            reviewImage = itemView.findViewById(R.id.reviewImage);
        }
    }
}