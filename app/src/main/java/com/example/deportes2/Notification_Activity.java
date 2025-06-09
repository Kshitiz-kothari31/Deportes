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
                // Use "accepted" or "rejected" as status values, normalize action string
                String status = action.equalsIgnoreCase("accept") ? "accepted" : action.equalsIgnoreCase("reject") ? "rejected" : action;

                SupabaseManager.updateFriendRequestStatus(requestId, status, accessToken, success -> {
                    runOnUiThread(() -> {
                        if (success) {
                            if (status.equals("accepted")) {
                                FriendRequest request = findRequestById(requestId);
                                if (request != null) {
                                    String senderId = request.getSenderId();

                                    SupabaseManager.addToFriendsTable(currentUserId, senderId, accessToken, new SupabaseManager.SupabaseCallback() {
                                        @Override
                                        public void onSuccess() {
                                            SupabaseManager.addToFriendsTable(senderId, currentUserId, accessToken, new SupabaseManager.SupabaseCallback() {
                                                @Override
                                                public void onSuccess() {
                                                    Toast.makeText(getApplicationContext(), "Friendship added!", Toast.LENGTH_SHORT).show();
                                                    recreate(); // Refresh UI
                                                }

                                                @Override
                                                public void onFailure(String error) {
                                                    Log.e("FRIENDSHIP", "Error adding reverse: " + error);
                                                    Toast.makeText(getApplicationContext(), "Partial friendship created", Toast.LENGTH_SHORT).show();
                                                    recreate();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(String error) {
                                            Log.e("FRIENDSHIP", "Error adding friendship: " + error);
                                            Toast.makeText(getApplicationContext(), "Failed to add friend", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(getApplicationContext(), "Friend request not found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // For rejected or other actions
                                Toast.makeText(getApplicationContext(), "Request " + status + ".", Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to update request", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(Notification_Activity.this, errorMessage, Toast.LENGTH_SHORT).show());
            }
        });
    }

    // Fix the findRequestById to actually compare by request ID, not senderId
    private FriendRequest findRequestById(String requestId) {
        for (FriendRequest request : friendRequests) {
            Log.d("DEBUG_REQUEST", "Checking requestId: " + request.getId());
            if (request.getId().equals(requestId)) {
                return request;
            }
        }
        return null;
    }


}
