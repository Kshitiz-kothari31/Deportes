package com.example.deportes;

import android.os.Bundle;
import android.widget.TextView;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.cldvideoplayer.CldVideoPlayer;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.ui.PlayerView;


public class Video extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MediaManager.init(this);

        TextView textView = findViewById(R.id.TextView);
        String videoText = getIntent().getStringExtra("videoText");
        String videoUriString = getIntent().getStringExtra("videoUri");

        if( videoText != null ){
            textView.setText(videoText);
        }

        if( videoUriString != null ){
           setVideoPlayer(videoUriString);
        }
    }

    private CldVideoPlayer player;

    private void setVideoPlayer(String videoUriString){
        player = new CldVideoPlayer(this,
                MediaManager.get().url()
                        .resourceType("video")
                        .transformation(new Transformation().quality("auto"))
                        .generate(videoUriString));

        PlayerView playerView = findViewById(R.id.playerView);
        playerView.setPlayer(player.getPlayer());

        player.getPlayer().play();
    }

    protected void onPause(){
        super.onPause();
        if(player != null) {
            player.getPlayer().pause();
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        if(player != null) {
            player.getPlayer().release();
            player = null;
        }
    }

}