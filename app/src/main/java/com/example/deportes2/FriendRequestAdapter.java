package com.example.deportes2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.function.BiConsumer;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {
    private List<FriendRequest> requests;
    private BiConsumer<String, String> actionHandler;

    public FriendRequestAdapter(List<FriendRequest> requests, BiConsumer<String, String> actionHandler) {
        this.requests = requests;
        this.actionHandler = actionHandler;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFrom;
        Button btnAccept, btnReject;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFrom = itemView.findViewById(R.id.tvFromUserId);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FriendRequest request = requests.get(position);
        holder.tvFrom.setText("From: " + request.getSenderId());
        Log.d("Adapter", "Binding request from: " + request.getSenderId());
        holder.btnAccept.setOnClickListener(v -> actionHandler.accept(request.getId(), "accepted"));
        holder.btnReject.setOnClickListener(v -> actionHandler.accept(request.getId(), "rejected"));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
}
