package com.example.deportes2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cloudinary.Cloudinary;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.HashMap;
import java.util.Map;
public class MainActivity extends AppCompatActivity {

    public Fragment basketballVideosFragment = new fragment_Basketball_videos();
    public Fragment footballVideosFragment = new football_videos();

    Fragment profileFragment = new FullscreenProfileFragment();
//    Fragment chatFragment = new
    Fragment homeFragment = new HomeCommunity();
    Fragment sportsFragment = new Sports();
    Fragment activeFragment;
    ExtendedFloatingActionButton aiBtn, addpost;
    Toolbar topToolbar;


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
        }

        ImageView homeIcon = findViewById(R.id.bottom_home_icon);
        ImageView sportsIcon = findViewById(R.id.bottom_sports_icon);
        ImageView profileIcon = findViewById(R.id.bottom_profile_icon);
//        ImageView chatIcon = findViewById(R.id.bottom_chat_icon);
        topToolbar = findViewById(R.id.top_toolbar);

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
            topToolbar.setVisibility(View.VISIBLE);
        });
        sportsIcon.setOnClickListener(v -> {
            switchFragments(sportsFragment);
            topToolbar.setVisibility(View.VISIBLE);
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

}

class CloudinaryManager {
    private static Cloudinary cloudinary;

    public static void initCloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "doxwvdotv");
        config.put("api_key", "773683899351999");
        config.put("api_secret", "yhSAqhL7TdqP5koICIVN7goLj1Q");
        cloudinary = new Cloudinary(config);
        System.out.println("Cloudinary initialized successfully!");
    }

    public static Cloudinary getCloudinary() {
        if(cloudinary == null){
            throw new IllegalStateException("Cloudinary is not initialized. Call initCloudinary() first.");
        }
        return cloudinary;
    }
}