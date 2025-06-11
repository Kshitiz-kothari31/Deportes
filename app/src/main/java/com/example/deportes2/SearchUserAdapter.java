package com.example.deportes2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.deportes2.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {

    public interface OnUserClickListener {
        void onUserClick(UserModel user);
    }

    private OnUserClickListener listener;

    public void setOnUserClickListener(OnUserClickListener listener) {
        this.listener = listener;
    }


    private List<UserModel> users;
    private Context context;

    public SearchUserAdapter(Context context, List<UserModel> users) {
        this.context = context;
        this.users = users;
    }

    public void updateList(List<UserModel> newUsers) {
        this.users.clear();            // clear the current list
        this.users.addAll(newUsers);   // add the new users
        notifyDataSetChanged();        // refresh the list
    }

    @NonNull
    @Override
    public SearchUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserAdapter.ViewHolder holder, int position) {
        UserModel user = users.get(position);
        holder.name.setText(user.getName());
        holder.username.setText("@" + user.getUsername());

        holder.itemView.setOnClickListener(v -> {
            if(listener != null){
                listener.onUserClick(users.get(position));
            }
        });

        Glide.with(context)
                .load(user.getProfileImg())
                .placeholder(R.drawable.ic_profile)
                .into(holder.profileImg);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, username;
        ShapeableImageView profileImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.search_user_name);
            username = itemView.findViewById(R.id.search_user_username);
            profileImg = itemView.findViewById(R.id.search_user_profile_img);
        }
    }
}