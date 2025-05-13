package com.example.deportes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Batminton extends AppCompatActivity {

    private String[] videoUrls = {
            "",
            "",
            "",
            "",
            ""
    };

    private String[] videoTexts = {
            "",
            "",
            "",
            "",
            ""
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_batminton);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void playVideo(int index)
    {
        if(index >= 0 && index < videoUrls.length)
        {
            Intent intent = new Intent(Batminton.this, Video.class);
            Uri videoUri = Uri.parse(videoUrls[index]);
            intent.putExtra("videoUri", videoUri.toString());
            intent.putExtra("videoText", videoTexts[index]);
            startActivity(intent);
        }
    }
}