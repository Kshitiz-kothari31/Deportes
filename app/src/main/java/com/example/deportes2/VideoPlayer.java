package com.example.deportes2;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.ui.PlayerView;

import java.util.ArrayList;

public class VideoPlayer extends AppCompatActivity {

    private VideoView videoView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_player);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        videoView = findViewById(R.id.videoView);
//        TextView textView = findViewById(R.id.descriptionText);
//        String videoText = getIntent().getStringExtra("videoText");
        String videoUrl = getIntent().getStringExtra("actionName");

//        if( videoText != null ){
//            textView.setText(videoText);
//        }

        if (videoUrl != null && !videoUrl.isEmpty()) {
            Uri videoUri = Uri.parse(videoUrl);

            // Set MediaController for play/pause/seek controls
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(videoUri);
            videoView.requestFocus();

            videoView.setOnPreparedListener(mp -> videoView.start());
        } else {
            // Handle error: No video URL passed
            finish(); // or show a message
        }
    }

    private void playVideo(String url){
        videoView.setVideoURI(Uri.parse(url));
        videoView.setOnPreparedListener(mp -> mp.start());
        videoView.setOnCompletionListener(mp -> Log.d("VideoPlayer", "Playback completed"));
        videoView.setOnErrorListener((mp, what, extra) -> {
            Log.e("VideoPlayer", "Error playing video: what=" + what + ", extra=" + extra);
            return true; // error handled
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.suspend();
        }
    }
}