package com.example.deportes2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        holder.name.setText(post.getUser_name()); // You might replace this with real user name later
        holder.time.setText(getTimeAgo(post.getCreated_at()));
        holder.postMessage.setText(post.getContent());

//        holder.commentBtn.setOnClickListener(v -> {
//            openCommentScreen(post.getId());
//        });

        Glide.with(context)
                .load(post.getProfile_image_url())
                .placeholder(R.drawable.profile_circle)
                .into(holder.userImg);

        Glide.with(context)
                .load(post.getImage_url()) // Actual post image
                .into(holder.postImg);

        holder.likesCount.setText(String.valueOf(post.getLikes()));
        holder.commentsCount.setText(String.valueOf(post.getComments()));

        if (post.isLikedByCurrentUser()) {
            holder.likeBtn.setBackgroundResource(R.drawable.filledlikebtn);
        } else {
            holder.likeBtn.setBackgroundResource(R.drawable.outlinedlikebtn);
        }

        holder.likeBtn.setOnClickListener(v -> {
            boolean currentlyLiked = post.isLikedByCurrentUser();
            post.setLikedByCurrentUser(!currentlyLiked);

            int newLikeCount = post.getLikes() + (currentlyLiked ? -1 : 1);
            post.setLikes(newLikeCount);
            holder.likesCount.setText(String.valueOf(newLikeCount));

            // Update Supabase like table
//            if (currentlyLiked) {
//                unlikePost(post.getId());
//            } else {
//                likePost(post.getId());
//            }

            // Optional: update button UI
            holder.likeBtn.setBackgroundResource(currentlyLiked ?
                    R.drawable.outlinedlikebtn : R.drawable.filledlikebtn);
        });

        // Handle comment button click
//        holder.commentBtn.setOnClickListener(v -> {
//            // You can launch a comment activity or bottom sheet
//            // Pass post.getId() to it
//            openCommentScreen(post.getId());
//        });
    }

    private void likePost(Context context, String postId) {
        SupabaseManager.likePost(context, postId);  // Create this method in SupabaseManager
    }

    private void unlikePost(Context context, String postId) {
        SupabaseManager.unlikePost(context, postId);  // Create this method in SupabaseManager
    }

//    private void openCommentScreen(String postId) {
//        Intent intent = new Intent(context, CommentActivity.class);
//        intent.putExtra("post_id", postId);
//        context.startActivity(intent);
//    }

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
            return json.getString("sub"); // might be "user_id" for older Supabase setups
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTimeAgo(String timestamp) {
        try {
            // Fix the timezone format (remove colon)
            if (timestamp.endsWith("Z")) {
                timestamp = timestamp.replace("Z", "+0000");
            } else {
                timestamp = timestamp.replaceAll("(\\+|\\-)(\\d{2}):(\\d{2})$", "$1$2$3");
            }

            // Trim microseconds to milliseconds (3 digits after '.')
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

}
