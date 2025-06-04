package com.example.deportes2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public Fragment basketballVideosFragment = new fragment_Basketball_videos();
    public Fragment footballVideosFragment = new football_videos();

    Fragment profileFragment = new FullscreenProfileFragment();
//    Fragment chatFragment = new
    Fragment homeFragment = new HomeCommunity();
    Fragment sportsFragment = new Sports();
    Fragment activeFragment;
    ExtendedFloatingActionButton aiBtn, addpost;

    public static List<String> videoPublicIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String token = prefs.getString("access_token", null);

        if(token == null){
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }else{
            checkAndRefreshToken();
        }

        ImageView homeIcon = findViewById(R.id.bottom_home_icon);
        ImageView sportsIcon = findViewById(R.id.bottom_sports_icon);
        ImageView profileIcon = findViewById(R.id.bottom_profile_icon);
//        ImageView chatIcon = findViewById(R.id.bottom_chat_icon);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, sportsFragment, "Sports")
                .hide(sportsFragment)
                .commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, footballVideosFragment, "FootballVideos")
                .hide(footballVideosFragment)
                .commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, basketballVideosFragment, "BasketballVideos")
                .hide(basketballVideosFragment)
                .commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, homeFragment, "Home")
                .commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, profileFragment, "Profile")
                .hide(profileFragment)
                .commit();

        activeFragment = homeFragment;

        homeIcon.setOnClickListener(v -> {
            switchFragments(homeFragment);
        });
        sportsIcon.setOnClickListener(v -> {
            switchFragments(sportsFragment);
        });

        profileIcon.setOnClickListener(v -> {
            switchFragments(profileFragment);
        });

        aiBtn = findViewById(R.id.aiBtn);
        aiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ai_chat.class);
                startActivity(intent);
            }
        });

        addpost = findViewById(R.id.addPost);
        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WritePost.class);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
            }
        });

    }

    public void switchFragments(Fragment targetFragment){
        if(activeFragment != targetFragment){
            Log.d("FragmentSwitch", "Switching to: " + targetFragment.getClass().getSimpleName());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(activeFragment);
            transaction.show(targetFragment);
            transaction.commit();
            activeFragment = targetFragment;
        }
    }

    public void showFootballFragmentVideos(){
        switchFragments(footballVideosFragment);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();

        if(activeFragment == footballVideosFragment){
            switchFragments(sportsFragment);
        } else if (activeFragment == basketballVideosFragment) {
            switchFragments(sportsFragment);
        } else if(activeFragment != homeFragment){
            switchFragments(homeFragment);
        }else{
            super.onBackPressed();
        }
    }

    public void fetchVideosFromBackend(String sportName) {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://supabase-server-la9g.onrender.com/getVideos?sport=" + sportName; // Replace with your actual backend URL

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray videoArray = response.getJSONArray("videos");
                        videoPublicIds.clear();

                        for (int i = 0; i < videoArray.length(); i++) {
                            String videourl = videoArray.getString(i);
                            videoPublicIds.add(videourl); // Store full URLs
                        }

                        Log.d("Debug", "Fetched Video IDs: " + videoPublicIds);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Volley", "Error parsing JSON");
                    }
                },
                error -> Log.e("Volley", "Error fetching videos: " + error.getMessage()));

        queue.add(request);
    }

    private void checkAndRefreshToken() {
        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        long expiresAt = prefs.getLong("expires_at", 0);
        long currentTime = System.currentTimeMillis() / 1000;

        if (currentTime >= expiresAt) {
            Log.d("Auth", "Access token expired. Refreshing...");
            SupabaseManager.refreshAccessToken(this, new SupabaseManager.RefreshTokenCallback() {
                @Override
                public void onSuccess() {
                    Log.d("Auth", "Token refreshed successfully.");
                }

                @Override
                public void onFailure() {
                    Log.e("Auth", "Failed to refresh token. Redirecting to login.");
                    runOnUiThread(() -> {
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                        finish();
                    });
                }
            });
        } else {
            Log.d("Auth", "Access token still valid.");
        }
    }

}