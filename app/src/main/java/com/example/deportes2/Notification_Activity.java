package com.example.deportes2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class Notification_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FriendRequestAdapter adapter;
    private List<FriendRequest> friendRequests = new ArrayList<>();
    private String currentUserId;
    private String senderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        recyclerView = findViewById(R.id.recyclerFriendRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FriendRequestAdapter(friendRequests, this::handleAction);
        recyclerView.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        currentUserId = prefs.getString("user_id", null);
        Log.d("NotificationActivity", "Current user ID from SharedPreferences: " + currentUserId);

        if (currentUserId != null) {
            SupabaseManager.checkAndRefreshToken(this, new SupabaseManager.TokenCheckCallback() {
                @Override
                public void onTokenReady(String accessToken) {
                    SupabaseManager.fetchIncomingFriendRequests(currentUserId, accessToken, requests -> {
                        runOnUiThread(() -> {
                            Log.d("NotificationActivity", "Requests fetched: " + requests.size());
                            friendRequests.clear();
                            friendRequests.addAll(requests);
                            adapter.notifyDataSetChanged();
                        });
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(Notification_Activity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void handleAction(String requestId, String action) {
        SupabaseManager.checkAndRefreshToken(this, new SupabaseManager.TokenCheckCallback() {
            @Override
            public void onTokenReady(String accessToken) {
                SupabaseManager.updateFriendRequestStatus(requestId, action, accessToken, success -> {
                    runOnUiThread(() -> {
                        if (success) {
                            if (action.equals("accept")) {
                                // Find the sender_id from the request
                                FriendRequest request = findRequestById(requestId);
                                if (request != null) {
                                    senderId = request.getSenderId();
                                    Log.e("Sender_id_check", "Sender ID: " + senderId);

                                    Log.d("DEBUG_FLOW", "Sender ID from request: " + request.getSenderId());
                                    SupabaseManager.addToFriendsTable(currentUserId, senderId, accessToken, new SupabaseManager.SupabaseCallback() {
                                        @Override
                                        public void onSuccess() {
                                            Log.d("FRIEND_ADD", "First row (user -> friend) added");
                                            // Add reverse relationship
                                            SupabaseManager.addToFriendsTable(senderId, currentUserId, accessToken, new SupabaseManager.SupabaseCallback() {
                                                @Override
                                                public void onSuccess() {
                                                    Log.d("FRIEND_ADD", "Second row (friend -> user) added");
                                                    Toast.makeText(getApplicationContext(), "Friendship established!", Toast.LENGTH_SHORT).show();
                                                    recreate(); // refresh list
                                                }

                                                @Override
                                                public void onFailure(String error) {
                                                    Log.d("FRIEND_ADD", "Failed to add reverse friend row: " + error);
                                                    Toast.makeText(getApplicationContext(), "Friend added, but failed to add reverse entry", Toast.LENGTH_SHORT).show();
                                                    recreate(); // still refresh list
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(String error) {
                                            Toast.makeText(getApplicationContext(), "Error adding friend: " + error, Toast.LENGTH_SHORT).show();
                                            Log.d("Friend table error", error);
                                        }
                                    });
                                }else {
                                    Log.d("DEBUG_REQUEST", "Found request: " + (request != null ? "YES" : "NO"));
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Request " + action + "ed", Toast.LENGTH_SHORT).show();
                                recreate(); // Refresh the list after action
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to " + action + " request", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(Notification_Activity.this, errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private FriendRequest findRequestById(String requestId) {
        for (FriendRequest request : friendRequests) {
            Log.d("DEBUG_REQUEST", "Checking requestId: " + request.getSenderId());
            if (request.getSenderId().equals(requestId)) {
                return request;
            }
        }
        return null;
    }

}
