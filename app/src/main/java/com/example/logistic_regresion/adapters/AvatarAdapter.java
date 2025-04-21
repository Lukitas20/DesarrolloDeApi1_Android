package com.example.logistic_regresion.adapters;

import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.logistic_regresion.R;
import java.util.List;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder> {
    private final List<Integer> avatarIds;
    private final OnAvatarClickListener listener;

    public interface OnAvatarClickListener {
        void onAvatarClick(int id);
    }

    public AvatarAdapter(List<Integer> avatarIds, OnAvatarClickListener listener) {
        this.avatarIds = avatarIds;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AvatarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        int size = (int) (120 * parent.getContext().getResources().getDisplayMetrics().density);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); // Asegura que la imagen se ajuste bien
        return new AvatarViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarViewHolder holder, int position) {
        int id = avatarIds.get(position);
        Glide.with(holder.imageView.getContext())
                .load(id)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(v -> listener.onAvatarClick(id));
    }

    @Override
    public int getItemCount() {
        return avatarIds.size();
    }

    public static class AvatarViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public AvatarViewHolder(@NonNull ImageView itemView) {
            super(itemView);
            this.imageView = itemView;
        }
    }
}