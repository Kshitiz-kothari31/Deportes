package com.example.deportes2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<UserProfile> friends;
    private Context context;
    private OnFriendClickListener listener;

    public interface OnFriendClickListener {
        void onFriendClick(UserProfile friend);
    }

    public FriendAdapter(List<UserProfile> friends, Context context, OnFriendClickListener listener) {
        this.friends = friends;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.each_friend_item, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        UserProfile friend = friends.get(position);
        holder.friendName.setText(friend.getName());

        Glide.with(context)
                .load(friend.getProfileImageUrl())
                .placeholder(R.drawable.ic_profile)
                .into(holder.friendProfileImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFriendClick(friend);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView friendName;
        ShapeableImageView friendProfileImage;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friendName);
            friendProfileImage = itemView.findViewById(R.id.friendProfileImage);
        }
    }
}
