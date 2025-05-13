package com.example.deportes;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.android.MediaManager;
import com.google.android.material.imageview.ShapeableImageView;

public class MainActivity extends AppCompatActivity {

    ShapeableImageView football, basketball, tabletenis, volleyball, swimming, batminton;

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

//        SharedPreferences preferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
//        boolean isFirstRun = preferences.getBoolean("isFirstRun", true);

//        if( isFirstRun ) {
//            Intent intent = new Intent(this, StartActivity.class);
//            startActivity(intent);
//
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean("isFirstRun", false);
//            editor.apply();
//            finish();
//        } else {
//            setContentView(R.layout.activity_main);
//        }

        if (!isNetworkAvailable()) {
            showInternetDialog();
        }

        setContentView(R.layout.activity_main);

        football = findViewById(R.id.footballimage);
        basketball = findViewById(R.id.basketballimage);
        tabletenis = findViewById(R.id.tabletenisimage);
        volleyball = findViewById(R.id.volleyballimage);
        swimming = findViewById(R.id.swimmingimage);
        batminton = findViewById(R.id.batmintonimage);

        football.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Football.class);
                startActivity(intent);
            }
        });

        basketball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Basketball.class);
                startActivity(intent);
            }
        });

        tabletenis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TableTenis.class);
                startActivity(intent);
            }
        });

        volleyball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Volleyball.class);
                startActivity(intent);
            }
        });

        swimming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Swimming.class);
                startActivity(intent);
            }
        });

        batminton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Batminton.class);
                startActivity(intent);
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Please enable your internet connection to use this app.");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
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
    }

    public static Cloudinary getCloudinary() {
        return cloudinary;
    }
}