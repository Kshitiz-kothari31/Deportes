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
import androidx.appcompat.widget.AppCompatButton;
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
    private AppCompatButton addFriendBtn, unfriendBtn, requestSentBtn;

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
        unfriendBtn = findViewById(R.id.unFriendBtn);
        requestSentBtn = findViewById(R.id.requestSentBtn);

        searchUserId = getIntent().getStringExtra("FRIEND_USER_ID");

        if (searchUserId != null) {
            fetchUserProfile(searchUserId);
            fetchUserPosts(searchUserId);

            updateFriendButtonsState();
        }else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String currentUserId = prefs.getString("user_id", null);
        String accessToken = prefs.getString("access_token", null); // from intent

        addFriendBtn.setOnClickListener(view -> {
            if (currentUserId == null || accessToken == null) {
                Toast.makeText(this, "You must be logged in to send friend requests", Toast.LENGTH_SHORT).show();
                return;
            }
            SupabaseManager.checkFriendRequestExists(currentUserId, searchUserId, accessToken, new SupabaseManager.FriendRequestExistCallback() {
                @Override
                public void onResult(boolean exists, String error) {
                    runOnUiThread(() -> {
                        addFriendBtn.setEnabled(true);
                        if (error != null) {
                            Toast.makeText(SearchUserProfileLayoutActivity.this, "Error checking request: " + error, Toast.LENGTH_SHORT).show();
                        } else if (exists) {
                            Toast.makeText(SearchUserProfileLayoutActivity.this, "Friend request already exists!", Toast.LENGTH_SHORT).show();
                            addFriendBtn.setVisibility(View.GONE);
                            requestSentBtn.setVisibility(View.VISIBLE);
                            unfriendBtn.setVisibility(View.GONE);
                        } else {
                            sendFriendRequest(searchUserId);
                        }
                    });
                }
            });
        });

        unfriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unfriendUser();
            }
        });

        requestSentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelFriendRequest();
            }
        });
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
        if (friendUserId == null || friendUserId.isEmpty()) {
            Toast.makeText(this, "Invalid user to send request", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String currentUserId = prefs.getString("user_id", null);

        if (currentUserId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        SupabaseManager.checkAndRefreshToken(this, new SupabaseManager.TokenCheckCallback() {
            @Override
            public void onTokenReady(String accessToken) {
                // Step 1: Delete any rejected request
                SupabaseManager.deleteRejectedFriendRequest(currentUserId, friendUserId, accessToken, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // Even if delete fails, try to send new request anyway
                        sendNewRequest(currentUserId, friendUserId, accessToken);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // After deleting, send the new request
                        sendNewRequest(currentUserId, friendUserId, accessToken);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(SearchUserProfileLayoutActivity.this, errorMessage, Toast.LENGTH_SHORT).show());
            }
        });
    }

    // This method sends the actual friend request
    private void sendNewRequest(String currentUserId, String friendUserId, String accessToken) {
        SupabaseManager.sendFriendRequest(currentUserId, friendUserId, accessToken, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();  // Log for debugging
                runOnUiThread(() -> {
                    Toast.makeText(SearchUserProfileLayoutActivity.this, "Failed to send friend request", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(SearchUserProfileLayoutActivity.this, "Friend request sent!", Toast.LENGTH_SHORT).show();
                        requestSentBtn.setVisibility(View.VISIBLE);
                        addFriendBtn.setVisibility(View.GONE);
                        unfriendBtn.setVisibility(View.GONE);
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(SearchUserProfileLayoutActivity.this, "Failed to send friend request: " + response.code(), Toast.LENGTH_SHORT).show();
                        addFriendBtn.setVisibility(View.VISIBLE);
                        requestSentBtn.setVisibility(View.GONE);
                        unfriendBtn.setVisibility(View.GONE);
                    });
                }
            }
        });
    }


    private void unfriendUser() {
        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String currentUserId = prefs.getString("user_id", null);
        String accessToken = prefs.getString("access_token", null);

        if (currentUserId == null || accessToken == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        SupabaseManager.checkAndRefreshToken(this, new SupabaseManager.TokenCheckCallback() {
            @Override
            public void onTokenReady(String newAccessToken) {
                SupabaseManager.unfriendUser(currentUserId, searchUserId, newAccessToken, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(() -> Toast.makeText(SearchUserProfileLayoutActivity.this, "Failed to unfriend user", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> {
                                Toast.makeText(SearchUserProfileLayoutActivity.this, "Unfriended successfully", Toast.LENGTH_SHORT).show();
                                // Update UI buttons visibility accordingly
                                unfriendBtn.setVisibility(View.GONE);
                                addFriendBtn.setVisibility(View.VISIBLE);
                                requestSentBtn.setVisibility(View.GONE);
                            });
                        } else {
                            runOnUiThread(() -> Toast.makeText(SearchUserProfileLayoutActivity.this, "Failed to unfriend: " + response.code(), Toast.LENGTH_SHORT).show());
                        }
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(SearchUserProfileLayoutActivity.this, errorMessage, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void cancelFriendRequest() {
        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String currentUserId = prefs.getString("user_id", null);
        String accessToken = prefs.getString("access_token", null);

        if (currentUserId == null || accessToken == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        SupabaseManager.checkAndRefreshToken(this, new SupabaseManager.TokenCheckCallback() {
            @Override
            public void onTokenReady(String newAccessToken) {
                SupabaseManager.cancelFriendRequest(currentUserId, searchUserId, newAccessToken, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(() -> Toast.makeText(SearchUserProfileLayoutActivity.this, "Failed to cancel friend request", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            runOnUiThread(() -> {
                                Toast.makeText(SearchUserProfileLayoutActivity.this, "Friend request canceled", Toast.LENGTH_SHORT).show();
                                // Update UI buttons visibility accordingly
                                requestSentBtn.setVisibility(View.GONE);
                                addFriendBtn.setVisibility(View.VISIBLE);
                                unfriendBtn.setVisibility(View.GONE);
                            });
                        } else {
                            runOnUiThread(() -> Toast.makeText(SearchUserProfileLayoutActivity.this, "Failed to cancel: " + response.code(), Toast.LENGTH_SHORT).show());
                        }
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(SearchUserProfileLayoutActivity.this, errorMessage, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void updateFriendButtonsState() {
        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String currentUserId = prefs.getString("user_id", null);
        String accessToken = prefs.getString("access_token", null);

        if (currentUserId == null || accessToken == null) {
            addFriendBtn.setVisibility(View.GONE);
            unfriendBtn.setVisibility(View.GONE);
            requestSentBtn.setVisibility(View.GONE);
            return;
        }

        SupabaseManager.checkAndRefreshToken(this, new SupabaseManager.TokenCheckCallback() {
            @Override
            public void onTokenReady(String newAccessToken) {
                SupabaseManager.checkFriendRequestStatus(currentUserId, searchUserId, newAccessToken, (status, error) -> {
                    runOnUiThread(() -> {
                        if (error != null) {
                            Toast.makeText(SearchUserProfileLayoutActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                            addFriendBtn.setVisibility(View.VISIBLE);
                            requestSentBtn.setVisibility(View.GONE);
                            unfriendBtn.setVisibility(View.GONE);
                            return;
                        }

                        if (status == null) {
                            addFriendBtn.setVisibility(View.VISIBLE);
                            requestSentBtn.setVisibility(View.GONE);
                            unfriendBtn.setVisibility(View.GONE);
                        } else if (status.equals("pending")) {
                            // Request sent, waiting for response
                            addFriendBtn.setVisibility(View.GONE);
                            requestSentBtn.setVisibility(View.VISIBLE);
                            unfriendBtn.setVisibility(View.GONE);
                        } else if (status.equals("rejected")) {
                            // Request was rejected
                            addFriendBtn.setVisibility(View.VISIBLE);
                            requestSentBtn.setVisibility(View.GONE);
                            unfriendBtn.setVisibility(View.GONE);
                        } else if (status.equals("accepted")) {
                            // Request accepted, show Unfriend
                            addFriendBtn.setVisibility(View.GONE);
                            requestSentBtn.setVisibility(View.GONE);
                            unfriendBtn.setVisibility(View.VISIBLE);
                        }
                    });
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(SearchUserProfileLayoutActivity.this, errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

}