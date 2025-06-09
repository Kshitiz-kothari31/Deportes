package com.example.deportes2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FriendAdapter adapter;
    private List<UserProfile> friendList = new ArrayList<>();

    ImageView backButton;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.friends_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.friendslist), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backButton = findViewById(R.id.listActivityBackBtn);
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        recyclerView = findViewById(R.id.recyclerViewFriends);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendAdapter(friendList, this, friend -> {
            Intent intent = new Intent(FriendsListActivity.this, ChatActivity.class);
            intent.putExtra("friend_id", friend.getId());
            intent.putExtra("friend_name", friend.getName());
            intent.putExtra("friend_profile_url", friend.getProfileImageUrl());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        SupabaseManager.checkAndRefreshToken(this, new SupabaseManager.TokenCheckCallback() {
            @Override
            public void onTokenReady(String accessToken) {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                SupabaseManager.getFriendsList(accessToken, new SupabaseManager.FriendsCallback() {
                    @Override
                    public void onFriendsFetched(List<UserProfile> friends) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(ProgressBar.GONE);
                            friendList.clear();
                            friendList.addAll(friends);
                            adapter.notifyDataSetChanged();
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(ProgressBar.GONE);
                            Toast.makeText(FriendsListActivity.this, error, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(FriendsListActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
