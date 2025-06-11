package com.example.deportes2;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<Post> postList;
    private final Context context;

    public PostAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        // Set name and profile image
        if (post.getProfiles() != null) {
            holder.name.setText(post.getProfiles().getName());
            Glide.with(context)
                    .load(post.getProfiles().getProfile_img())
                    .placeholder(R.drawable.profile_circle)
                    .into(holder.userImg);
        }

        holder.time.setText(getTimeAgo(post.getCreated_at()));
        holder.postMessage.setText(post.getContent());

        // Load post image only if it exists
        String imageUrl = post.getImage_url();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            holder.postImg.setVisibility(View.VISIBLE);
            Glide.with(context).load(imageUrl).into(holder.postImg);
        } else {
            holder.postImg.setVisibility(View.GONE);
        }

        // Like count and comment count
        holder.likesCount.setText(String.valueOf(post.getLikes()));
        holder.commentsCount.setText(String.valueOf(post.getComments()));

        // Like button state
        if (post.isLikedByCurrentUser()) {
            holder.likeBtn.setBackgroundResource(R.drawable.filledlikebtn);
        } else {
            holder.likeBtn.setBackgroundResource(R.drawable.outlinedlikebtn);
        }

        // Like button click
        holder.likeBtn.setOnClickListener(v -> {
            boolean liked = post.isLikedByCurrentUser();
            post.setLikedByCurrentUser(!liked);

            int updatedLikes = post.getLikes() + (liked ? -1 : 1);
            post.setLikes(updatedLikes);
            holder.likesCount.setText(String.valueOf(updatedLikes));

            holder.likeBtn.setBackgroundResource(!liked ?
                    R.drawable.filledlikebtn : R.drawable.outlinedlikebtn);

            if (liked) unlikePost(context, post.getId());
            else likePost(context, post.getId());
        });

        // Optional: comment button click
        // holder.commentBtn.setOnClickListener(v -> openCommentScreen(post.getId()));
    }

    private void likePost(Context context, String postId) {
        SupabaseManager.likePost(context, postId);
    }

    private void unlikePost(Context context, String postId) {
        SupabaseManager.unlikePost(context, postId);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView name, time, postMessage;
        ShapeableImageView userImg, postImg;
        AppCompatButton likeBtn, commentBtn;
        TextView likesCount, commentsCount;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            postMessage = itemView.findViewById(R.id.postMessage);
            userImg = itemView.findViewById(R.id.userImg);
            postImg = itemView.findViewById(R.id.postimg);
            likeBtn = itemView.findViewById(R.id.LikeBtn);
            commentBtn = itemView.findViewById(R.id.CommentBtn);
            likesCount = itemView.findViewById(R.id.likesCount);
            commentsCount = itemView.findViewById(R.id.commentsCount);
        }
    }

    public static String getTimeAgo(String timestamp) {
        try {
            // Fix timezone formatting
            if (timestamp.endsWith("Z")) {
                timestamp = timestamp.replace("Z", "+0000");
            } else {
                timestamp = timestamp.replaceAll("(\\+|\\-)(\\d{2}):(\\d{2})$", "$1$2$3");
            }

            // Trim microseconds to milliseconds
            int dotIndex = timestamp.indexOf('.');
            if (dotIndex != -1) {
                int endIndex = Math.min(dotIndex + 4, timestamp.length());
                String prefix = timestamp.substring(0, dotIndex + 4);
                String suffix = timestamp.substring(endIndex);
                timestamp = prefix + suffix;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
            Date postDate = sdf.parse(timestamp);
            long now = System.currentTimeMillis();
            long diff = now - postDate.getTime();

            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            long days = TimeUnit.MILLISECONDS.toDays(diff);

            if (minutes < 60) return minutes + " minutes ago";
            else if (hours < 24) return hours + " hours ago";
            else return days + " days ago";

        } catch (Exception e) {
            e.printStackTrace();
            return timestamp;
        }
    }

    private String getAccessToken() {
        SharedPreferences prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        return prefs.getString("access_token", null);
    }

    private String getUserIdFromJwt(String jwt) {
        try {
            String[] parts = jwt.split("\\.");
            byte[] decodedBytes = android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE);
            String payload = new String(decodedBytes);
            JSONObject json = new JSONObject(payload);
            return json.getString("sub"); // or "user_id"
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
