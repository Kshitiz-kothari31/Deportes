package com.example.deportes2;

import static com.example.deportes2.SupabaseManager.refreshAccessToken;

import android.app.Activity;
import android.content.Context;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public Fragment basketballVideosFragment = new fragment_Basketball_videos();
    public Fragment footballVideosFragment = new football_videos();
    public Fragment volleyballVideosFragment=new vollyball_videos();
    public Fragment tabletenisVideosFragment =new tabletennies_videos();
    public Fragment batmintonVideosFragment=new batminton_videos();
    public  Fragment swimmingVideosFragment=new swimming_videos();

    Fragment profileFragment = new FullscreenProfileFragment();
//    Fragment chatFragment = new
    Fragment homeFragment = new HomeCommunity();
    Fragment sportsFragment = new Sports();
    Fragment activeFragment;
    ExtendedFloatingActionButton aiBtn, addpost;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView drawerName;
    ImageView drawerPhoto, searchbtn, notificationIcon;

    private String userId;
    private String accessToken;
    SharedPreferences prefs;

    public static List<String> videoPublicIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        notificationIcon = findViewById(R.id.toolbar_notification);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        SearchView searchView = findViewById(R.id.searchbo);
//        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
//        searchEditText.setPadding(0, 0, 0, 0);
//        searchEditText.setBackground(null);
//drawer photo and username
        getSupportFragmentManager().setFragmentResultListener("profileUpdated", this, (key, bundle) -> {
            prefs = getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
            userId = prefs.getString("user_id", null);
            accessToken = prefs.getString("access_token", null);

            if (userId != null && accessToken != null) {
                SupabaseManager.getProfile(userId, accessToken, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseBody = response.body().string();
                            try {
                                JSONArray jsonArray = new JSONArray(responseBody);
                                if (jsonArray.length() > 0) {
                                    JSONObject profile = jsonArray.getJSONObject(0);
                                    String name = profile.optString("name", "");
                                    String profileImageUrl = profile.optString("profile_img", "error");

                                    runOnUiThread(() -> updateDrawerProfile(name, profileImageUrl));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("Supabase", "Failed to get profile: " + response.code());
                        }
                    }
                });
            } else {
                Log.e("Supabase", "Missing access token or user ID in SharedPreferences");
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        searchbtn = findViewById(R.id.searchIcon);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Search.class);
                startActivity(intent);
            }
        });

        ImageView toolbarIcon = findViewById(R.id.toolbar_icon);
        toolbarIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_profile) {
                switchFragments(profileFragment);
            } else if (id == R.id.nav_home) {
                switchFragments(homeFragment);
            } else if (id == R.id.nav_sport) {
                switchFragments(sportsFragment);
            }else if (id == R.id.nav_about_us) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            } else if (id == R.id.nav_logout) {
                logoutUser();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;

        });



        notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Notification_Activity.class);
                startActivity(intent);
            }
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
                .add(R.id.main_content, volleyballVideosFragment, "volleyballVideos")
                .hide(volleyballVideosFragment)
                .commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, batmintonVideosFragment, "batmintonVideos")
                .hide(batmintonVideosFragment)
                .commit();


        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, swimmingVideosFragment, "swimmingVideos")
                .hide(swimmingVideosFragment)
                .commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, tabletenisVideosFragment, "tabletenisVideos")
                .hide(tabletenisVideosFragment)
                .commit();

        getSupportFragmentManager().beginTransaction().add(R.id.main_content, homeFragment, "Home").commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_content, profileFragment, "Profile").hide(profileFragment).commit();

        activeFragment = homeFragment;

        homeIcon.setOnClickListener(v -> switchFragments(homeFragment));
        sportsIcon.setOnClickListener(v -> switchFragments(sportsFragment));
        profileIcon.setOnClickListener(v -> switchFragments(profileFragment));

        aiBtn = findViewById(R.id.aiBtn);
        aiBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ai_chat.class)));

        addpost = findViewById(R.id.addPost);
        addpost.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WritePost.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        });
    }

    private void logoutUser() {
        SharedPreferences.Editor editor = getSharedPreferences("AuthPrefs", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(MainActivity.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String savedName = prefs.getString("name", null);
        String savedProfileUrl = prefs.getString("profile_url", null);
        updateDrawerProfile(savedName, savedProfileUrl);
    }

    public void updateDrawerProfile(String name, String photoUrl) {
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        drawerName = headerView.findViewById(R.id.username);
        drawerPhoto = headerView.findViewById(R.id.user_photo);

        if (name != null) {
            drawerName.setText("Hi, " + name + "!");
        }

        if (photoUrl != null && !photoUrl.equals("error") && !photoUrl.isEmpty()) {
            Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.drawable.user_avatar)
                    .into(drawerPhoto);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            String selectedSport = data.getStringExtra("selectedSport");
            Log.d("MainActivity", "Selected Sport: " + selectedSport);

            if ("Basketball".equalsIgnoreCase(selectedSport)) {
                switchFragments(basketballVideosFragment);
            } else if ("Football".equalsIgnoreCase(selectedSport)) {
                switchFragments(footballVideosFragment);
            }else if ("table tennis".equalsIgnoreCase(selectedSport)) {
                switchFragments(tabletenisVideosFragment);
            }else if ("badminton".equalsIgnoreCase(selectedSport)) {
                switchFragments(batmintonVideosFragment);
            }else if ("volleyball".equalsIgnoreCase(selectedSport)) {
                switchFragments(volleyballVideosFragment);
            }else if ("swimming".equalsIgnoreCase(selectedSport)) {
                switchFragments(swimmingVideosFragment);
            }
        }
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
        }else if (activeFragment == volleyballVideosFragment) {
            switchFragments(sportsFragment);
        }else if (activeFragment == tabletenisVideosFragment) {
            switchFragments(sportsFragment);
        } else if (activeFragment == swimmingVideosFragment) {
            switchFragments(sportsFragment);
        } else if (activeFragment == batmintonVideosFragment) {
            switchFragments(sportsFragment);
        } else if(activeFragment != homeFragment){
            switchFragments(homeFragment);
        }else{
            super.onBackPressed();
        }
    }

    public void fetchVideosFromBackend(String sportName, Sports.OnVideosLoadedListener callback) {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://supabase-server-production.up.railway.app/getVideos?sport=" + sportName; // Replace with your actual backend URL

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
                        if (callback != null) {
                            callback.onVideosLoaded(new ArrayList<>(videoPublicIds));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Volley", "Error parsing JSON");
                        if (callback != null) {
                            callback.onVideosLoaded(new ArrayList<>()); // Empty list on error
                        }
                    }
                },
                error -> {
                    Log.e("Volley", "Error fetching videos: " + error.getMessage());
                    if (callback != null) {
                        callback.onVideosLoaded(new ArrayList<>()); // Empty list on error
                    }
                });

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
