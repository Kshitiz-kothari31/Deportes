package com.example.deportes2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class swimming_videos extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swimming_videos, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton s_breathing, s_floating,  s_kicking, s_Freestyle, s_Breaststorke, s_butterfly;

        s_breathing = view.findViewById(R.id.swimming_breathing);
        s_breathing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = Sports.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("Breathing.mp4", videos);
            }
        });

        s_floating = getView().findViewById(R.id.swimming_floating);
        s_floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = Sports.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("dribbling.mp4", videos);
            }
        });

        s_kicking= getView().findViewById(R.id.swimming_kicking);
        s_kicking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = Sports.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("shooting.mp4", videos);
            }
        });

        s_Freestyle = getView().findViewById(R.id.swimming_freestyle);
        s_Freestyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = Sports.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("Freestyle.mp4", videos);
            }
        });

        s_Breaststorke = getView().findViewById(R.id.swimming_breast_storke);
        s_Breaststorke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = Sports.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("Breaststroke.mp4", videos);
            }
        });

        s_butterfly = getView().findViewById(R.id.swimming_bettrfly);
        s_butterfly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = Sports.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("Butterfly.mp4", videos);
            }
        });
    }

    private void playVideoWithName(String videoName, List<String> videoUrls){
        for(String url : videoUrls){
            if(url.contains(videoName)){
                Intent intent = new Intent(requireContext(), VideoPlayer.class);
                intent.putExtra("actionName", url);
                startActivity(intent);
                return;
            }
        }

        Toast.makeText(requireContext(), "Video not found: " + videoName, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().hide();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if(activity.getSupportActionBar() != null){
                activity.getSupportActionBar().show();
            }
        }
    }
}