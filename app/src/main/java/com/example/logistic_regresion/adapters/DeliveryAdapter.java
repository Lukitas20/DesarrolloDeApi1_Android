package com.example.logistic_regresion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.logistic_regresion.R;
import com.example.logistic_regresion.models.DeliveryItem;
import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {

    private List<DeliveryItem> deliveryItemList;

    public DeliveryAdapter(List<DeliveryItem> deliveryItemList) {
        this.deliveryItemList = deliveryItemList;
    }

    @NonNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryViewHolder holder, int position) {
        DeliveryItem item = deliveryItemList.get(position);
        holder.productName.setText("Producto: " + item.getProductName());
        holder.deliveryTime.setText("Tiempo de Entrega: " + item.getDeliveryTime());
        holder.ratingBar.setRating(item.getRating());
    }

    @Override
    public int getItemCount() {
        return deliveryItemList.size();
    }

    public static class DeliveryViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView deliveryTime;
        RatingBar ratingBar;

        public DeliveryViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            deliveryTime = itemView.findViewById(R.id.deliveryTime);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}