package com.example.deportes2;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SportAdapter extends RecyclerView.Adapter<SportAdapter.ViewHolder> {

    private List<SportItem> allItems;
    private List<SportItem> filteredItems;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SportItem sport);
    }

    public SportAdapter(List<SportItem> itemList, OnItemClickListener listener) {
        this.allItems = itemList;
        this.filteredItems = new ArrayList<>(itemList);
        this.listener = listener;
    }

    public void filter(String text) {
        filteredItems.clear();
        for (SportItem sport : allItems) {
            if (sport.name.toLowerCase().contains(text.toLowerCase())) {
                filteredItems.add(sport);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sport, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SportItem sport = filteredItems.get(position);
        holder.sportName.setText(sport.name);
        holder.sportImage.setImageResource(sport.imageResId);

        holder.itemView.setOnClickListener(v -> {
            Log.d("SportAdapter", "Sport clicked: " + sport.name); // Debug log
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedSport", sport.name);
            ((Activity) holder.itemView.getContext()).setResult(Activity.RESULT_OK, resultIntent);
            ((Activity) holder.itemView.getContext()).finish();
        });
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sportImage;
        TextView sportName;

        public ViewHolder(View itemView) {
            super(itemView);
            sportImage = itemView.findViewById(R.id.sportImage);
            sportName = itemView.findViewById(R.id.sportName);
        }
    }
}
