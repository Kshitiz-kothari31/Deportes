package com.example.deportes2;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Response;

public class SearchUserProfileLayoutActivity extends AppCompatActivity {

    private ShapeableImageView profileImg;
    private ImageView bgImg;
    private TextView name, username, bio;
    private LinearLayout postsContainer;
    private Button addFriendBtn;

    private String searchUserId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.search_user_profile_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.users_profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bgImg = findViewById(R.id.users_bg_img);
        profileImg = findViewById(R.id.profile_img);
        name = findViewById(R.id.name);
        username = findViewById(R.id.user_name);
        bio = findViewById(R.id.bio);
        postsContainer = findViewById(R.id.postsContainer);
        addFriendBtn = findViewById(R.id.addFriend);

        searchUserId = getIntent().getStringExtra("FRIEND_USER_ID");

        if (searchUserId != null) {
            fetchUserProfile(searchUserId);
            fetchUserPosts(searchUserId);
        }else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        addFriendBtn.setOnClickListener(view -> sendFriendRequest(searchUserId));
    }

    private void fetchUserProfile(String id) {
        SupabaseManager.checkAndRefreshToken(this, new SupabaseManager.TokenCheckCallback() {
            @Override
            public void onTokenReady(String accessToken) {
                SupabaseManager.getProfile(id, accessToken, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(() -> {
                            // Show error
                            Toast.makeText(getApplicationContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String jsonData = response.body().string();
                            try {
                                JSONArray arr = new JSONArray(jsonData);
                                if (arr.length() > 0) {
                                    JSONObject profile = arr.getJSONObject(0);
                                    String u_name = profile.optString("name");
                                    String u_username = profile.optString("username");
                                    String u_profileImgUrl = profile.optString("profile_img");
                                    String u_bg_ImgUrl = profile.optString("bg_img");
                                    String u_bio = profile.optString("bio");

                                    runOnUiThread(() -> {
                                        name.setText(u_name);
                                        username.setText(u_username);
                                        bio.setText(u_bio);
                                        Glide.with(SearchUserProfileLayoutActivity.this).load(u_bg_ImgUrl).into(bgImg);
                                        Glide.with(SearchUserProfileLayoutActivity.this).load(u_profileImgUrl).into(profileImg);
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void fetchUserPosts(String userId) {
        SupabaseManager.checkAndRefreshToken(this, new SupabaseManager.TokenCheckCallback() {
            @Override
            public void onTokenReady(String accessToken) {
                SupabaseManager.fetchPostsByUser(userId, accessToken, new SupabaseManager.PostsCallback() {
                    @Override
                    public void onPostsFetched(List<Post> posts) {
                        runOnUiThread(() -> {
                            postsContainer.removeAllViews();
                            for (Post post : posts) {
                                View postView = LayoutInflater.from(SearchUserProfileLayoutActivity.this).inflate(R.layout.post, postsContainer, false);

                                ShapeableImageView postProfileImg = postView.findViewById(R.id.userImg);
                                TextView postname = postView.findViewById(R.id.name);
                                TextView postCreatedAt = postView.findViewById(R.id.time);
                                TextView postText = postView.findViewById(R.id.postMessage);
                                ShapeableImageView postImage = postView.findViewById(R.id.postimg);

                                postname.setText(post.getUser_name());
                                String timeAgo = PostAdapter.getTimeAgo(post.getCreated_at());
                                postCreatedAt.setText(timeAgo);
                                postText.setText(post.getContent());

                                Glide.with(SearchUserProfileLayoutActivity.this)
                                        .load(post.getProfile_image_url())
                                        .into(postProfileImg);

                                if (post.getImage_url() != null && !post.getImage_url().isEmpty()) {
                                    postImage.setVisibility(View.VISIBLE);
                                    Glide.with(SearchUserProfileLayoutActivity.this)
                                            .load(post.getImage_url())
                                            .into(postImage);
                                } else {
                                    postImage.setVisibility(View.GONE);
                                }

                                postsContainer.addView(postView);
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), "Failed to load posts: " + error, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void sendFriendRequest(String friendUserId) {
        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String currentUserId = prefs.getString("user_id", null);

        if (currentUserId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        SupabaseManager.checkAndRefreshToken(this, new SupabaseManager.TokenCheckCallback() {
            @Override
            public void onTokenReady(String accessToken) {
                SupabaseManager.sendFriendRequest(currentUserId, friendUserId, accessToken, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), "Failed to send friend request", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(), "Friend request sent!", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(), "Failed to send friend request: " + response.code(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show());
            }
        });
    }

}